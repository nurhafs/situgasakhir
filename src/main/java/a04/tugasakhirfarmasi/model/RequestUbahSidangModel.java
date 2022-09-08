package a04.tugasakhirfarmasi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="RequestUbahSidang")
public class RequestUbahSidangModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="sidang", referencedColumnName = "id")
    @JsonIgnore
    private SidangModel sidang;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="dosen", referencedColumnName = "id")
    @JsonIgnore
    private DosenModel dosenRequestUbahSidang;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="status", referencedColumnName = "id")
    @JsonIgnore
    private StatusModel statusRequestUbahSidang;

    @Size(max=100)
    @Column(name="nama", nullable = true)
    private String alasan;
}
