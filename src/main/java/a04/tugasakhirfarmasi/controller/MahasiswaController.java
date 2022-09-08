package a04.tugasakhirfarmasi.controller;

import a04.tugasakhirfarmasi.model.MahasiswaModel;
import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.service.MahasiswaService;
import a04.tugasakhirfarmasi.service.PenggunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class MahasiswaController {
    @Autowired
    private PenggunaService penggunaService;

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping("/mahasiswa")
    public String listMahasiswa(Model model){
        List<MahasiswaModel> listMahasiswa = mahasiswaService.getListMahasiswa();
        model.addAttribute("listMahasiswa", listMahasiswa);
        return "mahasiswa-viewall";
    }

    @GetMapping("/mahasiswa/profil")
    public String profilMahasiswa(Model model){
        String jenisSidang = mahasiswaService.getJenisSidang();
        String namaDosen = mahasiswaService.getNamaDosenPembimbing();
        MahasiswaModel mahasiswa = mahasiswaService.getMahasiswa();

        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("namaDosen", namaDosen);
        model.addAttribute("jenisSidang", jenisSidang);
        return "profil-mahasiswa";
    }

    @GetMapping("/mahasiswa/detail/{username}")
    public String detailMahasiswa(Model model, @PathVariable String username){
        PenggunaModel pengguna = penggunaService.getUserByUsername(username);
        MahasiswaModel mahasiswa = pengguna.getMahasiswa();
        PenggunaModel penggunaMhs = mahasiswa.getPenggunaMahasiswa();
        String jenisSidang = "";
        String namaDosen = "Belum ada dosen pembimbing";

        if (mahasiswa.getListSidang().size() != 0) {
            jenisSidang = mahasiswa.getListSidang().get(mahasiswa.getListSidang().size()-1).getJenisSidang().getNama();
        } else {
            jenisSidang = "Belum ada sidang";
        }

        if(mahasiswa.getListLamaran() != null) {
            for (int i = mahasiswa.getListLamaran().size()-1; i >= 0 ; i--) {
                if (mahasiswa.getListLamaran().get(i).getStatusLamaran().getNama().equalsIgnoreCase("disetujui")) {
                    namaDosen = mahasiswa.getListLamaran().get(i).getLamaranDosen().getNama();
                    break;
                }
            }
        } else {
            namaDosen = "Belum ada dosen pembimbing";
        }

        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("namaDosen", namaDosen);
        model.addAttribute("jenisSidang", jenisSidang);
        model.addAttribute("penggunaMhs", penggunaMhs);
        return "detail-mahasiswa";
    }
}