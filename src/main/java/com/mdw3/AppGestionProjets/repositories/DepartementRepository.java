package com.mdw3.AppGestionProjets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mdw3.AppGestionProjets.entities.Departement;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartementRepository extends JpaRepository<Departement,Long>{
    Optional<Departement> findByNom(String nom);
    //List<Departement> findByNomContainingIgnoreCase(String keyword);
    @Query("from Departement d where upper(d.nom) like upper(concat('%', :keyword, '%'))")
    List<Departement> findByNomContainingIgnoreCase(@Param("keyword") String keyword);

}
