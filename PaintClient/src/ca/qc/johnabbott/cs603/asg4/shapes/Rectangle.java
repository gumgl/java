package ca.qc.johnabbott.cs603.asg4.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Rectangle extends Shape {

	private float x1, y1, x2, y2;
	
	public Rectangle(float x1, float y1, float x2, float y2, int strokeColor, int width) {
		this(x1, y1, x2, y2, strokeColor, width, Color.TRANSPARENT);
	}
	
	public Rectangle(float x1, float y1, float x2, float y2, int strokeColor, int strokeWidth, int fillColor) {
		super(strokeColor, fillColor, strokeWidth);
		this.type = Shape.ShapeType.Rectangle;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public void draw(Paint paint, Canvas canvas) {
		
		// reset any path effect
		//paint.setPathEffect(null);
		
		// Draw Fill
		if(fillColor != Color.TRANSPARENT) {
			paint.setColor(fillColor);
			paint.setStyle(Style.FILL);
			canvas.drawRect(x1, y1, x2, y2, paint);
		}
		// Draw Stroke
		if(/*strokeColor != Color.TRANSPARENT &&*/ strokeWidth > 0) {
			paint.setStyle(Style.STROKE);
			paint.setColor(strokeColor);
			paint.setStrokeWidth(strokeWidth);
			paint.setStrokeCap(Paint.Cap.ROUND);
			canvas.drawRect(x1, y1, x2, y2, paint);
		}
		
	}

}
