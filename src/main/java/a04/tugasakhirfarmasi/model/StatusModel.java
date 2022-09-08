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
@Table(name="Status")
public class StatusModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max=100)
    @Column(name="nama", nullable = false)
    private String nama;

    @OneToMany(mappedBy = "statusRequestUbahSidang", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RequestUbahSidangModel> listRequestUbahSidangModel;

    @OneToMany(mappedBy = "statusLamaran", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LamaranModel> listStatusLamaran;

    @OneToMany(mappedBy = "statusSidang", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SidangModel> listStatusSidang;

    @OneToMany(mappedBy = "statusBerkas", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BerkasPrasyaratModel> listStatusBerkasPrasyarat;
}
