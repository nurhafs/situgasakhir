package a04.tugasakhirfarmasi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="BerkasPrasyarat")
public class BerkasPrasyaratModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="jenisBerkas", referencedColumnName = "id")
    @JsonIgnore
    private JenisBerkasModel jenisBerkas;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="mahasiswa", referencedColumnName = "id")
    @JsonIgnore
    private MahasiswaModel berkasMahasiswa;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="status", referencedColumnName = "id")
    @JsonIgnore
    private StatusModel statusBerkas;

    @Column(name="file", nullable = true)
    private byte[] file;

    @Column(name="nama", nullable = true)
    private String nama;

    @Column(name="tipeFile", nullable = true)
    private String tipe;

    @Column(name="last_updated", nullable = true)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime lastUpdated;

    public BerkasPrasyaratModel(String name, String type, byte[] data, LocalDateTime lastUpdated) {
        this.nama = name;
        this.tipe = type;
        this.file = data;
        this.lastUpdated = lastUpdated;
    }

    public BerkasPrasyaratModel(MahasiswaModel mahasiswa, JenisBerkasModel jenisBerkasMahasiswa) {
        this.berkasMahasiswa = mahasiswa;
        this.jenisBerkas = jenisBerkasMahasiswa;
    }

}
