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
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="PartisipanSidang")
public class PartisipanSidangModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="dosen", referencedColumnName = "id")
    @JsonIgnore
    private DosenModel partisipanSidangDosen;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="sidang", referencedColumnName = "id")
    @JsonIgnore
    private SidangModel partisipanSidang;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="rolePartisipanSidang", referencedColumnName = "id")
    @JsonIgnore
    private RolePartisipanSidangModel rolePartisipanSidang;

    public PartisipanSidangModel(SidangModel sidang, DosenModel dosen, RolePartisipanSidangModel role) {
        this.partisipanSidang = sidang;
        this.partisipanSidangDosen = dosen;
        this.rolePartisipanSidang = role;
    }
}
