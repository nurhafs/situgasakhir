package a04.tugasakhirfarmasi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
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
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="Lamaran")
public class LamaranModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="mahasiswa", referencedColumnName = "id")
    @JsonIgnore
    private MahasiswaModel lamaranMahasiswa;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="dosen", referencedColumnName = "id")
    @JsonIgnore
    private DosenModel lamaranDosen;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="status", referencedColumnName = "id")
    @JsonIgnore
    private StatusModel statusLamaran;

    @Getter(AccessLevel.NONE)
    @NotNull
    @Column(name="tanggal",nullable = false)
    private Date tanggal;

    @NotNull
    @Column(name="waktu", nullable = false)
    private LocalTime waktu;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="periode", referencedColumnName = "id")
    @JsonIgnore
    private PeriodeModel periodeLamaran;

    public String getTanggal() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(tanggal);
    }
}
