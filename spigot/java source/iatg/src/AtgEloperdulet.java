import java.awt.*;
import java.applet.Applet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.Hashtable;
import java.util.Vector;

@SuppressWarnings({ "serial", "deprecation", "unused" })
public class AtgEloperdulet extends JFrame implements ChangeListener {
	private JSlider slider;
	private JPanel controlpanel;
	private JPanel drawpanel;

	private Plotter drawing1;
	double xMin1 = 0.0, xMax1 = 1.8, yMin1 = 0.0, yMax1 = 2.3;

	private Plotter drawing2;
	double xMin2 = 0.0, xMax2 = 1.0, yMin2 = 0.0, yMax2 = 1.0;

	private JLabel message;
	Vector<Double> x;
	Vector<Double> yjg;
	Vector<Double> yjg_prerotationfree;
	Vector<Double> yr;

	public static void main(String[] args) {
		AtgEloperdulet app = new AtgEloperdulet();
        app.init();
    }
	
	public void init() {
		slider = new JSlider(JSlider.HORIZONTAL, 30, 120, 90);
		// Create the label table.
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		// PENDING: could use images, but we don't have any good ones.
		labelTable.put(new Integer(30), new JLabel("30 fok"));
		// new JLabel(createImageIcon("images/stop.gif")) );
		labelTable.put(new Integer(90), new JLabel("90 fok"));
		labelTable.put(new Integer(120), new JLabel("120 fok"));
		// new JLabel(createImageIcon("images/fast.gif")) );
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		slider.addChangeListener(this);

		message = new JLabel("Elõperdület lapátrács szög : 90 fok",
				JLabel.CENTER);

		controlpanel = new JPanel();
		controlpanel.setLayout(new GridLayout(0, 1));

		controlpanel.add(message);
		controlpanel.add(slider);

		setLayout(new BorderLayout());
		add("North", controlpanel);

		drawing1 = new Plotter();
		drawing1.setxMin(xMin1);
		drawing1.setxMax(xMax1);
		drawing1.setyMin(yMin1);
		drawing1.setyMax(yMax1);
		drawing1.setWidth(300);
		drawing1.setHeight(200);
		drawing1.setXlabel("Q/Qn");
		drawing1.setYlabel("H/Hn");
		drawing1.setxTic(0.5);
		drawing1.setyTic(0.5);
		drawing1.setShowTicks(true);
		drawing1.setShowNumbers(true);
		drawing1.setxOffset(40);
		drawing1.setyOffset(40);

		drawing2 = new Plotter();
		drawing2.setxMin(xMin2);
		drawing2.setxMax(xMax2);
		drawing2.setyMin(yMin2);
		drawing2.setyMax(yMax2);
		drawing2.setWidth(200);
		drawing2.setHeight(200);
		drawing2.setXlabel("");
		drawing2.setYlabel("");
		drawing2.setShowTicks(false);
		drawing2.setShowNumbers(false);
		drawing2.setShowAxis(false);
		drawing2.setxOffset(20);
		drawing2.setyOffset(10);
		drawing2.setTitle("Belépési sebességi háromszög:");
		drawing2.setShowTitle(true);

		update_curves();

		// Minden osszerakunk es rajzolunk.
		drawpanel = new JPanel();
		drawpanel.setLayout(new BoxLayout(drawpanel, BoxLayout.X_AXIS));
		drawpanel.add(drawing1);
		drawpanel.add(drawing2);
		add("Center", drawpanel);
		setSize(new Dimension(600, 350));
		setVisible(true);
	}

	public void update_curves() {

		drawing1.DeleteAll();
		drawing2.DeleteAll();

		int N = 100;
		double dx = (xMax1 - xMin1) / (N - 1);
		x = new Vector<Double>();
		yjg_prerotationfree = new Vector<Double>();
		yjg = new Vector<Double>();
		yr = new Vector<Double>();
		for (int i = 0; i < N; i++)
			x.addElement(i * dx);
		yjg = jelleggorbe(x, slider.getValue());
		yjg_prerotationfree = jelleggorbe(x, 90);
		yr = jelleggorbe_rendszer(x);

		drawing1.addCurve(x, yjg, Color.RED, Plotter.solid);
		drawing1.addCurve(x, yjg_prerotationfree, Color.BLUE, Plotter.solid);
		drawing1.addCurve(x, yr, Color.BLACK, Plotter.solid);
		drawing1.repaint();

		// Jobb oldali jelleggorbe
		// 90 fokos nyil
		double u0 = 1;
		double c0 = 0.25;
		double beta1 = java.lang.Math.atan(c0 / u0);
		double fi = 90 * java.lang.Math.PI / 180.;
		double veltriangX = getVelocityTriangleX(fi, u0, beta1);
		double veltriangY = veltriangX * java.lang.Math.tan(beta1);
		drawing2 = addArrow(drawing2, 0.0, u0, 0.0, 0.0, Color.blue);
		drawing2 = addArrow(drawing2, u0, veltriangX, 0.0, veltriangY,
				Color.blue);
		drawing2 = addArrow(drawing2, 0.0, veltriangX, 0.0, veltriangY,
				Color.blue);

		fi = (double) slider.getValue() * java.lang.Math.PI / 180.;
		veltriangX = getVelocityTriangleX(fi, u0, beta1);
		veltriangY = veltriangX * java.lang.Math.tan(beta1);
		drawing2 = addArrow(drawing2, 0.0, u0, 0.0, 0.0, Color.red);
		drawing2 = addArrow(drawing2, u0, veltriangX, 0.0, veltriangY,
				Color.red);
		drawing2 = addArrow(drawing2, 0.0, veltriangX, 0.0, veltriangY,
				Color.red);

		drawing2.repaint();
	}

	public Plotter addArrow(Plotter drawing, double fromX, double toX,
			double fromY, double toY, Color arrowcolor) {

		Vector<Double> ax = new Vector<Double>();
		Vector<Double> ay = new Vector<Double>();
		ax.addElement(fromX);
		ax.addElement(toX);
		ay.addElement(fromY);
		ay.addElement(toY);
		drawing.addArrow(ax, ay, arrowcolor);

		return drawing;
	}

	public double getVelocityTriangleX(double fi, double u, double beta) {
		return (u * (java.lang.Math.tan(fi)) / (java.lang.Math.tan(beta) + java.lang.Math
				.tan(fi)));
	}

	public void stateChanged(ChangeEvent ce) {

		message.setText("Elõperdület lapátrács szög: " + slider.getValue()
				+ " fok");

		update_curves();
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

}