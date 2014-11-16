package ca.qc.johnabbott.cs603.asg4.tools;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import ca.qc.johnabbott.cs603.asg4.ToolBox;
import ca.qc.johnabbott.cs603.asg4.shapes.Line;
import ca.qc.johnabbott.cs603.asg4.shapes.Shape;

public abstract class RectangleBaseTool extends Tool {
	
	protected float x1, y1, x2, y2;

	public RectangleBaseTool(ToolBox toolbox, ToolName name) {
		super(toolbox, name);
		// TODO Auto-generated constructor stub
		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;
	}

	@Override
	public void touchStart(MotionEvent event) {
		// TODO Auto-generated method stub
	    /*DEBUG*/System.out.println("RectangleBaseTool touchStart");
		x1 = event.getX();
	    y1 = event.getY();
		x2 = event.getX();
	    y2 = event.getY();
	    preview = true;
	}

	@Override
	public void touchEnd(MotionEvent event) {
		// TODO Auto-generated method stub
	    /*DEBUG*/System.out.println("RectangleBaseTool touchEnd");
		x2 = event.getX();
	    y2 = event.getY();
	    preview = false;
	    addToDrawing();
	}

	@Override
	public void touchMove(MotionEvent event) {
		// TODO Auto-generated method stub
		x2 = event.getX();
	    y2 = event.getY();
	}

}
