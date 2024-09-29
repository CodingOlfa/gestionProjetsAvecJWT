package com.mdw3.AppGestionProjets.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Data
public class Employe {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String Nom;
	private String Prenom;
	private String Email;
	private String Tel;
	//Association *,*
	@ManyToMany
    @JoinTable(
        name = "employe_projet",
        joinColumns = @JoinColumn(name = "employe_id"),
        inverseJoinColumns = @JoinColumn(name = "projet_id")
    )
    private List<Projet> projets;
}
