package hr.fer.seminar.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.seminar.network.BackPropagationNetwork;

/**
 * Main window that simulates classification.
 * 
 * @author Filip Husnjak
 */
public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Window width
	 */
	private static int DRAW_WIDTH = 500;
	
	/**
	 * Window height
	 */
	private static int DRAW_HEIGHT = 500;
	
	/**
	 * Main panel used for drawing
	 */
	private DrawPanel drawPanel = new DrawPanel();
	
	/**
	 * Red points
	 */
	private List<Point> coordinatesRed = new ArrayList<>();
	
	/**
	 * Blue points
	 */
	private List<Point> coordinatesBlue = new ArrayList<>();
	
	/**
	 * List that holds red points and blue points
	 */
	private List<List<Point>> listCoord = Arrays.asList(coordinatesRed, coordinatesBlue);
	
	/**
	 * Should window be cleared
	 */
	private boolean clear = true;
	
	/**
	 * Should iterations continue
	 */
	private boolean running = false;
	
	/**
	 * Points colors
	 */
	private Color[] colors = {Color.RED, Color.BLUE};
	
	/**
	 * Index of current color in use
	 */
	private int colorIndex = 0;
	
	/**
	 * Maximum number of iterations
	 */
	private static int MAX_ITER = 100000;
	
	/**
	 * How many iterations have to pass before drawing
	 */
	private static int ITER_RES = 1000;
	
	/**
	 * Maximum error of the network
	 */
	private static double MAX_ERROR = 0.1;
	
	/**
	 * Network used for learning and classification
	 */
	private BackPropagationNetwork network;
	
	/**
	 * Max error that network created
	 */
	private double maxError;
	
	/**
	 * Constructs and initializes new window.
	 */
	public Window() {
		super.setLocation(20, 20);
		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		super.setTitle("Simulation");
		super.setLayout(new BorderLayout());
		super.add(drawPanel, BorderLayout.CENTER);
		initGui();
		super.pack();
	}
	
	/**
	 * Initializes GUI with proper labels and buttons.
	 */
	private void initGui() {
		JPanel panelSouth = new JPanel();
		JButton btnStart = new JButton("Start");
		JButton btnStop = new JButton("Stop");
		JButton btnColor = new JButton("Toggle Color");
		JButton btnClear = new JButton("Clear");
		TextField definition = new TextField(10);
		TextField maxIter = new TextField();
		TextField maxErrortf = new TextField();
		TextField learningRatetf = new TextField();
		TextField iterRes = new TextField();
		Label defLabel = new Label("Network:");
		Label maxIterLabel = new Label("Max iterations:");
		Label maxErrorLabel = new Label("Max error:");
		Label learningRateLabel = new Label("Learning rate:");
		Label iterResLabel = new Label("Each");
		Label iterResLabelDesc = new Label("iterations picture is redrawn!");
		
		drawPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent componentEvent) {
				DRAW_WIDTH = drawPanel.getSize().width;
				DRAW_HEIGHT = drawPanel.getSize().height;
		    }
		});
		
		btnColor.setBackground(Color.RED);
		
		drawPanel.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				listCoord.get(colorIndex).add(new Point(e.getPoint().x, e.getPoint().y));
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
			int[] defNetwork = new int[parts.length];
			for (int i = 0; i < parts.length; ++i) {
				defNetwork[i] = Integer.parseInt(parts[i].trim());
			}
			MAX_ITER = Integer.parseInt(maxIter.getText().trim());
			MAX_ERROR = Double.parseDouble(maxErrortf.getText().trim());
			ITER_RES = Integer.parseInt(iterRes.getText().trim());
			BackPropagationNetwork.LEARNING_RATE = Double.parseDouble(learningRatetf.getText());
			network = new BackPropagationNetwork(defNetwork);
			new Thread(() -> {
				for (int j = 0, n = (int) Math.ceil(MAX_ITER / ITER_RES); j < n && running; ++j) {
					for (int i = 0; i < ITER_RES; ++i) {
						maxError = 0;
						loopCoordinates(coordinatesRed, 0);
						loopCoordinates(coordinatesBlue, 1);
						if (maxError < MAX_ERROR) break;
					}
					SwingUtilities.invokeLater(() -> drawPanel.repaint());
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
					if (maxError < MAX_ERROR) break;
				}
				SwingUtilities.invokeLater(() -> btnStart.setEnabled(true));
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
		
		definition.setText("2,1");
		
		maxIter.setText(Integer.toString(MAX_ITER));
		
		iterRes.setText(Integer.toString(ITER_RES));
		
		maxErrortf.setText(Double.toString(MAX_ERROR));
		
		learningRatetf.setText(Double.toString(BackPropagationNetwork.LEARNING_RATE));
		
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
		bottomPanel1.add(learningRateLabel);
		bottomPanel1.add(learningRatetf);
		
		panelSouth.add(bottomPanel2);
		bottomPanel2.add(maxIterLabel);
		bottomPanel2.add(maxIter);
		bottomPanel2.add(maxErrorLabel);
		bottomPanel2.add(maxErrortf);
		
		panelSouth.add(bottomPanel3);
		bottomPanel3.add(iterResLabel);
		bottomPanel3.add(iterRes);
		bottomPanel3.add(iterResLabelDesc);
		
		this.add(panelSouth, BorderLayout.SOUTH);
	}
	
	/**
	 * Represents panel used for drawing.
	 * 
	 * @author Filip Husnjak
	 */
	private class DrawPanel extends JPanel {
		
		private BufferedImage image;

		private static final long serialVersionUID = 1L;

		public DrawPanel() {
            setPreferredSize(new Dimension(DRAW_WIDTH, DRAW_HEIGHT));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null || image.getHeight() != getHeight() || image.getWidth() != getWidth()) {
            	image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            }
        	for (int y = 0; y < getHeight(); ++y) {
            	for (int x = 0; x < getWidth(); ++x) {
            		if (clear) {
            			image.setRGB(x, y, 0xFFFFFFFF);
            		} else {
            			double output = network.calcResult(transformCoordinateX(x), transformCoordinateY(y));
            			double opacity = output < 0.5
            					? 1 - output / 0.5
            					: output / 0.5 - 1;
            			image.setRGB(x, y, (output < 0.5 ? 0xFF0000 : 0xFF) | ((int)(0xFF * opacity) << 24));
            		}
            	}
            }
        	g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), null);
            for (Point c : coordinatesRed) {
            	g.setColor(Color.RED);
            	g.fillRect(c.getX(), c.getY(), 4, 4);
            	g.setColor(Color.WHITE);
            	g.drawRect(c.getX() - 1, c.getY() - 1, 6, 6);
            }
            for (Point c : coordinatesBlue) {
            	g.setColor(Color.BLUE);
            	g.fillRect(c.getX(), c.getY(), 4, 4);
            	g.setColor(Color.WHITE);
            	g.drawRect(c.getX() - 1, c.getY() - 1, 6, 6);
            }
        }
    }
	
	/**
	 * Loops through coordinates and trains the network.
	 * 
	 * @param coordinates
	 *        coordinates used for training
	 * @param desiredValue
	 *        desired output of the network
	 */
	private void loopCoordinates(List<Point> coordinates, int desiredValue) {
		for (Point c: coordinates) {
			network.calcResult(transformCoordinateX(c.getX()), transformCoordinateY(c.getY()));
			network.propagateError(desiredValue);
			if (Math.abs(network.getError()) > maxError) {
				maxError = Math.abs(network.getError());
			}
		}
	}
	
	/**
	 * Transforms x coordinate of the pixel to appropriate 2D coordinate system.
	 * 
	 * @param x
	 *        coordinate to be transformed
	 * @return transformed coordinate
	 */
	private static double transformCoordinateX(int x) {
		return (double) x / (DRAW_WIDTH - 1);
	}
	
	/**
	 * Transforms y coordinate of the pixel to appropriate 2D coordinate system.
	 * 
	 * @param y
	 *        coordinate to be transformed
	 * @return transformed coordinate
	 */
	private static double transformCoordinateY(int y) {
		return 1 - (double) y / (DRAW_HEIGHT - 1);
	}
	
}
