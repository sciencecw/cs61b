package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the plip class   
 *  @authr KakWong
 */

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34,0,231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.move();
        assertEquals(1.94, c.energy(), 0.01);
        c.stay();
        assertEquals(1.93, c.energy(), 0.01);
        c.stay();
        assertEquals(1.92, c.energy(), 0.01);
	Plip p =new Plip(2);
	c.attack(p);
        assertEquals(3.92, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);
	Clorus c2 = c.replicate();
	assertNotSame(c,c2);
        assertEquals(1, c.energy(), 0.01);
        assertEquals(1, c2.energy(), 0.01);
	

    }

    @Test
    public void testChoose() {
        Clorus p = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        //You can create new empties with new Empty();
        //Despite what the spec says, you cannot test for Cloruses nearby yet.
        //Sorry!  

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);
	
	// Alternative stay scenario: there is a plip nearby but no empty space
        surrounded.put(Direction.RIGHT, new Plip());

        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, actual);

	// Attack the Plips if there is at least one space and one plips
        surrounded.put(Direction.LEFT, new Empty());

        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.ATTACK,Direction.RIGHT);
        assertEquals(expected, actual);

	// test replicate
        surrounded.put(Direction.RIGHT, new Clorus(2));

        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.REPLICATE,Direction.LEFT);
        assertEquals(expected, actual);

        /*Clorus p2 = new Clorus(0.2);
        actual = p2.chooseAction(surrounded);
        expected = new Action(Action.ActionType.MOVE,Direction.RIGHT);

        assertEquals(expected, actual);*/
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestClorus.class));
    }
} 
