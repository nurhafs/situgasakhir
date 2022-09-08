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
@Table(name="Informasi")
public class InformasiModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max=100)
    @Column(name="judul")
    private String judul;

    @Lob
    @Column(name="isi")
    private String isi;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="tanggal",nullable = false)
    private Date tanggal;

    @NotNull
    @Column(name="waktu", nullable = false)
    private LocalTime waktu;

    @Column(name="file", nullable = true)
    private byte[] file;

    @Column(name = "namaFile", nullable = true)
    private String namaFile;

    @Column(name="isS1", nullable = true)
    private boolean isS1;

    @Column(name="isS2", nullable = true)
    private boolean isS2;

    @Column(name="isS3", nullable = true)
    private boolean isS3;

    @Column(name="isApoteker", nullable = true)
    private boolean isApoteker;
}
