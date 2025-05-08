package Exception;

public class ProduitException extends ContratException
{
	public ProduitException(String message) 
	{

		super(" ProduitException - " + message);
	}

}
