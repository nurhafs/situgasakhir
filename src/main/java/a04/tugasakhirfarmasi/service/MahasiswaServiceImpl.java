package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.BerkasPrasyaratModel;
import a04.tugasakhirfarmasi.model.JenisBerkasModel;
import a04.tugasakhirfarmasi.model.MahasiswaModel;
import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.model.PeriodeModel;
import a04.tugasakhirfarmasi.repository.JenisBerkasDB;
import a04.tugasakhirfarmasi.repository.MahasiswaDB;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import a04.tugasakhirfarmasi.repository.PeriodeDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MahasiswaServiceImpl implements MahasiswaService {
    @Autowired
    private MahasiswaDB mahasiswaDB;

    @Autowired
    private JenisBerkasDB jenisBerkasDB;

    @Autowired
    private PeriodeDB periodeDB;

    @Qualifier("berkasPrasyaratServiceImpl")
    @Autowired
    private BerkasPrasyaratService berkasPrasyaratService;

    @Autowired
    private PenggunaDB penggunaDB;

    @Override
    public void addMahasiswaS1(MahasiswaModel mahasiswa) {
        mahasiswaDB.save(mahasiswa);

        JenisBerkasModel jenisBerkas1 = jenisBerkasDB.getById(2);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas1));
        JenisBerkasModel jenisBerkas2 = jenisBerkasDB.getById(6);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas2));
    }

    @Override
    public void addMahasiswaS2(MahasiswaModel mahasiswa) {
        mahasiswaDB.save(mahasiswa);

        JenisBerkasModel jenisBerkas1 = jenisBerkasDB.getById(1);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas1));
        JenisBerkasModel jenisBerkas2 = jenisBerkasDB.getById(2);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas2));
        JenisBerkasModel jenisBerkas3 = jenisBerkasDB.getById(3);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas3));
        JenisBerkasModel jenisBerkas4 = jenisBerkasDB.getById(4);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas4));
        JenisBerkasModel jenisBerkas5 = jenisBerkasDB.getById(5);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas5));
    }

    @Override
    public void addMahasiswaS3(MahasiswaModel mahasiswa) {
        mahasiswaDB.save(mahasiswa);

        JenisBerkasModel jenisBerkas1 = jenisBerkasDB.getById(7);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas1));
        JenisBerkasModel jenisBerkas2 = jenisBerkasDB.getById(8);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas2));
        JenisBerkasModel jenisBerkas3 = jenisBerkasDB.getById(9);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas3));
        JenisBerkasModel jenisBerkas4 = jenisBerkasDB.getById(10);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas4));
        JenisBerkasModel jenisBerkas5 = jenisBerkasDB.getById(11);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas5));
        JenisBerkasModel jenisBerkas6 = jenisBerkasDB.getById(12);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas6));
        JenisBerkasModel jenisBerkas7 = jenisBerkasDB.getById(13);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas7));
        JenisBerkasModel jenisBerkas8 = jenisBerkasDB.getById(14);
        berkasPrasyaratService.addBerkas(new BerkasPrasyaratModel(mahasiswa, jenisBerkas8));
    }

    @Override
    public void addMahasiswaApt(MahasiswaModel mahasiswa) {
        mahasiswaDB.save(mahasiswa);
    }

    @Override
    public List<MahasiswaModel> getListMahasiswa() {
        return mahasiswaDB.findAllByOrderByUsernameAsc();
    }

    @Override
    public MahasiswaModel getMahasiswaByUsername(String username) {
        return mahasiswaDB.findByUsername(username);
    }

    @Override
    public String getNamaDosenPembimbing() {
        MahasiswaModel mahasiswa = getMahasiswa();
        String namaDosen = "Belum ada dosen pembimbing";

        try {
            if(mahasiswa.getListLamaran() != null) {
                for (int i = mahasiswa.getListLamaran().size()-1; i >= 0 ; i--) {
                    if (mahasiswa.getListLamaran().get(i).getStatusLamaran().getNama().equalsIgnoreCase("disetujui")) {
                        namaDosen = mahasiswa.getListLamaran().get(i).getLamaranDosen().getNama();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            namaDosen = "Belum ada dosen pembimbing";
        }
        return namaDosen;
    }

    @Override
    public String getJenisSidang() {
        MahasiswaModel mahasiswa = getMahasiswa();
        String jenisSidang = "Belum ada sidang";

        try {
            jenisSidang = mahasiswa.getListSidang().get(mahasiswa.getListSidang().size()-1).getJenisSidang().getNama();
        } catch (Exception e) {
            jenisSidang = "Belum ada sidang";
        }

        return jenisSidang;
    }

    @Override
    public MahasiswaModel getMahasiswa() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaDB.findByUsername(username);
        MahasiswaModel mahasiswa = pengguna.getMahasiswa();
        return mahasiswa;
    }

    @Override
    public List<MahasiswaModel> getAllMahasiswaByPeriode(String periode) {
        PeriodeModel periodeModel = periodeDB.findByPeriode(periode);
        return periodeModel.getListPeriodeMahasiswa();
    }
}