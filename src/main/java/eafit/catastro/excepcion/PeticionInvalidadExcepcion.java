package eafit.catastro.excepcion;

public class PeticionInvalidadExcepcion extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PeticionInvalidadExcepcion() {
		super();
	}

	public PeticionInvalidadExcepcion(String message, Throwable cause) {
		super(message, cause);
	}

	public PeticionInvalidadExcepcion(String message) {
		super(message);
	}

	public PeticionInvalidadExcepcion(Throwable cause) {
		super(cause);
	}

}
