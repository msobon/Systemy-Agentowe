import java.util.*;

/**
 * Class representing a single planet in the game.
 * All fields are public to speed up development.
 * Contains uninitilized fields for temporary meta information.
 */
public class Planet implements Cloneable {
    // Initializes a planet.
    public Planet(int planetID,
                  int owner,
		  int numShips,
		  int growthRate,
		  double x,
		  double y) {
	this.planetID = planetID;
	this.owner = owner;
	this.numShips = numShips;
	this.safety = numShips;
	this.growthRate = growthRate;
	this.x = x;
	this.y = y;
    }

    public int planetID;
    public int owner;
    public int numShips;
    public int growthRate;
    /**
     * The coordinates of the planet, used to calculate distance.
     */
    public double x, y;
    
    /**
     * 
     */
    public List<Fleet> incoming = new ArrayList<Fleet>();
    /**
     * An amount of ships on the planet, that the algorithms considers safe
     * to be moved to another planet without endangering this one.
     * The minimal amount of ships that need to be garrisoned to protect the planet.
     * Always less that the current number of ships on the planet.
     */
    public int safety;
    /**
     * Temporary field used to optimize the distance calculation mechanism.
     * Speeds up sorting. Causes the game state to be non-thread-safe.
     */
    public int tmp;

    private Planet (Planet _p) {
	planetID = _p.planetID;
	owner = _p.owner;
	numShips = _p.numShips;
	growthRate = _p.growthRate;
	x = _p.x;
	y = _p.y;
    }

    public Object clone() {
	return new Planet(this);
    }
}
