import java.awt.Point;
import java.awt.Shape;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;


public class SaveAndLoader {
	public SaveAndLoader(){
		
	}
    public void savePoints(ArrayList<Point> printList){
    	try{
    		//creates folder and prints array list
    		PrintWriter fwrite = new PrintWriter("data.txt", "UTF-8");
    		for(int i=0; i<printList.size(); i++){
    			fwrite.println( printList.get(i).x + " " + printList.get(i).y);
    		}
    		fwrite.close();
    	}
    	catch (IOException e){
    		System.err.println("Problem writing to the file data.txt");
    	}
    }
    
    public void saveOutput(ArrayList<double[]> printList){
    	try{
    		//creates folder and prints array list
    		PrintWriter fwrite = new PrintWriter("outputData.txt", "UTF-8");
    		for(int i=0; i<printList.size(); i++){
    			fwrite.println( printList.get(i)[0]*180/Math.PI + " " + printList.get(i)[1] + " " + printList.get(i)[2] + " " + printList.get(i)[3] + " " +  printList.get(i)[4] + " " + printList.get(i)[5] + " " + printList.get(i)[6] + " " + printList.get(i)[7] );
    		}
    		fwrite.close();
    	}
    	catch (IOException e){
    		System.err.println("Problem writing to the file outputData.txt");
    	}
    }
    
    public void saveStripOutput(ArrayList<double[]> printList){
    	try{
    		//creates folder and prints array list
    		PrintWriter fwrite = new PrintWriter("outputStripData.txt", "UTF-8");
    		for(int i=0; i<printList.size(); i++){
    			fwrite.println( printList.get(i)[0] + " " + printList.get(i)[1] + " " + printList.get(i)[2] + " " + printList.get(i)[3] + " " +  printList.get(i)[4] + " " + printList.get(i)[5] + " " + printList.get(i)[6] + " " + printList.get(i)[7] + " " +  printList.get(i)[8] + " " + printList.get(i)[9] + " " + printList.get(i)[10] + " " + printList.get(i)[11]);
    		}
    		fwrite.close();
    	}
    	catch (IOException e){
    		System.err.println("Problem writing to the file stripOutputData.txt");
    	}
    }
    
    public void saveShapes(ArrayList<Shape> shapesList){
    	try{
    		FileOutputStream f_out = new FileOutputStream("shapedata.data");
    		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
    		obj_out.writeObject(shapesList);
    		obj_out.close();
    	}
    	catch (IOException e){
    		System.err.println("Problem writing to the file shapedata.data");
    	}
    }
    
    public void saveDetectors(ArrayList<Detector> detectorList){
    	try{
    		FileOutputStream f_out = new FileOutputStream("detectorData.data");
    		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
    		obj_out.writeObject(detectorList);
    		obj_out.close();
    	}
    	catch (IOException e){
    		System.err.println("Problem writing to the file detectorData.data");
    	}
    }
    
    public void saveDetectorShapes(ArrayList<Detector> detectorList){
    	ArrayList<Shape> shapeList = new ArrayList<Shape>();
    	
    	for(Detector d : detectorList){
    		shapeList.add(d.getShape());
    	}
    	
    	saveShapes(shapeList);
    	
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<Shape> loadShapes(String filename){
    	
    	ArrayList<Shape> shapeList = new ArrayList<Shape>();
    	
    	try{
    		FileInputStream f_in = new FileInputStream(filename);
    		ObjectInputStream obj_in = new ObjectInputStream (f_in);
			ArrayList<Shape> tmp_array = (ArrayList<Shape>)obj_in.readObject();
    		obj_in.close();
    		
    		shapeList = tmp_array;
    	}
    	catch (Exception exc){
    		System.err.println("Problem reading to the file:" + filename);
    	}
    	
    	return shapeList;

    }
    
    @SuppressWarnings("unchecked")
   	public ArrayList<Detector> loadDetectors(String filename){
       	
       	ArrayList<Detector> detectorList = new ArrayList<Detector>();
       	
       	try{
       		FileInputStream f_in = new FileInputStream(filename);
       		ObjectInputStream obj_in = new ObjectInputStream (f_in);
   			ArrayList<Detector> tmp_array = (ArrayList<Detector>)obj_in.readObject();
       		obj_in.close();
       		
       		detectorList = tmp_array;
       	}
       	catch (Exception exc){
       		System.err.println("Problem reading to the file:" + filename);
       	}
       	
       	return detectorList;

       }
    
    public ArrayList<Detector> loadDetectorShapes(String filename){
    	
    	ArrayList<Shape> shapeList = loadShapes(filename);
    	ArrayList<Detector> detectorList = new ArrayList<Detector>();
    	for(Shape s: shapeList){
    		detectorList.add(new Detector(s));
    	}
    	
    	return detectorList;
    }
}
