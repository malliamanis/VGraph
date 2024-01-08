package com.theverdik.vgraph;

import static com.theverdik.vgraph.Main.*;

public class Graph {
	public interface Function {
		double assign(double x);
	}

	private Function f;
	private boolean[] data;

	private int color;
	private double scale;

	public Graph(Function f, double scale, int color) {
		this.f = f;
		this.scale = scale;
		this.color = color;
	}

	public void plot() {
		data = new boolean[WIDTH * HEIGHT];

		for (double x = -(WIDTH_HALF - 1); x < WIDTH_HALF; x += (double) 1 / (scale * 100)) {
			double y = -f.assign(x);

			double x_scaled = x * scale;
			double y_scaled = y * scale;

			if (y_scaled >= HEIGHT_HALF || y_scaled <= -HEIGHT_HALF || x_scaled >= WIDTH_HALF || x_scaled <= -WIDTH_HALF)
				continue;

			data[(int)(x_scaled + WIDTH_HALF - 1) + (int)(y_scaled + HEIGHT_HALF - 1) * WIDTH] = true;
		}
	}

	public Function getFunction() {
		return f;
	}

	public void setFunction(Function f) {
		this.f = f;
	}

	public boolean[] getData() {
		return data;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
}
