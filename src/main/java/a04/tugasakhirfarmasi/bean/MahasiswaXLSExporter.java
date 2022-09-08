package a04.tugasakhirfarmasi.bean;

import java.io.IOException;
import java.util.List;
 
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import a04.tugasakhirfarmasi.model.LamaranModel;
import a04.tugasakhirfarmasi.model.MahasiswaModel;

public class MahasiswaXLSExporter {
    private List<MahasiswaModel> listMahasiswa;
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Mahasiswa");

    public MahasiswaXLSExporter(List<MahasiswaModel> listMahasiswa) {
        this.listMahasiswa = listMahasiswa;
    }

    private void writeHeaderLine() {
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "No", style);      
        createCell(row, 1, "Nama", style);       
        createCell(row, 2, "NPM", style);    
        createCell(row, 3, "Program Studi", style);
        createCell(row, 4, "Dosen Pembimbing", style);   
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (MahasiswaModel mahasiswa:listMahasiswa) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            String dosenPembimbingMahasiswa = "";
            for (LamaranModel lamaran:mahasiswa.getListLamaran()) {
                if (lamaran.getStatusLamaran().getNama().equals("Disetujui")) {
                    dosenPembimbingMahasiswa += lamaran.getLamaranDosen().getNama();
                    dosenPembimbingMahasiswa += ", ";
                }
            }

            if (dosenPembimbingMahasiswa.length() == 0) {
                dosenPembimbingMahasiswa = "Belum Ada Dosen Pembimbing  ";
            }

            createCell(row, columnCount++, String.valueOf(rowCount - 1), style);
            createCell(row, columnCount++, mahasiswa.getUsername(), style);
            createCell(row, columnCount++, mahasiswa.getNpm(), style);
            createCell(row, columnCount++, mahasiswa.getPenggunaMahasiswa().getProdiPengguna().getNama(), style);
            createCell(row, columnCount++, dosenPembimbingMahasiswa.substring(0, dosenPembimbingMahasiswa.length() - 2), style);
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}
