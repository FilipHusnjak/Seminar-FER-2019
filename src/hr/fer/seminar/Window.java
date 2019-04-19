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
	
	private static final int DESIRED_RED = -1;
	private static final int DESIRED_BLUE = 1;
		
	private DrawPanel drawPanel = new DrawPanel();
	
	private List<Coordinate> coordinatesRed = new ArrayList<>();
	private List<Coordinate> coordinatesBlue = new ArrayList<>();
		
	List<List<Coordinate>> listCoord = Arrays.asList(coordinatesRed, coordinatesBlue);
	
	private boolean running = false;
	private boolean clear = true;
	
	private Color[] colors = {Color.RED, Color.BLUE};
	private int colorIndex = 0;
	
	Perceptron perceptron = new Perceptron();
	
	public Window() {
		super.setLocation(20, 20);
		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		super.setTitle("Simulation");
		super.setLayout(new BorderLayout());
		super.add(drawPanel, BorderLayout.CENTER);
		
		JPanel panelSouth = new JPanel();
		JButton btnStart = new JButton("Start");
		JButton btnStop = new JButton("Stop");
		JButton btnColor = new JButton("Color");
		JButton btnClear = new JButton("Clear");
		
		btnColor.setBackground(Color.RED);
		
		drawPanel.addMouseListener(new MouseAdapter() {
			
			@Override
            public void mousePressed(MouseEvent e) {
				listCoord.get(colorIndex).add(new Coordinate(e.getPoint().x, e.getPoint().y));
                drawPanel.repaint();
            }
			
		});
		
		btnClear.addActionListener(e -> {
			perceptron.setup();
			coordinatesRed.clear();
			coordinatesBlue.clear();
			clear = true;
			running = false;
			drawPanel.repaint();
			
		});
		
		btnStart.addActionListener(e -> {
			new Thread(() -> {
				running = true;
				clear = false;
				train();
				do {
					SwingUtilities.invokeLater(() -> drawPanel.repaint());
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
					}
			
				} while (running && !train());
				
				SwingUtilities.invokeLater(() -> drawPanel.repaint());
				
			}).start();
		});
		
		btnStop.addActionListener(e -> {
			perceptron.setup();
			clear = true;
			running = false;
			drawPanel.repaint();
		});
		
		btnColor.addActionListener(e -> {
			colorIndex = 1 - colorIndex;
			btnColor.setBackground(colors[colorIndex]);
		});
		
		panelSouth.add(btnStart);
		panelSouth.add(btnStop);
		panelSouth.add(btnColor);
		panelSouth.add(btnClear);
		
		this.add(panelSouth, BorderLayout.SOUTH);
		
		this.pack();
		
	}
	
	
	private boolean train() {
		boolean trained = true;
		trained = loopCoordinates(coordinatesRed, DESIRED_RED);
		return trained &&  loopCoordinates(coordinatesBlue, DESIRED_BLUE);
	}
	
	
	private boolean loopCoordinates(List<Coordinate> coordinates, int desiredValue) {
		boolean trained = true;
		for (Coordinate c : coordinates) {
			double xCord = (double) c.x / DRAW_WIDTH * 2 - 1;
			double yCord = (double) c.y / DRAW_HEIGHT * 2 - 1;
			
			double[] inputs = new double[] {1, xCord, yCord};
			int error = match(inputs, desiredValue);
			if (error != 0) trained = false;
			perceptron.adjustWeight(inputs, error);
		}
		return trained;
	}
	
	
	private int match(double[] inputs, int desiredOut) {
		return desiredOut - perceptron.guess(inputs);
	}
	
	
	private class DrawPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public DrawPanel() {
            setPreferredSize(new Dimension(DRAW_WIDTH, DRAW_HEIGHT));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
                                 
            for (int i = 0; i < getWidth(); ++i) {
            	for (int j = 0; j < getHeight(); ++j) {
            		double xCord = (double) i / DRAW_WIDTH * 2 - 1;
        			double yCord = (double) j / DRAW_HEIGHT * 2 - 1;
        			
            		if (perceptron.guess(new double[] {1, xCord, yCord}) > 0) {
            			g.setColor(Color.BLUE);
            		} else {
            			g.setColor(Color.RED); 
            		}
            		
            		if(clear) {
            			g.setColor(Color.WHITE); 
            		}
            		
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
