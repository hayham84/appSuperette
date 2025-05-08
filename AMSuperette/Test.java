package AMSuperette;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Exception.ContactAssocieeException;
import Exception.ContratException;
import Exception.FournisseurException;
import Exception.LotProduitException;
import Exception.ProduitException;
import Exception.VenteException;

public class Test 
{
	public static void main(String[] args)  
	{
        try {
            Connexion connexion = new Connexion();
            connexion.connect();
            Gestion gestion = new Gestion(connexion);

            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String choix;
            
            //creationTable(gestion);

            // Boucle principale du menu
            while (true) {
                
                System.out.println("MENU PRINCIPAL :\n"
                        + "1 - Fournisseurs\n"
                        + "2 - Commande\n"
                        + "3 - Produit\n"
                        + "4 - Résultat\n"
                        + "5 - Liste des lots périmés\n"
                        + "0 - Quitter\n"
                        + "Votre choix :");
                choix = reader.readLine();

                switch (choix) {
                    case "1":
                        gererFournisseurs(reader, gestion);
                        break;
                        
                    case "2":
                        gererCommandes(reader, gestion); 
                        break;
                        
                    case "3":
                        gererProduits(reader, gestion);  
                        break;
                        
                    case "4":
                    	afficherMenuResultat(reader, gestion);
                    	break;
                        
                    case "5":
                    	gestion.displayTable("perime");
                    	break;
                    	

                    case "0":
                        System.out.println("Fermeture de l'application...");
                        connexion.disconnect();
                        return;

                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                        break;
                }
            }
        } 
        catch (Exception e) {
            System.err.println("Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
 
    private static void ajouterFournisseur(BufferedReader reader, Gestion gestion) throws Exception,FournisseurException, ContactAssocieeException, ContratException {
        System.out.println("AJOUTER UN FOURNISSEUR :");

        // Informations de base du fournisseur
        String nom = demanderChaine(reader, "Nom de la société : ");
        int siret = demanderEntier(reader, "Numéro SIRET : ");
        String adresse = demanderChaine(reader, "Adresse : ");
        String mail = demanderChaine(reader, "E-mail principal : ");

        Fournisseur fournisseur = new Fournisseur(nom, siret, adresse, mail);
        gestion.insert(fournisseur, "Fournisseur");

        // Contacts associés
        int nombreContact = demanderEntier(reader, "Nombre de contacts associés : ");
        ArrayList<ContactAssociee> contacts = new ArrayList<>();
        for (int i = 0; i < nombreContact; i++) {
            System.out.println("Contact " + (i + 1) + " :");

           
            String prenomContact = demanderChaine(reader, "Prénom : ");
            String nomContact = demanderChaine(reader, "Nom : ");
            String fonction = demanderChaine(reader, "Fonction : ");
            int telephone = demanderEntier(reader, "Téléphone : ");
            String emailContact = demanderChaine(reader, "E-mail : ");

            ContactAssociee cont = new ContactAssociee(nomContact, prenomContact, fonction, emailContact, telephone);
            FournisseurContact fc = new FournisseurContact( siret, telephone);
            contacts.add(cont);

            gestion.insert(cont, "Contact");
            gestion.insert(fc, "Fournisseur_contact");
        }
        fournisseur.setContact(contacts);

        // Produits proposés
        int nombreProduit = demanderEntier(reader, "Nombre de produits proposés : ");
        
        
        ArrayList<Produit> produits = new ArrayList<>();
        for (int i = 0; i < nombreProduit; i++) {
            System.out.println("Produit " + (i + 1) + " :");

            String nomProduitt = demanderChaine(reader, "Nom : ");
            String categorie = demanderChaine(reader, "Catégorie : ");
            String description = demanderChaine(reader, "Description : ");
            float prix_vente = demanderFloat(reader, "Prix de vente actuel ( en rayon ): ");
            Produit p = new Produit(1, nomProduitt, description, categorie, prix_vente); // id_produit sera généré automatiquement
            
           
            
            
            gestion.insertProduit(p);  // Insère le produit dans la base pour récupérer son id
            
            
            
            produits.add(p);
        }
        fournisseur.setProduit(produits);
        
        // Contrats
        int nombreContrat = demanderEntier(reader, "Nombre de contrats : ");
        ArrayList<Contrat> contrats = new ArrayList<>();
        for (int i = 0; i < nombreContrat; i++) {
            System.out.println("Contrat " + (i + 1) + " :");

            String nomProduit = demanderChaine(reader, "Produit : ");
            String unite = demanderChaine(reader, "Unité exprimée : ");
            int quantiteMinimale = demanderEntier(reader, "Quantité minimale : ");
            String dateDebut = demanderDate(reader, "Date de début ( DD/MM//YYYY ) : ");
            String dateFin = demanderDate(reader, "Date de fin ( DD/MM//YYYY ) : ");
            double prix = demanderDouble(reader, "Prix " + unite + ": "); 
            
            int idProd = 0;
            
            
            for (Produit p : produits) 
            {
                if (nomProduit.equals(p.getNom())) 
                {
                    idProd = p.getNuméro_unique();
                    break;
                }
            }

            // Si le produit n'a pas été trouvé, il faut le créer
            if (idProd == 0) 
            {
                System.out.println("Le produit ne figure pas dans les produits proposés.");
                String categorie = demanderChaine(reader, "Catégorie : ");
                String description = demanderChaine(reader, "Description : ");
                float prix_vente = demanderFloat(reader, "Prix de vente actuel (en rayon) : ");
                
                Produit n = new Produit(1, nomProduit, description, categorie, prix_vente);
                gestion.insertProduit(n); // Insère le produit dans la base pour récupérer son ID
                idProd = n.getNuméro_unique();
                produits.add(n); // Ajoute le nouveau produit à la liste des produits proposés
            }
            

            Contrat c = new Contrat(siret, unite, nomProduit, idProd, quantiteMinimale, dateDebut, dateFin, prix);
            System.out.println(c);
            
            contrats.add(c);
            gestion.insertContrat(c);
        }
        fournisseur.setContrat(contrats);

        // Enregistrement du fournisseur dans la base de données
        System.out.println("Fournisseur ajouté avec succès !");
        System.out.println(fournisseur);
    }
 
	private static void gererFournisseurs(BufferedReader reader, Gestion gestion) throws Exception, FournisseurException {
        String choix1;
        while (true) {
            // Menu des fournisseurs
            System.out.println("FOURNISSEURS :\n"
                    + "1 - Ajouter un fournisseur\n"
                    + "2 - Tous les fournisseurs\n"
                    + "0 - Revenir au menu principal\n"
                    + "Votre choix :");
            choix1 = reader.readLine();

            switch (choix1) 
            {
                case "1":
                	
                    ajouterFournisseur(reader, gestion);
                    break;

                case "2":
                	
                    
                    System.out.println("Liste des fournisseurs :");
                    gestion.displayTable("Fournisseur");
                    
                    int numsiret = demanderEntier(reader, "Choisissez votre fournisseur (SIRET) pour le gérer ou 0 pour revenir\n"
                    		+ "Votre choix :");
                    
                    if ( numsiret == 0 ) break;
                    
                    Fournisseur fournisseur = gestion.getFournisseurBySiret(numsiret);
                    ArrayList<ContactAssociee> contact = gestion.getContactsdeFournisseur(numsiret);
                    ArrayList<Contrat> contrat = gestion.getContratdeFournisseur(numsiret);
                    ArrayList<Produit> produit = gestion.getProduitdeFournisseur(numsiret);
                    
                    
                    if ( fournisseur != null )
                	{
                    	fournisseur.setContact(contact);
                    	fournisseur.setContrat(contrat);
                    	fournisseur.setProduit(produit);
                    	gererFournisseurSelectionne(reader, gestion, fournisseur);
                	}
                    
                   
                    else System.out.println("Fournisseur non trouvé.");
                    break;
                    

                case "0":
                    return;

                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private static void gererFournisseurSelectionne(BufferedReader reader, Gestion gestion, Fournisseur fournisseur) throws Exception{
        String choix2;
        while (true) {
            System.out.println(fournisseur.getNom() + " :");
            System.out.println("1 - Afficher les informations");
            System.out.println("2 - Modifier");
            System.out.println("3 - Supprimer");
            System.out.println("0 - Revenir aux options précédentes");
            System.out.print("Votre choix : ");
            choix2 = reader.readLine();

            switch (choix2) {
                case "1":
                    afficherInformationsFournisseur(fournisseur);
                    break;

                case "2":
                    modifierFournisseur(reader, gestion, fournisseur);
                    break;

                case "3":
                	gestion.dropFournisseur(fournisseur.getSiret());
                	System.out.println("Fournisseur supprimé avec succès !");
                    return;

                case "0":
                    return;

                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private static void afficherInformationsFournisseur(Fournisseur fournisseur) {
        System.out.println("--- Information sur l’entreprise ---");
        System.out.println("Nom de la société : " + fournisseur.getNom());
        System.out.println("Numéro SIRET : " + fournisseur.getSiret());
        System.out.println("Adresse : " + fournisseur.getAdresse());
        System.out.println("E-mail principal : " + fournisseur.getMail());
        
        System.out.println("--- Contact associé ---");
        int i = 1,j = 1,k = 1;
        for (ContactAssociee contact : fournisseur.getContact()) 
        {
        	System.out.println("Contact n°:" + i++);
            System.out.println("Nom : " + contact.getNom());
            System.out.println("Prénom : " + contact.getPrenom());
            System.out.println("Fonction : " + contact.getFonction());
            System.out.println("E-mail : " + contact.getMail());
            System.out.println("Téléphone : " + contact.getTel());
        }
        
        System.out.println("--- Produits Proposés ---");
        for (Produit produit : fournisseur.getProduit()) 
        {
        	System.out.println("Produit n°:" + j++);
            System.out.println("Nom du produit : " + produit.getNom());
            System.out.println("Catégorie : " + produit.getCatégorie());
            System.out.println("Description : " + produit.getDescription());
            System.out.println("Prix de vente actuel : " + produit.getPrix_vente_actuel());
            
        }
        

        System.out.println("--- Contrats ---");
        for (Contrat contrat : fournisseur.getContrat()) 
        {
        	System.out.println("Contrat n°:" + k++);
            System.out.println("Produit : " + contrat.getNomProduit());
            System.out.println("Unité exprimée : " + contrat.getUniteExprimee());
            System.out.println("Quantité minimale : " + contrat.getQuantite_minimal());
            System.out.println("Date de début : " + contrat.getDate_debut());
            System.out.println("Date de fin : " + contrat.getDate_fin());
            System.out.println("Prix : " + contrat.getPrix());
        }
        
        
    }

    private static void modifierFournisseur(BufferedReader reader, Gestion gestion, Fournisseur fournisseur) throws Exception, FournisseurException, ContactAssocieeException, ContratException {
        String choixModif;
        while (true) {
            System.out.println(fournisseur.getNom() + " : (MODIFIER)");
            System.out.println("1 - Information sur l'entreprise");
            System.out.println("2 - Contact associé");
            System.out.println("3 - Contrat");
            System.out.println("0 - Revenir aux options précédentes");
            System.out.print("Votre choix : ");
            choixModif = reader.readLine();

            switch (choixModif) {
                case "1":
                    modifierInfoEntreprise(reader, gestion, fournisseur);
                    break;

                case "2":
                    modifierContactAssocie(reader, gestion, fournisseur);
                    break;

                case "3":
                    modifierContrat(reader, gestion,  fournisseur);
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private static void modifierInfoEntreprise(BufferedReader reader, Gestion gestion, Fournisseur fournisseur) throws Exception, FournisseurException {
        String nouveauNom = demanderChaine(reader, "Nouveau nom de la société : ");
        String nouvelleAdresse = demanderChaine(reader, "Nouvelle adresse : ");
        String nouveauMail = demanderChaine(reader, "Nouveau e-mail principal : ");
        fournisseur.setNom(nouveauNom);
        fournisseur.setAdresse(nouvelleAdresse);
        fournisseur.setMail(nouveauMail);
        gestion.modificationFournisseur(fournisseur);
        System.out.println("Informations modifiées avec succès !");
    }
        

    private static void modifierContactAssocie(BufferedReader reader, Gestion gestion, Fournisseur fournisseur) throws Exception, FournisseurException, ContactAssocieeException {
        // Afficher les contacts associés pour sélectionner lequel modifier
        //ArrayList<Contact_associee> contacts = gestion.getContactsdeFournisseur(fournisseur.getSiret());
        
        if (fournisseur.getContact().isEmpty()) {
            System.out.println("Aucun contact associé à ce fournisseur.");
            return;
        }

        // Demander quel contact modifier
        System.out.println("Choisissez un contact à modifier (entrez l'indice) ou 0 pour quittez : ");
        for (int i = 0; i < fournisseur.getContact().size(); i++) {
            System.out.println(i+1 + ". " + fournisseur.getContact().get(i).getNom() + " " + fournisseur.getContact().get(i).getPrenom());
            
        }

        int indexContact = Integer.parseInt(reader.readLine())-1; // L'utilisateur entre l'indice du contact
        if (indexContact == 0) return;
        if (indexContact < 0 || indexContact >= fournisseur.getContact().size()) 
        {
            System.out.println("Indice invalide.");
            return;
        }

        ContactAssociee contact = fournisseur.getContact().get(indexContact);

        // Demander les nouvelles informations pour le contact sélectionné
        String nouveauPrenom = demanderChaine(reader, "Nouveau prénom du contact : ");
        String nouveauNom = demanderChaine(reader, "Nouveau nom du contact : ");
        String nouvelleFonction = demanderChaine(reader, "Nouvelle fonction du contact : ");
        String nouveauTel = demanderChaine(reader, "Nouveau téléphone du contact : ");
        String nouveauMail = demanderChaine(reader, "Nouveau email du contact : ");
        
        // Mettre à jour le contact
        contact.setNom(nouveauNom);
        contact.setPrenom(nouveauPrenom);
        contact.setFonction(nouvelleFonction);
        int temp = contact.getTel();
        contact.setTel(Integer.parseInt(nouveauTel)); 
        contact.setMail(nouveauMail);                   
        												
        // Sauvegarder les modifications dans la base de données
        gestion.modificationContactAssociee(contact,temp);
        
        System.out.println("Contact modifié avec succès !");
    }


    private static void modifierContrat(BufferedReader reader, Gestion gestion, Fournisseur fournisseur) throws Exception, FournisseurException, ContratException {
        // Afficher les contrats associés au fournisseur
        //ArrayList<Contrat> contrats = gestion.getContratdeFournisseur(fournisseur.getSiret());
        
        if (fournisseur.getContrat().isEmpty()) {
            System.out.println("Aucun contrat associé à ce fournisseur.");
            return;
        }

        // Demander quel contrat modifier
        System.out.println("Choisissez un contrat à modifier (entrez l'indice) : ");
        for (int i = 0; i < fournisseur.getContrat().size(); i++) 
        {
            System.out.println(i + 1 + ". " + fournisseur.getContrat().get(i).getNomProduit() + " (SIRET: " + fournisseur.getContrat().get(i).getSiret() + ")");
        }

        int indexContrat = Integer.parseInt(reader.readLine()) - 1; // L'utilisateur entre l'indice du contrat
        if (indexContrat < 0 || indexContrat >= fournisseur.getContrat().size()) {
            System.out.println("Indice invalide.");
            return;
        }

        Contrat contrat = fournisseur.getContrat().get(indexContrat);

        // Demander les nouvelles informations pour le contrat sélectionné
        String nouveauNomProduit = demanderChaine(reader, "Nouveau nom du produit : ");
        String categorie = demanderChaine(reader, "Catégorie : ");
        String description = demanderChaine(reader, "Description : ");
        String nouvelleUniteExprimee = demanderChaine(reader, "Nouvelle unité exprimée : ");
        float prix_vente = demanderFloat(reader, "Prix de vente actuel (en rayon): ");
        int nouvelleQuantiteMin = Integer.parseInt(demanderChaine(reader, "Nouvelle quantité minimale : "));
        String nouvelleDateDebut = demanderDate(reader, "Nouvelle date de début (jj/mm/aaaa) : ");
        String nouvelleDateFin = demanderDate(reader, "Nouvelle date de fin (jj/mm/aaaa) : ");
        double nouveauPrix = Double.parseDouble(demanderChaine(reader, "Nouveau prix (fournisseur): "));

        // Mettre à jour le contrat
        contrat.setNomProduit(nouveauNomProduit);
        contrat.setUniteExprimee(nouvelleUniteExprimee);
        contrat.setQuantite_minimal(nouvelleQuantiteMin);
        contrat.setDate_debut(nouvelleDateDebut);
        contrat.setDate_fin(nouvelleDateFin);
        contrat.setPrix(nouveauPrix);
       System.out.println(contrat.toString());

        // Sauvegarder les modifications dans la base de données
        gestion.modificationContrat(contrat,categorie,description,prix_vente);
        
        System.out.println("Contrat modifié avec succès !");
    }
   
    
    private static void gererCommandes(BufferedReader reader, Gestion gestion) throws Exception, LotProduitException {
        String choixCommande;
        while (true) {
            // Menu des commandes
            System.out.println("COMMANDES :\n"
                    + "1 - Créer une commande\n"
                    + "2 - Afficher les commandes \n"
                    + "0 - Revenir au menu principal\n"
                    + "Votre choix :");
            choixCommande = reader.readLine();

            switch (choixCommande) {
                case "1":
                    creerCommande(reader, gestion); // Appel pour créer une commande
                    break;

                case "2":
                    gestion.displayTable("Lot"); // Appel pour afficher les commandes à effectuer
                    
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }
    
    
    private static void creerCommande(BufferedReader reader, Gestion gestion) throws Exception, LotProduitException {
        System.out.println("Création d’une commande :");

        // Demander les détails de la commande
        String produit = demanderChaine(reader, "Produit : ");
        
        // Obtenir la liste des fournisseurs proposant ce produit
        ArrayList<Fournisseur> fourn = gestion.produitFournisseur(produit);
        
        
        if (fourn.isEmpty()) return;
        
        // Si des fournisseurs existent, obtenir les contrats associés à ce produit
        ArrayList<Contrat> contrats = gestion.prixProduitFournisseur(produit);
        
        // Liste des fournisseurs qui proposent ce produit
        System.out.println("Fournisseurs qui proposent ce type de produit :");
        
        // Afficher la liste des fournisseurs et leur prix
        int i = 1;
        for (Fournisseur t : fourn) 
        {
        	
            System.out.println(i + " - " + t.getNom() + " (SIRET: " + t.getSiret() + ") Prix: " + contrats.get(i-1).getPrix());
            i++;
        	
        }
        

        // Demander à l'utilisateur de choisir un fournisseur
        int fournisseurChoisi = demanderEntier(reader, "Votre choix de fournisseur : ")-1;

        // Vérifier que le choix du fournisseur est valide
        while (fournisseurChoisi < 0 || fournisseurChoisi >= fourn.size())
        {
        	System.out.println("Choix invalide.");
        	fournisseurChoisi = demanderEntier(reader, "Votre choix de fournisseur : ")-1;
            
        }
        
        int siretFournisseurChoisi = fourn.get(fournisseurChoisi).getSiret();
        
        Produit p = gestion.infoProduitFournisseur(siretFournisseurChoisi, produit);

        // Récupérer le prix du fournisseur choisi
        float prix = (float) contrats.get(fournisseurChoisi).getPrix();   
        
        int quantite = demanderEntier(reader, "Quantité : ");
        while ( quantite < contrats.get(fournisseurChoisi).getQuantite_minimal())
        {
        	System.out.println("Quantité inférieur à celle mentionnée dans le contrat qui est de " + contrats.get(fournisseurChoisi).getQuantite_minimal());
        	quantite = demanderEntier(reader, "Quantité : ");
        }
       
        String dateCommande = demanderDate(reader, "Date de commande (jj/mm/aaaa) : ");
        String dateExp = demanderDate(reader, "Date de péremption (jj/mm/aaaa) : ");

        // Créer la commande avec les détails fournis par l'utilisateur
        LotProduit commande = new LotProduit(prix*quantite, quantite, dateCommande, dateExp); // Adaptez ce constructeur selon votre logique

        // Insérer la commande dans le système
        gestion.insertLot(commande);
        
        for ( int j = 0; j < quantite; j++ )
        {
        	Produit prod = new Produit(commande.getId_lot(), produit, p.getDescription(), p.getCatégorie(), p.getPrix_vente_actuel());
        	gestion.insertProduit(prod);
        }

        // Confirmer que la commande a été enregistrée
        System.out.println("Commande enregistrée !");
    }
    
    
    private static void gererProduits(BufferedReader reader, Gestion gestion) throws Exception, ProduitException {
        while (true) 
        {
            System.out.println("PRODUIT :\n"
                    + "1 - Stock\n"
                    + "2 - Vente\n"
                    + "0 - Revenir au options précédentes\n");

            int choix = demanderEntier(reader, "Votre choix :");

            switch (choix) {
                case 1:
                    gererStock(reader, gestion);
                    break;
                case 2:
                    gererVente(reader, gestion);
                    break;
                
                case 0:
                    return;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }
    
    
    private static void gererStock(BufferedReader reader, Gestion gestion) throws Exception, ProduitException {
        while (true) 
        {
            gestion.displayTable("Lot"); // Affiche les lots disponibles
            System.out.println("Taper le numéro de lot pour afficher la description détaillée du produit et modifier le prix actuel, ou 0 pour revenir.\n");
            
            int numLot = demanderEntier(reader, "Votre choix :");
            if (numLot == 0 || numLot == 1) return;

            gestion.afficheDetaillesProduit(numLot); // Affiche les détails du produit
            
            System.out.println("Voulez-vous modifier le prix de vente ? (1 - Oui, 0 - Non)");
            int modifierPrix = demanderEntier(reader, "Votre choix :");
            if (modifierPrix == 1) {
                float nouveauPrix = demanderFloat(reader, "Entrez le nouveau prix :");
                gestion.updatePrixProduit(numLot, nouveauPrix); // Méthode pour mettre à jour le prix
               
            }
        }
    }
    
    
    private static void gererVente(BufferedReader reader, Gestion gestion) throws Exception, VenteException {
        while (true) {
            gestion.displayTable("Ticket"); // Affiche les tickets de caisse
            System.out.println("1 - Déclarer une vente\n"
                    + "2 - Supprimer une vente\n"
                    + "0 - Revenir au options précédentes\n");

            int choix = demanderEntier(reader, "Votre choix :");
            switch (choix) {
                case 1:
                    int lot = demanderEntier(reader, "Numéro du lot vendu :"); 
                    float prixClient = demanderFloat(reader, "Prix d'achat par le client :");
                    String dateVente = demanderDate(reader, "Date de la vente ( DD/MM//YYYY ) :");
                    
                    Vente vente = new Vente(0,lot, dateVente, prixClient);

                    gestion.insertVente(vente); // Déclare une nouvelle vente
                    break;
                case 2:
                	gestion.displayTable("Ticket");
                    int idVente = demanderEntier(reader, "Entrez l'ID de la vente à supprimer ou 0 pour quitter :");
                    if (idVente == 0 ) break;
                    gestion.supprimerVente(idVente); // Supprime une vente
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }
    
    
    private static void afficherMenuResultat(BufferedReader reader, Gestion gestion) throws Exception, ProduitException {
        while (true) {
            System.out.println("RÉSULTAT :\n"
                    + "1 - Résultat journalier\n"
                    + "2 - Résultat mensuel\n"
                    + "0 - Revenir aux options précédentes\n"
                    + "Votre choix :");

            String choix = reader.readLine();

            switch (choix) {
                case "1":
                    afficherResultatJournalier(reader, gestion);
                    break;
                case "2":
                    afficherResultatMensuel(reader, gestion);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
    
    private static void afficherResultatJournalier(BufferedReader reader, Gestion gestion) throws Exception, ProduitException 
    {
        String date = demanderDate(reader, "Entrez la date (format : DD/MM/YYYY) : ");
        
        gestion.afficheTotalJournalier(date);
        
        gestion.afficheClassementJournalier(date);
        
        
    }
    
    private static void afficherResultatMensuel(BufferedReader reader, Gestion gestion) throws Exception, ProduitException {
        String mois = demanderChaine(reader, "Entrez le mois (format : MM/YYYY) : ");

        gestion.afficheTotalMensuel(mois);
        
        gestion.afficheClassementMensuel(mois);
        
    }
    
    private static String demanderDate(BufferedReader reader, String message) throws Exception {
        String dateFormat = "dd/MM/yyyy"; // Format de date souhaité
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false); // Désactive la tolérance pour les dates invalides
        String inputDate = "";

        while (true) {
            System.out.print(message);
            inputDate = reader.readLine();
            
            // Vérifier si la date saisie correspond au format spécifié
            try {
                Date date = sdf.parse(inputDate);
                return inputDate; // La date est valide, on la retourne
            } catch (ParseException e) {
                System.out.println("Erreur : Veuillez entrer une date au format " + dateFormat);
            }
        }
    }

    
    private static String demanderChaine(BufferedReader reader, String message) throws Exception {
        System.out.print(message);
        return reader.readLine();
    }

    
    private static int demanderEntier(BufferedReader reader, String message) throws Exception {
        int valeur = -1;
        while (valeur == -1) 
        {
            try 
            {
                System.out.print(message);
                valeur = Integer.parseInt(reader.readLine());
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Erreur : Veuillez entrer un nombre entier valide.");
            }
        }
        return valeur;
    }

    
    private static float demanderFloat(BufferedReader reader, String message) throws Exception {
        float valeur = -1;
        while (valeur == -1) {
            try {
                System.out.print(message);
                valeur = Float.parseFloat(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre valide.");
            }
        }
        return valeur;
    }
    
    
    private static double demanderDouble(BufferedReader reader, String message) throws Exception {
        double valeur = -1;
        while (valeur == -1) {
            try {
                System.out.print(message);
                valeur = Double.parseDouble(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre valide.");
            }
        }
        return valeur;
    }
    
    
    private static void creationTable(Gestion gestion) throws SQLException
    {
      // Création des tables avec gestion des dépendances
    	

		gestion.execute("""
		        CREATE TABLE Lot (
		            id_lot SERIAL PRIMARY KEY,
		            prix DECIMAL(10, 2) NOT NULL,
		            quantite INT NOT NULL,
		            date_achat VARCHAR(50),
		            date_peremption VARCHAR(50)
		        )
		    """);
		    System.out.println("Table Lot créée.");
		    
		    
		    gestion.execute("""
		            CREATE TABLE Fournisseur (
		                nom VARCHAR(100) NOT NULL,
		                siret INT PRIMARY KEY,
		                adresse TEXT NOT NULL,
		                email VARCHAR(100)
		            )
		        """);
		        System.out.println("Table Fournisseur créée.");
		    
		    
		gestion.execute("""
		   CREATE TABLE Produit (
		       id_produit SERIAL PRIMARY KEY,
		       id_lot_achat INT,
		       nom VARCHAR(50),
		       description VARCHAR(255),
		       categorie VARCHAR(50),
		       prix_vente_actuel FLOAT,
		       FOREIGN KEY (id_lot_achat) REFERENCES Lot(id_lot)
		   )
		""");
		System.out.println("Table Produit créée.");
		
		gestion.execute("CREATE TABLE Contrat (\n"
				+ "    idContrat SERIAL PRIMARY KEY,\n"
				+ "    siret INT NOT NULL,\n"
				+ "    nom_produit VARCHAR(50),\n"
				+ "    unite_exprimee VARCHAR(20),\n"
				+ "    id_produit INT,\n"
				+ "    quantite_minimal INT,\n"
				+ "    date_debut VARCHAR(50),\n"
				+ "    date_fin VARCHAR(50),\n"
				+ "    prix FLOAT,\n"
				+ "    FOREIGN KEY (siret) REFERENCES Fournisseur(siret) ON DELETE CASCADE,\n"
				+ "    FOREIGN KEY (id_produit) REFERENCES Produit(id_produit)\n"
				+ ");");
		System.out.println("Table Contrat créée.");
		
		
		gestion.execute("""
		   CREATE TABLE Contact (
		   telephone INT PRIMARY KEY,
		       nom VARCHAR(50) NOT NULL,
		       prenom VARCHAR(50) NOT NULL,
		       fonction VARCHAR(50),
		       email VARCHAR(100)
		       
		   )
		""");
		System.out.println("Table Contact créée.");
		
		
		gestion.execute("CREATE TABLE Ticket (\n"
				+ "ticket_caisse SERIAL PRIMARY KEY,\n"
				+ "id_lot INT NOT NULL,\n"
				+ "date_vente VARCHAR(50) NOT NULL,\n"
				+ "prix DECIMAL(10, 2) NOT NULL,\n"
				+ "FOREIGN KEY (id_lot) REFERENCES Lot(id_lot) );");
		
		
		System.out.println("Table Ticket créée.");
		
		
		gestion.execute("CREATE TABLE Ticket_Produit (\n"
				+ "ticket_caisse INT NOT NULL,\n"
				+ "id_produit INT NOT NULL,\n"
				+ "PRIMARY KEY (ticket_caisse, id_produit),\n"
				+ "FOREIGN KEY (ticket_caisse) REFERENCES Ticket(ticket_caisse) ON DELETE CASCADE,\n"
				+ "FOREIGN KEY (id_produit) REFERENCES Produit(id_produit) ON DELETE CASCADE);");
		
		System.out.println("Table Ticket_Produit créée.");
		
		
		gestion.execute("CREATE TABLE Fournisseur_Contact (\n"
				+ "    siret INT NOT NULL,\n"
				+ "    telephone INT NOT NULL,\n"
				+ "    PRIMARY KEY (siret, telephone),\n"
				+ "    FOREIGN KEY (siret) REFERENCES Fournisseur(siret) ON DELETE CASCADE,\n"
				+ "    FOREIGN KEY (telephone) REFERENCES Contact(telephone) ON DELETE CASCADE\n"
				+ ");");
		
		     
		       
		       System.out.println("Table Fournisseur_Contact créée.");
		       
		 LotProduit lot = new LotProduit(0, 1000000, "01/01/0001", "01/01/0001");
	     gestion.insert(lot, "Lot");

    }
}

