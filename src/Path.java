//package SnakeAndLadder;

import java.awt.*;
import java.awt.geom.*;


class Path {
	public final static int MAX_PATH = 15; //max no. of snakes or ladders, default no. of snakes and ladders is the height of the board - 3
	public final static int SNAKE = 0, LADDER = 1;
	public int high, low; //positions
	public Point p1, p2; //starting from p1, low to p2, high
	public int pathType;
	public int side;
	public boolean isStartFollow = true;
	private GeneralPath gp = new GeneralPath();
	private Point P[] = new Point[5];
	private Point divider1, divider2, d11, d21, d12, d22, d13, d23;
	private double length;
	private int segPoints = 0, pointNo = 0, segNo, segments;
	
	public Path(int pathType, int side) {
		this.pathType = pathType;
		this.side = side;
		for(int i = 0; i < 5; i++) P[i] = new Point();
	}
	
	
	private Point C(double t) {
		return new Point((int)(P[0].x*(1-t)*(1-t)*(1-t) + 3*P[1].x*t*(1-t)*(1-t) + 3*P[2].x*t*t*(1-t) + P[3].x*t*t*t),
				(int)(P[0].y*(1-t)*(1-t)*(1-t) + 3*P[1].y*t*(1-t)*(1-t) + 3*P[2].y*t*t*(1-t) + P[3].y*t*t*t));
	}
	
	private double length(Point p1, Point p2) {
		return Math.sqrt((p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y)); 
	}
	public void readyPath() {
		gp.reset();
		gp.moveTo(p2.x, p2.y);
		length = length(p1, p2);
		if(pathType == LADDER) {
			gp.lineTo(p1.x, p1.y);
		} else {		
			if(length < 250) { //one divider
				segments = 2;
				double ratio = 0.4 + (Math.random() * 0.5);
				divider2 = new Point((int)(p1.x + (p2.x - p1.x)*ratio), 
											(int)(p1.y + (p2.y - p1.y)*ratio));
				int normal2 = 40, normal1 = 40;
				double ratio2 = 0.66, ratio1 = 0.33;

				d23 = new Point(); d13 = new Point();
				d23.x = (int)(divider2.x + (p2.x - divider2.x)*ratio2 
								+ normal2 * side * (p2.y - p1.y)/length);
				d23.y = (int)(divider2.y + (p2.y - divider2.y)*ratio2 
								- normal2 * side * (p2.x - p1.x)/length);
				d13.x = (int)(divider2.x + (p2.x - divider2.x)*ratio1 
								+ normal1 * side * (p2.y - p1.y)/length);
				d13.y = (int)(divider2.y + (p2.y - divider2.y)*ratio1 
								- normal1 * side * (p2.x - p1.x)/length);
				
				gp.curveTo(d23.x, d23.y, d13.x, d13.y, divider2.x, divider2.y);

				d22 = new Point(); d12 = new Point();
				d22 = new Point(); d12 = new Point();				
				d22.x = (int)(divider2.x + (divider2.x - d13.x) * .2);//(int)(p1.x + (divider.x - p1.x)*ratio1 
							//+ (-1) * normal1 * side * (p2.y - p1.y)/length);
				d22.y = (int)(divider2.y + (divider2.y - d13.y) * .2);//(int)(p1.y + (divider.y - p1.y)*ratio1 
							//- (-1) * normal1 * side * (p2.x - p1.x)/length);
				d12.x = (int)(p1.x + (divider2.x - p1.x)*ratio1 
							+ (-1) * normal1 * side * (p2.y - p1.y)/length);
				d12.y = (int)(p1.y + (divider2.y - p1.y)*ratio1 
							- (-1) * normal1 * side * (p2.x - p1.x)/length);
				
				gp.curveTo(d22.x, d22.y, d12.x, d12.y, p1.x, p1.y);

			} else {
				segments = 3;
				double ratio = .7;//0.6 + (Math.random() * 0.71);
				divider2 = new Point((int)(p1.x + (p2.x - p1.x)*ratio), 
											(int)(p1.y + (p2.y - p1.y)*ratio));
				ratio = 0.28 + (Math.random() * 0.35);
				divider1 = new Point((int)(p1.x + (p2.x - p1.x)*ratio), 
											(int)(p1.y + (p2.y - p1.y)*ratio));
				int normal2 = 40, normal1 = 40;
				double ratio2 = 0.66, ratio1 = 0.33;

				d13 = new Point(); d23 = new Point();
				d23.x = (int)(divider2.x + (p2.x - divider2.x)*ratio2 
								+ normal2 * side * (p2.y - p1.y)/length);
				d23.y = (int)(divider2.y + (p2.y - divider2.y)*ratio2 
								- normal2 * side * (p2.x - p1.x)/length);
				d13.x = (int)(divider2.x + (p2.x - divider2.x)*ratio1 
						+ normal1 * side * (p2.y - p1.y)/length);
				d13.y = (int)(divider2.y + (p2.y - divider2.y)*ratio1 
						- normal1 * side * (p2.x - p1.x)/length);
				
				gp.curveTo(d23.x, d23.y, d13.x, d13.y, divider2.x, divider2.y);
			
				d22 = new Point(); d12 = new Point();
				d22.x = (int)(divider2.x + (divider2.x - d13.x) * .2);//(int)(divider1.x + (divider2.x - divider1.x)*ratio1 
							//+ (-1) * normal1 * side * (p2.y - p1.y)/length);
				d22.y = (int)(divider2.y + (divider2.y - d13.y) * .2);//(int)(divider1.y + (divider2.y - divider1.y)*ratio1 
							//- (-1) * normal1 * side * (p2.x - p1.x)/length);
				d12.x = (int)(divider1.x + (divider2.x - divider1.x)*ratio1 
							+ (-1) * normal1 * side * (p2.y - p1.y)/length);
				d12.y = (int)(divider1.y + (divider2.y - divider1.y)*ratio1 
							- (-1) * normal1 * side * (p2.x - p1.x)/length);
				
				gp.curveTo(d22.x, d22.y, d12.x, d12.y, divider1.x, divider1.y);
					
				d21 = new Point(); d11 = new Point();
				d21.x = (int)(divider1.x + (divider1.x - d12.x) * .2);//(int)(p1.x + (divider1.x - p1.x)*ratio1 
							//+ normal1 * side * (p2.y - p1.y)/length);
				d21.y = (int)(divider1.y + (divider1.y - d12.y) * .2);//(int)(p1.y + (divider1.y - p1.y)*ratio1 
							//- normal1 * side * (p2.x - p1.x)/length);
				d11.x = (int)(p1.x + (divider1.x - p1.x)*ratio1 
							+ normal1 * side * (p2.y - p1.y)/length);
				d11.y = (int)(p1.y + (divider1.y - p1.y)*ratio1 
							- normal1 * side * (p2.x - p1.x)/length);
			
				gp.curveTo(d21.x, d21.y, d11.x, d11.y, p1.x, p1.y);
				
			}
		}
	}
	
	public void drawPath(Graphics g) {
		Graphics2D g2d = ( Graphics2D ) g;
		if(pathType == LADDER) {
			g2d.setPaint( Color.darkGray );
			g2d.setStroke( new BasicStroke( 3.0f ) );
			g2d.draw(gp);
		} else {
			g2d.setPaint( Color.blue );
			g2d.setStroke( new BasicStroke( 3.0f ) );
			g2d.draw(gp);			
		}	
	}
	
	private double triArea(Point p1, Point p2, Point p3) {
		return (.5 * (p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y)));
	}
	
	public Point nextPoint() {
		if(pathType == LADDER) {
			if(isStartFollow) { isStartFollow = false;
								segPoints = (int)(length/5.0);
								pointNo = 1;
								P[0] = new Point();
				      			P[0].x = p1.x; P[0].y = p1.y; return P[0]; }
			if(pointNo > segPoints) { isStartFollow = true; return null; }
			P[0] = new Point();
			//P[0].x = p1.x; P[0].y = p1.y;
			P[0].x = (int)(p1.x + ((pointNo * 5.0)/length) * (double)(p2.x - p1.x));
			P[0].y = (int)(p1.y + ((pointNo * 5.0)/length) * (double)(p2.y - p1.y));
			pointNo++;
			System.out.println("p1 = (" + p1.x + ", " + p1.y + ")" + "    p2 = (" + p2.x + ", " + p2.y + ")" + "P[0] = (" + P[0].x + ", " + P[0].y + ")");
			System.out.println("triArea = " + triArea(p1, p2, P[0]));
			return P[0];
		} else { 
			if(isStartFollow) { isStartFollow = false;  
								segNo = 1; 
								pointNo = 1; P[0] = p2; P[1] = d23; P[2] = d13; P[3] = divider2;
								segPoints = (int)length(P[0], P[3])/5; 
								P[4] = new Point();
								P[4].x = p2.x; P[4].y = p2.y; return P[4]; }
			switch(segNo) {
				case 1 : P[4] = C((double)pointNo/segPoints);
						 if(pointNo == segPoints) {//ended seg = 1
							 segNo = 2;
    						 pointNo = 1; P[0] = divider2; P[1] = d22; P[2] = d12; P[3] = (segments == 2)? p1 : divider1;
							 segPoints = (int)length(P[0], P[3])/5; 
							 return P[4];
						 }
						 pointNo++;
						 return P[4];
						 
				case 2 : P[4] = C((double)pointNo/segPoints);
						 if(pointNo == segPoints) {//ended seg = 2
							 if(segments == 2) segNo = 4;
							 else {
								 segNo = 3;
								 pointNo = 1; P[0] = divider1; P[1] = d21; P[2] = d11; P[3] = p1;
								 segPoints = (int)length(P[0], P[3])/5; 
							 }
							 return P[4];
						 }
						 pointNo++;
						 return P[4];
						 
				case 3 : P[4] = C((double)pointNo/segPoints);
						 if(pointNo == segPoints) {//ended seg = 3
							 segNo = 4;
							 return P[4];
						 }
						 pointNo++;
						 return P[4];
						 
				case 4 : isStartFollow = true;
						 return null;
			}
			return null;
		}
		
	}
	
}
