package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class WindowGame extends JFrame{

	private static final long serialVersionUID = 1L;
	private JButton btnHero;
	private boolean play;
	private JLabel lblClock;
	private int seconds;
	private JLabel areaVision;
	private Timer timerEnemies;
	private Timer timerClock;

	public WindowGame() {

		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		setTitle("Game");
		getContentPane().setBackground(Color.WHITE);
		btnHero = new JButton("H");
		add(btnHero);
		lblClock = new JLabel("");
		lblClock.setBounds(10, 10, 200, 100);
		lblClock.setFont(new Font("Arial", Font.PLAIN, 22));
		add(lblClock);
		areaVision = new JLabel();
		add(areaVision);
		//areaVision.setBounds(btnHero.getX()-75, y, width, height);

		setVisible(true);
	}

	public void init(){
		play = true;
		btnHero.setBounds(getWidth()/2,getHeight() - 100 , 50,50);
		move();
		generateEnemies();
		moveEnemiesDown();
		refreshClock();
	}

	public void moveToRigth(){
		if (btnHero.getX()+60<getWidth()) {
			btnHero.setLocation(new Point(btnHero.getX()+10, btnHero.getY()));
		}
	}
	
	public void moveToLeft(){
		if (btnHero.getX()-10 > 0) {
			btnHero.setLocation(new Point(btnHero.getX()-10, btnHero.getY()));
		}
	}
	
	public void moveEnemiesDown(){
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while(play){
					for (Component component : getContentPane().getComponents()) {
						if(component instanceof JButton){
							JButton btnEnemie = (JButton)component;
							if(btnEnemie.getText().equals("X")){
								if (btnEnemie.getY()>getHeight()) {
									getContentPane().remove(component);
									repaint();
									break;
								}else{
									btnEnemie.setLocation(new Point(btnEnemie.getX(), btnEnemie.getY()+10));
									if(btnEnemie.getBounds().intersects(btnHero.getBounds())){
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
	
	private void gameOver() {
		play = false;
		timerClock.stop();
		timerEnemies.stop();
		JOptionPane.showMessageDialog(null, "Juego terminado");
	}
	
	public void addEnemie(){
		int x = (int)(Math.random()*getWidth()-50);
		JButton btnEnemie = new JButton("X");
		btnEnemie.setBounds(x, 0, 50, 50);
		add(btnEnemie);
		getContentPane().repaint();
	}
	public void refreshClock(){
		timerClock= new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seconds++;
				lblClock.setText("Time: "+seconds+"s");
			}
		});
		timerClock.start();
	}
	public void generateEnemies(){
		timerEnemies = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addEnemie();				
			}
		});
		timerEnemies.start();
	}
	
	public void move(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(play){
					double numero = Math.random();
					if (numero < 0.5){
						moveToLeft();
					}else{
						moveToRigth();
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	public static void main(String[] args) {
		WindowGame w = new WindowGame();
		w.init();
	}
}