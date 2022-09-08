package a04.tugasakhirfarmasi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import a04.tugasakhirfarmasi.model.BerkasPrasyaratModel;
import a04.tugasakhirfarmasi.model.JenisBerkasModel;
import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.model.StatusModel;
import a04.tugasakhirfarmasi.repository.BerkasPrasyaratDB;
import a04.tugasakhirfarmasi.repository.JenisBerkasDB;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import a04.tugasakhirfarmasi.repository.StatusDB;
import net.bytebuddy.asm.Advice.Local;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.transaction.Transactional;

@Service
@Transactional
public class BerkasPrasyaratServiceImpl implements BerkasPrasyaratService {

    @Autowired
    BerkasPrasyaratDB berkasPrasyaratDB;

    @Autowired
    JenisBerkasDB jenisBerkasDB;

    @Autowired
    StatusDB statusDB;

    @Autowired
    PenggunaDB penggunaDB;

    @Override
    public void addBerkas(BerkasPrasyaratModel berkas) {
        berkasPrasyaratDB.save(berkas);
    }

    @Override
    public void saveFile(MultipartFile file, Integer idJenisBerkas) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        JenisBerkasModel jenisBerkasModel = jenisBerkasDB.getById(idJenisBerkas);
        StatusModel statusModel = statusDB.getById(2);
        BerkasPrasyaratModel berkas = getBerkasPrasyaratByJenisBerkas(jenisBerkasModel);

        berkas.setNama(fileName);
        berkas.setTipe(file.getContentType());
        berkas.setFile(file.getBytes());
        berkas.setStatusBerkas(statusModel);
        berkas.setLastUpdated(ZonedDateTime.now(ZoneId.of("Asia/Jakarta")).toLocalDateTime());
        berkasPrasyaratDB.save(berkas);
    }

    @Override
    public void ubahStatus(Integer id, Integer status) {
        BerkasPrasyaratModel bpm = berkasPrasyaratDB.getById(id);
        StatusModel sm = statusDB.getById(status);
        bpm.setStatusBerkas(sm);
    }

    @Override
    public BerkasPrasyaratModel deleteBerkas(BerkasPrasyaratModel berkas) throws IOException {
        berkas.setFile(null);
        berkas.setNama(null);
        berkas.setTipe(null);
        berkas.setStatusBerkas(null);
        return berkas;
    }


    @Override
    public List<BerkasPrasyaratModel> getListBerkasPrasyaratNotNull() {
        List<BerkasPrasyaratModel> listBerkas = berkasPrasyaratDB.findAll();
        List<BerkasPrasyaratModel> listBerkasFiltered = new ArrayList<>();

        for (BerkasPrasyaratModel berkas:listBerkas) {
            if (berkas.getFile() != null) {
                listBerkasFiltered.add(berkas);
            }
        }
        return listBerkasFiltered;
    }

    @Override
    public BerkasPrasyaratModel getBerkasPrasyaratByJenisBerkas(JenisBerkasModel jenisBerkas) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaDB.findByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaDB.findByUsername(username);
        }
        List<BerkasPrasyaratModel> listBerkas = pengguna.getMahasiswa().getListBerkasPrasyarat();

        for (BerkasPrasyaratModel berkas:listBerkas) {
            if(berkas.getJenisBerkas().getId().equals(jenisBerkas.getId())) {
                return berkas;
            }
        }
        return null;
    }

    @Override
    public BerkasPrasyaratModel getBerkasPrasyaratByMahasiswaAndJenisBerkas(Integer idMahasiswa, Integer idJenisBerkas) {
        List<BerkasPrasyaratModel> listBerkas = berkasPrasyaratDB.findAll();
        for (BerkasPrasyaratModel berkas:listBerkas) {
            if (berkas.getBerkasMahasiswa().getId().equals(idMahasiswa) && berkas.getJenisBerkas().getId().equals(idJenisBerkas)) {
                return berkas;
            }
        }
        return null;
    }

    @Override
    public List<BerkasPrasyaratModel> getListBerkasPrasyaratByJenisBerkas(Integer jenisBerkas) {
        JenisBerkasModel jenisBerkasModel = jenisBerkasDB.getById(jenisBerkas);
        List<BerkasPrasyaratModel> listBerkas = jenisBerkasModel.getListBerkasPrasyarat();
        List<BerkasPrasyaratModel> listBerkasFiltered = new ArrayList<>();

        for (BerkasPrasyaratModel berkas:listBerkas) {
            if (berkas.getFile() != null) {
                listBerkasFiltered.add(berkas);
            }
        }
        return listBerkasFiltered;
    }

    @Override
    public List<BerkasPrasyaratModel> getListBerkasPrasyaratByProgramStudi(Integer prodi) {
        List<BerkasPrasyaratModel> listBerkas = berkasPrasyaratDB.findAll();
        List<BerkasPrasyaratModel> listBerkasFiltered = new ArrayList<>();

        for (BerkasPrasyaratModel berkas:listBerkas) {
            if (berkas.getBerkasMahasiswa().getPenggunaMahasiswa().getProdiPengguna().getId().equals(prodi) && berkas.getFile() != null) {
                listBerkasFiltered.add(berkas);
            }
        }
        return listBerkasFiltered;
    }

    @Override
    public List<BerkasPrasyaratModel> getListBerkasPrasyaratByJenisBerkasAndProgramStudi(Integer jenisBerkas, Integer prodi) {
        JenisBerkasModel jenisBerkasModel = jenisBerkasDB.getById(jenisBerkas);
        List<BerkasPrasyaratModel> listBerkasByJenis = jenisBerkasModel.getListBerkasPrasyarat();
        List<BerkasPrasyaratModel> listBerkasFiltered = new ArrayList<>();

        for (BerkasPrasyaratModel berkas:listBerkasByJenis) {
            if (berkas.getBerkasMahasiswa().getPenggunaMahasiswa().getProdiPengguna().getId().equals(prodi) && berkas.getFile() != null) {
                listBerkasFiltered.add(berkas);
            }
        }
        return listBerkasFiltered;
    }

    @Override
    public BerkasPrasyaratModel getBerkasById(Integer idBerkas) {
        BerkasPrasyaratModel berkas = berkasPrasyaratDB.getById(idBerkas);
        return berkas;
    }
}

