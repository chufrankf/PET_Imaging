import java.util.ArrayList;
import java.awt.Point;
import java.awt.geom.*;
import java.lang.Math;

public class Source
{
	int compX;
	int compY;
	double[] randAngles;
	public static int DURATION = 5;
	public static int MAGNETUDE = 800;
	ArrayList<Line2D.Float> lineList = new ArrayList<Line2D.Float>();
	
	public Source(Point point){
		compX = point.x;
		compY = point.y;
		randAngles = generateRandomAngleList();
		lineList = generateLineList();
		
	}
	
	private double[] generateRandomAngleList(){
		double[] angleList = new double[DURATION];
		for(int i=0; i<angleList.length; i++){
			angleList[i] = Math.random()*2*Math.PI;
		}
		
		return angleList;
	}
	public void printRandomAngleList(){
		for(int i=0; i<randAngles.length; i++){
			System.out.println(randAngles[i]);
		}
	}
	public ArrayList<Line2D.Float> getLineList(){
		return lineList;
	}
	
	public double[] getRandAngleArray(){
		
		return randAngles;
	}
	public Point2D.Float getPoint(){
		return new Point2D.Float(compX,compY);
	}
	private ArrayList<Line2D.Float> generateLineList(){
		
		ArrayList<Line2D.Float> outLineList = new ArrayList<Line2D.Float>();
		
		for(int i=0; i<randAngles.length; i++){
			outLineList.add(generateLine(randAngles[i]));
		}
		
		return outLineList;
	}
	private Line2D.Float generateLine(double angle){
		
		
		//positive change
		float pNewX = (float) (compX + MAGNETUDE*Math.cos(angle));
		float pNewY = (float) (compY + MAGNETUDE*Math.sin(angle));
		//find positive intercept
		Point2D.Float top = new Point2D.Float(pNewX, pNewY);
		//negative change
		float nNewX = (float) (compX - MAGNETUDE*Math.cos(angle));
		float nNewY = (float) (compY - MAGNETUDE*Math.sin(angle));
		//find negative intercept
		Point2D.Float bottom = new Point2D.Float(nNewX, nNewY);
		
		return new Line2D.Float(bottom, top);
	}
	
}
