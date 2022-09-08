package a04.tugasakhirfarmasi.repository;

import java.util.Optional;
import java.util.List;
import a04.tugasakhirfarmasi.model.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusDB extends JpaRepository<StatusModel, Integer>{
    StatusModel getById(Integer id);
    Optional<StatusModel> findByNama(String nama);
}
