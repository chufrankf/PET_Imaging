import java.awt.*;

import javax.swing.*;


public class SimulatorMain
{
	CoordinateArea coordinateArea = new CoordinateArea(this);
	
  private void buildUI(Container container) 
  {
	//sets the layout of the content plane to set alignments.
    container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
    
    //generates the coordinate area and adds to frame
    container.add(coordinateArea);
    
  }

  public static void main(String[] args) 
  {
	//creates new JFrame with the Simulator Name
    JFrame frame = new JFrame("PET Simulation");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    
    //sets the file as controller and builds UI
    SimulatorMain controller = new SimulatorMain();
    controller.buildUI(frame.getContentPane());
    
    //shows the jFrame
    frame.pack();
    frame.getContentPane().setBackground(new Color(240,240,240));
    frame.setVisible(true);
  }
  
  
}