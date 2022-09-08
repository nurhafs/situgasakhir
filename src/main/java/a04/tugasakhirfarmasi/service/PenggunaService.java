package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.DosenModel;
import a04.tugasakhirfarmasi.model.PenggunaModel;

import java.util.List;

public interface PenggunaService {
    void addUser(PenggunaModel user, String namaLengkap);
    String encrypt(String password);
    List<PenggunaModel> getListPengguna();
    PenggunaModel getUserByUsername(String username);
    PenggunaModel addPengguna(String username, String password);
    void setDosen(PenggunaModel pengguna, DosenModel dosen);
    PenggunaModel getPenggunaById(Integer id);
    PenggunaModel savePengguna(PenggunaModel pengguna);
    void updatePassword(PenggunaModel pengguna, String newPassword);
    String generateRandomPassword();
}
