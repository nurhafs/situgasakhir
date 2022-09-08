package a04.tugasakhirfarmasi.repository;

import a04.tugasakhirfarmasi.model.InformasiModel;
import a04.tugasakhirfarmasi.model.JadwalNonSidangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InformasiPaginationDB extends PagingAndSortingRepository<InformasiModel, Integer> {

}
