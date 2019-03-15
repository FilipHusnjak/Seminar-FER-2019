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
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static final int DRAW_WIDTH = 420;
	private static final int DRAW_HEIGHT = 420;
	
	private static final int NUM_INPUTS = 3;
	
	private static final int DESIRED_RED = -1;
	private static final int DESIRED_BLUE = 1;
	
	private static final double learningRate = 0.1;
		
	private DrawPanel drawPanel = new DrawPanel();
	
	private List<Coordinate> coordinatesRed = new ArrayList<>();
	private List<Coordinate> coordinatesBlue = new ArrayList<>();
	
	private double[] weights = new double[NUM_INPUTS];
	
	List<List<Coordinate>> listCoord = Arrays.asList(coordinatesRed, coordinatesBlue);
	
	private boolean running = false;
	private boolean clear = true;
	
	private Color[] colors = {Color.RED, Color.BLUE};
	private int colorIndex = 0;
	
	public Window() {
		
		super();
		
		setup();
		
		this.setLocation(20, 20);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.setTitle("Simulation");
		
		this.setLayout(new BorderLayout());
		
		this.add(drawPanel, BorderLayout.CENTER);
		
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
			
			setup();
			
			coordinatesRed.clear();
			coordinatesBlue.clear();
			running = false;
			clear = true;
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
				
				System.out.println(running);
				
			}).start();
		});
		
		btnStop.addActionListener(e -> {
			
			setup();
			
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
	
	
	
	public void setup() {
		Random random = new Random();
		for (int i = 0; i < NUM_INPUTS; ++i) {
			weights[i] = random.nextDouble();
		}
	}
	
	
	
	private boolean train() {
		
		boolean trained = true;
		trained = loopCoordinates(coordinatesRed, DESIRED_RED);
		trained = loopCoordinates(coordinatesBlue, DESIRED_BLUE);
		
		for (int i = 0; i < NUM_INPUTS; ++i) {
			System.out.print(weights[i] + " ");
		}
		System.out.println();
		
		return trained;
	}
	
	
	
	private boolean loopCoordinates(List<Coordinate> coordinates, int desiredValue) {
		
		boolean trained = true;
		for (Coordinate c : coordinates) {
			double xCord = (double) c.x / DRAW_WIDTH * 2 - 1;
			double yCord = (double) c.y / DRAW_HEIGHT * 2 - 1;
			
			double[] inputs = new double[] {1, xCord, yCord};
			int error = match(inputs, desiredValue);
			if (error != 0) trained = false;
			adjustWeight(inputs, error);
		}
		
		return trained;
	}
	
	
	
	private int match(double[] inputs, int desiredOut) {
		return desiredOut - guess(inputs);
	}
	
	
	
	private int guess(double[] inputs) {
		double weightedSum = 0;
		for (int i = 0; i < inputs.length; ++i) {
			weightedSum += inputs[i] * weights[i];
		}
		return (weightedSum > 0 ? DESIRED_BLUE : DESIRED_RED);
	}
	
	
	
	private void adjustWeight(double[] inputs, int error) {
		for (int i = 0; i < NUM_INPUTS; ++i) {
			weights[i] += inputs[i] * error * learningRate;
		}
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
        			
            		if (guess(new double[] {1, xCord, yCord}) > 0) {
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
