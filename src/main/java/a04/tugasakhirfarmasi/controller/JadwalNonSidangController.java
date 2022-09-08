package a04.tugasakhirfarmasi.controller;

import a04.tugasakhirfarmasi.model.*;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import a04.tugasakhirfarmasi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;

@Controller
@RequestMapping("/jadwal/non-sidang")
public class JadwalNonSidangController {

    @Qualifier("jadwalNonSidangServiceImpl")
    @Autowired
    private JadwalNonSidangService jadwalNonSidangService;

    @Autowired
    private KeperluanService keperluanService;

    @Autowired
    private PenggunaService penggunaService;

    @Autowired
    private DosenService dosenService;

    @Autowired
    private MailService mailService;

    @Autowired
    PenggunaDB penggunaDB;

    @RequestMapping("/detail/{id}")
    public String viewDetailJadwalNonSidang(
            @PathVariable Integer id,
            Model model
    ) {
        JadwalNonSidangModel jadwalNonSidang = jadwalNonSidangService.getJadwalNonSidangById(id);
        model.addAttribute("jadwalNonSidang", jadwalNonSidang);
        return "detail-non-sidang";
    }

    @GetMapping("/tambah")
    public String addNonSidangFormPage(Model model) {
        JadwalNonSidangModel jadwalNonSidangModel =  new  JadwalNonSidangModel();
        List<KeperluanModel> listKeperluan = keperluanService.getListKeperluan();
        List<DosenModel> listDosen = dosenService.getListDosen();
        model.addAttribute("listKeperluan", listKeperluan);
        model.addAttribute("listDosen", listDosen);
        model.addAttribute("jadwalNonSidangModel", jadwalNonSidangModel);

        return "form-add-non-sidang";

    }
    @PostMapping(value = "/tambah")
    private String addJadwalNonSidangSubmit(
            @ModelAttribute JadwalNonSidangModel jadwal,
            RedirectAttributes redirectAttributes,
            Model model) throws MessagingException, ParseException {

        if (!jadwalNonSidangService.cekJadwalKegiatan(jadwal)) {
            redirectAttributes.addFlashAttribute("message", "Penambahan tidak berhasil. Harap perbaiki waktu atau tanggal pengisian.");
            return "redirect:/jadwal/non-sidang/tambah";
        }
        jadwalNonSidangService.addJadwalNonSidang(jadwal);


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaService.getUserByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaService.getUserByUsername(username);
        }
        if (pengguna.getRole().getNama().equals("Admin Prodi") || pengguna.getRole().getNama().equals("Admin Fakultas")) {
            mailService.sendMailNonSidangByAdminProdi(jadwal);
        }

        model.addAttribute("jadwal", jadwal);
        redirectAttributes.addFlashAttribute("message", "Jadwal kegiatan berhasil ditambahkan.");

        return "redirect:/jadwal";
    }

    @GetMapping("/ubah/{id}")
    public String updateNonSidangFormPage(@PathVariable Integer id, Model model) {
        JadwalNonSidangModel nonsidang = jadwalNonSidangService.getJadwalNonSidangById(id);
        List<KeperluanModel> listKeperluan = keperluanService.getListKeperluan();
        List<DosenModel> listDosen = dosenService.getListDosen();
        model.addAttribute("listKeperluan", listKeperluan);
        model.addAttribute("listDosen", listDosen);
        model.addAttribute("jadwalNonSidangModel", nonsidang);

        return "form-update-non-sidang";
    }

    @PostMapping("/ubah")
    public String updateNonSidangSubmitPage(@ModelAttribute JadwalNonSidangModel jnsm, 
    RedirectAttributes redirectAttributes, Model model) throws MessagingException, ParseException {
        Integer idJadwal = jnsm.getId();

        if (!jadwalNonSidangService.cekJadwalKegiatan(jnsm)) {
            redirectAttributes.addFlashAttribute("message", "Perubahan tidak berhasil. Harap perbaiki waktu atau tanggal pengisian.");
            return "redirect:/jadwal/non-sidang/ubah/" + idJadwal;
        }
        jadwalNonSidangService.updateJadwal(idJadwal, jnsm);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaService.getUserByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaService.getUserByUsername(username);
        }
        if (pengguna.getRole().getNama().equals("Admin Prodi") || pengguna.getRole().getNama().equals("Admin Fakultas")) {
            mailService.sendMailUbahNonSidang(jnsm);
        }


        model.addAttribute("jadwal", jnsm);
        redirectAttributes.addFlashAttribute("message", "Jadwal kegiatan berhasil diubah.");

        return "redirect:/jadwal";
    }

    @RequestMapping("/delete/{id}")
    public String deleteJadwal(@PathVariable Integer id, RedirectAttributes redirectAttributes) throws MessagingException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaService.getUserByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaService.getUserByUsername(username);
        }
        if (pengguna.getRole().getNama().equals("Admin Prodi") || pengguna.getRole().getNama().equals("Admin Fakultas")) {
            mailService.sendMailHapusNonSidang(jadwalNonSidangService.getJadwalNonSidangById(id));
        }
        jadwalNonSidangService.deleteJadwal(id);
        redirectAttributes.addFlashAttribute("message", "Jadwal kegiatan berhasil dihapus.");

        return "redirect:/jadwal";
    }
}
