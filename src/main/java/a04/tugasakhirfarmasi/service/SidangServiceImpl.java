package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.DosenModel;
import a04.tugasakhirfarmasi.model.JadwalNonSidangModel;
import a04.tugasakhirfarmasi.model.JenisSidangModel;
import a04.tugasakhirfarmasi.model.PartisipanSidangModel;
import a04.tugasakhirfarmasi.model.PeriodeModel;
import a04.tugasakhirfarmasi.model.SidangModel;
import a04.tugasakhirfarmasi.repository.DosenDB;
import a04.tugasakhirfarmasi.repository.JenisSidangDB;
import a04.tugasakhirfarmasi.repository.PartisipanSidangDB;
import a04.tugasakhirfarmasi.repository.PeriodeDB;
import a04.tugasakhirfarmasi.repository.SidangDB;
import a04.tugasakhirfarmasi.setting.Setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SidangServiceImpl implements SidangService{

    @Autowired
    SidangDB sidangDB;

    @Autowired
    JenisSidangDB jenisSidangDB;

    @Autowired
    DosenDB dosenDB;

    @Autowired
    PartisipanSidangDB partisipanSidangDB;

    @Autowired
    PeriodeDB periodeDB;

    @Override
    public List<JenisSidangModel> getListJenisSidang() {
        return jenisSidangDB.findAll();
    }

    @Override
    public SidangModel addSidang(SidangModel sidang){
        sidang.setPeriodeSidang(periodeDB.findByPeriode(Setting.CURRENT_PERIODE));
        return sidangDB.save(sidang);
    }

    @Override
    public void updateSidang(SidangModel sidang) {
        sidangDB.save(sidang);
    }

    @Override
    public void deleteSidang(SidangModel sidang) {
        sidangDB.delete(sidang);
    }

    @Override
    public void convertDosenToPartisipanSidang(List<DosenModel> listDosen, List<PartisipanSidangModel> listPartisipanSidang){
        for(DosenModel dosen : listDosen){
            if(!(listPartisipanSidang.contains(dosen))) {
                PartisipanSidangModel partisipanSidang = new PartisipanSidangModel();
                partisipanSidang.setId(dosen.getId());
                partisipanSidang.setPartisipanSidangDosen(dosen);
                partisipanSidangDB.save(partisipanSidang);
            }
        }
    }

    @Override
    public List<SidangModel> getListSidang() {
        return sidangDB.findAll();
    }

    @Override
    public boolean isAvailable(SidangModel sidang){
        List<SidangModel> listSidang = getListSidang();
        for(SidangModel existingSidang : listSidang) {
            if (sidang.getTanggal().equals(existingSidang) &&
                    (sidang.getWaktuMulai().isBefore(existingSidang.getWaktuSelesai()) ||
                            sidang.getWaktuSelesai().isAfter(existingSidang.getWaktuMulai()))) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isSidangValid(SidangModel sidang, Integer[] dosenPartisipan) {
        List<DosenModel> listPartisipanDosen = new ArrayList<>();
        for (int i = 0; i < dosenPartisipan.length; i++) {
            listPartisipanDosen.add(dosenDB.getById(dosenPartisipan[i]));
        }
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();
        Date now = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        LocalTime nowTime = LocalTime.now();
        if(sidang.getTanggal().compareTo(now) == 0 && sidang.getWaktuMulai().isBefore(nowTime)){
            return false;
        }
        if (sidang.getTanggal().before(now)) {
            return false;
        }
        if(sidang.getWaktuMulai().isAfter(sidang.getWaktuSelesai())){
            return false;
        }
        for(SidangModel sidangMahasiswa: sidang.getMahasiswaSidang().getListSidang()){
            if (sidangMahasiswa.getTanggal().compareTo(sidang.getTanggal()) == 0) {
                if (sidang.getWaktuMulai().isAfter(sidangMahasiswa.getWaktuMulai()) && sidang.getWaktuMulai().isBefore(sidangMahasiswa.getWaktuSelesai())) {
                    return false;
                }
                if (sidang.getWaktuSelesai().isAfter(sidangMahasiswa.getWaktuMulai()) && sidang.getWaktuSelesai().isBefore(sidangMahasiswa.getWaktuSelesai())) {
                    return false;
                }
                if (sidang.getWaktuMulai().isBefore(sidangMahasiswa.getWaktuMulai()) && sidang.getWaktuSelesai().isAfter(sidangMahasiswa.getWaktuSelesai())) {
                    return false;
                }
                if (sidang.getWaktuMulai().compareTo(sidangMahasiswa.getWaktuMulai()) == 0 || sidang.getWaktuMulai().compareTo(sidangMahasiswa.getWaktuSelesai()) == 0) {
                    return false;
                }
                if (sidang.getWaktuSelesai().compareTo(sidangMahasiswa.getWaktuMulai()) == 0 || sidang.getWaktuSelesai().compareTo(sidangMahasiswa.getWaktuSelesai()) == 0) {
                    return false;
                }
            }
        }

        for (DosenModel partisipan:listPartisipanDosen) {
            for (PartisipanSidangModel existingSidang:partisipan.getListPartisipanSidangDosen()) {
                if (existingSidang.getPartisipanSidang().getId() == sidang.getId()) {
                    continue;
                }
                if (existingSidang.getPartisipanSidang().getTanggal().compareTo(sidang.getTanggal()) == 0) {
                    if (sidang.getWaktuMulai().isAfter(existingSidang.getPartisipanSidang().getWaktuMulai()) && sidang.getWaktuMulai().isBefore(existingSidang.getPartisipanSidang().getWaktuSelesai())) {
                        return false;
                    }
                    if (sidang.getWaktuSelesai().isAfter(existingSidang.getPartisipanSidang().getWaktuMulai()) && sidang.getWaktuSelesai().isBefore(existingSidang.getPartisipanSidang().getWaktuSelesai())) {
                        return false;
                    }
                    if (sidang.getWaktuMulai().isBefore(existingSidang.getPartisipanSidang().getWaktuMulai()) && sidang.getWaktuSelesai().isAfter(existingSidang.getPartisipanSidang().getWaktuSelesai())) {
                        return false;
                    }
                    if (sidang.getWaktuMulai().compareTo(existingSidang.getPartisipanSidang().getWaktuMulai()) == 0 || sidang.getWaktuMulai().compareTo(existingSidang.getPartisipanSidang().getWaktuSelesai()) == 0) {
                        return false;
                    }
                    if (sidang.getWaktuSelesai().compareTo(existingSidang.getPartisipanSidang().getWaktuMulai()) == 0 || sidang.getWaktuSelesai().compareTo(existingSidang.getPartisipanSidang().getWaktuSelesai()) == 0) {
                        return false;
                    }
                }
            }

            for (JadwalNonSidangModel jadwalNonSidang:partisipan.getListJadwalNonSidangDosen()) {
                if (sidang.getTanggal().compareTo(jadwalNonSidang.getTanggalMulai()) == 0 || sidang.getTanggal().compareTo(jadwalNonSidang.getTanggalSelesai()) == 0) {
                    if (sidang.getWaktuMulai().isAfter(jadwalNonSidang.getWaktuMulai()) && sidang.getWaktuMulai().isBefore(jadwalNonSidang.getWaktuSelesai())) {
                        return false;
                    }
                    if (sidang.getWaktuSelesai().isAfter(jadwalNonSidang.getWaktuMulai()) && sidang.getWaktuSelesai().isBefore(jadwalNonSidang.getWaktuSelesai())) {
                        return false;
                    }
                    if (sidang.getWaktuMulai().isBefore(jadwalNonSidang.getWaktuMulai()) && sidang.getWaktuSelesai().isAfter(jadwalNonSidang.getWaktuSelesai())) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    @Override
    public List<PartisipanSidangModel> getListPartisipanSidang() {
        return partisipanSidangDB.findAll();
    }

    @Override
    public SidangModel getSidangById(Integer id){
        Optional<SidangModel> sidang = sidangDB.findById(id);
        if(sidang.isPresent()) return sidang.get();
        else return null;
    }

    @Override
    public List<SidangModel> getAllSidang() {
        return sidangDB.findAll();
    }

    @Override
    public List<SidangModel> getAllSidangByPeriode(String periode) {
        PeriodeModel periodeModel = periodeDB.findByPeriode(periode);
        List<SidangModel> listAllSidang = sidangDB.findAll();
        List<SidangModel> listFilteredSidang = new ArrayList<>();

        for (SidangModel sidang:listAllSidang) {
            if (sidang.getPeriodeSidang().getPeriode().equals(periode)) {
                listFilteredSidang.add(sidang);
            }
        }

        return listFilteredSidang;
    }
}
