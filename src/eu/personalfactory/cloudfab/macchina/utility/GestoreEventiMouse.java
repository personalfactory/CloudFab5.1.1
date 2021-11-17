package eu.personalfactory.cloudfab.macchina.utility;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GestoreEventiMouse implements MouseListener
{

	private boolean mousePressed;
	
	public GestoreEventiMouse()
	{
		super(); 
		mousePressed=false;
		
	}
	@Override
	public void mouseClicked(MouseEvent e)  {  }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) { mousePressed=true; }

	@Override
	public void mouseReleased(MouseEvent e) { mousePressed=false; }

	public boolean isPressed(){ return mousePressed; }

		
}
