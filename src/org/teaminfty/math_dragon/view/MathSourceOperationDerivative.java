package org.teaminfty.math_dragon.view;

import org.teaminfty.math_dragon.view.math.MathObject;
import org.teaminfty.math_dragon.view.math.MathObjectEmpty;
import org.teaminfty.math_dragon.view.math.MathOperationDerivative;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class MathSourceOperationDerivative extends MathSourceObject
{
	/** The paint we use to draw the operator */
    private Paint paintOperator = new Paint();
    
    @Override
    public MathObject createMathObject()
    { return new MathOperationDerivative(); }

    @Override
    public void draw(Canvas canvas, int w, int h)
    {
    	// Set the size of the "d"
    	paintOperator.setTextSize( (h - 3 * MathObject.lineWidth) / 2 );
    	
        // Get a boxes that fit twice in the given height (we'll use it to draw the empty boxes)
        Rect emptyBox = getRectBoundingBox(w, (int) (h - 3 * MathObject.lineWidth) / 2, MathObjectEmpty.RATIO);
        Rect textBox = new Rect();
        paintOperator.getTextBounds("d", 0, "d".length(), textBox);
        
        // Draw the boxes
        emptyBox.offsetTo((w - textBox.width() / 5) / 2, (int) (h - 2 * emptyBox.height() - 3 * MathObject.lineWidth) / 2);
        drawEmptyBox(canvas, emptyBox);
        
        emptyBox = getRectBoundingBox(w, (int) (h - 3 * MathObject.lineWidth) / 2, MathObjectEmpty.RATIO);
        emptyBox.offset((w - textBox.width() / 5) / 2, (int) (emptyBox.height() + 3 * MathObject.lineWidth));
        drawEmptyBox(canvas, emptyBox);
        
        // Draw the text
        canvas.drawText("d", (w - emptyBox.width()) / 2 - textBox.width(), emptyBox.height() * 2 + MathObject.lineWidth, paintOperator);
        canvas.drawText("d", (w - emptyBox.width()) / 2 - textBox.width(), emptyBox.height(), paintOperator);
        
        // Draw the operator
        paintOperator.setStrokeWidth(MathObject.lineWidth);
        canvas.drawLine(emptyBox.left - textBox.width() * 1.5f, h / 2, emptyBox.right, h / 2, paintOperator);
    }
}
