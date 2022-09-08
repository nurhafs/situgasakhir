package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.BerkasPrasyaratModel;
import a04.tugasakhirfarmasi.model.MahasiswaModel;
import a04.tugasakhirfarmasi.model.StatusModel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface BerkasPrasyaratDB extends JpaRepository<BerkasPrasyaratModel, Integer> {
    List<BerkasPrasyaratModel> findAllByStatusBerkas(Integer status);
    List<BerkasPrasyaratModel> findAllByBerkasMahasiswa(MahasiswaModel mahasiswa);
}
