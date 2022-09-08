package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.BerkasPrasyaratModel;
import a04.tugasakhirfarmasi.model.JadwalNonSidangModel;
import a04.tugasakhirfarmasi.model.LamaranModel;
import a04.tugasakhirfarmasi.model.SidangModel;

import javax.mail.MessagingException;
import java.text.ParseException;

public interface MailService {
    String getWaktu();
    void sendMailSidang(SidangModel sidang) throws MessagingException, ParseException;
    void sendMailNonSidangByAdminProdi(JadwalNonSidangModel nonSidang) throws MessagingException;
    void sendMailUbahStatusLamaran(LamaranModel lamaran) throws MessagingException;
    void sendMailUbahSidang(SidangModel sidang) throws MessagingException, ParseException;
    void sendMailHapusSidang(SidangModel sidang) throws MessagingException, ParseException;
    void sendMailUbahNonSidang(JadwalNonSidangModel nonSidangModel) throws MessagingException;
    void sendMailHapusNonSidang(JadwalNonSidangModel nonSidangModel) throws MessagingException;
    void sendMailUbahStatusBerkas(BerkasPrasyaratModel berkas) throws MessagingException;
}
