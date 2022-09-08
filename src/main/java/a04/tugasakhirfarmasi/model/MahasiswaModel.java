package a04.tugasakhirfarmasi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@Table(name="Mahasiswa")
public class MahasiswaModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max=100)
    @Column(name="username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Size(max=100)
    @Column(name="npm", nullable = false, unique = true)
    private String npm;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="tahapTugasAkhir", referencedColumnName = "id")
    @JsonIgnore
    private TahapTugasAkhirModel tahapTugasAkhir;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="periode", referencedColumnName = "id")
    @JsonIgnore
    private PeriodeModel periodeMahasiswa;

    @OneToMany(mappedBy = "mahasiswaSidang", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SidangModel> listSidang;

    @OneToMany(mappedBy = "lamaranMahasiswa", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LamaranModel> listLamaran;

    @OneToMany(mappedBy = "berkasMahasiswa", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BerkasPrasyaratModel> listBerkasPrasyarat;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPengguna")
    private PenggunaModel penggunaMahasiswa;

    public MahasiswaModel(PenggunaModel pengguna, String npmMahasiswa, String namaLengkap) {
        this.npm = npmMahasiswa;
        this.username = namaLengkap;
        this.penggunaMahasiswa = pengguna;
    }
}
