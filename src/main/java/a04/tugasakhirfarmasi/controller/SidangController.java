package a04.tugasakhirfarmasi.controller;

import a04.tugasakhirfarmasi.bean.SidangPDFExporter;
import a04.tugasakhirfarmasi.bean.SidangXLSExporter;
import a04.tugasakhirfarmasi.model.*;
import a04.tugasakhirfarmasi.repository.RolePartisipanSidangDB;
import a04.tugasakhirfarmasi.service.*;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/jadwal/sidang")
public class SidangController {
    @Autowired
    private SidangService sidangService;

    @Autowired
    private MahasiswaService mahasiswaService;

    @Autowired
    private DosenService dosenService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PartisipanSidangService partisipanSidangService;

    @Autowired
    RolePartisipanSidangDB rolePartisipanSidangDB;

    private int incrementCounter = 1;

    // @GetMapping("/tambah")
    // public String addSidangFormPage(Model model) {
    //     SidangModel sidang = new SidangModel();
    //     ArrayList<JenisSidangModel> listJenisSidang = (ArrayList<JenisSidangModel>) sidangService.getListJenisSidang();
    //     ArrayList<MahasiswaModel> listMahasiswa = (ArrayList<MahasiswaModel>) mahasiswaService.getListMahasiswa();
    //     ArrayList<DosenModel> listDosen = (ArrayList<DosenModel>) dosenService.getListAllDosen();
    //     ArrayList<PartisipanSidangModel> listPartisipanSidangBefore = (ArrayList<PartisipanSidangModel>) sidangService.getListPartisipanSidang();
    //     sidangService.convertDosenToPartisipanSidang(listDosen, listPartisipanSidangBefore);
    //     ArrayList<PartisipanSidangModel> listPartisipanSidangUpdated = (ArrayList<PartisipanSidangModel>) sidangService.getListPartisipanSidang();
    //     ArrayList<PartisipanSidangModel> listPartisipanSidang = new ArrayList<PartisipanSidangModel>();
    //     model.addAttribute("listJenisSidang", listJenisSidang);
    //     model.addAttribute("listMahasiswa", listMahasiswa);
    //     model.addAttribute("existingListPartisipanSidang", listPartisipanSidangUpdated);
    //     model.addAttribute("listPartisipanSidang", listPartisipanSidang);
    //     model.addAttribute("sidang", sidang);
    //     return "form-add-sidang";
    // }

    @GetMapping("/tambah")
    public String addSidangForm(Model model) {
        SidangModel sidang = new SidangModel();
        List<JenisSidangModel> listJenisSidang = sidangService.getListJenisSidang();
        List<MahasiswaModel> listMahasiswa = mahasiswaService.getListMahasiswa();
        List<DosenModel> listDosen = dosenService.getListAllDosen();
        List<RolePartisipanSidangModel> listRolePartisipanSidang = rolePartisipanSidangDB.findAll();
        incrementCounter = 1;

        model.addAttribute("counter", incrementCounter);
        model.addAttribute("listJenisSidang", listJenisSidang);
        model.addAttribute("listMahasiswa", listMahasiswa);
        model.addAttribute("listDosen", listDosen);
        model.addAttribute("sidang", sidang);
        model.addAttribute("listRolePartisipanSidang", listRolePartisipanSidang);
        return "form-add-sidang";
    }

    @GetMapping(value = "/tambah/tambah-partisipan")
    public String addSidangFormAddRow(Model model) {
        SidangModel sidang = new SidangModel();
        List<JenisSidangModel> listJenisSidang = sidangService.getListJenisSidang();
        List<MahasiswaModel> listMahasiswa = mahasiswaService.getListMahasiswa();
        List<DosenModel> listDosen = dosenService.getListAllDosen();
        List<RolePartisipanSidangModel> listRolePartisipanSidang = rolePartisipanSidangDB.findAll();
        incrementCounter += 1;

        model.addAttribute("counter", incrementCounter);
        model.addAttribute("listJenisSidang", listJenisSidang);
        model.addAttribute("listMahasiswa", listMahasiswa);
        model.addAttribute("listDosen", listDosen);
        model.addAttribute("sidang", sidang);
        model.addAttribute("listRolePartisipanSidang", listRolePartisipanSidang);
        return "form-add-sidang";
    }

    @GetMapping(value = "/tambah/kurang-partisipan")
    public String addSidangFormRemoveRow(Model model) {
        SidangModel sidang = new SidangModel();
        List<JenisSidangModel> listJenisSidang = sidangService.getListJenisSidang();
        List<MahasiswaModel> listMahasiswa = mahasiswaService.getListMahasiswa();
        List<DosenModel> listDosen = dosenService.getListAllDosen();
        List<RolePartisipanSidangModel> listRolePartisipanSidang = rolePartisipanSidangDB.findAll();
        if (incrementCounter != 1) {
            incrementCounter -= 1;
        }

        model.addAttribute("counter", incrementCounter);
        model.addAttribute("listJenisSidang", listJenisSidang);
        model.addAttribute("listMahasiswa", listMahasiswa);
        model.addAttribute("listDosen", listDosen);
        model.addAttribute("sidang", sidang);
        model.addAttribute("listRolePartisipanSidang", listRolePartisipanSidang);
        return "form-add-sidang";
    }

    // @PostMapping (value="/tambah", params= {"addRow"})
    // public String addRow(@ModelAttribute SidangModel sidang, BindingResult bindingResult, Model model) {
    //     if (sidang.getListPartisipanSidang() == null) {
    //         sidang.setListPartisipanSidang(new ArrayList<PartisipanSidangModel>());
    //     }
    //     ArrayList<JenisSidangModel> listJenisSidang = (ArrayList<JenisSidangModel>) sidangService.getListJenisSidang();
    //     ArrayList<MahasiswaModel> listMahasiswa = (ArrayList<MahasiswaModel>) mahasiswaService.getListMahasiswa();
    //     ArrayList<DosenModel> listDosen = (ArrayList<DosenModel>) dosenService.getListAllDosen();
    //     ArrayList<PartisipanSidangModel> listPartisipanSidang = (ArrayList<PartisipanSidangModel>) sidangService.getListPartisipanSidang();
    //     sidangService.convertDosenToPartisipanSidang(listDosen, listPartisipanSidang);
    //     ArrayList<PartisipanSidangModel> listPartisipanSidangUpdated = (ArrayList<PartisipanSidangModel>) sidangService.getListPartisipanSidang();
    //     sidang.getListPartisipanSidang().add(new PartisipanSidangModel());
    //     model.addAttribute("listJenisSidang", listJenisSidang);
    //     model.addAttribute("listMahasiswa", listMahasiswa);
    //     model.addAttribute("existingListPartisipanSidang", listPartisipanSidangUpdated);
    //     model.addAttribute("sidang", sidang);
    //     return "form-add-sidang";
    // }

    // @PostMapping(value="/tambah", params={"removeRow"})
    // public String removeRow(@ModelAttribute SidangModel sidang, final BindingResult bindingResult, final HttpServletRequest req, Model model) {
    //     final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
    //     sidang.getListPartisipanSidang().remove(rowId.intValue());

    //     model.addAttribute("sidang", sidang);
    //     return "form-add-sidang";
    // }

    @RequestMapping(value="/tambah", method = RequestMethod.POST, params={"save"})
    public String addSidangSubmitPage(
        @ModelAttribute SidangModel sidang,
        @RequestParam(value = "dosenId") Integer[] dosenPartisipan,
        @RequestParam(value = "rolePartisipanSidangId") Integer[] rolePartisipan,
        RedirectAttributes redirectAttributes,
        Model model
    ) throws MessagingException, ParseException {
        boolean isSidangValid = sidangService.isSidangValid(sidang, dosenPartisipan);
        if (!isSidangValid) {
            redirectAttributes.addFlashAttribute("message", "Jadwal sidang tidak berhasil ditambah karena ada jadwal lain yang bentrok.");
            return "redirect:/jadwal";
        }
        sidang = sidangService.addSidang(sidang);
        for (int i = 0; i < dosenPartisipan.length; i++) {
            partisipanSidangService.addPartisipanSidang(sidang, dosenPartisipan[i], rolePartisipan[i]);
        }
        model.addAttribute("sidang", sidang);

        redirectAttributes.addFlashAttribute("message", "Jadwal sidang berhasil ditambah.");

        //Kirim Mail
        mailService.sendMailSidang(sidang);

        return "redirect:/jadwal";
    }

    @GetMapping("/ubah/{id}")
    public String updateSidangForm (
        @PathVariable Integer id,
        Model model
    ) {
        SidangModel sidang = sidangService.getSidangById(id);
        model.addAttribute("sidang", sidang);
        return "form-ubah-sidang";
    }

    @PostMapping("/ubah")
    public String updateSidangSubmit (
        @ModelAttribute SidangModel sidang,
        RedirectAttributes redirectAttributes,
        Model model
    ) throws MessagingException, ParseException {
        Integer[] dosenPartisipan = new Integer[sidang.getListPartisipanSidang().size()];
        for (int i = 0; i < sidang.getListPartisipanSidang().size(); i++) {
            dosenPartisipan[i] = sidang.getListPartisipanSidang().get(i).getPartisipanSidangDosen().getId();
        }
        boolean isSidangValid = sidangService.isSidangValid(sidang, dosenPartisipan);
        if (!isSidangValid) {
            model.addAttribute("idSidang", sidang.getId());
            redirectAttributes.addFlashAttribute("message", "Jadwal sidang tidak berhasil diubah karena ada jadwal lain yang bentrok.");
            return "redirect:/jadwal";
        }
        sidangService.updateSidang(sidang);
        model.addAttribute("idSidang", sidang.getId());
        redirectAttributes.addFlashAttribute("message", "Jadwal sidang berhasil diubah.");

        mailService.sendMailUbahSidang(sidang);

        return "redirect:/jadwal";
//        return "update-sidang";
    }

    @PostMapping("/hapus/{id}")
    public String deleteSidangSubmit (
        @PathVariable Integer id,
        RedirectAttributes redirectAttributes,
        Model model
    ) throws MessagingException, ParseException {
        SidangModel sidang = sidangService.getSidangById(id);
        mailService.sendMailHapusSidang(sidang);
        sidangService.deleteSidang(sidang);
        redirectAttributes.addFlashAttribute("message", "Jadwal sidang berhasil dihapus.");
        return "redirect:/jadwal";
//        return "delete-sidang";
    }

    @RequestMapping("/detail/{id}")
    public String viewDetailSidangPage(
            @PathVariable(value = "id") Integer id,
            Model model
    ) {
        SidangModel sidang = sidangService.getSidangById(id);
        List<RequestUbahSidangModel> listRequestUbahSidang = sidang.getListRequestUbahSidangModel();
        List<PartisipanSidangModel> listPartisipanSidang = sidang.getListPartisipanSidang();

        model.addAttribute("sidang", sidang);
        model.addAttribute("listRequestUbahSidang", listRequestUbahSidang);
        model.addAttribute("listPartisipanSidang", listPartisipanSidang);

        return "detail-sidang";
    }

    @GetMapping("/laporan")
    public void exportSidangToPDF(HttpServletResponse response, @RequestParam(value = "periodeSidang", required = false) String periodeSidang) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=sidang_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<SidangModel> listSidang = sidangService.getAllSidangByPeriode(periodeSidang);

        // SidangPDFExporter exporter = new SidangPDFExporter(listSidang);
        SidangXLSExporter exporter = new SidangXLSExporter(listSidang);
        exporter.export(response);
    }

}
