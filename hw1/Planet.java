public class Planet{
	double x;
	double y;
	double xVelocity;
	double yVelocity;
	double mass;
	String img;

	double xNetForce;
	double yNetForce;
	double xAccel;
	double yAccel;

	public Planet(double x0, double y0, double xV0, double yV0, double m0, String img0){
		x =  x0;
		y =  y0;
		xVelocity =  xV0;
		yVelocity =  yV0;
		mass =  m0;
		img =  img0;
	}

	// calculate distance of two planets
	public double calcDistance(Planet p0){
		double dx = this.x - p0.x;
		double dy = this.y - p0.y;
		return Math.sqrt(dx*dx+dy*dy);
	}

	/* syntax p0.calcPairwiseForce(p1)     *
	 * returns the pairwise force between  *
	 * two objects						   */
	public double calcPairwiseForce(Planet p0){
		double r = calcDistance(p0);
		double constG = 6.67e-11;
		double force = constG*mass*p0.mass/(r*r);
		return force;
	}

	public double calcPairwiseForceX(Planet p0){
		double force = calcPairwiseForce(p0);
		double dx = p0.x - this.x;
		double r  = calcDistance(p0);
		return force*dx/r;
	}

	public double calcPairwiseForceY(Planet p0){
		double force = calcPairwiseForce(p0);
		double dy = p0.y - this.y;
		double r  = calcDistance(p0);
		return force*dy/r;
	}

	public void setNetForce(Planet[] parray){
		xNetForce = 0;
		yNetForce = 0;
		for (Planet planeti:parray){
			if (planeti != this){
				xNetForce += calcPairwiseForceX(planeti);
				yNetForce += calcPairwiseForceY(planeti);			
			}
		}
		return;
	}

	public void draw(){
		StdDraw.picture(x, y, "images/"+img);
		return;
	}

	public void update(double dt){
		//set acceleration
		xAccel = xNetForce/mass;
		yAccel = yNetForce/mass;		
		//set new velocity with dt
		xVelocity +=dt*xAccel;
		yVelocity +=dt*yAccel;
		//set position using velocity and dt
		x += dt*xVelocity;
		y += dt*yVelocity;
		return;
	}



}





