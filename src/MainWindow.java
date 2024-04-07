import Controller.ImageController;
import Controller.CommandManager;
import Controller.PerspectiveController;
import Model.ImageModel;
import View.PersepectiveImageView;
import View.ThumbnailImageView;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainWindow extends Application {
	public static final double HEIGHT = 600;
	public static final double WIDTH = 750;
	public static final double LIMIT = WIDTH / 2;

	private ImageView thumbnailImageView, perspectiveImageView1, perspectiveImageView2;
	private ImageModel iModel1, iModel2, iModel3;
	private ThumbnailImageView tImageView;
	private PersepectiveImageView pImageView1, pImageView2;
	private ImageController iController;
	private PerspectiveController pController1, pController2;
	private MenuBar menuBar;
	private VBox root = new VBox();

	@Override
	public void start(Stage primaryStage) {
		CommandManager operationManager = CommandManager.getInstance();
		iModel1 = new ImageModel();
		iModel2 = new ImageModel();
		iModel3 = new ImageModel();
		tImageView = new ThumbnailImageView();
		pImageView1 = new PersepectiveImageView();
		pImageView2 = new PersepectiveImageView();
		pImageView1.getImageView().toBack();
		pImageView2.getImageView().toBack();

		thumbnailImageView = tImageView.getImageView();
		perspectiveImageView1 = pImageView1.getImageView();
		perspectiveImageView2 = pImageView2.getImageView();

		iController = new ImageController(iModel1, iModel2, iModel3);
		pController1 = new PerspectiveController(operationManager, pImageView1);
		pController2 = new PerspectiveController(operationManager, pImageView2);
		iModel1.addObserver(tImageView);
		iModel2.addObserver(pImageView1);
		iModel3.addObserver(pImageView2);

		menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem saveItem = new MenuItem("Save");
		MenuItem openItem = new MenuItem("Open");
		MenuItem exitItem = new MenuItem("Exit");
		fileMenu.getItems().addAll(openItem, saveItem, new SeparatorMenuItem(), exitItem);
		Menu optionMenu = new Menu("Options");
		Menu copyMenu = new Menu("Copy");
		Menu editMenu = new Menu("Edit");
		MenuItem pasteItem = new MenuItem("Paste");
		MenuItem allItem = new MenuItem("Copy all");
		MenuItem scaleItem = new MenuItem("Copy scale");
		MenuItem translationItem = new MenuItem("Copy translation");
		MenuItem undoItem = new MenuItem("Undo");
		MenuItem redoItem = new MenuItem("Redo");
		copyMenu.getItems().addAll(allItem, scaleItem, translationItem);
		optionMenu.getItems().addAll(copyMenu, pasteItem);
		editMenu.getItems().addAll(undoItem, redoItem);
		
		menuBar.getMenus().addAll(fileMenu, editMenu, optionMenu);
		menuBar.setUseSystemMenuBar(true);
		root.getChildren().add(menuBar);

		openItem.setOnAction(e -> openImage(""));
		saveItem.setOnAction(e -> saveModels());
		exitItem.setOnAction(e -> primaryStage.close());

		undoItem.setOnAction(e -> pController1.undo());
		undoItem.setOnAction(e -> pController2.undo());
		redoItem.setOnAction(e -> pController1.redo());
		redoItem.setOnAction(e -> pController2.redo());

		Scene scene = new Scene(root, WIDTH, HEIGHT);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Image viewer");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public void saveModels() {
		System.out.println("Saving");
	}

	public void loadModels(String path) {
		System.out.println("Loading");
	}

	public void openImage(String path) {
		iController.selectImage();
		Pane thumbnail = new Pane();
		Pane perspective1 = new Pane();
		Pane perspective2 = new Pane();

		perspective1.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) {
				perspective1.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
			}
		});

		perspective2.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) {
				perspective2.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
			}
		});
		
		thumbnailImageView.setFitHeight(150);
		thumbnailImageView.setFitWidth(200);
		thumbnailImageView.setPreserveRatio(true);

		perspectiveImageView1.setFitHeight(HEIGHT);
		perspectiveImageView1.setFitWidth(WIDTH / 2);
		perspectiveImageView1.setPreserveRatio(true);

		perspectiveImageView2.setFitHeight(HEIGHT);
		perspectiveImageView2.setFitWidth(WIDTH / 2);
		perspectiveImageView2.setPreserveRatio(true);

		perspectiveImageView1.setOnMouseEntered(e -> pController1.handleMouseEntered());
		perspectiveImageView2.setOnMouseEntered(e -> pController2.handleMouseEntered());

		perspectiveImageView1.setOnMousePressed(e -> pController1.handleMousePressed(e));
		perspectiveImageView2.setOnMousePressed(e -> pController2.handleMousePressed(e));

		perspectiveImageView1.setOnMouseReleased(e -> pController1.handleMouseReleased(e));
		perspectiveImageView2.setOnMouseReleased(e -> pController2.handleMouseReleased(e));

		perspectiveImageView1.setOnMouseDragged(e -> pController1.handleMouseDragged(e));
		perspectiveImageView2.setOnMouseDragged(e -> pController2.handleMouseDragged(e));

		perspectiveImageView1.setOnScroll(e -> pController1.handleMouseScrolled(e));
		perspectiveImageView2.setOnScroll(e -> pController2.handleMouseScrolled(e));
		
		thumbnail.getChildren().add(thumbnailImageView);
		perspective1.getChildren().add(perspectiveImageView1);
		perspective2.getChildren().add(perspectiveImageView2);

		System.out.println(perspective1.localToScene(0, 0).getX() + ", " + perspective1.localToScene(0, 0).getY());
		HBox thumbnailBox = new HBox();
		thumbnailBox.getChildren().add(thumbnail);
		thumbnailBox.setAlignment(Pos.CENTER);

		HBox persectiveBox = new HBox(20);
		persectiveBox.getChildren().addAll(perspective1, perspective2);
		persectiveBox.setAlignment(Pos.CENTER);
		persectiveBox.setLayoutY(HEIGHT / 2);
		root.getChildren().addAll(persectiveBox, thumbnailBox);

		VBox.setVgrow(persectiveBox, Priority.ALWAYS);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
