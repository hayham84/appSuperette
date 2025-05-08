package AMSuperette;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Contrat implements IData {
	
	private static int cpt = 0;

    private int idContrat;          // Correspond à idcontrat
    private int siret;              // Correspond à siret dans la table
    private String nomProduit;      // Correspond à nom_produit
    private String uniteExprimee;   // Correspond à unite_exprimee
    private int id_produit;         // Correspond à id_produit
    private int quantite_minimal;   // Correspond à quantite_minimal
    private String date_debut;      // Correspond à date_debut
    private String date_fin;        // Correspond à date_fin
    private double prix;            // Correspond à prix (double precision)

    private String values;
    private HashMap<String, fieldType> map;

    public Contrat(int fournisseur, String uniteExprimee, String nomProduit, int id_produit, int quantite_minimal,
            String date_debut, String date_fin, double prix) throws Exception 
	    {
				 this.siret = fournisseur;
				 this.uniteExprimee = uniteExprimee;
				 this.nomProduit = nomProduit;
				 this.id_produit = id_produit;
				 this.quantite_minimal = quantite_minimal;
				 this.date_debut = date_debut;
				 this.date_fin = date_fin;
				 this.prix = prix;
				 this.map = new HashMap<>();
	}


    // REMPLIT LA MAP AVEC LES ATTRIBUTS ET TYPES DE LA CLASSE
    public void getStruct() {
        map.put("idcontrat", fieldType.INT4);
        map.put("siret", fieldType.INT4);  // Correctement lié à siret
        map.put("nom_produit", fieldType.VARCHAR);
        map.put("unite_exprimee", fieldType.VARCHAR);
        map.put("id_produit", fieldType.INT4);
        map.put("quantite_minimal", fieldType.INT4);
        map.put("date_debut", fieldType.VARCHAR);
        map.put("date_fin", fieldType.VARCHAR);
        map.put("prix", fieldType.FLOAT8); // Correspond à double precision
    }

    // RETOURNE LES VALEURS DE L'OBJET FORMATEES POUR UNE REQUÊTE SQL
    public String getValues() {
        return "(" +
               siret + ", '" +
               nomProduit + "', '" +
               uniteExprimee + "', " +
               (id_produit != 0 ? id_produit : 0) + ", " +
               quantite_minimal + ", '" +
               date_debut + "', '" +
               date_fin + "', " +
               prix + ")";
    }
    
 // GETTER DE LA STRUCTURE
    public HashMap<String, fieldType> getMap() {
    	for (String i : map.keySet())
		{
			fieldType f = map.get(i);
		}
		return map;
    }

    // VERIFIE SI LA STRUCTURE DE L'OBJET CORRESPOND A LA STRUCTURE DE LA TABLE
    public boolean check(HashMap<String, fieldType> tableStruct) {
    	// Vérifie si la structure actuelle correspond à celle de la table
	    if (map == null) 
	    {
	        getStruct(); // Initialise map si ce n'est pas déjà fait
	    }
	    return map.equals(tableStruct);
    }

    @Override
    public String toString() {
        return "Contrat{" +
                "idContrat=" + idContrat +
                ", nomProduit='" + nomProduit + '\'' +
                ", uniteExprimee='" + uniteExprimee + '\'' +
                ", id_produit=" + id_produit +
                ", quantite_minimal=" + quantite_minimal +
                ", date_debut='" + date_debut + '\'' +
                ", date_fin='" + date_fin + '\'' +
                ", prix=" + prix +
                '}';
    }

    // GETTERS ET SETTERS
    public int getIdContrat() {
        return idContrat;
    }

    public void setIdContrat(int idContrat) {
        this.idContrat = idContrat;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getUniteExprimee() {
        return uniteExprimee;
    }

    public void setUniteExprimee(String uniteExprimee) {
        this.uniteExprimee = uniteExprimee;
    }

    public int getId_produit() {
        return id_produit;
    }

    public void setId_produit(int id_produit) {
        this.id_produit = id_produit;
    }

    public int getQuantite_minimal() {
        return quantite_minimal;
    }

    public void setQuantite_minimal(int quantite_minimal) {
        this.quantite_minimal = quantite_minimal;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

	public int getSiret() {
		return siret;
	}

	public void setSiret(int siret) {
		this.siret = siret;
	}
    
    
}
