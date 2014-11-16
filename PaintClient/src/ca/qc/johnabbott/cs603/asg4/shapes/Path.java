package ca.qc.johnabbott.cs603.asg4.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Path extends Shape {
	
	SerializablePath path;

	public Path(float startX, float startY) {
		super(0,0,0); // Don't set the colors now (path shape is initialized and kept before drawing it according to certain color)
		this.type = Shape.ShapeType.Path;
		path = new SerializablePath();
		path.setLastPoint(startX,  startY);
		//path.setFillType(android.graphics.Path.FillType.EVEN_ODD);
	}

	public void addPoint(float x, float y) {
		path.lineTo(x, y);
	}
	
	@Override
	public void draw(Paint paint, Canvas canvas) {
		// reset any path effect
		//paint.setPathEffect(null);
		
		// Draw Fill
		if(fillColor != Color.TRANSPARENT) {
			paint.setColor(fillColor);
			paint.setStyle(Style.FILL);
			canvas.drawPath(path, paint);
		}
		// Draw Stroke
		if(/*strokeColor != Color.TRANSPARENT &&*/ strokeWidth > 0) {
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(strokeColor);
			paint.setStrokeWidth(strokeWidth);
			paint.setStrokeCap(Paint.Cap.ROUND);
			canvas.drawPath(path, paint);
		}
	}
	
	public static double distance(float x1, float y1, float x2, float y2) {
		return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}

}
