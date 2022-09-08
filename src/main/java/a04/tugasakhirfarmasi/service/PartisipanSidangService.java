package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.DosenModel;
import a04.tugasakhirfarmasi.model.JenisSidangModel;
import a04.tugasakhirfarmasi.model.PartisipanSidangModel;
import a04.tugasakhirfarmasi.model.SidangModel;

import java.util.List;

public interface PartisipanSidangService {
    void addPartisipanSidang(SidangModel sidang, Integer dosenId, Integer rolePartisipanSidangId);
//    List<PartisipanSidangModel> getPartisipanBySidang(SidangModel sidang);
}
