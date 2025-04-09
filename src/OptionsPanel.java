import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionsPanel extends JPanel{
	JLabel opsionet;
	JButton vazhdoButon;
	JButton piketButon;
	JButton ruajButon;
	JButton ngarkoButon;
	JButton rifilloButon;
	JButton dilButon;
	Font joystix;
	
	public OptionsPanel() {
		this.setPreferredSize(new Dimension(600, 600));
		this.setBackground(new Color(0, 0, 0));
		this.setLayout(null);
		
		// new font import
		try {
			File joystixFile = new File("joystix monospace.otf");
			joystix = Font.createFont(Font.TRUETYPE_FONT, joystixFile);
			joystix = joystix.deriveFont(40f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.out.println("Fonti nuk gjendet!");
		}
		
		opsionet = new JLabel("Opsionet");
		opsionet.setBounds(170, 0, 300, 50);
		opsionet.setForeground(Color.yellow);
		opsionet.setFont(joystix);
		
		vazhdoButon = new JButton("Vazhdo");
		vazhdoButon.setBounds(100, 90, 400, 50);
		vazhdoButon.setForeground(Color.white);
		vazhdoButon.setFont(joystix);
		vazhdoButon.setFocusable(false);
		vazhdoButon.setOpaque(false);
		vazhdoButon.setContentAreaFilled(false);
		vazhdoButon.setBorderPainted(false);
		
		ruajButon = new JButton("Ruaj");
		ruajButon.setBounds(100, 170, 400, 50);
		ruajButon.setForeground(Color.white);
		ruajButon.setFont(joystix);
		ruajButon.setFocusable(false);
		ruajButon.setOpaque(false);
		ruajButon.setContentAreaFilled(false);
		ruajButon.setBorderPainted(false);
		
		ngarkoButon = new JButton("Ngarko");
		ngarkoButon.setBounds(100, 250, 400, 50);
		ngarkoButon.setForeground(Color.white);
		ngarkoButon.setFont(joystix);
		ngarkoButon.setFocusable(false);
		ngarkoButon.setOpaque(false);
		ngarkoButon.setContentAreaFilled(false);
		ngarkoButon.setBorderPainted(false);
		
		rifilloButon = new JButton("Rifillo");
		rifilloButon.setBounds(100, 330, 400, 50);
		rifilloButon.setForeground(Color.white);
		rifilloButon.setFont(joystix);
		rifilloButon.setFocusable(false);
		rifilloButon.setOpaque(false);
		rifilloButon.setContentAreaFilled(false);
		rifilloButon.setBorderPainted(false);
		
		piketButon = new JButton("Pikët");
		piketButon.setBounds(100, 410, 400, 50);
		piketButon.setForeground(Color.white);
		piketButon.setFont(joystix);
		piketButon.setFocusable(false);
		piketButon.setOpaque(false);
		piketButon.setContentAreaFilled(false);
		piketButon.setBorderPainted(false);
		
		dilButon = new JButton("Dil");
		dilButon.setBounds(100, 490, 400, 50);
		dilButon.setForeground(Color.white);
		dilButon.setFont(joystix);
		dilButon.setFocusable(false);
		dilButon.setOpaque(false);
		dilButon.setContentAreaFilled(false);
		dilButon.setBorderPainted(false);
		
		this.add(opsionet);
		this.add(vazhdoButon);
		this.add(piketButon);
		this.add(ruajButon);
		this.add(ngarkoButon);
		this.add(rifilloButon);
		this.add(dilButon);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
	}
}
