package UI;
import java.awt.Point;
import java.util.LinkedList;
import java.util.UUID;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;

import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import map.*;

public class UI extends Application {
    //Class containing grid (see below)
    private GridDisplay gridDisplay;
    protected Map map;

    //Class responsible for displaying the grid containing the Rectangles
    public class GridDisplay {

        public static final double WINDOW_SIZE = 1000;
        public static final int INIT_GRID_SIZE = 49;

        public final double initSize;
        
        public double gridSize;
        public double tileSize;
        public double scale;
        public Base base;
        
        public Pane pane = new Pane();
        public LinkedList<Drone> drones = new LinkedList<Drone>();
        public Group display = new Group(pane);
        
        public GridDisplay() {
        	resizeGrid(INIT_GRID_SIZE);
        	initSize = INIT_GRID_SIZE;
        }
        
        public GridDisplay(int gridSize) {
        	resizeGrid(gridSize);
        	initSize = WINDOW_SIZE / gridSize;
        }

        public void resizeGrid(int n){
        	gridSize = n;
        	double newtileSize = WINDOW_SIZE / gridSize;
        	if(tileSize != 0){
        		scale = (newtileSize / tileSize);
		    	pane.setScaleX(scale);
		    	pane.setScaleY(scale);
        	}
        	else{
        		scale = 1;
        	}
        	
        	tileSize = newtileSize;
        }
        
        public void insetBlank(GridDisplay gridDisplay, int x, int y){
        	Empty empty = new Empty(gridDisplay, x, y);
        	this.pane.getChildren().add(empty);
        }
        public void moveDrone(UUID id, Point p){
        	for (int i = 0; i<gridDisplay.drones.size(); i++){
        		if (gridDisplay.drones.get(i).ID.equals(id)){
        			gridDisplay.drones.get(i).point.x = p.x;
        			gridDisplay.drones.get(i).point.y = p.y;
        		}
        	}
        }
        public void moveDrone(UUID ID, int dir){
        	for (int i = 0; i<gridDisplay.drones.size();i++){
        		if (gridDisplay.drones.get(i).ID.equals(ID)){
        			switch(dir){
        			case 1: gridDisplay.drones.get(i).setX(gridDisplay.drones.get(i).point.x+1); break;
        			case 2: gridDisplay.drones.get(i).setX(gridDisplay.drones.get(i).point.x-1); break;
        			case 3: gridDisplay.drones.get(i).setY(gridDisplay.drones.get(i).point.y+1); break;
        			case 4: gridDisplay.drones.get(i).setY(gridDisplay.drones.get(i).point.y-1); break;
        			}
        		}
        	}
        }
        
    }


    @Override
    public void start(Stage primaryStage) {
    	//Represents the grid with Rectangles
        gridDisplay = new GridDisplay();
        Pane pane = gridDisplay.pane;
        Group display = gridDisplay.display;
        Canvas canvas = new Canvas(GridDisplay.WINDOW_SIZE, GridDisplay.WINDOW_SIZE);
        pane.setPrefHeight(GridDisplay.WINDOW_SIZE);
        pane.setPrefWidth(GridDisplay.WINDOW_SIZE);
        
        Scene scene = new Scene(display, GridDisplay.WINDOW_SIZE-12, GridDisplay.WINDOW_SIZE-12);
        
        World world = new World(new Point(0,0), GridDisplay.INIT_GRID_SIZE);
		Map map = new Map(world);
		this.map = map;
		UIcontrol uic = new UIcontrol(map, this.gridDisplay);
		map.UI = uic;
		try {
			Thread.currentThread().sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		LinkedList<Tuple> items = map.RetrieveTuples();
				
		pane.getChildren().clear();
		Base b = new Base(gridDisplay, 0, 0);
		gridDisplay.base = b;
        pane.getChildren().add(b);
        pane.getChildren().add(canvas);
        pane.setStyle("-fx-background-color: black;");
        for(Tuple t : items){
        	String str = t.getElementAt(String.class, 0);
        	//System.out.println(str);
        	int x = t.getElementAt(Integer.class, 1);
        	int y = t.getElementAt(Integer.class, 2);
        	Shape c = null;
        	switch (str) {
			case "GOLD": c = new Gold(gridDisplay, x, y); break;
			case "TREE": c = new Tree(gridDisplay, x, y); break;
			case "BASE": break;//base = nothing
			case "WATER": c = new Drone(gridDisplay, x, y, uic); break;
			case "EXPDRONE" : c = new Drone(gridDisplay, x, y, uic); break;
			case "HARDRONE" : c = new Drone(gridDisplay, x, y, uic); break;
			}
            if(c != null) {
            	//System.out.println(c.getClass().toString());
            	pane.getChildren().add(c);
            }
        }//*/       
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("Test grid display");
        primaryStage.setScene(scene);
        primaryStage.show(); 
        
        try {
        	//Thread.sleep(200);
        	//Platform.runLater(() -> gridDisplay.pane.getChildren().add(new Tree(gridDisplay, -1, 0)));
			//Thread.sleep(1000);
			//Platform.runLater(() -> gridDisplay.resizeGrid(69));
			//Thread.sleep(2000);
			//Platform.runLater(() -> gridDisplay.pane.getChildren().add(new Tree(gridDisplay, 1, 0)));
			//Thread.sleep(2000);
			//Platform.runLater(() -> gridDisplay.resizeGrid(89));
			//Thread.sleep(500);
			//Platform.runLater(() -> gridDisplay.insetBlank(gridDisplay, 10, 10));
			Thread.sleep(200);
			//Platform.runLater(() -> gridDisplay.moveDrone(3, 2));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        map.run();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    static abstract class RectangleShape extends Rectangle{
    	public GridDisplay gridDisplay;
    	
    	public RectangleShape(GridDisplay gridDisplay, int x, int y, Paint c){
    		super(gridDisplay.tileSize, gridDisplay.tileSize, c);
    		this.gridDisplay = gridDisplay;
    		setX(x);
    		setY(y);
    	}
    	public void setX(int x){
    		double zero = (gridDisplay.base == null) ? GridDisplay.WINDOW_SIZE/2 : gridDisplay.base.getTranslateX();
    		double offset = gridDisplay.tileSize * x;
    		double newX = zero + offset;
    		setTranslateX(newX);
    	}
    	public void setY(int y){
    		double zero = (gridDisplay.base == null) ? GridDisplay.WINDOW_SIZE/2 : gridDisplay.base.getTranslateX();
    		double offset = gridDisplay.tileSize * y;
    		double newY = zero+offset;
    		setTranslateY(newY);
    	}
    	
    }
    
    static abstract class CoordinateShape extends Circle {
    	public GridDisplay gridDisplay;
    	
    	public CoordinateShape(GridDisplay gridDisplay, int x, int y, Paint c){
    		super(x,y,gridDisplay.tileSize/2,c);
    		//System.out.println(gridDisplay.tileSize/2);
    		this.gridDisplay = gridDisplay;
			setX(x);
    		setY(y);
    	}
    	
    	public void setX(int x){
    		this.setCenterX(x);
    		this.setLayoutY(x);
    		double zero = (gridDisplay.base == null) ? GridDisplay.WINDOW_SIZE/2 : gridDisplay.base.getTranslateX();
    		double offset = gridDisplay.tileSize * x;
    		double newX = zero + offset;
    		//System.out.println("x:"+x+" - os:"+offset+" - z:"+zero+" - nx:"+newX);
    		setTranslateX(newX);    		
    	}
    	
    	public void setY(int y){
    		this.setCenterY(y);
    		this.setLayoutY(y);
    		double zero = (gridDisplay.base == null) ? GridDisplay.WINDOW_SIZE/2 : gridDisplay.base.getTranslateY();
    		double offset = gridDisplay.tileSize * y;
    		double newY = zero + offset;
    		//System.out.println("y:"+y+" - os:"+offset+" - z:"+zero+" - ny:"+newY);
    		setTranslateY(newY);
    	}
    	
    }
    
    static class Base extends CoordinateShape {
		static final Paint c = Color.RED;		
		public Base(GridDisplay gridDisplay, int x, int y){
			super(gridDisplay, x, y, c);
		}
	}
	
	static class Drone extends CoordinateShape {
		static final Paint c = Color.BLUE;		
		UUID ID;
		Point point = new Point(0,0);
		public Drone(GridDisplay gridDisplay, int x, int y, UIcontrol uic){
			super(gridDisplay, x, y, c);
			point.x = x;
			point.y = y;
			this.ID = uic.getID(point);
			gridDisplay.drones.add(this);
		}
	}
	
	static class Tree extends CoordinateShape{
		static final Paint c = Color.GREEN;
		public Tree(GridDisplay gridDisplay, int x, int y){
			super(gridDisplay, x, y, c);
		}
	}
	
	static class Gold extends CoordinateShape {
		static final Paint c = Color.GOLD;
		public Gold(GridDisplay gridDisplay, int x, int y){
			super(gridDisplay, x, y, c);
		}
	}
    
    static class Box extends Rectangle {
		static final Paint c = Color.BLACK;		
		public Box(double sizeN){
			super(sizeN, sizeN, c);			
		}
	}
    static class Empty extends RectangleShape{
    	static final Paint c = Color.WHITE;
    	public Empty(GridDisplay gridDisplay, int x, int y){
    		super(gridDisplay, x, y, c);
       	}
    	public void reSize(){
    		double size = gridDisplay.tileSize;
    		super.setX(size);
    		super.setY(size);
    	}
    }
}
