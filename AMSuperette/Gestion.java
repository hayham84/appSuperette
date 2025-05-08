package AMSuperette;

import java.sql.*;
import java.util.ArrayList;


public class Gestion 
{
	
	private Connexion connection;

    public Gestion(Connexion connection) 
    {
        this.connection = connection;
    }
   
    //Première méthode pour afficher une table
//    public void displayTable(String table) throws SQLException 
//	{
//		String query = "";
//		if (table == "Lot") query = "SELECT distinct(id_lot),nom,prix_vente_actuel,categorie,prix,quantite,date_achat,date_peremption FROM Lot join Produit on id_lot=id_lot_achat where id_lot != 1;"
//				+ ""
//				+ "";
//		else query = "SELECT * FROM " + table;
//        try (PreparedStatement ps = connection.conn.prepareStatement(query))
//        {
//            ResultSet rs = ps.executeQuery();
//            ResultSetMetaData metaData = rs.getMetaData();
//            
//            int columnCount = metaData.getColumnCount();
//
//            for (int i = 1; i <= columnCount; i++) 
//            {
//                System.out.print(metaData.getColumnName(i) + "\t");
//            }
//            System.out.println();
//
//            while (rs.next()) 
//            {
//                for (int i = 1; i <= columnCount; i++) 
//                {
//                    System.out.print(rs.getObject(i) + "\t");
//                }
//                System.out.println();
//            }
//           
//        }
//    }
    
  //Deuxième méthode pour afficher une table, un affichage plus confortable mais plus technique
	public void displayTable(String table) throws SQLException 
	{
		String query = "";
		if (table == "Lot") 
			{
				query = """
					    SELECT distinct id_lot "Numéro de lot",
					    nom "Produit",prix_vente_actuel as "Pris de vente actuel",
					    categorie as "Catégorie",prix as "Prix d'achat du lot",
					    quantite as "Quantité",date_achat as "Date d'achat",
					    date_peremption as "Date de péremption" 
					    FROM Lot join Produit on id_lot=id_lot_achat 
					    where id_lot != 1;
					    """;
			}
		else if (table == "Ticket")
			{
				query = """
						SELECT distinct ticket_caisse, nom, id_lot, date_vente, Ticket.prix, Ticket.prix/Contrat.prix as "Quantité vendu" FROM Ticket join Produit on id_lot=id_lot_achat join Contrat on Contrat.id_produit=Produit.id_produit
						;
						
						""";
			}
		else if (table == "perime")
		{
			query = """
					SELECT distinct id_lot, nom, prix_vente_actuel, quantite, date_achat, date_peremption FROM Lot join Produit on id_lot=id_lot_achat where TO_DATE(Lot.date_peremption, 'DD/MM/YYYY') < CURRENT_DATE and id_lot != 1
					;
					""";
		}
		else
			query = "SELECT * FROM " + table;
		
		// Crée un PreparedStatement avec un ResultSet défilable
	    try (PreparedStatement ps = connection.conn.prepareStatement(query, 
	                ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                ResultSet.CONCUR_READ_ONLY)) {

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData metaData = rs.getMetaData();

	        int columnCount = metaData.getColumnCount();
	        int[] columnWidths = new int[columnCount];

	        // Calcule les largeurs des colonnes (basé sur les noms des colonnes)
	        for (int i = 1; i <= columnCount; i++) {
	            columnWidths[i - 1] = Math.max(metaData.getColumnName(i).length(), 15); // Minimum 15 caractères
	        }

	        // Ajuste les largeurs en fonction des données
	        while (rs.next()) {
	            for (int i = 1; i <= columnCount; i++) {
	                String value = String.valueOf(rs.getObject(i));
	                columnWidths[i - 1] = Math.max(columnWidths[i - 1], value.length());
	            }
	        }

	        // Réinitialise le curseur du ResultSet
	        rs.beforeFirst();

	        // Affiche les noms des colonnes avec le bon format
	        for (int i = 1; i <= columnCount; i++) {
	            System.out.print(padRight(metaData.getColumnName(i), columnWidths[i - 1]) + " ");
	        }
	        System.out.println();

	        // Ligne de séparation
	        for (int width : columnWidths) {
	            System.out.print("-".repeat(width) + " ");
	        }
	        System.out.println();

	        // Affiche les données
	        while (rs.next()) 
	        {
	            for (int i = 1; i <= columnCount; i++) {
	                String value = String.valueOf(rs.getObject(i));
	                System.out.print(padRight(value, columnWidths[i - 1]) + " ");
	            }
	            System.out.println();
	        }
	    }
	}
	
	// Méthode utilitaire pour ajuster la largeur d'une chaîne
	private String padRight(String text, int width) {
	    return String.format("%-" + width + "s", text);
	}
	
	//Méthode qui permet d'éxecuter une requête SQL
	public void execute(String query) throws SQLException 
	{
        try (Statement statement = connection.conn.createStatement()) 
        {
            statement.execute(query);
            System.out.println("Requête exécutée.");
        }

    }
	
	//Méthode permettant d'insérer des valeurs dans une table 
	public void insert(IData data, String table) throws SQLException {

	    String query = "INSERT INTO " + table + " VALUES " + data.getValues();
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) 
	    {
	        ps.executeUpdate();
	        //System.out.println("Insertion réussie.");
	    }
	}
	
	//Méthode permettant d'insérer plus précisement des valeurs dans la table Contrat.
	//C'est une méthode à part entière car vu qu'un contrat à un identifiant générer automatiquement par la base de donnée
	//On doit aller recuperer cet identifiant là pour la mettre dans l'instance crée ( Contrat data )
	public void insertContrat(Contrat data) throws SQLException 
	{
	    String query = "INSERT INTO Contrat " + " (siret, nom_produit, unite_exprimee, id_produit, quantite_minimal, date_debut, date_fin, prix) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement ps = connection.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) 
	    {
	        ps.setInt(1, data.getSiret());
	        ps.setString(2, data.getNomProduit());
	        ps.setString(3, data.getUniteExprimee());
	        ps.setInt(4, data.getId_produit());
	        ps.setInt(5, data.getQuantite_minimal());
	        ps.setString(6, data.getDate_debut());
	        ps.setString(7, data.getDate_fin());
	        ps.setDouble(8, data.getPrix());
	        
	        
	        ps.executeUpdate();



	        // Récupére l'ID généré automatiquement
	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                data.setIdContrat(rs.getInt(1)); // Récupère l'ID généré
	                System.out.println("Insertion réussie.");
	            }
	        }

	        
	    }
	}
	
	
	//Même fonctionnement que pour insertContrat( Contrat data )
	public void insertProduit(Produit data) throws SQLException 
	{
		
	    String query = "INSERT INTO Produit " +
	                   "(id_lot_achat, nom, description, categorie, prix_vente_actuel) " +
	                   "VALUES (?, ?, ?, ?, ?)";
	    try (PreparedStatement ps = connection.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        ps.setInt(1, data.getNuméro_de_lot_achat());
	        ps.setString(2, data.getNom());
	        ps.setString(3, data.getDescription());
	        ps.setString(4, data.getCatégorie());
	        ps.setDouble(5, data.getPrix_vente_actuel());

	        ps.executeUpdate();

	        // Récupére l'ID généré automatiquement
	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                data.setNuméro_unique(rs.getInt(1));
	            }
	        }

	        //System.out.println("Produit inséré avec succès.");
	    }
	}
	
	
	//Même fonctionnement que pour insertContrat( Contrat data )
	public void insertLot(LotProduit data) throws SQLException 
	{
	    String query = "INSERT INTO Lot " +
	                   "(prix, quantite, date_achat, date_peremption) " +
	                   "VALUES (?, ?, ?, ?)";
	    try (PreparedStatement ps = connection.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        ps.setDouble(1, data.getPrix());
	        ps.setInt(2, data.getQuantite());
	        ps.setString(3, data.getDate_achat()); 
	        ps.setString(4, data.getDate_peremption()); 

	        ps.executeUpdate();

	        // Récupére l'ID généré automatiquement
	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                data.setId_lot(rs.getInt(1)); 
	            }
	        }
	    }
	}
	
	
	// Méthode pour supprimer une ligne de valeur qui a comme identifiant id
	public void dropFournisseur(int id) throws SQLException 
	{
	    String query = "DELETE FROM Fournisseur WHERE siret = ?";

	   
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) {
	        ps.setInt(1, id);
	        int confirmation = ps.executeUpdate();
	        
	        if (confirmation > 0) {
	            System.out.println("Suppression réussie pour l'id : " + id);
	        } else {
	            System.out.println("Aucun enregistrement trouvé pour l'id : " + id);
	        }
	    } catch (SQLException e) {
	        System.out.println("Erreur lors de la suppression : " + e.getMessage());
	    }
	}
	
	
	//Méthode pour supprimer une Table ( Pas utilisé dans le projet, je supprimer directement dans le terminal pour mes tests

//	public void dropTable(String table) throws SQLException {
//	    String query = "DROP TABLE IF EXISTS " + table + "CASCADE";
//	    execute(query);
//	    System.out.println("Table " + table + " supprimée.");
//	}

	
	//Récupère les informations d'un fournisseur avec le siret
	public Fournisseur getFournisseurBySiret(int siret) 
	{
	    String query = "SELECT * FROM Fournisseur WHERE siret = ?";
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) 
	    {
	        ps.setInt(1, siret); 
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) 
	        {
	            // Récupére les informations du fournisseur
	            String nom = rs.getString("nom");
	            String adresse = rs.getString("adresse");
	            String mail = rs.getString("email");

	            // Crée l'objet Fournisseur
	            Fournisseur fournisseur = new Fournisseur(nom, siret, adresse, mail);

	           
	            return fournisseur;
	        } 
	        
	    }
	    catch (SQLException e )
	    {
	    	System.out.println("Fournisseur avec SIRET " + siret + " non trouvé.");
        
	    }
		return null;
	}
	    
	
	//Récupère les contacts associées d'un fournisseur
	public ArrayList<ContactAssociee> getContactsdeFournisseur(int siret) throws SQLException 
	{
	    String query = "SELECT Contact.nom, Contact.prenom, Contact.fonction, Contact.telephone, Contact.email " +
	                   "FROM Contact " +
	                   "NATURAL JOIN Fournisseur_contact " +
	                   "WHERE Fournisseur_contact.siret = ?";
	    
	    // Liste pour stocker les contacts associés
	    ArrayList<ContactAssociee> contacts = new ArrayList<>(); 

	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) 
	    {
	        ps.setInt(1, siret); 
	        ResultSet rs = ps.executeQuery();
	        
	        // Parcours de tous les résultats
	        while (rs.next()) 
	        { 
	            String nom = rs.getString("nom");
	            String prenom = rs.getString("prenom");
	            String fonction = rs.getString("fonction");
	            String mail = rs.getString("email");
	            int tel = rs.getInt("telephone"); 

	            // Crée l'objet Contact_associee et l'ajoute à la liste
	            ContactAssociee c = new ContactAssociee(nom, prenom, fonction, mail, tel);
	            contacts.add(c);
	        }

	    }
	    
	    return contacts; 
	}
	
	
	//Récupère les contrats d'un fournisseur
	public ArrayList<Contrat> getContratdeFournisseur(int siret) throws Exception {
	  
	    String query = "SELECT idcontrat, siret, nom_produit, unite_exprimee, id_produit, quantite_minimal, date_debut, date_fin, prix " +
	                   "FROM Contrat " +
	                   "WHERE siret = ?"; 

	    ArrayList<Contrat> contrats = new ArrayList<>(); // Liste pour stocker les contrats associés

	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) {
	        ps.setInt(1, siret); 
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) 
	        { 
	            int idcontrat = rs.getInt("idcontrat");
	            String nomProduit = rs.getString("nom_produit");
	            String uniteExprimee = rs.getString("unite_exprimee");
	            int idProduit = rs.getInt("id_produit");
	            int quantiteMinimale = rs.getInt("quantite_minimal");
	            String dateDebut = rs.getString("date_debut");
	            String dateFin = rs.getString("date_fin");
	            double prix = rs.getDouble("prix");

	            // Crée l'objet Contrat et l'ajoute à la liste
	            Contrat contrat = new Contrat(siret, uniteExprimee, nomProduit,  idProduit, quantiteMinimale, dateDebut, dateFin, prix);
	            contrats.add(contrat);
	        }
	    }
	    
	    return contrats; 
	}
	
	
	//Récupère les Produits d'un fournisseur
	public ArrayList<Produit> getProduitdeFournisseur(int siret) throws Exception 
	{
	  
	    String query = "SELECT id_produit, id_lot_achat, nom, description, categorie, prix_vente_actuel " +
	                   "FROM Produit NATURAL JOIN Contrat " +
	                   "WHERE siret = ?"; 

	    ArrayList<Produit> produits = new ArrayList<>(); // Liste pour stocker les contrats associés

	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) {
	        ps.setInt(1, siret); 
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) 
	        { 
	            int id_produit = rs.getInt("id_produit");
	            int id_lot_achat = rs.getInt("id_lot_achat");
	            String nom = rs.getString("nom");
	            String description = rs.getString("description");
	            String categorie = rs.getString("categorie");
	            double prix_vente_actuel = rs.getFloat("prix_vente_actuel");
	           

	            // Crée l'objet Contrat et l'ajoute à la liste
	            Produit produit = new Produit(id_lot_achat, nom, description,  categorie, prix_vente_actuel);
	            produits.add(produit);
	        }

	    }
	    
	    return produits;
	}
	    
	    	
	
	//
	public void modificationFournisseur(Fournisseur f) throws SQLException {
	   
	    String query = "UPDATE Fournisseur SET nom = ?, adresse = ?, email = ? WHERE siret = ?";
	    
	    
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) 
	    {
	        
	        ps.setString(1, f.getNom());
	        ps.setString(2, f.getAdresse());
	        ps.setString(3, f.getMail());
	        ps.setInt(4, f.getSiret()); 
	        
	        ps.executeUpdate();
	    }
	}
	
	
    public void modificationContrat(Contrat contrat, String cat, String desc, float prix) throws SQLException {
	   
	    String query = "UPDATE Contrat SET nom_produit = ?, unite_exprimee = ?, quantite_minimal = ?, " +
	                   "date_debut = ?, date_fin = ?, prix = ? WHERE id_produit = ?";

	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) 
	    {
	        ps.setString(1, contrat.getNomProduit());
	        ps.setString(2, contrat.getUniteExprimee());
	        ps.setInt(3, contrat.getQuantite_minimal());
	        ps.setString(4, contrat.getDate_debut());
	        ps.setString(5, contrat.getDate_fin());
	        ps.setDouble(6, contrat.getPrix());
	        ps.setInt(7, contrat.getId_produit()); 

	        ps.executeUpdate(); 
	    }
	    
	    // Mise à jour dans la table Produit
        String updateProduitQuery = "UPDATE Produit SET nom = ?, description = ?, categorie = ?, prix_vente_actuel = ? " +
                                    "WHERE id_produit = ?";
        try (PreparedStatement ps = connection.conn.prepareStatement(updateProduitQuery)) {
            ps.setString(1, contrat.getNomProduit());
            ps.setString(2, cat); 
            ps.setString(3, desc);
            ps.setDouble(4, prix);
            ps.setInt(5, contrat.getId_produit());
            ps.executeUpdate();
        }
	}
	
	
	
    public void modificationContactAssociee(ContactAssociee contact, int ancienTel) throws SQLException 
	{
	  
	        // Mise à jour dans la table Contact
	        String updateContactQuery = "UPDATE Contact SET nom = ?, prenom = ?, fonction = ?, telephone = ?, email = ? WHERE telephone = ?";
	        try (PreparedStatement ps1 = connection.conn.prepareStatement(updateContactQuery)) 
	        {
	            ps1.setString(1, contact.getNom());
	            ps1.setString(2, contact.getPrenom());
	            ps1.setString(3, contact.getFonction());
	            ps1.setInt(4, contact.getTel()); // Nouveau téléphone pas reussi a changer le num dans les 2 table pareil pour supprimer un fournisseur
	            ps1.setString(5, contact.getMail());
	            ps1.setInt(6, ancienTel); // Ancien téléphone
	            ps1.executeUpdate();
	        }

	        // Mise à jour dans la table Fournisseur_contact
	        String updateFournisseurContactQuery = "UPDATE Fournisseur_contact SET telephone = ? WHERE telephone = ?";
	        try (PreparedStatement ps2 = connection.conn.prepareStatement(updateFournisseurContactQuery)) 
	        {
	            ps2.setInt(1, contact.getTel()); // Nouveau téléphone
	            ps2.setInt(2, ancienTel); // Ancien téléphone
	            ps2.executeUpdate();
	        }
	   
	}
	
	
	
	public ArrayList<Fournisseur> produitFournisseur(String produit) throws SQLException
	{
		 String updateContactQuery = "SELECT * FROM Contrat natural join Fournisseur Where Contrat.nom_produit = ?";
		
		 ArrayList<Fournisseur> f = new ArrayList<>();
		 try (PreparedStatement ps = connection.conn.prepareStatement(updateContactQuery)) 
	        {
	            ps.setString(1, produit);
	            ResultSet rs = ps.executeQuery();
	            
	            while (rs.next()) 
		        { 
		            String nom = rs.getString("nom");
		            int siret = rs.getInt("siret");
		            String adresse = rs.getString("adresse");
		            String mail = rs.getString("email");
		          
		            // Crée l'objet Contrat et l'ajouter à la liste
		            Fournisseur fournisseur = new Fournisseur(nom, siret, adresse, mail);
		            f.add(fournisseur);
		        }

		        if (f.isEmpty()) 
		        {
		            System.out.println("Aucun fournisseur ne propose se produit.");
		        }
	            
	        }
		 return f;
		
	}
	
	
	
	public ArrayList<Contrat> prixProduitFournisseur(String produit) throws Exception
	{
		 String updateContactQuery = "SELECT * FROM Contrat Where Contrat.nom_produit = ?";
		
		 ArrayList<Contrat> c = new ArrayList<>();
		 try (PreparedStatement ps = connection.conn.prepareStatement(updateContactQuery)) 
	        {
	            ps.setString(1, produit);
	            ResultSet rs = ps.executeQuery();
	            
	            while (rs.next()) 
		        { // Parcours de tous les résultats
		            int idcontrat = rs.getInt("idcontrat");
		            int siret = rs.getInt("siret");
		            String nomProduit = rs.getString("nom_produit");
		            String uniteExprimee = rs.getString("unite_exprimee");
		            int idProduit = rs.getInt("id_produit");
		            int quantiteMinimale = rs.getInt("quantite_minimal");
		            String dateDebut = rs.getString("date_debut");
		            String dateFin = rs.getString("date_fin");
		            double prix = rs.getDouble("prix");

		            // Crée l'objet Contrat et l'ajouter à la liste
		            Contrat contrat = new Contrat(siret, uniteExprimee, nomProduit,  idProduit, quantiteMinimale, dateDebut, dateFin, prix);
		            c.add(contrat);
		        }

		        
	            
	        }
		 return c;
		
	}
	
	
	
	public Produit infoProduitFournisseur(int contrat, String produit) throws Exception {
	    String query = "SELECT * FROM Produit natural join Contrat WHERE Produit.nom = ? and siret = ?";
	    Produit p = null;
	    
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) {
	        ps.setString(1, produit);
	        ps.setInt(2, contrat);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) 
	        { 
	            int idProduit = rs.getInt("id_produit");
	            String nomProduit = rs.getString("nom");
	            String description = rs.getString("description");
	            String categorie = rs.getString("categorie");
	            float prixVente = rs.getFloat("prix_vente_actuel");
	            
	            // Crée l'objet Produit et l'ajouter à la liste
	            p = new Produit(idProduit, nomProduit, description, categorie, prixVente);
	            
	        }
	    }
	    return p;
	}
	
		
	
	public void afficheDetaillesProduit(int num) throws Exception {
	    String query = "SELECT nom,description,prix_vente_actuel,categorie FROM Produit WHERE id_lot_achat = ?";
	    Produit p = null;
	    
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) {
	        ps.setInt(1, num);
	   
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) 
	        { 
	            
	            String nomProduit = rs.getString("nom");
	            String description = rs.getString("description");
	            String categorie = rs.getString("categorie");
	            float prixVente = rs.getFloat("prix_vente_actuel");
	            
	            System.out.println("Nom du produit : " + nomProduit + " - Description : " + description + " - Catégorie : " + categorie + " - Prix de vente : " + prixVente + "\n");
	            
	            
	        }
	    }
	    
	}
	
	
	public void updatePrixProduit(int numLot, float nouveauPrix) throws SQLException 
	{
	    String query = "UPDATE Produit SET prix_vente_actuel = ? WHERE id_lot_achat = ?";
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) 
	    {
	        ps.setFloat(1, nouveauPrix);
	        ps.setInt(2, numLot);
	        ps.executeUpdate();
	        System.out.println("Prix mis à jour avec succès !");
	    }
	    
	}
	
	
	public void insertVente( Vente v) throws SQLException 

	{
	    String query = "INSERT INTO Ticket (id_lot, prix, date_vente) VALUES (?, ?, ?)";
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) 
	    {
	        ps.setInt(1, v.getId_lot());
	        ps.setFloat(2, v.getPrix());
	        ps.setString(3, v.getDate_vente());
	        ps.executeUpdate();
	        
	     // Récupére l'ID généré automatiquement
	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                v.setTicket_caisse(1);
	                System.out.println("Insertion réussie.");
	            }
	        }
	        
	        System.out.println("Vente déclarée avec succès !");
	    }
	}
	
	
	
	public void supprimerVente(int idVente) throws SQLException {
	    String query = "DELETE FROM Ticket WHERE ticket_caisse = ?";
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) 
	    {
	        ps.setInt(1, idVente);
	        ps.executeUpdate();
	        System.out.println("Vente supprimée avec succès !");
	    }
	}


	
	public void afficheTotalJournalier(String date) throws SQLException
	{
		 String query = "select SUM(Ticket.prix) AS total_ventes FROM Ticket WHERE date_vente = ?;";
		 
		 try (PreparedStatement ps = connection.conn.prepareStatement(query))
		 {
			 ps.setString(1, date);
			 ResultSet rs = ps.executeQuery();
			 
			 if ( rs.next())
			 {
				 String total = rs.getString("total_ventes");
				 System.out.println("Total des ventes : " + total);
			 }
		 }
	}
	
	
	public void afficheClassementJournalier(String date) throws SQLException {
	    
	    String query = "SELECT ticket_caisse, id_lot, date_vente, prix FROM Ticket WHERE date_vente = ? ORDER BY prix DESC Limit 10;";

	    
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) {
	        ps.setString(1, date);  
	        ResultSet rs = ps.executeQuery();  

	        // Afficher les résultats sous forme de classement
	        System.out.println("Classement des ventes par prix :");
	        System.out.println("-------------------------------------------------");
	        System.out.printf("%-15s%-15s%-15s%-15s\n", "Ticket Caisse", "ID Lot", "Date Vente", "Prix");

	       
	        while (rs.next()) {
	            String ticket = rs.getString("ticket_caisse");
	            String id_lot = rs.getString("id_lot");
	            String date_vente = rs.getString("date_vente");
	            int prix = rs.getInt("prix");

	            // Affichage formaté des résultats
	            System.out.printf("%-15s%-15s%-15s%-15d\n", ticket, id_lot, date_vente, prix);
	        }
	    } catch (SQLException e) {
	        System.out.println("Erreur lors de l'affichage des classements : " + e.getMessage());
	    }
	}
	
	
	public void afficheTotalMensuel(String date) throws SQLException {
	    
	    String query = "SELECT SUM(Ticket.prix) AS total_ventes " +
	                   "FROM Ticket WHERE date_vente LIKE ?;";

	    // Construire le modèle de recherche pour le mois (format: "MM/YYYY")
	    String mois = "%" + date;

	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) {
	        // Remplir le paramètre de la requête avec le modèle de date
	        ps.setString(1, mois);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                String total = rs.getString("total_ventes");
	                System.out.println("Total des ventes pour le mois : " + total);
	            } else {
	                System.out.println("Aucune vente enregistrée pour le mois.");
	            }
	        }
	    }
	}
	
	
	public void afficheClassementMensuel(String date) throws SQLException {
	    
		String query = "SELECT SUM(Ticket.prix) AS total_ventes " +
                "FROM Ticket WHERE date_vente LIKE ? ORDER BY prix DESC Limit 10;";

	    
	    try (PreparedStatement ps = connection.conn.prepareStatement(query)) {
	        ps.setString(1, date);  
	        ResultSet rs = ps.executeQuery();  

	        // Afficher les résultats sous forme de classement
	        System.out.println("Classement des ventes par prix :");
	        System.out.println("-------------------------------------------------");
	        System.out.printf("%-15s%-15s%-15s%-15s\n", "Ticket Caisse", "ID Lot", "Date Vente", "Prix");

	        
	        while (rs.next()) {
	            String ticket = rs.getString("ticket_caisse");
	            String id_lot = rs.getString("id_lot");
	            String date_vente = rs.getString("date_vente");
	            int prix = rs.getInt("prix");

	            // Affichage formaté des résultats
	            System.out.printf("%-15s%-15s%-15s%-15d\n", ticket, id_lot, date_vente, prix);
	        }
	    } catch (SQLException e) {
	        System.out.println("Erreur lors de l'affichage des classements : " + e.getMessage());
	    }
	}


}

