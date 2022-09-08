package a04.tugasakhirfarmasi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name="Keperluan")
public class KeperluanModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max=100)
    @Column(name="nama", nullable = false)
    private String nama;

    @OneToMany(mappedBy = "keperluan", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<JadwalNonSidangModel> listKeperluanNonSidang;
}
