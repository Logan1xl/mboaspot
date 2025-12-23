
package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "admin", catalog = "logement_cameroun")
@NamedQueries({
    @NamedQuery(name = "Admin.findAll", query = "SELECT a FROM Admin a"),
    @NamedQuery(name = "Admin.findById", query = "SELECT a FROM Admin a WHERE a.id = :id"),
    @NamedQuery(name = "Admin.findByDepartement", query = "SELECT a FROM Admin a WHERE a.departement = :departement")})
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "departement")
    private String departement;
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Utilisateur idUser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAdmin")
    private List<Signalement> signalementList;

    public Admin() {
    }

    public Admin(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public Utilisateur getIdUser() {
        return idUser;
    }

    public void setIdUser(Utilisateur idUser) {
        this.idUser = idUser;
    }

    public List<Signalement> getSignalementList() {
        return signalementList;
    }

    public void setSignalementList(List<Signalement> signalementList) {
        this.signalementList = signalementList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Admin[ id=" + id + " ]";
    }
    
}
