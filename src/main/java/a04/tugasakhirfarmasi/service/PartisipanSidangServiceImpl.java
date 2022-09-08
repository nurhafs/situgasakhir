package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.DosenModel;
import a04.tugasakhirfarmasi.model.JenisSidangModel;
import a04.tugasakhirfarmasi.model.PartisipanSidangModel;
import a04.tugasakhirfarmasi.model.RolePartisipanSidangModel;
import a04.tugasakhirfarmasi.model.SidangModel;
import a04.tugasakhirfarmasi.repository.DosenDB;
import a04.tugasakhirfarmasi.repository.JenisSidangDB;
import a04.tugasakhirfarmasi.repository.PartisipanSidangDB;
import a04.tugasakhirfarmasi.repository.RolePartisipanSidangDB;
import a04.tugasakhirfarmasi.repository.SidangDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartisipanSidangServiceImpl implements PartisipanSidangService {
    
    @Autowired
    PartisipanSidangDB partisipanSidangDB;

    @Autowired
    RolePartisipanSidangDB rolePartisipanSidangDB;

    @Autowired
    DosenDB dosenDB;

    @Override
    public void addPartisipanSidang(SidangModel sidang, Integer dosenId, Integer rolePartisipanSidangId) {
        DosenModel dosen = dosenDB.getById(dosenId);
        RolePartisipanSidangModel rolePartisipanSidang = rolePartisipanSidangDB.getById(rolePartisipanSidangId);
        PartisipanSidangModel partisipanSidang = new PartisipanSidangModel(sidang, dosen, rolePartisipanSidang);
        partisipanSidangDB.save(partisipanSidang);
    }
//
//    @Override
//    public List<PartisipanSidangModel> getPartisipanBySidang(SidangModel sidang) {
//        return partisipanSidangDB.findByIdSidang(sidang);
//    }
}
