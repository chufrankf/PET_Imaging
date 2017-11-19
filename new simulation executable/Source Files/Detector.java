import java.awt.*;
import java.awt.geom.*;

public class Detector
{
	private Polygon polygonDetector;
	private Rectangle2D.Float rectangle;
	private Shape iShape;
	private double shapeAngle;
	private SimulationArithmetic calculator = new SimulationArithmetic();
	private Line2D.Float[] lineList = new Line2D.Float[4];
	private Point2D.Float[] pointList = new Point2D.Float[4];
	
	public Detector(Rectangle2D.Float rect){
		polygonDetector = rectToPoly(rect);
		rectangle = rect;
		iShape = rect;
		shapeAngle = 0;
		
	}
	
	public Detector(Rectangle2D.Float rect, double angle){
		
		polygonDetector = rectToPoly(rect);
		rectangle = rect;
		iShape = rect;
		shapeAngle = 0;
		rotateShape(angle);
	}
	
	public Detector(Polygon poly){
		polygonDetector = poly;
		rectangle = new Rectangle2D.Float((float) poly.getBounds2D().getX(), (float) poly.getBounds2D().getY(), (float) poly.getBounds2D().getWidth(), (float) poly.getBounds2D().getHeight());
		iShape = poly;
		shapeAngle = 0;
	}
	
	public Detector(Shape shape){
		iShape = shape;
		rectangle = new Rectangle2D.Float((float) shape.getBounds2D().getX(), (float) shape.getBounds2D().getY(), (float) shape.getBounds2D().getWidth(), (float) shape.getBounds2D().getHeight());
		polygonDetector = rectToPoly(rectangle);
		shapeAngle = 0;
	}
	
	public Shape getShape(){
		return iShape;
	}
	
	public Polygon getPolygon(){
		return polygonDetector;
	}
	
	public Rectangle2D.Float getOrigonalRectangle(){
		return rectangle;
	}
	
	public double getAngle(){
		return shapeAngle;
	}
	
	public void rectToLines(Rectangle2D.Float rect){
		float x1 = rect.x;
		float x2 = (rect.x + rect.width);
		
		float y1 = rect.y;
		float y2 = (rect.y + rect.height);
		
		pointList[0] = new Point2D.Float(x1, y1);
		pointList[1] = new Point2D.Float(x2, y1);
		pointList[2] = new Point2D.Float(x2, y2);
		pointList[3] = new Point2D.Float(x1, y2);
		
		pointToLines();
		
	}
	
	public void pointToLines(){
		lineList[0] = new Line2D.Float(pointList[0],pointList[1]);
		lineList[1] = new Line2D.Float(pointList[1],pointList[2]);
		lineList[2] = new Line2D.Float(pointList[2],pointList[3]);
		lineList[3] = new Line2D.Float(pointList[3],pointList[0]);
	}
	
	public Polygon rectToPoly(Rectangle2D.Float rect){
		int[] xPoints = new int[4];
		int[] yPoints = new int[4];
		xPoints[0] = (int) rect.getX();
		xPoints[1] = (int) rect.getX();
		xPoints[2] = (int) (rect.getX() + rect.getWidth());
		xPoints[3] = (int) (rect.getX() + rect.getWidth());
		
		yPoints[0] = (int) rect.getY();
		yPoints[1] = (int) rect.getY();
		yPoints[2] = (int) (rect.getY() + rect.getHeight());
		yPoints[3] = (int) (rect.getY() + rect.getHeight());
		
		rectToLines(rect);
		
		return new Polygon(xPoints,yPoints,4);
	}
	
	public void enlargenShape(double scale){
		//initialize transformer
		AffineTransform transformer = new AffineTransform();
		
		transformer.scale(scale, scale);
		Shape newShape = transformer.createTransformedShape(iShape);
		double dx = iShape.getBounds2D().getCenterX() - newShape.getBounds2D().getCenterX();
		double dy = iShape.getBounds2D().getCenterY() - newShape.getBounds2D().getCenterY();
		
		transformer.setToTranslation(dx,dy);
		newShape = transformer.createTransformedShape(newShape);
		
		iShape = newShape;
	}
	
	public void rotateShape(double angle){
 	    
		shapeAngle += angle;
		
 		Point2D.Double center = new Point2D.Double(iShape.getBounds2D().getCenterX(), iShape.getBounds2D().getCenterY());
 		
 		AffineTransform transformer = new AffineTransform();
 		transformer.rotate(angle, center.x, center.y);
 		Shape newShape = transformer.createTransformedShape(iShape);
 		
 		iShape = newShape;
	}
	
	public void rotateLines(double angle){
		shapeAngle += angle;
		Point2D.Float center = new Point2D.Float((float) iShape.getBounds2D().getCenterX(),(float) iShape.getBounds2D().getCenterY());
		
		for(int i = 0; i<pointList.length; i++){
			pointList[i] = calculator.rotatePoint(pointList[i], center, angle);
		}
		
		pointToLines();
		
	}
	
	public void moveShape(int dx, int dy){
		
		AffineTransform transformer = new AffineTransform();
	    
	    transformer.setToTranslation(dx, dy);
	    Shape newShape = transformer.createTransformedShape(iShape);
	    
		iShape = newShape;
	}
	
	public void enlargenPolygon(int scale){


	}
	
	public void rotatePolygon(double angle){
		Point center = new Point(polygonDetector.getBounds().x, polygonDetector.getBounds().y);
		
		int[] xPoints = new int[polygonDetector.npoints];
		int[] yPoints = new int[polygonDetector.npoints];;
		
		for(int i=0; i<polygonDetector.npoints; i++){
			xPoints[i] = calculator.rotatePoint(new Point(polygonDetector.xpoints[i], polygonDetector.ypoints[i]), center , angle).x;
			yPoints[i] = calculator.rotatePoint(new Point(polygonDetector.xpoints[i], polygonDetector.ypoints[i]), center , angle).y;
		}
		
		polygonDetector = new Polygon(xPoints, yPoints, polygonDetector.npoints);
	}
	
	public void movePolygon(int dx, int dy){
		
		int[] xPoints = new int[polygonDetector.npoints];
		int[] yPoints = new int[polygonDetector.npoints];;
		
		for(int i=0; i<polygonDetector.npoints; i++){
			xPoints[i] += dx;
			yPoints[i] += dy;
		}
		
		polygonDetector = new Polygon(xPoints, yPoints, polygonDetector.npoints);
		
	}
	
	public Shape copyShape(){
		AffineTransform transformer = new AffineTransform();
		Shape copiedShape = this.getShape();
		
		transformer.setToTranslation(10, 10);
		copiedShape = transformer.createTransformedShape(copiedShape);
		
		return copiedShape;
	}
	
	public boolean doesIntercept(Line2D.Double line){
		
		for(Line2D.Float l : lineList){
			if(l.intersectsLine(line))
				return true;
		}
		
		return false;
	}

}
