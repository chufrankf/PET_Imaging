import java.awt.geom.*;

public class StripDetector extends Detector{

	private Line2D.Float ptLine;
	private SimulationArithmetic calculator = new SimulationArithmetic();
	double ptAngle;
	private Point2D.Float point1;
	private Point2D.Float point2;
	
	public StripDetector(Rectangle2D.Float rect) {
		super(rect);
		ptLine = findSidePoints();
		ptAngle = 0;
	}
	public StripDetector(Rectangle2D.Float rect, double angle){
		super(rect);
		ptLine = findSidePoints();
		rotateShape(angle);
	}
	
	public Line2D.Float getLine(){
		return ptLine;
	}
	
	public Point2D.Double getRightPoint(){
		
		if(ptLine.x1 > ptLine.x2){
			return new Point2D.Double(ptLine.x1, ptLine.y1);
		}
		else{
			return new Point2D.Double(ptLine.x2, ptLine.y2);
		}
		
	}
	
	public Point2D.Double getLeftPoint(){
		
		if(ptLine.x1 < ptLine.x2){
			return new Point2D.Double(ptLine.x1, ptLine.y1);
		}
		else{
			return new Point2D.Double(ptLine.x2, ptLine.y2);
		}
	}
	
	public double getAngle(){
		return ptAngle;
	}
	
	public Line2D.Float findSidePoints(){
		
		Rectangle2D.Float rect = super.getOrigonalRectangle();
		
		if(rect.height > rect.width){
			
			point1 = new Point2D.Float((float)(rect.x + 0.5*rect.width), rect.y);
			point2 = new Point2D.Float((float)(rect.x + 0.5*rect.width), rect.y + rect.height);
		}
		else{
			point1 = new Point2D.Float((float)(rect.x), (float)(rect.y + 0.5*rect.height));
			point2 = new Point2D.Float(rect.x + rect.width, (float)(rect.y + 0.5*rect.height));
		}
		
		return new Line2D.Float(point1,point2);
	}
	
	public void rotateShape(double angle){
		super.rotateShape(angle);
		
		ptAngle += angle;
		Point2D.Float center = new Point2D.Float((float)super.getShape().getBounds2D().getCenterX(), (float)super.getShape().getBounds2D().getCenterY());
		
		Point2D.Float pt1 = new Point2D.Float(ptLine.x1, ptLine.y1);
		Point2D.Float pt2 = new Point2D.Float(ptLine.x2, ptLine.y2);
		
		point1 = calculator.rotatePoint(pt1, center, angle);
		point2 = calculator.rotatePoint(pt2, center, angle);
		
		ptLine = new Line2D.Float(point1,point2);
		
	}
	
	public Rectangle2D.Float copyStripShape(){
		
		Rectangle2D.Float rect = super.getOrigonalRectangle();
		rect.x += 10;
		rect.y += 10;
		
		return rect;
		
	}
	
	public void moveShape(int dx, int dy){
		super.moveShape(dx, dy);
		
		Line2D.Float newLine = new Line2D.Float(ptLine.x1 + dx, ptLine.y1 + dy, ptLine.x2 + dx, ptLine.y2 + dy);
		
		ptLine = newLine;
		getPoints();
	}
	
	public void getPoints(){
		point1 = new Point2D.Float(ptLine.x1,ptLine.y1);
		point2 = new Point2D.Float(ptLine.x2,ptLine.y2);
	}

}
