package ca.qc.johnabbott.cs603.asg3;

import ca.qc.johnabbott.cs603.R;
/*import ca.qc.johnabbott.cs603.asg3.shapes.Bezier;
import ca.qc.johnabbott.cs603.asg3.shapes.Ellipse;
import ca.qc.johnabbott.cs603.asg3.shapes.Line;
import ca.qc.johnabbott.cs603.asg3.shapes.PolyGon;
import ca.qc.johnabbott.cs603.asg3.shapes.PolyLine;
import ca.qc.johnabbott.cs603.asg3.shapes.Rectangle;
import ca.qc.johnabbott.cs603.asg3.shapes.Shape;*/
import ca.qc.johnabbott.cs603.asg3.shapes.*;
import ca.qc.johnabbott.cs603.asg3.tools.CurveTool;
import ca.qc.johnabbott.cs603.asg3.tools.EllipseTool;
import ca.qc.johnabbott.cs603.asg3.tools.LineTool;
import ca.qc.johnabbott.cs603.asg3.tools.PathTool;
import ca.qc.johnabbott.cs603.asg3.tools.RectangleTool;
import ca.qc.johnabbott.cs603.asg3.tools.ToolName;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ToolSettingsDialog extends Dialog {

	private ToolBox toolBox;
	private Paint previewPaint;
	
	/* Interface elements */
	private RadioButton radioRectangle;
	private RadioButton radioEllipse;
	private RadioButton radioLine;
	private RadioButton radioPath; 
	private RadioButton radioCurve;
	private SeekBar seekBarWidth;
	private ImageButton buttonStrokeColor;
	private ImageButton buttonFillColor;
	
	public ToolSettingsDialog(Context context, ToolBox tbox) {
		super(context);
		this.setContentView(R.layout.dialog_tools);
		this.setTitle(R.string.tools_dialog_title);
		this.setCanceledOnTouchOutside(true);

		this.toolBox = tbox;
		
		this.previewPaint = new Paint();
		this.previewPaint.setAntiAlias(true);
		this.previewPaint.setStrokeCap(Paint.Cap.ROUND);
		
		radioRectangle = (RadioButton) findViewById(R.id.radioRectangle);
		radioRectangle.setOnClickListener(new ToolClick(ToolName.RECTANGLE));
		
		radioEllipse = (RadioButton) findViewById(R.id.radioEllipse);
		radioEllipse.setOnClickListener(new ToolClick(ToolName.ELLIPSE));
		
		radioLine = (RadioButton) findViewById(R.id.radioLine);
		radioLine.setOnClickListener(new ToolClick(ToolName.LINE));
		
		radioCurve = (RadioButton) findViewById(R.id.radioCurve);
		radioCurve.setOnClickListener(new ToolClick(ToolName.CURVE));
		
		radioPath = (RadioButton) findViewById(R.id.radioPath);
		radioPath.setOnClickListener(new ToolClick(ToolName.PATH));
		
		switch(toolBox.getCurrentToolName()) {
		case RECTANGLE:
			radioRectangle.setChecked(true); break;
		case LINE:
			radioLine.setChecked(true); break;
		case ELLIPSE:
			radioEllipse.setChecked(true); break;
		case CURVE:
			radioCurve.setChecked(true); break;
		case PATH:
			radioPath.setChecked(true); break;
		default:
			radioLine.setChecked(true);
		}
		
		seekBarWidth = (SeekBar)this.findViewById(R.id.widthSeekBar);
		seekBarWidth.setProgress(toolBox.getStrokeWidth());
		seekBarWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
				toolBox.setStrokeWidth(progress);
				updatePreview();
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {}			
		});
				
		buttonStrokeColor = (ImageButton) findViewById(R.id.btnFG);
		buttonStrokeColor.setBackgroundColor(toolBox.getStrokeColor());
		buttonStrokeColor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				final ColorChooserDialog dialog = new ColorChooserDialog(getContext(), toolBox.getStrokeColor());
				dialog.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						toolBox.setStrokeColor(dialog.getColor());
						buttonStrokeColor.setBackgroundColor(dialog.getColor());
						updatePreview();
					}
				});
				dialog.show();
			
			}
		});
		
		
		buttonFillColor = (ImageButton) findViewById(R.id.btnBG);
		buttonFillColor.setBackgroundColor(toolBox.getFillColor());
		
		buttonFillColor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				final ColorChooserDialog dialog = new ColorChooserDialog(getContext(), toolBox.getFillColor());
				dialog.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						toolBox.setFillColor(dialog.getColor());
						buttonFillColor.setBackgroundColor(dialog.getColor());
						updatePreview();
					}
				});
				dialog.show();		
			}
		});
		
		Button done = (Button)this.findViewById(R.id.widthDone);
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	
		updatePreview();
		this.show();
	}

	private void updatePreview() {
		
		// use the toolBox to update the preview.
		ImageView image = (ImageView)findViewById(R.id.widthImageView);
			
		Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(Color.WHITE);
		
		Shape s = null;
		//TODO draw preview shape for current tool (in 400x400 canvas)
		final float size = 400;
		switch (toolBox.getCurrentToolName()) {
		case LINE:
			s = new Line(40, 80, size-40, size-80, toolBox.getStrokeColor(), toolBox.getStrokeWidth());
			break;
		case RECTANGLE:
			s = new Rectangle(80, 40, size-80, size-40, toolBox.getStrokeColor(), toolBox.getStrokeWidth(), toolBox.getFillColor());
			break;
		case ELLIPSE:
			s = new Ellipse(size/2, size/2, size/2-30, size/2-70, toolBox.getStrokeColor(), toolBox.getStrokeWidth(), toolBox.getFillColor());
			break;
		case PATH:
			Path path = new Path(size/8, size/8);
			path.addPoint(size/8, size/8*3);
			path.addPoint(size/2, size/2);
			path.addPoint(size/8*7, size/2);
			path.addPoint(size/8*7, size/8*7);
			path.setStrokeColor(toolBox.getStrokeColor());
			path.setStrokeWidth(toolBox.getStrokeWidth());
			path.setFillColor(toolBox.getFillColor());
			
			s = path;
			break;
		case CURVE:
			Curve curve = new Curve(40, 80);
			curve.updateLine(size-40, size-80);
			curve.updateCurve(size/8, size-size/8);
			curve.setStrokeColor(toolBox.getStrokeColor());
			curve.setStrokeWidth(toolBox.getStrokeWidth());
			curve.setFillColor(toolBox.getFillColor());
			
			s = curve;
			break;
		case NONE:
			break;
		}
		
		if(s != null)
			s.draw(previewPaint, canvas);		
		image.setImageBitmap(bitmap);
	}
	
	private class ToolClick implements View.OnClickListener {

		private ToolName name;
	
		public ToolClick(ToolName name) {
			this.name = name;
		}

		@Override
		public void onClick(View view) {
			toolBox.changeTool(name);
			updatePreview();
		}
		
	}
	
	
}
