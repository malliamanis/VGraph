package com.theverdik.vgraph;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Mouse implements MouseWheelListener {
	public boolean forward = false;
	public boolean back = false;

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0)
			forward = true;
		else if (e.getWheelRotation() > 0)
			back = true;
	}

	public void update() {
		forward = back = false;
	}
}
