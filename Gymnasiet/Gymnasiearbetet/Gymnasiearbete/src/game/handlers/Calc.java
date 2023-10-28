package game.handlers;

import java.awt.geom.Point2D;

public class Calc {
	
		/**
		 * sätter detta objekt i mitten av en enhetscirkel.
		 * kollar sedan vilken "absolutvinkel" detta objekt har mot en punkt
		 * som ligger vid delta x och delta y i en cirkel med radien radius
		 * funkar bra att kolla vilken riktning man färdas i.
		 * 
		 * @param x
		 * @param y
		 * @param radius
		 * @return
		 */
		public static double calcDir(double x, double y, double radius){
			double t = x/(radius == 0 ? 1 : radius);
			double cosV1 = Math.toDegrees(Math.acos(t));
			double cosV2 = Math.toDegrees(-Math.acos(t) + 2*Math.PI);
			
			boolean down = y > 0;
			
			return down ? cosV2 : cosV1;
		}
		
		/**
		 * räknar ut vinkeln mellan två objekt. vinkeln från center till other.
		 * 
		 * @param center
		 * @param other
		 * @return
		 */
		public static double calcDir(Point2D.Double center, Point2D.Double other){
			return calcDir(other.getX()-center.getX(), other.getY()-center.getY(), Calc.calcLength(center, other));
		}
		
		/**
		 * Hittar punkten i en cirkel
		 * 
		 * @param x
		 * @param y
		 * @param radius
		 * @param angle
		 * @return
		 */
		public static Point2D.Double getCirclePoint(double x, double y, double radius, double angle){
			return new Point2D.Double(getCircleX(x, radius, angle), getCircleY(y, radius, angle));
		}
		
		/**
		 * hitta xpos i en cirkel
		 * 
		 * @param x
		 * @param radius
		 * @param angle
		 * @return
		 */
		public static double getCircleX(double x, double radius, double angle){
			return x+Math.cos(Math.toRadians(angle))*radius;
		}
		
		/**
		 * hitta y i en cirkel. anpassad för att funka med origo i övre vänstra hörnet.
		 * 
		 * @param y
		 * @param radius
		 * @param angle
		 * @return
		 */
		public static double getCircleY(double y, double radius, double angle){
			return y-Math.sin(Math.toRadians(angle))*radius;
		}
		
		/**
		 * sätter vilken vinkel som helst till att bli mellan 0 och 360
		 * 
		 * @param ang
		 * @return
		 */
		public static double fixAngle(double ang){
			return ((ang % 360) + 360) % 360;
		}
		
		/**
		 * hittar kortase vilkeln för att gå från start till target
		 * 
		 * @param target
		 * @param start
		 * @return
		 */
		public static double getShortestAngleDistance(double target, double start){
			double t = fixAngle(target);
			double s = fixAngle(start);
			
			//olika sidor
			if(t < 180 && s > 180){
				s -= 360;
			}else if(t > 180 && s < 180){
				t -= 360;
			}
			
			double result1 = t-s;
			
			if(Math.abs(result1) > 180){
				result1 = (360 - Math.abs(result1)) * Math.abs(result1)/result1;
			}
			
			return result1;
		}
		
		/**
		 * 1 om det är närmast att gå motsols från ang1 till ang2
		 * <b>
		 * -1 om det är närmast att gå medsols från ang1 till ang2
		 * <b>
		 * 0 om de är lika 
		 * 
		 * @param ang1
		 * @param ang2
		 * @return -1, 0 eller 1
		 */
		public static int compareAngle(double ang1, double ang2){
			if(ang1 == ang2){
				return 0;
			}
			double a = getShortestAngleDistance(ang1, ang2);
			return (int)(Math.abs(a)/a); 
		}
		
		/**
		 * hitta hypotenusan i en rätvinklig triangel
		 * 
		 * @param dx
		 * @param dy
		 * @return
		 */
		public static double calcLength(double dx, double dy){
			return Math.sqrt(dx*dx + dy*dy);
		}
		
		/**
		 * hitta hypotenusan i en rätvinklig triangel
		 * 
		 * @param dx
		 * @param dy
		 * @return
		 */
		public static double calcLength(Point2D.Double p1, Point2D.Double p2){
			return Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY()-p2.getY(), 2));
		}
		
		/**
		 * hitta sträckan mellan 2 punkter
		 * 
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 * @return
		 */
		public static double getLength(double x1, double y1, double x2, double y2){
			return calcLength(x1-x2, y1-y2);
		}
	
	
}
