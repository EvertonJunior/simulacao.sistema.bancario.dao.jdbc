package banco.model.entities;

public class TransacaoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public TransacaoException(String msg) {
		super(msg);
	}

}
