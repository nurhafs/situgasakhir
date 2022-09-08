package a04.tugasakhirfarmasi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="Sidang")
public class SidangModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="jenisSidang", referencedColumnName = "id")
    @JsonIgnore
    private JenisSidangModel jenisSidang;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="mahasiswa", referencedColumnName = "id")
    @JsonIgnore
    private MahasiswaModel mahasiswaSidang;

//    @Getter(AccessLevel.NONE)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="tanggal",nullable = false)
    private Date tanggal;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name="waktuMulai", nullable = false)
    private LocalTime waktuMulai;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name="waktuSelesai", nullable = false)
    private LocalTime waktuSelesai;

    @Column(name="ruangan", nullable = true)
    private String ruangan;

    @Column(name="hasil", nullable = true)
    private String hasil;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="periode", referencedColumnName = "id")
    @JsonIgnore
    private PeriodeModel periodeSidang;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="status", referencedColumnName = "id")
    @JsonIgnore
    private StatusModel statusSidang;

    @OneToMany(mappedBy = "sidang", fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<RequestUbahSidangModel> listRequestUbahSidangModel;

    @OneToMany(mappedBy = "partisipanSidang", fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<PartisipanSidangModel> listPartisipanSidang;

    public String getTanggalToString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(tanggal);
    }

}