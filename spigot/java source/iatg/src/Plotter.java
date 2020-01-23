import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.Vector;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class Plotter extends JPanel {

	private double xMax, xMin, yMax, yMin, xScale, yScale;
	private int width, height;
	// private double xTicLen, yTicLen;
	// Tic mark intervals
	double xTic = (xMax - xMin) / 2;
	double yTic = (yMax - yMin) / 2;
	double xTicInt = getTheX(xTic);
	double yTicInt = getTheY(yTic);
	double xTicLen = (yMax - yMin) / 50;
	double yTicLen = (xMax - xMin) / 50;
	int xOffset = 60;
	int yOffset = 40;
	boolean ShowAxis = true;
	boolean ShowTicks = true;
	boolean ShowNumbers = true;
	int ARROW_HEADWIDTH = 3;
	int ARROW_HEADLENGTH = 10;
	String title;
	boolean ShowTitle = false;

	public void setCircleRadiusPixels(int circleRadiusPixels) {
		CircleRadiusPixels = circleRadiusPixels;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setShowTitle(boolean showTitle) {
		ShowTitle = showTitle;
	}

	final static float dash1[] = { 10.0f, 5.0f };
	final static BasicStroke dashed = new BasicStroke(1.0f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

	final static BasicStroke solid = new BasicStroke();

	// Gorbek
	Vector<Vector<Double>> curve_x_vals = new Vector<Vector<Double>>();
	Vector<Vector<Double>> curve_y_vals = new Vector<Vector<Double>>();
	Vector<Color> curve_colors = new Vector<Color>();
	Vector<BasicStroke> linestyles = new Vector<BasicStroke>();

	// Nyilak
	Vector<Vector<Double>> arrow_x_vals = new Vector<Vector<Double>>();
	Vector<Vector<Double>> arrow_y_vals = new Vector<Vector<Double>>();
	Vector<Color> arrowcolors = new Vector<Color>();

	// Pontok
	Vector<Double> CircleX = new Vector<Double>();
	Vector<Double> CircleY = new Vector<Double>();
	Vector<Color> CircleColor = new Vector<Color>();
	int CircleRadiusPixels = 3;

	public void addCircle(double x, double y, Color col) {
		CircleX.addElement(x);
		CircleY.addElement(y);
		CircleColor.addElement(col);
	}

	public void setxTic(double xTic) {
		this.xTic = xTic;
		this.xTicInt = getTheX(xTic);
	}

	public void setyTic(double yTic) {
		this.yTic = yTic;
		this.yTicInt = getTheY(yTic);
	}

	public void setShowAxis(boolean showAxis) {
		ShowAxis = showAxis;
	}

	public void DeleteAll() {
		DeleteAllCurves();
		DeleteAllArrows();
		DeleteAllCircles();
	}

	private void DeleteAllCircles() {
		CircleX.removeAllElements();
		CircleY.removeAllElements();
		CircleColor.removeAllElements();
	}

	private void DeleteAllCurves() {
		linestyles.removeAllElements();
		curve_colors.removeAllElements();
		curve_x_vals.removeAllElements();
		curve_y_vals.removeAllElements();
	}

	private void DeleteAllArrows() {
		arrow_x_vals.removeAllElements();
		arrow_y_vals.removeAllElements();
	}

	public void addCurve(Vector<Double> x, Vector<Double> y, Color curvecolor,
			BasicStroke linestyle) {
		curve_colors.addElement(curvecolor);
		linestyles.addElement(linestyle);
		curve_x_vals.addElement(x);
		curve_y_vals.addElement(y);
	}

	public void addArrow(Vector<Double> x, Vector<Double> y, Color arrowcolor) {
		arrowcolors.addElement(arrowcolor);
		arrow_x_vals.addElement(x);
		arrow_y_vals.addElement(y);
	}

	private void drawAllArrows(Graphics g) {
		for (int i = 0; i < arrow_x_vals.size(); i++) {
			int[] xx = new int[arrow_x_vals.elementAt(i).size()];
			int[] yy = new int[arrow_x_vals.elementAt(i).size()];
			for (int j = 0; j < arrow_x_vals.elementAt(i).size(); j++) {
				xx[j] = getTheX(arrow_x_vals.elementAt(i).elementAt(j));
				yy[j] = getTheY(arrow_y_vals.elementAt(i).elementAt(j));
			}
			drawPolylineArrow(g, xx, yy, ARROW_HEADLENGTH, ARROW_HEADWIDTH,
					arrowcolors.elementAt(i));
		}
	}

	private void drawAllCurves(Graphics g) {
		for (int i = 0; i < curve_x_vals.size(); i++)
			drawCurve(g, curve_x_vals.elementAt(i), curve_y_vals.elementAt(i),
					curve_colors.elementAt(i), linestyles.elementAt(i));
	}

	private void drawAllCircles(Graphics g) {
		for (int i = 0; i < CircleX.size(); i++)
			drawCircle(g, CircleX.elementAt(i), CircleY.elementAt(i),
					CircleRadiusPixels, CircleColor.elementAt(i));
	}

	private void drawCurve(Graphics g, Vector<Double> x, Vector<Double> y,
			Color curvecolor, BasicStroke linestyle) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(curvecolor);
		g2.setStroke(linestyle);
		for (int i = 0; i < x.size() - 1; i++) {
			if ((x.elementAt(i) <= xMax) && (y.elementAt(i) <= yMax)
					&& (x.elementAt(i) >= xMin) && (y.elementAt(i) >= yMin))
				g.drawLine(getTheX(x.elementAt(i)), getTheY(y.elementAt(i)),
						getTheX(x.elementAt(i + 1)),
						getTheY(y.elementAt(i + 1)));
		}
	}

	private void drawPolylineArrow(Graphics g, int[] xPoints, int[] yPoints,
			int headLength, int headwidth, Color arrowcolor) {

		double theta1;

		g.setColor(arrowcolor);

		// calculate the length of the line - convert from Object to Integer to
		// int value

		Object tempX1 = ((Array.get(xPoints, ((xPoints.length) - 2))));

		Object tempX2 = ((Array.get(xPoints, ((xPoints.length) - 1))));

		Integer fooX1 = (Integer) tempX1;

		int x1 = fooX1.intValue();

		Integer fooX2 = (Integer) tempX2;

		int x2 = fooX2.intValue();

		Object tempY1 = ((Array.get(yPoints, ((yPoints.length) - 2))));

		Object tempY2 = ((Array.get(yPoints, ((yPoints.length) - 1))));

		Integer fooY1 = (Integer) tempY1;

		int y1 = fooY1.intValue();

		Integer fooY2 = (Integer) tempY2;

		int y2 = fooY2.intValue();

		int deltaX = (x2 - x1);

		int deltaY = (y2 - y1);

		double theta = Math.atan((double) (deltaY) / (double) (deltaX));

		if (deltaX < 0.0) {

			theta1 = theta + Math.PI; // If theta is negative make it positive

		}

		else {

			theta1 = theta; // else leave it alone

		}

		int lengthdeltaX = -(int) (Math.cos(theta1) * headLength);

		int lengthdeltaY = -(int) (Math.sin(theta1) * headLength);

		int widthdeltaX = (int) (Math.sin(theta1) * headwidth);

		int widthdeltaY = (int) (Math.cos(theta1) * headwidth);

		g.drawPolyline(xPoints, yPoints, xPoints.length);

		g.drawLine(x2, y2, x2 + lengthdeltaX + widthdeltaX, y2 + lengthdeltaY
				- widthdeltaY);

		g.drawLine(x2, y2, x2 + lengthdeltaX - widthdeltaX, y2 + lengthdeltaY
				+ widthdeltaY);

	}// end drawPolylineArrow

	public void setShowNumbers(boolean showNumbers) {
		ShowNumbers = showNumbers;
	}

	public void setShowTicks(boolean showTicks) {
		ShowTicks = showTicks;
	}

	String xlabel, ylabel;

	public void setXlabel(String xlabel) {
		this.xlabel = xlabel;
	}

	public void setYlabel(String ylabel) {
		this.ylabel = ylabel;
	}

	// Methods, constructors, fields.
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // paints background
		// Calculate the scale factors
		xScale = (width - xOffset * 2) / (xMax - xMin);
		yScale = (height - yOffset) / (yMax - yMin);

		// Set the origin based on the
		// minimum values in x and y
		g.translate((int) ((0 - xMin) * xScale), (int) ((0 - yMin) * yScale));
		if (ShowAxis)
			drawAxes(g);
		drawAllCurves(g);
		drawAllArrows(g);
		drawAllCircles(g);
		if (ShowTitle)
			addTitle(g);
	}

	private void addTitle(Graphics g) {

		char[] array = title.toCharArray();
		FontMetrics fontMetrics = g.getFontMetrics();
		Rectangle2D r2d = fontMetrics
				.getStringBounds(array, 0, array.length, g);
		int labWidth = (int) (r2d.getWidth());
		int labHeight = (int) (r2d.getHeight());

		g.setColor(Color.black);
		g.drawString(
				title,
				xOffset
						+ (int) java.lang.Math.round((double) width / 2.0
								- Math.round((double) labWidth / 2)),
				(int) java.lang.Math.round(labHeight));
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	void drawAxes(Graphics g) {
		g.setColor(Color.BLACK);

		xTicLen = (yMax - yMin) / 50;
		yTicLen = (xMax - xMin) / 50;

		// x & y labels
		char[] array = xlabel.toCharArray();
		FontMetrics fontMetrics = g.getFontMetrics();
		Rectangle2D r2d = fontMetrics
				.getStringBounds(array, 0, array.length, g);
		int labWidth = (int) (r2d.getWidth());
		int labHeight = (int) (r2d.getHeight());
		g.drawString(xlabel, getTheX(xMax) + 3,
				getTheY(0) + (int) Math.round((double) labHeight / 2.0));

		array = ylabel.toCharArray();
		r2d = fontMetrics.getStringBounds(array, 0, array.length, g);
		labWidth = (int) (r2d.getWidth());
		labHeight = (int) (r2d.getHeight());
		g.drawString(ylabel,
				getTheX(0) - (int) Math.round((double) labWidth / 2.0),
				getTheY(yMax) - labHeight);

		// Draw the axes
		g.drawLine(getTheX(xMin), getTheY(0.0), getTheX(xMax), getTheY(0.0));
		g.drawLine(getTheX(xMax), getTheY(0.0), getTheX(xMax) - 5,
				getTheY(0.0) + 3);
		g.drawLine(getTheX(xMax), getTheY(0.0), getTheX(xMax) - 5,
				getTheY(0.0) - 3);

		g.drawLine(getTheX(0.0), getTheY(yMin), getTheX(0.0), getTheY(yMax));
		g.drawLine(getTheX(0.0), getTheY(yMax), getTheX(0.0) - 3,
				getTheY(yMax) + 5);
		g.drawLine(getTheX(0.0), getTheY(yMax), getTheX(0.0) + 3,
				getTheY(yMax) + 5);

		// Draw the tic marks on axes
		if (ShowTicks) {
			xTics(g);
			yTics(g);
		}

	}// end drawAxes

	// Method to draw tic marks on x-axis
	void xTics(Graphics g) {
		float xDoub = 0;
		int x = 0;

		// Get the ends of the tic marks.
		int topEnd = getTheY(xTicLen / 2);
		int bottomEnd = getTheY(-xTicLen / 2);

		// If the vertical size of the
		// plotting area is small, the
		// calculated tic size may be too
		// small. In that case, set it to
		// 10 pixels.
		if (topEnd < 5) {
			topEnd = 5;
			bottomEnd = -5;
		}// end if

		while (xDoub < xMax) {
			x = getTheX(xDoub);
			g.drawLine(x, topEnd, x, bottomEnd);
			if (ShowNumbers)
				addXTickLabel(g, "" + xDoub, (double) xDoub);
			xDoub += xTic;
		}// end while

		// Now do the negative x-axis moving
		// to the left from zero
		xDoub = 0;
		while (xDoub > xMin) {
			x = getTheX(xDoub);
			g.drawLine(x, topEnd, x, bottomEnd);
			if (ShowNumbers)
				addXTickLabel(g, "" + xDoub, (double) xDoub);
			xDoub -= xTic;
		}// end while

	}// end xTics

	// ---------------------------------//

	// Method to draw tic marks on y-axis
	void yTics(Graphics g) {
		float yDoub = 0;
		int y = 0;
		int rightEnd = getTheX(yTicLen / 2);
		int leftEnd = getTheX(-yTicLen / 2);

		// Loop and draw a series of short
		// lines to serve as tic marks.
		// Begin with the positive y-axis
		// moving up from zero.
		while (yDoub < yMax) {
			y = getTheY(yDoub);
			g.drawLine(rightEnd, y, leftEnd, y);
			if (ShowNumbers)
				addYTickLabel(g, "" + yDoub, (double) yDoub);
			yDoub += yTic;
		}// end while

		// Now do the negative y-axis moving
		// down from zero.
		yDoub = 0;
		while (yDoub > yMin) {
			y = getTheY(yDoub);
			g.drawLine(rightEnd, y, leftEnd, y);
			if (ShowNumbers)
				addYTickLabel(g, "" + yDoub, (double) yDoub);
			yDoub -= yTic;
		}// end while

	}// end yTics

	private void addXTickLabel(Graphics g, String ticstring, Double x) {

		char[] array = ticstring.toCharArray();
		FontMetrics fontMetrics = g.getFontMetrics();
		Rectangle2D r2d = fontMetrics
				.getStringBounds(array, 0, array.length, g);
		int labWidth = (int) (r2d.getWidth());
		int labHeight = (int) (r2d.getHeight());
		g.drawString(ticstring,
				getTheX(x) - java.lang.Math.round(labWidth / 2),
				(int) (getTheY(0) + java.lang.Math.round(1.5 * labHeight)));
	}

	private void addYTickLabel(Graphics g, String ticstring, Double y) {

		char[] array = ticstring.toCharArray();
		FontMetrics fontMetrics = g.getFontMetrics();
		Rectangle2D r2d = fontMetrics
				.getStringBounds(array, 0, array.length, g);
		int labWidth = (int) (r2d.getWidth());
		int labHeight = (int) (r2d.getHeight());
		g.drawString(ticstring,
				(int) (getTheX(0) - java.lang.Math.round(1.5 * labWidth)),
				(int) (getTheY(y) + java.lang.Math.round(labHeight / 2)));
	}

	public void setxMax(double xMax) {
		this.xMax = xMax;
	}

	public void setxMin(double xMin) {
		this.xMin = xMin;
	}

	public void setyMax(double yMax) {
		this.yMax = yMax;
	}

	public void setyMin(double yMin) {
		this.yMin = yMin;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	// This method translates and scales
	// a double y value to plot properly
	// in the integer coordinate system.
	// In addition to scaling, it causes
	// the positive direction of the
	// y-axis to be from bottom to top.
	int getTheY(double y) {
		double yDoub = (yMax + yMin) - y;
		int yInt = yOffset + (int) (yDoub * yScale);
		return yInt;
	}// end getTheY
		// ---------------------------------//

	// This method scales a double x value
	// to plot properly in the integer
	// coordinate system.
	int getTheX(double x) {
		return (int) (xOffset + x * xScale);
	}

	private void drawCircle(Graphics g, double x, double y, int radius,
			Color col) {
		// g.setForeground(col);
		g.setColor(col);
		g.fillOval(getTheX(x) - radius, getTheY(y) - radius, radius * 2,
				radius * 2);
	}
	
	

}
