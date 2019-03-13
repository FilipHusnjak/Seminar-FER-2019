package hr.fer.seminar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static final int DRAW_WIDTH = 420;
	private static final int DRAW_HEIGHT = 420;
	
	private DrawPanel drawPanel = new DrawPanel();
	
	private List<Coordinate> coordinatesRed = new ArrayList<>();
	private List<Coordinate> coordinatesBlue = new ArrayList<>();
	
	List<List<Coordinate>> listCoord = Arrays.asList(coordinatesRed, coordinatesBlue);
	
	private boolean running = true;
	
	private Color[] colors = {Color.RED, Color.BLUE};
	private int colorIndex = 0;
	
	public Window() {
		
		super();
		
		this.setLocation(20, 20);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.setTitle("Simulation");
		
		this.setLayout(new BorderLayout());
		
		this.add(drawPanel, BorderLayout.CENTER);
		
		JPanel panelSouth = new JPanel();
		JButton btnStart = new JButton("Start");
		JButton btnStop = new JButton("Stop");
		JButton btnColor = new JButton("Color");
		btnColor.setBackground(Color.RED);
		
		drawPanel.addMouseListener(new MouseAdapter() {
			
			@Override
            public void mousePressed(MouseEvent e) {
				
				listCoord.get(colorIndex).add(new Coordinate(e.getPoint().x, e.getPoint().y));
                
                drawPanel.repaint();
            }
			
		});
		
		btnStart.addActionListener(e -> {
			new Thread(() -> {
				
				while (running) {
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					
					SwingUtilities.invokeLater(() -> drawPanel.repaint());
				}
				
			}).start();
		});
		
		btnStop.addActionListener(e -> {
			running = false;
		});
		
		btnColor.addActionListener(e -> {
			colorIndex = 1 - colorIndex;
			btnColor.setBackground(colors[colorIndex]);
		});
		
		panelSouth.add(btnStart);
		panelSouth.add(btnStop);
		panelSouth.add(btnColor);
		
		this.add(panelSouth, BorderLayout.SOUTH);
		
		this.pack();
		
	}
	
	
	
	private class DrawPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public DrawPanel() {
            setPreferredSize(new Dimension(DRAW_WIDTH, DRAW_HEIGHT));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.setColor(Color.WHITE);          
            for (int i = 0; i < getWidth(); ++i) {
            	for (int j = 0; j < getHeight(); ++j) {
            		g.drawLine(i, j, i, j);
            	}
            }
            
            g.setColor(Color.RED);
            for (Coordinate c : coordinatesRed) {
            	g.drawRect(c.x, c.y, 2, 2);
            }
            
            g.setColor(Color.BLUE);
            for (Coordinate c : coordinatesBlue) {
            	g.drawRect(c.x, c.y, 2, 2);
            }
        }
    }
	
	private static class Coordinate {
		int x;
		int y;
		
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
	}
	
}
