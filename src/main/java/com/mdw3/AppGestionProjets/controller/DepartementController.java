package com.mdw3.AppGestionProjets.controller;

import com.mdw3.AppGestionProjets.entities.Projet;
import com.mdw3.AppGestionProjets.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.mdw3.AppGestionProjets.entities.Departement;
import com.mdw3.AppGestionProjets.repositories.DepartementRepository;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/departements")
public class DepartementController {
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private ProjetRepository projetRepository;
    //////////////////////////   1   //////////////////
    // Create a new Departement

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Departement createDepartement(@RequestBody Departement departement) {
        // Vérifier que l'objet Departement n'est pas null
        if (departement == null) {
            throw new RuntimeException("Le département ne peut pas être null");
        }
        // Vérifier que le nom du département n'est pas null ou vide
        if (departement.getNom() == null || departement.getNom().trim().isEmpty()) {
            throw new RuntimeException("Le nom du département est obligatoire");
        }
        // Vérifier que le nom du département n'existe pas déjà
        Optional<Departement> existingDepartement = departementRepository.findByNom(departement.getNom());
        if (existingDepartement.isPresent()) {
            throw new RuntimeException("Un département avec ce nom existe déjà");
        }
        // Sauvegarder le nouveau département
        Departement savedDepartement = departementRepository.save(departement);
        // Retourner le département créé
        return savedDepartement;
    } //Fin
    //////////////////////////   2   //////////////////
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RESPONSABLE')")
    public List<Departement> getAllDepartements() {
        List<Departement> departements = departementRepository.findAll();

        if (departements.isEmpty()) {
            throw new RuntimeException("Aucun département trouvé");
        }

        return departements;
    }//Fin
    //////////////////////////   3   //////////////////
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RESPONSABLE')")
    public Departement getDepartementById(@PathVariable Long id) {
        // Trouver le département par ID
        Optional<Departement> departement = departementRepository.findById(id);
        if (departement.isEmpty()) {
            // Lancer une exception si le département n'existe pas
            throw new RuntimeException("Département non trouvé");
        }
        // Retourner le département trouvé
        return departement.get();
    }//FIn
    //////////////////////////   4   //////////////////
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDepartement(@PathVariable Long id) {
        // Vérifier si le département existe
        Optional<Departement> departement = departementRepository.findById(id);
        if (departement.isEmpty()) {
            // Lancer une exception si le département n'existe pas
            throw new RuntimeException("Département non trouvé");
        }
        // Supprimer le département mais avant il faut tester qu'aucun projet
        // // n'est mené par ce département
        if (departement.get().getProjets().isEmpty()) {
            departementRepository.deleteById(id);
            // Retourner un message de succès
            //return "Département supprimé avec succès";
        }
        else {
            // Lancer une exception si le département n'existe pas
            throw new RuntimeException("Impossible de supprimer le département car des projets y sont affectés");
        }
    }//Fin
    //////////////////////////   5   //////////////////
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Departement updateDepartement(@RequestBody Departement upDepartement) {
        // Trouver le département existant par ID
        Departement existingDepartement = departementRepository.findById(upDepartement.getId())
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));
        // Vérifier si le nom proposé existe déjà pour un autre département
        Optional<Departement> departement = departementRepository.findByNom(upDepartement.getNom());
        if (departement.isPresent() && (!departement.get().getId().equals(upDepartement.getId())))
            throw new RuntimeException("Un département avec ce nom existe déjà");
        if (upDepartement.getNom() == null || upDepartement.getNom().trim().isEmpty()) {
            throw new RuntimeException("Le nom du département est obligatoire");
        }
        // Mettre à jour les informations du département
        existingDepartement.setNom(upDepartement.getNom());
        // Sauvegarder le département mis à jour
        return departementRepository.save(existingDepartement);
    }//Fin
    //////////////////////////   6   //////////////////
    @GetMapping("/{id}/projets")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RESPONSABLE')")
    public List<Projet> getProjetsByDepartementId(@PathVariable Long id) {
        // Cherche le département par son ID
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));
        // Récupère la liste des projets
        List<Projet> projets = departement.getProjets();
        // Vérifie si la liste des projets est vide
        if (projets.isEmpty()) {
            throw new RuntimeException("Aucun projet n'est associé à ce département");
        }
        return projets;
    }//Fin
    //////////////////////////   7   //////////////////
    @GetMapping("/recherche")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RESPONSABLE')")
    public List<Departement> searchDepartements(@RequestParam String keyword) {
        List<Departement> resultats = departementRepository.findByNomContainingIgnoreCase(keyword);
        if (resultats.isEmpty()) {
            throw new RuntimeException("Aucun département trouvé pour le mot-clé: " + keyword);
        }
        return resultats;
    }//Fin

    //////////////////////////   8   //////////////////
    @PostMapping("/add/{projetId}/to/{departementId}")
    public String addProjetToDepartement(@PathVariable Long departementId, @PathVariable Long projetId) {
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));

        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        // Vérification si le projet est déjà associé au département
        if (projet.getDepartement() != null && projet.getDepartement().getId().equals(departementId)) {
            throw new RuntimeException("Le projet est déjà associé à ce département.");
        }

        projet.setDepartement(departement);
        departement.getProjets().add(projet);
        departementRepository.save(departement);
        return "Projet affecté";
    }//Fin
    //////////////////////////   9   //////////////////
    @DeleteMapping("/remove/{projetId}/from/{departementId}")
    public String removeProjetFromDepartement(@PathVariable Long departementId, @PathVariable Long projetId) {
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));
        // Vérifier si le projet est bien associé au département
        if (!departement.getProjets().contains(projet)) {
            throw new RuntimeException("Le projet n'est pas associé à ce département.");
        }
        departement.getProjets().remove(projet);
        projet.setDepartement(null); // Délier le projet de tout département
        departementRepository.save(departement);
        return "Projet retiré du département avec succès";
    }//Fin

}
