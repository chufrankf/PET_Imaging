import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.Math;

public class SimulationArithmetic {
	
	public static final long SPEEDOFLIGHT = 300000000;
	public static final int LENGTHM = 1000; //1000 pixels per m
	
	public SimulationArithmetic(){
	}
	
	public double angleOf3Points(Point2D.Float p1, Point2D.Float p2, Point2D.Float p3, int pointnum){
		
		double seg12,seg2,seg3,angle;
		seg12 = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
		seg2 = Math.sqrt(Math.pow(p1.x - p3.x, 2) + Math.pow(p1.y - p3.y, 2));
		seg3 = Math.sqrt(Math.pow(p2.x - p3.x, 2) + Math.pow(p2.y - p3.y, 2));
		
		switch(pointnum){
		case 1:	angle = Math.acos((Math.pow(seg12,2) + Math.pow(seg2,2) - Math.pow(seg3, 2)/(2*seg12*seg2))); break;
		case 2: angle = Math.acos((Math.pow(seg12,2) + Math.pow(seg3,2) - Math.pow(seg2, 2)/(2*seg12*seg3))); break;
		case 3: angle = Math.acos((Math.pow(seg3,2) + Math.pow(seg2,2) - Math.pow(seg12, 2)/(2*seg3*seg2))); break;
		default: angle = 0;
		}
		return angle;
	}
	
	public double angleOfChange(Point2D.Double center, Point2D.Double pStart, Point2D.Double pEnd) {
		
		double angle;
		
		angle = angleFromCenter(center,pEnd) - angleFromCenter(center,pStart);
		
		return angle;
	}
	
	public double angleFromCenter(Point2D.Double center, Point2D.Double point){
		double angle;
		
		double dx = point.x - center.x;
		double dy = point.y - center.y;
		
		angle = Math.atan(dy/dx);
		
		return angle;
	}
	
	public Point2D.Float rotatePoint(Point2D.Float point, Point2D.Float center, double angle){
		
		float dx = point.x - center.x;
		float dy = point.y - center.y;
		float newX = (float)(center.x - dx*Math.cos(angle) + dy*Math.sin(angle));
		float newY = (float)(center.y - dx*Math.sin(angle) - dy*Math.cos(angle));
		
		return new Point2D.Float(newX, newY);
	}
	
	public Point2D.Double centerOfLine(Line2D.Double line, Point2D.Double center){
		
		double slope = getSlope(line);
		double yInt = getYIntercept(line);
		
		return new Point2D.Double((slope*center.y + center.x - slope*yInt)/(slope*slope+1), (slope*slope*center.y + slope*center.x + yInt)/(slope*slope+1));
	}
	
	public double findAngleOfLine(Line2D.Double line){
		
		return Math.atan((line.y2-line.y1)/(line.x2-line.x1));
	}
	
	public double getSlope(Line2D.Double line){
		
		return (line.y2-line.y1)/(line.x2-line.x1);
	}
	
	public double getYIntercept(Line2D.Double line){
		
		double slope = getSlope(line);
		
		return line.y1 - slope*line.x1;
	}
	
	public Point2D.Double getIntersectionPoint(Line2D.Double line, Line2D.Double detectorLine){
		
		if(isVerticleLine(detectorLine)){
			
			double x = detectorLine.x1;
			double slope1 = getSlope(line);
			double yInt1 = getYIntercept(line);
			double y = slope1*x + yInt1;
			
			return new Point2D.Double(x,y);
		}
		else if(isVerticleLine(line)){
			
			double x = line.x1;
			double slope2 = getSlope(detectorLine);
			double yInt2 = getYIntercept(detectorLine);
			double y = slope2*x + yInt2;
			
			return new Point2D.Double(x,y);
		}
		else{
			double slope1 = getSlope(line);
			double slope2 = getSlope(detectorLine);
			double yInt1 = getYIntercept(line);
			double yInt2 = getYIntercept(detectorLine);
			
			double x = (yInt2 - yInt1)/(slope1 - slope2);
			double y = slope1*x + yInt1;
			
			return new Point2D.Double(x,y);
		}
		
	}
	
	public double findMagnitudeForSino(Line2D.Double line, Point2D.Double center){
		
		// Need to check for vertical straight line issues.
		
		if(isVerticleLine(line)){
			
			return line.x1-center.x;
		}
		else{
			double slope = getSlope(line);
			double yInt = getYIntercept(line);
			
			Point2D.Double lineCenter = centerOfLine(line, center);
			double dy = lineCenter.y - center.y;
			double dx = lineCenter.x - center.x;
			double magnitude;
			
			if((dx>=0 && dy>=0) || (dx<=0 && dy>0)){
				magnitude = -Math.abs(center.y - slope*center.x - yInt)/Math.sqrt(slope*slope + 1);
			}
			else{
				magnitude = Math.abs(center.y - slope*center.x - yInt)/Math.sqrt(slope*slope + 1);
			}
			
			return magnitude;
		}
	}
	
	public double findSlopeFromArray(double[] inArray){
		
		return Math.tan(Math.toRadians(inArray[0]));
	}
	
	public Point2D.Double findPointFromArray(double[] inArray, Point2D.Double center){
		
		
		return null;
	}
	
	public Point rotatePoint(Point pt, Point center, double angle)
	{
	    double cosAngle = Math.cos(angle);
	    double sinAngle = Math.sin(angle);

	    pt.x = center.x + (int) ((pt.x-center.x)*cosAngle-(pt.y-center.y)*sinAngle + .5);
	    pt.y = center.y + (int) ((pt.x-center.x)*sinAngle+(pt.y-center.y)*cosAngle + .5);
	    return pt;
	}
	
	public boolean isVerticleLine(Line2D.Double line){
		
		if(line.x1 == line.x2){
			return true;
		}
		else{
			return false;
		}
	}

	public double getMagnitude(Point2D.Double p1, Point2D.Double p2){
		
		return Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
	}
	
	public double getTime(Point2D.Double p1, Point2D.Double p2, double resolution, double pixelsPerM){
		
		// change 1 to 1000 when changing to mm per pixel
		if(resolution != 0){
			return (double)Math.round((getMagnitude(p1,p2)/(pixelsPerM*SPEEDOFLIGHT))*resolution)/resolution;
		}
		else{
			return getMagnitude(p1,p2)/(pixelsPerM*SPEEDOFLIGHT);
		}
			
	}
	
}
