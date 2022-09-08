package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.model.ProgramStudiModel;

import java.util.List;

public interface ProgramStudiService {
    List<ProgramStudiModel> getListProgramStudi();
    ProgramStudiModel getProgramStudiById(Integer id);
    void assignProgramStudiDosen(PenggunaModel pengguna);
    List<ProgramStudiModel> getListProgramStudiExceptDosenAdmin();
}
