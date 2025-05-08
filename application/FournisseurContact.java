package AMSuperette;


import java.util.HashMap;

public class FournisseurContact implements IData {
    
    private int id_fournisseur;
    private int id_contact;
    
    private String values;
    private HashMap<String, fieldType> map;
    
    // Constructeur de la classe
    public FournisseurContact(int id_fournisseur, int id_contact) {
        this.id_fournisseur = id_fournisseur;
        this.id_contact = id_contact;
        
        this.values = null;
        this.map = new HashMap<>();
    }
    
    // Implémentation de la méthode getStruct() qui définit la structure de la table Fournisseur_Contact
    public void getStruct() {
        map.put("id_fournisseur", fieldType.INT4);
        map.put("id_contact", fieldType.INT4);
    }

    // Implémentation de la méthode getValues() pour récupérer les valeurs sous forme de chaîne SQL
   
    public String getValues() {
        // Retourne les valeurs de l'objet sous le format pour une requête SQL
        return values = "(" + id_fournisseur + ", " + id_contact + ")";
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
        return "Fournisseur_contact{" +
                "id_fournisseur=" + id_fournisseur +
                ", id_contact=" + id_contact +
                '}';
    }
    
    // Getters et Setters
    public int getId_fournisseur() {
        return id_fournisseur;
    }

    public void setId_fournisseur(int id_fournisseur) {
        this.id_fournisseur = id_fournisseur;
    }

    public int getId_contact() {
        return id_contact;
    }

    public void setId_contact(int id_contact) {
        this.id_contact = id_contact;
    }

    
}