package a04.tugasakhirfarmasi.bean;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import a04.tugasakhirfarmasi.model.LamaranModel;
import a04.tugasakhirfarmasi.model.PartisipanSidangModel;
import a04.tugasakhirfarmasi.model.SidangModel;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;


public class SidangPDFExporter {
    private List<SidangModel> listSidang;

    public SidangPDFExporter(List<SidangModel> listSidang) {
        this.listSidang = listSidang;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(6);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        font.setSize(9.5f);

        cell.setPhrase(new Phrase("No", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Jenis Sidang", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Mahasiswa", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Dosen", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tanggal", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Waktu Mulai", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Waktu Selesai", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        int iter = 1;

        PdfPCell cell = new PdfPCell();

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);
        font.setSize(9.5f);

        for (SidangModel sidang : listSidang) {
            String partisipanSidang = "";
            for (PartisipanSidangModel partisipan:sidang.getListPartisipanSidang()) {
                partisipanSidang += "- ";
                partisipanSidang += partisipan.getPartisipanSidangDosen().getNama();
                partisipanSidang += "\n";
            }

            cell.setPhrase(new Phrase(String.valueOf(iter), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(sidang.getJenisSidang().getNama(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(sidang.getMahasiswaSidang().getPenggunaMahasiswa().getUsername(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(partisipanSidang, font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(sidang.getTanggal().toString().substring(0,11), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(sidang.getWaktuMulai().toString(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(sidang.getWaktuSelesai().toString(), font));
            table.addCell(cell);

            iter++;
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setSize(10);
        font.setColor(Color.BLACK);

        Paragraph p = new Paragraph("Daftar Sidang Mahasiswa", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.0f, 4.0f, 5.0f, 5.0f, 2.0f, 1.5f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);

        p = new Paragraph("Jumlah sidang adalah sebanyak: ");
        p.add(String.valueOf(listSidang.size()));
        document.add(p);

        document.close();
    }
}