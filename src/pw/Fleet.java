package pw;
/**
 * Class representing a single fleet travelling between two planets.
 * Contains all information accessible from the input.
 */
public class Fleet implements Comparable, Cloneable {
    // Initializes a fleet.
    public Fleet(int owner,
		 int numShips,
		 int sourcePlanet,
		 int destinationPlanet,
		 int totalTripLength,
		 int turnsRemaining) {
	this.owner = owner;
	this.numShips = numShips;
	this.sourcePlanet = sourcePlanet;
	this.destinationPlanet = destinationPlanet;
	this.totalTripLength = totalTripLength;
	this.turnsRemaining = turnsRemaining;
    }

    // Initializes a fleet.
    public Fleet(int owner,
		 int numShips) {
	this.owner = owner;
	this.numShips = numShips;
	this.sourcePlanet = -1;
	this.destinationPlanet = -1;
	this.totalTripLength = -1;
	this.turnsRemaining = -1;
    }

    // Accessors and simple modification functions. These should be mostly
    // self-explanatory.
    public int getOwner() {
	return owner;
    }

    public int getNumShips() {
	return numShips;
    }

    public int getSourcePlanet() {
	return sourcePlanet;
    }

    public int getDestinationPlanet() {
	return destinationPlanet;
    }

    public int getTotalTripLength() {
	return totalTripLength;
    }

    public int getTurnsRemaining() {
	return turnsRemaining;
    }

    public void RemoveShips(int amount) {
	numShips -= amount;
    }

    // Subtracts one turn remaining. Call this function to make the fleet get
    // one turn closer to its destination.
    public void TimeStep() {
	if (turnsRemaining > 0) {
	    --turnsRemaining;
	} else {
	    turnsRemaining = 0;
	}
    }

    @Override
    public int compareTo(Object o) {
	Fleet f = (Fleet)o;
	return this.numShips - f.numShips;
    }

    public int owner;
    public int numShips;
    public int sourcePlanet;
    public int destinationPlanet;
    public int totalTripLength;
    public int turnsRemaining;
	
	private Fleet(Fleet _f) {
		owner = _f.owner;
		numShips = _f.numShips;
		sourcePlanet = _f.sourcePlanet;
		destinationPlanet = _f.destinationPlanet;
		totalTripLength = _f.totalTripLength;
		turnsRemaining = _f.turnsRemaining;
	}
	public Object clone() {
		return new Fleet(this);
	}
}
