package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.LamaranModel;
import a04.tugasakhirfarmasi.model.MahasiswaModel;
import a04.tugasakhirfarmasi.model.StatusModel;
import a04.tugasakhirfarmasi.model.DosenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface LamaranDB extends JpaRepository<LamaranModel, Integer> {
    Optional<LamaranModel> findById(Integer id);
    List<LamaranModel> findByStatusLamaran(StatusModel statusLamaran);
    List<LamaranModel> findByLamaranMahasiswa(MahasiswaModel mahasiswa);
}
