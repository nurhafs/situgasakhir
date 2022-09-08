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

public class LamaranXLSExporter {
    private List<LamaranModel> listLamaran;
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Lamaran");

    public LamaranXLSExporter(List<LamaranModel> listLamaran) {
        this.listLamaran = listLamaran;
    }

    private void writeHeaderLine() {
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "No", style);      
        createCell(row, 1, "Mahasiswa", style);       
        createCell(row, 2, "Dosen", style);    
        createCell(row, 3, "Tanggal", style);
        createCell(row, 4, "Waktu", style);
        createCell(row, 5, "Status", style);       
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
                 
        for (LamaranModel lamaran:listLamaran) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, String.valueOf(rowCount - 1), style);
            createCell(row, columnCount++, lamaran.getLamaranMahasiswa().getPenggunaMahasiswa().getUsername(), style);
            createCell(row, columnCount++, lamaran.getLamaranDosen().getNama(), style);
            createCell(row, columnCount++, lamaran.getTanggal(), style);
            createCell(row, columnCount++, lamaran.getWaktu().toString(), style);
            createCell(row, columnCount++, lamaran.getStatusLamaran().getNama(), style);
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
