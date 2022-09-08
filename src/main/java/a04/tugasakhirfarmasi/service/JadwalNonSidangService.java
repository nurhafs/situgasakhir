package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.JadwalNonSidangModel;

import java.text.ParseException;
import java.util.List;

public interface JadwalNonSidangService {
    void addJadwalNonSidang(JadwalNonSidangModel jadwal);
    JadwalNonSidangModel getJadwalNonSidangById(Integer id);
    void deleteJadwal(Integer id);
    void updateJadwal(Integer id, JadwalNonSidangModel newjadwal);
    List<JadwalNonSidangModel> getAllJadwalNonSidang();
    Boolean cekJadwalKegiatan(JadwalNonSidangModel jadwal) throws ParseException;
}
