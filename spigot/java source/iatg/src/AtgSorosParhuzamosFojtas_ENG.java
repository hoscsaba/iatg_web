import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
public class AtgSorosParhuzamosFojtas_ENG extends JFrame implements ChangeListener {
	private JSlider slider_Q;
	private JSlider slider_dp;
	private JPanel slider_panel;
	private JPanel left_inputpanel;
	private JPanel right_inputpanel;
	private JPanel input_panel;
	// private JPanel slider_text_panel;
	// private JPanel slider_slider_panel;
	private JLabel message_Q;
	private JLabel message_dp;
	private Color OriginalSliderColor;

	private JPanel controlpanel;
	private JPanel drawpanel;
	private JPanel bottompanel;

	private Plotter drawing1;
	double xMin = 0.0, xMax = 1.2;
	double yMin = 0.0, yMax = 120;

	private Plotter drawing2;

	Vector<Double> Qjg;
	Vector<Double> dpjg;
	Vector<Double> Qsoros;
	Vector<Double> dpsoros;
	Vector<Double> Qparh;
	Vector<Double> dpparh;

	JLabel label_Vgsz;
	JLabel label_nsz;
	JLabel label_Qsz;

	JLabel label_Vgm;
	JLabel label_nm;
	JLabel label_Qm;
	JTextField label_Vgsz_val;
	JTextField label_nsz_val;
	JTextField label_Qsz_val;
	JTextField label_Vgm_val;
	JTextField label_nm_val;
	JTextField label_Qm_val;

	JLabel label_soros_Mm;
	JTextField label_soros_Mm_val;
	JLabel label_soros_Ph;
	JTextField label_soros_Ph_val;
	JLabel label_parh_Mm;
	JTextField label_parh_Mm_val;
	JLabel label_parh_Ph;
	JTextField label_parh_Ph_val;

	JLabel label_soros_Qte;
	JLabel label_soros_dpte;
	JLabel label_soros_Msz;
	JLabel label_soros_Pbe;
	JLabel label_soros_Pnyh;
	JLabel label_soros_Pf;
	JLabel label_soros_eta;

	JTextField label_soros_Qte_val;
	JTextField label_soros_dpte_val;
	JTextField label_soros_Msz_val;
	JTextField label_soros_Pbe_val;
	JTextField label_soros_Pnyh_val;
	JTextField label_soros_Pf_val;
	JTextField label_soros_eta_val;

	JLabel label_parh_Qte;
	JLabel label_parh_dpte;
	JLabel label_parh_Msz;
	JLabel label_parh_Pbe;
	JLabel label_parh_Pnyh;
	JLabel label_parh_Pf;
	JLabel label_parh_eta;

	JTextField label_parh_Qte_val;
	JTextField label_parh_dpte_val;
	JTextField label_parh_Msz_val;
	JTextField label_parh_Pbe_val;
	JTextField label_parh_Pnyh_val;
	JTextField label_parh_Pf_val;
	JTextField label_parh_eta_val;

	public static void main(String[] args) {
		AtgSorosParhuzamosFojtas_ENG app = new AtgSorosParhuzamosFojtas_ENG();
        app.init();
    }
	
	public void init() {
		int N = 100;
		double dx = (1.0 - xMin) / (N - 1);
		Qjg = new Vector<Double>();
		dpjg = new Vector<Double>();
		Qsoros = new Vector<Double>();
		dpsoros = new Vector<Double>();
		Qparh = new Vector<Double>();
		dpparh = new Vector<Double>();

		double QQ;
		for (int i = 0; i < N; i++) {
			QQ = i * dx;
			Qjg.addElement(QQ);
			dpjg.addElement(tapegysegjg(QQ));
		}

		slider_Q = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		// Create the label table.
		Hashtable<Integer, JLabel> labelTable_Q = new Hashtable<Integer, JLabel>();
		labelTable_Q.put(new Integer(0), new JLabel("0"));
		labelTable_Q.put(new Integer(50), new JLabel("0.5"));
		labelTable_Q.put(new Integer(100), new JLabel("1.0"));
		slider_Q.setLabelTable(labelTable_Q);
		slider_Q.setPaintLabels(true);
		slider_Q.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		slider_Q.addChangeListener(this);
		OriginalSliderColor = slider_Q.getBackground();

		message_Q = new JLabel("", JLabel.CENTER);
		message_Q
				.setText("Q/Q pump geo.: " + slider_Q.getValue() / 100 + " [-]");

		slider_dp = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		// Create the label table.
		Hashtable<Integer, JLabel> labelTable_dp = new Hashtable<Integer, JLabel>();
		labelTable_dp.put(new Integer(0), new JLabel("0"));
		labelTable_dp.put(new Integer(50), new JLabel("50"));
		labelTable_dp.put(new Integer(100), new JLabel("100"));
		slider_dp.setLabelTable(labelTable_dp);
		slider_dp.setPaintLabels(true);
		slider_dp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		slider_dp.addChangeListener(this);

		message_dp = new JLabel("", JLabel.CENTER);
		message_dp.setText("Pressure: " + slider_dp.getValue() + " [bar]");

		label_Vgsz = new JLabel("Vg pump [cm^3] : ", JLabel.CENTER);
		label_Vgsz.setForeground(Color.BLACK);
		label_nsz = new JLabel("n pump [rot./min] :", JLabel.CENTER);
		label_nsz.setForeground(Color.BLACK);
		label_Qsz = new JLabel("Q pump,geo [liter/min] :", JLabel.CENTER);
		label_Qsz.setForeground(Color.BLACK);

		label_Vgm = new JLabel("Vg motor [cm^3/rot.] : ", JLabel.CENTER);
		label_Vgm.setForeground(Color.BLUE);
		label_nm = new JLabel("n motor [rot./min] :", JLabel.CENTER);
		label_nm.setForeground(Color.BLUE);
		label_Qm = new JLabel("Q motor [liter/min] :", JLabel.CENTER);
		label_Qm.setForeground(Color.BLUE);

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				update_curves();
			}
		};

		FocusListener focusListener = new FocusListener() {
			public void focusGained(FocusEvent focusEvent) {
				update_curves();
			}

			public void focusLost(FocusEvent focusEvent) {
				update_curves();
			}
		};

		label_Vgsz_val = new JTextField("8");
		label_Vgsz_val.addActionListener(actionListener);
		label_Vgsz_val.addFocusListener(focusListener);
		label_nsz_val = new JTextField("1200");
		label_nsz_val.addActionListener(actionListener);
		label_nsz_val.addFocusListener(focusListener);
		label_Qsz_val = new JTextField("8");
		label_Qsz_val.setEditable(false);

		label_Vgm_val = new JTextField("8");
		label_Vgm_val.addActionListener(actionListener);
		label_Vgm_val.addFocusListener(focusListener);
		label_nm_val = new JTextField("8");
		label_nm_val.setEditable(false);
		label_Qm_val = new JTextField("8");
		label_Qm_val.setEditable(false);

		left_inputpanel = new JPanel(new GridLayout(3, 2));
		left_inputpanel.add(label_Vgsz);
		left_inputpanel.add(label_Vgsz_val);
		left_inputpanel.add(label_nsz);
		left_inputpanel.add(label_nsz_val);
		left_inputpanel.add(label_Qsz);
		left_inputpanel.add(label_Qsz_val);

		right_inputpanel = new JPanel(new GridLayout(3, 2));
		right_inputpanel.add(label_Vgm);
		right_inputpanel.add(label_Vgm_val);
		right_inputpanel.add(label_nm);
		right_inputpanel.add(label_nm_val);
		right_inputpanel.add(label_Qm);
		right_inputpanel.add(label_Qm_val);

		slider_panel = new JPanel();
		slider_panel.setLayout(new GridLayout(2, 2));

		slider_panel.add(message_Q);
		slider_panel.add(slider_Q);
		slider_panel.add(message_dp);
		slider_panel.add(slider_dp);

		controlpanel = new JPanel(new GridLayout(2, 2));
		input_panel = new JPanel(new GridLayout(1, 2));
		input_panel.add(left_inputpanel);
		input_panel.add(right_inputpanel);

		controlpanel.add(slider_panel);
		controlpanel.add(input_panel);

		setLayout(new BorderLayout());
		add("North", controlpanel);

		drawing1 = new Plotter();
		drawing1.setxMin(xMin);
		drawing1.setxMax(xMax);
		drawing1.setyMin(yMin);
		drawing1.setyMax(yMax);
		drawing1.setWidth(300);
		drawing1.setHeight(200);
		drawing1.setXlabel("Q/Qsg [-]");
		drawing1.setYlabel("dp [bar]");
		drawing1.setxTic(0.2);
		drawing1.setyTic(25);
		drawing1.setShowTicks(true);
		drawing1.setShowNumbers(true);
		drawing1.setxOffset(60);
		drawing1.setyOffset(40);
		drawing1.setTitle("Serial operation");
		drawing1.setShowTitle(true);

		drawing2 = new Plotter();
		drawing2.setxMin(xMin);
		drawing2.setxMax(xMax);
		drawing2.setyMin(yMin);
		drawing2.setyMax(yMax);
		drawing2.setWidth(300);
		drawing2.setHeight(200);
		drawing2.setXlabel("Q/Qsg [-]");
		drawing2.setYlabel("dp [bar]");
		drawing2.setxTic(0.2);
		drawing2.setyTic(25);
		drawing2.setShowTicks(true);
		drawing2.setShowNumbers(true);
		drawing2.setxOffset(60);
		drawing2.setyOffset(40);
		drawing2.setTitle("Parallel operation");
		drawing2.setShowTitle(true);

		// Minden osszerakunk es rajzolunk.
		drawpanel = new JPanel();
		drawpanel.setLayout(new GridLayout(1, 2, 10, 10));
		drawpanel.add(drawing1);
		drawpanel.add(drawing2);

		add("Center", drawpanel);

		bottompanel = new JPanel(new GridLayout(6, 4));

		label_soros_Mm = new JLabel("M motor. [Nm] :", JLabel.CENTER);
		label_soros_Mm.setForeground(Color.BLUE);
		label_soros_Ph = new JLabel("P effective (motor) [W] :", JLabel.CENTER);
		label_soros_Ph.setForeground(Color.BLUE);
		label_soros_Mm_val = new JTextField("8");
		label_soros_Mm_val.setEditable(false);
		label_soros_Ph_val = new JTextField("8");
		label_soros_Ph_val.setEditable(false);

		label_soros_Pbe = new JLabel("P in (pump) [W] :", JLabel.CENTER);
		label_soros_Pbe.setForeground(Color.GREEN);
		label_soros_Pbe_val = new JTextField("8");
		label_soros_Pbe_val.setEditable(false);

		label_soros_Pnyh = new JLabel("P pressure relief valve [W] :", JLabel.CENTER);
		label_soros_Pnyh.setForeground(Color.BLACK);
		label_soros_Pnyh_val = new JTextField("8");
		label_soros_Pnyh_val.setEditable(false);

		label_soros_Pf = new JLabel("P throttling [W] :", JLabel.CENTER);
		label_soros_Pf.setForeground(Color.RED);
		label_soros_Pf_val = new JTextField("8");
		label_soros_Pf_val.setEditable(false);

		label_soros_eta = new JLabel("efficiency [%] :", JLabel.CENTER);
		label_soros_eta.setForeground(Color.BLACK);
		label_soros_eta_val = new JTextField("8");
		label_soros_eta_val.setEditable(false);

		label_parh_Mm = new JLabel("M motor. [Nm] :", JLabel.CENTER);
		label_parh_Mm.setForeground(Color.BLUE);
		label_parh_Ph = new JLabel("P effective (motor) [W] :", JLabel.CENTER);
		label_parh_Ph.setForeground(Color.BLUE);
		label_parh_Mm_val = new JTextField("8");
		label_parh_Mm_val.setEditable(false);
		label_parh_Ph_val = new JTextField("8");
		label_parh_Ph_val.setEditable(false);

		label_parh_Pbe = new JLabel("P in (pump) [W] :", JLabel.CENTER);
		label_parh_Pbe.setForeground(Color.GREEN);
		label_parh_Pbe_val = new JTextField("8");
		label_parh_Pbe_val.setEditable(false);

		label_parh_Pnyh = new JLabel("P pressure relief valve [W] :", JLabel.CENTER);
		label_parh_Pnyh.setForeground(Color.BLACK);
		label_parh_Pnyh_val = new JTextField("8");
		label_parh_Pnyh_val.setEditable(false);

		label_parh_Pf = new JLabel("P throttling [W] :", JLabel.CENTER);
		label_parh_Pf.setForeground(Color.RED);
		label_parh_Pf_val = new JTextField("8");
		label_parh_Pf_val.setEditable(false);

		label_parh_eta = new JLabel("efficiency [%] :", JLabel.CENTER);
		label_parh_eta.setForeground(Color.BLACK);
		label_parh_eta_val = new JTextField("8");
		label_parh_eta_val.setEditable(false);

		bottompanel.add(label_soros_Mm);
		bottompanel.add(label_soros_Mm_val);
		bottompanel.add(label_parh_Mm);
		bottompanel.add(label_parh_Mm_val);
		bottompanel.add(label_soros_Ph);
		bottompanel.add(label_soros_Ph_val);
		bottompanel.add(label_parh_Ph);
		bottompanel.add(label_parh_Ph_val);
		bottompanel.add(label_soros_Pbe);
		bottompanel.add(label_soros_Pbe_val);
		bottompanel.add(label_parh_Pbe);
		bottompanel.add(label_parh_Pbe_val);

		bottompanel.add(label_soros_Pnyh);
		bottompanel.add(label_soros_Pnyh_val);
		bottompanel.add(label_parh_Pnyh);
		bottompanel.add(label_parh_Pnyh_val);

		bottompanel.add(label_soros_Pf);
		bottompanel.add(label_soros_Pf_val);
		bottompanel.add(label_parh_Pf);
		bottompanel.add(label_parh_Pf_val);

		bottompanel.add(label_soros_eta);
		bottompanel.add(label_soros_eta_val);
		bottompanel.add(label_parh_eta);
		bottompanel.add(label_parh_eta_val);

		add("South", bottompanel);

		update_curves();

		setSize(new Dimension(700, 550));
		setVisible(true);
	}

	public void update_curves() {

		drawing1.DeleteAll();
		drawing2.DeleteAll();

		double Qakt = (double) slider_Q.getValue() / 100.;
		double dpakt = (double) slider_dp.getValue();

		if (dpakt > tapegysegjg(Qakt))
			slider_dp.setBackground(Color.RED);
		else
			slider_dp.setBackground(OriginalSliderColor);

		if (Qakt > inverztapegysegjg(dpakt))
			slider_Q.setBackground(Color.RED);
		else
			slider_Q.setBackground(OriginalSliderColor);

		double Qte_soros = Qakt;
		double Qte_parh = inverztapegysegjg(dpakt);
		double dpte_soros = tapegysegjg(Qakt);
		double dpte_parh = dpakt;
		double Qsz_soros, Qsz_parh, Qnyh_soros, Qnyh_parh, dpnyh_soros, dpnyh_parh;
		if (dpte_soros < 70)
			Qsz_soros = Qte_soros;
		else
			Qsz_soros = inverztapegysegjg(70);
		if (dpte_parh < 70)
			Qsz_parh = Qte_parh;
		else
			Qsz_parh = inverztapegysegjg(70);
		Qnyh_soros = Qsz_soros - Qte_soros;
		Qnyh_parh = Qsz_parh - Qte_parh;
		if (dpte_soros < 70)
			dpnyh_soros = 0;
		else
			dpnyh_soros = tapegysegjg(Qakt) - dpakt;
		dpnyh_parh = dpakt;

		Vector<Double> Qnyit = new Vector<Double>();
		Vector<Double> dpnyit = new Vector<Double>();
		Qnyit.addElement(0.);
		Qnyit.addElement(inverztapegysegjg(70.));
		dpnyit.addElement(70.);
		dpnyit.addElement(70.);

		drawing1.addCurve(Qjg, dpjg, Color.BLACK, Plotter.solid);
		drawing2.addCurve(Qjg, dpjg, Color.BLACK, Plotter.solid);
		drawing1.addCurve(Qnyit, dpnyit, Color.WHITE, Plotter.solid);
		drawing2.addCurve(Qnyit, dpnyit, Color.WHITE, Plotter.solid);
		drawing1.addCircle(Qakt, dpakt, Color.BLUE);
		drawing2.addCircle(Qakt, dpakt, Color.BLUE);
		drawing1.addCircle(Qakt, tapegysegjg(Qakt), Color.BLACK);
		drawing2.addCircle(inverztapegysegjg(dpakt), dpakt, Color.BLACK);
		drawing1.addCircle(Qsz_soros, dpte_soros, Color.GREEN);
		drawing2.addCircle(Qsz_parh, dpte_parh, Color.GREEN);

		// Soros param�terei
		double B = (tapegysegjg(Qakt) - dpakt) / Qakt / Qakt;
		Qsoros.removeAllElements();
		dpsoros.removeAllElements();
		for (int i = 0; i < Qjg.size(); i++) {
			Qsoros.addElement(Qjg.elementAt(i));
			dpsoros.addElement(dpakt + B * Qjg.elementAt(i) * Qjg.elementAt(i));
		}
		drawing1.addCurve(Qsoros, dpsoros, Color.RED, Plotter.solid);

		// P�rhuzamos param�terei
		B = dpakt / (inverztapegysegjg(dpakt) - Qakt)
				/ (inverztapegysegjg(dpakt) - Qakt);
		Qparh.removeAllElements();
		dpparh.removeAllElements();
		for (int i = 0; i < Qjg.size(); i++) {
			if (Qjg.elementAt(i) > Qakt) {
				Qparh.addElement(Qjg.elementAt(i));
				dpparh.addElement(B * (Qakt - Qjg.elementAt(i))
						* (Qakt - Qjg.elementAt(i)));
			}
		}
		drawing2.addCurve(Qparh, dpparh, Color.RED, Plotter.solid);

		// Segedvonalak
		Vector<Double> xs1 = new Vector<Double>();
		Vector<Double> ys1 = new Vector<Double>();
		Vector<Double> xs2 = new Vector<Double>();
		Vector<Double> ys2 = new Vector<Double>();
		Vector<Double> xs3 = new Vector<Double>();
		Vector<Double> ys3 = new Vector<Double>();
		xs1.addElement(0.0);
		xs1.addElement(Qakt);
		ys1.addElement(dpakt);
		ys1.addElement(dpakt);

		xs2.addElement(Qakt);
		xs2.addElement(Qakt);
		ys2.addElement(0.0);
		ys2.addElement(dpakt);

		xs3.addElement(Qakt);
		xs3.addElement(Qakt);
		ys3.addElement(dpakt);
		ys3.addElement(tapegysegjg(Qakt));
		drawing1.addCurve(xs1, ys1, Color.BLUE, Plotter.solid);
		drawing1.addCurve(xs2, ys2, Color.BLUE, Plotter.solid);
		drawing1.addCurve(xs3, ys3, Color.RED, Plotter.solid);

		Vector<Double> xp1 = new Vector<Double>();
		Vector<Double> yp1 = new Vector<Double>();
		Vector<Double> xp2 = new Vector<Double>();
		Vector<Double> yp2 = new Vector<Double>();
		Vector<Double> xp3 = new Vector<Double>();
		Vector<Double> yp3 = new Vector<Double>();
		xp1.addElement(0.0);
		xp1.addElement(Qakt);
		yp1.addElement(dpakt);
		yp1.addElement(dpakt);

		xp2.addElement(Qakt);
		xp2.addElement(inverztapegysegjg(dpakt));
		yp2.addElement(dpakt);
		yp2.addElement(dpakt);

		xp3.addElement(Qakt);
		xp3.addElement(Qakt);
		yp3.addElement(0.0);
		yp3.addElement(dpakt);
		drawing2.addCurve(xp1, yp1, Color.BLUE, Plotter.solid);
		drawing2.addCurve(xp2, yp2, Color.RED, Plotter.solid);
		drawing2.addCurve(xp3, yp3, Color.BLUE, Plotter.solid);

		drawing1.repaint();
		drawing2.repaint();

		double Vgsz = Double.parseDouble(label_Vgsz_val.getText());
		double Qsz = Vgsz * Double.parseDouble(label_nsz_val.getText()) / 1000.;
		double Qm = Qakt * Qsz;
		double Vgm = Double.parseDouble(label_Vgm_val.getText());
		double nm = Qm / Vgm * 1000.;
		double Mm = Vgm * dpakt / 2. / java.lang.Math.PI / 10.;

		double Ph = Qm / 60.e3 * dpakt * 1.e5;
		double Pbe_soros = Qsz_soros * Qsz / 60.e3 * tapegysegjg(Qakt) * 1.e5;
		double Pbe_parh = Qsz_parh * Qsz / 60.e3 * dpakt * 1.e5;
		double Pnyh_soros = Qnyh_soros * Qsz / 60.e3 * dpnyh_soros * 1.e5;
		double Pnyh_parh = Qnyh_parh * Qsz / 60.e3 * dpnyh_parh * 1.e5;
		double Pf_soros = Qakt * Qsz / 60.e3 * (tapegysegjg(Qakt) - dpakt)
				* 1.e5;
		double Pf_parh = (Qsz_parh - Qnyh_parh - Qakt) * Qsz / 60.e3 * dpakt
				* 1.e5;
		double eta_soros = Ph / Pbe_soros * 100.;
		double eta_parh = Ph / Pbe_parh * 100.;

		label_Qsz_val.setText(RoundToDigits(Qsz, 1));
		label_nm_val.setText(RoundToDigits(nm, 0));
		label_Qm_val.setText(RoundToDigits(Qm, 1));

		label_soros_Mm_val.setText(RoundToDigits(Mm, 1));
		label_parh_Mm_val.setText(RoundToDigits(Mm, 1));
		label_soros_Ph_val.setText(RoundToDigits(Ph, 1));
		label_parh_Ph_val.setText(RoundToDigits(Ph, 1));
		label_soros_Pbe_val.setText(RoundToDigits(Pbe_soros, 1));
		label_parh_Pbe_val.setText(RoundToDigits(Pbe_parh, 1));
		label_soros_Pnyh_val.setText(RoundToDigits(Pnyh_soros, 1));
		label_parh_Pnyh_val.setText(RoundToDigits(Pnyh_parh, 1));
		label_soros_Pf_val.setText(RoundToDigits(Pf_soros, 1));
		label_parh_Pf_val.setText(RoundToDigits(Pf_parh, 1));
		label_soros_eta_val.setText(RoundToDigits(eta_soros, 1));
		label_parh_eta_val.setText(RoundToDigits(eta_parh, 1));

	}

	public void stateChanged(ChangeEvent ce) {

		message_Q.setText("Q motor / Q pump geo.: " + slider_Q.getValue()
				/ 100. + " [-]");
		message_dp.setText("Pressure: " + slider_dp.getValue() + " [bar]");

		update_curves();
	}

	private double tapegysegjg(double QQ) {
		return 100 + 10 * java.lang.Math.log(1. - QQ
				* (1. - java.lang.Math.exp(-100. / 10.)));
	}

	private double inverztapegysegjg(double dp) {
		return (1. - java.lang.Math.exp((dp - 100.) / 10.))
				/ (1. - java.lang.Math.exp(-100. / 10.));
	}

	public String RoundToDigits(double x, int digits) {
		String s = "";
		if (digits == 0)
			s += (double) java.lang.Math.round(x);
		if (digits == 1)
			s += (double) java.lang.Math.round(10 * x) / 10;
		if (digits == 2)
			s += (double) java.lang.Math.round(100 * x) / 100;
		if (digits == 3)
			s += (double) java.lang.Math.round(1000 * x) / 1000;

		return s;
	}
}
