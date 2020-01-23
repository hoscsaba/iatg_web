import java.applet.Applet;
import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings({ "serial", "deprecation", "unused" })
public class AtgKepminta extends JFrame implements ChangeListener {
	ShowImage ShowImage_right;
	ShowImage ShowImage_left;

	Vector<String> imagenames_left;
	Vector<String> imagenames_right;

	private JSlider slider;
	private JPanel controlpanel;
	private JPanel drawpanel;

	private JPanel drawing_bal;
	private JPanel drawing_jobb;

	private JLabel message;

	int num_of_images;

	public static void main(String[] args) {
		AtgKepminta app = new AtgKepminta();
        app.init();
    }
	
	// Create a constructor method
	public void init() {

		imagenames_left = new Vector<String>();
		imagenames_left.addElement("fishing.gif");
		imagenames_left.addElement("org_father_figure.gif");
		imagenames_left.addElement("stick_figure.gif");

		imagenames_right = new Vector<String>();
		imagenames_right.addElement("stick_figure.gif");
		imagenames_right.addElement("fishing.gif");
		imagenames_right.addElement("org_father_figure.gif");

		num_of_images = imagenames_right.size();

		slider = new JSlider(JSlider.HORIZONTAL, 1, num_of_images, 1);
		// Create the label table.
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = 1; i <= num_of_images; i++) {
			labelTable.put(new Integer(i), new JLabel(i + " "));
		}

		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		slider.addChangeListener(this);

		message = new JLabel("Elõperdület lapátrács szög: 90 fok",
				JLabel.CENTER);
		controlpanel = new JPanel();
		controlpanel.setLayout(new GridLayout(0, 1));

		controlpanel.add(message);
		controlpanel.add(slider);

		setLayout(new BorderLayout());
		add("North", controlpanel);

		ShowImage_left = new ShowImage(imagenames_left);
		ShowImage_right = new ShowImage(imagenames_right);

		// Minden osszerakunk es rajzolunk.
		drawpanel = new JPanel();
		drawpanel.setLayout(new BoxLayout(drawpanel, BoxLayout.X_AXIS));
		drawpanel.add(ShowImage_left);
		drawpanel.add(ShowImage_right);

		update();

		add("Center", drawpanel);
		setSize(new Dimension(620, 400));
		setVisible(true);
	}

	public void update() {
		int fig_num = slider.getValue() - 1;
		ShowImage_left.setAkt_fig_num(fig_num);
		ShowImage_right.setAkt_fig_num(fig_num);
		ShowImage_left.repaint();
		ShowImage_right.repaint();
		message.setText("Kép: " + (fig_num + 1));
	}

	public void stateChanged(ChangeEvent ce) {
		update();
	}
}