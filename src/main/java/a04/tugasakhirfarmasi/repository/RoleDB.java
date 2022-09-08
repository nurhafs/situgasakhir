package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDB extends JpaRepository<RoleModel, Integer> {
    RoleModel getById(Integer id);
    RoleModel getRoleModelByNama(String nama);
}