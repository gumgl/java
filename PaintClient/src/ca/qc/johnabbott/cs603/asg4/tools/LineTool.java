package ca.qc.johnabbott.cs603.asg4.tools;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import ca.qc.johnabbott.cs603.asg4.ToolBox;
import ca.qc.johnabbott.cs603.asg4.shapes.Line;
import ca.qc.johnabbott.cs603.asg4.shapes.Shape;

public class LineTool extends RectangleBaseTool {
	

	public LineTool(ToolBox toolbox, ToolName name) {
		super(toolbox, name);
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public void drawPreview(Canvas canvas) {
		/*DEBUG*/System.out.println("LineTool drawPreview");
		Paint paint = toolBox.getPaintPreview(); // The paint to draw preview of shape
		Shape shape = new Line(x1, y1, x2, y2, paint.getColor(), (int)paint.getStrokeWidth()); // Build shape
		shape.draw(paint, canvas); // Draw it
	}
	
	@Override
	public void addToDrawing() {
		/*DEBUG*/System.out.println("LineTool addToDrawing");
		Paint paint = toolBox.getDrawingView().getPaint(); // The paint to draw the shape
		Shape shape = new Line(x1, y1, x2, y2, toolBox.getStrokeColor(), toolBox.getStrokeWidth()); // Build shape
		toolBox.getDrawingView().addShape(shape); // Add it on the shapes array
	}

}
