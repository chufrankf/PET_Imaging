import java.util.ArrayList;
import java.util.Hashtable;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.event.*;

import java.lang.Math;

public class CoordinateArea extends JComponent implements MouseInputListener, Runnable
  {
    /**
	 * 
	 */
	
	private static final long serialVersionUID = -4905218031629317584L;
	
	Thread thread;
	
	//Initialize controller
    SimulatorMain controller;
    SaveAndLoader fileManager = new SaveAndLoader();
    SimulationArithmetic calculator = new SimulationArithmetic();
    //RunDetector runnable = new RunDetector((Graphics2D)this.getGraphics());

    //Draw Dimensions
    private static final int PREFWIDTH = 1300;
    private static final int PREFHEIGHT = 725;
    private static final int RIGHTBORDER = 50;
    private static final int LEFTBORDER = 50;
    //Control Panel Dimensions
    public static final int TOPPANELHEIGHT = 100;
    public static final int BOTTOMPANELHEIGHT = 75;
    
    // pixels/cm
    private int MMPERTENPIXEL = 10;
    private double RESOLUTION = 0; // perfect default
    private int SPEED = 0; // speed of simulation 0-highest
    private double ENERGY = 10.0; // eV
    private double RATIO = 1000.0;  // eV/cm
    private int RECTSIZE = 20;
    private int ARRAYSIZE = 100;
    Dimension preferredSize = new Dimension(PREFWIDTH, PREFHEIGHT);
    
    //Generates Slider Buttons
    JSlider rectSizeSlider = new JSlider(JSlider.HORIZONTAL,5,60,20);
    JSlider arraySizeSlider = new JSlider(JSlider.HORIZONTAL,20,120,100);
    JSlider speedSlider = new JSlider(JSlider.VERTICAL,0,10,0);
    JSlider timeResSlider = new JSlider(JSlider.HORIZONTAL,1,62,62);
    JSlider measureScaleSlider = new JSlider(JSlider.HORIZONTAL,1,40,10);
    JSlider energySlider = new JSlider(JSlider.HORIZONTAL,1,60,1);
    JSlider pMSlider = new JSlider(JSlider.HORIZONTAL,1,30,1);

    String label = "Salutations! Welcome to the PET Simulation";
    
    //instantiate Buttons
    JButton stopButton = new JButton(new ImageIcon(((new ImageIcon("Images/stop-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton startButton = new JButton(new ImageIcon(((new ImageIcon("Images/play-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton resetsourceButton = new JButton(new ImageIcon(((new ImageIcon("Images/sreset-icon.png").getImage().getScaledInstance(84, 68,java.awt.Image.SCALE_SMOOTH)))));
    JButton resetdetectorsButton = new JButton(new ImageIcon(((new ImageIcon("Images/dreset-icon.png").getImage().getScaledInstance(84, 68,java.awt.Image.SCALE_SMOOTH)))));
    JButton resetallButton = new JButton(new ImageIcon(((new ImageIcon("Images/allreset-icon.png").getImage().getScaledInstance(84, 68,java.awt.Image.SCALE_SMOOTH)))));
    JButton saveButton = new JButton(new ImageIcon(((new ImageIcon("Images/save-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton loadButton = new JButton(new ImageIcon(((new ImageIcon("Images/load-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton sourcesButton = new JButton(new ImageIcon(((new ImageIcon("Images/source-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton detectorsButton = new JButton(new ImageIcon(((new ImageIcon("Images/detectors-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton stripDetectorsButton = new JButton(new ImageIcon(((new ImageIcon("Images/strip-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton arrowButton = new JButton(new ImageIcon(((new ImageIcon("Images/mouse-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton moveButton = new JButton(new ImageIcon(((new ImageIcon("Images/move-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton angleButton = new JButton(new ImageIcon(((new ImageIcon("Images/angle-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton copyButton = new JButton(new ImageIcon(((new ImageIcon("Images/copy-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton deleteButton = new JButton(new ImageIcon(((new ImageIcon("Images/delete-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton intersectionButton = new JButton(new ImageIcon(((new ImageIcon("Images/intersection-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton largeRectButton = new JButton(new ImageIcon(((new ImageIcon("Images/largerect-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton ringButton = new JButton(new ImageIcon(((new ImageIcon("Images/ring-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton horizontalStripButton = new JButton(new ImageIcon(((new ImageIcon("Images/horizontalstrip-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));
    JButton verticalStripButton = new JButton(new ImageIcon(((new ImageIcon("Images/verticalstrip-icon.png").getImage().getScaledInstance(32, 32,java.awt.Image.SCALE_SMOOTH)))));

    //0-dont_draw 1-draw_source 2-draw_detector 3-move_detector 
    //4-angle_detector 5-copy_detector 6-delete_detector 7-draw_strip_detector
    private int drawvalue = 0;
    
    //0-no_detector_selected 1-detector_selected
    private int selectvalue = 0;
    
    //Array lists for storage and painting;
    ArrayList<Point> pointList = new ArrayList<Point>(); //includes all points for print
   
    ArrayList<Source> sourceList = new ArrayList<Source>(); //includes all sources which are points
    ArrayList<Detector> detectorList = new ArrayList<Detector>(); // includes all detectors for computation
    ArrayList<StripDetector> stripDetectorList = new ArrayList<StripDetector>();
    
    ArrayList<double[]> outputList = new ArrayList<double[]>(); // includes the output data(angle,distance, t1(pos), t2(neg)
    ArrayList<double[]> stripOutputList = new ArrayList<double[]>(); // includes the output data (x1, y1, t1 ... 4 points)
    //p Represents Right side which is (++x, +-y)
    
    
    ArrayList<Line2D.Double> testLineDraw = new ArrayList<Line2D.Double>(); // draws the test lines to show intersections
    ArrayList<Shape> petShapeList = new ArrayList<Shape>();
    ArrayList<Shape> petLineList = new ArrayList<Shape>();
    
    //point values
    private Point point = null;
	private Point startDrag, endDrag;
	
	//for use of movement, scale, and angle
	private int preX, preY, index;

    public CoordinateArea(SimulatorMain controller) 
    {
    	this.controller = controller;

    	//add Sliders
    	rectSizeSlider.setBounds(230, PREFHEIGHT-BOTTOMPANELHEIGHT+30,300,40);
    	rectSizeSlider.setMajorTickSpacing(5);
    	rectSizeSlider.setMinorTickSpacing(1);
    	rectSizeSlider.setPaintTicks(true);
    	rectSizeSlider.setPaintLabels(true);
    	rectSizeSlider.setBackground(new Color(153,153,255));
    	
    	arraySizeSlider.setBounds(540, PREFHEIGHT-BOTTOMPANELHEIGHT+30,300,40);
    	arraySizeSlider.setMajorTickSpacing(10);
    	arraySizeSlider.setPaintTicks(true);
    	arraySizeSlider.setPaintLabels(true);
    	arraySizeSlider.setBackground(new Color(153,153,255));
    	
    	speedSlider.setBounds(PREFWIDTH-45, PREFHEIGHT/2-220,40,150);
    	speedSlider.setMajorTickSpacing(1);
    	speedSlider.setPaintTicks(true);
    	speedSlider.setPaintLabels(true);
    	speedSlider.setBackground(new Color(153,153,255));
    	
    	timeResSlider.setBounds(10,50,400,40);
    	timeResSlider.setMajorTickSpacing(10);
    	timeResSlider.setMinorTickSpacing(1);
    	timeResSlider.setPaintTicks(true);
    	timeResSlider.setPaintLabels(true);
    	timeResSlider.setBackground(new Color(153,153,255));
    	
    	Hashtable<Integer, JLabel> timeTable = new Hashtable<Integer, JLabel>();
    	timeTable.put(1, new JLabel("1uS"));
    	timeTable.put(11, new JLabel("100nS"));
    	timeTable.put(21, new JLabel("10nS"));
    	timeTable.put(31, new JLabel("1nS"));
    	timeTable.put(41, new JLabel("100pS"));
    	timeTable.put(51, new JLabel("10pS"));
    	timeTable.put(61, new JLabel("1pS"));
    	
    	timeResSlider.setLabelTable(timeTable);
    	
    	measureScaleSlider.setBounds(420,50,400,40);
    	measureScaleSlider.setMinorTickSpacing(1);
    	measureScaleSlider.setPaintTicks(true);
    	measureScaleSlider.setPaintLabels(true);
    	measureScaleSlider.setBackground(new Color(153,153,255));
    	
    	Hashtable<Integer, JLabel> scaleTable = new Hashtable<Integer, JLabel>();
    	scaleTable.put(1, new JLabel(".1mm"));
    	scaleTable.put(10, new JLabel("1mm"));
    	scaleTable.put(20, new JLabel("1cm"));
    	scaleTable.put(30, new JLabel("1dm"));
    	scaleTable.put(40, new JLabel("1m"));
    	
    	measureScaleSlider.setLabelTable(scaleTable);
    	
    	energySlider.setBounds(900,5,400,40);
    	energySlider.setMinorTickSpacing(1);
    	energySlider.setPaintTicks(true);
    	energySlider.setPaintLabels(true);
    	energySlider.setBackground(new Color(153,153,255));
    	
    	Hashtable<Integer, JLabel> energyTable = new Hashtable<Integer, JLabel>();
    	energyTable.put(1, new JLabel("10eV"));
    	energyTable.put(10, new JLabel("100eV"));
    	energyTable.put(20, new JLabel("1keV"));
    	energyTable.put(30, new JLabel("10keV"));
    	energyTable.put(40, new JLabel("100keV"));
    	energyTable.put(50, new JLabel("1MeV"));
    	energyTable.put(60, new JLabel("10MeV"));
    	
    	energySlider.setLabelTable(energyTable);
    	
    	pMSlider.setBounds(900,50,400,40);
    	pMSlider.setMinorTickSpacing(1);
    	pMSlider.setPaintTicks(true);
    	pMSlider.setPaintLabels(true);
    	pMSlider.setBackground(new Color(153,153,255));
    	
    	Hashtable<Integer, JLabel> pMTable = new Hashtable<Integer, JLabel>();
    	pMTable.put(1, new JLabel("1keV/cm"));
    	pMTable.put(10, new JLabel("10keV/cm"));
    	pMTable.put(20, new JLabel("100keV/cm"));
    	pMTable.put(30, new JLabel("1MeV/com"));
    	
    	pMSlider.setLabelTable(pMTable);
    	
    	add(rectSizeSlider);
    	add(arraySizeSlider);
    	add(speedSlider);
    	add(timeResSlider);
    	add(measureScaleSlider);
    	add(energySlider);
    	add(pMSlider);
    	
    	rectSizeSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				stop();
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            RECTSIZE = (int)source.getValue();
		        }
		        repaint();
				
			}});
    	
    	arraySizeSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				stop();
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            ARRAYSIZE = (int)source.getValue();
		        }
		        repaint();
				
			}});
    	
    	speedSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				stop();
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            SPEED = (int)source.getValue()*2;
		        }
				
			}});
    	
    	timeResSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				stop();
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            int value = source.getValue()-1;
		            int exp = (int)(value)/10;
		            if(value == 61){
		            	RESOLUTION = 0;
		            }
		            else if(value%10 == 0){
		            	RESOLUTION = (long)(1000000*Math.pow(10,exp));
		            }
		            else{
		            	RESOLUTION = (long)(1000000*Math.pow(10,exp)*(value%10));
		            }
		            repaint();
		        }
				
			}});
    	
    	measureScaleSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				stop();
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            int value = source.getValue();
		            int exp = (int)(value)/10;
		            
		            if(value%10 == 0){
		            	MMPERTENPIXEL = (int)(Math.pow(10,exp));
		            }
		            else{
		            	MMPERTENPIXEL = (int)(Math.pow(10,exp)*(value%10));
		            }
		            repaint();
		        }
				
			}});

    	energySlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				stop();
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            int value = source.getValue();
		            int exp = (int)(value)/10;
		            
		            if(value%10 == 0){
		            	ENERGY = (int)(Math.pow(10,exp)*10);
		            }
		            else{
		            	ENERGY = (int)(Math.pow(10,exp)*(value%10)*10);
		            }
		            repaint();
		        }
				
			}});

    	pMSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				stop();
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            int value = source.getValue();
		            int exp = (int)(value)/10;
		            
		            if(value%10 == 0){
		            	RATIO = (int)(Math.pow(10,exp)*1000);
		            }
		            else{
		            	RATIO = (int)(Math.pow(10,exp)*(value%10)*1000);
		            }
		            repaint();
		        }
				
			}});

    	
    	//CHANGE
    	//add Buttons
    	// side box boundaries: PREFWIDTH-50, PREFHEIGHT/2-65, 50, 130
    	startButton.setBounds(PREFWIDTH-45, PREFHEIGHT/2-60, 40, 40);
    	stopButton.setBounds(PREFWIDTH-45, PREFHEIGHT/2-20, 40, 40);
    	intersectionButton.setBounds(PREFWIDTH-45,PREFHEIGHT/2+20,40,40);
    	
    	resetsourceButton.setBounds(5,PREFHEIGHT-BOTTOMPANELHEIGHT+20,40,40);
    	resetdetectorsButton.setBounds(45,PREFHEIGHT-BOTTOMPANELHEIGHT+20,40,40);
    	resetallButton.setBounds(85,PREFHEIGHT-BOTTOMPANELHEIGHT+20,40,40);
    	
    	saveButton.setBounds(5,TOPPANELHEIGHT+5, 40, 40);
    	loadButton.setBounds(5,PREFHEIGHT-BOTTOMPANELHEIGHT-45, 40, 40);
    	
    	arrowButton.setBounds(5,TOPPANELHEIGHT+60, 40, 40);
    	sourcesButton.setBounds(5,TOPPANELHEIGHT+100, 40, 40);
    	detectorsButton.setBounds(5,TOPPANELHEIGHT+140, 40, 40);
    	stripDetectorsButton.setBounds(5,TOPPANELHEIGHT+180, 40, 40);
    	
    	moveButton.setBounds(5,TOPPANELHEIGHT+230, 40, 40);
    	angleButton.setBounds(5,TOPPANELHEIGHT+270, 40, 40);
    	
    	ringButton.setBounds(140,PREFHEIGHT-BOTTOMPANELHEIGHT+20,40,40);
    	largeRectButton.setBounds(180,PREFHEIGHT-BOTTOMPANELHEIGHT+20,40,40);
    	horizontalStripButton.setBounds(850,PREFHEIGHT-BOTTOMPANELHEIGHT+20,40,40);
    	verticalStripButton.setBounds(890,PREFHEIGHT-BOTTOMPANELHEIGHT+20,40,40);
    	
    	copyButton.setBounds(5,TOPPANELHEIGHT+320, 40, 40);
    	deleteButton.setBounds(5,TOPPANELHEIGHT+360, 40, 40);
    	
    	add(resetallButton);
    	add(arrowButton);
    	add(detectorsButton);
    	add(sourcesButton);
    	add(startButton);
    	add(stopButton);
    	add(resetsourceButton);
    	add(saveButton);
    	add(resetdetectorsButton);
    	add(moveButton);
    	add(angleButton);
    	add(copyButton);
    	add(deleteButton);
    	add(loadButton);
    	add(intersectionButton);
    	add(stripDetectorsButton);
    	add(largeRectButton);
    	add(ringButton);
    	add(horizontalStripButton);
    	add(verticalStripButton);
    	
    	//adds actions for each button

    	resetsourceButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			label = "Reset Source";
    			pointList.clear();
    			sourceList.clear();
    			repaint();
    		}
    	});
    	
    	startButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			outputList.clear();
    			stripOutputList.clear();
    			label = "Start";
    			drawvalue = 0;
    			start();
    		}
    	});
    	
    	stopButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			label = "Stop";
    			stop();
    			repaint();
    		}
    	});
    	
    	resetdetectorsButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Reset Detectors";
    			detectorList.clear();
    			stripDetectorList.clear();
    			repaint();
    		}
    	});
    	
    	resetallButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Reset All";
    			detectorList.clear();
    			pointList.clear();
    			sourceList.clear();
    			stripDetectorList.clear();
    			repaint();
    		}
    	});
    	
    	saveButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Save";
    			fileManager.savePoints(pointList);
    			fileManager.saveDetectorShapes(detectorList);
    			repaint();
    		}
    	});
    	
    	sourcesButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Draw Sources By Clicking on Screen";
    			drawvalue = 1;
    			repaint();
    		}
    	});
    	
    	detectorsButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Draw Detectors, Click and Drag Mouse on screen";
    			drawvalue = 2;
    			repaint();
    		}
    	});
    	
    	stripDetectorsButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Draw Strip Detectors, Click and Drag Mouse on screen";
    			drawvalue = 7;
    			repaint();
    		}
    	});
    	
    	arrowButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "No action selected";
    			drawvalue = 0;
    			repaint();
    		}
    	});
    	
    	moveButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Drag a Detector to move it";
    			drawvalue = 3;
    			repaint();
    		}
    	});
    	
    	angleButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Drag a Detector to angle it";
    			drawvalue = 4;
    			repaint();
    		}
    	});
    	
    	copyButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Click on a detector to copy it";
    			drawvalue = 5;
    			repaint();
    		}
    	});
    	
    	deleteButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Click a detector to delete it";
    			drawvalue = 6;
    			repaint();
    		}
    	});
    	
    	loadButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			label = "Loading sources from data.txt and Detectors from shapeData.data";
    			drawvalue = 0;
    			detectorList = fileManager.loadDetectorShapes("shapeData.data");
    			repaint();
    		}
    	});
    	
    	intersectionButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			drawvalue = 0;
    			testDrawLines(testLineDraw);
    		}
    	});

    	
    	largeRectButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			drawvalue = 0;
    			AddDetectorArray();
    			repaint();
    		}
    	});
    	
    	ringButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			drawvalue = 0;
    			AddCircularDetectorArray();
    			repaint();
    		}
    	});
    	
    	horizontalStripButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			drawvalue = 0;
    			AddHorizontalStripDetectorArray();
    			repaint();
    		}
    	});
    	
    	verticalStripButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			stop();
    			drawvalue = 0;
    			AddVerticalStripDetectorArray();
    			repaint();
    		}
    	});
    	
    	// adds mouse listener functions
    	addMouseListener(this);
    	addMouseMotionListener(this);
    	addMouseWheelListener(new MouseWheelListener(){
    		public void mouseWheelMoved(MouseWheelEvent e){
    			int x = e.getX();
    		    int y = e.getY();
    		    
    		    if(isDetector(x,y)){
    		    	index = findDetectorIndex(x,y);

    		    	if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL){
    		    		if(detectorList.get(index).getShape().contains(x,y)){
    		    			double scale = 1;
    		    			scale += e.getWheelRotation() * 0.01f;
    		    			detectorList.get(index).enlargenShape(scale);
    		    			repaint();
    		    		}
    		    	}
    		    }
    		    
    		}
    	});
    	
    }
    
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
      }
    
    public void stop() {
        if (thread != null)
          thread.interrupt();
        thread = null;
      }
    
	public void run() {
		
		Thread me = Thread.currentThread();
		
		if(!pointList.isEmpty() && !(stripDetectorList.isEmpty() && detectorList.isEmpty())){
			generateSourceList();
			testLineDraw.clear();
			
			for(int i=0 ; i<sourceList.get(0).getLineList().size() ; i++){
				if(thread != me){ break;}
				for(Source s: sourceList){
					paintSlowLine(s.getLineList().get(i),s.getPoint());
					if(thread != me){ break;}
				}
				
			}
		}
	   	fileManager.saveOutput(outputList);
		fileManager.saveStripOutput(stripOutputList);
		sourceList.clear();
		
		repaint();
	   	
    }

    private void generateSourceList(){
    	//generates the list of sources from the point list. // used in Play()
    	for(Point p: pointList){
    		sourceList.add(new Source(p));
    	}
    }
    
    private void testDrawLines(ArrayList<Line2D.Double> drawList){
    	
    	//SimulationArithmetic calculator = new SimulationArithmetic();
    	Graphics2D g2 = (Graphics2D) this.getGraphics();
    	for(Line2D.Double l : drawList){
    		g2.setPaint(Color.BLACK);
    		g2.draw(l);
    	}
    	
    }

    private void paintSlowLine(Line2D.Float line, Point2D.Float center){
    	
    	Graphics2D g2 = (Graphics2D)this.getGraphics();
		paintBordersRunning(g2);
		drawPoints(g2);
		drawPETShapes(g2);
    	
    	float px = center.x;
    	float py = center.y;
    	float nx = center.x;
    	float ny = center.y;
    	Point2D.Double start = new Point2D.Double(px, py);
    	// Need to check for vertical straight line issues.
    	double angle;
    	
    	if(line.x2 != line.x1){
    		float slope = (line.y2-line.y1)/(line.x2-line.x1);
    		angle = Math.atan(slope);
    	}
    	else{
    		angle = (Math.PI/2 - .0000001);
    	}
    	
    	final int MAGNITUDE = (int) Math.ceil(ENERGY*100/(RATIO*MMPERTENPIXEL));
    	int index1 = -1;
    	int index2 = -1;
    	int sindex1 = -1;
    	int sindex2 = -1;
    	
    	//hit variables for detectors
    	boolean pHit = false;
    	boolean nHit = false;
    	//hit variables for strip detectors
    	boolean psHit = false;
    	boolean nsHit = false;
    	Point2D.Float pHitPoint = new Point2D.Float();
    	Point2D.Float nHitPoint = new Point2D.Float();
    	Point2D.Float psHitPoint = new Point2D.Float();
    	Point2D.Float nsHitPoint = new Point2D.Float();
    	
    	//long values for keeping timing
    	long pHitTime = 0;
    	long nHitTime = 0;

    	while((!nsHit || !psHit ) && (!pHit || !nHit) && ( px>0 && py>0 && px<PREFWIDTH && py<PREFHEIGHT) || ( nx>0 && ny>0 && nx<PREFWIDTH && ny<PREFHEIGHT)){
    		px += MAGNITUDE*Math.cos(angle);
    		py += MAGNITUDE*Math.sin(angle);
    		nx -= MAGNITUDE*Math.cos(angle);
    		ny -= MAGNITUDE*Math.sin(angle);

    		if(!pHit && isDetector(px,py)){
    			pHit = true;
    			pHitPoint = new Point2D.Float(px,py);
    			index1 = findDetectorIndex(pHitPoint.x, pHitPoint.y);
    			pHitTime = System.nanoTime();
    		}
    		if(!psHit && isStripDetector(px,py)){
    			psHit = true;
    			psHitPoint = new Point2D.Float(px,py);
    			sindex1 = findStripDetectorIndex(psHitPoint.x, psHitPoint.y);
    		}
    		if(!nHit && isDetector(nx,ny)){
    			nHit = true;
    			nHitPoint = new Point2D.Float(nx,ny);
    			index2 = findDetectorIndex(nHitPoint.x, nHitPoint.y);
    			nHitTime = System.nanoTime();
    		}
    		if(!nsHit && isStripDetector(nx,ny)){
    			nsHit = true;
    			nsHitPoint = new Point2D.Float(nx,ny);
    			sindex2 = findStripDetectorIndex(nsHitPoint.x, nsHitPoint.y);
    		}
    	}

    	if(pHit && nHit){
    		addToList(index1, index2, pHitTime, nHitTime, start);
    		drawShapesRun(g2,index1, index2);
    	}
    	if(psHit && nsHit){
       		paintStripLines(psHitPoint, nsHitPoint, sindex1, sindex2, start);
    		drawShapesRun(g2,sindex1, sindex2);
    	}
		
		try {
			Thread.sleep(SPEED);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
    	
    }
    
    private void paintStripLines(Point2D.Float pHitPoint, Point2D.Float nHitPoint, int index1, int index2, Point2D.Double start){
    	Line2D.Double line = new Line2D.Double(pHitPoint,nHitPoint);
    	//petLineList.add(line);
    	//testLineDraw.add(line);
    	
    	Line2D.Float pLine;
    	Line2D.Float nLine;
    	double pAngle;
    	double nAngle;
    	int pIndex;
    	int nIndex;
    	
    	if(index1<index2){
    		pLine = stripDetectorList.get(index1).getLine();
        	nLine = stripDetectorList.get(index2).getLine();
        	pAngle = stripDetectorList.get(index1).getAngle();
        	nAngle = stripDetectorList.get(index2).getAngle();
        	pIndex = index1;
        	nIndex = index2;
    	}
    	else{
    		pLine = stripDetectorList.get(index2).getLine();
    		nLine = stripDetectorList.get(index1).getLine();
    		pAngle = stripDetectorList.get(index2).getAngle();
    		nAngle = stripDetectorList.get(index1).getAngle();
    		pIndex = index2;
        	nIndex = index1;
    	}
    	
    	//petLineList.add(pLine);
    	//petLineList.add(nLine);
    	final int MAGNITUDE = 5;
    	
    	Point2D.Double pRightPoint = calculator.getIntersectionPoint(line, new Line2D.Double(pLine.x1, pLine.y1, pLine.x2, pLine.y2));
    	Point2D.Double pLeftPoint = calculator.getIntersectionPoint(line, new Line2D.Double(pLine.x1, pLine.y1, pLine.x2, pLine.y2));
    	Point2D.Double nRightPoint = calculator.getIntersectionPoint(line, new Line2D.Double(nLine.x1, nLine.y1, nLine.x2, nLine.y2));
    	Point2D.Double nLeftPoint = calculator.getIntersectionPoint(line, new Line2D.Double(nLine.x1, nLine.y1, nLine.x2, nLine.y2));
    	
    	if(!stripDetectorList.get(pIndex).getShape().contains(pRightPoint) || !stripDetectorList.get(pIndex).getShape().contains(pLeftPoint) || !stripDetectorList.get(nIndex).getShape().contains(nLeftPoint) || !stripDetectorList.get(nIndex).getShape().contains(nRightPoint)){
    		return;
    	}
    	
    	Point2D.Double pLStripPoint = new Point2D.Double(pLine.x1,pLine.y1);
    	Point2D.Double pRStripPoint = new Point2D.Double(pLine.x2,pLine.y2);
    	Point2D.Double nLStripPoint = new Point2D.Double(nLine.x1,nLine.y1);
    	Point2D.Double nRStripPoint = new Point2D.Double(nLine.x2,nLine.y2);
    	
    	double calcPRTime = calculator.getTime(start, pRightPoint, RESOLUTION, 10000/MMPERTENPIXEL) + calculator.getTime(pRightPoint, pRStripPoint, RESOLUTION, 10000/MMPERTENPIXEL);
    	double calcPLTime = calculator.getTime(start, pLeftPoint, RESOLUTION, 10000/MMPERTENPIXEL) + calculator.getTime(pLeftPoint, pLStripPoint, RESOLUTION, 10000/MMPERTENPIXEL);
    	double calcNRTime = calculator.getTime(start, nRightPoint, RESOLUTION, 10000/MMPERTENPIXEL) + calculator.getTime(nRightPoint, nRStripPoint, RESOLUTION, 10000/MMPERTENPIXEL);
    	double calcNLTime = calculator.getTime(start, nLeftPoint, RESOLUTION, 10000/MMPERTENPIXEL) + calculator.getTime(nLeftPoint, nLStripPoint, RESOLUTION, 10000/MMPERTENPIXEL);
    	
    	//********test**********
    	petLineList.add(new Line2D.Double(start, pLeftPoint));
    	petLineList.add(new Line2D.Double(start, nLeftPoint));
    	petLineList.add(new Line2D.Double(pRStripPoint, pRightPoint));
    	petLineList.add(new Line2D.Double(pLStripPoint, pLeftPoint));
    	petLineList.add(new Line2D.Double(nRStripPoint, nRightPoint));
    	petLineList.add(new Line2D.Double(nLStripPoint, nLeftPoint));
    	
    	testLineDraw.add(new Line2D.Double(start, pLeftPoint));
    	testLineDraw.add(new Line2D.Double(start, nLeftPoint));
    	testLineDraw.add(new Line2D.Double(pRStripPoint, pRightPoint));
    	testLineDraw.add(new Line2D.Double(pLStripPoint, pLeftPoint));
    	testLineDraw.add(new Line2D.Double(nRStripPoint, nRightPoint));
    	testLineDraw.add(new Line2D.Double(nLStripPoint, nLeftPoint));
    	
    	//********test end********
    	
    	boolean pRightHit = false;
    	boolean pLeftHit = false;
    	boolean nRightHit = false;
    	boolean nLeftHit = false;
    	
    	//LEGACY TIME
    	long pRightTime = 0;
    	long pLeftTime = 0;
    	long nRightTime = 0;
    	long nLeftTime = 0;
    	
    	while(!pRightHit || !pLeftHit || !nRightHit || !nLeftHit){
    		
    		if(stripDetectorList.get(pIndex).getShape().contains(pRightPoint)){
    			pRightPoint.x += MAGNITUDE*Math.cos(pAngle);
    			pRightPoint.y += MAGNITUDE*Math.sin(pAngle);
    		}
    		else{
    			pRightHit = true;
    			pRightTime = System.nanoTime();
    		}
    			
    		if(stripDetectorList.get(nIndex).getShape().contains(pLeftPoint)){
    			pLeftPoint.x += -MAGNITUDE*Math.cos(pAngle);
    			pLeftPoint.y += -MAGNITUDE*Math.sin(pAngle);
    		}
    		else{
    			pLeftHit = true;
    			pLeftTime = System.nanoTime();
    		}
    		if(stripDetectorList.get(pIndex).getShape().contains(nRightPoint)){
    			nRightPoint.x = MAGNITUDE*Math.cos(nAngle);
    			nRightPoint.y = MAGNITUDE*Math.sin(nAngle);
    		}
    		else{
    			nRightHit = true;
    			nRightTime = System.nanoTime();
    		}
    		if(stripDetectorList.get(nIndex).getShape().contains(nLeftPoint)){
    			nLeftPoint.x += -MAGNITUDE*Math.cos(nAngle);
    			nLeftPoint.y += -MAGNITUDE*Math.sin(nAngle);
    		}
    		else{
    			nLeftHit = true;
    			nLeftTime = System.nanoTime();
    		}
    		
    	}
    	
    	
    	
    	//LEGACY ARRAY
    	@SuppressWarnings("unused")
		double[] ouputArrayPrevious = {pLine.x1, pLine.y1, pRightTime, pLine.x2, pLine.y2, pLeftTime, nLine.x1, nLine.y1, nRightTime, nLine.x2, nLine.y2, nLeftTime };
    	
    	
    	double[] ouputArray = {pLStripPoint.x, pLStripPoint.y, calcPLTime, pRStripPoint.x, pRStripPoint.y, calcPRTime, nLStripPoint.x, nLStripPoint.y, calcNLTime, nRStripPoint.x, nRStripPoint.y, calcNRTime };
    	
    	stripOutputList.add(ouputArray);
    	
    	petShapeList.add(stripDetectorList.get(index1).getShape());
		petShapeList.add(stripDetectorList.get(index2).getShape());
    
    
    }
    
    private void addToList(int indexP1, int indexP2, long pHitTime, long nHitTime, Point2D.Double start){
    	
    	SimulationArithmetic calculator = new SimulationArithmetic();
    	
    	Point2D.Double center = new Point2D.Double(PREFWIDTH/2,PREFHEIGHT/2);
    	Point2D.Double centerP1 = new Point2D.Double(detectorList.get(indexP1).getShape().getBounds2D().getCenterX(), detectorList.get(indexP1).getShape().getBounds2D().getCenterY());
    	Point2D.Double centerP2 = new Point2D.Double(detectorList.get(indexP2).getShape().getBounds2D().getCenterX(), detectorList.get(indexP2).getShape().getBounds2D().getCenterY());
    	Line2D.Double line = new Line2D.Double(centerP1, centerP2);
    	
    	testLineDraw.add(line);
    	petLineList.add(line);
    	
    	double[] arrayOfOutput = new double[8];
    	
    	double calcPTime = calculator.getTime(start,centerP1, RESOLUTION, 10000/MMPERTENPIXEL);
    	double calcNTime = calculator.getTime(start,centerP2, RESOLUTION, 10000/MMPERTENPIXEL);
    	
    	arrayOfOutput[0] = calculator.findAngleOfLine(line);
    	arrayOfOutput[1] = calculator.findMagnitudeForSino(line, center);
    	arrayOfOutput[2] = centerP1.x;
    	arrayOfOutput[3] = centerP1.y;
    	arrayOfOutput[4] = centerP2.x;
    	arrayOfOutput[5] = centerP2.y;
    	arrayOfOutput[6] = calcPTime;
    	arrayOfOutput[7] = calcNTime;
    	
    	outputList.add(arrayOfOutput);
    	
    	petShapeList.add(detectorList.get(indexP1).getShape());
		petShapeList.add(detectorList.get(indexP2).getShape());
    	
    }
    
    private void paintBorders(Graphics2D g2){
    	
    	//Basic Background
    	g2.setColor(new Color(240,240,240));
    	g2.fillRect(0, 0, PREFWIDTH, PREFHEIGHT);
    	
    	//Borders
    	g2.setColor(new Color(153,153,255));
    	g2.fillRect(0, 0, PREFWIDTH, TOPPANELHEIGHT);
    	g2.fillRect(0,PREFHEIGHT-BOTTOMPANELHEIGHT,PREFWIDTH,BOTTOMPANELHEIGHT);
    	g2.fillRect(PREFWIDTH-RIGHTBORDER, TOPPANELHEIGHT, 50, PREFHEIGHT-BOTTOMPANELHEIGHT-TOPPANELHEIGHT);
    	g2.fillRect(0, TOPPANELHEIGHT, LEFTBORDER, PREFHEIGHT-BOTTOMPANELHEIGHT-TOPPANELHEIGHT);
    	
    	g2.setColor(Color.BLACK);
    	g2.drawRect(0, 0, PREFWIDTH, TOPPANELHEIGHT);
    	g2.drawRect(0,PREFHEIGHT-BOTTOMPANELHEIGHT,PREFWIDTH,BOTTOMPANELHEIGHT);
    	g2.drawRect(PREFWIDTH-RIGHTBORDER, TOPPANELHEIGHT, 50, PREFHEIGHT-BOTTOMPANELHEIGHT-TOPPANELHEIGHT);
    	g2.drawRect(0, TOPPANELHEIGHT, LEFTBORDER, PREFHEIGHT-BOTTOMPANELHEIGHT-TOPPANELHEIGHT);
    	
    	
    	
    	
    }
    
    private void paintTopRuler(Graphics2D g2){
    	//Draw Ruler
    	g2.setColor(Color.BLACK);
    	g2.drawString("0", LEFTBORDER-3, TOPPANELHEIGHT+4);
    	int lengthRuler = (PREFWIDTH-RIGHTBORDER-LEFTBORDER)*MMPERTENPIXEL; //(mm/10 of length)
    	
    	if(lengthRuler<100000000 && lengthRuler>10000000){
			for(int i = 1; i<=lengthRuler/10000000; i++){
    			// for every 100000 mm
    			g2.drawString(i + "km", LEFTBORDER+(i*10000000/MMPERTENPIXEL)-8, TOPPANELHEIGHT+15);
    		}
		}
    	if(lengthRuler<500000000){
    		for(int i = 0; i<=lengthRuler/1000000; i++){
    			// for every 100000 mm
    			g2.drawLine(LEFTBORDER+(i*1000000/MMPERTENPIXEL),TOPPANELHEIGHT-9,LEFTBORDER+(i*1000000/MMPERTENPIXEL),TOPPANELHEIGHT+9);
    		}
    		if(lengthRuler<10000000 && lengthRuler>1000000){
    			for(int i = 1; i<=lengthRuler/1000000; i++){
        			// for every 100000 mm
        			g2.drawString(i*100 + "m", LEFTBORDER+(i*1000000/MMPERTENPIXEL)-8, TOPPANELHEIGHT+15);
        		}
    		}
    	}
    	if(lengthRuler<50000000){
    		for(int i = 0; i<=lengthRuler/100000; i++){
    			// for every 10000 mm
    			g2.drawLine(LEFTBORDER+(i*100000/MMPERTENPIXEL),TOPPANELHEIGHT-7,LEFTBORDER+(i*100000/MMPERTENPIXEL),TOPPANELHEIGHT+7);
    		}
    		if(lengthRuler<1000000 && lengthRuler>100000){
    			for(int i = 1; i<=lengthRuler/100000; i++){
        			// for every 10000 mm
        			g2.drawString(i*10 + "m", LEFTBORDER+(i*100000/MMPERTENPIXEL)-8, TOPPANELHEIGHT+15);
        		}
    		}
    	}
    	if(lengthRuler<5000000){
    		for(int i = 0; i<=lengthRuler/10000; i++){
    			// for every 1000 mm
    			g2.drawLine(LEFTBORDER+(i*10000/MMPERTENPIXEL),TOPPANELHEIGHT-5,LEFTBORDER+(i*10000/MMPERTENPIXEL),TOPPANELHEIGHT+5);
    		}
    		if(lengthRuler<100000 && lengthRuler>10000){
    			for(int i = 1; i<=lengthRuler/10000; i++){
        			// for every 1000 mm
        			g2.drawString(i + "m", LEFTBORDER+(i*10000/MMPERTENPIXEL)-8, TOPPANELHEIGHT+15);
        		}
    		}
    	}
    	if(lengthRuler<500000){
    		for(int i = 0; i<=lengthRuler/1000; i++){
    			// for every 100 mm
    			g2.drawLine(LEFTBORDER+(i*1000/MMPERTENPIXEL),TOPPANELHEIGHT-3,LEFTBORDER+(i*1000/MMPERTENPIXEL),TOPPANELHEIGHT+3);
    		}
    		if(lengthRuler<10000 && lengthRuler>1000){
    			for(int i = 1; i<=lengthRuler/1000; i++){
        			// for every 100 mm
        			g2.drawString(i*10 + "cm", LEFTBORDER+(i*1000/MMPERTENPIXEL)-8, TOPPANELHEIGHT+15);
        		}
    		}
    		
    	}
    	if(lengthRuler<50000){
    		for(int i = 0; i<=lengthRuler/100; i++){
    			// for every 10 mm
    			g2.drawLine(LEFTBORDER+(i*100/MMPERTENPIXEL),TOPPANELHEIGHT-1,LEFTBORDER+(i*100/MMPERTENPIXEL),TOPPANELHEIGHT+1);
    		}
    		if(lengthRuler<1000 && lengthRuler>100){
    			for(int i = 1; i<=lengthRuler/100; i++){
        			// for every 100 mm
        			g2.drawString(i + "cm", LEFTBORDER+(i*100/MMPERTENPIXEL)-8, TOPPANELHEIGHT+15);
        		}
    		}
    	}
    }

    private void paintSideRuler(Graphics2D g2){
    	g2.setColor(Color.BLACK);
    	int lengthSideRuler = (PREFHEIGHT-TOPPANELHEIGHT-BOTTOMPANELHEIGHT)*MMPERTENPIXEL;
    	
    	if(lengthSideRuler<1000000000 && lengthSideRuler>100000000){
			for(int i = 1; i<=lengthSideRuler/10000000; i++){
    			// for every 100000 mm
				g2.drawString(i + "km", LEFTBORDER+5, TOPPANELHEIGHT+(i*1000000/MMPERTENPIXEL)+4);
    		}
		}
    	if(lengthSideRuler<100000000){
    		for(int i = 0; i<=lengthSideRuler/1000000; i++){
    			// for every 100000 mm
    			g2.drawLine(LEFTBORDER-9,TOPPANELHEIGHT+(i*1000000/MMPERTENPIXEL),LEFTBORDER+9,TOPPANELHEIGHT+(i*1000000/MMPERTENPIXEL));
    		}
    		if(lengthSideRuler<10000000 && lengthSideRuler>1000000){
    			for(int i = 1; i<=lengthSideRuler/1000000; i++){
        			// for every 100000 mm
    				g2.drawString(i*100 + "m", LEFTBORDER+5, TOPPANELHEIGHT+(i*1000000/MMPERTENPIXEL)+4);
        		}
    		}
    	}
    	if(lengthSideRuler<10000000){
    		for(int i = 0; i<=lengthSideRuler/100000; i++){
    			// for every 10000 mm
    			g2.drawLine(LEFTBORDER-7,TOPPANELHEIGHT+(i*100000/MMPERTENPIXEL),LEFTBORDER+7,TOPPANELHEIGHT+(i*100000/MMPERTENPIXEL));
    		}
    		if(lengthSideRuler<1000000 && lengthSideRuler>100000){
    			for(int i = 1; i<=lengthSideRuler/100000; i++){
        			// for every 10000 mm
    				g2.drawString(i*10 + "m", LEFTBORDER+5, TOPPANELHEIGHT+(i*100000/MMPERTENPIXEL)+4);
        		}
    		}
    	}
    	if(lengthSideRuler<1000000){
    		for(int i = 0; i<=lengthSideRuler/10000; i++){
    			// for every 1000 mm
    			g2.drawLine(LEFTBORDER-5,TOPPANELHEIGHT+(i*10000/MMPERTENPIXEL),LEFTBORDER+5,TOPPANELHEIGHT+(i*10000/MMPERTENPIXEL));
    		}
    		if(lengthSideRuler<100000 && lengthSideRuler>10000){
    			for(int i = 1; i<=lengthSideRuler/10000; i++){
        			// for every 1000 mm
    				g2.drawString(i + "m", LEFTBORDER+5, TOPPANELHEIGHT+(i*10000/MMPERTENPIXEL)+4);
        		}
    		}
    	}
    	if(lengthSideRuler<100000){
    		for(int i = 0; i<=lengthSideRuler/1000; i++){
    			// for every 100 mm
    			g2.drawLine(LEFTBORDER-3,TOPPANELHEIGHT+(i*1000/MMPERTENPIXEL),LEFTBORDER+3,TOPPANELHEIGHT+(i*1000/MMPERTENPIXEL));
    		}
    		if(lengthSideRuler<10000 && lengthSideRuler>1000){
    			for(int i = 1; i<=lengthSideRuler/1000; i++){
        			// for every 100 mm
    				g2.drawString(i*10 + "cm", LEFTBORDER+5, TOPPANELHEIGHT+(i*1000/MMPERTENPIXEL)+4);
        		}
    		}
    		
    	}
    	if(lengthSideRuler<10000){
    		for(int i = 0; i<=lengthSideRuler/100; i++){
    			// for every 10 mm
    			g2.drawLine(LEFTBORDER-1,TOPPANELHEIGHT+(i*100/MMPERTENPIXEL),LEFTBORDER+1,TOPPANELHEIGHT+(i*100/MMPERTENPIXEL));
    		}
    		if(lengthSideRuler<1000 && lengthSideRuler>100){
    			for(int i = 1; i<=lengthSideRuler/100; i++){
        			// for every 100 mm
        			g2.drawString(i + "cm", LEFTBORDER+5, TOPPANELHEIGHT+(i*100/MMPERTENPIXEL)+4);
        		}
    		}
    	}
    }
    
    private void paintBordersRunning(Graphics2D g2){
    	
    	g2.setColor(new Color(240,240,240));
    	g2.fillRect(LEFTBORDER, TOPPANELHEIGHT, PREFWIDTH-RIGHTBORDER-LEFTBORDER, PREFHEIGHT-BOTTOMPANELHEIGHT-TOPPANELHEIGHT);
    	
    	//g2.setColor(new Color(153,153,255));
    	//g2.fillRect(0, 0, PREFWIDTH, TOPPANELHEIGHT);
    	//g2.fillRect(0,PREFHEIGHT-BOTTOMPANELHEIGHT,PREFWIDTH,BOTTOMPANELHEIGHT);
    	
    	
    }

    private void paintDropBoxStrings(Graphics2D g2){
    	
    	g2.setColor(Color.BLACK);
    	g2.setFont(new Font("Arial", Font.PLAIN, 12));
    	g2.drawString("TIME RESOLUTION: " + 1/RESOLUTION,50,48);
    	g2.drawString("MM PER PIXEL: " + (double)MMPERTENPIXEL/10,450,48);
    	g2.drawString("DETECTOR SIZE: " + RECTSIZE,280, PREFHEIGHT-BOTTOMPANELHEIGHT+27);
    	g2.drawString("ARRAY SIZE: " + ARRAYSIZE,580, PREFHEIGHT-BOTTOMPANELHEIGHT+27);
    	
    	g2.drawString("ENERGY:", 830, 25);
    	if(ENERGY<1000){
    		g2.drawString(ENERGY + "eV", 840, 40);
    	}
    	else if(ENERGY<1000000){
    		g2.drawString((double)ENERGY/1000 + "keV", 840, 40);
    	}
    	else{
    		g2.drawString((double)ENERGY/1000000 + "MeV", 840, 40);
    	}
    	
    	g2.drawString("E/T:", 840, 65);
    	if(RATIO<1000000){
    		g2.drawString((double)RATIO/1000 + "keV/cm", 825, 80);
    	}
    	else{
    		g2.drawString((double)RATIO/1000000 + "MeV/cm", 825, 80);
    	}
    }
    
    public Dimension getPreferredSize() {
    	return preferredSize;
    }
    
    private void drawPoints(Graphics2D g2){
    	
    	for(int i=0; i<pointList.size();i++){
    		g2.setColor(Color.BLACK);
    		if(checkSource(pointList.get(i).x,pointList.get(i).y)){
    			g2.fillOval(pointList.get(i).x, pointList.get(i).y, 6, 6);
			}
    		else{
    			pointList.remove(i);
    			i--;
    		}
    		
    	}
    }
    
    private void drawShapesRun(Graphics2D g2, int index1, int index2){
    	//draws shapes
    	//creates random colors
    	//draws shapes

    	if(index1 >= 0 && index2 >= 0){
    		
    		
    		for(Shape s: petShapeList){
    			g2.setPaint(Color.GREEN);
    			g2.fill(s);
    			g2.setPaint(Color.BLACK);
    			g2.draw(s);
    		}
    		for(Shape s: petLineList){
    			g2.setPaint(Color.BLACK);
    			g2.draw(s);
    		}
    		
    		petShapeList.clear();
    		petLineList.clear();
    	}
    	
    	
    	
    	
    }
    
    private void drawShapes(Graphics2D g2){
    	//creates transparency
    	g2.setStroke(new BasicStroke(2));
    	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));
    	
    	for (Detector d : detectorList) {
    		g2.setPaint(Color.BLACK);
    		g2.draw(d.getShape());
    		g2.setPaint(Color.CYAN);
    		g2.fill(d.getShape());
    	}
    	
    	for (StripDetector d : stripDetectorList) {
    		g2.setPaint(Color.BLACK);
    		g2.draw(d.getShape());
    		g2.setPaint(Color.ORANGE);
    		g2.fill(d.getShape());
    	}
    	
    	g2.setComposite(AlphaComposite.SrcOver);
    	
    }
    
    private void drawPETShapes(Graphics2D g2){

    	for (Detector d : detectorList) {
    		g2.setPaint(Color.LIGHT_GRAY);
    		g2.fill(d.getShape());
    		g2.setPaint(Color.BLACK);
    		g2.draw(d.getShape());
    	}
    	for (StripDetector d : stripDetectorList) {
    		g2.setPaint(Color.LIGHT_GRAY);
    		g2.fill(d.getShape());
    		g2.setPaint(Color.BLACK);
    		g2.draw(d.getShape());
    	}
    	
    	
    	
    }

    protected void paintComponent(Graphics g) {
    	
    	super.paintComponent(g);
    	Graphics2D g2 = (Graphics2D) g;
    	
    	paintBorders(g2);
    	paintTopRuler(g2);
    	paintSideRuler(g2);
    	drawPoints(g2);
    	paintDropBoxStrings(g2);
    	drawShapes(g2);
    	
    	if (startDrag != null && endDrag != null) {
    		g2.setPaint(Color.LIGHT_GRAY);
    		Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
    		g2.draw(r);
    	}
    	g.setColor(Color.BLACK);
    	g2.setFont(new Font("Arial", Font.BOLD, 16));
    	g.drawString(label, 50, 20);
    	g2.setFont(new Font("Arial", Font.PLAIN, 12));
    }
    
    private boolean checkSource(int x, int y){
    	// side box boundaries: PREFWIDTH-50, PREFHEIGHT/2-65, 50, 130
    	
    	boolean check = (y>=TOPPANELHEIGHT && y<=PREFHEIGHT-BOTTOMPANELHEIGHT && x<PREFWIDTH-RIGHTBORDER && x>LEFTBORDER);
    	for(Detector d: detectorList){
    		check = check && !d.getShape().contains(x, y);
    	}
    	for(StripDetector d: stripDetectorList){
    		check = check && !d.getShape().contains(x, y);
    	}
    	return check;
    }
    
    private boolean checkDetector(Shape s){
    	return  !(s.intersects(0,0,PREFWIDTH,TOPPANELHEIGHT) || s.intersects(0,PREFHEIGHT-BOTTOMPANELHEIGHT,PREFWIDTH,BOTTOMPANELHEIGHT) || s.intersects(PREFWIDTH-RIGHTBORDER, TOPPANELHEIGHT, RIGHTBORDER, PREFHEIGHT-BOTTOMPANELHEIGHT-TOPPANELHEIGHT) || s.intersects(0, TOPPANELHEIGHT, LEFTBORDER, PREFHEIGHT-BOTTOMPANELHEIGHT-TOPPANELHEIGHT));
    }
    
    private boolean isDetector(float x, float y){
    	boolean isdetect = false;
    	for(Detector s: detectorList){
    		isdetect = isdetect || s.getShape().contains(x, y);
    	}
    	return isdetect;
    }
    
    private boolean isStripDetector(float x, float y){
    	boolean isdetect = false;
    	for(StripDetector s: stripDetectorList){
    		isdetect = isdetect || s.getShape().contains(x, y);
    	}
    	return isdetect;
    }
    
    private int findDetectorIndex(float x, float y){
    	int index = 0;
    	for(int i = 0; i<detectorList.size(); i++){
    		if(detectorList.get(i).getShape().contains(x,y)){
    			index = i;
    		}
    	}
    	return index;
    }
    
    private int findStripDetectorIndex(float x, float y){
    	int index = 0;
    	for(int i = 0; i<stripDetectorList.size(); i++){
    		if(stripDetectorList.get(i).getShape().contains(x,y)){
    			index = i;
    		}
    	}
    	return index;
    }
    
    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2){
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
    
    public void AddCircularDetectorArray(){
    	detectorList.clear();
    	
    	int diameter = ARRAYSIZE*4;
    	
    	double circumference = diameter*Math.PI;
    	int detectorNum = (int)(circumference/RECTSIZE);
    	double extAngle = 2*Math.PI/detectorNum;
    	
    	for(int i = 0; i< detectorNum; i++){
  			detectorList.add(new Detector(
  					new Rectangle2D.Float((float)(PREFWIDTH/2 + diameter/2*Math.cos(i*extAngle)), (float)(PREFHEIGHT/2 + diameter/2*Math.sin(i*extAngle)), RECTSIZE, RECTSIZE), i*extAngle));
  		}
    	
    	
    	
    }
    
    public void AddHorizontalStripDetectorArray(){
    	
    	stripDetectorList.clear();
    	
    	int height = ARRAYSIZE*4;
    	int width = RECTSIZE*12;
    	
    	stripDetectorList.add(new StripDetector(new Rectangle2D.Float(PREFWIDTH/2-width/2,PREFHEIGHT/2-height/2-20,width,30)));
    	stripDetectorList.add(new StripDetector(new Rectangle2D.Float(PREFWIDTH/2-width/2,PREFHEIGHT/2+height/2,width,30)));
    	
    }
    
    public void AddVerticalStripDetectorArray(){
    	
    	stripDetectorList.clear();
    	
    	int height = RECTSIZE*6;
    	int width = ARRAYSIZE*8;
    	
    	stripDetectorList.add(new StripDetector(new Rectangle2D.Float(PREFWIDTH/2-width/2-30,PREFHEIGHT/2-height/2,30,height)));
    	stripDetectorList.add(new StripDetector(new Rectangle2D.Float(PREFWIDTH/2+width/2,PREFHEIGHT/2-height/2,30,height)));
    	
    }
    
    //*****Augustus Ellis*****
  	public void AddDetectorArray(){
  		detectorList.clear();
  		
  		Rectangle2D.Float[] Rects = AllDetectors();
  		for(int i = 0; i< Rects.length; i++){
  			detectorList.add( new Detector(Rects[i]));
  		}
  	}
  //This First part goes at the end of Coordinate Area. These are just some functions to make the sides of the detector array and add them to the detector list.
  	public float[][] VertDetectorRow(float startpoint_X, float startpoint_Y, float height, float width, int length){
  			
  			float[][] PointArray = new float[length][2];
  			int arrayindex = 0;
  			for(float i = startpoint_Y; i < startpoint_Y + length*height; i=i+height){
  				PointArray[arrayindex][0] = startpoint_X;
  				PointArray[arrayindex++][1] = i;
  			}
  			return PointArray;
  			}
  	//Makes the points for a Horizontal Row of Detectors
  	public float[][] HorzDetectorRow(float startpoint_X, float startpoint_Y, float height, float width, int length){
  			
  			float[][] PointArray = new float[length][2];
  			int arrayindex = 0;
  			for(float i = startpoint_X; i < startpoint_X + length*width; i=i+width){
  				PointArray[arrayindex][1] = startpoint_Y;
  				PointArray[arrayindex++][0] = i;
  			}
  			return PointArray;
  			}
  	//Puts all four sides together and makes them into rectangles. Return an array of Rectangle2D.Float objects ready to be converted to Detector types.
  	public Rectangle2D.Float[] AllDetectors(){
  			
  			int ArrayHeight = 4*ARRAYSIZE/RECTSIZE;
  			int ArrayWidth = 8*ARRAYSIZE/RECTSIZE;
  			float TopLeft_X = PREFWIDTH/2 - 8*ARRAYSIZE/2;
  			float TopLeft_Y = PREFHEIGHT/2 - 4*ARRAYSIZE/2;
  			float DetectorHeight = RECTSIZE;
  			float DetectorWidth = RECTSIZE;
  			
  			float[][] TopRow = HorzDetectorRow(TopLeft_X, TopLeft_Y, DetectorHeight, DetectorWidth, ArrayWidth);
  			
  			float[][] BottomRow = HorzDetectorRow(TopLeft_X, TopLeft_Y + DetectorHeight*(ArrayHeight-1), DetectorHeight, DetectorWidth, ArrayWidth);
  			
  			float[][] RightColumn = VertDetectorRow(TopLeft_X+(ArrayWidth-1)*DetectorWidth, TopLeft_Y+DetectorHeight, DetectorHeight, DetectorWidth, ArrayHeight-2);
  			
  			float[][] LeftColumn = VertDetectorRow(TopLeft_X, TopLeft_Y+DetectorHeight, DetectorHeight, DetectorWidth, ArrayHeight-2);
  			 
  			float[][] DetectorPoints = new float[TopRow.length + BottomRow.length + RightColumn.length + LeftColumn.length][2];
  			
  			int i =0;
  			int j =0;
  			while(i<TopRow.length){
  				DetectorPoints[j][0] = TopRow[i][0];
  				DetectorPoints[j][1] = TopRow[i][1];
  				j++;
  				DetectorPoints[j][0] = BottomRow[i][0];
  				DetectorPoints[j][1] = BottomRow[i][1];
  				j++;
  				i++;
  			}
  			i = 0;
  			while(i < RightColumn.length){
  				
  				DetectorPoints[j][0] = RightColumn[i][0];
  				DetectorPoints[j][1] = RightColumn[i][1];
  				j++;
  				DetectorPoints[j][0] = LeftColumn[i][0];
  				DetectorPoints[j][1] = LeftColumn[i][1];
  				j++;
  				i++;
  			}
  			Rectangle2D.Float[] Rects= new Rectangle2D.Float[DetectorPoints.length];
  			for(i = 0; i<DetectorPoints.length; i++){
  				Rects[i] = new Rectangle2D.Float(DetectorPoints[i][0], DetectorPoints[i][1], DetectorWidth, DetectorHeight);
  			}
  			return Rects;
  			
  		}

    //*****MUST INCLUDE FOR IMPLEMENT MOUSE LISTENER*******
    public void mouseClicked(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
    	//finishes draw phase if drawing rectangle and detector is not selected
  
    	if(drawvalue == 2){
    		Rectangle2D.Float r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
    		
    		if(checkDetector(r)){
    			detectorList.add(new Detector(r));
    		}
    		startDrag = null;
    		endDrag = null;
    		repaint();
    	}
    	if(drawvalue == 7){
    		Rectangle2D.Float r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
    		
    		if(checkDetector(r)){
    			stripDetectorList.add(new StripDetector(r));
    		}
    		startDrag = null;
    		endDrag = null;
    		repaint();
    	}

    	//returns to detector is not selected value
    	selectvalue = 0;
    }

    public void mouseEntered(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
    	//gets coordinates
    	int x = e.getX();
    	int y = e.getY();
    	
    	
    	//if drawing source
    	if(drawvalue == 1){
    		if (point == null) {
    			point = new Point(x, y);
    		} 
    		else if(checkSource(x,y)){
    			point = new Point(x-3, y-3);
    			if(checkSource(point.x,point.y)){
    				pointList.add(point);
    			}
    			label = "Created Point at (" + x + "," + y + ").";
    			repaint();
    		}
    	}
    	//if drawing detector
    	else if(drawvalue == 2 || drawvalue == 7){
    		preX = x;
    		preY = y;
    		startDrag = new Point(x, y);
			endDrag = startDrag;
			label = "Created Detector at (" + x + "," + y + ").";
			repaint();
    	}
    	//if a detector was selected and is moving or changing angle
    	else if(isDetector(x,y) && (drawvalue == 3 || drawvalue == 4)){
    		selectvalue = 1;
    		
    		//remember position
    		preX = x;
    		preY = y;
    		index = findDetectorIndex(preX,preY);
    	}
    	else if(isStripDetector(x,y) && ( drawvalue == 3|| drawvalue == 4)){
    		selectvalue = 1;
    		
    		//remember position
    		preX = x;
    		preY = y;
    		index = findStripDetectorIndex(preX,preY);
    	}

    	//if detector was selected and copying
    	else if(isDetector(x,y) && drawvalue == 5){
    		selectvalue = 1;
    		preX = x;
    		preY = y;
    		index = findDetectorIndex(preX,preY);
    		
    		if(detectorList.get(index).getShape().contains(preX,preY)){
    			//deletes detector
    			detectorList.add(new Detector(detectorList.get(index).copyShape()));
    			repaint();
    		}
    		
    		
    	}
    	else if(isStripDetector(x,y) && drawvalue == 5){
    		selectvalue = 1;
    		preX = x;
    		preY = y;
    		index = findStripDetectorIndex(preX,preY);
    		
    		if(stripDetectorList.get(index).getShape().contains(preX,preY)){
    			//deletes detector
    			stripDetectorList.add(new StripDetector(stripDetectorList.get(index).copyStripShape(), stripDetectorList.get(index).getAngle()));
    			repaint();
    		}
    		
    		
    	}
    	//if detector was selected and deleting
    	else if(isDetector(x,y) && drawvalue == 6){
    		selectvalue = 1;
    		preX = x;
    		preY = y;
    		index = findDetectorIndex(preX,preY);
    		
    		if(detectorList.get(index).getShape().contains(preX,preY)){
    			//deletes detector
    			detectorList.remove(index);
    			repaint();
    		}
    		
    	}
    	else if(isStripDetector(x,y) && drawvalue == 6){
    		selectvalue = 1;
    		preX = x;
    		preY = y;
    		index = findStripDetectorIndex(preX,preY);
    		
    		if(stripDetectorList.get(index).getShape().contains(preX,preY)){
    			//deletes detector
    			stripDetectorList.remove(index);
    			repaint();
    		}
    		
    	}

    	
    	
    	
    }

    public void mouseDragged(MouseEvent e) {
    	
    	//draws a large amount of points
    	if(drawvalue == 1){
    		if (point == null) {
    			point = new Point(e.getX(), e.getY());
    		} 
    		else if(checkSource(e.getX(), e.getY())){
    			point = new Point(e.getX() - 3, e.getY() - 3);
    			if(checkSource(point.x,point.y)){
    				pointList.add(point);
    			}
    			label = "Created Point at (" + e.getX() + "," + e.getY() + ").";
    			repaint();
    		}
    	}    	
    	//drawing while dragging mouse if drawing detector
    	if(drawvalue == 2 || drawvalue == 7){
    		endDrag = new Point(e.getX(), e.getY());
    		label = "Detector Created at  (" + preX + "," + preY + "), Dragging to (" + e.getX() + "," + e.getY() + ").";
    		repaint();
    	}
    	else if(isDetector(preX,preY) && selectvalue == 1 && drawvalue == 3){
			//if moving detector is selected
			//move detector
    		int dx = e.getX() - preX;
    	    int dy = e.getY() - preY;

    	    if(detectorList.get(index).getShape().contains(preX,preY)){
    	    	detectorList.get(index).moveShape(dx, dy);
    	    }
    	    repaint();
    	    
    	    preX += dx;
    	    preY += dy;
    		
		}
    	else if(isStripDetector(preX,preY) && selectvalue == 1 && drawvalue == 3){
			//if moving detector is selected
			//move detector
    		int dx = e.getX() - preX;
    	    int dy = e.getY() - preY;

    	    if(stripDetectorList.get(index).getShape().contains(preX,preY)){
    	    	stripDetectorList.get(index).moveShape(dx, dy);
    	    }
    	    repaint();
    	    
    	    preX += dx;
    	    preY += dy;
    		
		}
    	else if(isDetector(preX,preY) && selectvalue == 1 && drawvalue == 4){
    		//if changing angle detector is selected
    		//change angle
    		int dx = e.getX() - preX;
    	    int dy = e.getY() - preY;
    		
    	    SimulationArithmetic calculator = new SimulationArithmetic();
    	    
    		Point2D.Double center = new Point2D.Double(detectorList.get(index).getShape().getBounds2D().getCenterX(), detectorList.get(index).getShape().getBounds2D().getCenterY());
    		double angle = calculator.angleOfChange(center, new Point2D.Double(preX,preY), new Point2D.Double(e.getX(),e.getY()));
    		
    		detectorList.get(index).rotateShape(angle);
    		repaint();
    
    		
    		preX += dx;
    	    preY += dy;
    		
    	}
    	else if(isStripDetector(preX,preY) && selectvalue == 1 && drawvalue == 4){
    		//if changing angle detector is selected
    		//change angle
    		int dx = e.getX() - preX;
    	    int dy = e.getY() - preY;
    		
    	    SimulationArithmetic calculator = new SimulationArithmetic();
    	    
    		Point2D.Double center = new Point2D.Double(stripDetectorList.get(index).getShape().getBounds2D().getCenterX(), stripDetectorList.get(index).getShape().getBounds2D().getCenterY());
    		double angle = calculator.angleOfChange(center, new Point2D.Double(preX,preY), new Point2D.Double(e.getX(),e.getY()));
    		
    		stripDetectorList.get(index).rotateShape(angle);
    		repaint();
    
    		
    		preX += dx;
    	    preY += dy;
    		
    	}

    }

	public void actionPerformed(ActionEvent e) {}

  }