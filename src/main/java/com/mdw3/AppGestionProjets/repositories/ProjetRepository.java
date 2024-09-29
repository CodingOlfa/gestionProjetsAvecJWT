package com.mdw3.AppGestionProjets.repositories;

import com.mdw3.AppGestionProjets.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mdw3.AppGestionProjets.entities.Departement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetRepository extends JpaRepository<Projet,Long>{
    Optional<Projet> findByNom(String nom);
    List<Projet> findByNomContainingIgnoreCase(String keyword);
    List<Projet> findByDateDebutGreaterThanEqualAndDateFinLessThanEqual(LocalDate dateDebut, LocalDate dateFin);
}
