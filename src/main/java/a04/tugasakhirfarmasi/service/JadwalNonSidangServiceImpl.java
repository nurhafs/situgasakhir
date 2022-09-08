package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.JadwalNonSidangModel;
import a04.tugasakhirfarmasi.repository.JadwalNonSidangDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JadwalNonSidangServiceImpl implements JadwalNonSidangService {
    @Autowired
    private JadwalNonSidangDB jadwalNonSidangDb;

    @Override
    public void addJadwalNonSidang(JadwalNonSidangModel jadwal) {
        jadwalNonSidangDb.save(jadwal);
    }

    @Override
    public JadwalNonSidangModel getJadwalNonSidangById(Integer id){
        Optional<JadwalNonSidangModel> jadwalNonSidang = jadwalNonSidangDb.findById(id);
        if(jadwalNonSidang.isPresent()) return jadwalNonSidang.get();
        else return null;
    }

    @Override
    public void deleteJadwal(Integer id) {
        JadwalNonSidangModel jns = jadwalNonSidangDb.findById(id).get();
        jadwalNonSidangDb.delete(jns);
    }

    @Override
    public void updateJadwal(Integer id, JadwalNonSidangModel newjadwal) {
        JadwalNonSidangModel jadwal = jadwalNonSidangDb.getById(id);
        jadwal.setTanggalMulai(newjadwal.getTanggalMulai());
        jadwal.setTanggalSelesai(newjadwal.getTanggalSelesai());
        jadwal.setWaktuMulai(newjadwal.getWaktuMulai());
        jadwal.setWaktuSelesai(newjadwal.getWaktuSelesai());
        jadwal.setKeperluan(newjadwal.getKeperluan());
        jadwal.setDosen(newjadwal.getDosen());
        jadwal.setDeskripsi(newjadwal.getDeskripsi());
    }

    @Override
    public List<JadwalNonSidangModel> getAllJadwalNonSidang() {
        List<JadwalNonSidangModel> listJadwalNonSidang = jadwalNonSidangDb.findAll();
        return listJadwalNonSidang;
    }

    @Override
    public Boolean cekJadwalKegiatan(JadwalNonSidangModel jadwal) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();
        Date now = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        LocalTime nowTime = LocalTime.now();
        if (jadwal.getTanggalMulai().compareTo(now) == 0 && jadwal.getWaktuMulai().isBefore(nowTime)) {
            return false;
        }
        else if(jadwal.getTanggalMulai().before(now)){
            return false;
        } else {
            if (jadwal.getTanggalMulai().after(jadwal.getTanggalSelesai())) {
                return false;
            } else {
                if (jadwal.getTanggalMulai().after(jadwal.getTanggalSelesai())) {
                    return false;
                } else if (jadwal.getTanggalMulai().equals(jadwal.getTanggalSelesai())) {
                    if (jadwal.getWaktuMulai().isAfter(jadwal.getWaktuSelesai())) {
                        return false;
                    }
                } else if (jadwal.getTanggalMulai().equals(jadwal.getTanggalSelesai())) {
                    if (jadwal.getWaktuMulai().isAfter(jadwal.getWaktuSelesai())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
