import java.io.*;
import java.util.*;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Main class
public class Ninja_Run extends JFrame{

	public static void main(String args[]){

		ninja_run m = new ninja_run();
		Ninja_Run jf = new Ninja_Run();
		jf.setSize(600,320);
		jf.add(m);

		jf.setResizable(false);

		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
}

interface Ninja{

	//Locations for Images
	String run_name_indx = "png/Run__00",
	jump_name_indx = "png/Jump__00",
	glide_name_indx = "png/Glide_00",
	obstacle_name_indx = "obstacle/obstacle__00";}


class ninja_run extends Canvas implements Runnable, Ninja{

	Toolkit t = Toolkit.getDefaultToolkit();

	private Image ninja = t.getImage("idle.png"),obstacle_img;
	private Image[] ninja_Run =  new Image[10];
	private Image[] ninja_Jump =  new Image[10];
	private Image[] ninja_Glide = new Image[10];
	private Image[] obs_Image =  new Image[4];

	Thread th;

	//Coordinates
	Integer inx = 0, obx = 0,ninja_pos_y = 230, ninja_pos_x = 50; 

	//Bool vars for check
	Boolean jump = false,fall = false;

	//Score Card var
	Integer Score  = 0;

	private Image bg_image = t.getImage("obstacle/City1.png");

	//Default Constructor
	ninja_run(){

		get_images();
		//Starting the thread for animation
		th = new Thread(this);
		th.start();

		this.addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e) {
		        jump = (fall == true)?jump:true;
		        inx = 0;
		        }

				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
				}

		});
		this.setFocusable(true);
	}


	//For Run Action
	private void run_action(){
		ninja = ninja_Run[inx%10];
	}

	//For Jump Action
	private void jump_action(){
		if(inx == 9)
		{
			jump = false;
			fall = true;
			inx = 0;
			return;
		}
		ninja = ninja_Jump[inx%10];
		ninja_pos_y-=10;
	}

	//For Fall Action
	private void fall_action(){
		if(ninja_pos_y == 230)
		{
			inx =0;
			jump = false;
			fall = false;
			return;
		}
		ninja = ninja_Glide[inx%10];
		ninja_pos_y+=5;
	}



	//Storing images in an Image[]
	private void get_images(){
		for(Integer i = 0;i<10;i++){
			String img_name = i.toString() + ".png";
			ninja_Run[i] = t.getImage(run_name_indx+img_name);
			ninja_Jump[i] = t.getImage(jump_name_indx+img_name);
			ninja_Glide[i] = t.getImage(glide_name_indx+img_name);
		}

		for(Integer i = 0;i<4;i++){
			String img_name = i.toString() + ".png";
			obs_Image[i] = t.getImage(obstacle_name_indx+img_name);
		}
	}


	//Selection of obstacle
	private void choose_obstacle(){
		Random rnd = new Random();
		obstacle_img = obs_Image[rnd.nextInt(4)];}

	//Generate Obstacles
	private void generate_obstacle(){
		choose_obstacle();
		obx = 600; 
	}


	//Obstacle movement
	private void obstacle_action(){
		if(obx <= 0){
			generate_obstacle();
			Score++;
		}
		else{
			obx-=5;
		}
	}

	public void run(){	
		try{
			while(true){

				obstacle_action();
				
				//Jump Action
				if(jump){
					jump_action();
				}

				//Fall Action
				else if(fall){
				    fall_action();
				}
				
				//Run Action
				else{
					run_action();
				}

				//Collision check
				if((ninja_pos_x >= (obx-20) && ninja_pos_x <=(obx+5)) && ninja_pos_y == 230){
					JOptionPane.showMessageDialog(this,"Game Over Your Score is " + Score.toString());  
            		System.exit(0);		
				}				
				repaint();
				th.sleep(55);
				inx+=1;
			}
		}
		catch(InterruptedException e ){
			System.out.print("Exception Caught");
		}
	}

	//Paint
	public void paint(Graphics g){	
		g.drawImage(bg_image,0,-76,600,360,this);
		g.drawImage(ninja,ninja_pos_x,ninja_pos_y,45,60,this);
		g.drawImage(obstacle_img,obx,260,50,30,this);
	}
}