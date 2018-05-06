/**
 * Created by mateuszstolowski on 27.05.2017.
 */

import javax.swing.*;
import java.awt.*;

public class pipeColumn {


    /**
     * Parameters
     */
    private Image downPipeTexture = new ImageIcon("resources/downPipe - 192px.png").getImage();
    private Image topPipeTexture = new ImageIcon("resources/upPipe - 192px.png").getImage();

    private final int PIPE_WIDTH = 26;
    private final int topHeight = topPipeTexture.getHeight(null);


    private  int downHeight = downPipeTexture.getHeight(null);
    private  int gapBetween;
    private int posX;
    private int posY;


    /**
     * Constructors
     */

    public pipeColumn(int x){
        this.setposX(x);
        this.makeAGap();
    }


    /**
     * Methods
     */

    public void setposX(int x){ this.posX = x; }

    public void setPosY(int y) { this.posY = y; }

    public void setDownHeight(int h) { this.downHeight = h; }

    public void setGapBetween(int up, int down) { gapBetween = up+down; }

    public Image getDownPipeTexture() {
        return downPipeTexture;
    }

    public Image getTopPipeTexture(){
        return topPipeTexture;
    }

    public int getPIPE_WIDTH(){
        return PIPE_WIDTH;
    }

    public int getTopHeight() { return topHeight; }

    public int getDownHeight() {
        return downHeight;
    }

    public int getPosX(){ return posX; }

    public int getPosY(){ return posY; }

    public int getGapBetween() { return gapBetween; }

    public void movePipe(){ this.setposX(getPosX()-1); }

    public void makeAGap(){
        int upStop = 0;
        int downStop = 0;

        int upOrDown = (int) (Math.random() * 2) +1 ;
        if(upOrDown == 1) {
            upStop = (int) (Math.random() * 90) + 60;
            if(upStop>=100) downStop = 0 - upStop/3;
            else downStop = 0 - upStop/5;
        }
        else{
            upStop = 0;
            downStop = (int) (Math.random() * 60) + 45;
        }
        this.setPosY(0-upStop);
        this.setDownHeight(getDownHeight()-downStop);
        this.setGapBetween(upStop, downStop );

    }
}
