package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.DosenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DosenDB extends JpaRepository<DosenModel, Integer>{
    DosenModel getById(Integer id);
    Optional<DosenModel> findById(Integer id);
}
