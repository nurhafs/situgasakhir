package a04.tugasakhirfarmasi.service;
import a04.tugasakhirfarmasi.model.LamaranModel;
import a04.tugasakhirfarmasi.model.StatusModel;
import a04.tugasakhirfarmasi.model.DosenModel;
import com.lowagie.text.pdf.PdfPTable;

import java.util.List;

public interface LamaranService {
    List<LamaranModel> retrieveListLamaran();
    LamaranModel getLamaranById(Integer id);
    List<LamaranModel> getLamaranByStatus(Integer status);
    void updateJumlahMahasiswa(DosenModel dosen);
    void updateJumlahLamaran(DosenModel dosen);
    void updateStatus(LamaranModel lamaran, Integer newStat);
    Integer countKuotaMahasiswa (DosenModel dosen);
    StatusModel getStatusByNama(String namaStatus);
    boolean searchNotApprovedLamaran();
    List<LamaranModel> getAllLamaranByPeriode(String periode);
    List<LamaranModel> getAllLamaranFiltered(List<LamaranModel> lamaranAdmin, Integer idDosen, Integer idProdi);
}
