package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.JenisBerkasModel;
import a04.tugasakhirfarmasi.repository.JenisBerkasDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JenisBerkasServiceImpl implements JenisBerkasService {
    @Autowired
    JenisBerkasDB jenisBerkasDB;

    @Override
    public List<JenisBerkasModel> getListJenisBerkas() {
        return jenisBerkasDB.findAll();
    }
}
