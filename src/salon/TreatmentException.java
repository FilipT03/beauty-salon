package salon;

public class TreatmentException extends Exception{
	private static final long serialVersionUID = -864999009570405459L;
	
	public TreatmentException(String message) {
		super(message);
	}
	public TreatmentException(Throwable cause) {
		super(cause);
	}
	public TreatmentException(String message, Throwable cause) {
		super(message, cause);
	}
}
