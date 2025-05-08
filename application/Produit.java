package AMSuperette;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Produit implements IData {
	
	
    
    private int id_produit; // L'ID sera géré automatiquement par la base de données
    private int id_lot_achat;
    private String nom;
    private String description;
    private String categorie;
    private double prix_vente_actuel;
    
    private String values; 
    private HashMap<String, fieldType> map;

    // Constructeur sans l'ID, qui sera généré automatiquement
    public Produit(int numéro_de_lot_achat, String nom, String description, String catégorie, double prix_vente_actuel) throws SQLException 
    {
        
        // Initialisation des autres attributs
        this.id_lot_achat = numéro_de_lot_achat;
        this.nom = nom;
        this.description = description;
        this.categorie = catégorie;
        this.prix_vente_actuel = prix_vente_actuel;
        
        
        this.values = null;
        this.map = new HashMap<String, fieldType>();
    }
    
    public void getStruct() {
        map.put("id_produit", fieldType.INT4); // La base de données s'en occupera
        map.put("id_lot_achat", fieldType.INT4);
        map.put("nom", fieldType.VARCHAR);
        map.put("description", fieldType.VARCHAR);
        map.put("catégorie", fieldType.VARCHAR);
        map.put("prix_vente_actuel", fieldType.FLOAT8);
    }

    public String getValues() 
    {
        return  values = "(" + id_lot_achat + ", '" +
                nom + "', '" + description + "', '" +
                categorie + "', " + prix_vente_actuel + ")";
    }

    public HashMap<String, fieldType> getMap() 
    {
        for (String i : map.keySet()) 
        {
            fieldType f = map.get(i);
        }
        return map;
    }

    public boolean check(HashMap<String, fieldType> tableStruct) 
    {
        if (map == null) 
        {
            getStruct();
        }
        return map.equals(tableStruct);
    }

    public String toString() 
    {    
        return "Produit : " + nom + " id_produit = " + id_produit + ", id_lot_achat = " + id_lot_achat +
               ", description = '" + description + '\'' + ", catégorie= '" + categorie + '\'' + ", prix_vente_actuel=" + prix_vente_actuel; 
    }

    public int getNuméro_unique() {
        return id_produit;
    }

    public void setNuméro_unique(int numéro_unique) {
        this.id_produit = numéro_unique;
    }

    public int getNuméro_de_lot_achat() {
        return id_lot_achat;
    }

    public void setNuméro_de_lot_achat(int numéro_de_lot_achat) {
        this.id_lot_achat = numéro_de_lot_achat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCatégorie() {
        return categorie;
    }

    public void setCatégorie(String catégorie) {
        this.categorie = catégorie;
    }

    public double getPrix_vente_actuel() {
        return prix_vente_actuel;
    }

    public void setPrix_vente_actuel(int prix_vente_actuel) {
        this.prix_vente_actuel = prix_vente_actuel;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public void setMap(HashMap<String, fieldType> map) {
        this.map = map;
    }
}