package ca.qc.johnabbott.cs603.asg3.tools;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import ca.qc.johnabbott.cs603.asg3.ToolBox;
import ca.qc.johnabbott.cs603.asg3.shapes.Path;

public class PathTool extends Tool {

	protected final float DIST = 50; // distance from last point when new point is added
	protected float lastX, lastY;
	protected Path path;
	
	public PathTool(ToolBox toolbox, ToolName name) {
		super(toolbox, name);
		lastX = 0;
		lastY = 0;
	}

	@Override
	public void touchStart(MotionEvent event) {
	    /*DEBUG*/System.out.println("PathTool::touchStart");
		lastX = event.getX();
		lastY = event.getY();
	    path = new Path(lastX, lastY);
	    preview = true;
	}

	@Override
	public void touchEnd(MotionEvent event) {
	    preview = false;
	    path.addPoint(event.getX(), event.getY());
	    addToDrawing();
	}

	@Override
	public void touchMove(MotionEvent event) {
		// TODO Auto-generated method stub
		float newX = event.getX();
		float newY = event.getY();
		if (Path.distance(lastX, lastY, newX, newY) > DIST) { // if distance > 30
			path.addPoint(newX, newY);
			lastX = newX;
			lastY = newY;
		}
	}

	@Override
	public void drawPreview(Canvas canvas) {
		Paint paint = toolBox.getPaintPreview(); // The paint to draw preview of shape
		path.setStrokeColor(paint.getColor());
		path.setStrokeWidth((int)paint.getStrokeWidth());
		path.setFillColor(Color.TRANSPARENT);
		
		path.draw(paint, canvas);
	}

	@Override
	public void addToDrawing() {
		// Get colors from toolBox:
		path.setStrokeColor(toolBox.getStrokeColor());
		path.setStrokeWidth(toolBox.getStrokeWidth());
		path.setFillColor(toolBox.getFillColor());
		
		toolBox.getDrawingView().addShape(path);
	}

}
