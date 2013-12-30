package org.teaminfty.math_dragon.view.math;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


public class MathVariable extends MathObject
{
	/** The XML element name */
	public static final String NAME = "variable";
	/** The name XML element attribute */
	public static final String ATTR_NAME = "name";
	
    /** The name of the variable */
    private String c;
    
    /** The paint that is used to draw the variable */
    private final Paint paint = new Paint();

    /** Default constructor */
    public MathVariable()
    {
        c = "x";
    }

    /** Constructor
     * @param c The letter this MathVariable represents.  (Is a string so we can later support multiple-letter variables)
     */
    public MathVariable(String c)
    {
        this.c = c;
    }

    /** Calculates the right text size for the given level
     * @param lvl The level
     * @return The right text size for the given level */
    protected float findTextSize(int lvl)
    {
        return defaultHeight * (float) Math.pow(2.0 / 3.0, lvl);
    }

    /**
     * Adds necessary padding given a standard size
     * @param size
     * @return
     */
    protected Rect sizeAddPadding(Rect size)
    {
        // Copy the rectangle
        Rect out = new Rect(size);
        
        // Add the padding
        out.inset(-out.width() / 10, -out.height() / 10);
        out.offsetTo(0, 0);
        
        // Return the result
        return out;
    }
    
    /**
     * Returns the bounding boxes with the required padding
     */
    @Override
    public Rect[] getOperatorBoundingBoxes()
    {
        paint.setTextSize(findTextSize(level));
        Rect bounds = new Rect();
        paint.getTextBounds(c, 0, 1, bounds);
        return new Rect[] { sizeAddPadding(bounds) };
    }

    @Override
    public Rect getChildBoundingBox(int index) throws IndexOutOfBoundsException
    {
        // Will always throw an error since constants do not have children
        checkChildIndex(index);
        return null;
    }

    @Override
    public void draw(Canvas canvas)
    {
        // Draw the bounding boxes
        drawBoundingBoxes(canvas);
        
        // Set the text size and calculate the bounding boxes
        paint.setTextSize(findTextSize(level));
        Rect textBounding = new Rect();
        paint.getTextBounds(c, 0, 1, textBounding);
        Rect totalBounding = sizeAddPadding(textBounding);

        // Draw the variable
        canvas.save();
        canvas.translate((totalBounding.width() - textBounding.width()) / 2, (totalBounding.height() - textBounding.height()) / 2);
        paint.setColor(getColor());
        canvas.drawText(c, -textBounding.left, -textBounding.top, paint);
        canvas.restore();
    }

	@Override
	public void writeToXML(Document doc, Element el)
	{
		Element e = doc.createElement(NAME);
		e.setAttribute(ATTR_NAME, c);
		el.appendChild(e);
	}
}