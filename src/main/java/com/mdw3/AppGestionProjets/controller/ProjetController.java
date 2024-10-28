package com.mdw3.AppGestionProjets.controller;

import com.mdw3.AppGestionProjets.entities.Employe;
import com.mdw3.AppGestionProjets.entities.Projet;
import com.mdw3.AppGestionProjets.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mdw3.AppGestionProjets.entities.Departement;
import com.mdw3.AppGestionProjets.repositories.DepartementRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/projets")
public class ProjetController {
    @Autowired
    private ProjetRepository projetRepository;

    //////////////////////////   1   //////////////////
    // Create a new projet
    @PostMapping
    public Projet createProjet(@RequestBody Projet projet) {
        // Vérifier que l'objet projet n'est pas null
        if (projet == null) {
            throw new RuntimeException("Le projet ne peut pas être null");
        }
        // Vérifier que le nom du projet n'est pas null ou vide
        if (projet.getNom() == null || projet.getNom().trim().isEmpty()) {
            throw new RuntimeException("Le nom du département est obligatoire");
        }
        // Vérifier que le nom du projet n'existe pas déjà
        Optional<Projet> existingProjet = projetRepository.findByNom(projet.getNom());
        if (existingProjet.isPresent()) {
            throw new RuntimeException("Un projet avec ce nom existe déjà");
        }
        // Sauvegarder le nouveau projet
        Projet savedProjet = projetRepository.save(projet);
        // Retourner le projet créé
        return savedProjet;
    } //Fin

    //////////////////////////   2   //////////////////
    @GetMapping
    public List<Projet> getAllProjets() {
        List<Projet> projets = projetRepository.findAll();

        if (projets.isEmpty()) {
            throw new RuntimeException("Aucun projet trouvé");
        }

        return projets;
    }//Fin

    //////////////////////////   3   //////////////////
    @GetMapping("/{id}")
    public Projet getProjetById(@PathVariable Long id) {
        // Trouver le projet par ID
        Optional<Projet> projet = projetRepository.findById(id);
        if (projet.isEmpty()) {
            // Lancer une exception si le projet n'existe pas
            throw new RuntimeException("Projet non trouvé");
        }
        // Retourner le projet trouvé
        return projet.get();
    }//FIn

    //////////////////////////   4   //////////////////
    @DeleteMapping("/{id}")
    public String deleteProjet(@PathVariable Long id) {
        // Vérifier si le projet existe
        Optional<Projet> projet = projetRepository.findById(id);
        if (projet.isEmpty()) {
            // Lancer une exception si le projet n'existe pas
            throw new RuntimeException("Projet non trouvé");
        }
        // Supprimer le projet
        projetRepository.deleteById(id);
        // Retourner un message de succès
        return "Projet supprimé avec succès";
    }//Fin

    //////////////////////////   5   //////////////////
    @PutMapping("/{id}")
    public Projet updateProjet(@PathVariable Long id, @RequestBody Projet upProjet) {
        // Trouver le projet existant par ID
        Projet existingProjet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        // Vérifier si le nom proposé existe déjà pour un autre projet
        Optional<Projet> projet = projetRepository.findByNom(upProjet.getNom());
        if (projet.isPresent() && (!projet.get().getId().equals(id)))
            throw new RuntimeException("Un projet avec ce nom existe déjà");
        // Mettre à jour les informations du projet
         existingProjet.setNom(upProjet.getNom());
         existingProjet.setDateDebut(upProjet.getDateDebut());
        existingProjet.setDateFin(upProjet.getDateFin());
        // Sauvegarder le projet mis à jour
        return projetRepository.save(existingProjet);
    }//Fin

    //////////////////////////   6   //////////////////
    @GetMapping("/recherche")
    public List<Projet> searchProjets(@RequestParam String keyword) {
        List<Projet> resultats = projetRepository.findByNomContainingIgnoreCase(keyword);
        if (resultats.isEmpty()) {
            throw new RuntimeException("Aucun projet trouvé pour le mot-clé: " + keyword);
        }
        return resultats;
    }//Fin
    //////////////////////////   7   //////////////////
    @GetMapping("/recherche2")
    public List<Projet> getProjetsEntreDates(@RequestParam("dateDebut") LocalDate dateDebut,
                                             @RequestParam("dateFin") LocalDate dateFin) {
        if (dateDebut.isAfter(dateFin))
        {
            throw new RuntimeException("Date début doit être <=date fin");
        }
        return projetRepository.findByDateDebutGreaterThanEqualAndDateFinLessThanEqual(dateDebut, dateFin);
    }

    //////////////////////////   8   //////////////////
    @GetMapping("/{id}/employes")
    public List<Employe> getEmployesByProjetId(@PathVariable Long id) {
        // Cherche le projet par son ID
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        // Récupère les employés qui sont  affectés au projet
        List<Employe> employes = projet.getEmployes();
        // Vérifie si la liste des employés est vide
        if (employes.isEmpty()) {
            throw new RuntimeException("Aucun employé n'est associé à ce projet");
        }
        return employes;
    }//Fin
    //////////////////////////   9   //////////////////
    @GetMapping("/{id}/departement")
    public Departement getDepartementByProjetId(@PathVariable Long id) {
        // Cherche le projet par son ID
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        // Récupère le département qui travaille sur le projet
        Departement departement = projet.getDepartement();
        // Vérifie si département null
        if (departement == null) {
            throw new RuntimeException("Aucun département n'est associé à ce projet");
        }
        return departement;
    }//Fin
}