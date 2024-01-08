package com.theverdik.vgraph;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {
	public Window(int width, int height, String title, Main main) {
		JFrame frame = new JFrame(title);

		Dimension dimension = new Dimension(width, height);
		frame.setPreferredSize(dimension);
		frame.setMinimumSize(dimension);
		frame.setMaximumSize(dimension);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(main);

		frame.setVisible(true);
	}
}
