package Model;

import javafx.geometry.Point2D;

public class PerspectiveModel extends Observable {
	
	private double scale = 1;
	private Point2D location = new Point2D(0, 0);


	public double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
		//notifyObserver();
	}

	public Point2D getLocation() {
		return location;
	}

	public void setLocation(Point2D location) {
		this.location = location;
		//notifyObserver();
	}

	/*public Memento createMemento() {
		return new Memento(this);
	}*/

	public void restoreFromMemento() {
		notifyObserver();
	}
}
