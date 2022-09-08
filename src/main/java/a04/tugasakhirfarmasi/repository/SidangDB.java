package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.RoleModel;
import a04.tugasakhirfarmasi.model.SidangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SidangDB extends JpaRepository<SidangModel, Integer> {
    Optional<SidangModel> findById(Integer id);
}
