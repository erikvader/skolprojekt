package associativity;

/**
 * Ett interface för att på ett ganska snyggt sätt kunna få associativity att funka
 * 
 * @author ErRi0401
 *
 */
public interface Associativity {

	/**
	 * Avgör i fall 'prio' ska ersätta 'highestPrio'. Görs på olika sätt beroende på vilken associativity man vill ha.
	 * 
	 * @param highestPrio nuvarande högsta prioritet
	 * @param prio den nya prioriteten som ska testas
	 * @return returnar true ifall 'prio' ska ersätta 'highestPrio'
	 */
	public boolean determine(int highestPrio, int prio);
}
