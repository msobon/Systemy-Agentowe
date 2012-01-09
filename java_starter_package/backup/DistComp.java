import java.util.*;

public class DistComp implements Comparator<Planet> {
	private Planet center;
	
	public DistComp(Planet center) {
		this.center = center;
	}
	
	public int compare(Planet p1, Planet p2) {
		return PlanetWars.Distance(p1,center)-PlanetWars.Distance(p2,center);
	}

}
