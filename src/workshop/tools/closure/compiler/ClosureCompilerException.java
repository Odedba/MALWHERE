package workshop.tools.closure.compiler;

/**
 * Exception thrown on failed compilation by closure compiler.
 */
public class ClosureCompilerException extends Exception{
	private static final long serialVersionUID = 1L;

	public ClosureCompilerException(String message){
		super(message);
	}
	
}
