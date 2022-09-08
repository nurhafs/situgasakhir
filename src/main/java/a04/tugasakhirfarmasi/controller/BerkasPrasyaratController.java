package a04.tugasakhirfarmasi.controller;

import a04.tugasakhirfarmasi.service.MailService;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.result.view.RedirectView;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import java.time.LocalDate;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import a04.tugasakhirfarmasi.model.BerkasPrasyaratContainer;
import a04.tugasakhirfarmasi.model.BerkasPrasyaratModel;
import a04.tugasakhirfarmasi.model.JenisBerkasModel;
import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.model.ProgramStudiModel;
import a04.tugasakhirfarmasi.repository.BerkasPrasyaratDB;
import a04.tugasakhirfarmasi.repository.JenisBerkasDB;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import a04.tugasakhirfarmasi.repository.StatusDB;
import a04.tugasakhirfarmasi.service.BerkasPrasyaratService;
import a04.tugasakhirfarmasi.service.JenisBerkasService;
import a04.tugasakhirfarmasi.service.ProgramStudiService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Controller
@ControllerAdvice
public class BerkasPrasyaratController {
    int a = 0;
    @Qualifier("berkasPrasyaratServiceImpl")
    @Autowired
    BerkasPrasyaratService berkasPrasyaratService;

    @Qualifier("programStudiServiceImpl")
    @Autowired
    ProgramStudiService programStudiService;

    @Qualifier("jenisBerkasServiceImpl")
    @Autowired
    JenisBerkasService jenisBerkasService;

    @Autowired
    BerkasPrasyaratDB berkasPrasyaratDB;

    @Autowired
    JenisBerkasDB jenisBerkasDB;

    @Autowired
    PenggunaDB penggunaDB;

    @Autowired
    MailService mailService;

    @GetMapping("/tugasakhir")
    private String viewallBerkasMahasiswa(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaDB.findByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaDB.findByUsername(username);
        }

        model.addAttribute("pengguna", pengguna);
        if (pengguna.getMahasiswa().getListBerkasPrasyarat() != null) {
            model.addAttribute("listBerkas", pengguna.getMahasiswa().getListBerkasPrasyarat());
        }
        return "viewall-berkas-mahasiswa";
    }

    @GetMapping("/tugasakhir/berkas/{id}")
    private String addBerkasForm (
        @PathVariable Integer id,
        Model model
    ) {
        model.addAttribute("idBerkas", id);
        return "form-add-berkas";
    }

    @GetMapping("/tugasakhir/hapus-berkas/{idBerkas}")
    private String deleteBerkasForm (
            @PathVariable Integer idBerkas,
            RedirectAttributes redirectAttributes,
            Model model
    ) throws IOException {
        BerkasPrasyaratModel berkas = berkasPrasyaratService.getBerkasById(idBerkas);
        String pesan = "Berkas untuk tipe berkas " + berkas.getJenisBerkas().getNama() + " berhasil dihapus!";

        berkasPrasyaratService.deleteBerkas(berkas);
        redirectAttributes.addFlashAttribute("message", pesan);

        return "redirect:/tugasakhir";
    }

    @RequestMapping(value=("/tugasakhir/berkas"), headers=("content-type=multipart/*"))
    public ModelAndView addBerkasPost(
            @RequestParam(value = "file") MultipartFile fileBerkas,
            @RequestParam(value = "idBerkas") Integer idBerkas,
            HttpServletRequest httpReq,
            RedirectAttributes attributes
    ) throws MessagingException, IOException {
        ModelAndView mav;
        httpReq.setAttribute("idBerkas", idBerkas);
        a = idBerkas;
        System.out.println("a " + fileBerkas.getSize());
        try {
            if(fileBerkas.getSize() > 30728792) {
                mav = new ModelAndView("redirect:/tugasakhir/berkas/" + idBerkas);
                attributes.addFlashAttribute("alert", "PERINGATAN: Maksimum ukuran berkas yang bisa diunggah adalah 30MB!");
            } else {
                berkasPrasyaratService.saveFile(fileBerkas, idBerkas);
                mav = new ModelAndView("redirect:/tugasakhir");
            }
        } catch (IOException e) {
            mav = new ModelAndView("redirect:/tugasakhir/berkas/" + idBerkas);
            attributes.addFlashAttribute("alert", "PERINGATAN: Maksimum ukuran berkas yang bisa diunggah adalah 30MB!");
        }
        return mav;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadError(
            RedirectAttributes attributes
    ) {
        attributes.addFlashAttribute("alert", "PERINGATAN: Maksimum ukuran berkas yang bisa diunggah adalah 30MB!");
        return "redirect:/tugasakhir/berkas/" + String.valueOf(a);
    }

    @GetMapping("/tugasakhir/berkas/download/{idMahasiswa}/{idJenisBerkas}")
    public ResponseEntity<byte[]> download(
        @PathVariable Integer idMahasiswa,
        @PathVariable Integer idJenisBerkas
    ) throws IOException {
        BerkasPrasyaratModel berkas = berkasPrasyaratService.getBerkasPrasyaratByMahasiswaAndJenisBerkas(idMahasiswa, idJenisBerkas);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + berkas.getNama() + "\"")
                .body(berkas.getFile());
    }

    @GetMapping("/tugasakhir/berkas/viewall")
    public String viewallBerkasAdmin(
        @RequestParam(value = "jenisBerkas", required = false) Integer jenisBerkas,
        @RequestParam(value = "programStudi", required = false) Integer programStudi,
        @RequestParam(value = "checked", defaultValue = "false") Boolean checked,
        Model model
    ) {
        List<BerkasPrasyaratModel> listBerkas = new ArrayList<>();
        if (jenisBerkas != null && programStudi == null) {
            listBerkas = berkasPrasyaratService.getListBerkasPrasyaratByJenisBerkas(jenisBerkas);
        } else if (jenisBerkas == null && programStudi != null) {
            listBerkas = berkasPrasyaratService.getListBerkasPrasyaratByProgramStudi(programStudi);
        } else if (jenisBerkas != null && programStudi != null) {
            listBerkas = berkasPrasyaratService.getListBerkasPrasyaratByJenisBerkasAndProgramStudi(jenisBerkas, programStudi);
        } else {
            listBerkas = berkasPrasyaratService.getListBerkasPrasyaratNotNull();
        }

        List<BerkasPrasyaratModel> butuhApproval = new ArrayList<>();
        List<ProgramStudiModel> listProdi = programStudiService.getListProgramStudiExceptDosenAdmin();
        List<JenisBerkasModel> listJenisBerkas = jenisBerkasService.getListJenisBerkas();
        BerkasPrasyaratContainer bpc = new BerkasPrasyaratContainer();
        for (BerkasPrasyaratModel brks : listBerkas) {
            if (!checked) {
                if (brks.getStatusBerkas().getId().equals(2)) {
                    butuhApproval.add(brks);
                }
            }
            else {
                if (brks.getStatusBerkas().getId().equals(1) || brks.getStatusBerkas().getId().equals(5)) {
                    butuhApproval.add(brks);
                }
            }
        }
        bpc.setSemua(butuhApproval);
        model.addAttribute("listJenisBerkas", listJenisBerkas);
        model.addAttribute("listProdi", listProdi);
        model.addAttribute("listBerkas", bpc.getSemua());
        model.addAttribute("berkasContainer", bpc);
        model.addAttribute("checked", checked);
        return "viewall-berkas-admin";
    }

    @PostMapping("/tugasakhir/berkas/setuju")
    public String approveBerkas(
        @ModelAttribute BerkasPrasyaratContainer bpc,
        RedirectAttributes redirectAttributes,
        Model model) throws MessagingException {
            Integer counter = 0;
            for (BerkasPrasyaratModel bpm: bpc.getSemua()) {
                berkasPrasyaratService.ubahStatus(bpm.getId(), 1);
                mailService.sendMailUbahStatusBerkas(bpm);
                counter++;
            }
            redirectAttributes.addFlashAttribute("message", (counter + " berkas berhasil disetujui."));
            return "redirect:/tugasakhir/berkas/viewall";
        }

    @PostMapping("/tugasakhir/berkas/tolak")
    public String rejectBerkas(
        @ModelAttribute BerkasPrasyaratContainer bpc,
        RedirectAttributes redirectAttributes,
        Model model) throws MessagingException {
            Integer counter = 0;
            for (BerkasPrasyaratModel bpm: bpc.getSemua()) {
                berkasPrasyaratService.ubahStatus(bpm.getId(), 5);
                mailService.sendMailUbahStatusBerkas(bpm);
                counter++;
            }
            redirectAttributes.addFlashAttribute("message", (counter + " berkas berhasil ditolak."));
            return "redirect:/tugasakhir/berkas/viewall";
        }

}