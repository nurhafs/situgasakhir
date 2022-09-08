package a04.tugasakhirfarmasi.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import a04.tugasakhirfarmasi.model.InformasiModel;
import a04.tugasakhirfarmasi.service.InformasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.reactive.function.client.WebClient;

import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.model.WhitelistPenggunaModel;
import a04.tugasakhirfarmasi.repository.ProgramStudiDB;
import a04.tugasakhirfarmasi.repository.RoleDB;
import a04.tugasakhirfarmasi.repository.WhitelistPenggunaDB;
import a04.tugasakhirfarmasi.rest.ServiceResponse;
import a04.tugasakhirfarmasi.service.PenggunaService;
import a04.tugasakhirfarmasi.setting.Setting;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {
    private final String SPRING_SECURITY_CONTEXT_KEY = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
    private final WebClient webClient;

    @Autowired
    private InformasiService informasiService;

    @Autowired
    RoleDB roleDB;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    ProgramStudiDB programStudiDB;

    @Autowired
    WhitelistPenggunaDB whitelistPenggunaDB;

    @Autowired
    private HttpSession httpSession;

    @Qualifier("penggunaServiceImpl")
    @Autowired
    PenggunaService penggunaService;

    public HomeController(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @GetMapping("/")
    public String HomepageAdmin(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        String username = String.valueOf(principal);

        PenggunaModel pengguna = penggunaService.getUserByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaService.getUserByUsername(username);
        }

        String role = pengguna.getRole().getNama();
        String prodi = pengguna.getProdiPengguna().getNama();
        String name = pengguna.getUsername();

        List<InformasiModel> listInformasi = informasiService.getAllInformasi(role, prodi);
        model.addAttribute("name", name);
        model.addAttribute("listInformasi", listInformasi);

        return "redirect:/informasi?page=1";
    }

    @RequestMapping("/login")
    public String login()  {
        return "login";
    }

    @GetMapping("/login-sso")
    public ModelAndView loginSSO() {
        return new ModelAndView("redirect:" + Setting.SERVER_LOGIN + Setting.CLIENT_LOGIN);
    }

    @GetMapping("/validate-ticket")
    public ModelAndView afterLoginSSO(
            @RequestParam(value = "ticket", required = false) String ticket,
            HttpServletRequest request,
            Model model
    ) {
        ServiceResponse response = webClient.get().uri(
                String.format(Setting.SERVER_VALIDATE_TICKET, ticket, Setting.CLIENT_LOGIN)
        ).retrieve().bodyToMono(ServiceResponse.class).block();
        System.out.println("NPM:");
        System.out.println(response.getAuthenticationSuccess().getAttributes().getNpm());
        PenggunaModel pengguna = penggunaService.getUserByUsername(response.getAuthenticationSuccess().getUser());
        if (pengguna == null) {
            pengguna = new PenggunaModel();
            pengguna.setUsername(response.getAuthenticationSuccess().getUser());
            pengguna.setPassword(response.getAuthenticationSuccess().getAttributes().getNpm());

            WhitelistPenggunaModel whitelistPengguna = whitelistPenggunaDB.findByUsername(response.getAuthenticationSuccess().getUser());
            if (whitelistPengguna != null) {
                pengguna.setRole(whitelistPengguna.getRoleWhitelistPengguna());
                if (whitelistPengguna.getRoleWhitelistPengguna().getNama().equals("Mahasiswa")) {
                    if (response.getAuthenticationSuccess().getAttributes().getKd_org().equals("") || response.getAuthenticationSuccess().getAttributes().getKd_org().equals("")) {
                        pengguna.setProdiPengguna(programStudiDB.getProgramStudiModelByNama("S1"));
                    } else if (response.getAuthenticationSuccess().getAttributes().getKd_org().equals("") || response.getAuthenticationSuccess().getAttributes().getKd_org().equals("")) {
                        pengguna.setProdiPengguna(programStudiDB.getProgramStudiModelByNama("S2"));
                    } else if (response.getAuthenticationSuccess().getAttributes().getKd_org().equals("")) {
                        pengguna.setProdiPengguna(programStudiDB.getProgramStudiModelByNama("S3"));
                    } else if (response.getAuthenticationSuccess().getAttributes().getKd_org().equals("")) {
                        pengguna.setProdiPengguna(programStudiDB.getProgramStudiModelByNama("Apt"));
                    } else {
                        pengguna.setProdiPengguna(programStudiDB.getProgramStudiModelByNama("S1"));
                    }
                } else if (whitelistPengguna.getRoleWhitelistPengguna().getNama().equals("Admin Prodi") || whitelistPengguna.getRoleWhitelistPengguna().getNama().equals("Admin Fakultas")) {
                    pengguna.setProdiPengguna(programStudiDB.getProgramStudiModelByNama("Admin"));
                } else if (whitelistPengguna.getRoleWhitelistPengguna().getNama().equals("Dosen")) {
                    pengguna.setProdiPengguna(programStudiDB.getProgramStudiModelByNama("Dosen"));
                }
            } else {
                String npm = response.getAuthenticationSuccess().getAttributes().getNpm();
                if (npm == null) {
                    pengguna.setRole(roleDB.getRoleModelByNama("Dosen"));
                    pengguna.setProdiPengguna(programStudiDB.getProgramStudiModelByNama("Dosen"));
                } else {
                    return new ModelAndView("redirect:/no-entry");
                }
                
            }
            penggunaService.addUser(pengguna, response.getAuthenticationSuccess().getAttributes().getNama());
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(pengguna.getRole().getNama()));
        grantedAuthorities.add(new SimpleGrantedAuthority(pengguna.getProdiPengguna().getNama()));

        Authentication auth = new UsernamePasswordAuthenticationToken(pengguna.getUsername(), pengguna.getPassword(), grantedAuthorities);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        // model.addAttribute("username", pengguna.getUsername());
        // return "login-web";
        System.out.println(auth.isAuthenticated());
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getName());
        return new ModelAndView("redirect:/informasi?page=1");
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);
        PenggunaModel pengguna = penggunaService.getUserByUsername(username);

        if (pengguna != null) {
            return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
        }
        return new ModelAndView("redirect:/log-out");
    }

    @GetMapping(value = "/informasi")
    public String listInfo(
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("page") Optional<Integer> page) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);

        PenggunaModel pengguna = penggunaService.getUserByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaService.getUserByUsername(username);
        }

        String role = pengguna.getRole().getNama();
        String name = getNama(role, pengguna);

        int currentPage = page.orElse(1);
        Integer previousPage = Integer.valueOf(currentPage-1);
        Integer nextPage = Integer.valueOf(currentPage+1);

        int pageSize = 3;

        Page<InformasiModel> infoPage = informasiService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("name", name);
        model.addAttribute("listInformasi", infoPage);

        int start;
        int totalPages = infoPage.getTotalPages();
        int end = totalPages;

        if (totalPages > 0 && currentPage <= totalPages) {
            if (totalPages < 5) {
                start = 1;
                end = totalPages;
            } else if (currentPage == totalPages - 1) {
                start = currentPage - 3;
                end = currentPage + 1;
            } else if (currentPage == totalPages) {
                start = end - 4;
                end = totalPages;
            } else {
                start = currentPage - 2;
                end = currentPage + 2;
            }

            List<Integer> pageNumbers = IntStream.rangeClosed(start , end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("previousPage", previousPage);
            model.addAttribute("nextPage", nextPage);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("currentPage", currentPage);
        } else {
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("message", "Belum ada informasi untuk saat ini.");
        }

        if (currentPage > totalPages) {
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("message", "Belum ada informasi untuk saat ini.");
        }

        return "homepage";
    }

    @GetMapping(value = "/no-entry")
    public String noEntry(Model model) {
        model.addAttribute("server_logout", Setting.SERVER_LOGOUT);
        model.addAttribute("client_logout", Setting.CLIENT_LOGOUT);
        return "no-entry";
    }

    public String getNama(String role, PenggunaModel pengguna) {
        String nama = "";
        if (role.equals("Mahasiswa")) {
            nama = pengguna.getMahasiswa().getUsername();
        } else if (role.equals("Admin Prodi") || role.equals("Admin Fakultas")) {
            nama = pengguna.getUsername();
        } else if (role.equals("Dosen")){
            nama = pengguna.getDosen().getNama();
        }
        return nama;
    }
}
