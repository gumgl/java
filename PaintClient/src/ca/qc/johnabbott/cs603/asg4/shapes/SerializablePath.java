package ca.qc.johnabbott.cs603.asg4.shapes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import ca.qc.johnabbott.cs603.asg4.shapes.SerializablePath.PathAction.PathActionType;

import android.graphics.Path;

public class SerializablePath extends Path implements Serializable {

	private ArrayList<PathAction> actions = new ArrayList<SerializablePath.PathAction>();

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
	    in.defaultReadObject();
	    drawThisPath();
	}

	@Override
	public void moveTo(float x, float y) {
	    actions.add(new ActionMove(x, y));
	    super.moveTo(x, y);
	}

	@Override
	public void lineTo(float x, float y){
	    actions.add(new ActionLine(x, y));
	    super.lineTo(x, y);
	}
	@Override
	public void setLastPoint(float x, float y){
	    actions.add(new ActionSetLastPoint(x, y));
	    super.setLastPoint(x, y);
	}
	@Override
	public void quadTo(float a, float b, float c, float d){
	    actions.add(new ActionQuad(a, b, c, d));
	    super.quadTo(a, b, c, d);
	}

	private void drawThisPath(){
	    for(PathAction p : actions){
	        if(p.getType().equals(PathActionType.MOVE_TO)){
	        	ActionMove a = (ActionMove)p;
	            super.moveTo(a.getX(), a.getY());
	        } else if(p.getType().equals(PathActionType.LINE_TO)){
	        	ActionLine a = (ActionLine)p;
	            super.lineTo(a.getX(), a.getY());
	        } else if(p.getType().equals(PathActionType.QUAD_TO)){
	        	ActionQuad a = (ActionQuad)p;
	            super.quadTo(a.getA(), a.getB(), a.getC(), a.getD());
	        } else if(p.getType().equals(PathActionType.SET_LAST_POINT)){
	        	ActionSetLastPoint a = (ActionSetLastPoint)p;
	            super.setLastPoint(a.getX(), a.getY());
	        }
	    }
	}
	
public interface PathAction {
    public enum PathActionType {LINE_TO,MOVE_TO,QUAD_TO,SET_LAST_POINT};
    public PathActionType getType();
}

public class ActionMove implements PathAction, Serializable{
    private static final long serialVersionUID = -7198142191254133295L;

    private float x,y;

    public ActionMove(float x, float y){
        this.x = x;
        this.y = y;
    }

    public PathActionType getType() {
        return PathActionType.MOVE_TO;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}

public class ActionLine implements PathAction, Serializable{
    private static final long serialVersionUID = 8307137961494172589L;

    private float x,y;

    public ActionLine(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public PathActionType getType() {
        return PathActionType.LINE_TO;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
public class ActionSetLastPoint implements PathAction, Serializable{
    private static final long serialVersionUID = 8307137961494172589L;

    private float x,y;

    public ActionSetLastPoint(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public PathActionType getType() {
        return PathActionType.SET_LAST_POINT;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
public class ActionQuad implements PathAction, Serializable{
    private static final long serialVersionUID = 8307137961494172589L;

    private float a, b, c, d;

    public ActionQuad(float a, float b, float c, float d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public PathActionType getType() {
        return PathActionType.QUAD_TO;
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public float getC() {
        return c;
    }

    public float getD() {
        return d;
    }
}
@Override
public void reset() {
	super.reset();
	actions.clear();
}

}
