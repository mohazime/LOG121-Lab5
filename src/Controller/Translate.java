package Controller;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class Translate extends Operation {

	private ImageView imageView;
	private Point2D translation;

	public Translate(ImageView imageView, Point2D translation) {
		this.imageView = imageView;
		this.translation = translation;
	}

	@Override
	public void execute() {
		imageView.setTranslateX(imageView.getTranslateX() + translation.getX());
		imageView.setTranslateY(imageView.getTranslateY() + translation.getY());
	}

	@Override
	public void undo() {
		
	}
	
}
