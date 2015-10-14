package toh;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TowersOfHanoi {
	
	Stack[] towers = new Stack[3];
	private static final int DEPTH = 370;
	private static final double VERSION = 1.0;
	JFrame jf;
	JPanel jp;
	int diskno,height;// no of disks and height.
	Graphics g;
	Image img;
	int popspace,popsize;// variables to hold position and size of popped disk
	ImageIcon imgicon;
	
	public static void main(String[] args) {
		new TowersOfHanoi();// to exit static scope
	}
	
	@SuppressWarnings("serial")
	public TowersOfHanoi(){
		
		// to obtain the icon in title bar and exit message
		imgicon = new ImageIcon(
						"/res/exiticon.PNG"
						)
						;
		
		//Graphics
		
		jf = new JFrame("Towers of Hanoi");
		// to display exit message when the window is closed.
		jf.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				exit();
			}
		});
		
		// sets up the window's size and title bar icon
		jf.setSize(1000,600);
		jf.setVisible(true);
		jf.setIconImage(imgicon.getImage());
		
		
		// to obtain the number of disks from the user.
		String a;// raw input place holder
		boolean parsable = false;
		while(!parsable){// continually asks for input until a valid input is entered
			a = JOptionPane.showInputDialog(jf, "Enter number of disks: ");// input prompt
			if(a==null)exit();
			// tries to parse raw input into integer
			try{
				diskno = Integer.parseInt(a.trim());
				parsable = true;
				// checks if the parsed number is too big or a negative number
				if(diskno>19||diskno<1){
					parsable = false;
					JOptionPane.showMessageDialog(jf, "The number is too big or non-physical!!\n" +
							"Please enter a proper number!!");
				}
			}catch(NumberFormatException e){
				// catches exception if the raw input cannot be parsed
				JOptionPane.showMessageDialog(jf, "That is not a number!!\n" +
						"Please enter a proper number!!");
				parsable = false;
			}
		}
		
		height = Math.max((diskno+8)*PaintableStack.DISK_H,600);// height of the towers of hanoi setup
		img = jf.createImage(1000, height);// double buffering image
		g = img.getGraphics();// graphics object to draw
		
		jp = new JPanel(){
			public void paintComponent(Graphics gg){
				// draws the double buffered canvas image
				gg.drawImage(img, 0, 0, null);
				
				// clears the canvas image
				g.setColor(Color.white);
				g.fillRect(0, 0, 1000, height);			
			}
		};
		jp.setPreferredSize(new Dimension(1000,height));// changes the size of the window
		
		
		//set up of the three stacks as paintable stacks
		int[][] disks = new int[3][diskno];// disks is an int array
		// towers is an array of paintable stacks
		towers[0] = new PaintableStack(0,diskno,disks[0],this);
		towers[1] = new PaintableStack(0,diskno,disks[1],this);
		towers[2] = new PaintableStack(0,diskno,disks[2],this);
		for(int i = diskno;i>0;i--){
			towers[0].push(i);
		}
		
		// packing the components		
		jf.setContentPane(jp);
		jf.pack();
		
		//Graphics end
		
		//Thread
		Thread t = new Thread(new Runnable(){
			public void run(){
				for(int i = 0;i<100;i++){
					draw(g);// draws all required elements on the canvas image
					jf.repaint();// repaints the jpanel
					try {
						Thread.sleep(10);// delay
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				// sets up the initial position of the puzzle
				moveDisks(towers[0],diskno,towers[2],towers[1]);
				// continually refreshes the double buffered image and the jpanel.
				while(true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					draw(g);// refresh the double buffered image
					jf.repaint();// refresh the jpanel
				}
			}
		});
		
		t.start();
		
		//Thread end
		
		// for debugging purposes
//		Stack stt = towers[0].subStack(2);
//		System.out.println(towers[0]+"\n"+
//				"start = "+towers[0].start+"\n"+
//				"stop = "+towers[0].stop+"\n"+
//				"size = "+towers[0].size+"\n"+
//				"top = "+towers[0].top+"\n");
//		System.out.println(stt+"\n"+
//				"start = "+stt.start+"\n"+
//				"stop = "+stt.stop+"\n"+
//				"size = "+stt.size+"\n"+
//				"top = "+stt.top+"\n");
//		System.out.println(towers[0].subStack(1).top);

	}
	
	
	// The main logic of solving the puzzle:
	// moveDisks moves 'num' number of disks from the stack 'from' to the stack 'to' using a memory
	// stack called 'memo' using recursion.
	public void moveDisks(Stack from, int num, Stack to, Stack memo){
		
		// two base cases if the number of disks are 1 or 2
		if(num == 1){// simple case
			to.push(from.pop());
//			display();
		}else if(num == 2){// base case to move 2 disks
			memo.push(from.pop());
//			display();
			to.push(from.pop());
//			display();
			to.push(memo.pop());
//			display();
		}else{// recursive case
			moveDisks(from,num-1,memo,to);// moves top num-1 disks to the 'memo'
			moveDisks(from,1,to,memo);// moves one disk from 'from' to 'to'
			moveDisks(memo,num-1,to,from);// moves the num-1 disks from 'memo' to 'to'
		}
		//for debugging purposes
//		System.out.println(towers[0]+"\n"+towers[1]+"\n"+towers[2]+"\n\n");
		
	}
	
	public void draw(Graphics g){
		// to draw the three stacks

		//first stack
		PaintableStack pt1 = (PaintableStack)towers[0];// casting the stack into a paintable stack
		int depth = DEPTH-pt1.h/2;// finding the y coordinate of the stack
		pt1.setX(150);// initial stack has x coordinate 150
		pt1.setY(depth);
		pt1.paintStack(g);// paints the stack
		
		//second stack
		PaintableStack pt2 = (PaintableStack)towers[1];
		pt2.setX(150+PaintableStack.STACK_W+50);//adds the width of one stack to the initial value
		pt2.setY(depth);
		pt2.paintStack(g);
		
		//third stack
		PaintableStack pt3 = (PaintableStack)towers[2];
		pt3.setX(150+2*PaintableStack.STACK_W+100);//adds the width of two stacks to the initial value
		pt3.setY(depth);
		pt3.paintStack(g);
		
	}
	
	public void exit(){
		//message displayed when window is closed
		String msg = "Towers of Hanoi v"+VERSION+" made by Rajat V D\n" +
				"Copyright \u24b8 2013";
		JOptionPane.showMessageDialog(jf, msg, "Exiting...", JOptionPane.PLAIN_MESSAGE, imgicon);
		System.exit(0);
	}
	
	//for debugging purposes
	public void display(){
		System.out.println(towers[0]+"\n"+towers[1]+"\n"+towers[2]+"\n\n");
		jf.repaint();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
