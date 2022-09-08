package a04.tugasakhirfarmasi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="Dosen")
public class DosenModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max=100)
    @Column(name="nama", nullable = false)
    private String nama;
    
    @Column(name="jumlahMahasiswa", nullable = true)
    private Integer jumlahMahasiswa;

    @Column(name="jumlahPelamar", nullable = true)
    private Integer jumlahPelamar;

    @Column(name="isDosenPembimbing", nullable = true)
    private boolean isDosenPembimbing;

    @Column(name="isDosenPenguji", nullable = true)
    private boolean isDosenPenguji;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPengguna")
    private PenggunaModel penggunaDosen;

    @OneToMany(mappedBy = "dosen", fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<JadwalNonSidangModel> listJadwalNonSidangDosen;

    @OneToMany(mappedBy = "partisipanSidangDosen", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PartisipanSidangModel> listPartisipanSidangDosen;

    @OneToMany(mappedBy = "dosenRequestUbahSidang", fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<RequestUbahSidangModel> listRequestUbahSidangDosen;

    @OneToMany(mappedBy = "lamaranDosen", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LamaranModel> listLamaranDosen;

    public DosenModel(Integer jmlMahasiswa, Integer jmlPelamar, boolean isDosbing, boolean isDospenguji) {
        jumlahMahasiswa = jmlMahasiswa;
        jumlahPelamar = jmlPelamar;
        isDosenPembimbing = isDosbing;
        isDosenPenguji = isDospenguji;
    }
}
