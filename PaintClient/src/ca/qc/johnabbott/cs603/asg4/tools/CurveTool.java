package ca.qc.johnabbott.cs603.asg4.tools;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import ca.qc.johnabbott.cs603.asg4.ToolBox;
import ca.qc.johnabbott.cs603.asg4.shapes.Curve;
import ca.qc.johnabbott.cs603.asg4.shapes.Path;

public class CurveTool extends Tool {
	
	protected Curve curve;
	protected boolean curving = false;
	
	public CurveTool(ToolBox toolbox, ToolName name) {
		super(toolbox, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void touchStart(MotionEvent event) {
		/*DEBUG*/System.out.println("CurveTool::touchStart");
		float x = event.getX();
		float y = event.getY();
		
		if (curving) { // Curving the line
			curve.updateCurve(x, y);
		} else { // New Curve
			curve = new Curve(x, y);
			curve.updateLine(x, y);
		    preview = true; // started the line, start preview
		}
	}

	@Override
	public void touchEnd(MotionEvent event) {
		/*DEBUG*/System.out.println("CurveTool::touchStart");
		float x = event.getX();
		float y = event.getY();
		if (curving) { // Curving the line
			curve.updateCurve(x, y); // final touch
			curving = false;
			preview = false; // finished curving, end preview
		    addToDrawing();
		} else { // The Line
			curving = true; // finished the line, start curving
		}
	}

	@Override
	public void touchMove(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		if (curving) { // Curving the line
			curve.updateCurve(x, y);
		} else { // New Curve
			curve.updateLine(x, y);
		}
	}

	@Override
	public void drawPreview(Canvas canvas) {
		Paint paint = toolBox.getPaintPreview(); // The paint to draw preview of shape
		curve.setStrokeColor(paint.getColor());
		curve.setStrokeWidth((int)paint.getStrokeWidth());
		curve.setFillColor(Color.TRANSPARENT);
		
		curve.draw(paint, canvas);
	}

	@Override
	public void addToDrawing() {
		// Get colors from toolBox:
		curve.setStrokeColor(toolBox.getStrokeColor());
		curve.setStrokeWidth(toolBox.getStrokeWidth());
		curve.setFillColor(toolBox.getFillColor());
		
		toolBox.getDrawingView().addShape(curve);
	}

}
