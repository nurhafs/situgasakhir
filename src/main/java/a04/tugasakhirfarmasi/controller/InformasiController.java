package a04.tugasakhirfarmasi.controller;

import a04.tugasakhirfarmasi.model.*;
import a04.tugasakhirfarmasi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/informasi")
public class InformasiController {
    @Qualifier("penggunaServiceImpl")
    @Autowired
    PenggunaService penggunaService;

    @Autowired
    private InformasiService informasiService;

    @GetMapping("/tambah")
    public String addInformasiFormPage(Model model) {
        InformasiModel informasi =  new  InformasiModel();
        model.addAttribute("informasi", informasi);

        return "form-add-informasi";

    }

    @RequestMapping(value = ("/tambah"), headers = ("content-type=multipart/*"))
    private String addInformasiSubmit(
            @ModelAttribute InformasiModel informasi,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "isS1") int isS1,
            @RequestParam(value = "isS2") int isS2,
            @RequestParam(value = "isS3") int isS3,
            @RequestParam(value = "isApoteker") int isApoteker,
            @RequestParam(value = "fileInformasi") MultipartFile fileInformasi,
            Model model) throws IOException
    {
        if(isS1==0){
            informasi.setS1(false);
        }
        else if(isS1==1){
            informasi.setS1(true);
        }

        if(isS2==0){
            informasi.setS2(false);
        }
        else if(isS2==1){
            informasi.setS2(true);
        }

        if(isS3==0){
            informasi.setS3(false);
        }
        else if(isS1==1){
            informasi.setS3(true);
        }

        if(isApoteker==0){
            informasi.setApoteker(false);
        }
        else if(isS1==1){
            informasi.setApoteker(true);
        }

        informasiService.addInformasi(fileInformasi, informasi);
        model.addAttribute("informasi", informasi);
        redirectAttributes.addFlashAttribute("message", "Informasi berhasil ditambahkan.");

        return "redirect:/informasi?page=1";
    }

    @GetMapping("/ubah/{id}")
    public String updateInformasiPage(@PathVariable Integer id, Model model) {
        InformasiModel informasi = informasiService.getInformasiById(id);

        model.addAttribute("informasi", informasi);
        return "form-update-informasi";
    }

    @PostMapping(value = "/ubah", headers = ("content-type=multipart/*"))
    public String updateInformasiPage(
        @ModelAttribute InformasiModel informasi,
        @RequestParam(value = "fileInformasi") MultipartFile fileInformasi,
        RedirectAttributes redirectAttributes,
        Model model ) throws MessagingException, IOException
        
    {
        Integer idInfo = informasi.getId();
        informasiService.updateInformasi(idInfo, informasi, fileInformasi);
        model.addAttribute("informasi", informasi);

        redirectAttributes.addFlashAttribute("message", "Informasi berhasil diubah.");
        return "redirect:/informasi?page=1";
    }

    @RequestMapping("/delete/{id}")
    public String deleteInformasi(@PathVariable Integer id,
                                  RedirectAttributes redirectAttributes
                                  ) throws MessagingException {
        informasiService.deleteInformasi(id);

        redirectAttributes.addFlashAttribute("message", "Informasi berhasil dihapus.");

        return "redirect:/informasi?page=1";
    }

    @GetMapping("/download/{idInformasi}")
    public ResponseEntity<byte[]> downloadInformasi (
        @PathVariable Integer idInformasi
    ) throws IOException {
        InformasiModel berkas = informasiService.getInformasiById(idInformasi);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + berkas.getNamaFile() + "\"")
                .body(berkas.getFile());
    }

}
