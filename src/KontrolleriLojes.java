import java.awt.Color;
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
import javax.swing.JOptionPane;

public class KontrolleriLojes {
	
	Lojtar lojtar;
	Labirint labirint;
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String username = "system";
	String password = "vasian";
	
	public void perfundoLojen(GameFrame gameFrame, MainPanel mainPanel, GamePanel gamePanel, int status) {
		if(status == 1) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameFrame.remove(gamePanel);
			gameFrame.add(mainPanel);
			gameFrame.revalidate();
			gameFrame.repaint();
			lojtar.status = 0;
		} else if(status == 2) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameFrame.remove(gamePanel);
			gameFrame.add(mainPanel);
			gameFrame.revalidate();
			gameFrame.repaint();
			lojtar.status = 0;
		}
		
	}
	
	public void ruajLojen(GameFrame gameFrame) {
		// db connection
				Connection connection = null;
				try {
					connection = DriverManager.getConnection(url, username, password);
				} catch(SQLException exc) {
					JOptionPane.showMessageDialog(null, "Lidhja me databazën nuk është e mundur: " + exc.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
				}
				try {
					String sql = "UPDATE PLAYERS SET pozicion_rresht = ?, pozicion_kolone = ?, fillim_rresht = ?, fillim_kolone = ?, fund_rresht = ?, fund_kolone = ?, piket = ?, labirintiMatrice = ? WHERE username = ?";
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setInt(1, gameFrame.gamePanel.row_position);
					statement.setInt(2, gameFrame.gamePanel.col_position);
					statement.setInt(3, gameFrame.kontroller.labirint.fillim_rresht);
					statement.setInt(4, gameFrame.kontroller.labirint.fillim_kolone);
					statement.setInt(5, gameFrame.kontroller.labirint.fund_rresht);
					statement.setInt(6, gameFrame.kontroller.labirint.fund_kolone);
					statement.setInt(7, gameFrame.kontroller.lojtar.piket);
					
					StringBuilder labyrinth_data = new StringBuilder("{\"matrix\": [");
					for(int i = 0; i < 20; i++) {
						labyrinth_data.append("[");
						for(int j = 0; j < 19; j++) {
							labyrinth_data.append(gameFrame.kontroller.labirint.rrjetiLabirint[i][j] + ", ");
						}
						labyrinth_data.append(gameFrame.kontroller.labirint.rrjetiLabirint[i][19] + "]");
						if (i < 19) {
					        labyrinth_data.append(", ");
					    }
					}
					labyrinth_data.append("]}");
					
					statement.setString(8, labyrinth_data.toString());
					statement.setString(9, gameFrame.gameUsername);
					
					statement.executeUpdate();
					
					statement.close();
					connection.close();
					JOptionPane.showMessageDialog(null, "Loja u ruajt!", "Info", JOptionPane.PLAIN_MESSAGE);
				} catch (SQLException exc) {
					JOptionPane.showMessageDialog(null, "Loja nuk u ruajt!", "Database Error", JOptionPane.ERROR_MESSAGE);
				}
				
				gameFrame.remove(gameFrame.optionsPanel);
				gameFrame.add(gameFrame.gamePanel);
				gameFrame.gamePanel.opsionetButon.setBackground(Color.LIGHT_GRAY);
				gameFrame.optionsPanel.ruajButon.setForeground(Color.white);
				gameFrame.optionsPanel.ruajButon.setOpaque(false);
				gameFrame.optionsPanel.ruajButon.setContentAreaFilled(false);
				gameFrame.revalidate();
				gameFrame.repaint();
	}
	
	public void ngarkoLojen(GameFrame gameFrame) {
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
					statement.setString(1, gameFrame.gameUsername);
					ResultSet result = statement.executeQuery();
					
					if(result.next()) {
						//fillo lojen
						gameFrame.kontroller.lojtar = new Lojtar(result.getInt("pozicion_rresht"), result.getInt("pozicion_kolone"), result.getInt("piket"));
						for(int i = 0; i < 20; i++) {
							for(int j = 0; j < 20; j++) {
								String query = String.format("SELECT JSON_VALUE(labirintiMatrice, '$.matrix[%d][%d]') AS VALUE FROM PLAYERS WHERE username = ?", i, j);
								PreparedStatement statement2 = connection.prepareStatement(query);
								statement2.setString(1, gameFrame.gameUsername);
								ResultSet result2 = statement2.executeQuery();
								result2.next();
								gameFrame.rrjetiLabirint[i][j] = result2.getInt("value");
								result2.close();
								statement2.close();
							}
						}
						gameFrame.kontroller.labirint = new Labirint(gameFrame.rrjetiLabirint, result.getInt("fillim_rresht"), result.getInt("fund_rresht"), result.getInt("fillim_kolone"), result.getInt("fund_kolone"));
						gameFrame.kontroller.lojtar.gameUsername = gameFrame.gameUsername;
						gameFrame.kontroller.lojtar.vektorCount = gameFrame.vektorCount;
						gameFrame.kontroller.lojtar.piketVektor = new int[gameFrame.kontroller.lojtar.vektorCount];
						connection = null;
						try {
							connection = DriverManager.getConnection(url, username, password);
							for(int i = 0; i < gameFrame.kontroller.lojtar.vektorCount; i++) {
								String sql2 = String.format("SELECT JSON_VALUE(piketVektor, '$.matrix[0][%d]') AS VALUE FROM PLAYERS WHERE username = ?", i);
								PreparedStatement statement2 = connection.prepareStatement(sql2);
								statement2.setString(1, gameFrame.gameUsername);
								ResultSet result2 = statement2.executeQuery();
								result2.next();
								gameFrame.kontroller.lojtar.piketVektor[i] = result2.getInt("value");
								statement2.close();
								result2.close();
							}
							connection.close();
						} catch (SQLException exc) {
							JOptionPane.showMessageDialog(null, "Gabim ne veprimet me databaze!", "Error", JOptionPane.ERROR_MESSAGE);
						}
						gameFrame.gamePanel = new GamePanel(gameFrame.kontroller);
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
						// kontrollon nqs ndryshon statusi i lojes
						new Thread(() -> {
				            while (true) {
				                if (gameFrame.kontroller.lojtar.status == 2) {
				                	gameFrame.kontroller.perfundoLojen(gameFrame, gameFrame.mainPanel, gameFrame.gamePanel, gameFrame.kontroller.lojtar.status);
				                	gameFrame.mainPanel.loadButton.setForeground(Color.white);
				                	gameFrame.mainPanel.loadButton.setOpaque(false);
				                	gameFrame.mainPanel.loadButton.setContentAreaFilled(false);
				                } else if(gameFrame.kontroller.lojtar.status == 1) {
				                	gameFrame.kontroller.perfundoLojen(gameFrame, gameFrame.mainPanel, gameFrame.gamePanel, gameFrame.kontroller.lojtar.status);
				                	gameFrame.vektorCount++;
				                	gameFrame.mainPanel.loadButton.setForeground(Color.white);
				                	gameFrame.mainPanel.loadButton.setOpaque(false);
				                	gameFrame.mainPanel.loadButton.setContentAreaFilled(false);
				                }
				    			
				                try {
				                    Thread.sleep(10);
				                } catch (InterruptedException exc) {
				                    exc.printStackTrace();
				                }
				            }
				        }).start();
					}
					statement.close();
					connection.close();
				} catch (SQLException exc) {
					JOptionPane.showMessageDialog(null, "Loja nuk u ngarkua!", "Database Error", JOptionPane.ERROR_MESSAGE);
				}
	}
	
	public int[] renditPiket(int[] piket) {
	    if (piket == null || piket.length == 0) {
	        return new int[0];
	    }

	    int n = piket.length;
	    for (int i = 0; i < n - 1; i++) {
	        for (int j = 0; j < n - 1 - i; j++) {
	            if (piket[j] < piket[j + 1]) {
	                int temp = piket[j];
	                piket[j] = piket[j + 1];
	                piket[j + 1] = temp;
	            }
	        }
	    }

	    return piket;
	}
}
