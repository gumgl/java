package ca.qc.johnabbott.cs603.asg4.shapes;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Shape implements java.io.Serializable {
	public enum ShapeType {Line, Rectangle, Ellipse, Path, Curve};

	protected int strokeColor;
	protected int fillColor;
	protected int strokeWidth;
	protected ShapeType type;
	
	private static final long serialVersionUID = 0L;

	public Shape(int strokeColor, int fillColor, int lineWidth) {
		super();
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
		this.strokeWidth = lineWidth;
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

	public int getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	
	public ShapeType getType() {
		return type;
	}

	public abstract void draw(Paint paint, Canvas canvas);
	
	/*private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		// write 'this' to 'out'...
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		// populate the fields of 'this' from the data in 'in'...
	}*/

}
