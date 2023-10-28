package exceptions;

/**
 * Ett {@link Exception} som sl�ngs ifall ett problem med att generera tr�det uppst�r. Kan vara
 * att ett felaktigt uttryck matades in. 'index' sparar vart i originalstr�ngen felet intr�ffade.
 * 
 * @author ErRi0401
 *
 */
public class TreeException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private int index;

 	public TreeException() { super(); }
    public TreeException(String message) { super(message); }
    public TreeException(String message, Throwable cause) { super(message, cause); }
    public TreeException(Throwable cause) { super(cause); }
    public TreeException(String message, int index) { super(message); this.index = index; }
    public TreeException(String message, Throwable cause, int index) { super(message, cause); this.index = index;}

    
    public int getIndex(){return index;}
}
