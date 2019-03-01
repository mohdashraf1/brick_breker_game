package brickbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private  boolean play = false; //at the starting game is not playing
    private int score = 0;

    private int totalBricks = 21; //if the dimension 7 by 3

    private Timer timer;
    private int delay=8;

    private int playerX = 310; // x axis of player

    private int ballX = 120; // x axis of ball
    private int ballY = 350; // y axis
    private  int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map; // initialising variable for the creation of object og mapgenerator claa.

    public Gameplay() // a contructor ,,when an object is initialised at main then ALL the info within this block would be called upon.
    {
        map = new MapGenerator(3,7); //creating an object of mapgenerator class
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g){
        //background
        g.setColor(Color.blue);
        g.fillRect(1,1,692,592);

        // drawing map
        map.draw((Graphics2D)g);

        //borders
        g.setColor(Color.black);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);
        // not adding border to the bottom so that game should be over after the fall to ground

        // scores
        g.setColor(Color.yellow);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("score"+score, 590,30);



        //the paddle
        g.setColor(Color.RED);
        g.fillRect(playerX,550,100,8);


        //the ball
        g.setColor(Color.green);
        g.fillOval(ballX,ballY,20,20);

        //begin for gameOver condition
        if(totalBricks <= 0){
            play=false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.yellow);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString(" ...YOU WON!!! SCORE="+score,260,300);


            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("PRESS ENTER TO RESTART!!",260,350);

        }

        if(ballY>570){
            play=false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.yellow);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER !!! SCORE="+score,190,300);


            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("PRESS ENTER TO RESTART!!",230,350);

        }

        g.dispose();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){
            if( new Rectangle(ballX,ballY,20,20).intersects(new Rectangle(playerX,550,100,8))){
                ballYdir = - ballYdir; //for detection of intersection of ball and player
            }
            for(int i=0; i<map.map.length; i++){ //map.map = first map is the object that is created above in this class the second map is 2D matrix of the map generator class.
                A: for( int j=0; j<map.map[0].length; j++){
                    if(map.map[i][j] > 0){ //describes the position
                        int brickX = j*map.brickWidth + 80;
                        int brickY = i*map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight); //building rectangle around the brick
                        Rectangle ballRect = new Rectangle(ballX,ballY,20,20); //build rect around the ball.
                        Rectangle brickRect = rect;

                        //intersection from bottom layer of bricks
                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score +=5;

                            //intersecttion from left & right
                            if(ballX + 19 <= brickRect.x || ballX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            }else {
                                ballYdir = -ballYdir;
                            }
                            break A;

                        }



                    }


                }
            }


            ballX += ballXdir;
            ballY += ballYdir;
            if(ballX < 0){//checking left border
                ballXdir = -ballXdir;
            }
            if(ballY < 0){ //checking to border
                ballYdir = -ballYdir;
            }
            if(ballX > 670){//checking right border
                ballXdir = -ballXdir;
            }

        }
        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT ){
            if(playerX >=600 ){
                playerX = 600;
            }else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }else{
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballX = 120;
                ballY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);
                repaint();
            }
        }

    }
    public void moveRight(){
        play = true;
        playerX+=20;
    }
    public void moveLeft(){
        play = true;
        playerX-=20;
    }


}
