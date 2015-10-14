package toh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PaintableStack extends Stack{
	
	public int x,y;//position
	int h,num,drate;//certain variables which help in painting the stack
	public static final int DISK_H = 30, STACK_W=200, MAX_D=170, MIN_D=70, D_DIFF = MAX_D-MIN_D,
			ARCNESS = 30, V=4, DELAY = 10;//initializing variables for stack painting.
	JPanel owner;// panel in which the stack is painted
	JFrame frame;
	Image img;// for double buffering
	TowersOfHanoi t;
	
	public PaintableStack(int sstart,int sstop,int[] elemss,TowersOfHanoi tt){
		super(sstart, sstop, elemss);
		num = stop-start;// max number of elements
		h = num*DISK_H;//total height
		drate = D_DIFF/num;//the rate at which width of disks decreases
		frame = tt.jf;
		img = tt.img;// to get the the canvas image of the towers of hanoi object to draw
		t=tt;//towers of hanoi object which uses this stack
	}
	
	public void paintStack(Graphics g){
		//to draw the stack and its disks
		
		//basal plate
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y+h, STACK_W, DISK_H);
		g.setColor(Color.black);
		g.drawRect(x, y+h, STACK_W, DISK_H);
		
		//column
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x+(STACK_W/2)-10,y-(2*DISK_H),20,h+(2*DISK_H));
		g.setColor(Color.black);
		g.drawRect(x+(STACK_W/2)-10,y-(2*DISK_H),20,h+(2*DISK_H));
		
		//disks
		for(int i = 1;i<=num;i++){
			drawDisk(x+STACK_W/2,y+h-(i*DISK_H),elems[start+i-1],g);
		}
		
	}
	
	public void drawDisk(int centre,int y, int n, Graphics g){
		if(n==0)return;
		int w = MIN_D + drate*n;// to get the width
		g.setColor(Color.red);//body
		g.fillRoundRect(centre-w/2, y, w, DISK_H, ARCNESS, ARCNESS);
		g.setColor(Color.black);//border
		g.drawRoundRect(centre-w/2, y, w, DISK_H, ARCNESS, ARCNESS);
	}
	
	public int pop(){// this has animation of popping
		Graphics g;
		int a = super.pop();// pops the disk from the stack
		t.popspace = x+STACK_W/2;// position of popped disk
		t.popsize = a;// size of disk popped
		int disky = y+h-((top+2)*DISK_H);
		while(disky>=y-3*DISK_H){// moves until the disk is above the stack
			disky-=V;// moves the disk upward
			g=img.getGraphics();
			t.draw(g);// draws all the stacks
			drawDisk(x+STACK_W/2,disky,a,g);// draws the moved disk
			frame.repaint();
			try {
				Thread.sleep(DELAY);// delay
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return a;
	}
	
	public void push(int a){
		if(t.popspace==0){super.push(a);return;}
		
		//to move disk over to current stack
		
		Graphics g;
		int d = x+STACK_W/2-t.popspace;// displacement to move disk
		int diskx = t.popspace;// starting x
		int s = Math.abs(d);//distance to move disk
		int sign = d/s;// whether to move left or right
		while(s>3){
			s=Math.abs(x+STACK_W/2-diskx);//updates distance from destination
			diskx+=sign*V;// moves disk in required direction
			g=img.getGraphics();
			t.draw(g);// draw the stacks
			drawDisk(diskx,y-3*DISK_H,a,g);// draw the disk which is moving
			frame.repaint();
			try {
				Thread.sleep(DELAY);// delay
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//to push disk down the current stack.
		
		double disky = y-3*DISK_H;
		double v = 0;
		double acc = (0.4/3)*V;// acceleration to make it feel like the disk is being dropped
		while(disky<=y+h-((top+2)*DISK_H)){// moves until the disk is on the top disk of current stack
			disky+=v;// update position
			v+=acc;// update velocity
			g=img.getGraphics();
			t.draw(g);// draw stacks
			drawDisk(x+STACK_W/2,(int)disky,a,g);// draw disk being dropped
			frame.repaint();
			try {
				Thread.sleep(DELAY);// delay
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		super.push(a);// push the disk into the internal element array
	}
	
	public void setX(int xx){ x=xx; }
	public void setY(int yy){ y=yy; }
	
}
