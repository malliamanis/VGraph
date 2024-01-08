package com.theverdik.vgraph;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

public class Main extends Canvas implements Runnable {

	public static final int WIDTH = 1680;
	public static final int HEIGHT = WIDTH / 16 * 9;

	public static final double WIDTH_HALF = WIDTH / 2.0;
	public static final double HEIGHT_HALF = HEIGHT / 2.0;

	public static final String TITLE = "VGraph";

	private Thread thread;
	private boolean running = false;

	private Window window;
	private Mouse mouse;

	private BufferedImage image;
	private int[] pixels;

	private double scale = 50.0;
	private ArrayList<Graph> graphs;

	public Main() {
		window = new Window(WIDTH, HEIGHT, TITLE, this);
		mouse = new Mouse();
		addMouseWheelListener(mouse);

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

		graphs = new ArrayList<Graph>(0);

		graphs.add(new Graph(x -> x*Math.exp(Math.sin(x)), scale, 0x0000DD));
		graphs.getFirst().plot();
	}

	private synchronized void start() {
		running = true;

		thread = new Thread(this, "Display"); thread.start();
	}

	private synchronized void stop() {
		running = false;

		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		this.requestFocus();

		final int ticks = 60;

		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / ticks;

		double delta = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;

			lastTime = now;

			while (delta >= 1) {
				render();
				--delta;
			}
			update();
		}

		stop();
	}

	private static final double SCALE_INCREMENT = 10;
	private static final double ISCALE_INCREMENT = 1 / SCALE_INCREMENT;

	private void update() {
		if (mouse.back) {
			if (scale > SCALE_INCREMENT)
				scale -= SCALE_INCREMENT;
			else if (scale <= SCALE_INCREMENT) {
				if (scale > 1) {
					double temp = 1 / SCALE_INCREMENT - scale;
					scale = 1;
					scale -= temp;
					System.out.println(scale);
				}
				else if (scale > 0)
					scale -= ISCALE_INCREMENT;
			}

			for (Graph graph : graphs) {
				graph.setScale(scale);
				graph.plot();
			}
		}
		else if (mouse.forward) {
			if (scale > 1)
				scale += SCALE_INCREMENT;
			else if (scale <= 1) {
				if (scale > 1 - ISCALE_INCREMENT) {
					double temp = 1 / (ISCALE_INCREMENT - (1 - scale));
					scale = 1;
					scale += temp;
				}
				else
					scale += ISCALE_INCREMENT;
			}

			for (Graph graph : graphs) {
				graph.setScale(scale);
				graph.plot();
			}
		}

		mouse.update();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		for (int x = 0; x < WIDTH; ++x) {
			for (int y = 0; y < HEIGHT; ++y) {
				if (x == WIDTH_HALF - 1/* || y == HEIGHT_HALF - 1*/)
					pixels[x + (int)(HEIGHT_HALF - 1) * WIDTH] = 0x000000;
				else
					pixels[x + y * WIDTH] = 0xFFFFFF;
			}

			pixels[x + (int)(HEIGHT_HALF - 1) * WIDTH] = 0x000000;
		}

		for (Graph graph : graphs) {
			boolean[] data = graph.getData();
			int color = graph.getColor();

			for (int x = 0; x < WIDTH; ++x) {
				for (int y = 0; y < HEIGHT; ++y) {
					if ((graph.getData())[x + y * WIDTH]) {
						pixels[x + y * WIDTH] = color;
						if (x < WIDTH - 1)
							pixels[x + 1 + y * WIDTH] = color;
						if (x > 0)
							pixels[x - 1 + y * WIDTH] = color;

						if (y < HEIGHT - 1)
							pixels[x + (y + 1) * WIDTH] = color;
						if (y > 0)
							pixels[x + (y - 1) * WIDTH] = color;
					}
				}
			}
		}

		g.drawImage(image, 0, 0, window);

		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Main main = new Main();

		main.start();
	}
}
