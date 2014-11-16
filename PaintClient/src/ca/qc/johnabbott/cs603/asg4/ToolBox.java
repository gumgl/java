package ca.qc.johnabbott.cs603.asg4;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import ca.qc.johnabbott.cs603.asg4.tools.*;

public class ToolBox {
	private int strokeWidth;
	private int strokeColor;
	private int fillColor;
	private DrawingView drawingView;
	private Paint previewPaint;
	private Tool currentTool;

	public ToolBox() {
		previewPaint = new Paint();
		previewPaint.setStyle(Paint.Style.STROKE);
		previewPaint.setColor(Color.GRAY);
		previewPaint.setStrokeWidth(1);
		previewPaint.setStrokeCap(Paint.Cap.ROUND);
		previewPaint.setPathEffect(new DashPathEffect(new float[]{4.0f, 4.0f}, 1.0f));
		
		fillColor = (int)Color.BLUE;
		strokeColor = (int)Color.BLACK;
		strokeWidth = 3;
		// DEBUG
		currentTool = new LineTool(this, ToolName.LINE);
	}
	
	public int getStrokeWidth() {
		return strokeWidth;
	}
	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	public int getStrokeColor() {
		return strokeColor;
	}
	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}
	public int getFillColor() {
		return fillColor;
	}
	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
	}
	public DrawingView getDrawingView() {
		return drawingView;
	}
	public void setDrawingView(DrawingView drawingView) {
		this.drawingView = drawingView;
	}
	public Paint getPaintPreview() {
		return previewPaint;
	}
	public Tool getCurrentTool() {
		return currentTool;
	}
	public ToolName getCurrentToolName() {
		return currentTool.getName();
	}
	public void changeTool(ToolName name) {
		switch (name) {
		case LINE:
			currentTool = new LineTool(this, ToolName.LINE);
			break;
		case RECTANGLE:
			currentTool = new RectangleTool(this, ToolName.RECTANGLE);
			break;
		case ELLIPSE:
			currentTool = new EllipseTool(this, ToolName.ELLIPSE);
			break;
		case PATH:
			currentTool = new PathTool(this, ToolName.PATH);
			break;
		case CURVE:
			currentTool = new CurveTool(this, ToolName.CURVE);
			break;
		case NONE:
			currentTool = null;
			break;
		}
	}
}
