public class NBody{
	// main method
	public static void main(String[] args) {
		// store the 0th and 1st line as T and dt
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		// 2nd commandline arg as filename
		String filename = args[2];
		// new instance of In class using filename
		In in = new In(filename);
		// Read number of planets and universe radius
		int N = in.readInt();
		double size = in.readDouble();
		// Create planet array with getPlanet method
		Planet[] parray = new Planet[N];
		for (int i=0;i<N;i++){
			parray[i] = getPlanet(in);
		}

		// set Canvas
		StdDraw.setCanvasSize();
		StdDraw.setScale(-size,size);
		StdDraw.picture(0, 0, "images/starfield.jpg");
		// print planets
		for (Planet planeti: parray){
			planeti.draw();
		}

		for (double t=0;t<T;t+=dt){
			// set net force
			for (Planet planeti: parray){
				planeti.setNetForce(parray);
			}
			// update planet positions
			for (Planet planeti: parray){
				planeti.update(dt);
			}
			// draw background
			StdDraw.picture(0, 0, "images/starfield.jpg");
			// draw planets
			for (Planet planeti: parray){
				planeti.draw();
			}
			//update canvas
			StdDraw.show(5);
		}

		// print out result (for grader)
		StdOut.printf("%d\n", N);
		StdOut.printf("%.2e\n", size);
		for (int i = 0; i < N; i++) {
		    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		    parray[i].x, parray[i].y, parray[i].xVelocity, parray[i].yVelocity, parray[i].mass, parray[i].img);
		}

	}

	public static Planet getPlanet(In inObj){
		// read parameters from a line
		double x = inObj.readDouble();
		double y = inObj.readDouble();
		double xVelocity = inObj.readDouble();
		double yVelocity = inObj.readDouble();
		double mass = inObj.readDouble();
		String img = inObj.readString();
		// instantiate new planet
		Planet p = new Planet(x,y,xVelocity,yVelocity,mass,img);
		return p;
	}

}