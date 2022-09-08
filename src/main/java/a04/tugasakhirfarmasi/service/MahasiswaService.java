package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.model.MahasiswaModel;

import java.util.List;

public interface MahasiswaService {
    void addMahasiswaS1(MahasiswaModel mahasiswa);
    void addMahasiswaS2(MahasiswaModel mahasiswa);
    void addMahasiswaS3(MahasiswaModel mahasiswa);
    void addMahasiswaApt(MahasiswaModel mahasiswa);
    String getJenisSidang();
    String getNamaDosenPembimbing();
    MahasiswaModel getMahasiswa();
    List<MahasiswaModel> getListMahasiswa();
    MahasiswaModel getMahasiswaByUsername(String username);
    List<MahasiswaModel> getAllMahasiswaByPeriode(String periode);
}