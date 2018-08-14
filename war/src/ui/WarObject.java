package ui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WarObject extends VBox {
	
	private Label 		label;
	private ImageView	image;
	
	private double 		x0;
	private double 		y0;

	public WarObject(String id, String imagePath, boolean isHidden, double x0, double y0){
		this(id, imagePath, isHidden);
		this.x0 = x0;
		this.y0 = y0;
	}
	
	public WarObject(String id, String imagePath, boolean isHidden){
		super();
		label = new Label(id);
		image = new ImageView();
		image.setImage(new Image(imagePath));
		setHidden(isHidden);
		image.setFitHeight(30);
		image.setFitWidth(30);
		this.getChildren().add(label);
		this.getChildren().add(image);	
		this.getStylesheets().add("ui/warObj.css");
	}
	
	public void setHidden(boolean isHidden){
		image.setVisible(!isHidden);
	}
	
	public String getID(){
		return label.getText();
	}
	
	public void setX0(double x0){
		this.x0 = x0;
	}
	
	public void setY0(double y0){
		this.y0 = y0;
	}
	
	public double getX0(){
		return x0;
	}
	
	public double getY0(){
		return y0;
	}

	public String toString() {
		return this.getClass().getSimpleName() + " #" + getID();
	}
	
	
	
}
