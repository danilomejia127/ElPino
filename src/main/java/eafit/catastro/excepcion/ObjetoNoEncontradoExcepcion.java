package eafit.catastro.excepcion;

public class ObjetoNoEncontradoExcepcion extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjetoNoEncontradoExcepcion() {
		super();
	}

	public ObjetoNoEncontradoExcepcion(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjetoNoEncontradoExcepcion(String message) {
		super(message);
	}

	public ObjetoNoEncontradoExcepcion(Throwable cause) {
		super(cause);
	}

}
