package eu.personalfactory.cloudfab.macchina.utility;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.SCROLL_TOLLERANZA_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.SCROLL_TOLLERANZA_Y;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GestoreScrollLista implements MouseMotionListener {

    private int dY;
    private int dX;
    private int yPrec = 0;
    private int xPrec = 0;
    private boolean isYDraggedUP;
    private boolean isYDraggedDOWN;
    private boolean isXDraggedLEFT;
    private boolean isXDraggedRIGHT;
    private int X_RIF;
    private int Y_RIF;

    //COSTRUTTORE
    public GestoreScrollLista() {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {

        if (arg0.getY() - Y_RIF >= SCROLL_TOLLERANZA_Y
                | Y_RIF - arg0.getY() >= SCROLL_TOLLERANZA_Y) {
            Y_RIF = arg0.getY();
            X_RIF = arg0.getX();
            if (arg0.getY() - yPrec >= 0) {
                yPrec = arg0.getY();
                setDx(0);
                dY++;
                 
                isYDraggedUP = true;
                isYDraggedDOWN = false;
            } else {
                setDx(0); 
                yPrec = arg0.getY();
                isYDraggedDOWN = true;
                isYDraggedUP = false;
                dY++;
            }
        }
        if (arg0.getX() - X_RIF >= SCROLL_TOLLERANZA_X
                | X_RIF - arg0.getX() >= SCROLL_TOLLERANZA_X) {
            X_RIF = arg0.getX();
            Y_RIF = arg0.getY();
            if (arg0.getX() - xPrec >= 0) {

                xPrec = arg0.getX();
                dX++;
                isXDraggedLEFT = true;
                isXDraggedRIGHT = false;

            } else {
                xPrec = arg0.getX();
                dX++;
                isXDraggedRIGHT = true;
                isXDraggedLEFT = false;

            }
        }



    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        reset();
    }

    public void reset() {
        isXDraggedLEFT = false;
        isXDraggedRIGHT = false;
        isYDraggedUP = false;
        isYDraggedDOWN = false;
    }

    public boolean isYDraggedUP() {
        return isYDraggedUP;
    }

    public boolean isYDraggedDW() {
        return isYDraggedDOWN;
    }

    public boolean isXDraggedLF() {
        return isXDraggedLEFT;
    }

    public boolean isXDraggedRG() {
        return isXDraggedRIGHT;
    }

    public int getDy() {
        return dY;
    }

    public int getDx() {
        return dX;
    }

    public void setDy(int i) {
        dY = i;
    }

    public void setDx(int i) {
        dX = i;
    }

    public void setXYRif(int x, int y) {
        X_RIF = x;
        Y_RIF = y;
    }
}