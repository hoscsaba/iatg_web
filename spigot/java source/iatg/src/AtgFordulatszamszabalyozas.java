import java.awt.*;
import java.applet.Applet;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.Hashtable;
import java.util.Vector;

@SuppressWarnings({ "serial", "deprecation", "unused" })
public class AtgFordulatszamszabalyozas extends JFrame implements
		ChangeListener {
	private JSlider slider;
	private JPanel controlpanel;
	private JPanel bottompanel;
	private JLabel label_Qk;
	private JLabel label_Qs;
	private JLabel label_nnevl;
	private JLabel label_nuj;

	private Plotter drawing1;
	double xMin = 0.0, xMax = 250;

	private JLabel message;
	Vector<Double> x;
	Vector<Double> yjg;
	Vector<Double> yjguj;
	Vector<Double> yaffin;
	Vector<Double> yrendszer;
	double nnevl = 1470;

	public static void main(String[] args) {
		AtgFordulatszamszabalyozas app = new AtgFordulatszamszabalyozas();
        app.init();
    }
	
	public void init() {
		int N = 100;
		double dx = (xMax - xMin) / (N - 1);
		x = new Vector<Double>();
		yjg = new Vector<Double>();
		yjguj = new Vector<Double>();
		yaffin = new Vector<Double>();
		yrendszer = new Vector<Double>();
		double Q;
		for (int i = 0; i < N; i++) {
			Q = i * dx;
			x.addElement(Q);
			yjg.addElement(-0.0004 * Q * Q + 0.04 * Q + 24);
			yrendszer.addElement(10 + 0.0003 * Q * Q);
		}

		slider = new JSlider(JSlider.HORIZONTAL, 80, 240, 160);
		// Create the label table.
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		// PENDING: could use images, but we don't have any good ones.
		labelTable.put(new Integer(80), new JLabel("80 m3/h"));
		// new JLabel(createImageIcon("images/stop.gif")) );
		labelTable.put(new Integer(160), new JLabel("160 m3/h"));
		labelTable.put(new Integer(240), new JLabel("240 m3/h"));
		// new JLabel(createImageIcon("images/fast.gif")) );
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		slider.addChangeListener(this);

		message = new JLabel("Kívánt térfogatáram: 160 m3/h", JLabel.CENTER);

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
		drawing1.setyMax(45);
		drawing1.setWidth(450);
		drawing1.setHeight(350);
		drawing1.setXlabel("Q [m3/h]");
		drawing1.setYlabel("H [m]");
		drawing1.setxTic(100);
		drawing1.setyTic(10);
		drawing1.setShowTicks(true);
		drawing1.setShowNumbers(true);
		drawing1.setxOffset(60);
		drawing1.setyOffset(40);
		drawing1.setTitle("Jelleggörbe");
		// drawing1.setShowTitle(true);

		bottompanel = new JPanel(new GridLayout(2, 2));

		label_Qk = new JLabel("Kívánt térfogatáram:  80  m3/h");
		label_Qk.setForeground(Color.BLUE);
		label_Qs = new JLabel("Segédpont          : 2 m3/h");
		label_Qs.setForeground(Color.RED);
		label_nnevl = new JLabel("Névleges fordulatszám: 1470/perc");
		label_nnevl.setForeground(Color.RED);
		label_nuj = new JLabel("Új fordulatszám        : 2/perc");
		label_nuj.setForeground(Color.DARK_GRAY);
		bottompanel.add(label_Qk);
		bottompanel.add(label_nnevl);
		bottompanel.add(label_Qs);
		bottompanel.add(label_nuj);

		update_curves();

		add("Center", drawing1);
		add("South", bottompanel);
		setSize(new Dimension(500, 550));
		setVisible(true);

	}

	public void update_curves() {

		drawing1.DeleteAll();

		yjguj.removeAllElements();
		yaffin.removeAllElements();

		double Qk = (double) slider.getValue();
		double Hk = 10 + 0.0003 * Qk * Qk;
		double beta = (10 + 0.0003 * Qk * Qk) / Qk / Qk;
		double Qs = (0.04 + Math.sqrt(0.04 * 0.04 + 4 * 24 * (beta + 0.0004)))
				/ (2 * (beta + 0.0004));
		double Hs = 24 + 0.04 * Qs - 0.0004 * Qs * Qs;
		double nuj = nnevl * Qk / Qs;
		double Q;
		for (int i = 0; i < x.size(); i++) {
			Q = x.elementAt(i);
			yaffin.addElement(beta * Q * Q);
			yjguj.addElement(24 * (nuj / nnevl) * (nuj / nnevl) + 0.04
					* (nuj / nnevl) * Q - 0.0004 * Q * Q);
		}
		drawing1.addCurve(x, yjg, Color.RED, Plotter.solid);
		drawing1.addCurve(x, yrendszer, Color.BLUE, Plotter.solid);
		drawing1.addCurve(x, yaffin, Color.BLACK, Plotter.solid);
		drawing1.addCurve(x, yjguj, Color.DARK_GRAY, Plotter.solid);
		drawing1.addCircle(Qk, Hk, Color.BLUE);
		drawing1.addCircle(Qk, 0, Color.BLUE);
		drawing1.addCircle(Qs, Hs, Color.RED);

		drawing1.repaint();

		label_Qk.setText("Kívánt térfogatáram          : " + Qk + " m3/h");
		label_Qs.setText("Segédpont térfogatáram : " + Math.round(Qs) + " m3/h");
		label_nnevl.setText("Névleges fordulatszám : 1470/perc");
		label_nuj.setText("Új fordulatszám              : " + Math.round(nuj)
				+ "/perc");

	}

	public void stateChanged(ChangeEvent ce) {

		message.setText("Kívánt térfogatáram: " + slider.getValue() + " m3/h ");

		update_curves();
	}
}
