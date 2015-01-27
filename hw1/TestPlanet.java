public class TestPlanet{
	public static void main(String[] args){
		// creates two planets and print out the pairwise force between them
		// optional test class

		Planet p1 = new Planet(1.0, 1.0, 3.0, 4.0, 5.0, "jupiter.gif");
		Planet p2 = new Planet(2.0, 1.0, 3.0, 4.0, 4e11, "jupiter.gif");
		Planet p3 = new Planet(4.0, 5.0, 3.0, 4.0, 5.0, "jupiter.gif");
		Planet p4 = new Planet(3.0, 2.0, 3.0, 4.0, 5.0, "jupiter.gif");

		Planet[] planets = {p2, p3, p4};

		p1=p3;

		System.out.println("1to2 normal:" + p1.calcPairwiseForce(p2));
		System.out.println("2to1 reverse:" + p2.calcPairwiseForce(p1));
		System.out.println("1to2 normal:" + p1.calcPairwiseForceX(p2));
		System.out.println("2to1 reverse:" + p2.calcPairwiseForceX(p1));
		System.out.println("1to2 normal:" + p1.calcPairwiseForceY(p2));
		System.out.println("2to1 reverse:" + p2.calcPairwiseForceY(p1));
	}
}