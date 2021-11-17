package eu.personalfactory.cloudfab.macchina.utility;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GestoreMovimentoMouse implements MouseMotionListener 
{
	private boolean mouseMoved;
	private boolean mouseDragged;
	private Point mousePosition;
	
	public GestoreMovimentoMouse()
	{
		super();
		
		mousePosition=new Point();
		mouseMoved=false;
		mouseDragged=false;
	}
	public void mouseMoved(MouseEvent e)
	{  
		
		mousePosition.setLocation(e.getPoint());
		
		mouseMoved=true;
		mouseDragged=false;
	}
	@Override
	public void mouseDragged(MouseEvent arg0) 
	{	
		mousePosition.setLocation(arg0.getLocationOnScreen());
		
		mouseMoved=false;
		mouseDragged=true;
	}
	
	public boolean isMoved(){ return mouseMoved; }
	public boolean isDragged(){ return mouseDragged; }
	
	public Point getMouseLocationOnScreen(){ return mousePosition; }
}
