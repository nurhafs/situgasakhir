package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.PartisipanSidangModel;
import a04.tugasakhirfarmasi.model.SidangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartisipanSidangDB extends JpaRepository<PartisipanSidangModel, Integer> {
}
