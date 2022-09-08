package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.MahasiswaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MahasiswaDB extends JpaRepository<MahasiswaModel, Integer> {
    List<MahasiswaModel> findAllByOrderByUsernameAsc();
    MahasiswaModel findByUsername(String username);
}
