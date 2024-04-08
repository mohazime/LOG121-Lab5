package Model;

import View.Observer;

public abstract class Observable {

	private Observer observer;

	public void addObserver(Observer observer) {
		if (observer != null) {
			this.observer = observer;
		} else {
			System.out.println("Null observer!");
		}
	}

	public void removeObserver() {
		this.observer = null;
	}

	public void notifyObserver() {
		if(observer != null) {
			observer.update(this);
		} else {
			System.out.println("Observer (view) is null!");
		}
	}
	
}
