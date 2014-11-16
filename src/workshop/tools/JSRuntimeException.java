package workshop.tools;

/**
 * Exception thrown on failed evaluation of JavaScript code.
 */
public class JSRuntimeException extends Exception{
	private static final long serialVersionUID = 1L;

	public JSRuntimeException(String message){
		super(message);
	}
}
