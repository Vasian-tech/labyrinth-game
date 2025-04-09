import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements KeyListener{
	Image road;
	Image wall;
	Image treasure;
	Image dalja;
	Image character;
	Image gameOver;
	Image winningBackground;
	ImageIcon opsionetIcon;
	int row_position;
	int col_position;
	int[][] labirinti = new int[20][20];
	KontrolleriLojes kontroller;
	Labirint labirint;
	Lojtar lojtar;
	JLabel piketLabel;
	JLabel kohaLabel;
	JLabel piketPerfundimtare;
	Font joystix;
	JButton opsionetButon;
	long startTime;
	long endTime;
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String username = "system";
	String password = "vasian";
	
	public GamePanel(KontrolleriLojes kontroller) {
		this.setPreferredSize(new Dimension(600, 600));
		this.setLayout(null);
		this.kontroller = kontroller;
		this.labirint = kontroller.labirint;
		this.lojtar = kontroller.lojtar;
		this.row_position = lojtar.pozicioni_rresht;
		this.col_position = lojtar.pozicioni_kolone;
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				this.labirinti[i][j] = kontroller.labirint.rrjetiLabirint[i][j];
			}
		}
		
		// new font import
		try {
			File joystixFile = new File("joystix monospace.otf");
			joystix = Font.createFont(Font.TRUETYPE_FONT, joystixFile);
			joystix = joystix.deriveFont(30f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.out.println("Fonti nuk gjendet!");
		}
		
		piketPerfundimtare = new JLabel();
		piketPerfundimtare.setForeground(Color.white);
		piketPerfundimtare.setFont(joystix);
		piketPerfundimtare.setBounds(170, 450, 300, 50);
		
		kohaLabel = new JLabel();
		kohaLabel.setForeground(Color.white);
		kohaLabel.setFont(joystix);
		kohaLabel.setBounds(170, 500, 300, 50);
		
		startTime = System.currentTimeMillis();
		
		piketLabel = new JLabel("Pikët: " + lojtar.piket);
		piketLabel.setForeground(Color.white);
		piketLabel.setFont(joystix);
		piketLabel.setBounds(10, 0, 200, 50);
		this.add(piketLabel);
				
		road = new ImageIcon("road.png").getImage();
		wall = new ImageIcon("wall.png").getImage();
		treasure = new ImageIcon("treasure.png").getImage();
		dalja = new ImageIcon("dalja.png").getImage();
		character = new ImageIcon("character.png").getImage();
		gameOver = new ImageIcon("gameover.png").getImage();
		winningBackground = new ImageIcon("winningBackground.png").getImage();
		opsionetIcon = new ImageIcon("opsionetIcon.png");
		
		opsionetButon = new JButton();
		opsionetButon.setIcon(opsionetIcon);
		opsionetButon.setBounds(545, 5, 50, 50);
		opsionetButon.setBorder(new javax.swing.border.LineBorder(Color.WHITE, 2, true));
		opsionetButon.setBackground(Color.LIGHT_GRAY);
		this.add(opsionetButon);
		
		addKeyListener(this);
	}
	
	@Override
	public void addNotify() {
	    super.addNotify();
	    requestFocusInWindow();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        this.remove(piketLabel);
        
        piketLabel = new JLabel("Pikët: " + lojtar.piket);
		piketLabel.setForeground(Color.white);
		piketLabel.setFont(joystix);
		piketLabel.setBounds(10, 0, 200, 50);
		this.add(piketLabel);
        
        labirint.gjeneroLabirint(row_position, col_position);
        
        for(int i = labirint.fillim_rresht; i < labirint.fund_rresht; i++) {
        	for(int j = labirint.fillim_kolone; j < labirint.fund_kolone; j++) {
        		if(labirinti[i][j] == 0) {
        			g2d.drawImage(road, (j-labirint.fillim_kolone)*50, (i-labirint.fillim_rresht)*50, null);
        		} else if(labirinti[i][j] == 1) {
        			g2d.drawImage(wall, (j-labirint.fillim_kolone)*50, (i-labirint.fillim_rresht)*50, null);
        		} else if(labirinti[i][j] == 2) {
        			g2d.drawImage(treasure, (j-labirint.fillim_kolone)*50, (i-labirint.fillim_rresht)*50, null);
        		} else if(labirinti[i][j] == 3) {
        			g2d.drawImage(dalja, (j-labirint.fillim_kolone)*50, (i-labirint.fillim_rresht)*50, null);
        		}
        	}
        }
        
        g2d.drawImage(character, (col_position-labirint.fillim_kolone)*50, (row_position-labirint.fillim_rresht)*50, null);
        
        if(labirint.eshteMur(row_position, col_position)) {
			lojtar.status = 2;
			this.remove(piketLabel);
			this.remove(opsionetButon);
			g2d.drawImage(gameOver, 0, 0, null);
			removeKeyListener(this);
			// sound effect
			try {
	            File audioFile = new File("Game Over  Sound Effect.wav");
	            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	            Clip clip = AudioSystem.getClip();
	            clip.open(audioStream);
	            clip.start();
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
	            System.out.println(exc);
	        }
		} else if(labirint.eshteThesar(row_position, col_position)) {
			lojtar.piket++;
			labirinti[row_position][col_position] = 0;
			labirint.rrjetiLabirint[row_position][col_position] = 0;
			// sound effect
			try {
	          File audioFile = new File("treasuresound.wav");
	          AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	          Clip clip = AudioSystem.getClip();
	          clip.open(audioStream);
	          clip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
				System.out.println(exc);
			}
			this.repaint();
		} else if(labirint.eshteDalje(row_position, col_position)) {
			lojtar.status = 1;
			
			lojtar.vektorCount++;
			int[] tempVektor = new int[lojtar.vektorCount-1];
			for(int i = 0; i < lojtar.vektorCount-1; i++) {
				tempVektor[i] = lojtar.piketVektor[i];
			}
			lojtar.piketVektor = new int[lojtar.vektorCount];
			StringBuilder piketVektorString = new StringBuilder("{\"matrix\": [[");
			for(int i = 0; i < lojtar.vektorCount-1; i++) {
				lojtar.piketVektor[i] = tempVektor[i];
				piketVektorString.append(lojtar.piketVektor[i] + ", ");
			}
			lojtar.piketVektor[lojtar.vektorCount-1] = lojtar.piket;
			piketVektorString.append(lojtar.piketVektor[lojtar.vektorCount-1] + "]]}");
			
			//db connection
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url, username, password);
				String sql = "UPDATE PLAYERS SET piketVektor = ?, vektorCount = ? WHERE username = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, piketVektorString.toString());
				statement.setInt(2, lojtar.vektorCount);
				statement.setString(3, lojtar.gameUsername);
				statement.executeUpdate();
			} catch(SQLException exc) {
				JOptionPane.showMessageDialog(null, "Lidhja me databazën nuk është e mundur: " + exc.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			}
			
			this.remove(piketLabel);
			this.remove(opsionetButon);
			piketPerfundimtare.setText("Pikët: " + lojtar.piket + "/" + "6");
			
			endTime = System.currentTimeMillis();

	        // LLogaritja e kohes
	        long koha = endTime - startTime;
	        long minuta = (koha / 1000) / 60;
	        long sekonda = (koha / 1000) % 60;

	        // Formati si mm:ss
	        String formattedTime = String.format("%02d:%02d", minuta, sekonda);
			kohaLabel.setText("Koha: " + formattedTime);
			
			this.add(piketPerfundimtare);
			this.add(kohaLabel);
			g2d.drawImage(winningBackground, 0, 0, null);
			removeKeyListener(this);
			// sound effect
			try {
				File audioFile = new File("win.wav");
		        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		        Clip clip = AudioSystem.getClip();
		        clip.open(audioStream);
		        clip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
				System.out.println(exc);
			}
		}
    }

	@Override
	public void keyTyped(KeyEvent e) {
	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			row_position--;
			this.repaint();
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			row_position++;
			this.repaint();
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			col_position--;
			this.repaint();
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			col_position++;
			this.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
