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
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="JadwalNonSidang")
public class JadwalNonSidangModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="tanggalMulai",nullable = false)
    private Date tanggalMulai;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="tanggalSelesai",nullable = false)
    private Date tanggalSelesai;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name="waktuMulai", nullable = false)
    private LocalTime waktuMulai;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name="waktuSelesai", nullable = false)
    private LocalTime waktuSelesai;

    @Size(max=100)
    @Column(name="deskripsi")
    private String deskripsi;

    @NotNull
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="keperluan", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private KeperluanModel keperluan;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="dosen", referencedColumnName = "id")
    @JsonIgnore
    private DosenModel dosen;
}
