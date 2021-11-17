package eu.personalfactory.cloudfab.macchina.utility;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

public class GestoreEventiMouseLista implements MouseListener
{

	public JLabel lab;
	private boolean mouseReleased;
	private boolean mousePressed;
	private int X_POS;
        private int Y_POS;
	public GestoreEventiMouseLista()
	{
		super(); 
		mouseReleased=false; 
		
	}
	@Override
	public void mouseClicked(MouseEvent e)  { }
	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { 
		
                setX_POS(e.getX());
                setY_POS(e.getY());
		mouseReleased=false; 
		mousePressed=true;  
		lab = (JLabel) e.getSource();  
	}

	@Override
	public void mouseReleased(MouseEvent e) { 
		
		mouseReleased=true; 
		mousePressed=false;  
	}

	public boolean isReleased(){ return mouseReleased;} 
	public boolean isPressed(){ return mousePressed;} 
 
	public void Reset(){ mouseReleased=false; mousePressed=false; }
	public JLabel getSource(){ return lab;} 
         
        public int getX_POS() {
        return X_POS;
    }

    public void setX_POS(int X_POS) {
        this.X_POS = X_POS;
    }

    public int getY_POS() {
        return Y_POS;
    }

    public void setY_POS(int Y_POS) {
        this.Y_POS = Y_POS;
    }
}
