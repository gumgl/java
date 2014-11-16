package ca.qc.johnabbott.cs603.asg4.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Curve extends Shape {

	SerializablePath path;
	protected float x1, y1, x2, y2, xc, yc;
	
	public Curve(float startX, float startY) {
		super(0,0,0); // Don't set the colors now (see shapes.Path)
		this.type = Shape.ShapeType.Curve;
		this.path = new SerializablePath();
		this.x1 = startX;
		this.y1 = startY;
	}
	
	public void updateLine(float x, float y) {
		path.reset(); // Clear previous line
		x2 = x;
		y2 = y;
		path.setLastPoint(x1, y1); // Start from pt 1
		path.lineTo(x2, y2); // Line to pt 2
	}
	
	public void updateCurve(float x, float y) {
		path.reset(); // Clear previous curve
		xc = x;
		yc = y;
		path.setLastPoint(x1, y1); // Start from pt 1
		path.quadTo(xc, yc, x2, y2); // Curve to pt 2 passing by pt c
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

}
