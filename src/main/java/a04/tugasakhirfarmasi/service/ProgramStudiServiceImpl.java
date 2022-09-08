package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.model.ProgramStudiModel;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import a04.tugasakhirfarmasi.repository.ProgramStudiDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProgramStudiServiceImpl implements ProgramStudiService {
    @Autowired
    ProgramStudiDB programStudiDB;

    @Autowired
    PenggunaDB penggunaDB;

    @Override
    public ProgramStudiModel getProgramStudiById(Integer id){
        return programStudiDB.getById(id);
    }

    @Override
    public List<ProgramStudiModel> getListProgramStudi() {
        return programStudiDB.findAll();
    }

    @Override
    public void assignProgramStudiDosen(PenggunaModel pengguna){
        if(programStudiDB.findByNama("Dosen") == null){
            ProgramStudiModel programStudi = new ProgramStudiModel();
            programStudi.setNama("Dosen");
            programStudiDB.save(programStudi);
            pengguna.setProdiPengguna(programStudi);
            penggunaDB.save(pengguna);
        }
        else{
            pengguna.setProdiPengguna(programStudiDB.findByNama("Dosen"));
            penggunaDB.save(pengguna);
        }
    }

    @Override
    public List<ProgramStudiModel> getListProgramStudiExceptDosenAdmin() {
        List<ProgramStudiModel> listProgramStudi = programStudiDB.findAll();
        for (int i = listProgramStudi.size() - 1; i >= 0; i--) {
            if (listProgramStudi.get(i).getNama().equals("Admin") || listProgramStudi.get(i).getNama().equals("Dosen")) {
                listProgramStudi.remove(i);
            }
        }
        return listProgramStudi;
    }

}
