package a04.tugasakhirfarmasi.bean;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import a04.tugasakhirfarmasi.model.LamaranModel;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class LamaranPDFExporter {
    private List<LamaranModel> listLamaran;

    public LamaranPDFExporter(List<LamaranModel> listLamaran) {
        this.listLamaran = listLamaran;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(6);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("No", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Mahasiswa", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Dosen", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tanggal", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Waktu", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Status", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        int iter = 1;
        for (LamaranModel lamaran : listLamaran) {
            table.addCell(String.valueOf(iter));
            table.addCell(lamaran.getLamaranMahasiswa().getPenggunaMahasiswa().getUsername());
            table.addCell(lamaran.getLamaranDosen().getNama());
            table.addCell(lamaran.getTanggal());
            table.addCell(lamaran.getWaktu().toString());
            table.addCell(lamaran.getStatusLamaran().getNama());
            iter++;
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setSize(18);
        font.setColor(Color.BLACK);

        Paragraph p = new Paragraph("Daftar Lamaran Mahasiswa", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.0f, 4.0f, 4.0f, 2.5f, 2.0f, 4.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        p = new Paragraph("Jumlah lamaran adalah sebanyak: ");
        p.add(String.valueOf(listLamaran.size()));
        document.add(p);

        document.add(table);
        document.close();
    }
}