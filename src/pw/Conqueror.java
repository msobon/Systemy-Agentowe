package pw;
import java.util.*;

/**
 * A static-method helper class responsible for initiating battles
 * on the opponent's planets.
 */
public class Conqueror {
	private static double factor = 0.0;
	public static void setFactor(double f) {
		factor = f;
	}

	/**
	 * For each of your planets containing spare forces the algorithm
	 * searches for the nearest enemy planet and estimates if it can
	 * be taken over with these troops - possibly attacking.
	 */
	public static void conquer(PlanetWars pw) {
		for (Planet source : pw.myplanets) if (source.safety > 0) {
			Planet target = pw.getClosest(source,pw.enplanets);
			
			int requirement = target.numShips+target.tmp*target.growthRate;
			if (requirement <= source.safety) 
				pw.IssueOrder(source,target,source.safety);
			
		}
	}
	
	/**
	 * Different method of initiating attack, currently unused because
	 * it works only on enemies with poor defense algorithms.
	 * It searches for opponent's planets which it considers forsaken,
	 * and the enemy doesn't send any reinforcements there, possibly
	 * meaning his algorithm does not defend them.
	 * It attacks all these planets even if they are far away, most
	 * likely shattering any supply lines.
	 */
	public static void steal(PlanetWars pw) {
		//empiric factor, to be tuned
		int aggro = (int)factor+(pw.myForces/10);
		//find the forsaken
		List<Planet> forsaken = new ArrayList<Planet>();
		for (Planet p : pw.planets) {
	    	double defense = p.numShips;
	    	if (p.owner >= 2)
	   		if (defense < aggro)
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
			for (Planet source : pw.MyClosestPlanets(p)) {
				int power = p.numShips+1;
				int distance = PlanetWars.Distance(source,p);
				if (p.owner >= 2) power += p.growthRate*distance;
				if ( (power < aggro)&&(power <= source.safety) ) {
					pw.IssueOrder(source,p,power);
					break;
				}
			}
		}
	}
	
	/**
	 * Calculates the juicyness factor of the opponent's planet.
	 * Unlike neutral planets, a large fleet has a small effect on the factor.
	 * It was empirically deduced to usually attack planets with high growthRate.
	 */
	public static int juicyness(Planet p) {
		//empiric factor, to be tuned
		return p.growthRate*8-p.numShips;
	}
}
