import java.awt.*;
import java.applet.Applet;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.Hashtable;
import java.util.Vector;

@SuppressWarnings({ "serial", "deprecation", "unused" })
public class AtgHatasfokEsFajlagos extends JFrame implements ChangeListener {
	private JSlider slider;
	private JPanel controlpanel;
	private JPanel drawpanel;

	private Plotter drawing1;
	private Plotter drawing2;
	private Plotter drawing3;
	private Plotter drawing4;
	double xMin = 0.0, xMax = 350;

	private JLabel message;
	Vector<Double> x;
	Vector<Double> yjg;
	Vector<Double> ycso;
	Vector<Double> yeta;
	Vector<Double> yteljesitmeny;
	Vector<Double> yfajlagos;
	Vector<Double> xf;
	Vector<Double> yf;

	public static void main(String[] args) {
        AtgHatasfokEsFajlagos app = new AtgHatasfokEsFajlagos();
        app.init();
    }
	
	public void init() {
		int N = 100;
		double dx = (xMax - xMin) / (N - 1);
		x = new Vector<Double>();
		yjg = new Vector<Double>();
		yeta = new Vector<Double>();
		yteljesitmeny = new Vector<Double>();
		yfajlagos = new Vector<Double>();
		ycso = new Vector<Double>();
		xf = new Vector<Double>();
		yf = new Vector<Double>();

		double Q;
		for (int i = 0; i < N; i++) {
			Q = i * dx;
			x.addElement(Q);
			yjg.addElement(-0.0004 * Q * Q + 0.04 * Q + 24);
			yteljesitmeny.addElement(-0.0001 * Q * Q + 0.064 * Q + 4.1);
			yeta.addElement(Q / 3600 * yjg.elementAt(i) * 9810 / 1000
					/ yteljesitmeny.elementAt(i) * 100);
			yfajlagos.addElement(yteljesitmeny.elementAt(i) / Q);
		}

		for (int i = 0; i < 100; i++) {
			xf.addElement((double) i);
			yf.addElement(0.004 * (1 / (xf.elementAt(i) / 100.) - 1)
					* (1 / (xf.elementAt(i) / 100.) - 1));
		}

		slider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
		// Create the label table.
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		// PENDING: could use images, but we don't have any good ones.
		labelTable.put(new Integer(1), new JLabel("1 %"));
		// new JLabel(createImageIcon("images/stop.gif")) );
		labelTable.put(new Integer(50), new JLabel("50 %"));
		labelTable.put(new Integer(100), new JLabel("100 %"));
		// new JLabel(createImageIcon("images/fast.gif")) );
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		slider.addChangeListener(this);

		message = new JLabel("Tolózár állás: 50 %", JLabel.CENTER);

		controlpanel = new JPanel();
		controlpanel.setLayout(new GridLayout(0, 1));

		controlpanel.add(message);
		controlpanel.add(slider);

		setLayout(new BorderLayout());
		add("North", controlpanel);

		drawing1 = new Plotter();
		drawing1.setxMin(xMin);
		drawing1.setxMax(xMax);
		drawing1.setyMin(0);
		drawing1.setyMax(40);
		drawing1.setWidth(300);
		drawing1.setHeight(200);
		drawing1.setXlabel("Q [m3/h]");
		drawing1.setYlabel("H [m]");
		drawing1.setxTic(100);
		drawing1.setyTic(10);
		drawing1.setShowTicks(true);
		drawing1.setShowNumbers(true);
		drawing1.setxOffset(60);
		drawing1.setyOffset(40);
		drawing1.setTitle("Jelleggörbe");
		drawing1.setShowTitle(true);

		drawing2 = new Plotter();
		drawing2.setxMin(0);
		drawing2.setxMax(110);
		drawing2.setyMin(0);
		drawing2.setyMax(1.1);
		drawing2.setWidth(300);
		drawing2.setHeight(200);
		drawing2.setXlabel("Nyitás");
		drawing2.setYlabel("K");
		drawing2.setxTic(25);
		drawing2.setyTic(0.2);
		drawing2.setShowTicks(true);
		drawing2.setShowNumbers(true);
		drawing2.setShowAxis(true);
		drawing2.setxOffset(40);
		drawing2.setyOffset(40);
		drawing2.setTitle("Tolózár ellenállástényezõ");
		drawing2.setShowTitle(true);

		drawing3 = new Plotter();
		drawing3.setxMin(xMin);
		drawing3.setxMax(xMax);
		drawing3.setyMin(0);
		drawing3.setyMax(110);
		drawing3.setWidth(300);
		drawing3.setHeight(200);
		drawing3.setXlabel("Q [m3/h]");
		drawing3.setYlabel("hatásfok [%]");
		drawing3.setxTic(100);
		drawing3.setyTic(20);
		drawing3.setShowTicks(true);
		drawing3.setShowNumbers(true);
		drawing3.setxOffset(60);
		drawing3.setyOffset(40);
		drawing3.setTitle("Hatásfok");
		drawing3.setShowTitle(true);

		drawing4 = new Plotter();
		drawing4.setxMin(xMin);
		drawing4.setxMax(xMax);
		drawing4.setyMin(0);
		drawing4.setyMax(0.45);
		drawing4.setWidth(300);
		drawing4.setHeight(200);
		drawing4.setXlabel("Q [m3/h]");
		drawing4.setYlabel("f [kWh/m3]");
		drawing4.setxTic(100);
		drawing4.setyTic(0.1);
		drawing4.setShowTicks(true);
		drawing4.setShowNumbers(true);
		drawing4.setxOffset(40);
		drawing4.setyOffset(40);
		drawing4.setTitle("Fajlagos energiafogyasztás");
		drawing4.setShowTitle(true);

		update_curves();

		// Minden osszerakunk es rajzolunk.
		drawpanel = new JPanel();
		drawpanel.setLayout(new GridLayout(2, 2, 10, 10));
		drawpanel.add(drawing1);
		drawpanel.add(drawing2);
		drawpanel.add(drawing3);
		drawpanel.add(drawing4);

		add("Center", drawpanel);
		setSize(new Dimension(700, 600));
		setVisible(true);
		
	}

	public void update_curves() {

		drawing1.DeleteAll();
		drawing2.DeleteAll();
		drawing3.DeleteAll();
		drawing4.DeleteAll();

		double K = (double) slider.getValue() / 100.;
		// double ize = 0.004 * (1 / K - 1) * (1 / K - 1);
		double ize = 0.0001 * java.lang.Math.pow(1.29 / K - 1, 1.5);
		ycso.removeAllElements();
		for (int i = 0; i < x.size(); i++)
			ycso.addElement(10 + ize * x.elementAt(i) * x.elementAt(i));

		double c = -14.;
		double b = -0.04;
		double a = ize + 0.0004;
		double Qmp = (-b + Math.sqrt(b * b - 4 * a * c)) / 2 / a;
		double Hmp = -0.0004 * Qmp * Qmp + 0.04 * Qmp + 24;
		double Pmp = -0.0001 * Qmp * Qmp + 0.064 * Qmp + 4.1;
		double etamp = Qmp / 3600 * Hmp * 9810 / 1000 / Pmp;
		double fmp = Pmp / Qmp;

		drawing1.addCurve(x, yjg, Color.RED, Plotter.solid);
		drawing1.addCurve(x, yteljesitmeny, Color.BLUE, Plotter.solid);
		drawing1.addCurve(x, ycso, Color.BLACK, Plotter.solid);
		drawing1.addCircle(Qmp, Hmp, Color.RED);
		drawing1.addCircle(Qmp, Pmp, Color.BLUE);

		drawing2.addCurve(xf, yf, Color.BLUE, Plotter.solid);
		double Kyakt = 0.004 * (1 / K - 1) * (1 / K - 1);
		drawing2.addCircle(K * 100., Kyakt, Color.RED);

		drawing3.addCurve(x, yeta, Color.BLUE, Plotter.solid);
		drawing3.addCircle(Qmp, etamp * 100, Color.RED);

		drawing4.addCurve(x, yfajlagos, Color.BLUE, Plotter.solid);
		drawing4.addCircle(Qmp, fmp, Color.RED);

		drawing1.repaint();
		drawing2.repaint();
		drawing3.repaint();
		drawing4.repaint();
	}

	public void stateChanged(ChangeEvent ce) {

		message.setText("Tolózár állás: " + slider.getValue() + " %");

		update_curves();
	}
}