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
import a04.tugasakhirfarmasi.model.PartisipanSidangModel;
import a04.tugasakhirfarmasi.model.SidangModel;

public class SidangXLSExporter {
    private List<SidangModel> listSidang;
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Sidang");
     
    public SidangXLSExporter(List<SidangModel> listSidang) {
        this.listSidang = listSidang;
    }
 
 
    private void writeHeaderLine() {
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "No", style);      
        createCell(row, 1, "Jenis Sidang", style);       
        createCell(row, 2, "Mahasiswa", style);    
        createCell(row, 3, "Dosen", style);
        createCell(row, 4, "Tanggal", style);
        createCell(row, 5, "Waktu Mulai", style);
        createCell(row, 6, "Waktu Selesai", style);
        createCell(row, 7, "Ruangan", style);
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
                 
        for (SidangModel sidang:listSidang) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            String partisipanSidang = "";
            for (PartisipanSidangModel partisipan:sidang.getListPartisipanSidang()) {
                partisipanSidang += partisipan.getPartisipanSidangDosen().getNama();
                partisipanSidang += ", ";
            }
             
            createCell(row, columnCount++, String.valueOf(rowCount - 1), style);
            createCell(row, columnCount++, sidang.getJenisSidang().getNama(), style);
            createCell(row, columnCount++, sidang.getMahasiswaSidang().getPenggunaMahasiswa().getUsername(), style);
            createCell(row, columnCount++, partisipanSidang.substring(0, partisipanSidang.length() - 2), style);
            createCell(row, columnCount++, sidang.getTanggalToString(), style);
            createCell(row, columnCount++, sidang.getWaktuMulai().toString(), style);
            createCell(row, columnCount++, sidang.getWaktuSelesai().toString(), style);
            createCell(row, columnCount++, sidang.getRuangan(), style);
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
