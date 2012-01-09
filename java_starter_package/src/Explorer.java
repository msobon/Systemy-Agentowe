import java.util.*;

/**
 * A static-method class reponsible for capturing neutral planets.
 */
public class Explorer {
	/**
	 * Responsible for determining which neutral planets to attack and when.
	 * First calculates the aggro factor, based on the strength of it's forces.
	 * The aggro factor is the minimum juicyness factor of a neutral planet to be attacked.
	 * Juicyness factor is the ratio of growthRate and the number of defending forces.
	 * As the game goes on and we have more forces, the algorithm attacks bolder.
	 * Attacks planets with juicyness first in order to maximise the growthrate with
	 * a minimal loss of forces.
	 */
	public static void explore(PlanetWars pw) {
		//empiric factor, to be tuned
		int aggro = 1000/(13+pw.myForces/100);
		//find the forsaken
		List<Planet> forsaken = new ArrayList<Planet>();
		for (Planet p : pw.planets) {
	    	if (p.owner == 0)
	   		if (juicyness(p) > aggro)
	   		if (p.incoming.size() == 0) {
	   			forsaken.add(p);
	   		}
		}
		//order of relevance
		Comparator<Planet> comparator = new Comparator<Planet>()
{public int compare(Planet p1, Planet p2){return juicyness(p2)-juicyness(p1);}};
		Collections.sort(forsaken,comparator);
		
		//filter and capture
		for (Planet p : forsaken) {
			for (Planet source : pw.MyClosestPlanets(p)) if (p.numShips < source.safety) {
				int distance = PlanetWars.Distance(source,p);
				boolean safe = true;
				for (Planet pe : pw.planets)
					if ((pe.owner >= 2) && (PlanetWars.Distance(p,pe) <= distance))
							{safe = false; break;}
				if (safe) {pw.IssueOrder(source,p,p.numShips+1);break;}
			}
		}
	}
	
	/**
	 * Calculates the juicyness factor of the planet.
	 * It is the ratio of it's growthRate and defending forces.
	 */
	public static int juicyness(Planet p) {
		//empiric factor, to be tuned
		return p.growthRate*1000/p.numShips;
	}
}
