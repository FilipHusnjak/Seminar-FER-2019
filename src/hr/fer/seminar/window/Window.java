package hr.fer.seminar.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
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

import hr.fer.seminar.network.BackPropagationNetwork;

public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static final int DRAW_WIDTH = 420;
	private static final int DRAW_HEIGHT = 420;
		
	private DrawPanel drawPanel = new DrawPanel();
	
	private List<Coordinate> coordinatesRed = new ArrayList<>();
	private List<Coordinate> coordinatesBlue = new ArrayList<>();
		
	private List<List<Coordinate>> listCoord = Arrays.asList(coordinatesRed, coordinatesBlue);
	
	private boolean clear = true;
	
	private boolean running = false;
	
	private Color[] colors = {Color.RED, Color.BLUE};
	private int colorIndex = 0;
	
	private static int MAX_ITER = 100000;
	
	private static int ITER_RES = 1000;
	
	private static double precision = 0.2;
	
	private BackPropagationNetwork network = new BackPropagationNetwork(new int[] {5, 1});
	
	private double maxError;
	
	public Window() {
		super.setLocation(20, 20);
		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		super.setTitle("Simulation");
		super.setLayout(new BorderLayout());
		super.add(drawPanel, BorderLayout.CENTER);
		super.setResizable(false);
		
		JPanel panelSouth = new JPanel();
		JButton btnStart = new JButton("Start");
		JButton btnStop = new JButton("Stop");
		JButton btnColor = new JButton("Toggle Color");
		JButton btnClear = new JButton("Clear");
		TextField definition = new TextField();
		TextField maxIter = new TextField();
		TextField iterRes = new TextField();
		Label defLabel = new Label("Network:");
		Label maxIterLabel = new Label("Max iterations:");
		Label iterResLabel = new Label("Each");
		Label iterResLabelDesc = new Label("iterations picture is redrawn!");
		
		btnColor.setBackground(Color.RED);
		
		drawPanel.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				listCoord.get(colorIndex).add(new Coordinate(e.getPoint().x, e.getPoint().y));
                drawPanel.repaint();
            }
		});
		
		btnClear.addActionListener(e -> {
			coordinatesRed.clear();
			coordinatesBlue.clear();
			clear = true;
			running = false;
			network.restart();
			drawPanel.repaint();
		});
		
		btnStart.addActionListener(event -> {
			btnStart.setEnabled(false);
			clear = false;
			running = true;
			String[] parts = definition.getText().split(",");
			int[] defNetwork = new int[parts.length - 1];
			for (int i = 1; i < parts.length; ++i) {
				defNetwork[i - 1] = Integer.parseInt(parts[i].trim());
			}
			MAX_ITER = Integer.parseInt(maxIter.getText().trim());
			ITER_RES = Integer.parseInt(iterRes.getText().trim());
			network = new BackPropagationNetwork(defNetwork);
			new Thread(() -> {
				for (int j = 0, n = (int) Math.ceil(MAX_ITER / ITER_RES); j < n && running; ++j) {
					for (int i = 0; i < ITER_RES; ++i) {
						maxError = 0;
						loopCoordinates(coordinatesRed, 0);
						loopCoordinates(coordinatesBlue, 1);
						if (maxError < precision) break;
					}
					SwingUtilities.invokeLater(() -> drawPanel.repaint());
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
					if (maxError < precision) break;
				}
				btnStart.setEnabled(true);
			}).start();
		});
		
		btnStop.addActionListener(e -> {
			clear = true;
			running = false;
			network.restart();
			drawPanel.repaint();
		});
		
		btnColor.addActionListener(e -> {
			colorIndex = 1 - colorIndex;
			btnColor.setBackground(colors[colorIndex]);
		});
		
		definition.setPreferredSize(new Dimension(100, 25));
		definition.setText("2,1");
		
		maxIter.setText("100000");
		
		iterRes.setText("1000");
		
		panelSouth.setLayout(new GridLayout(4, 1));
		JPanel bottomPanelNorth = new JPanel();
		JPanel bottomPanel1 = new JPanel();
		JPanel bottomPanel2 = new JPanel();
		JPanel bottomPanel3 = new JPanel();
		
		panelSouth.add(bottomPanelNorth);
		bottomPanelNorth.add(btnStart);
		bottomPanelNorth.add(btnStop);
		bottomPanelNorth.add(btnColor);
		bottomPanelNorth.add(btnClear);
		
		panelSouth.add(bottomPanel1);
		bottomPanel1.add(defLabel);
		bottomPanel1.add(definition);
		
		panelSouth.add(bottomPanel2);
		bottomPanel2.add(maxIterLabel);
		bottomPanel2.add(maxIter);
		
		panelSouth.add(bottomPanel3);
		bottomPanel3.add(iterResLabel);
		bottomPanel3.add(iterRes);
		bottomPanel3.add(iterResLabelDesc);
		
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
        	for (int y = 0; y < getHeight(); ++y) {
            	for (int x = 0; x < getWidth(); ++x) {
            		if (clear) {
            			g.setColor(Color.WHITE);
            		} else {
            			double output = network.calcResult(transformCoordinateX(x), transformCoordinateY(y));
            			float opacity = (float) (output < 0.5 ? 1 - output / 0.5 : output / 0.5 - 1);
            			Color color = output < 0.5 ? new Color(1, 0, 0, opacity) : new Color(0, 0, 1, opacity);
            			g.setColor(color);
            		}
            		g.drawLine(x, y, x, y);
            	}
            }
            g.setColor(Color.RED);
            for (Coordinate c : coordinatesRed) {
            	g.setColor(Color.RED);
            	g.fillRect(c.getX(), c.getY(), 4, 4);
            	g.setColor(Color.WHITE);
            	g.drawRect(c.getX() - 1, c.getY() - 1, 6, 6);
            }
            g.setColor(Color.BLUE);
            for (Coordinate c : coordinatesBlue) {
            	g.setColor(Color.BLUE);
            	g.fillRect(c.getX(), c.getY(), 4, 4);
            	g.setColor(Color.WHITE);
            	g.drawRect(c.getX() - 1, c.getY() - 1, 6, 6);
            }
        }
    }
	
	private void loopCoordinates(List<Coordinate> coordinates, int desiredValue) {
		for (Coordinate c: coordinates) {
			network.calcResult(transformCoordinateX(c.getX()), transformCoordinateY(c.getY()));
			network.calculateAndAdjustError(desiredValue);
			if (Math.abs(network.getError()) > maxError) {
				maxError = Math.abs(network.getError());
			}
		}
	}
	
	private static double transformCoordinateX(int x) {
		return (double) x / (DRAW_WIDTH - 1);
	}
	
	private static double transformCoordinateY(int y) {
		return 1 - (double) y / (DRAW_HEIGHT - 1);
	}
	
}
