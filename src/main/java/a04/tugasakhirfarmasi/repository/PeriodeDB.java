package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.PeriodeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeriodeDB extends JpaRepository<PeriodeModel, Integer> {
    List<PeriodeModel> findAll();
    PeriodeModel findByPeriode(String periode);
}
