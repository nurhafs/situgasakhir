package a04.tugasakhirfarmasi.controller;

import a04.tugasakhirfarmasi.model.*;
import a04.tugasakhirfarmasi.repository.LamaranDB;
import a04.tugasakhirfarmasi.repository.RoleDB;
import a04.tugasakhirfarmasi.repository.DosenDB;
import a04.tugasakhirfarmasi.repository.MahasiswaDB;
import a04.tugasakhirfarmasi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dosen")
public class DosenController {
    @Qualifier("dosenServiceImpl")
    @Autowired
    private DosenService dosenService;

    @Qualifier("lamaranServiceImpl")
    @Autowired
    private LamaranService lamaranService;

    @Qualifier("penggunaServiceImpl")
    @Autowired
    private PenggunaService penggunaService;

    @Qualifier("programStudiServiceImpl")
    @Autowired
    private ProgramStudiService programStudiService;

    @Autowired
    RoleDB roleDB;

    @Autowired
    private DosenDB dosenDB;

    @Autowired
    private MahasiswaDB mahasiswaDB;

    @Autowired
    private LamaranDB lamaranDB;

    @GetMapping("/daftar-dosbing")
    public String daftarDosbing(Model model) {
        List<DosenModel> listDosbing = dosenService.getListDosbing();
        boolean doesMahasiswaHaveActiveLamaran = lamaranService.searchNotApprovedLamaran();
        model.addAttribute("listDosbing", listDosbing);
        model.addAttribute("activeLamaran", doesMahasiswaHaveActiveLamaran);
        return "daftar-dosbing";
    }

    @GetMapping("/profil")
    public String profilDosen(Model model) {
        DosenModel dosen = dosenService.getDosen();
        List<LamaranModel> listLamaranDosen = dosen.getListLamaranDosen();
        List<MahasiswaModel> listMahasiswaBimbingan = new ArrayList<>();
        StatusModel disetujui = lamaranService.getStatusByNama("Disetujui");
        for(LamaranModel lamaran : listLamaranDosen){
            if(lamaran.getLamaranDosen().equals(dosen) && lamaran.getStatusLamaran().equals(disetujui)){
                listMahasiswaBimbingan.add(lamaran.getLamaranMahasiswa());
            }
        }
        model.addAttribute("dosen", dosen);
        model.addAttribute("listMahasiswaBimbingan", listMahasiswaBimbingan);
        return "profil-dosen";
    }

    @GetMapping("/lihat-dosbing")
    private String getDosbing(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaService.getUserByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaService.getUserByUsername(username);
        }
        boolean sudahDaftar = true;
        if (pengguna.getRole().getNama().equals("Mahasiswa")) {
            MahasiswaModel mahasiswa = mahasiswaDB.findByUsername(username);
            sudahDaftar = lamaranService.searchNotApprovedLamaran();
        }
        List<DosenModel> listDosbing = dosenService.getListDosbing();
        model.addAttribute("sudahDaftar", sudahDaftar);
        model.addAttribute("listDosbing", listDosbing);
        return "list-dosbing";
    }

    @PostMapping("/daftar-dosbing/{idDosen}")
    public String daftarDosbingPilih(
            @PathVariable int idDosen
    ){
        DosenModel dosen = dosenDB.findById(idDosen).get();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel penggunaMahasiswa = penggunaService.getUserByUsername(username);
        if (penggunaMahasiswa == null) {
            username = ((UserDetails)principal).getUsername();
            penggunaMahasiswa = penggunaService.getUserByUsername(username);
        }
        dosenService.daftarDosbing(dosen, penggunaMahasiswa.getMahasiswa());
        return "redirect:/dosen/lihat-dosbing";
    }

    @GetMapping("/tambah")
    public String addDosenFormPage(Model model) {
        PenggunaModel pengguna = new PenggunaModel();
        DosenModel dosen = new DosenModel();
        dosen.setPenggunaDosen(pengguna);
        model.addAttribute("dosen", dosen);
        return "form-add-dosen";
    }

    @PostMapping("/tambah")
    public String addDosenSubmitPage(
          @ModelAttribute DosenModel dosen,
          @RequestParam(value = "username") String username,
          @RequestParam(value = "password") String password,
          @RequestParam(value = "isDosenPembimbing") int isDosenPembimbing,
          @RequestParam(value = "isDosenPenguji") int isDosenPenguji,
          Model model
    ) {
        PenggunaModel penggunaBaru = penggunaService.addPengguna(username, password);
        if(isDosenPembimbing==0){
            dosen.setDosenPembimbing(false);
        }
        else if(isDosenPembimbing==1){
            dosen.setDosenPembimbing(true);
        }

        if(isDosenPenguji==0){
            dosen.setDosenPenguji(false);
        }
        else if(isDosenPenguji==1){
            dosen.setDosenPenguji(true);
        }

        PenggunaModel pengguna = penggunaService.getPenggunaById(penggunaBaru.getId());
        programStudiService.assignProgramStudiDosen(pengguna);
        dosen.setPenggunaDosen(pengguna);
        dosenService.addDosen(dosen);
        model.addAttribute("namaDosen", dosen.getNama());
        return "add-dosen";
    }

    @GetMapping("/ubah/{id}")
    public String updateDosenFormPage(
            @PathVariable Integer id,
            Model model
    ) {
        DosenModel dosen = dosenService.getDosenById(id);
        model.addAttribute("dosen", dosen);
        return "form-update-dosen";
    }

    @PostMapping("/ubah")
    public String updateDosenSubmitPage(
            @ModelAttribute DosenModel dosen,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "isDosenPembimbing") int isDosenPembimbing,
            @RequestParam(value = "isDosenPenguji") int isDosenPenguji,
            Model model
    ){
        if(isDosenPembimbing==0){
            dosen.setDosenPembimbing(false);
        }
        else if(isDosenPembimbing==1){
            dosen.setDosenPembimbing(true);
        }

        if(isDosenPenguji==0){
            dosen.setDosenPenguji(false);
        }
        else if(isDosenPenguji==1){
            dosen.setDosenPenguji(true);
        }
        dosen.getPenggunaDosen().setUsername(username);
        penggunaService.savePengguna(dosen.getPenggunaDosen());
        dosen.setPenggunaDosen(dosen.getPenggunaDosen());
        DosenModel updatedDosen = dosenService.saveDosen(dosen);
        model.addAttribute("namaDosen", updatedDosen.getNama());
        return "update-dosen";
    }

    @RequestMapping("/detail/{id}")
    public String viewDetailDosen(
            @PathVariable Integer id,
            Model model
    ) {
        DosenModel dosen = dosenService.getDosenById(id);
        List<LamaranModel> listLamaranDosen = dosen.getListLamaranDosen();
        List<MahasiswaModel> listMahasiswaBimbingan = new ArrayList<>();
        StatusModel disetujui = lamaranService.getStatusByNama("Disetujui");
        for(LamaranModel lamaran : listLamaranDosen){
            if(lamaran.getLamaranDosen().equals(dosen) && lamaran.getStatusLamaran().equals(disetujui)){
                listMahasiswaBimbingan.add(lamaran.getLamaranMahasiswa());
            }
        }
        model.addAttribute("dosen", dosen);
        model.addAttribute("listMahasiswaBimbingan", listMahasiswaBimbingan);
        return "detail-dosen";
    }
    
    // @GetMapping("/jadwal")
    // public String viewConfirmList(Model model) {
    //     List<SidangModel> konfirms = 
    // }
}

