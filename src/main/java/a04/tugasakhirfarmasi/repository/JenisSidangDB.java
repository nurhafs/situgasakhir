package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.JenisSidangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JenisSidangDB extends JpaRepository<JenisSidangModel, Integer> {
}
