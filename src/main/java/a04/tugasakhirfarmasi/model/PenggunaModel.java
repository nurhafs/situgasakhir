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
@Table(name="Pengguna")
public class PenggunaModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 50)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Lob
    @Column(name="password", nullable = true)
    private String password;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="programStudi", referencedColumnName = "id", nullable = true)
    @JsonIgnore
    private ProgramStudiModel prodiPengguna;

    @NotNull
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="role", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private RoleModel role;

    @OneToOne(mappedBy = "penggunaMahasiswa", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MahasiswaModel mahasiswa;

    @OneToOne(mappedBy = "penggunaDosen", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DosenModel dosen;

    @OneToOne(mappedBy = "penggunaAdmin", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AdminModel admin;
}
