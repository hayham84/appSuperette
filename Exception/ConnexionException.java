package Exception;

public class ConnexionException extends Exception
{
	public ConnexionException(String message) 
	{

		super(" ConnexionException - " + message);
	}

}
