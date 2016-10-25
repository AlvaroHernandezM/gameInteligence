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
	private int seconds, areaSize;
	private Timer timerEnemies;
	private Timer timerClock;
	private JLabel lblLeft,lblRigth,lblCenter,lblClock;

	public WindowGame() {
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setExtendedState(MAXIMIZED_BOTH);
		setSize(1380, 740);
		setTitle("Game");
		getContentPane().setBackground(Color.WHITE);
		btnHero = new JButton("H");
		add(btnHero);
		lblClock = new JLabel("");
		lblClock.setBounds(10, 10, 200, 100);
		lblClock.setFont(new Font("Arial", Font.PLAIN, 22));
		add(lblClock);
		setVisible(true);
	}

	public void init(int areaSize){
		this.areaSize = areaSize;
		play = true;
		btnHero.setBounds(getWidth()/2,getHeight() - 100 , 50,50);
		createAreas();
		generateEnemies();
		moveEnemiesDown();
		refreshClock();
	}

	private void createAreas() {
		lblCenter = new JLabel("");	
		lblCenter.setBounds(btnHero.getX(), btnHero.getY()-this.areaSize, btnHero.getWidth(), this.areaSize);
		lblCenter.setBackground(Color.RED);
		lblCenter.setOpaque(true);
		add(lblCenter);

		lblLeft = new JLabel("");
		lblLeft.setBounds(this.lblCenter.getX()-this.lblCenter.getWidth(), btnHero.getY()-this.areaSize, btnHero.getWidth(), this.areaSize);
		lblLeft.setOpaque(true);
		lblLeft.setBackground(Color.YELLOW);
		add(lblLeft);
		
		lblRigth = new JLabel("");
		lblRigth.setBounds(this.lblCenter.getX()+this.lblCenter.getWidth(), btnHero.getY()-this.areaSize, btnHero.getWidth(), this.areaSize);
		lblRigth.setOpaque(true);
		lblRigth.setBackground(Color.BLUE);
		add(lblRigth);
				
		repaint();
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


	public static void main(String[] args) {
		WindowGame w = new WindowGame();
		w.init(200);
	}
}