
package com.baselet.gwt.client.copy.element;

import java.util.Vector;

import com.baselet.gwt.client.copy.diagram.draw.geom.Line;
import com.baselet.gwt.client.copy.diagram.draw.geom.Point;


public class StickingPolygon {

	public class StickLine extends Line {

		private StickLine(Point p1, Point p2) {
			super(p1, p2);
		}

		// calculates the difference between this line and the other line at the specified x or y coordinate (whichever fits better)
		public Point diffToLine(StickLine s, int x, int y) {
			Point diff = new Point(0, 0);
			if (getEnd().x == getStart().x) {
				// AB: Fixed: use s.getStart().x instead of getStart().x
				diff.x = s.getStart().x - (s.getEnd().x - s.getStart().x) - x; // mitte der neuen linie

				if (s.getEnd().x == s.getStart().x) {
					// vertical lines - no y difference except the line is at an end
					diff.y = 0;
					if (s.getStart().y > s.getEnd().y) {
						if (s.getStart().y < y) diff.y = s.getStart().y - y;
						else if (s.getEnd().y > y) diff.y = s.getEnd().y - y;
					}
					else {
						if (s.getEnd().y < y) diff.y = s.getEnd().y - y;
						else if (s.getStart().y > y) diff.y = s.getStart().y - y;
					}
					return diff;
				}
			}
			else diff.x = (x - getStart().x) * (s.getEnd().x - s.getStart().x) / (getEnd().x - getStart().x) + s.getStart().x - x;

			if (getEnd().y == getStart().y) {
				// AB: Fixed: use s.getStart().x instead of getStart().x
				diff.y = s.getStart().y - (s.getEnd().y - s.getStart().y) - y;

				if (s.getEnd().y == s.getStart().y) {
					// horizontal lines - no x difference except the line is at an end
					diff.x = 0;
					if (s.getStart().x > s.getEnd().x) {
						if (s.getStart().x < x) diff.x = s.getStart().x - x;
						else if (s.getEnd().x > x) diff.x = s.getEnd().x - x;
					}
					else {
						if (s.getEnd().x < x) diff.x = s.getEnd().x - x;
						else if (s.getStart().x > x) diff.x = s.getStart().x - x;
					}
				}
			}
			else diff.y = (y - getStart().y) * (s.getEnd().y - s.getStart().y) / (getEnd().y - getStart().y) + s.getStart().y - y;

			return diff;
		}

		private boolean isConnected(Point p, int gridSize) {
			double distance = this.distance(p);
			return (distance < gridSize);
		}
	}

	private Vector<StickLine> stick = new Vector<StickLine>();
	private Point lastpoint = null;
	private Point firstpoint = null;

	public void addPoint(int x, int y) {
		addPoint(new Point(x, y));
	}

	public void addPoint(int x, int y, boolean connect_to_first) {
		addPoint(new Point(x, y), connect_to_first);
	}
	
	public void addPoint(Point p) {
		// add a line with corresponding stickingInfo
		if (this.lastpoint != null) {
			stick.add(new StickLine(lastpoint, p));
		}
		else this.firstpoint = p;
		this.lastpoint = p;
	}

	public StickLine getLine(int index) {
		return this.stick.get(index);
	}

	public void addPoint(Point p, boolean connect_to_first) {
		this.addPoint(p);
		if (connect_to_first && (this.firstpoint != null)) this.addPoint(this.firstpoint);
	}

	public void addLine(Point p1, Point p2) {
		stick.add(new StickLine(p1, p2));
	}

	public Vector<? extends Line> getStickLines() {
		return this.stick;
	}

	public int isConnected(Point p, int gridSize) {
		int con = -1;
		for (int i = 0; i < this.stick.size(); i++)
			if (this.stick.get(i).isConnected(p, gridSize)) return i;

		return con;
	}
}