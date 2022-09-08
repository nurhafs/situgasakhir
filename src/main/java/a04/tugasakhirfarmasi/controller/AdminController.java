package a04.tugasakhirfarmasi.controller;

import a04.tugasakhirfarmasi.bean.SidangPDFExporter;
import a04.tugasakhirfarmasi.model.*;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import a04.tugasakhirfarmasi.repository.PeriodeDB;
import a04.tugasakhirfarmasi.service.DosenService;
import a04.tugasakhirfarmasi.service.LamaranService;
import a04.tugasakhirfarmasi.service.MahasiswaService;
import a04.tugasakhirfarmasi.bean.LamaranPDFExporter;
import a04.tugasakhirfarmasi.bean.LamaranXLSExporter;
import a04.tugasakhirfarmasi.bean.MahasiswaXLSExporter;
import a04.tugasakhirfarmasi.service.MailService;
import a04.tugasakhirfarmasi.service.ProgramStudiService;
import a04.tugasakhirfarmasi.service.SidangService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Qualifier("lamaranServiceImpl")
    @Autowired
    LamaranService lamaranService;

    @Qualifier("sidangServiceImpl")
    @Autowired
    SidangService sidangService;

    @Qualifier("mahasiswaServiceImpl")
    @Autowired
    MahasiswaService mahasiswaService;

    @Qualifier("dosenServiceImpl")
    @Autowired
    DosenService dosenService;

    @Qualifier("programStudiServiceImpl")
    @Autowired
    ProgramStudiService programStudiService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PenggunaDB penggunaDB;

    @Autowired
    private PeriodeDB periodeDB;

    @GetMapping("/profil")
    private String profilAdmin (Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaDB.findByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaDB.findByUsername(username);
        }
        AdminModel admin = pengguna.getAdmin();
        model.addAttribute("admin", admin);

        return "profil-admin";
    }

    @GetMapping("/daftar-lamaran")
    private String viewAllLamaran(
        @RequestParam(value = "namaDosen", required = false) Integer namaDosen,
        @RequestParam(value = "programStudi", required = false) Integer programStudi,
        Model model
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaDB.findByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaDB.findByUsername(username);
        }
        String role = pengguna.getRole().getNama();
        List<LamaranModel> listLamaran = new ArrayList<>();
        if (role.equals("Admin Prodi")) {
        // if (role.equals("Admin Prodi")) { ganti sblm commit push
            listLamaran = lamaranService.getLamaranByStatus(2);
        } else if (role.equals("Admin Fakultas")) {
            listLamaran = lamaranService.getLamaranByStatus(3);
        }
        listLamaran = lamaranService.getAllLamaranFiltered(listLamaran, namaDosen, programStudi);

        List<ProgramStudiModel> listProdi = programStudiService.getListProgramStudiExceptDosenAdmin();
        List<DosenModel> listDosen = dosenService.getListDosbing();
        LamaranContainer lc = new LamaranContainer();
        lc.setSemua(listLamaran);;

        model.addAttribute("listProdi", listProdi);
        model.addAttribute("listDosen", listDosen);
        model.addAttribute("listLamaran", lc.getSemua());
        model.addAttribute("cont", lc);
        model.addAttribute("choseNone", false);
        return "list-lamaran";
    }

    @PostMapping("/setuju")
    private String approveLamaran(@ModelAttribute LamaranContainer lc, Model model) throws MessagingException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaDB.findByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaDB.findByUsername(username);
        }
        String role = pengguna.getRole().getNama();
        List<LamaranModel> berhasil = new ArrayList<LamaranModel>();
        List<LamaranModel> gagal = new ArrayList<LamaranModel>();
        if (role.equals("Admin Prodi")) {
            for (LamaranModel mdl : lc.getSemua()) {
                Integer kuota = lamaranService.countKuotaMahasiswa(mdl.getLamaranDosen());
                if (kuota != 0) {
                    lamaranService.updateStatus(mdl, 3);
                    berhasil.add(mdl);
                    mailService.sendMailUbahStatusLamaran(mdl);
                } else {
                    gagal.add(mdl);
                    lamaranService.updateStatus(mdl, 5);
                    mailService.sendMailUbahStatusLamaran(mdl);
                }
            }
            if (berhasil.size() == 0 && gagal.size() == 0) {
                return "noentry-page";
            }
        }
        else if (role.equals("Admin Fakultas")) {
            for (LamaranModel mdl : lc.getSemua()) {
                Integer kuota = lamaranService.countKuotaMahasiswa(mdl.getLamaranDosen());
                if (kuota != 0) {
                    lamaranService.updateStatus(mdl, 1);
                    lamaranService.updateJumlahMahasiswa(mdl.getLamaranDosen());
                    lamaranService.updateJumlahLamaran(mdl.getLamaranDosen());
                    berhasil.add(mdl);
                    mailService.sendMailUbahStatusLamaran(mdl);
                } else {
                    gagal.add(mdl);
                    lamaranService.updateStatus(mdl, 5);
                    mailService.sendMailUbahStatusLamaran(mdl);
                }
            }
            if (berhasil.size() == 0 && gagal.size() == 0) {
                return "noentry-page";
            }
        }
        
        model.addAttribute("berhasil", berhasil);
        model.addAttribute("gagal", gagal);
        model.addAttribute("role", role);
        return "approve-page";
    }

    @PostMapping("/tolak")
    private String rejectLamaran(@ModelAttribute LamaranContainer lc, Model model) throws MessagingException {
        if (lc.getSemua().size() == 0) {
            return "noentry-page";
        }
        for (LamaranModel mdl : lc.getSemua()) {
            lamaranService.updateStatus(mdl, 5);
            lamaranService.updateJumlahLamaran(mdl.getLamaranDosen());
            mailService.sendMailUbahStatusLamaran(mdl);
        }
        return "reject-page";
    }

    @GetMapping("/laporan")
    public String laporanViewall(Model model) {
        List<PeriodeModel> listPeriode = periodeDB.findAll();
        model.addAttribute("listPeriode", listPeriode);
        return "viewall-laporan";
    }

    @GetMapping("/lamaran/laporan")
    public void exportLamaranToPDF(HttpServletResponse response, @RequestParam(value = "periodeLamaran", required = false) String periodeLamaran) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=lamaran_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<LamaranModel> listLamaran = lamaranService.getAllLamaranByPeriode(periodeLamaran);

        LamaranXLSExporter exporter = new LamaranXLSExporter(listLamaran);
        // LamaranPDFExporter exporter = new LamaranPDFExporter(listLamaran);
        exporter.export(response);
    }

    @GetMapping("/mahasiswa/laporan")
    public void exportMahasiswaToXLSX(HttpServletResponse response, @RequestParam(value = "periodeMahasiswa", required = false) String periodeMahasiswa) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=mahasiswa_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<MahasiswaModel> listMahasiswa = mahasiswaService.getAllMahasiswaByPeriode(periodeMahasiswa);

        MahasiswaXLSExporter exporter = new MahasiswaXLSExporter(listMahasiswa);
        // LamaranPDFExporter exporter = new LamaranPDFExporter(listLamaran);
        exporter.export(response);
    }
}