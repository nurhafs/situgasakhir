package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.RoleModel;
import a04.tugasakhirfarmasi.repository.RoleDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleDB roleDB;

    @Override
    public List<RoleModel> getListRole() {
        return roleDB.findAll();
    }
}
