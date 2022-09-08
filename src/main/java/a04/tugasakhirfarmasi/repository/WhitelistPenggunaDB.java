package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.WhitelistPenggunaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhitelistPenggunaDB extends JpaRepository<WhitelistPenggunaModel, Integer> {
    List<WhitelistPenggunaModel> findAll();
    WhitelistPenggunaModel findByUsername(String username);
}
