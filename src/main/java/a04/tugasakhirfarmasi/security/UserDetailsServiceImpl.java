package a04.tugasakhirfarmasi.security;

import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.repository.PenggunaDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private PenggunaDB penggunaDB;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PenggunaModel pengguna = penggunaDB.findByUsername(username);
        // Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // String password = ((UserDetails)principal).getPassword();

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(pengguna.getRole().getNama()));
        grantedAuthorities.add(new SimpleGrantedAuthority(pengguna.getProdiPengguna().getNama()));
        return new User(pengguna.getUsername(), pengguna.getPassword(), grantedAuthorities);
    }
}
