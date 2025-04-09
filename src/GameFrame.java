import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GameFrame extends JFrame implements MouseListener{
	
	LoginPanel loginPanel;
	RegisterPanel registerPanel;
	InstructionsPanel instructionsPanel;
	MainPanel mainPanel;
	GamePanel gamePanel;
	OptionsPanel optionsPanel;
	ImageIcon background;
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String username = "system";
	String password = "vasian";
	String gameUsername;
	Font joystix;
	boolean running;
	KontrolleriLojes kontroller;
	int vektorCount;
	int[][] rrjetiLabirint = {
		    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		    {1, 0, 0, 0, 1, 0, 2, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1},
		    {1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 2, 1},
		    {1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1},
		    {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1},
		    {1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
		    {1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 2, 0, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1},
		    {1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
		    {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1},
		    {1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
		    {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
		    {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
		    {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 1, 2, 1, 0, 1},
		    {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
		    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1}
	};
	// 0 perfaqeson rrugen qe ndjek lojtari, 1 jane muret ku nuk mund te kalohet, 2 jane thesaret dhe 3 eshte dalja
	
	public GameFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Loja e Labirintit");
		this.setResizable(false);
		this.setIconImage(getIconImage());
		
		background = new ImageIcon("background.png");
		this.setIconImage(background.getImage());
		
		kontroller = new KontrolleriLojes();
		loginPanel = new LoginPanel();
		registerPanel = new RegisterPanel();
		instructionsPanel = new InstructionsPanel();
		mainPanel = new MainPanel();
		optionsPanel = new OptionsPanel();
		this.loginPanel.loginButton.addMouseListener(this);
		this.loginPanel.registerButton.addMouseListener(this);
		this.registerPanel.loginButton.addMouseListener(this);
		this.registerPanel.registerButton.addMouseListener(this);
		this.instructionsPanel.continueButton.addMouseListener(this);
		this.mainPanel.startButton.addMouseListener(this);
		this.mainPanel.loadButton.addMouseListener(this);
		this.optionsPanel.vazhdoButon.addMouseListener(this);
		this.optionsPanel.ruajButon.addMouseListener(this);
		this.optionsPanel.ngarkoButon.addMouseListener(this);
		this.optionsPanel.rifilloButon.addMouseListener(this);
		this.optionsPanel.piketButon.addMouseListener(this);
		this.optionsPanel.dilButon.addMouseListener(this);
		
		this.add(loginPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
	
	public void paint() {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == this.loginPanel.registerButton) {
			this.remove(loginPanel);
			this.add(registerPanel);
			this.revalidate();
			this.repaint();
		} else if(e.getSource() == this.registerPanel.loginButton) {
			this.remove(registerPanel);
			this.add(loginPanel);
			this.revalidate();
			this.repaint();
		} else if(e.getSource() == this.instructionsPanel.continueButton) {
			try {
	            File audioFile = new File("transition_explosion.wav");
	            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	            Clip clip = AudioSystem.getClip();
	            clip.open(audioStream);
	            clip.start();
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
	        	exc.printStackTrace();
	        }
			this.remove(instructionsPanel);
			this.add(mainPanel);
			this.revalidate();
			this.repaint();
		} else if(e.getSource() == this.loginPanel.loginButton) {
			// db connection
			try {
				Connection connection = DriverManager.getConnection(url, username, password);
				String sql = "SELECT * FROM PLAYERS WHERE username = ? AND password = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, this.loginPanel.username.getText());
				statement.setString(2, new String(this.loginPanel.password.getPassword()));
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					// sound effect
					try {
			            File audioFile = new File("transition_explosion.wav");
			            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			            Clip clip = AudioSystem.getClip();
			            clip.open(audioStream);
			            clip.start();
			        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
			            System.out.println(exc);
			        }
										
					gameUsername = this.loginPanel.username.getText();
					vektorCount = result.getInt("vektorCount");
										
					this.remove(loginPanel); 
					this.add(instructionsPanel);
					this.revalidate();
					this.repaint();
				} else {
					JOptionPane.showMessageDialog(null, "Username ose Password janë gabim!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				result.close();
				statement.close();
				connection.close();
			} catch (SQLException exc) {
				JOptionPane.showMessageDialog(null, "Lidhja me databazën nuk është e mundur: " + exc.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(e.getSource() == this.registerPanel.registerButton) {
			// db connection
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url, username, password);
			} catch(SQLException exc) {
				JOptionPane.showMessageDialog(null, "Lidhja me databazën nuk është e mundur: " + exc.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			}
			try {
				String sql = "SELECT * FROM PLAYERS WHERE username = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, this.registerPanel.username.getText());
				ResultSet result = statement.executeQuery();
				if(!result.next()) {
					sql = "INSERT INTO PLAYERS(username, password, pozicion_rresht, pozicion_kolone, fillim_rresht, fillim_kolone, fund_rresht, fund_kolone, piket, labirintiMatrice, piketVektor, vektorCount) VALUES(?, ?, 1, 1, 0, 0, 12, 12, 0, '{\"matrix\": [[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 0, 0, 0, 1, 0, 2, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1], [1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 2, 1], [1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1], [1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1], [1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1], [1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 2, 0, 0, 1, 0, 1], [1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1], [1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1], [1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1], [1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1], [1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1], [1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1], [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1], [1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1], [1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1], [1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1], [1, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 1, 2, 1, 0, 1], [1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1]]}', '{\"matrix\": [[]]}', 0)";
					statement = connection.prepareStatement(sql);
					statement.setString(1, this.registerPanel.username.getText());
					statement.setString(2, new String(this.registerPanel.password.getPassword()));
					statement.executeUpdate();
					JOptionPane.showMessageDialog(null, "Urime, ju u regjistruat me sukses!", "Info", JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Përdoruesi me këtë username ekziston, provoni përsëri!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				result.close();
				statement.close();
				connection.close();
			} catch (SQLException exc) {
				JOptionPane.showMessageDialog(null, "Username dhe Password nuk duhet te jene bosh!", "Database Error", JOptionPane.ERROR_MESSAGE);
			}
	} else if(e.getSource() == this.mainPanel.startButton) {
		this.kontroller.lojtar = new Lojtar();
		this.kontroller.labirint = new Labirint(rrjetiLabirint, 0, 12, 0, 12);
		kontroller.lojtar.gameUsername = gameUsername;
		kontroller.lojtar.vektorCount = vektorCount;
		kontroller.lojtar.piketVektor = new int[kontroller.lojtar.vektorCount];
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			for(int i = 0; i < kontroller.lojtar.vektorCount; i++) {
				String sql2 = String.format("SELECT JSON_VALUE(piketVektor, '$.matrix[0][%d]') AS VALUE FROM PLAYERS WHERE username = ?", i);
				PreparedStatement statement2 = connection.prepareStatement(sql2);
				statement2.setString(1, gameUsername);
				ResultSet result2 = statement2.executeQuery();
				result2.next();
				kontroller.lojtar.piketVektor[i] = result2.getInt("value");
				statement2.close();
				result2.close();
			}
			connection.close();
		} catch (SQLException exc) {
			JOptionPane.showMessageDialog(null, "Gabim ne veprimet me databaze!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		gamePanel = new GamePanel(this.kontroller);
		this.remove(mainPanel);
		// sound effect
		try {
            File audioFile = new File("transition_explosion.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
            System.out.println(exc);
        }
		this.add(gamePanel);
		this.gamePanel.opsionetButon.addMouseListener(this);
		this.revalidate();
		this.repaint();
		
		running = true;
				
		// kontrollon nqs ndryshon statusi i lojes
		new Thread(() -> {
            while (running) {
                if (kontroller.lojtar.status == 2) {
                    kontroller.perfundoLojen(this, mainPanel, gamePanel, kontroller.lojtar.status);
                    this.mainPanel.startButton.setForeground(Color.white);
        			this.mainPanel.startButton.setOpaque(false);
        			this.mainPanel.startButton.setContentAreaFilled(false);
                } else if(kontroller.lojtar.status == 1) {
                	kontroller.perfundoLojen(this, mainPanel, gamePanel, kontroller.lojtar.status);
                	vektorCount++;
                	running = false;
                	this.mainPanel.startButton.setForeground(Color.white);
        			this.mainPanel.startButton.setOpaque(false);
        			this.mainPanel.startButton.setContentAreaFilled(false);
                }
    			
                try {
                    Thread.sleep(10);
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            }
        }).start();
	} else if(e.getSource() == this.mainPanel.loadButton) {
		kontroller.ngarkoLojen(this);
		this.remove(this.mainPanel);
		// sound effect
		try {
            File audioFile = new File("transition_explosion.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
            System.out.println(exc);
        }
		this.add(this.gamePanel);
		this.gamePanel.opsionetButon.addMouseListener(this);
		this.revalidate();
		this.repaint();
	} else if(e.getSource() == this.gamePanel.opsionetButon) {
		this.add(optionsPanel);
		this.remove(gamePanel);
		this.revalidate();
		this.repaint();
	} else if(e.getSource() == this.optionsPanel.vazhdoButon) {
		this.remove(optionsPanel);
		this.add(gamePanel);
		this.gamePanel.opsionetButon.setBackground(Color.LIGHT_GRAY);
		this.optionsPanel.vazhdoButon.setForeground(Color.white);
		this.optionsPanel.vazhdoButon.setOpaque(false);
		this.optionsPanel.vazhdoButon.setContentAreaFilled(false);
		this.revalidate();
		this.repaint();
		this.gamePanel.requestFocusInWindow();
	} else if(e.getSource() == this.optionsPanel.ruajButon) {
		kontroller.ruajLojen(this);
	} else if(e.getSource() == this.optionsPanel.rifilloButon) {
		this.remove(optionsPanel);
		this.remove(gamePanel);
		this.kontroller.lojtar = new Lojtar();
		this.kontroller.labirint = new Labirint(rrjetiLabirint, 0, 12, 0, 12);
		
		this.optionsPanel.rifilloButon.setForeground(Color.white);
		this.optionsPanel.rifilloButon.setOpaque(false);
		this.optionsPanel.rifilloButon.setContentAreaFilled(false);
		
		gamePanel = new GamePanel(this.kontroller);
		// sound effect
		try {
            File audioFile = new File("transition_explosion.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
            System.out.println(exc);
        }
		this.add(gamePanel);
		this.gamePanel.opsionetButon.addMouseListener(this);
		this.revalidate();
		this.repaint();
		
		running = true;
		
		// kontrollon nqs ndryshon statusi i lojes
		new Thread(() -> {
            while (running) {
                if (kontroller.lojtar.status == 2) {
                    kontroller.perfundoLojen(this, mainPanel, gamePanel, kontroller.lojtar.status);
                } else if(kontroller.lojtar.status == 1) {
                	kontroller.perfundoLojen(this, mainPanel, gamePanel, kontroller.lojtar.status);
                	vektorCount++;
                	running = false;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            }
        }).start();
	} else if(e.getSource() == this.optionsPanel.ngarkoButon) {
		kontroller.ngarkoLojen(this);
		
		this.remove(optionsPanel);
		this.gamePanel.opsionetButon.addMouseListener(this);
		this.add(gamePanel);
		this.gamePanel.opsionetButon.setBackground(Color.LIGHT_GRAY);
		this.optionsPanel.ngarkoButon.setForeground(Color.white);
		this.optionsPanel.ngarkoButon.setOpaque(false);
		this.optionsPanel.ngarkoButon.setContentAreaFilled(false);
		this.revalidate();
		this.repaint();
	} else if (e.getSource() == this.optionsPanel.piketButon) {
	    JPanel piketPanel = new JPanel();
	    piketPanel.setPreferredSize(new Dimension(600, 600));
	    piketPanel.setBackground(Color.black);
	    piketPanel.setLayout(null);

	    JLabel piketLabel = new JLabel("Pikët e kaluara");
	    piketLabel.setForeground(Color.white);
	    
	    // new font import
	    try {
	        File joystixFile = new File("joystix monospace.otf");
	        joystix = Font.createFont(Font.TRUETYPE_FONT, joystixFile);
	        joystix = joystix.deriveFont(30f);
	    } catch (FontFormatException | IOException exc) {
	        System.out.println("Fonti nuk gjendet!");
	    }
	    piketLabel.setFont(joystix);
	    piketLabel.setBounds(100, 50, 400, 50);

	    kontroller.renditPiket(kontroller.lojtar.piketVektor);

	    JPanel labelPanel = new JPanel();
	    labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
	    labelPanel.setBackground(Color.black);

	    for (int i = 0; i < kontroller.lojtar.piketVektor.length; i++) {
	        JLabel pointLabel = new JLabel("Pikët: " + kontroller.lojtar.piketVektor[i] + "/6");
	        pointLabel.setForeground(Color.white);
	        pointLabel.setFont(joystix);
	        labelPanel.add(pointLabel);
	    }

	    JScrollPane scrollPane = new JScrollPane(labelPanel);
	    scrollPane.setBounds(50, 120, 500, 400);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	    JButton closeButton = new JButton("X");
	    closeButton.setBounds(500, 10, 80, 80);
	    closeButton.setBackground(Color.red);
	    closeButton.setForeground(Color.white);
	    closeButton.setFont(new Font("Arial", Font.BOLD, 20));
	    closeButton.setBorderPainted(false);
	    closeButton.setFocusPainted(false);
	    closeButton.setContentAreaFilled(false);
	    closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	    closeButton.addActionListener(e2 -> {
	        this.remove(piketPanel);
	        this.optionsPanel.piketButon.setForeground(Color.white);
			this.optionsPanel.piketButon.setOpaque(false);
			this.optionsPanel.piketButon.setContentAreaFilled(false);
	        this.add(optionsPanel); 
	        this.revalidate();
	        this.repaint();
	    });

	    piketPanel.add(piketLabel);
	    piketPanel.add(scrollPane);
	    piketPanel.add(closeButton);

	    this.remove(optionsPanel);
	    this.add(piketPanel);
	    this.revalidate();
	    this.repaint();
	} else if(e.getSource() == this.optionsPanel.dilButon) {
		System.exit(0);
	}
}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == this.loginPanel.loginButton) {
			this.loginPanel.loginButton.setForeground(Color.black);
			this.loginPanel.loginButton.setBackground(Color.white);
			this.loginPanel.loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.loginPanel.registerButton) {
			this.loginPanel.registerButton.setForeground(Color.black);
			this.loginPanel.registerButton.setBackground(Color.white);
			this.loginPanel.registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.registerPanel.loginButton) {
			this.registerPanel.loginButton.setForeground(Color.black);
			this.registerPanel.loginButton.setBackground(Color.white);
			this.registerPanel.loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.registerPanel.registerButton) {
			this.registerPanel.registerButton.setForeground(Color.black);
			this.registerPanel.registerButton.setBackground(Color.white);
			this.registerPanel.registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.instructionsPanel.continueButton) {
			this.instructionsPanel.continueButton.setForeground(Color.black);
			this.instructionsPanel.continueButton.setBackground(Color.white);
			this.instructionsPanel.continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.mainPanel.startButton) {
			this.mainPanel.startButton.setOpaque(true);
			this.mainPanel.startButton.setContentAreaFilled(true);
			this.mainPanel.startButton.setForeground(Color.black);
			this.mainPanel.startButton.setBackground(Color.white);
			this.mainPanel.startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.mainPanel.loadButton) {
			this.mainPanel.loadButton.setOpaque(true);
			this.mainPanel.loadButton.setContentAreaFilled(true);
			this.mainPanel.loadButton.setForeground(Color.black);
			this.mainPanel.loadButton.setBackground(Color.white);
			this.mainPanel.loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.vazhdoButon) {
			this.optionsPanel.vazhdoButon.setOpaque(true);
			this.optionsPanel.vazhdoButon.setContentAreaFilled(true);
			this.optionsPanel.vazhdoButon.setForeground(Color.black);
			this.optionsPanel.vazhdoButon.setBackground(Color.white);
			this.optionsPanel.vazhdoButon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.ruajButon) {
			this.optionsPanel.ruajButon.setOpaque(true);
			this.optionsPanel.ruajButon.setContentAreaFilled(true);
			this.optionsPanel.ruajButon.setForeground(Color.black);
			this.optionsPanel.ruajButon.setBackground(Color.white);
			this.optionsPanel.ruajButon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.ngarkoButon) {
			this.optionsPanel.ngarkoButon.setOpaque(true);
			this.optionsPanel.ngarkoButon.setContentAreaFilled(true);
			this.optionsPanel.ngarkoButon.setForeground(Color.black);
			this.optionsPanel.ngarkoButon.setBackground(Color.white);
			this.optionsPanel.ngarkoButon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.rifilloButon) {
			this.optionsPanel.rifilloButon.setOpaque(true);
			this.optionsPanel.rifilloButon.setContentAreaFilled(true);
			this.optionsPanel.rifilloButon.setForeground(Color.black);
			this.optionsPanel.rifilloButon.setBackground(Color.white);
			this.optionsPanel.rifilloButon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.piketButon) {
			this.optionsPanel.piketButon.setOpaque(true);
			this.optionsPanel.piketButon.setContentAreaFilled(true);
			this.optionsPanel.piketButon.setForeground(Color.black);
			this.optionsPanel.piketButon.setBackground(Color.white);
			this.optionsPanel.piketButon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.dilButon) {
			this.optionsPanel.dilButon.setOpaque(true);
			this.optionsPanel.dilButon.setContentAreaFilled(true);
			this.optionsPanel.dilButon.setForeground(Color.black);
			this.optionsPanel.dilButon.setBackground(Color.white);
			this.optionsPanel.dilButon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.gamePanel.opsionetButon) {
			this.gamePanel.opsionetButon.setOpaque(true);
			this.gamePanel.opsionetButon.setContentAreaFilled(true);
			this.gamePanel.opsionetButon.setForeground(Color.black);
			this.gamePanel.opsionetButon.setBackground(Color.white);
			this.gamePanel.opsionetButon.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == this.loginPanel.loginButton) {
			this.loginPanel.loginButton.setForeground(Color.white);
			this.loginPanel.loginButton.setBackground(Color.black);
		} else if(e.getSource() == this.loginPanel.registerButton) {
			this.loginPanel.registerButton.setForeground(Color.white);
			this.loginPanel.registerButton.setBackground(Color.black);
		} else if(e.getSource() == this.registerPanel.loginButton) {
			this.registerPanel.loginButton.setForeground(Color.white);
			this.registerPanel.loginButton.setBackground(Color.black);
		} else if(e.getSource() == this.registerPanel.registerButton) {
			this.registerPanel.registerButton.setForeground(Color.white);
			this.registerPanel.registerButton.setBackground(Color.black);
		} else if(e.getSource() == this.instructionsPanel.continueButton) {
			this.instructionsPanel.continueButton.setForeground(Color.white);
			this.instructionsPanel.continueButton.setBackground(Color.black);
		} else if(e.getSource() == this.mainPanel.startButton) {
			this.mainPanel.startButton.setForeground(Color.white);
			this.mainPanel.startButton.setOpaque(false);
			this.mainPanel.startButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.mainPanel.loadButton) {
			this.mainPanel.loadButton.setForeground(Color.white);
			this.mainPanel.loadButton.setOpaque(false);
			this.mainPanel.loadButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.vazhdoButon) {
			this.optionsPanel.vazhdoButon.setForeground(Color.white);
			this.optionsPanel.vazhdoButon.setOpaque(false);
			this.optionsPanel.vazhdoButon.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.ruajButon) {
			this.optionsPanel.ruajButon.setForeground(Color.white);
			this.optionsPanel.ruajButon.setOpaque(false);
			this.optionsPanel.ruajButon.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.ngarkoButon) {
			this.optionsPanel.ngarkoButon.setForeground(Color.white);
			this.optionsPanel.ngarkoButon.setOpaque(false);
			this.optionsPanel.ngarkoButon.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.rifilloButon) {
			this.optionsPanel.rifilloButon.setForeground(Color.white);
			this.optionsPanel.rifilloButon.setOpaque(false);
			this.optionsPanel.rifilloButon.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.piketButon) {
			this.optionsPanel.piketButon.setForeground(Color.white);
			this.optionsPanel.piketButon.setOpaque(false);
			this.optionsPanel.piketButon.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.dilButon) {
			this.optionsPanel.dilButon.setForeground(Color.white);
			this.optionsPanel.dilButon.setOpaque(false);
			this.optionsPanel.dilButon.setContentAreaFilled(false);
		} else if(e.getSource() == this.gamePanel.opsionetButon) {
			this.gamePanel.opsionetButon.setBackground(Color.LIGHT_GRAY);
		}
	}

}
