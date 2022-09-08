package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.DosenModel;
import a04.tugasakhirfarmasi.model.JenisSidangModel;
import a04.tugasakhirfarmasi.model.PartisipanSidangModel;
import a04.tugasakhirfarmasi.model.SidangModel;

import java.util.List;

public interface SidangService {
    List<JenisSidangModel> getListJenisSidang();
    SidangModel addSidang(SidangModel sidang);
    void updateSidang(SidangModel sidang);
    void deleteSidang(SidangModel sidang);
    void convertDosenToPartisipanSidang(List<DosenModel> listDosen, List<PartisipanSidangModel> listPartisipanSidang);
    List<PartisipanSidangModel> getListPartisipanSidang();
    List<SidangModel> getListSidang();
    boolean isAvailable(SidangModel sidang);
    boolean isSidangValid(SidangModel sidang, Integer[] dosenPartisipan);
    SidangModel getSidangById(Integer id);
    List<SidangModel> getAllSidang();
    List<SidangModel> getAllSidangByPeriode(String periode);
}
