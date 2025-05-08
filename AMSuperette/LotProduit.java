package AMSuperette;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class LotProduit implements IData {

	private int id_lot;
    private float prix;
    private int quantite;
    private String date_achat;
    private String date_peremption;

    private String values;
    private HashMap<String, fieldType> map;

    public LotProduit(float prix, int quantite, String date_achat, String date_peremption) throws SQLException {
        this.prix = prix;
        this.quantite = quantite;
        this.date_achat = date_achat;
        this.date_peremption = date_peremption;

        this.values = null;
        this.map = new HashMap<>();
        
        
    }

    
    public void getStruct() {
        map.put("id_lot", fieldType.INT4); 
        map.put("prix", fieldType.FLOAT8); 
        map.put("quantite", fieldType.INT4); 
        map.put("date_achat", fieldType.VARCHAR);
        map.put("date_peremption", fieldType.VARCHAR);
    }

    // Retourne les valeurs au format SQL pour l'insertion
    

    // Retourne les valeurs sans l'ID, utile pour les insertions sans l'ID auto-incrémenté
    public String getValues() {
        return values = "(" + id_lot + "," + prix + ", " + quantite + ", '" + date_achat + "', '" + date_peremption + "')";
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

    // Méthode toString pour afficher les informations de l'objet
    @Override
    public String toString() {
        return "Lot_produit{" +
                "id_lot=" + id_lot +
                ", prix=" + prix +
                ", quantite=" + quantite +
                ", date_achat='" + date_achat + '\'' +
                ", date_peremption='" + date_peremption + '\'' +
                '}';
    }

    // Getters et setters
    public int getId_lot() {
        return id_lot;
    }

    public void setId_lot(int id_lot) {
        this.id_lot = id_lot;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getDate_achat() {
        return date_achat;
    }

    public void setDate_achat(String date_achat) {
        this.date_achat = date_achat;
    }

    public String getDate_peremption() {
        return date_peremption;
    }

    public void setDate_peremption(String date_peremption) {
        this.date_peremption = date_peremption;
    }

   

    
}