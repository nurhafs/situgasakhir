package a04.tugasakhirfarmasi.service;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import a04.tugasakhirfarmasi.repository.LamaranDB;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import a04.tugasakhirfarmasi.repository.PeriodeDB;
import a04.tugasakhirfarmasi.repository.StatusDB;
import a04.tugasakhirfarmasi.repository.DosenDB;
import a04.tugasakhirfarmasi.model.LamaranModel;
import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.model.PeriodeModel;
import a04.tugasakhirfarmasi.model.StatusModel;
import a04.tugasakhirfarmasi.model.DosenModel;


import java.awt.*;
import java.util.List;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.util.Optional;
import java.util.ArrayList;

import javax.transaction.Transactional;

@Service
@Transactional
public class LamaranServiceImpl implements LamaranService{
    @Autowired
    LamaranDB lamaranDB;

    @Autowired
    StatusDB statusDB;

    @Autowired
    PenggunaDB penggunaDB;

    @Autowired
    DosenDB dosenDB;

    @Autowired
    PeriodeDB periodeDB;

    @Override
    public List<LamaranModel> retrieveListLamaran() {
        return lamaranDB.findAll();
    }

    @Override
    public LamaranModel getLamaranById(Integer id) {
        Optional<LamaranModel> lamaran = lamaranDB.findById(id);
        if (lamaran.isPresent()) {
            return lamaran.get();
        }
        return null;
    }

    @Override
    public List<LamaranModel> getLamaranByStatus(Integer status) {
        List<LamaranModel> all = lamaranDB.findAll();
        List<LamaranModel> filtered = new ArrayList<LamaranModel>();
        StatusModel stat = statusDB.getById(status);
        for (LamaranModel lm : all) {
            if (lm.getStatusLamaran().equals(stat)) {
                filtered.add(lm);
            }
        }
        return filtered;
    }

    @Override
    public void updateJumlahMahasiswa(DosenModel dsn) {
        Integer jmlMhs = dsn.getJumlahMahasiswa();
        dsn.setJumlahMahasiswa(jmlMhs+1);
    }

    @Override
    public void updateJumlahLamaran(DosenModel dosen) {
        Integer jmlLmr = dosen.getJumlahPelamar();
        dosen.setJumlahPelamar(jmlLmr-1);
    }

    @Override
    public void updateStatus(LamaranModel lamaran, Integer newstat) {
        StatusModel stat = statusDB.getById(newstat);
        lamaran.setStatusLamaran(stat);
        lamaranDB.save(lamaran);
    }

    @Override
    public Integer countKuotaMahasiswa (DosenModel dosen) {
        return (10 - dosen.getJumlahMahasiswa()); //tiap dosen maks 10 mhs
    }

    @Override
    public StatusModel getStatusByNama(String namaStatus){
        Optional<StatusModel> status = statusDB.findByNama(namaStatus);
        if(status.isPresent()) return status.get();
        else return null;
    }

    @Override
    public boolean searchNotApprovedLamaran() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaDB.findByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaDB.findByUsername(username);
        }

        List<LamaranModel> listLamaranMahasiswa = pengguna.getMahasiswa().getListLamaran();
        if (listLamaranMahasiswa.size() == 0) {
            return false;
        } else {
            for (LamaranModel lamaran:listLamaranMahasiswa) {
                if (lamaran.getStatusLamaran().getId() == 2 || lamaran.getStatusLamaran().getId() == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<LamaranModel> getAllLamaranByPeriode(String periode) {
        PeriodeModel periodeModel = periodeDB.findByPeriode(periode);
        return periodeModel.getListPeriodeLamaran();
    }

    @Override
    public List<LamaranModel> getAllLamaranFiltered(List<LamaranModel> lamaranAdmin, Integer idDosen, Integer idProdi) {
        if (idDosen != null) {
            for (int i = lamaranAdmin.size() - 1; i >= 0; i--) {
                if (lamaranAdmin.get(i).getLamaranDosen().getId() != idDosen) {
                    lamaranAdmin.remove(i);
                }
            }
        }

        if (idProdi != null) {
            for (int i = lamaranAdmin.size() - 1; i >= 0; i--) {
                if (lamaranAdmin.get(i).getLamaranMahasiswa().getPenggunaMahasiswa().getProdiPengguna().getId() != idProdi) {
                    lamaranAdmin.remove(i);
                }
            }
        }
        return lamaranAdmin;
    }
}
