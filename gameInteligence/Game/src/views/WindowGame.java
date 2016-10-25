package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.border.Border;

public class WindowGame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton btnHero;
	private boolean play;
	private int seconds, areaSize;
	private Timer timerEnemies;
	private Timer timerClock;
	private JLabel lblLeft, lblRigth, lblCenter, lblClock;

	public WindowGame() {
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setExtendedState(MAXIMIZED_BOTH);
		setSize(1350, 730);
		setLocationRelativeTo(null);
		setTitle("Game");
		getContentPane().setBackground(Color.WHITE);
		btnHero = new JButton("H");
		add(btnHero);
		lblCenter = new JLabel("");
		lblLeft = new JLabel("");
		lblRigth = new JLabel("");
		lblClock = new JLabel("");
		add(lblCenter);
		add(lblRigth);
		add(lblLeft);
		lblClock.setBounds(10, 10, 200, 100);
		lblClock.setFont(new Font("Arial", Font.PLAIN, 22));
		add(lblClock);
		setVisible(true);
	}

	public void init(int areaSize) {
		this.areaSize = areaSize;
		play = true;
		btnHero.setBounds(getWidth() / 2, getHeight() - 100, 50, 50);
		createAreas();
		generateEnemies();
		moveEnemiesDown();
		moveHero();
		refreshClock();
	}

	private void createAreas() {
		lblCenter.setBounds(btnHero.getX(), btnHero.getY() - this.areaSize, btnHero.getWidth(), this.areaSize);
		//lblCenter.setBackground(Color.RED);
		//lblCenter.setOpaque(true);
		Border border = BorderFactory.createLineBorder(Color.RED, 2);
		lblCenter.setBorder(border);


		lblLeft.setBounds(this.lblCenter.getX() - this.lblCenter.getWidth(), btnHero.getY() - this.areaSize,
				btnHero.getWidth(), this.areaSize + btnHero.getHeight());
		//lblLeft.setOpaque(true);
		//lblLeft.setBackground(Color.YELLOW);
		border = BorderFactory.createLineBorder(Color.BLUE, 2);
		lblLeft.setBorder(border);

		lblRigth.setBounds(this.lblCenter.getX() + this.lblCenter.getWidth(), btnHero.getY() - this.areaSize,
				btnHero.getWidth(), this.areaSize + btnHero.getHeight());
		//lblRigth.setOpaque(true);
		//lblRigth.setBackground(Color.BLUE);
		border = BorderFactory.createLineBorder(Color.BLUE, 2);
		lblRigth.setBorder(border);

		repaint();
	}

	public void moveToRigth() {
		if (btnHero.getX() + 60 < getWidth()) {
			btnHero.setLocation(new Point(lblRigth.getX(), btnHero.getY()));
			createAreas();
		}
	}

	public void moveToLeft() {
		if (btnHero.getX() - 10 > 0) {
			btnHero.setLocation(new Point(lblLeft.getX(), btnHero.getY()));
			createAreas();
		}
	}

	public void moveEnemiesDown() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (play) {
					for (Component component : getContentPane().getComponents()) {
						if (component instanceof JButton) {
							JButton btnEnemie = (JButton) component;
							if (btnEnemie.getText().equals("X")) {
								if (btnEnemie.getY() > getHeight()) {
									getContentPane().remove(component);
									repaint();
									break;
								} else {
									btnEnemie.setLocation(new Point(btnEnemie.getX(), btnEnemie.getY() + 10));
									if (btnEnemie.getBounds().intersects(btnHero.getBounds())) {
										gameOver();
									}
									repaint();
								}
							}
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		thread.start();
	}

	public void moveHero() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (play) {
					for (Component component : getContentPane().getComponents()) {
						if (component instanceof JButton) {
							JButton btnEnemie = (JButton) component;
							if (btnEnemie.getText().equals("X")) {
								if (btnEnemie.getBounds().intersects(lblCenter.getBounds())) {
									for (Component newcomponent : getContentPane().getComponents()) {
										int xEnemie1 = 0 , xEnemie2 = 0;
										if (newcomponent instanceof JButton) {
											JButton newbtnEnemie = (JButton) newcomponent;
											if (newbtnEnemie.getText().equals("X")) {
												if (newbtnEnemie.getBounds().intersects(lblRigth.getBounds())) {
													System.out.println("Izq");
													moveToLeft();
													break;
												} 
												else if (newbtnEnemie.getBounds().intersects(lblLeft.getBounds())) {
													System.out.println("Der");
													moveToRigth();
													break;
												}
											}
										}
									}									
								}
							}
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		thread.start();
	}

	private void gameOver() {
		play = false;
		timerClock.stop();
		timerEnemies.stop();
		JOptionPane.showMessageDialog(null, "Juego terminado");
	}

	public void addEnemie() {
		int x = (int) (Math.random() * getWidth() - 50);
		JButton btnEnemie = new JButton("X");
		btnEnemie.setBounds(x, 0, 50, 50);
		add(btnEnemie);
		getContentPane().repaint();
	}

	public void refreshClock() {
		timerClock = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seconds++;
				lblClock.setText("Time: " + seconds + "s");
			}
		});
		timerClock.start();
	}

	public void generateEnemies() {
		timerEnemies = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addEnemie();
			}
		});
		timerEnemies.start();
	}

	public static void main(String[] args) {
		WindowGame w = new WindowGame();
		w.init(200);
	}
}