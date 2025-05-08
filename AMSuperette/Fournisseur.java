package AMSuperette;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class Fournisseur implements IData {
	
	private String nom;
	private int siret;
	private String adresse;
	private String mail;
	private ArrayList<Contrat> contrat;
	private ArrayList<ContactAssociee> contact;
	private ArrayList<Produit> produit;
	
	private String values ; 
	private HashMap<String, fieldType> map;

	
	
	public Fournisseur(String nom, int siret, String adresse, String mail) 
	{
		this.nom = nom;
		this.siret = siret;
		this.adresse = adresse;
		this.mail = mail;
		this.contrat = new ArrayList<>(); 
        this.contact = new ArrayList<>(); 
        
        
		this.values = null;
		this.map = new HashMap<String, fieldType>();
	}

	
	
	
	//REMPLIE DANS LA CLASSE HashMap<String, fieldType> LE NOM DES Attributs ET leur type DE VARIABLE ET CREE LA CHAINE values ...
	public void getStruct() 
	{

	    map.put("nom", fieldType.VARCHAR);
	    map.put("siret", fieldType.INT4);
	    map.put("adresse", fieldType.VARCHAR);
	    map.put("mail", fieldType.VARCHAR);
	    
	}

	
	//RETOURNE UNE CHAINE DE CARACTERE PRE-REMPLIE PERMETTANT DE COMPOSER LA REQUETE INSERT ...
	public String getValues() {

		return  values = "('" + nom + "', " + siret + ", '" +
				adresse + "', '" + mail +  "')";
	}
	

	//GETTER DE LA MAP CREE AVEC LA METHODE getStruct ...
	public HashMap<String, fieldType> getMap() {
		for (String i : map.keySet())
		{
			fieldType f = map.get(i);
		}
		return map;
	}

	
	//METHODE PERMETTANT DE VERIFIER QUE LA TABLE ET L'INSTANCE PARTAGE LES MEMES ATTRIBUTS ET MEMES TYPES
	//PREND EN PARAMETRE LA MAP ATTRIBUT/TYPE DE LA TABLE ...
	public boolean check(HashMap<String, fieldType> tableStruct) 
	{
	    // Vérifie si la structure actuelle correspond à celle de la table
	    if (map == null) 
	    {
	        getStruct(); // Initialise map si ce n'est pas déjà fait
	    }
	    return map.equals(tableStruct);
	}

	
	public String toString()
	{	
	    return "Fournisseur : " + nom + ", siret = " + siret + ", adresse = " + adresse +
	           ", mail = '" + mail;
	}
	


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public int getSiret() {
		return siret;
	}


	public void setSiret(int siret) {
		this.siret = siret;
	}


	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}
	
	
	public ArrayList<Contrat> getContrat() {
        return contrat;
    }

    public void setContrat(ArrayList<Contrat> contrat) {
        this.contrat = contrat;
    }

    public ArrayList<ContactAssociee> getContact() {
        return contact;
    }

    public void setContact(ArrayList<ContactAssociee> contact) {
        this.contact = contact;
    }

    
	public ArrayList<Produit> getProduit() {
		return produit;
	}


	public void setProduit(ArrayList<Produit> produit) {
		this.produit = produit;
	}
    
    

}
