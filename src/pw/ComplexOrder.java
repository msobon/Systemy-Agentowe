package pw;
import java.util.*;

/**
 * Class representing the decision to make a move/attack on a planet.
 * Contains the target, the number of ships required and the time at
 * which the attack should commence.
 */
public class ComplexOrder {
	//target of attack-move
	public Planet target;
	
	//amount of ships required
	public int strength;
	
	//maximum realization time
	public int eta;
	
	public ComplexOrder() {}
	public ComplexOrder(Planet target, int strength, int eta) {
		this.target = target;
		this.strength = strength;
		this.eta = eta;
	}
	
	/**
	 * Calls the fastMove method on all elements of the list.
	 * Required to defend multiple planets.
	 */
	public static void protect(PlanetWars pw, List<ComplexOrder> l) {
		//todo: sort by importance
		for (ComplexOrder c : l) c.fastMove(pw);
	}
	
	/**
	 * Calls the strictMove method on all elements of the list.
	 * Required in attacks where the prediction engine needs precision.
	 */
	public static void strike(PlanetWars pw, List<ComplexOrder> l) {
		//todo: sort by importance
		for (ComplexOrder c : l) c.strictMove(pw);
	}
	
	/**
	 * Tries to issue the order to the game engine so that the estimated time to arrival
	 * is exactly as requested by the order, not a turn earlier.
	 * Required for cheap stealing neutral planets from the opponent.
	 */
	public void strictMove(PlanetWars pw) {
		Planet source = null;
		for (Planet p2 : pw.MyClosestPlanets(target))
			if ( (target != p2)&&(p2.safety >= strength)&&(PlanetWars.Distance(target,p2) == eta) ) {
				source = p2; break;
			}
		if (source != null) pw.IssueOrder(source,target,strength);
	}
	
	/**
	 * Tries to issue the order to the game engine so that the estimated time to arrival
	 * is at least as requested by the order. Searches for source planets containing the
	 * required amount of troops.
	 */
	public boolean fastMove(PlanetWars pw) {
		Planet source = null;
		for (Planet p2 : pw.MyClosestPlanets(target))
			if ( (target != p2)&&(p2.safety >= strength)&&(PlanetWars.Distance(target,p2) <= eta) ) {
				source = p2; break;
			}
		if (source == null) {
			//there is no hope, order evacuation, so p.safety = p.numShips;
			//there is no hope, fight to the last man, then p.safety = 0;
			return false;
		} else {
			pw.IssueOrder(source,target,strength);
			return true;
		}
	}
}
