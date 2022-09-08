package a04.tugasakhirfarmasi.controller;

import a04.tugasakhirfarmasi.model.*;
import a04.tugasakhirfarmasi.repository.PartisipanSidangDB;
import a04.tugasakhirfarmasi.service.DosenService;
import a04.tugasakhirfarmasi.service.JadwalNonSidangService;
import a04.tugasakhirfarmasi.service.SidangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class JadwalController {
    @Qualifier("jadwalNonSidangServiceImpl")
    @Autowired
    private JadwalNonSidangService jadwalNonSidangService;

    @Qualifier("sidangServiceImpl")
    @Autowired
    private SidangService sidangService;

    @Qualifier("dosenServiceImpl")
    @Autowired
    private DosenService dosenService;

    @Autowired
    PartisipanSidangDB partisipanSidangDB;

    @GetMapping("/jadwal")
    public String jadwal(
        @RequestParam(value = "namaDosen", required = false) Integer namaDosen,
        Model model
    ) {
        //Untuk tiap Dosen
        List<JadwalNonSidangModel> listJadwalNSDosen = new ArrayList<>();
        List<String> listKeperluanDosen = new ArrayList<>();
        DosenModel dosen = new DosenModel();
        if (dosenService.getDosen() != null) {
            dosen = dosenService.getDosen();
        }

        //Untuk Admin Prodi dan Admin Fakultas
        List<JadwalNonSidangModel> listJadwalNonSidang = jadwalNonSidangService.getAllJadwalNonSidang();
        if (namaDosen != null) {
            listJadwalNonSidang = dosenService.getDosenById(namaDosen).getListJadwalNonSidangDosen();
        }
        List<String> listKeperluan = new ArrayList<>();
        List<String> listDosen = new ArrayList<>();

        for (int i = 0; i < listJadwalNonSidang.size(); i++) {
            listKeperluan.add(listJadwalNonSidang.get(i).getKeperluan().getNama());
            listDosen.add(listJadwalNonSidang.get(i).getDosen().getNama());

            //Untuk tiap Dosen
            if (dosenService.getDosen() != null) {
                if (listJadwalNonSidang.get(i).getDosen().getPenggunaDosen().getUsername().equals(dosen.getPenggunaDosen().getUsername())) {
                    listJadwalNSDosen.add(listJadwalNonSidang.get(i));
                    listKeperluanDosen.add(listJadwalNonSidang.get(i).getKeperluan().getNama());
                }
            }
        }

        //Untuk Admin Prodi dan Admin Fakultas
        List<SidangModel> listSidang = sidangService.getAllSidang();
        if (namaDosen != null) {
            listSidang.clear();
            for (PartisipanSidangModel partisipanSidang:dosenService.getDosenById(namaDosen).getListPartisipanSidangDosen()) {
                listSidang.add(partisipanSidang.getPartisipanSidang());
            }
        }
        List<String> listJenisSidang = new ArrayList<>();
        List<String> listMahasiswa = new ArrayList<>();

        //Untuk tiap Dosen
        List<SidangModel> listSidangDosen = new ArrayList<>();
        List<String> listJenisSidangDosen = new ArrayList<>();
        List<String> listMahasiswaSidangDosen = new ArrayList<>();

        //Untuk Admin Prodi dan Admin Fakultas
        for (int i = 0; i < listSidang.size(); i++) {
            listJenisSidang.add(listSidang.get(i).getJenisSidang().getNama());
            listMahasiswa.add(listSidang.get(i).getMahasiswaSidang().getPenggunaMahasiswa().getUsername());

            //Untuk Dosen
            //TODO: DAPETIN GETPENGGUNADOSEN DARI LISTPARTISIPANSIDANG
            if (dosenService.getDosen() != null) {
                boolean correctDosen = isParticipant(listSidang.get(i));
                if (correctDosen == true) {
                    listSidangDosen.add(listSidang.get(i));
                    listMahasiswaSidangDosen.add(listSidang.get(i).getMahasiswaSidang().getPenggunaMahasiswa().getUsername());
                    listJenisSidangDosen.add(listSidang.get(i).getJenisSidang().getNama());
                }
            }
        }

        //Untuk Admin Prodi dan Admin Fakultas
        model.addAttribute("listJadwalNS", listJadwalNonSidang);
        model.addAttribute("listSidang", listSidang);
        model.addAttribute("listJenisSidang", listJenisSidang);
        model.addAttribute("listMahasiswa", listMahasiswa);
        model.addAttribute("listKeperluan", listKeperluan);
        model.addAttribute("listDosen", listDosen);

        //Untuk tiap Dosen
        model.addAttribute("listJadwalNSDosen", listJadwalNSDosen);
        model.addAttribute("listKeperluanDosen", listKeperluanDosen);
        model.addAttribute("dosen", dosen.getNama());
        model.addAttribute("listSidangDosen", listSidangDosen);
        model.addAttribute("listMahasiswaDosen", listMahasiswaSidangDosen);
        model.addAttribute("listJenisSidangDosen", listJenisSidangDosen);
        model.addAttribute("listDosenFilter", dosenService.getListAllDosen());
        return "viewall-jadwal";
    }

    private boolean isParticipant(SidangModel sidang) {
        DosenModel dosen = new DosenModel();
        if (dosenService.getDosen() != null) {
            dosen = dosenService.getDosen();
        }
        List<PartisipanSidangModel> partisipans = sidang.getListPartisipanSidang();
        boolean correctDosen = false;
        for (PartisipanSidangModel partisipan : partisipans) {
            if (partisipan.getPartisipanSidangDosen().getPenggunaDosen().getUsername().equals(dosen.getPenggunaDosen().getUsername())) {
                correctDosen = true;
                break;
            }
        }
        return correctDosen;
    }
}
