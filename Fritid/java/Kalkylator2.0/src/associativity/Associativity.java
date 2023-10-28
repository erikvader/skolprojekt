package associativity;

/**
 * Ett interface f�r att p� ett ganska snyggt s�tt kunna f� associativity att funka
 * 
 * @author ErRi0401
 *
 */
public interface Associativity {

	/**
	 * Avg�r i fall 'prio' ska ers�tta 'highestPrio'. G�rs p� olika s�tt beroende p� vilken associativity man vill ha.
	 * 
	 * @param highestPrio nuvarande h�gsta prioritet
	 * @param prio den nya prioriteten som ska testas
	 * @return returnar true ifall 'prio' ska ers�tta 'highestPrio'
	 */
	public boolean determine(int highestPrio, int prio);
}
