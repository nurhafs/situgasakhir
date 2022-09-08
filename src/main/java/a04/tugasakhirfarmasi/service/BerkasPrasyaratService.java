package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.BerkasPrasyaratModel;
import a04.tugasakhirfarmasi.model.JenisBerkasModel;
import a04.tugasakhirfarmasi.model.MahasiswaModel;

import java.io.IOException;
import java.util.List;

//import com.sun.jndi.ldap.Ber;
//import com.sun.jndi.ldap.Ber;
import org.springframework.web.multipart.MultipartFile;

public interface BerkasPrasyaratService {

    void addBerkas(BerkasPrasyaratModel berkas);
    void saveFile(MultipartFile file, Integer idJenisBerkas) throws IOException;
    void ubahStatus(Integer id, Integer status);
    BerkasPrasyaratModel deleteBerkas(BerkasPrasyaratModel berkas) throws IOException;
    List<BerkasPrasyaratModel> getListBerkasPrasyaratNotNull();
    BerkasPrasyaratModel getBerkasPrasyaratByJenisBerkas(JenisBerkasModel jenisBerkas);
    BerkasPrasyaratModel getBerkasPrasyaratByMahasiswaAndJenisBerkas(Integer idMahasiswa, Integer idJenisBerkas);
    List<BerkasPrasyaratModel> getListBerkasPrasyaratByJenisBerkas(Integer jenisBerkas);
    List<BerkasPrasyaratModel> getListBerkasPrasyaratByProgramStudi(Integer prodi);
    List<BerkasPrasyaratModel> getListBerkasPrasyaratByJenisBerkasAndProgramStudi(Integer jenisBerkas, Integer prodi);
    BerkasPrasyaratModel getBerkasById(Integer idBerkas);
}
