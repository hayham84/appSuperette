package Exception;

public class LotProduitException extends ProduitException
{
	public LotProduitException(String message) 
	{

		super(" LotProduitException - " + message);
	}

}
