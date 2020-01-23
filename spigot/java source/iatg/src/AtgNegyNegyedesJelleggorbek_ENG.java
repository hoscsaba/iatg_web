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

@SuppressWarnings({ "serial", "unused", "deprecation" })
public class AtgNegyNegyedesJelleggorbek_ENG extends JFrame implements
		ChangeListener {
	private JSlider slider;
	private JPanel controlpanel;
	private JPanel bottompanel;
	private JLabel label_nominal;
	private JLabel label_actual;

	private Plotter drawing1;
	double xMin = -150, xMax = 250;

	private JLabel message;
	Vector<Double> x;
	Vector<Double> Hnom, Mnom, Huj, Muj;
	double ncs;

	public static void main(String[] args) {
		AtgNegyNegyedesJelleggorbek_ENG app = new AtgNegyNegyedesJelleggorbek_ENG();
        app.init();
    }
	
	public void init() {
		int N = 100;
		double dx = (xMax - xMin) / (N - 1);
		x = new Vector<Double>();
		for (int i = 0; i < N; i++)
			x.addElement(xMin + i * dx);

		Hnom = new Vector<Double>();
		Mnom = new Vector<Double>();
		Huj = new Vector<Double>();
		Muj = new Vector<Double>();
		Hnom = computeH(100, x);
		Mnom = computeM(100, x);

		slider = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
		// Create the label table.
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		// PENDING: could use images, but we don't have any good ones.
		labelTable.put(new Integer(0), new JLabel("0%"));
		// new JLabel(createImageIcon("images/stop.gif")) );
		labelTable.put(new Integer(-100), new JLabel("-100%"));
		labelTable.put(new Integer(+100), new JLabel("+100%"));
		// new JLabel(createImageIcon("images/fast.gif")) );
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		slider.addChangeListener(this);

		message = new JLabel("RPM (n/n nom): 0%", JLabel.CENTER);

		controlpanel = new JPanel();
		controlpanel.setLayout(new GridLayout(0, 1));

		controlpanel.add(message);
		controlpanel.add(slider);

		setLayout(new BorderLayout());
		add("North", controlpanel);

		drawing1 = new Plotter();
		drawing1.setxMin(xMin);
		drawing1.setxMax(xMax);
		drawing1.setyMin(-250);
		drawing1.setyMax(250);
		drawing1.setWidth(450);
		drawing1.setHeight(400);
		drawing1.setXlabel("Q/Q_nom [%]");
		drawing1.setYlabel("H/H_nom, M/M_nom [%]");
		drawing1.setxTic(50);
		drawing1.setyTic(50);
		drawing1.setShowTicks(true);
		drawing1.setShowNumbers(true);
		drawing1.setxOffset(60);
		drawing1.setyOffset(40);

		update_curves();

		bottompanel = new JPanel(new GridLayout(1, 2));

		label_nominal = new JLabel("Relativ head (H/H_nom)");
		label_nominal.setForeground(Color.BLACK);
		label_actual = new JLabel("Relativ torque (M/M_nom)");
		label_actual.setForeground(Color.RED);

		bottompanel.add(label_nominal);
		bottompanel.add(label_actual);

		add("Center", drawing1);
		add("South", bottompanel);
		setSize(new Dimension(500, 520));
		setVisible(true);
	}

	public void update_curves() {

		drawing1.DeleteAll();

		Huj.removeAllElements();
		Muj.removeAllElements();
		Huj = computeH(slider.getValue(), x);
		Muj = computeM(slider.getValue(), x);

		// drawing1.addCurve(x, Hnom, Color.BLACK, Plotter.dashed);
		drawing1.addCurve(x, Huj, Color.BLACK, Plotter.dashed);
		// drawing1.addCurve(x, Mnom, Color.RED, Plotter.solid);
		drawing1.addCurve(x, Muj, Color.RED, Plotter.solid);

		drawing1.repaint();

	}

	public void stateChanged(ChangeEvent ce) {
		message.setText("RPM: " + slider.getValue() + " % ");
		update_curves();
	}

	public Vector<Double> computeH(double n, Vector<Double> x) {
		Vector<Double> H = new Vector<Double>();
		double a3 = 1.0e-7 * n - 3.0e-5;
		double a2 = -2.0e-7 * n * n + 4.0e-5 * n + 8.0e-4;
		double a1 = -2.0e-5 * n * n + 0.0019 * n - 0.3171;
		double a0 = 0.0081 * n * n + 0.4179 * n + 0.6277;
		for (int i = 0; i < x.size(); i++)
			H.add(a0 + a1 * x.elementAt(i) + a2 * x.elementAt(i)
					* x.elementAt(i) + a3 * x.elementAt(i) * x.elementAt(i)
					* x.elementAt(i));

		return H;
	}

	public Vector<Double> computeM(double n, Vector<Double> x) {
		Vector<Double> M = new Vector<Double>();
		double b3 = -1.0e-9 * n * n + 2.0e-7 * n - 3.0e-5;
		double b2 = -5.0e-7 * n * n + 7.0e-5 * n + 0.0017;
		double b1 = -0.0000328 * n * n + 0.008054 * n - 0.3295;
		double b0 = 0.000381 * n * n + 0.6024 * n + 2.572;
		for (int i = 0; i < x.size(); i++)
			M.add(b0 + b1 * x.elementAt(i) + b2 * x.elementAt(i)
					* x.elementAt(i) + b3 * x.elementAt(i) * x.elementAt(i)
					* x.elementAt(i));
		return M;
	}
}