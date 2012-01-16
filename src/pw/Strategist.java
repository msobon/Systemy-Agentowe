package pw;
import java.util.*;

/**
 * A static-method helper class that contains methods for furthering
 * long-term goals such as strategic position.
 */
public class Strategist {
	/**
	 * Shifts idle armies toward the opponent to make future moves easier.
	 * Moves them closer to the enemy, but has very strict limitations
	 * in order not to compromise safety by temporarily immobilizing fleets.
	 */
	public static void microShift(PlanetWars pw) {
		ArrayList<Planet> myplanets = pw.myplanets;
		ArrayList<Planet> hisplanets = pw.enplanets;
		
		if ( (myplanets.size() < 3)||(hisplanets.size() < 1) ) return;
		for (Planet p : pw.planets) if (p.owner == 1) {
			pw.sortByClosest(p,myplanets);
			Planet myclosest = myplanets.get(1);
			Planet hisclosest = pw.getClosest(p,hisplanets);
			if ( (myclosest.tmp * 2 < hisclosest.tmp)
			&&   (pw.Distance(myclosest,hisclosest) < hisclosest.tmp) )
				pw.IssueOrder(p,myclosest,p.safety);
			else {
				myclosest = myplanets.get(2);
				if ( (myclosest.tmp * 2 < hisclosest.tmp)
				&&   (pw.Distance(myclosest,hisclosest) < hisclosest.tmp) )
					pw.IssueOrder(p,myclosest,p.safety);
			}
		}
	}
}
