package AMSuperette;

import java.util.HashMap;

public class ContactAssociee implements IData {

    private String nom;
    private String prenom;
    private String fonction;
    private String mail;
    private int tel;

    private String values;
    private HashMap<String, fieldType> map;

    public ContactAssociee(String nom, String prenom, String fonction, String mail, int tel) {
        this.nom = nom;
        this.prenom = prenom;
        this.fonction = fonction;
        this.mail = mail;
        this.tel = tel;

        this.values = null;
        this.map = new HashMap<>();
    }

    // REMPLIT LA MAP AVEC LES ATTRIBUTS ET TYPES DE LA CLASSE
    public void getStruct() {
    	map.put("tel", fieldType.INT4);
        map.put("nom", fieldType.VARCHAR);
        map.put("prenom", fieldType.VARCHAR);
        map.put("fonction", fieldType.VARCHAR);
        map.put("mail", fieldType.VARCHAR);
    }

    // RETOURNE UNE CHAINE DE VALEURS FORMATÉE POUR UNE REQUÊTE SQL
    public String getValues() {
        return values = "(" + tel + ", '" + nom + "', '" + prenom + "', '" + fonction + "', '" + mail + "')";
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
        return "Contact_associee{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", fonction='" + fonction + '\'' +
                ", mail='" + mail + '\'' +
                ", tel=" + tel +
                '}';
    }

    // GETTERS ET SETTERS
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }
}