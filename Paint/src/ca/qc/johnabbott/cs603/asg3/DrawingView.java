package ca.qc.johnabbott.cs603.asg3;

import java.util.ArrayList;

import ca.qc.johnabbott.cs603.asg3.shapes.Shape;
import ca.qc.johnabbott.cs603.asg3.tools.ToolName;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
	
	private Paint paint;
	ToolBox toolBox;
	ArrayList<Shape> shapes;
	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setAntiAlias(true);
		toolBox = new ToolBox();
		toolBox.setDrawingView(this);
		shapes = new ArrayList<Shape>();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//TODO
		/*DEBUG*/System.out.println("DrawingView onDraw");
		for(Shape shape : shapes) { // Draw all shapes
			/*DEBUG*/System.out.println("DrawingView::onDraw drawing shape");
			shape.draw(paint, canvas);
		}
		
		if (toolBox.getCurrentTool().hasPreview()) {
			/*DEBUG*/System.out.println("DrawingView::OnDraw drawing preview");
			toolBox.getCurrentTool().drawPreview(canvas);
		}
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			//TODO
			/*DEBUG*/System.out.println("DrawingView begin touch");
			toolBox.getCurrentTool().touchStart(event);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			//TODO
			/*DEBUG*/System.out.println("DrawingView end touch");
			toolBox.getCurrentTool().touchEnd(event);
			break;
		default:
			//TODO
			/*DEBUG*/System.out.println("DrawingView move touch");
			toolBox.getCurrentTool().touchMove(event);
		}
		invalidate();
		return true;
	}
	
	public void erase() {
		shapes.clear();
	}
	
	public ToolBox getToolBox() {
		return toolBox;
	}
	
	public Paint getPaint() {
		return paint;
	}
	
	public void addShape(Shape shape) {
		/*DEBUG*/System.out.println("DrawingView addShape");
		shapes.add(shape);
	}
	
}
