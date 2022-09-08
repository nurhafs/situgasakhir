package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.*;
import a04.tugasakhirfarmasi.repository.PartisipanSidangDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Qualifier("partisipanSidangServiceImpl")
    @Autowired
    private PartisipanSidangService partisipanSidangService;

    @Autowired
    PartisipanSidangDB partisipanSidangDB;

    @Override
    public String getWaktu() {
        LocalTime waktuPagi = LocalTime.of(2, 0);
        LocalTime waktuSiang = LocalTime.of(11, 0);
        LocalTime waktuSore = LocalTime.of(15, 0);
        LocalTime waktuMalam = LocalTime.of(18, 0);

        LocalTime now = LocalTime.now();
        String waktu = "";

        if (now.isAfter(waktuPagi) && now.isBefore(waktuSiang)) {
            waktu = "pagi";
        } else if (now.isAfter(waktuSiang) && now.isBefore(waktuSore)) {
            waktu = "siang";
        } else if (now.isAfter(waktuSore) && now.isBefore(waktuMalam)) {
            waktu = "sore";
        } else if (now.isAfter(waktuMalam) || now.isBefore(waktuPagi)) {
            waktu = "malam";
        }
        return waktu;
    }

    public void sendMailSidang(SidangModel sidang) throws MessagingException, ParseException {
        String waktu = getWaktu();

        String mahasiswa = sidang.getMahasiswaSidang().getPenggunaMahasiswa().getUsername();;
        String jenisSidang = sidang.getJenisSidang().getNama();

        Format formatter = new SimpleDateFormat("EEEEEE, dd MMMMM yyyy", new Locale("id"));
        String tanggal = formatter.format(sidang.getTanggal());

        String waktuMulai = sidang.getWaktuMulai().toString();
        String waktuSelesai = sidang.getWaktuSelesai().toString();
        String ruang = sidang.getRuangan();
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        ArrayList<String> listEmailPartisipan = new ArrayList<>();

        List<PartisipanSidangModel> partisipans = partisipanSidangDB.findAll();
        for (PartisipanSidangModel partisipan : partisipans) {
            if (partisipan.getPartisipanSidang().getId() == sidang.getId()) {
                listEmailPartisipan.add(partisipan.getPartisipanSidangDosen().getPenggunaDosen().getUsername() + "@farmasi.ui.ac.id");
            }
        }
        listEmailPartisipan.add(sidang.getMahasiswaSidang().getPenggunaMahasiswa().getUsername() + "@ui.ac.id");

        String[] emailDosenPartisipan = listEmailPartisipan.toArray(new String[listEmailPartisipan.size()]);

        helper.setTo(emailDosenPartisipan);
        helper.setSubject("Penetapan Jadwal " + jenisSidang + " Untuk Mahasiswa " + mahasiswa);
        helper.setText("Selamat " + waktu + ",<br>" +
                "Berikut ini kami lampirkan tanggal dan waktu penetapan sidang yang akan Bapak/Ibu/Saudara/i hadiri:<br><br>" +
                "Jenis Sidang : " +   jenisSidang + "<br>" +
                "Mahasiswa : " + mahasiswa + "<br>" +
                "Tanggal : " + tanggal + "<br>" +
                "Waktu : " + waktuMulai + " - " + waktuSelesai + " WIB<br>" +
                "Ruang : " + ruang + " <br><br>" +
                "Demikian informasi ini kami sampaikan. Atas perhatiannya, kami ucapkan terima kasih.<br><br>" +
                "Salam,<br>" +
                "Admin", true
        );
        javaMailSender.send(msg);
    }

    @Override
    public void sendMailNonSidangByAdminProdi(JadwalNonSidangModel nonSidang) throws MessagingException {
        String waktu = getWaktu();

        String dosen = nonSidang.getDosen().getNama();
        String keperluan = nonSidang.getKeperluan().getNama();
        String deskripsi = nonSidang.getDeskripsi();

        Format formatter = new SimpleDateFormat("EEEEEE, dd MMMMM yyyy", new Locale("id"));
        String tanggalMulai = formatter.format(nonSidang.getTanggalMulai());
        String tanggalSelesai = formatter.format(nonSidang.getTanggalSelesai());

        String waktuMulai = nonSidang.getWaktuMulai().toString();
        String waktuSelesai = nonSidang.getWaktuSelesai().toString();
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        ArrayList<String> listEmailPartisipan = new ArrayList<>();

        listEmailPartisipan.add(nonSidang.getDosen().getPenggunaDosen().getUsername() + "@farmasi.ui.ac.id");

        String[] emailDosenPartisipan = listEmailPartisipan.toArray(new String[listEmailPartisipan.size()]);

        helper.setTo(emailDosenPartisipan);
        helper.setSubject("Penetapan Jadwal Kegiatan " + keperluan + " Untuk " + dosen);
        helper.setText("Selamat " + waktu + ",<br>" +
                "Berikut ini kami lampirkan tanggal dan waktu penetapan jadwal kegiatan yang akan Bapak/Ibu ikuti:<br><br>" +
                "Jenis Kegiatan : " +   keperluan + "<br>" +
                "Tanggal dan Waktu Mulai : " + tanggalMulai + " " + waktuMulai + " WIB<br>" +
                "Tanggal dan Waktu Selesai : " + tanggalSelesai + " " + waktuSelesai + " WIB<br>" +
                "Informasi Tambahan : " + deskripsi + "<br><br>" +
                "Demikian informasi ini kami sampaikan. Atas perhatiannya, kami ucapkan terima kasih.<br><br>" +
                "Salam,<br>" +
                "Admin", true
        );
        javaMailSender.send(msg);
    }

    @Override
    public void sendMailUbahStatusLamaran(LamaranModel lamaran) throws MessagingException {
        String waktu = getWaktu();

        String mahasiswa = lamaran.getLamaranMahasiswa().getPenggunaMahasiswa().getUsername();
        String dosen = lamaran.getLamaranDosen().getNama();
        String tanggal = lamaran.getTanggal();
        String waktuLamaran = lamaran.getWaktu().toString();
        String status = lamaran.getStatusLamaran().getNama().toUpperCase();

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        ArrayList<String> listEmailPartisipan = new ArrayList<>();
        listEmailPartisipan.add(lamaran.getLamaranMahasiswa().getPenggunaMahasiswa().getUsername() + "@ui.ac.id");

        String[] emailDosenPartisipan = listEmailPartisipan.toArray(new String[listEmailPartisipan.size()]);

        helper.setTo(emailDosenPartisipan);
        helper.setSubject("Perubahan Status Lamaran Dosen Pembimbing Untuk " + mahasiswa);
        helper.setText("Selamat " + waktu + ",<br>" +
                "<p>Berikut ini kami informasikan bahwa status lamaran dosen pembimbing untuk mahasiswa <strong>" + mahasiswa + "&nbsp;</strong>untuk dosen pembimbing <strong>" + dosen + "</strong> pada tanggal <strong>" + tanggal + " " + waktuLamaran + "</strong> telah berubah menjadi :</p><br>" +
                "<h3>" + status + "</h3><br>" +
                "Demikian informasi ini kami sampaikan. Atas perhatiannya, kami ucapkan terima kasih.<br><br>" +
                "Salam,<br>" +
                "Admin", true
        );
        javaMailSender.send(msg);
    }

    @Override
    public void sendMailUbahSidang(SidangModel sidang) throws MessagingException, ParseException {
        String waktu = getWaktu();

        String mahasiswa = sidang.getMahasiswaSidang().getPenggunaMahasiswa().getUsername();;
        String jenisSidang = sidang.getJenisSidang().getNama();

        Format formatter = new SimpleDateFormat("EEEEEE, dd MMMMM yyyy", new Locale("id"));
        String tanggal = formatter.format(sidang.getTanggal());

        String waktuMulai = sidang.getWaktuMulai().toString();
        String waktuSelesai = sidang.getWaktuSelesai().toString();
        String ruang = sidang.getRuangan();
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        ArrayList<String> listEmailPartisipan = new ArrayList<>();

        for (PartisipanSidangModel partisipan: sidang.getListPartisipanSidang()) {
            listEmailPartisipan.add(partisipan.getPartisipanSidangDosen().getPenggunaDosen().getUsername() + "@farmasi.ui.ac.id");
        }
        listEmailPartisipan.add(sidang.getMahasiswaSidang().getPenggunaMahasiswa().getUsername() + "@ui.ac.id");

        String[] emailDosenPartisipan = listEmailPartisipan.toArray(new String[listEmailPartisipan.size()]);

        helper.setTo(emailDosenPartisipan);
        helper.setSubject("Perubahan Jadwal " + jenisSidang + " Untuk Mahasiswa " + mahasiswa);
        helper.setText("Selamat " + waktu + ",<br>" +
                "Berikut ini kami lampirkan tanggal dan waktu terbaru sidang yang akan Bapak/Ibu/Saudara/i hadiri:<br><br>" +
                "Jenis Sidang : " +   jenisSidang + "<br>" +
                "Mahasiswa : " + mahasiswa + "<br>" +
                "Tanggal : " + tanggal + "<br>" +
                "Waktu : " + waktuMulai + " - " + waktuSelesai + " WIB<br>" +
                "Ruang : " + ruang + " <br><br>" +
                "Demikian informasi ini kami sampaikan. Atas perhatiannya, kami ucapkan terima kasih.<br><br>" +
                "Salam,<br>" +
                "Admin", true
        );
        javaMailSender.send(msg);
    }

    @Override
    public void sendMailHapusSidang(SidangModel sidang) throws MessagingException, ParseException {
        String waktu = getWaktu();

        String mahasiswa = sidang.getMahasiswaSidang().getPenggunaMahasiswa().getUsername();;
        String jenisSidang = sidang.getJenisSidang().getNama();

        Format formatter = new SimpleDateFormat("EEEEEE, dd MMMMM yyyy", new Locale("id"));
        String tanggal = formatter.format(sidang.getTanggal());

        String waktuMulai = sidang.getWaktuMulai().toString();
        String waktuSelesai = sidang.getWaktuSelesai().toString();
        String ruang = sidang.getRuangan();
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        ArrayList<String> listEmailPartisipan = new ArrayList<>();

        for (PartisipanSidangModel partisipan: sidang.getListPartisipanSidang()) {
            listEmailPartisipan.add(partisipan.getPartisipanSidangDosen().getPenggunaDosen().getUsername() + "@farmasi.ui.ac.id");
        }
        listEmailPartisipan.add(sidang.getMahasiswaSidang().getPenggunaMahasiswa().getUsername() + "@ui.ac.id");

        String[] emailDosenPartisipan = listEmailPartisipan.toArray(new String[listEmailPartisipan.size()]);

        helper.setTo(emailDosenPartisipan);
        helper.setSubject("Pembatalan Jadwal " + jenisSidang + " Untuk Mahasiswa " + mahasiswa);
        helper.setText("Selamat " + waktu + ",<br>" +
                "Berikut ini kami informasikan bahwa sidang yang sedianya akan dilaksanakan pada :<br><br>" +
                "Jenis Sidang : " +   jenisSidang + "<br>" +
                "Mahasiswa : " + mahasiswa + "<br>" +
                "Tanggal : " + tanggal + "<br>" +
                "Waktu : " + waktuMulai + " - " + waktuSelesai + " WIB<br>" +
                "Ruang : " + ruang + " <br>" +
                "<h3>TELAH DIBATALKAN.</h3><br>" +
                "Demikian informasi ini kami sampaikan. Atas perhatiannya, kami ucapkan terima kasih.<br><br>" +
                "Salam,<br>" +
                "Admin", true
        );
        javaMailSender.send(msg);
    }

    @Override
    public void sendMailUbahNonSidang(JadwalNonSidangModel nonSidang) throws MessagingException {
        String waktu = getWaktu();

        String dosen = nonSidang.getDosen().getNama();
        String keperluan = nonSidang.getKeperluan().getNama();
        String deskripsi = nonSidang.getDeskripsi();

        Format formatter = new SimpleDateFormat("EEEEEE, dd MMMMM yyyy", new Locale("id"));
        String tanggalMulai = formatter.format(nonSidang.getTanggalMulai());
        String tanggalSelesai = formatter.format(nonSidang.getTanggalSelesai());

        String waktuMulai = nonSidang.getWaktuMulai().toString();
        String waktuSelesai = nonSidang.getWaktuSelesai().toString();
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        ArrayList<String> listEmailPartisipan = new ArrayList<>();

        listEmailPartisipan.add(nonSidang.getDosen().getPenggunaDosen().getUsername() + "@farmasi.ui.ac.id");

        String[] emailDosenPartisipan = listEmailPartisipan.toArray(new String[listEmailPartisipan.size()]);

        helper.setTo(emailDosenPartisipan);
        helper.setSubject("Perubahan Jadwal Kegiatan " + keperluan + " Untuk " + dosen);
        helper.setText("Selamat " + waktu + ",<br>" +
                "Berikut ini kami lampirkan tanggal dan waktu terbaru jadwal kegiatan yang akan Bapak/Ibu ikuti:<br><br>" +
                "Jenis Kegiatan : " +   keperluan + "<br>" +
                "Tanggal dan Waktu Mulai : " + tanggalMulai + " " + waktuMulai + " WIB<br>" +
                "Tanggal dan Waktu Selesai : " + tanggalSelesai + " " + waktuSelesai + " WIB<br>" +
                "Informasi Tambahan : " + deskripsi + "<br><br>" +
                "Demikian informasi ini kami sampaikan. Atas perhatiannya, kami ucapkan terima kasih.<br><br>" +
                "Salam,<br>" +
                "Admin", true
        );
        javaMailSender.send(msg);
    }

    @Override
    public void sendMailHapusNonSidang(JadwalNonSidangModel nonSidang) throws MessagingException {
        String waktu = getWaktu();

        String dosen = nonSidang.getDosen().getNama();
        String keperluan = nonSidang.getKeperluan().getNama();
        String deskripsi = nonSidang.getDeskripsi();

        Format formatter = new SimpleDateFormat("EEEEEE, dd MMMMM yyyy", new Locale("id"));
        String tanggalMulai = formatter.format(nonSidang.getTanggalMulai());
        String tanggalSelesai = formatter.format(nonSidang.getTanggalSelesai());

        String waktuMulai = nonSidang.getWaktuMulai().toString();
        String waktuSelesai = nonSidang.getWaktuSelesai().toString();
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        ArrayList<String> listEmailPartisipan = new ArrayList<>();

        listEmailPartisipan.add(nonSidang.getDosen().getPenggunaDosen().getUsername() + "@farmasi.ui.ac.id");

        String[] emailDosenPartisipan = listEmailPartisipan.toArray(new String[listEmailPartisipan.size()]);

        helper.setTo(emailDosenPartisipan);
        helper.setSubject("Pembatalan Jadwal Kegiatan " + keperluan + " Untuk " + dosen);
        helper.setText("Selamat " + waktu + ",<br>" +
                "Berikut ini kami informasikan kegiatan yang sedianya akan dilaksanakan pada:<br><br>" +
                "Jenis Kegiatan : " +   keperluan + "<br>" +
                "Tanggal dan Waktu Mulai : " + tanggalMulai + " " + waktuMulai + " WIB<br>" +
                "Tanggal dan Waktu Selesai : " + tanggalSelesai + " " + waktuSelesai + " WIB<br>" +
                "Informasi Tambahan : " + deskripsi + "<br>" +
                "<h3>TELAH DIBATALKAN.</h3><br>" +
                "Demikian informasi ini kami sampaikan. Atas perhatiannya, kami ucapkan terima kasih.<br><br>" +
                "Salam,<br>" +
                "Admin", true
        );
        javaMailSender.send(msg);
    }

    @Override
    public void sendMailUbahStatusBerkas(BerkasPrasyaratModel berkas) throws MessagingException {
        String waktu = getWaktu();

        String jenisBerkas = berkas.getJenisBerkas().getNama();
        String statusBerkas = berkas.getStatusBerkas().getNama().toUpperCase();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE, dd-MM-yyyy HH:mm:ss", new Locale("id"));
        String lastUpdate = berkas.getLastUpdated().format(format);

        String namaMhs = berkas.getBerkasMahasiswa().getUsername();
        String username = berkas.getBerkasMahasiswa().getPenggunaMahasiswa().getUsername();

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        ArrayList<String> listEmailPartisipan = new ArrayList<>();
        listEmailPartisipan.add(username + "@ui.ac.id");

        String[] emailDosenPartisipan = listEmailPartisipan.toArray(new String[listEmailPartisipan.size()]);

        helper.setTo(emailDosenPartisipan);
        helper.setSubject("Perubahan Status Persetujuan Berkas " + jenisBerkas + " Untuk " + namaMhs);
        helper.setText("Selamat " + waktu + ",<br>" +
                "<p>Berikut ini kami informasikan bahwa status berkas untuk mahasiswa <strong>" + namaMhs + "&nbsp;</strong>dengan tipe <strong>" + jenisBerkas + "</strong> yang diunggah pada tanggal <strong>" + lastUpdate + "</strong> telah berubah menjadi :</p><br>" +
                "<h3>" + statusBerkas + "</h3><br>" +
                "Demikian informasi ini kami sampaikan. Atas perhatiannya, kami ucapkan terima kasih.<br><br>" +
                "Salam,<br>" +
                "Admin", true
        );
        javaMailSender.send(msg);
    }
}