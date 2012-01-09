import java.util.*;

/**
 * A static-method helper class responsible for predicting the outcomes of all future
 * battles based on currently flying fleets.
 */
public class Oracle {

	/**
	 * Sorts the incoming fleets for each planet.
	 * After it is called, each planet contains chronologically ordered list
	 * of all incoming fleets by the number of turns to arrival.
	 */
	public static void sortFleets(PlanetWars pw) {
		Comparator<Fleet> comparator = new Comparator<Fleet>()
{public int compare(Fleet f1, Fleet f2){return f1.turnsRemaining-f2.turnsRemaining;}};
		for (Planet p : pw.planets)
			Collections.sort(p.incoming,comparator);
	}

	/**
	 * Calculates all future battles and calls reinforcements.
	 * For each planet this algorithm analyzes all incoming fleets to
	 * determine if the defense and natural growth is strong enough to hold.
	 * If it finds that the opponent will take our planet, it calls for an
	 * appropiate amount of reinforcements (minimum required to hold).
	 * If it finds that the opponent will capture a neutral planet, it will
	 * call for a counter-strike to take away that planet with minimum loss
	 * by sending a fleet so that it arrives exactly one turn after the
	 * opponent, after he lost his forces figthing neutrals.
	 * If it finds that as a result of a failed attack on the opponent's
	 * planet, he will be left with relatively small forces, it will call
	 * for for reinforcements.
	 * While analyzing incoming battles, it calculates the safety of each
	 * planet - the minimal amount of forces which have to stay on the
	 * planet to defend it and can't be sent help others / attack.
	 */
	public static void analyzeBattles(PlanetWars pw) {
		List<ComplexOrder> defRequest = new ArrayList<ComplexOrder>();
		List<ComplexOrder> atkRequest = new ArrayList<ComplexOrder>();
		
		for (Planet p : pw.planets)
		if (p.incoming.size() > 0) {
			int time = 0;
			int strength = p.numShips;
			int owner = p.owner;
			
			Iterator<Fleet> it = p.incoming.iterator();
			Fleet f = null;
			Fleet nextf = it.hasNext() ? it.next() : null;
			
			while (nextf != null) {
				int mystr = 0;
				int enstr = 0;
				
				do { //accumulate all fleets incoming at time unit
					f = nextf;
					nextf = it.hasNext() ? it.next() : null;
					if (f.owner == 1) mystr += f.numShips;
					if (f.owner >= 2) enstr += f.numShips;	
				} while ( (nextf != null)&&(nextf.turnsRemaining == f.turnsRemaining) );
				
				//apply growth to planet
				strength += p.growthRate*(f.turnsRemaining-time)*(owner==0?0:1);
				time = f.turnsRemaining;
				
				//resolve battle
				int oldowner = owner;
				if (owner == 0) {
					if (strength<mystr && enstr<mystr)
						{owner=1; strength=mystr-(strength>enstr?strength:enstr);}
					else
					if (strength<enstr && mystr<enstr)
						{owner=2; strength=enstr-(strength>mystr?strength:mystr);}
					else
					if (mystr<strength && enstr<strength)
						{owner=0; strength-=(mystr>enstr?mystr:enstr);}
					else
					if (mystr==enstr)
						{owner=0; strength=(strength>mystr?strength-mystr:0);}
				} else {
					strength = mystr - enstr + strength*(owner==1?1:-1);
					if (owner == 1) {
						if (strength < 0) {
							owner = 2; strength *= -1; p.safety = 0;							
						} else if (p.safety>strength) p.safety=strength;
					} else {
						if (strength > 0) {
							owner = 1;
						}
					}
				}
				
				//take action
				int aggro = 10;
				
				if (oldowner==1 && owner==2)
					defRequest.add(new ComplexOrder(p,strength,time));
				else if (oldowner==0 && owner==2)
					atkRequest.add(new ComplexOrder(p,strength+p.growthRate+1,time+1));
				else if (owner==2 && strength<aggro) {
					atkRequest.add(new ComplexOrder(p,strength+1,time));
					atkRequest.add(new ComplexOrder(p,strength+p.growthRate+1,time+1));
				}

			}
		}
		
		ComplexOrder.protect(pw,defRequest);
		ComplexOrder.strike(pw,atkRequest);
	}
	
	/**
	 * Gives the integer representing owner.
	 * @param p planet to be analysed
	 * @return If it is ours returns 1, if neutral returns 0, if enemy returns -1.
	 */
	public static int sign(Planet p) {
		if (p.owner == 1) return 1;
		else if (p.owner == 0) return 0;
		else return -1;
	}
}
