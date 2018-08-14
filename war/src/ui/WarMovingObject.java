package ui;

public class WarMovingObject extends WarObject {

	private double vX;
	private double vY;
	
	private double x;
	private double y;

	
	public WarMovingObject(String id, String imagePath, boolean isHidden, double x0, double y0, double targetX, double targetY, int time) {
		super(id, imagePath, isHidden, x0, y0);
		this.x = x0;
		this.y = y0;
		
		vX = (targetX - x0)/(time);
		vY = (targetY - y0)/(time);
	}

	public double getvX() {
		return vX;
	}

	public double getvY() {
		return vY;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void updateX(double x) {
		this.x = x;
	}

	public void updateY(double y) {
		this.y = y;
	}

	public void update() {
		
	}
}
