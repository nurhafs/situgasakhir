package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.KeperluanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeperluanDB extends JpaRepository<KeperluanModel, Integer> {
    KeperluanModel getById(Integer id);
    KeperluanModel getKeperluanModelByNama(String nama);
}