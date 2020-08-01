package BrickGame;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener{
	private boolean play=false;
	private int score =0;
	private int totBricks= 21;
	private Timer timer;
	private int delay = 10;
	private int playX=310; 
	private int ballXpos=120;
	private int ballYpos=350;
	private int ballXdir=-1;
	private int ballYdir=-2;
	
	private MapGen map;
	
	public GamePlay() {
		map=new MapGen(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer=new Timer(delay,this);
		timer.start();
	}
	public void paint(Graphics g) {
		g.setColor(Color.black);//for background
		g.fillRect(1, 1, 692, 592);
		
		map.draw((Graphics2D)g);//to draw map
		
		g.setColor(Color.blue);//for borders
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		g.setColor(Color.white);//for scores
		g.setFont(new Font("serif",Font.BOLD,25));
		g.drawString(""+ score, 590, 30);
		
		g.setColor(Color.green);//for paddle
		g.fillRect(playX, 550, 100, 8);
		
		g.setColor(Color.yellow);//for ball
		g.fillOval(ballXpos, ballYpos, 20, 20);
		
		if(totBricks<=0) {
			play=false;
			ballXdir=0;
			ballYdir=0;
			g.setColor(Color.red);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("You WON! "+ score,260,300);
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart! ",230,350);	
		}
		if(ballYpos>570) {
			play=false;
			ballXdir=0;
			ballYdir=0;
			g.setColor(Color.red);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("Game Over! Score: "+score,190,300);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart! ",230,350);
		}
		
		g.dispose();	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(play) {
			if(new Rectangle(ballXpos,ballYpos,20,20).intersects(new Rectangle(playX,550,100,8))) {
				ballYdir=-ballYdir;
			}
			
			A: for(int i=0;i<map.map.length;i++) {
				for(int j=0;j<map.map[0].length;j++) {
					if(map.map[i][j]>0) {
						int brickX=j*map.brickW + 80;
						int brickY=i*map.brickH + 50;
						int brickW=map.brickW;
						int brickH=map.brickH;
						
						Rectangle rect=new Rectangle(brickX,brickY,brickW,brickH);
						Rectangle ballRect=new Rectangle(ballXpos,ballYpos,20,20);
						Rectangle brickRect=rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickVal(0,i,j);
							totBricks--;
							score+=5;
							
							if(ballXpos + 19 <= brickRect.x ||ballXpos+1 >= brickRect.x+brickRect.width) {
								ballXdir=-ballXdir;
							}
							else {
								ballYdir=-ballYdir;
							}
							break A;
						}
					}
				}
			}
			
			ballXpos+=ballXdir;
			ballYpos+=ballYdir;
			if(ballXpos<0) {
				ballXdir= -ballXdir;
			}
			if(ballYpos<0) {
				ballYdir= -ballYdir;
			}
			if(ballXpos>670) {
				ballXdir= -ballXdir;
			}
		}
		repaint();	
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
		if(playX>=600) {
			playX=600;
		}else {
			moveRight();
			}
		}
        if(e.getKeyCode()==KeyEvent.VK_LEFT) {
        	if(playX<10) {
    			playX=10;
    		}else {
    			moveLeft();
    		}	
		}
        if(e.getKeyCode()==KeyEvent.VK_ENTER) {
        	if(!play) {
        		play=true;
        		ballXpos=120;
        		ballYpos=350;
        		ballXdir=-1;
        		ballYdir=-2;
        		playX=310;
        		score=0;
        		totBricks=21;
        		map=new MapGen(3,7);
        		
        		repaint();
        	}
        }
		
	}
	public void moveRight() {
		play=true;
		playX+=20;
	}
	public void moveLeft() {
		play=true;
		playX-=20;
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
