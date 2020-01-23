import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
class drawfig extends JPanel {
	private double fi;
	private double xMax, xMin, yMax, yMin, xScale, yScale, width, height;
	// private double xTicLen, yTicLen;
	// Tic mark intervals
	double xTicInt = 25.0;
	double yTicInt = 25.0;
	double xTicLen = (yMax - yMin) / 50;
	double yTicLen = (xMax - xMin) / 50;
	int xOffset = 40;
	int yOffset = 40;
	boolean ShowTicks = true;
	boolean ShowNumbers = true;

	public void setFi(double fi) {
		this.fi = fi;
	}

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
		xScale = width / (xMax - xMin);
		yScale = height / (yMax - yMin);

		// Set the origin based on the
		// minimum values in x and y
		g.translate((int) ((0 - xMin) * xScale), (int) ((0 - yMin) * yScale));
		drawAxes(g);
		drawFigure(g);
	}

	void drawFigure(Graphics g) {
		double x1 = xMin;
		double x2 = xMax;
		int N = 100;
		double dx = (x2 - x1) / (N - 1);
		Vector<Double> x = new Vector<Double>();
		for (int i = 0; i < N; i++)
			x.addElement(i * dx);
		Vector<Double> yep = jelleggorbe(x, fi);

		// Eloperdulet jelleggorbe
		g.setColor(Color.BLUE);
		for (int i = 0; i < x.size() - 1; i++) {
			// System.out.print("\n x_from=" + x_from + ", y_from=" + y_from
			// + ", x_to=" + x_to + ", y_to=" + y_to);
			if ((x.elementAt(i) < xMax) && (yep.elementAt(i) < yMax)
					&& (x.elementAt(i) > xMin) && (yep.elementAt(i) > yMin))
				g.drawLine(getTheX(x.elementAt(i)), getTheY(yep.elementAt(i)),
						getTheX(x.elementAt(i + 1)),
						getTheY(yep.elementAt(i + 1)));
		}

		// Rendszer jelleggorbe
		g.setColor(Color.RED);
		Vector<Double> yr = jelleggorbe_rendszer(x);
		for (int i = 0; i < x.size() - 1; i++) {
			// System.out.print("\n x_from=" + x_from + ", y_from=" + y_from
			// + ", x_to=" + x_to + ", y_to=" + y_to);
			if ((x.elementAt(i) < xMax) && (yr.elementAt(i) < yMax)
					&& (x.elementAt(i) > xMin) && (yr.elementAt(i) > yMin))
				g.drawLine(getTheX(x.elementAt(i)), getTheY(yr.elementAt(i)),
						getTheX(x.elementAt(i + 1)),
						getTheY(yr.elementAt(i + 1)));
		}
	}

	Vector<Double> jelleggorbe(Vector<Double> x, double fi) {
		Vector<Double> y = new Vector<Double>();
		Vector<Double> b = new Vector<Double>();
		Vector<Double> c = new Vector<Double>();
		b.addElement(0.0052);
		b.addElement(0.0077);
		b.addElement(-0.011);
		b.addElement(0.0481);
		b.addElement(-0.026);
		c.addElement(1.43);
		c.addElement(-2.71);
		c.addElement(5.025);
		c.addElement(-8.68);
		c.addElement(3.627);
		for (int i = 0; i < x.size(); i++) {
			double yy = 0;
			for (int j = 0; j < b.size(); j++)
				yy += (b.elementAt(j) * fi + c.elementAt(j))
						* java.lang.Math.pow(x.elementAt(i), j);
			y.addElement(yy);
		}
		return y;
	}

	Vector<Double> jelleggorbe_rendszer(Vector<Double> x) {
		Vector<Double> y = new Vector<Double>();
		for (int i = 0; i < x.size(); i++)
			y.addElement(0.5 + x.elementAt(i) * x.elementAt(i));

		return y;
	}

	void drawAxes(Graphics g) {
		g.setColor(Color.BLACK);

		xTicLen = (yMax - yMin) / 50;
		yTicLen = (xMax - xMin) / 50;

		// Label left x-axis and bottom
		// y-axis. These are the easy
		// ones. Separate the labels from
		// the ends of the tic marks by
		// two pixels.
		// g.drawString("" + (int) xMin, getTheX(xMin), getTheY(xTicLen / 2) +
		// 2);
		// g.drawString("" + (int) yMin, getTheX(yTicLen / 2) + 2,
		// getTheY(yMin));

		// Label the right x-axis and the
		// top y-axis. These are the hard
		// ones because the position must
		// be adjusted by the font size and
		// the number of characters.
		// Get the width of the string for
		// right end of x-axis and the
		// height of the string for top of
		// y-axis
		// Create a string that is an
		// integer representation of the
		// label for the right end of the
		// x-axis. Then get a character
		// array that represents the
		// string.
		int xMaxInt = (int) xMax;
		String xMaxStr = "" + xMaxInt;
		char[] array = xMaxStr.toCharArray();

		// Get a FontMetrics object that can
		// be used to get the size of the
		// string in pixels.
		FontMetrics fontMetrics = g.getFontMetrics();
		// Get a bounding rectangle for the
		// string
		Rectangle2D r2d = fontMetrics
				.getStringBounds(array, 0, array.length, g);
		// Get the width and the height of
		// the bounding rectangle. The
		// width is the width of the label
		// at the right end of the
		// x-axis. The height applies to
		// all the labels, but is needed
		// specifically for the label at
		// the top end of the y-axis.
		int labWidth = (int) (r2d.getWidth());
		int labHeight = (int) (r2d.getHeight());

		// Label the positive x-axis and the
		// positive y-axis using the width
		// and height from above to
		// position the labels. These
		// labels apply to the very ends of
		// the axes at the edge of the
		// plotting surface.

		g.drawString(xlabel, getTheX(xMax) + 10, getTheY(0));
		g.drawString(ylabel, getTheX(0) - 5, getTheY(yMax) - 10);

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
		if (ShowNumbers) {
			g.drawString("" + (int) xMax, getTheX(xMax) - labWidth,
					getTheY(xTicLen / 2) + 20);
			g.drawString("" + (int) yMax, getTheX(yTicLen / 2) - yOffset,
					getTheY(yMax) + labHeight);
		}
	}// end drawAxes

	// Method to draw tic marks on x-axis
	void xTics(Graphics g) {
		double xDoub = 0;
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

		// Loop and draw a series of short
		// lines to serve as tic marks.
		// Begin with the positive x-axis
		// moving to the right from zero.
		while (xDoub < xMax) {
			x = getTheX(xDoub);
			g.drawLine(x, topEnd, x, bottomEnd);
			System.out.print("\n x=" + x + ", xDoub=" + xDoub
					+ ", getTheX(xDoub)=" + getTheX(xDoub) + ", topEnd="
					+ topEnd + ", bottomEnd=" + bottomEnd + ", xTicLen="
					+ xTicLen);
			xDoub += xTicInt;
		}// end while

		// Now do the negative x-axis moving
		// to the left from zero
		xDoub = 0;
		System.out.print("\n xDoub=" + xDoub + ", xMin=" + xMin);
		while (xDoub > xMin) {
			x = getTheX(xDoub);
			System.out.print("\n x=" + x + ", xDoub=" + xDoub
					+ ", getTheX(xDoub)=" + getTheX(xDoub) + ", topEnd="
					+ topEnd + ", bottomEnd=" + bottomEnd);
			g.drawLine(x, topEnd, x, bottomEnd);
			xDoub -= xTicInt;
		}// end while

	}// end xTics

	// ---------------------------------//

	// Method to draw tic marks on y-axis
	void yTics(Graphics g) {
		double yDoub = 0;
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
			yDoub += yTicInt;
		}// end while

		// Now do the negative y-axis moving
		// down from zero.
		yDoub = 0;
		while (yDoub > yMin) {
			y = getTheY(yDoub);
			g.drawLine(rightEnd, y, leftEnd, y);
			yDoub -= yTicInt;
		}// end while

	}// end yTics

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

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
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
}
