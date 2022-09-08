package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.PenggunaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PenggunaDB extends JpaRepository<PenggunaModel, Integer> {
    PenggunaModel findByUsername(String username);
    Optional<PenggunaModel> findById(Integer id);

}
