package hr.fer.seminar.demo;

import javax.swing.SwingUtilities;

import hr.fer.seminar.window.Window;

public class Main {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Window().setVisible(true));
	}
	
}
