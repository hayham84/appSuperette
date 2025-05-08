package AMSuperette;

import java.util.HashMap;

public class VenteProduit implements IData
{
	private int ticket_caisse;
	private int id_produit;
	
	private String values ; 
	private HashMap<String, fieldType> map;
	
	
	public VenteProduit(int ticket_caisse, int id_produit) 
	{
		
		this.ticket_caisse = ticket_caisse;
		this.id_produit = id_produit;
		
		this.values = null;
		this.map = new HashMap<>();
	}
	
	//REMPLIE DANS LA CLASSE HashMap<String, fieldType> LE NOM DES Attributs ET leur type DE VARIABLE ET CREE LA CHAINE values ...
	public void getStruct() 
	{

	    map.put("ticket_caisse", fieldType.INT4);
	    map.put("id_produit", fieldType.INT4);
	    
	}

	
	//RETOURNE UNE CHAINE DE CARACTERE PRE-REMPLIE PERMETTANT DE COMPOSER LA REQUETE INSERT ...
	public String getValues() {

		return  values = "(" + ticket_caisse + ", " + id_produit +  ")";
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
	    return "Ticket numéro : " + ticket_caisse + ", Produit = " + id_produit;
	}


	public int getId_produit() {
		return id_produit;
	}


	public void setId_produit(int id_produit) {
		this.id_produit = id_produit;
	}

	public int getTicket_caisse() {
		return ticket_caisse;
	}

	public void setTicket_caisse(int ticket_caisse) {
		this.ticket_caisse = ticket_caisse;
	}


	
	
	
	

}
