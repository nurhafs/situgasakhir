package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.DosenModel;
import a04.tugasakhirfarmasi.model.MahasiswaModel;
import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import a04.tugasakhirfarmasi.repository.PeriodeDB;
import a04.tugasakhirfarmasi.repository.ProgramStudiDB;
import a04.tugasakhirfarmasi.repository.RoleDB;
import a04.tugasakhirfarmasi.setting.Setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PenggunaServiceImpl implements PenggunaService{
    @Autowired
    PenggunaDB penggunaDB;

    @Autowired
    RoleDB roleDB;

    @Autowired
    ProgramStudiDB programStudiDB;

    @Autowired
    private PenggunaDB penggunaDb;

    @Autowired
    private PeriodeDB periodeDB;

    @Qualifier("mahasiswaServiceImpl")
    @Autowired
    private MahasiswaService mahasiswaService;

    @Qualifier("dosenServiceImpl")
    @Autowired
    private DosenService dosenService;

    @Override
    public PenggunaModel addPengguna(String username, String password) {
        System.out.println(password);
        PenggunaModel pengguna = new PenggunaModel();
        pengguna.setRole(roleDB.getRoleModelByNama("Dosen"));
        pengguna.setUsername(username);
        String pass = encrypt(password);
        pengguna.setPassword(pass);
        pengguna.setProdiPengguna(programStudiDB.getById(5));
        penggunaDB.save(pengguna);
        return pengguna;
    }

    @Override
    public void addUser(PenggunaModel user, String namaLengkap) {
        penggunaDb.save(user);
        String passwordLama = user.getPassword();

        if (user.getRole().getNama().equals("Mahasiswa")) {
            MahasiswaModel mahasiswa = new MahasiswaModel(user, user.getPassword(), namaLengkap);
            mahasiswa.setPeriodeMahasiswa(periodeDB.findByPeriode(Setting.CURRENT_PERIODE));
            user.setPassword(encrypt(generateRandomPassword()));
            if (user.getProdiPengguna().getNama().equals("S1")) {
                mahasiswaService.addMahasiswaS1(mahasiswa);
            } else if (user.getProdiPengguna().getNama().equals("S2")) {
                mahasiswaService.addMahasiswaS2(mahasiswa);
            } else if (user.getProdiPengguna().getNama().equals("S3")) {
                mahasiswaService.addMahasiswaS3(mahasiswa);
            } else {
                mahasiswaService.addMahasiswaApt(mahasiswa);
            }
        } else if (user.getRole().getNama().equals("Dosen")) {
            user.setPassword(encrypt(generateRandomPassword()));
            DosenModel dosen = new DosenModel();
            dosen.setDosenPembimbing(true);
            dosen.setDosenPenguji(true);
            dosen.setJumlahMahasiswa(0);
            dosen.setJumlahPelamar(0);
            dosen.setNama(namaLengkap);
            dosen.setPenggunaDosen(user);
            dosenService.addDosen(dosen);
        } else {
            user.setPassword(encrypt(generateRandomPassword()));
        }
        user.setPassword(encrypt(passwordLama));
    }

    @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public List<PenggunaModel> getListPengguna() {
        return penggunaDb.findAll();
    }

    @Override
    public PenggunaModel getUserByUsername(String username) {
        return penggunaDb.findByUsername(username);
    }

    @Override
    public void setDosen(PenggunaModel pengguna, DosenModel dosen){
        pengguna.setDosen(dosen);
        penggunaDB.save(pengguna);
    }

    @Override
    public PenggunaModel getPenggunaById(Integer id){
        Optional<PenggunaModel> pengguna = penggunaDB.findById(id);
        if(pengguna.isPresent()) return pengguna.get();
        else return null;
    }

    @Override
    public PenggunaModel savePengguna(PenggunaModel pengguna){
        penggunaDB.save(pengguna);
        return pengguna;
    }

    @Override
    public void updatePassword(PenggunaModel pengguna, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        pengguna.setPassword(encodedPassword);

        penggunaDB.save(pengguna);
    }


    @Override
    public String generateRandomPassword() {
        String r = "";
        for(int i = 0; i < 15; i++) {
            r += (char)(Math.random() * 26 + 97);
        }
        return r;
    }
}
