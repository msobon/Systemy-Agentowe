package pw;
import java.util.*;

/**
 * A static-method helper class containing methods for improving
 * the defense of the player and moving defensive fleets.
 */
public class Protector {

	/**
	 * Recalculates safety of planets based not on incoming fleets,
	 * but on the proximity of enemy planets and possible moves.
	 * This is not a precise algorithm, it performs an estimation.
	 * Assumes the opponent does not have an algorithm for combined
	 * simultaneous attacks and counter-prediction.
	 */
	public static void calcSafety(PlanetWars pw) {
		//include stationary troops in safety calculations
		for (Planet p : pw.enplanets) {
			Planet trgt = pw.getClosest(p,pw.myplanets);
			//assume opponent attacks from 1 planet only
			int threat = p.numShips-trgt.tmp*trgt.growthRate;
			if (threat > 0) trgt.safety -= threat;
		}
	}


	/*deprecated*/
	/**
	 * Deprecated.
	 */
	public static void protect(PlanetWars pw, List<ComplexOrder> l) {
		//todo: sort by importance
		for (ComplexOrder c : l) callReinforcements(pw,c.target,c.eta,c.strength);
	}
	
	/**
	 * Deprecated.
	 */
	public static void strike(PlanetWars pw, List<ComplexOrder> l) {
		//todo: sort by importance
		for (ComplexOrder c : l) preciseStrike(pw,c.target,c.eta,c.strength);
	}
	
	/**
	 * Deprecated.
	 */
	public static void preciseStrike(PlanetWars pw, Planet p, int eta, int str) {
		Planet source = null;
		for (Planet p2 : pw.MyClosestPlanets(p))
			if ( (p != p2)&&(p2.safety >= str)&&(PlanetWars.Distance(p,p2) == eta) ) {
				source = p2;
				break;
			}
		if (source != null) pw.IssueOrder(source,p,str);
	}
	
	/**
	 * Deprecated.
	 */
	public static void callReinforcements(PlanetWars pw, Planet p, int eta, int str) {
		Planet source = null;
		for (Planet p2 : pw.MyClosestPlanets(p))
			if ( (p != p2)&&(p2.safety >= str)&&(PlanetWars.Distance(p,p2) <= eta) ) {
				source = p2;
				break;
			}
		if (source == null) {
			//there is no hope, order evacuation
			//p.safety = p.numShips;
			
			//there is no hope, fight to the last man
			p.safety = 0;
			return;
		}
		pw.IssueOrder(source,p,str);
	}
	

}
