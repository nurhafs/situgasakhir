package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.DosenModel;
import a04.tugasakhirfarmasi.model.MahasiswaModel;
import a04.tugasakhirfarmasi.model.PartisipanSidangModel;

import java.util.List;

public interface DosenService {
    List<DosenModel> getListDosen();
    List<DosenModel> getListDosbing();
    void daftarDosbing(DosenModel dosbing, MahasiswaModel mahasiswa);
    DosenModel saveDosen(DosenModel dosen);
    void addDosen(DosenModel dosen);
    DosenModel getDosenById(Integer id);
    List<DosenModel> getListAllDosen();
    DosenModel getDosen();
}