package Controller;

import Model.PerspectiveModel;
import View.PersepectiveImageView;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class PerspectiveController {

	private static final double ZOOM_FACTOR = 0.1;
	private CommandManager commandManager;
	private PersepectiveImageView imageView;
	private double initialX, initialY;
	private double totalZoom = 1;
	private PerspectiveModel model;

	public PerspectiveController(PerspectiveModel model, PersepectiveImageView imageView) {
		this.commandManager = CommandManager.getInstance();
		this.imageView = imageView;
		this.model = model;
		commandManager.addMemento(model.createMemento());
		//this.model = new PerspectiveModel();
	}

	public void handleMouseEntered() {
		imageView.getImageView().setCursor(Cursor.OPEN_HAND);
	}
    
	public void handleMousePressed(MouseEvent event) {
		imageView.getImageView().setCursor(Cursor.CLOSED_HAND);
		initialX = event.getX();
		initialY = event.getY();
	}

	public void handleMouseReleased(MouseEvent event) {
		imageView.getImageView().setCursor(Cursor.OPEN_HAND);
		if(event.getX() != initialX && event.getY() != initialY) {
			addTranslate(event);
		}
	}


	public void addTranslate(MouseEvent event) {
		double deltaX = event.getX() - initialX;
    	double deltaY = event.getY() - initialY;
    	Point2D translation = new Point2D(deltaX, deltaY);
		
		//Command command = new TranslateCommand(imageView.getImageView(), translation);
		//Command command = new TranslateCommand(model, translation);
		Command command = new TranslateCommand(translation);
		commandManager.addMemento(model.createMemento());
		//commandManager.executeCommand(command, model);
	}

	public void handleMouseDragged(MouseEvent event) {
		double deltaX = event.getX() - initialX;
    	double deltaY = event.getY() - initialY;
    	Point2D translation = new Point2D(deltaX, deltaY);
		//Command command = new TranslateCommand(model, translation);
		Command command = new TranslateCommand(translation);
		//this.command = command;
		//commandManager.addMemento(command);
    	commandManager.executeCommand(command, model);
	}

	public void handleMouseScrolled(ScrollEvent event) {
		double deltaY = event.getDeltaY();
		double zoom;

		if(deltaY > 0) {
			zoom = 1 + ZOOM_FACTOR;
		} else {
			zoom = 1 - ZOOM_FACTOR;
		}

		totalZoom *= zoom;

		if(totalZoom < 0.51) {
			totalZoom /= zoom;
			return;
		}
		
		//Command command = new ZoomCommand(imageView.getImageView(), zoom);
		//Command command = new ZoomCommand(model, totalZoom);
		Command command = new ZoomCommand(totalZoom);
		commandManager.executeCommand(command, model);
		commandManager.addMemento(model.createMemento());
	}

	public void loadModel(PerspectiveModel model) {
		if(this.model.getX() != model.getX()) {
			this.model.setX(model.getX());
		}
		if(this.model.getY() != model.getY()) {
			this.model.setY(model.getY());
		}
		if(this.model.getScale() != model.getScale()) {
			this.model.setScale(model.getScale());
			totalZoom = this.model.getScale();
		}
	}

	public void setTotalZoom(double totalZoom) {
		this.totalZoom = totalZoom;
	}

	public PerspectiveModel getPerspectiveModel() {
		return this.model;
	}

	public void undo() {
		commandManager.undo();
	}

	public void redo() {
		commandManager.redo();
	}

	/*public void zoom(Double scale) {

	}

	public void translate(Point2D translation) {

	}*/
	
}
