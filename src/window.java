import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/*********************************************************************************************************************
 * Created by mateuszstolowski on 4.04.2018.
 * @author mateuszstolowski kamilszpakowski
 * @version 2.0
 *
 * Main class
 */


public class window extends JFrame implements KeyListener {

    /*****************************************************************************************************************
     * Class parameters
     *
     * @param background            holds background image
     * @param FRAME_WIDTH           holds frame width
     * @param FRAME_HEIGHT          holds frame height
     * @param flappy                this is a Flappy Bird, player
     * @param keypressed            informs if player hit spacebar at least one time to start animation thread
     * @param countdownTexture      holds texture for countdown numbers
     * @param score1NumberTexture   holds texture for first number of score
     * @param score2NumberTexture   holds texture for second number of score
     * @param movement              holds information how the bird is moving
     * @param speed                 holds information how quickly animation is happening
     * @param coutndownTextureX     holds information where to paint countdown texture
     * @param coutndownTextureY     holds information where to paint countdown texture
     * @param countdown             tells if coutndown is happening
     * @param collision             tells if collision happened
     * @param pipesTimer            tells when to add another pipes column
     * @param score                 holds user score
     * @param score1Number          holds 1 number of a score
     * @param score2Number          holds 2 number of a score
     * @param birdAnimList          holds list of threads for animating the bird
     * @param pipeAnimList          holds list of threads for animating pipes
     * @param pipes                 holds list of pipes that are painted on the frame
     * @param birdAnim              holds single reference to a thread that is currently animatig bird
     * @param pipeAnim              holds single reference to a thread that is currently animatig pipes
     */

    private final       Image background = new ImageIcon("resources/bg.png").getImage();
    private final       int   FRAME_WIDTH = background.getWidth(null);
    private final       int   FRAME_HEIGHT = background.getHeight(null);
    private final       Image GROUND_TEXTURE = new ImageIcon("resources/Ground.png").getImage();


    private             Image countdownTexture = new ImageIcon("resources/logo.png").getImage();
    private             Image score1NumberTexture;
    private             Image score2NumberTexture;
    private             bird flappy = new bird();
    private boolean     keypressed = false;
    private int         movement;
    private int         speed;
    private int         coutndownTextureX;
    private int         coutndownTextureY;
    private boolean     countdown = true;
    private boolean     collision = false;
    private int         pipesTimer;
    private int         score;
    private int         score1Number;
    private int         score2Number;


    //Array lists for new threads
    private ArrayList<Thread> birdAnimList = new ArrayList<>();
    private ArrayList<Thread> pipeAnimList = new ArrayList<>();

    //ArrayList for pipes
    private ArrayList<pipeColumn> pipes = new ArrayList<>();

    //Bird and pipe animation threads
    private Thread birdAnim;
    private Thread pipeAnim;

    /*****************************************************************************************************************
     * Constructor - defines the frame, sets primary values and draws everything
     */
    public window(){

        //Creating and setting flappy bird on starting position
        setBirdAtStartPosition();

        //Ensuring capacity for program to work faster
        ensureCapacity();

        setScore1NumberTexture(0);
        setScore2NumberTexture(0);

        //Setting position of logo image
        setCoutndownTextureX(flappy.getPosX()-35);
        setCoutndownTextureY(flappy.getPosY()-30);

        //Setting frame size & title & non-resizable & close operation & location center & KeyListener to this frame
        this.setSize(2*FRAME_WIDTH, FRAME_HEIGHT+22);
        this.setTitle("Flappy Bird - Game");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.addKeyListener(this);


        //Adding new object to print everything on a frame
        this.add(new drawing());


        //Setting window visable
        this.setVisible(true);
    }

    /***************************************************************************************************************
     * Methods
     */

    public static void main(String args[]){
        EventQueue.invokeLater(new Runnable()
        {
            public void run(){
                new window();
            }
        });
    }

    public void setMovement(int m){ this.movement = m; }

    public void setAnimSpeed(int s){ this.speed = s; }

    public void setCountdownTexture(String texture){ this.countdownTexture = new ImageIcon(texture).getImage(); }

    public void setCoutndownTextureX(int x) { this.coutndownTextureX = x; }

    public void setCoutndownTextureY(int y) { this.coutndownTextureY = y; }

    public void setCountdown(boolean statement){ this.countdown = statement; }

    public void setPipesTimer(int pipesTimer) { this.pipesTimer = pipesTimer; }

    public void setScore1Number(int n) { score1Number = n; }

    public void setScore2Number(int n) { score2Number = n; }

    public void setScore1NumberTexture(int t){ score1NumberTexture = new ImageIcon("resources/" + t +".png").getImage(); }

    public void setScore2NumberTexture(int t){ score2NumberTexture = new ImageIcon("resources/" + t +".png").getImage(); }

    public int getMovement(){ return this.movement; }

    public int getAnimSpeed(){ return this.speed; }

    public int getCoutndownTextureX(){ return this.coutndownTextureX; }

    public int getCoutndownTextureY(){ return this.coutndownTextureY; }

    public int getPipesTimer() { return this.pipesTimer; }

    public int getScore() { return score; }

    public int getScore1Number(){ return score1Number; }

    public int getScore2Number(){ return score2Number; }

    public Image getScore1NumberTexture(){ return score1NumberTexture; }

    public Image getScore2NumberTexture(){ return score2NumberTexture; }




    public boolean isCountingDown() { return this.countdown; }

    public void cleanArrayLists(){
        pipes.clear();
        birdAnimList.clear();
        pipeAnimList.clear();
    }

    public void ensureCapacity(){
        birdAnimList.ensureCapacity(30);
        pipeAnimList.ensureCapacity(30);
    }

    public void setCollision(boolean statement){ this.collision = statement; }

    public void checkForCollision(){

             if(flappy.getPosY() + flappy.getHEIGHT() >= FRAME_HEIGHT-(GROUND_TEXTURE.getHeight(null))) setCollision(true);
             for(pipeColumn p : pipes){
                 if(        (flappy.getPosX() >= p.getPosX())
                         && (flappy.getPosX() <= p.getPosX() + p.getPIPE_WIDTH())
                         && (flappy.getPosY() <= p.getPosY() + p.getTopHeight()) ) setCollision(true);

                 if(        (flappy.getPosX() + flappy.getWIDTH() >= p.getPosX())
                         && (flappy.getPosX() + flappy.getWIDTH() <= p.getPosX() + p.getPIPE_WIDTH())
                         && (flappy.getPosY() <= p.getPosY() + p.getTopHeight()) ) setCollision(true);

                 if(        (flappy.getPosX() >= p.getPosX())
                         && (flappy.getPosX() <= p.getPosX() + p.getPIPE_WIDTH())
                         && (flappy.getPosY() + flappy.getHEIGHT()
                         >= p.getPosY() + p.getTopHeight() + p.getGapBetween()) ) setCollision(true);

                 if(        (flappy.getPosX() + flappy.getWIDTH() >= p.getPosX())
                         && (flappy.getPosX() + flappy.getWIDTH() <= p.getPosX() + p.getPIPE_WIDTH())
                         && (flappy.getPosY() + flappy.getHEIGHT()
                         >= p.getPosY() + p.getTopHeight() + p.getGapBetween()) ) setCollision(true);
             }
    }

    public void setBirdAtStartPosition(){
        flappy.setPosX(FRAME_WIDTH);
        flappy.setPosY(FRAME_HEIGHT/2);
    }

    public void setScore(){

        score=0;
        //loop to check how many pipes player has passed
        for(pipeColumn p : pipes){
            if(flappy.getPosX() > p.getPosX() + p.getPIPE_WIDTH()) score++;
        }

        //Setting score texture for future display
        this.setScoreTexture();
    }

    public void setScoreTexture(){

        //Taking separate numbers
        this.setScore1Number(getScore()/10);
        this.setScore2Number(getScore()%10);

        //Loop for setting number of a texture
        for(int i=0; i<10; i++){
            if(this.getScore1Number()==i) this.setScore1NumberTexture(i);
            if(this.getScore2Number()==i) this.setScore2NumberTexture(i);
        }

    }




    /**
     * KeyListener methods
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {

        if(keypressed == false && e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            //Setting bird in the middle of the screen
            setBirdAtStartPosition();

            //Removing pipes ArrayList, bird animation and pipes animation threads
            cleanArrayLists();

            /*
            Adding new bird to the Array list whenever game have to start &&
            Setting last made thread to Thread variable
            */
            birdAnimList.add(new Thread(new birdAnimationThread()));
            birdAnim = birdAnimList.get(birdAnimList.size()-1);

            /*
            Adding new bird to the Array list whenever game have to start &&
            Setting last made thread to Thread variable
            */
            pipeAnimList.add(new Thread(new pipesAnimationThread()));
            pipeAnim = pipeAnimList.get(pipeAnimList.size()-1);

            //Setting bird movement speed, animation speed, collision to false and score
            setMovement(0);
            setAnimSpeed(3);
            setCollision(false);
            setScore();

            //Starting new thread
            birdAnim.start();
            pipeAnim.start();
            keypressed = true;
        }
        else if( keypressed == true && e.getKeyCode() == KeyEvent.VK_SPACE) {
            setMovement(0);
            setAnimSpeed(3);
        }
    }




    /*****************************************************************************************************************
     * Inner classes
     * Class drawing is for printing everything on a frame
     */

    private class drawing extends JPanel{

        /**
         * Methods
         * paintComponent method - automatically prints everything on a screen
         */

        public void paintComponent(Graphics g){

            //Drawing a background
            g.drawImage(background, 0, 0, null);
            g.drawImage(background, FRAME_WIDTH, 0, null);

            //Drawing a flappy bird
            g.drawImage(flappy.getBirdTexture(),
                        flappy.getPosX(),
                        flappy.getPosY(),
                        null);

            //Drawing pipes
            for (pipeColumn p : pipes){
                g.drawImage(p.getTopPipeTexture(),
                            p.getPosX(),
                            p.getPosY(),
                            null);

                g.drawImage(p.getDownPipeTexture(),
                            p.getPosX(),
                         FRAME_HEIGHT - p.getDownHeight(),
                            null);
            }

            //Drawing score label, number, Label above the bird and Ground
            g.drawImage(new ImageIcon("resources/score.png").getImage(), 20, 20, null);
            g.drawImage(getScore1NumberTexture(), 32, 37,null);
            g.drawImage(getScore2NumberTexture(), 42, 37, null);
            g.drawImage(countdownTexture, getCoutndownTextureX(), getCoutndownTextureY(),null);
            g.drawImage(GROUND_TEXTURE, 0, FRAME_HEIGHT-(GROUND_TEXTURE.getHeight(null)),null);
            g.drawImage(GROUND_TEXTURE, GROUND_TEXTURE.getWidth(null), FRAME_HEIGHT-(GROUND_TEXTURE.getHeight(null)),null);
            g.drawImage(GROUND_TEXTURE, 2*GROUND_TEXTURE.getWidth(null), FRAME_HEIGHT-(GROUND_TEXTURE.getHeight(null)), null);
            g.drawImage(GROUND_TEXTURE, 3*GROUND_TEXTURE.getWidth(null), FRAME_HEIGHT-(GROUND_TEXTURE.getHeight(null)), null);
        }
    }

    /**
     * Class birdAnimationThread is for running thread responsible for animating the bird
     */

    private class birdAnimationThread implements Runnable{

        @Override
        public void run() {

            try {

                //Setting countdown to true for pipes to wait for drawing
                setCountdown(true);

                //Countdown loop
                for(int i=4; i>=1; i--){
                    //first display get ready
                    if(i == 4){
                        setCountdownTexture("resources/getReady.png");
                        setCoutndownTextureX(flappy.getPosX()-35);
                        setCoutndownTextureY(flappy.getPosY()-30);
                    }
                    //then display number 3 and 2 and 1
                    else if(i==3){
                        setCountdownTexture("resources/3.png");
                        setCoutndownTextureX(flappy.getPosX()+5);
                        setCoutndownTextureY(flappy.getPosY()-15);
                    }
                    else if(i==2) setCountdownTexture("resources/2.png");
                    else if(i==1) setCountdownTexture("resources/1.png");

                    repaint();
                    Thread.sleep(1000);
                }

                //Deleting numbers from window
                setCountdownTexture("resources/Blank.png");
                repaint();

                //Setting countdown to false for pipes to be drawn
                setCountdown(false);

                do
                {
                    //Movement up after hitting space else If a bird is at the top of a frame
                    if(     getMovement()<=50
                            && flappy.getPosY()>0) {

                        //Decreasig sppeed of lifting
                        if(getMovement() == 0)  flappy.setBirdTexture("resources/Flappy-wUp.png");
                        if(getMovement() == 10) flappy.setBirdTexture("resources/Flappy-wMid.png");
                        if(getMovement() == 30) flappy.setBirdTexture("resources/Flappy-wDown.png");
                        if(getMovement() == 50) flappy.setBirdTexture("resources/Flappy-wMid.png");

                        //Changing position of a bird
                        flappy.moveUp();

                    }
                    else{
                        flappy.setPosY(flappy.getPosY());
                        flappy.setPosX(flappy.getPosX());
                    }

                    //Growing up movement speed, Growing down movement speed and Movement down after 50 pixels up
                    if(     getMovement() % 10 == 0
                            && getMovement() <= 50 )     { setAnimSpeed(getAnimSpeed()+1); }

                    if(     getMovement() % 20 == 0
                            && getMovement() > 50
                            && getAnimSpeed() > 3 )          { setAnimSpeed(getAnimSpeed()-1); }
                    else if(getMovement()>50 )           { flappy.moveDown(); }

                    //Repainting, Stopping thread for certain time duration, Movement increase
                    repaint();
                    Thread.sleep(getAnimSpeed());
                    setMovement(getMovement()+1);


                    //Counting down to add new pipes
                    if(isCountingDown()==false) setPipesTimer(getPipesTimer()+getAnimSpeed());

                    //Adding new pipes if 2 seconds passed
                    if(getPipesTimer()>=2000){

                        //Setting pipes timer to wait another  2 seconds and Adding and starting new pipes thread
                        setPipesTimer(0);
                        pipes.add(new pipeColumn(2*FRAME_WIDTH));
                    }

                    //Checking for bird collision and Setting score
                    checkForCollision();
                    setScore();

                //Repeat while bird doesn't hit edges of the frame or pipes
                }while(collision==false);

                //Displaying Game Over text
                setCountdownTexture("resources/gameOver.png");
                setCoutndownTextureX(flappy.getPosX()-35);
                setCoutndownTextureY(flappy.getPosY()-30);
                repaint();

                //Setting keypressed to false and countdown to true
                Thread.sleep(1000);
                keypressed = false;
                setCountdown(true);
            }
            catch (InterruptedException e){System.out.println("EXCEPTION");}
        }
    }

    /**
     * Class pipesAnimationThread is for running thread responsible for animating pipes
     */

    private class pipesAnimationThread implements Runnable{


        @Override
        public void run() {

            do{
                try {

                    //Don't start animating pipes if countdow is enabled
                    if (isCountingDown()) Thread.sleep(1);
                    else {

                        //For every pipe column move them 1 pixel to the left
                        for (pipeColumn p : pipes) p.movePipe();

                        repaint();
                        Thread.sleep(10);
                    }
                }
                catch (InterruptedException e) {}

            }while(collision==false);
        }
    }
}