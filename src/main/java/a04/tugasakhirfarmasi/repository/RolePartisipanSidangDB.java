package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.RolePartisipanSidangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePartisipanSidangDB extends JpaRepository<RolePartisipanSidangModel, Integer> {
    RolePartisipanSidangModel getById(Integer id);
    RolePartisipanSidangModel getByNama(String nama);
}
