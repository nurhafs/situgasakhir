package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.ProgramStudiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramStudiDB extends JpaRepository<ProgramStudiModel, Integer> {
    ProgramStudiModel getProgramStudiModelByNama(String nama);
    ProgramStudiModel findByNama(String nama);
}
