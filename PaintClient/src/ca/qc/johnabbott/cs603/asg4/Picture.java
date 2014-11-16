package ca.qc.johnabbott.cs603.asg4;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import ca.qc.johnabbott.cs603.asg4.shapes.*;

public class Picture implements Serializable {
	protected transient ArrayList<Shape> shapes;

	public Picture() {
		shapes = new ArrayList<Shape>();
	}
	
	public void addShape(Shape shape) {
		shapes.add(shape);
	}
	
	public void erase() {
		shapes.clear();
	}
	
	private void writeObject(ObjectOutputStream oos) {
		try {
			System.out.println("Writing static variable manually...");
			oos.defaultWriteObject();
			oos.writeInt(shapes.size());
			for (Shape shape : shapes) {
				oos.writeObject(shape.getType().name());
				System.out.println(shape.getType().name());
				oos.writeObject(shape);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	private void readObject(ObjectInputStream ois) {
		try {
			System.out.println("Reading static variable manually...");
			ois.defaultReadObject();
			shapes = new ArrayList<Shape>();
			int size = ois.readInt();
			for (int i=0; i<size; i++) {
				String typeString = (String)ois.readObject();
				Shape.ShapeType type = Shape.ShapeType.valueOf(typeString);
				Shape shape;
				switch(type) {
				case Line:
					shape = (Line)ois.readObject();
					break;
				case Rectangle:
					shape = (Rectangle)ois.readObject();
					break;
				case Ellipse:
					shape = (Ellipse)ois.readObject();
					break;
				case Path:
					shape = (Path)ois.readObject();
					break;
				case Curve:
					shape = (Curve)ois.readObject();
					break;
				default:
					shape = (Shape)ois.readObject();
				}
				shapes.add(shape);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
	} 
}
