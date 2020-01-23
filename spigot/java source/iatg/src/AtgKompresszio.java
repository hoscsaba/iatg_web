import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
//import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.Hashtable;
import java.util.Vector;

@SuppressWarnings({ "serial", "deprecation", "unused" })
public class AtgKompresszio extends JFrame implements ChangeListener {
	//private JPanel mainPanel;
    private JSlider p_slider;
    private JSlider n_slider;
    private JPanel controlpanel;
    private JPanel bottompanel;
    private JLabel label_kiindulo;
    private JLabel label_izentrop;
    private JLabel label_politrop;
    private JLabel label_eleje;

    private JLabel label_eleje_p = new JLabel("p [bar]", JLabel.CENTER);
    private JLabel label_eleje_t = new JLabel("T [C]", JLabel.CENTER);
    private JLabel label_eleje_h = new JLabel("h [kJ/kg]", JLabel.CENTER);
    private JLabel label_eleje_s = new JLabel("s [kJ/kg]", JLabel.CENTER);
    private JLabel label_eleje_y = new JLabel("Y [kJ/kg]", JLabel.CENTER);

    private JLabel label_p1 = new JLabel("", JLabel.CENTER);
    private JLabel label_t1 = new JLabel("", JLabel.CENTER);
    private JLabel label_h1 = new JLabel("", JLabel.CENTER);
    private JLabel label_s1 = new JLabel("", JLabel.CENTER);
    private JLabel label_y1 = new JLabel("", JLabel.CENTER);

    private JLabel label_p2s = new JLabel("", JLabel.CENTER);
    private JLabel label_t2s = new JLabel("", JLabel.CENTER);
    private JLabel label_h2s = new JLabel("", JLabel.CENTER);
    private JLabel label_s2s = new JLabel("", JLabel.CENTER);
    private JLabel label_y2s = new JLabel("", JLabel.CENTER);

    private JLabel label_p2 = new JLabel("", JLabel.CENTER);
    private JLabel label_t2 = new JLabel("", JLabel.CENTER);
    private JLabel label_h2 = new JLabel("", JLabel.CENTER);
    private JLabel label_s2 = new JLabel("", JLabel.CENTER);
    private JLabel label_y2 = new JLabel("", JLabel.CENTER);

    private Plotter drawing1;
    double sMin = -0.51, sMax = 0.31;

    private JLabel p_message;
    private JLabel n_message;
    Vector<Double> s;
    Vector<Double> h1, h2, h3, h4, h5, h6;
    double ncs;

    public static void main(String[] args) {
        AtgKompresszio app = new AtgKompresszio();
        app.init();
    }

    public void init() {
        int N = 100;
        double ds = (sMax - sMin) / (N - 1);
        s = new Vector<Double>();
        for (int i = 0; i < N; i++)
            s.addElement(sMin + i * ds);

        h1 = computehx(1.0, s);
        h2 = computehx(2.0, s);
        h3 = computehx(3.0, s);
        h4 = computehx(4.0, s);
        h5 = computehx(5.0, s);
        h6 = computehx(6.0, s);

        // Kompresszioviszony slider
        p_slider = new JSlider(JSlider.HORIZONTAL, 10, 60, 30);
        Hashtable<Integer, JLabel> p_labelTable = new Hashtable<Integer, JLabel>();
        p_labelTable.put(new Integer(10), new JLabel("1 bar"));
        p_labelTable.put(new Integer(60), new JLabel("6 bar"));

        // new JLabel(createImageIcon("images/fast.gif")) );
        p_slider.setLabelTable(p_labelTable);
        p_slider.setPaintLabels(true);
        p_slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        p_slider.addChangeListener(this);

        // Politropikus kitevo slider slider
        n_slider = new JSlider(JSlider.HORIZONTAL, 100, 160, 145);
        Hashtable<Integer, JLabel> n_labelTable = new Hashtable<Integer, JLabel>();
        n_labelTable.put(new Integer(100), new JLabel("1 (izoterm)"));
        n_labelTable.put(new Integer(140), new JLabel("1.4 (izentrópikus)"));
        n_labelTable.put(new Integer(160), new JLabel("1.6"));

        n_slider.setLabelTable(n_labelTable);
        n_slider.setPaintLabels(true);
        n_slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        n_slider.addChangeListener(this);

        p_message = new JLabel("", JLabel.CENTER);
        n_message = new JLabel("", JLabel.CENTER);

        controlpanel = new JPanel();
        controlpanel.setLayout(new GridLayout(2, 2, 10, 10));

        controlpanel.add(p_message);
        controlpanel.add(n_message);
        controlpanel.add(p_slider);
        controlpanel.add(n_slider);

        //mainPanel = new JPanel(new BorderLayout());
        setLayout(new BorderLayout());
        add("North", controlpanel);

        drawing1 = new Plotter();
        drawing1.setxMin(sMin);
        drawing1.setxMax(sMax);
        drawing1.setyMin(0);
        drawing1.setyMax(350);
        drawing1.setWidth(600);
        drawing1.setHeight(400);
        drawing1.setXlabel("s [kJ/kgK]");
        drawing1.setYlabel("h [kJ/kg]");
        drawing1.setxTic(0.1);
        drawing1.setyTic(100);
        drawing1.setShowTicks(true);
        drawing1.setShowNumbers(true);
        drawing1.setxOffset(60);
        drawing1.setyOffset(40);

        bottompanel = new JPanel(new GridLayout(6, 4));

        label_eleje = new JLabel("");
        label_eleje.setForeground(Color.BLACK);
        label_kiindulo = new JLabel("Kiinduló állapot", JLabel.CENTER);
        label_kiindulo.setForeground(Color.BLACK);
        label_izentrop = new JLabel("Izentrópikus", JLabel.CENTER);
        label_izentrop.setForeground(Color.RED);
        label_politrop = new JLabel("Politrópikus", JLabel.CENTER);
        label_politrop.setForeground(Color.BLUE);

        bottompanel.add(label_eleje);
        bottompanel.add(label_kiindulo);
        bottompanel.add(label_izentrop);
        bottompanel.add(label_politrop);

        bottompanel.add(label_eleje_p);
        bottompanel.add(label_p1);
        bottompanel.add(label_p2s);
        bottompanel.add(label_p2);

        bottompanel.add(label_eleje_t);
        bottompanel.add(label_t1);
        bottompanel.add(label_t2s);
        bottompanel.add(label_t2);

        bottompanel.add(label_eleje_h);
        bottompanel.add(label_h1);
        bottompanel.add(label_h2s);
        bottompanel.add(label_h2);

        bottompanel.add(label_eleje_s);
        bottompanel.add(label_s1);
        bottompanel.add(label_s2s);
        bottompanel.add(label_s2);

        bottompanel.add(label_eleje_y);
        bottompanel.add(label_y1);
        bottompanel.add(label_y2s);
        bottompanel.add(label_y2);


        update_curves();

       add("Center", drawing1);
       add("South", bottompanel);
       setSize(new Dimension(650, 700));
       setVisible(true);
//       mainPanel.setVis;
    }

    public void update_curves() {

        double p = (double) p_slider.getValue() / 10;
        double n = (double) n_slider.getValue() / 100;

        p_message.setText("Kompresszióviszony (p2/p1): " + p);
        n_message.setText("Politrópikus kitevõ (n): " + n);

        drawing1.DeleteAll();

        drawing1.addCurve(s, h1, Color.BLACK, Plotter.solid);
        drawing1.addCurve(s, h2, Color.BLACK, Plotter.solid);
        drawing1.addCurve(s, h3, Color.BLACK, Plotter.solid);
        drawing1.addCurve(s, h4, Color.BLACK, Plotter.solid);
        drawing1.addCurve(s, h5, Color.BLACK, Plotter.solid);
        drawing1.addCurve(s, h6, Color.BLACK, Plotter.solid);

        Vector<Double> x_izentr = new Vector<Double>();
        Vector<Double> y_izentr = new Vector<Double>();
        Vector<Double> x_poly = new Vector<Double>();
        Vector<Double> y_poly = new Vector<Double>();

        double p_from = 1.0;
        double t_from = 25;
        double s_from = 0.088016;
        double h_from = computehx(1.0, s_from);

        double p_to = p_from * p;
        double t_to_s = (t_from + 273) * java.lang.Math.pow(p, (1.4 - 1) / 1.4)
                        - 273;
        double t_to = (t_from + 273) * java.lang.Math.pow(p, (n - 1) / n) - 273;

        double h_to_s = computehx(p, s_from);
        double T2pT1 = java.lang.Math.pow(p, (n - 1) / n);
        double ds = 1.0045 * java.lang.Math.log(T2pT1) - 0.287
                    * java.lang.Math.log(p);
        double s_to_poly = s_from + ds;
        double h_to = computehx(p, s_to_poly);

        double w_fajl_izentr = 1.4/(1.4 - 1)*287*(t_to_s-t_from)/1000;
        double w_fajl_poly;
        if (n>1.01) 
        	w_fajl_poly= n/(n - 1)*287*(t_to-t_from)/1000;
        else
        	w_fajl_poly=287*(t_from+273)*java.lang.Math.log(p_to/p_from)/1000;
        // System.out.println("\nds=" + ds);

        x_izentr.addElement(s_from);
        x_izentr.addElement(s_from);
        y_izentr.addElement(h_from);
        y_izentr.addElement(h_to_s);
        drawing1.addCurve(x_izentr, y_izentr, Color.RED, Plotter.solid);
        drawing1.addCircle(s_from, h_from, Color.RED);
        drawing1.addCircle(s_from, h_to_s, Color.RED);

        x_poly.addElement(s_from);
        x_poly.addElement(s_to_poly);
        y_poly.addElement(h_from);
        y_poly.addElement(h_to);
        drawing1.addCurve(x_poly, y_poly, Color.BLUE, Plotter.solid);
        drawing1.addCircle(s_from, h_from, Color.BLACK);
        drawing1.addCircle(s_to_poly, h_to, Color.BLUE);

        drawing1.repaint();

        label_p1.setText("" + p_from);
        label_p2s.setText("" + p_to);
        label_p2.setText("" + p_to);

        label_t1.setText(RoundToDigits(t_from, 1));
        label_t2s.setText(RoundToDigits(t_to_s, 1));
        label_t2.setText(RoundToDigits(t_to, 1));

        label_h1.setText(RoundToDigits(h_from, 1));
        label_h2s.setText(RoundToDigits(h_to_s, 1));
        label_h2.setText(RoundToDigits(h_to, 1));

        label_s1.setText(RoundToDigits(s_from, 3));
        label_s2s.setText(RoundToDigits(s_from, 3));
        label_s2.setText(RoundToDigits(s_to_poly, 3));

        label_y1.setText("-");
        label_y2s.setText(RoundToDigits(w_fajl_izentr, 1));
        label_y2.setText(RoundToDigits(w_fajl_poly, 1));
    }

    public void stateChanged(ChangeEvent ce) {
        p_message.setText("Fordulatszám: " + p_slider.getValue() + " % ");
        update_curves();
    }

    public Vector<Double> computehx(double x, Vector<Double> s) {
        Vector<Double> hx = new Vector<Double>();
        for (int i = 0; i < s.size(); i++)
            hx.add(273 * 1.0045 * (java.lang.Math.exp(s.elementAt(i) / 1.0045)
                                   * java.lang.Math.pow(x, 0.287 / 1.0045) - 1));

        return hx;
    }

    public double computehx(double x, double s) {
        return 273 * 1.0045 * (java.lang.Math.exp(s / 1.0045)
                               * java.lang.Math.pow(x, 0.287 / 1.0045) - 1);
    }

    public String RoundToDigits(double x, int digits) {
        String s = "";
        if (digits == 1)
            s += (double) java.lang.Math.round(10 * x) / 10;
        if (digits == 2)
            s += (double) java.lang.Math.round(100 * x) / 100;
        if (digits == 3)
            s += (double) java.lang.Math.round(1000 * x) / 1000;

        return s;
    }
}
