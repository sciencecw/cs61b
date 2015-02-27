package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

/** An implementation of a motile pacifist photosynthesizer.
 *  @author Josh Hug
 */
public class Clorus extends Creature {
    /** red color. */
    private int r=34;
    /** green color. */
    private int g=0;
    /** blue color. */
    private int b=231;

    /** creates plip with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        energy = e;
    }

    /** creates a plip with energy equal to 1. */
    public Clorus() {
        this(1);
    }

    /** Should return a color with red = 99, blue = 76, and green that varies
     *  linearly based on the energy of the Plip. If the plip has zero energy,
     *  it should have a green value of 63. If it has max energy, it should
     *  have a green value of 255. The green value should vary with energy
     *  linearly in between these two extremes. It's not absolutely vital
     *  that you get this exactly correct.
     */
    public Color color() {
        return color(r, g, b);
    }

    public void attack(Creature c) {
	energy+=c.energy();
    }

    /** Plips should lose 0.15 units of energy when moving. If you want to
     *  to avoid the magic number warning, you'll need to make a
     *  private static final variable. This is not required for this lab.
     */
    public void move() {
	energy-=0.03;
    }


    /** Plips gain 0.2 energy when staying due to photosynthesis. */
    public void stay() {
	energy-=0.01;
    }

    /** Plips and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Plip.
     */
    public Clorus replicate() {
	energy=energy/2;
        return new Clorus(energy);
    }

    /** Plips take exactly the following actions based on NEIGHBORS:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if energy >= 1, REPLICATE.
     *  3. Otherwise, if any Cloruses, MOVE with 50% probability.
     *  4. Otherwise, if nothing else, STAY
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
	List<Direction> empties = getNeighborsOfType(neighbors, "empty");
	List<Direction> plips = getNeighborsOfType(neighbors, "plip");
	if (empties.size()==0){
		return new Action(Action.ActionType.STAY);
	}
	if (plips.size()>0){
		Direction attackDir = HugLifeUtils.randomEntry(plips);
		return new Action(Action.ActionType.ATTACK, attackDir);
	}
	if (energy>1.0){
		Direction repDir = HugLifeUtils.randomEntry(empties);
		return new Action(Action.ActionType.REPLICATE, repDir);
	}
	Direction moveDir = HugLifeUtils.randomEntry(empties);
	return new Action(Action.ActionType.MOVE, moveDir);
    }
}
