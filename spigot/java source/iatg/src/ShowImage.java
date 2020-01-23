import java.awt.*;
import java.util.Vector;

import javax.swing.*;

@SuppressWarnings("serial")
public class ShowImage extends JPanel {
	Vector<Image> images; // Declare a name for our Image object.
	int akt_fig_num;

	public void setAkt_fig_num(int akt_fig_num) {
		this.akt_fig_num = akt_fig_num;
	}

	// Create a constructor method
	public ShowImage(Vector<String> imagenames) {
		super();

		images = new Vector<Image>();
		for (int i = 0; i < imagenames.size(); i++) {
			System.out.println("\n Loading " + imagenames.elementAt(i));

			images.addElement(Toolkit.getDefaultToolkit().getImage(
					imagenames.elementAt(i)));
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.print("\n MEGVAGY!!!");
		g.drawImage(images.elementAt(akt_fig_num), 10, 10, 300, 300, this);
	}
}