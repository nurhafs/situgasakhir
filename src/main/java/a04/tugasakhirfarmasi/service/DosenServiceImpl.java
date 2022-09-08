package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.*;
import a04.tugasakhirfarmasi.repository.DosenDB;
import a04.tugasakhirfarmasi.repository.LamaranDB;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import a04.tugasakhirfarmasi.repository.PeriodeDB;
import a04.tugasakhirfarmasi.repository.StatusDB;
import a04.tugasakhirfarmasi.setting.Setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DosenServiceImpl implements DosenService{
    @Autowired
    private DosenDB dosenDb;

    @Autowired
    private LamaranDB lamaranDb;

    @Autowired
    private StatusDB statusDB;

    @Autowired
    private PenggunaDB penggunaDB;

    @Autowired
    private PeriodeDB periodeDB;

    @Override
    public List<DosenModel> getListDosen() {
        return dosenDb.findAll();
    }

    @Override
    public List<DosenModel> getListDosbing() {
        List<DosenModel> listDosbing = new ArrayList<DosenModel>();
        List<DosenModel> listDosen = getListDosen();
        for (DosenModel dosbing:listDosen) {
            if(dosbing.isDosenPembimbing()) {
                listDosbing.add(dosbing);
            }
        }
        return listDosbing;
    }

    @Override
    public void daftarDosbing(DosenModel dosbing, MahasiswaModel mahasiswa) {
        dosbing.setJumlahPelamar(dosbing.getJumlahPelamar() + 1);
        LamaranModel lamaran = new LamaranModel();
        StatusModel statusModel = statusDB.getById(2);
        lamaran.setLamaranMahasiswa(mahasiswa);
        lamaran.setLamaranDosen(dosbing);
        lamaran.setStatusLamaran(statusModel);
        lamaran.setTanggal(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        lamaran.setWaktu(LocalTime.now());
        lamaran.setPeriodeLamaran(periodeDB.findByPeriode(Setting.CURRENT_PERIODE));
        lamaranDb.save(lamaran);
        dosenDb.save(dosbing);
    }

    @Override
    public DosenModel saveDosen(DosenModel dosen){
        dosenDb.save(dosen);
        return dosen;
    }

    @Override
    public void addDosen(DosenModel dosen){
        dosenDb.save(dosen);
    }

    @Override
    public DosenModel getDosenById(Integer id){
        Optional<DosenModel> dosen = dosenDb.findById(id);
        if(dosen.isPresent()) return dosen.get();
        else return null;
    }

    @Override
    public List<DosenModel> getListAllDosen() {
        return dosenDb.findAll();
    }

    @Override
    public DosenModel getDosen() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel dosenP = penggunaDB.findByUsername(username);
        if (dosenP == null) {
            username = ((UserDetails)principal).getUsername();
            dosenP = penggunaDB.findByUsername(username);
        }
        DosenModel dosen = dosenP.getDosen();
        return dosen;

        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // User user = (User)auth.getPrincipal();
        // PenggunaModel dosenP = penggunaDB.findByUsername(user.getUsername());
        // DosenModel dosen = dosenP.getDosen();
        // return dosen;
    }
}
