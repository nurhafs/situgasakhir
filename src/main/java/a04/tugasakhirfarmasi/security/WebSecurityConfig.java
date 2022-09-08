package a04.tugasakhirfarmasi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/no-entry").permitAll()
                .antMatchers("/img/makara-ui-farmasi.png").permitAll()
                .antMatchers("/login-sso", "/validate-ticket").permitAll()
                .antMatchers("/tugasakhir").hasAuthority("Mahasiswa")
                .antMatchers("/tugasakhir/berkas/1", "/tugasakhir/berkas/3", "/tugasakhir/berkas/4", "/tugasakhir/berkas/5").hasAuthority("Mahasiswa")
                .antMatchers("/tugasakhir/berkas/1", "/tugasakhir/berkas/3", "/tugasakhir/berkas/4", "/tugasakhir/berkas/5").hasAuthority("S2")
                .antMatchers("/tugasakhir/berkas/2").hasAuthority("Mahasiswa").antMatchers("/tugasakhir/berkas/2").hasAnyAuthority("S1", "S2")
                .antMatchers("/tugasakhir/berkas/6").hasAuthority("Mahasiswa")
                .antMatchers("/tugasakhir/berkas/6").hasAuthority("S1")
                .antMatchers("/tugasakhir/berkas/7", "/tugasakhir/berkas/8", "/tugasakhir/berkas/9", "/tugasakhir/berkas/10", "/tugasakhir/berkas/11", "/tugasakhir/berkas/12", "/tugasakhir/berkas/13", "/tugasakhir/berkas/14").hasAuthority("Mahasiswa")
                .antMatchers("/tugasakhir/berkas/7", "/tugasakhir/berkas/8", "/tugasakhir/berkas/9", "/tugasakhir/berkas/10", "/tugasakhir/berkas/11", "/tugasakhir/berkas/12", "/tugasakhir/berkas/13", "/tugasakhir/berkas/14").hasAuthority("S3")
                .antMatchers("/tugasakhir/berkas/viewall").hasAnyAuthority("Admin Prodi", "Admin Fakultas", "Dosen")
                .antMatchers("/tugasakhir/berkas/download/**").hasAnyAuthority("Admin Prodi", "Admin Fakultas", "Dosen")
                .antMatchers("/dosen/daftar-dosbing").hasAuthority("Mahasiswa")
                .antMatchers("/admin/**").hasAnyAuthority("Admin Prodi", "Admin Fakultas")
                .antMatchers("/dosen/tambah").hasAnyAuthority("Admin Prodi", "Admin Fakultas")
                .antMatchers("/dosen/ubah/**").hasAnyAuthority("Admin Prodi", "Admin Fakultas")
                .antMatchers("/dosen/detail/**").hasAnyAuthority("Admin Prodi", "Admin Fakultas", "Dosen")
                .antMatchers("/jadwal").hasAnyAuthority("Admin Prodi", "Admin Fakultas", "Dosen")
                .antMatchers("/jadwal/**").hasAnyAuthority("Admin Prodi", "Admin Fakultas", "Dosen")
                .antMatchers("/mahasiswa").hasAnyAuthority("Admin Prodi", "Admin Fakultas")
                .antMatchers("/mahasiswa/detail/**").hasAnyAuthority("Admin Prodi", "Admin Fakultas", "Dosen")
                .antMatchers("/informasi/tambah").hasAnyAuthority("Admin Prodi", "Admin Fakultas")
                .antMatchers("/informasi/ubah/**").hasAnyAuthority("Admin Prodi", "Admin Fakultas")
                .antMatchers("/informasi/delete/**").hasAnyAuthority("Admin Prodi", "Admin Fakultas")
                .antMatchers("/mahasiswa/profil").hasAuthority("Mahasiswa")
                .antMatchers(HttpMethod.GET, "/dosen/daftar-dosbing/**").denyAll()
                .antMatchers(HttpMethod.GET, "/tugasakhir/berkas/**").denyAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/log-out")).logoutSuccessUrl("/login").permitAll()
                .and()
                .cors()
                .and()
                .csrf()
                .disable();
    }

   @Autowired
   private UserDetailsService userDetailsService;

   @Autowired
   public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
       BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
       auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
   }

//     @Bean
//     DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//         return daoAuthenticationProvider;


//      @Autowired
//      public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//          BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//          auth.inMemoryAuthentication()
//                  .passwordEncoder(encoder)
//                  .withUser("mahasiswa").password(encoder.encode("mahasiswa123"))
//                  .roles("Mahasiswa");
//          auth.inMemoryAuthentication()
//                  .passwordEncoder(encoder)
//                  .withUser("dosen").password(encoder.encode("dosen123"))
//                  .roles("Dosen");
//          auth.inMemoryAuthentication()
//                  .passwordEncoder(encoder)
//                  .withUser("adminfakultas").password(encoder.encode("adminfakultas123"))
//                  .roles("Admin Fakultas");
//          auth.inMemoryAuthentication()
//                  .passwordEncoder(encoder)
//                  .withUser("adminprodi").password(encoder.encode("adminprodi123"))
//                  .roles("Admin Prodi");
//          auth.inMemoryAuthentication()
//                  .passwordEncoder(encoder)
//                  .withUser("alif").password(encoder.encode("alif"))
//                  .roles("Mahasiswa");
//     }

}
