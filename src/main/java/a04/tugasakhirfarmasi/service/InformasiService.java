package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.InformasiModel;
import a04.tugasakhirfarmasi.model.JadwalNonSidangModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface InformasiService {
    void addInformasi(MultipartFile fileInformasi, InformasiModel informasi) throws IOException;
    List<InformasiModel> getAllInformasi(String role, String prodi);
    void deleteInformasi(Integer id);
    InformasiModel getInformasiById(Integer id);
    void updateInformasi(Integer id, InformasiModel info, MultipartFile file) throws IOException;
    List<InformasiModel> findInfoPaginated(int pageNo, int pageSize);
    Page<InformasiModel> findPaginated(Pageable pageable);
}
