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
public class AtgNPSH extends JFrame implements ChangeListener {
	private JSlider slider;
	private JPanel controlpanel;
	private JPanel drawpanel;

	private Plotter drawing1;
	double xMin1 = 0.0, xMax1 = 120;
	double yMin1 = 0, yMax1 = 30;

	private Plotter drawing2;
	double xMin2 = 0.0, xMax2 = 10;
	double yMin2 = 0, yMax2 = 30;

	private JLabel message;
	Vector<Double> Q;
	Vector<Double> Qr;
	Vector<Double> H;
	Vector<Double> NPSHr;
	Vector<Double> NPSH;
	Vector<Double> NPSH_H;

	public static void main(String[] args) {
		AtgNPSH app = new AtgNPSH();
        app.init();
    }
	
	public void init() {
		int N = 100;
		double dx = (xMax1 - xMin1) / (N - 1);
		Q = new Vector<Double>();
		Qr = new Vector<Double>();
		H = new Vector<Double>();
		NPSH = new Vector<Double>();
		NPSHr = new Vector<Double>();
		NPSH_H = new Vector<Double>();

		double QQ;
		for (int i = 0; i < N; i++) {
			QQ = i * dx;
			Q.addElement(QQ);
			H.addElement(24 - 0.0024 * QQ * QQ);
			if ((QQ > 20) && (QQ < 80)) {
				Qr.addElement(QQ);
				NPSHr.addElement(-0.2 * java.lang.Math.exp(QQ / 70)
						* java.lang.Math.log(0.03));
			}
		}

		slider = new JSlider(JSlider.HORIZONTAL, 20, 80, 50);
		// Create the label table.
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(20), new JLabel("20"));
		labelTable.put(new Integer(40), new JLabel("40"));
		labelTable.put(new Integer(60), new JLabel("60"));
		labelTable.put(new Integer(80), new JLabel("80"));
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		slider.addChangeListener(this);

		message = new JLabel("", JLabel.CENTER);
		message.setText("Térfogatáram: " + slider.getValue() + " m3/h");

		controlpanel = new JPanel();
		controlpanel.setLayout(new GridLayout(2, 1));

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
		drawing1.setXlabel("Q [m3/h]");
		drawing1.setYlabel("H,NPSH [m]");
		drawing1.setxTic(20);
		drawing1.setyTic(10);
		drawing1.setShowTicks(true);
		drawing1.setShowNumbers(true);
		drawing1.setxOffset(60);
		drawing1.setyOffset(40);
		drawing1.setTitle("Jelleggörbe");
		drawing1.setShowTitle(true);

		drawing2 = new Plotter();
		drawing2.setxMin(xMin2);
		drawing2.setxMax(xMax2);
		drawing2.setyMin(yMin2);
		drawing2.setyMax(yMax2);
		drawing2.setWidth(300);
		drawing2.setHeight(200);
		drawing2.setXlabel("NPSH [m]");
		drawing2.setYlabel("H [m]");
		drawing2.setxTic(2);
		drawing2.setyTic(10);
		drawing2.setShowTicks(true);
		drawing2.setShowNumbers(true);
		drawing2.setShowAxis(true);
		drawing2.setxOffset(40);
		drawing2.setyOffset(40);
		drawing2.setTitle("Leszívási görbe");
		drawing2.setShowTitle(true);

		update_curves();

		// Minden osszerakunk es rajzolunk.
		drawpanel = new JPanel();
		drawpanel.setLayout(new GridLayout(1, 2, 10, 10));
		drawpanel.add(drawing1);
		drawpanel.add(drawing2);

		add("Center", drawpanel);
		setSize(new Dimension(700, 350));
		setVisible(true);
	}

	public void update_curves() {

		drawing1.DeleteAll();
		drawing2.DeleteAll();

		double Qakt = (double) slider.getValue();
		double Hakt = 24 - 0.0024 * Qakt * Qakt;
		double a = 0.2 * java.lang.Math.exp(Qakt / 70);

		double NPSHrakt = -a * java.lang.Math.log(0.03);
		double dx = 0.1;
		double xx = 0;
		NPSH.removeAllElements();
		NPSH_H.removeAllElements();
		while (xx < 10) {
			NPSH.addElement(xx);
			NPSH_H.addElement(Hakt * (1 - java.lang.Math.exp(-xx / a)));
			xx += dx;
		}

		drawing1.addCurve(Q, H, Color.RED, Plotter.solid);
		drawing1.addCurve(Qr, NPSHr, Color.BLUE, Plotter.solid);
		drawing1.addCircle(Qakt, NPSHrakt, Color.BLUE);
		drawing1.addCircle(Qakt, Hakt, Color.RED);

		Vector<Double> x1 = new Vector<Double>();
		Vector<Double> x2 = new Vector<Double>();
		Vector<Double> y1 = new Vector<Double>();
		Vector<Double> y2 = new Vector<Double>();

		x1.addElement(NPSHrakt);
		x1.addElement(NPSHrakt);
		y1.addElement(Hakt * 0.97);
		y1.addElement(0.0);

		x2.addElement(NPSHrakt);
		x2.addElement(0.0);
		y2.addElement(Hakt * 0.97);
		y2.addElement(Hakt * 0.97);

		drawing2.addCurve(NPSH, NPSH_H, Color.BLACK, Plotter.solid);
		drawing2.addArrow(x1, y1, Color.BLUE);
		drawing2.addArrow(x2, y2, Color.RED);
		drawing2.addCircle(NPSHrakt, Hakt * 0.97, Color.BLACK);
		drawing2.setTitle("Leszívási görbe, Q=" + Qakt + "m3/h");

		drawing1.repaint();
		drawing2.repaint();
	}

	public void stateChanged(ChangeEvent ce) {

		message.setText("Térfogatáram: " + slider.getValue() + " m3/h");

		update_curves();
	}
}
