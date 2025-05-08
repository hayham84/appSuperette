package AMSuperette;


import java.util.ArrayList;
import java.util.HashMap;

public class Vente implements IData {
	
	
	private int ticket_caisse;
	private int id_lot;
	private String date_vente;
	private float prix;
	
	private String values ; 
	private HashMap<String, fieldType> map;
	
	
	public Vente(int ticket_caisse, int id_lot, String date_vente, float prix) 
	{
		
		this.ticket_caisse = ticket_caisse;
		this.id_lot = id_lot;
		this.date_vente = date_vente;
		this.prix = prix;
		
		
		this.values = null;
		this.map = new HashMap<>();
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

			return  values = "(" + ticket_caisse + ", " + id_lot + ", '" +
					date_vente + "', '" + prix +  ")";
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
		    return "Ticket numéro : " + ticket_caisse + ", Lot = " + id_lot + ", date de vente = " + date_vente +
		           ", prix = '" + prix;
		}


		public int getTicket_caisse() {
			return ticket_caisse;
		}


		public void setTicket_caisse(int ticket_caisse) {
			this.ticket_caisse = ticket_caisse;
		}


		public int getId_lot() {
			return id_lot;
		}


		public void setId_lot(int id_lot) {
			this.id_lot = id_lot;
		}


		public String getDate_vente() {
			return date_vente;
		}


		public void setDate_vente(String date_vente) {
			this.date_vente = date_vente;
		}


		public float getPrix() {
			return prix;
		}


		public void setPrix(float prix) {
			this.prix = prix;
		}


		public void setValues(String values) {
			this.values = values;
		}


		public void setMap(HashMap<String, fieldType> map) {
			this.map = map;
		}
		

		

}

