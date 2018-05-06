import javax.swing.*;
import java.awt.*;


/**
 * Created by mateuszstolowski on 21.05.2017.
 */
public class bird {

    /**
     * Class parameters
     *
     * @param WIDTH         - holds width of a bird
     * @param HEIGHT        - hold height of a bird
     * @param birdTexture   - holds the texture for a bird
     * @param positionX     - hold actual X position of a bird
     * @param positionY     - hold actual Y position of a bird
     */

    private final int WIDTH = 17;
    private final int HEIGHT = 12;
    private Image birdTexture = new ImageIcon("resources/Flappy-wMid.png").getImage();


    private int positionX;
    private int positionY;


    /**
     * Constructor
     */

    public bird()
    {
        positionY = 0;
        positionX = 0;
    }

    /**
     * Methods
     */

    public int getPosX(){ return positionX; }

    public int getPosY(){ return positionY; }

    public int getWIDTH(){ return WIDTH; }

    public int getHEIGHT(){ return HEIGHT; }

    public Image getBirdTexture() { return birdTexture; }

    public void setPosX(int x){ positionX = x; }

    public void setPosY(int y){ positionY = y; }

    public void setBirdTexture(String texture){ birdTexture = new ImageIcon(texture).getImage(); }

    public void moveUp(){ this.setPosY(getPosY()-1); }

    public void moveDown(){ this.setPosY(getPosY()+1); }

}
