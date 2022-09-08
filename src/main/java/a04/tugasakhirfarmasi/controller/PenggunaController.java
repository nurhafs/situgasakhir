package a04.tugasakhirfarmasi.controller;

import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.model.ProgramStudiModel;
import a04.tugasakhirfarmasi.model.RoleModel;
import a04.tugasakhirfarmasi.service.PenggunaService;
import a04.tugasakhirfarmasi.service.ProgramStudiService;
import a04.tugasakhirfarmasi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class PenggunaController {
    @Autowired
    private PenggunaService penggunaService;

    @Autowired
    private RoleService roleService;

    @Qualifier("programStudiServiceImpl")
    @Autowired
    private ProgramStudiService programStudiService;

    @GetMapping("/add")
    private String addUserFormPage(Model model){
        PenggunaModel user = new PenggunaModel();
        List<RoleModel> listRole = roleService.getListRole();
        List<ProgramStudiModel> listProdi = programStudiService.getListProgramStudi();
        model.addAttribute("listProdi", listProdi);
        model.addAttribute("user", user);
        model.addAttribute("listRole", listRole);
        return "form-add-user";
    }

    @PostMapping(value = "/add")
    private String addUserSubmit(
            @ModelAttribute PenggunaModel user,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (penggunaService.getUserByUsername(user.getUsername()) == null) {
            penggunaService.addUser(user, user.getUsername());
            model.addAttribute("user", user);
            redirectAttributes.addFlashAttribute("message", "User berhasil ditambahkan.");
        } else {
            redirectAttributes.addFlashAttribute("message", "User gagal ditambahkan karena username telah dipakai user lain. Silakan coba lagi.");
        }
        return "redirect:/user/view-all";
    }

    @GetMapping("/view-all")
    private String getUsers(Model model) {
        List<PenggunaModel> listUser = penggunaService.getListPengguna();
        model.addAttribute("listUser", listUser);
        return "viewall-user";
    }

    @GetMapping("/edit-password")
    private String editPassword(Model model) {
        return "form-edit-password";
    }

    @PostMapping("/edit-password")
    private String processResetPassword(HttpServletRequest request, Model model) {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword1 = request.getParameter("newPassword1");
        String newPassword2 = request.getParameter("newPassword2");

        PenggunaModel pengguna = getUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        List<String> alert = new ArrayList<>();
        boolean flag = true;

        if (encoder.matches(oldPassword, pengguna.getPassword()) == false) {
            alert.add("Masukkan password lama yang tepat!");
            flag = false;
        }
        if (newPassword1.equals(newPassword2) == false) {
            alert.add("Password baru dengan konfirmasi password tidak sesuai!");
            flag = false;
        }
        if (newPassword1.length() < 8) {
            alert.add("Password harus lebih dari 8 karakter!");
            flag = false;
        }
        if (!digitCasePatten.matcher(newPassword1).find()) {
            alert.add("Password harus mengandung setidaknya 1 angka!");
            flag = false;
        }

        if (!flag) {
            model.addAttribute("alerts", alert);
            model.addAttribute("alertSize", alert.size()>0);
            return "form-edit-password";
        } else {
            penggunaService.updatePassword(pengguna, newPassword1);
            model.addAttribute("message", "Anda telah berhasil mengubah password anda!");
            return "edit-password";
        }
    }

    private PenggunaModel getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)auth.getPrincipal();
        PenggunaModel pengguna = penggunaService.getUserByUsername(user.getUsername());
        return pengguna;
    }
}
