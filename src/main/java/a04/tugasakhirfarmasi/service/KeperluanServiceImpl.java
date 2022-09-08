package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.KeperluanModel;
import a04.tugasakhirfarmasi.repository.KeperluanDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class KeperluanServiceImpl implements KeperluanService{
    @Autowired
    KeperluanDB keperluanDB;

    @Override
    public List<KeperluanModel> getListKeperluan() {
        return keperluanDB.findAll();
    }
}
