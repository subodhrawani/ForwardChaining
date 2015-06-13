package ai_project_final;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class DisplayTree{
	
	public static JLabel imageView;
    public static BufferedImage image;
    protected static LinkedList<String> FactStack;
    

	public static final double TRIM_FACTOR = 0.80;
    public static int SIZE_X;
    public static int SIZE_Y;
    public static final double INITIAL_LENGTH = 180;
    public static final double EXIT_LENGTH = 10;
    public static final double BRANCH_ANGLE = Math.PI / 3.0;
    public static final int WAIT = 15;
    private DrawTree thread = new DrawTree();
	public DisplayTree()
	{
		FactStack= new LinkedList<String>();
		 image = new BufferedImage(800, 700, BufferedImage.TYPE_INT_RGB);
		 imageView = new JLabel(new ImageIcon(image));
		 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 SIZE_X=screenSize.width/2;
		 SIZE_Y=screenSize.height;

	}
    
	public Component getGui() {
		JPanel gui = new JPanel(new BorderLayout(5, 5));
	    gui.setBorder(new EmptyBorder(3, 3, 3, 3));
	    //gui.setBackground(Color.gray);
	    gui.add(imageView, BorderLayout.CENTER);

	    drawTree();
	    return gui;
	}
	
	 
	     public void initiateTree() 
		 {
	            
	            	Runnable r = new Runnable() {
            public void run() {
                // Frame-Init
                JFrame f = new JFrame("BackWard Chaining");
                f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                f.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                      thread.stopTimer();
                      BackwardChaining.FactStack.clear();
                      BackwardChaining.TrueFacts.clear();
                      BackwardChaining.FactDetails.clear();
                    	
                    }
                  });
                //f.setBackground(Color.gray);
                //f.setLocationByPlatform(true);
                Container cp = f.getContentPane();
                cp.setLayout(new BorderLayout(3, 3));

                cp.add(getGui());

                f.pack();
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
	            
    }
	     public void drawTree() {
			 
	    	 
	         thread.setDaemon(true);
	         thread.start();
	   }

public static class  DrawTree extends Thread
{
	boolean stop=false;
	
	public void stopTimer() {
	    stop = true;
	}
	
	@Override
    public void run() {
		
	
        while (true) {
        	
        	if(BackwardChaining.FactStack.size()==0)
        		continue;
        	if (stop)
                break;
        	init();
        	Graphics graphics = image.getGraphics();
        	
	        Graphics2D g=(Graphics2D) graphics;
	        g.fillRect(0, 0, 800,700);
	        g.setStroke(new BasicStroke(5));
	        //g.setBackground(Color.WHITE);
	        drawLine2(g,SIZE_X / 2, -150, INITIAL_LENGTH, Math.PI / 2);
            //System.out.println("Executed!");
            imageView.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
	}
	
	public void drawLine2(Graphics g, double x1, double y1, double l, double theta) {
		double x2=0,y2=0;
		//System.out.println("\n-------------");
		
		while(FactStack.size()!=0)
		{
			
			String fact=FactStack.pop();
			if(fact.compareTo("(")==0)
			{
				
				drawLine2(g, x2, y2, l , theta + 1.5*BRANCH_ANGLE);
				continue;
			}
			else if(fact.compareTo(")")==0) 
				return;
			
			x2 = x1 + l * Math.cos(theta);
			y2 = y1 + l * Math.sin(theta);
			
           //	System.out.print("coord--> "+x2+","+y2);
			g.setColor(Color.getHSBColor(48, 53, 100));
			
				
			g.drawLine((int)x1, (int)(y1), (int)(x2), (int)(y2));
				
			g.setColor(Color.getHSBColor(202, 100, 100));
			boolean colorchang=false;
			for(Object tf : BackwardChaining.TrueFacts) {
				String element = (String) tf;
				if(element.compareTo(fact)==0)
					colorchang=true;
			}
			if(colorchang==true)
				g.setColor(Color.getHSBColor(48, 53, 100));
			g.fill3DRect((int)x2-30, (int)(y2)-30,60,60,true);
			//g.fillOval((int)x2-30, (int)(y2)-30,60,60);
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 20)); 
			g.drawString(fact, (int)x2, (int)y2);
			g.drawString(BackwardChaining.FactDetails.get(fact),(int)x2+10, (int)y2+50);
			theta=theta-BRANCH_ANGLE;
			
		}
		System.out.println();
	}
	public void init() {

		FactStack.clear();
		for(Object fact : BackwardChaining.FactStack) {
			String element = (String) fact;
			//System.out.print(element);
			FactStack.add(element);
		}
		
		System.out.println("Element Present in Stack -->: "+ BackwardChaining.FactDetails.size());
		for(Object fact : FactStack) {
			String element = (String) fact;
			System.out.print(element);
		}
		System.out.println();
		for (Map.Entry<String, String> entry : BackwardChaining.FactDetails.entrySet())
		{
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		}
		System.out.println();
		
	}
}
}
