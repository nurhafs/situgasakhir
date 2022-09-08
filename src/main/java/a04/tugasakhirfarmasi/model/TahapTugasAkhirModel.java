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
@Table(name="TahapTugasAkhir")
public class TahapTugasAkhirModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max=100)
    @Column(name="nama", nullable = false)
    private String nama;

    @OneToMany(mappedBy = "tahapTugasAkhir", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MahasiswaModel> listMahasiswaTahapTugasAkhir;
}
