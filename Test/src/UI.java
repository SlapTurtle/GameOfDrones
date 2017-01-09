import java.util.UUID;

import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
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
    private static GridDisplay gridDisplay;

    //Class responsible for displaying the grid containing the Rectangles
    public class GridDisplay {

        public static final double WINDOW_SIZE = 1000;
        public static final int INIT_GRID_SIZE = 31;
        
        public double gridSize;
        public double tileSize;
        public Base base;
        
        public Pane pane = new Pane();
        public Group display = new Group(pane);

        public GridDisplay() {
        	resizeGrid(INIT_GRID_SIZE);
        }
        
        public GridDisplay(int gridSize) {
        	resizeGrid(gridSize);
        }

        public void resizeGrid(int n){
        	gridSize = n;
        	tileSize = WINDOW_SIZE / gridSize;
        	pane.setScaleX(tileSize/2);
        	pane.setScaleY(tileSize/2);
        }

        /*private Shape createElement(int x, int y) {
        	Box box = new Box(GAP);
            Drone drone = new Drone(x,y,GAP/2);
            if(x==1) box.setVisible(false);
            return (x==2 && y ==2 || x==3 && y==3) ? drone : box;
        }*/

    }

    @Override
    public void start(Stage primaryStage) {
    	//Represents the grid with Rectangles
        gridDisplay = new GridDisplay();
        Pane pane = gridDisplay.pane;
        Group display = gridDisplay.display;
        pane.setPrefHeight(gridDisplay.WINDOW_SIZE);
        pane.setPrefWidth(gridDisplay.WINDOW_SIZE);
        Scene scene = new Scene(display, gridDisplay.WINDOW_SIZE, gridDisplay.WINDOW_SIZE);
        
        World world = new World(GridDisplay.INIT_GRID_SIZE);
		Map map = new Map(world);
		map.
		
		pane.getChildren().clear();
		Base b = new Base(gridDisplay, 0, 0);
        gridDisplay.base = b;
        pane.getChildren().add(b);
        
        for (int i = 0; i < gridDisplay.gridSize; i++) {
            for (int j = 0; j < gridDisplay.gridSize; j++) {
            	Shape c = null;
            	switch (N[i][j]) {
				case 1: c = new Gold(gridDisplay, i, j); break;
				case 2: c = new Tree(gridDisplay, i,j); break;
				case 3: //base = nothing
				case 4: c = new Drone(gridDisplay, i,j); break;
				}
                if(c != null) {
                	System.out.println(c.getClass().toString());
                	pane.getChildren().add(c);
                }
            }
        }
        
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("Test grid display");
        primaryStage.setScene(scene);
        primaryStage.show();       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    static abstract class CoordinateShape extends Circle {
    	public GridDisplay gridDisplay;
    	
    	public CoordinateShape(GridDisplay gridDisplay, int x, int y, Paint c){
    		super(x,y,1,c);
    		this.gridDisplay = gridDisplay;
    		if(!getClass().equals(Base.class)) {
    			setX(x);
        		setY(y);
    		}
    		else {
    			setTranslateX(gridDisplay.WINDOW_SIZE / 2);
    			setTranslateY(gridDisplay.WINDOW_SIZE / 2);
    		}
    	}
    	
    	public void setX(int x){
    		double zero = gridDisplay.base.getTranslateX();
    		double offset = gridDisplay.tileSize * x;
    		double newX = zero + offset;
    		setTranslateX(newX);
    		this.setCenterX(x);
    	}
    	
    	public void setY(int y){
    		double zero = gridDisplay.base.getTranslateY();
    		double offset = gridDisplay.tileSize * y;
    		double newY = zero + offset;
    		setTranslateY(newY);
    		this.setCenterY(y);
    	}
    }
    
    static class Base extends CoordinateShape {
		static final Paint c = Color.RED;		
		public Base(GridDisplay gridDisplay, int x, int y){
			super(gridDisplay, x, y, c);
		}
	}
	
	static class Drone extends CoordinateShape {
		static final Color c = Color.BLUE;		
		public Drone(GridDisplay gridDisplay, int x, int y){
			super(gridDisplay, x, y, c);
		}
	}
	
	static class Tree extends CoordinateShape{
		static final Color c = Color.GREEN;
		public Tree(GridDisplay gridDisplay, int x, int y){
			super(gridDisplay, x, y, c);
		}
	}
	
	static class Gold extends CoordinateShape {
		static final Color c = Color.GOLD;
		public Gold(GridDisplay gridDisplay, int x, int y){
			super(gridDisplay, x, y, c);
		}
	}
    
    static class Box extends Rectangle {
		static final Color c = Color.BLACK;		
		public Box(double sizeN){
			super(sizeN, sizeN, c);			
		}
	}
}
