import java.util.UUID;

import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;



public class UI extends Application {
    //Class containing grid (see below)
    private GridDisplay gridDisplay;
 
    //Class responsible for displaying the grid containing the Rectangles
    public class GridDisplay {

        public static final double ELEMENT_SIZE = 800;
        public static final int GRID = 4;
        public static final double GAP = ELEMENT_SIZE / GRID;

        private TilePane tilePane = new TilePane();
        private Group display = new Group(tilePane);
        private int nRows;
        private int nCols;

        public GridDisplay(int nRows, int nCols) {
            tilePane.setPrefTileHeight(GAP);
            tilePane.setPrefTileWidth(GAP);
            setColumns(nCols);
            setRows(nRows);
        }

        public void setColumns(int newColumns) {
            nCols = newColumns;
            tilePane.setPrefColumns(nCols);
            createElements();
        }

        public void setRows(int newRows) {
            nRows = newRows;
            tilePane.setPrefRows(nRows);
            createElements();
        }

        public Group getDisplay() {
            return display;
        }

        private void createElements() {
            tilePane.getChildren().clear();
            for (int i = 0; i < nCols; i++) {
                for (int j = 0; j < nRows; j++) {
                    tilePane.getChildren().add(createElement(i,j));
                }
            }
        }

        private Shape createElement(int x, int y) {
        	Box box = new Box(GAP);
            Drone drone = new Drone(x,y,GAP/2);
            if(x==1) box.setVisible(false);
            return (x==2 && y ==2 || x==3 && y==3) ? drone : box;
        }

    }

    @Override
    public void start(Stage primaryStage) {

        //Represents the grid with Rectangles
        gridDisplay = new GridDisplay(GridDisplay.GRID, GridDisplay.GRID);
        TilePane grid = gridDisplay.tilePane;
        grid.setPrefHeight(GridDisplay.ELEMENT_SIZE);
        grid.setPrefWidth(GridDisplay.ELEMENT_SIZE);
        Scene scene = new Scene(gridDisplay.getDisplay(), GridDisplay.ELEMENT_SIZE-15, GridDisplay.ELEMENT_SIZE-15);
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
    
    static class Base extends Circle {
		static final Paint c = Color.RED;		
		public Base(int x, int y, double size){
			super(x, y, size, c);
		}
	}
	
	static class Drone extends Circle {
		static final Color c = Color.BLUE;		
		public Drone(int x, int y, double size){
			super(x,y,size,c);
		}
	}
	
	static class Tree extends Circle {
		static final Color c = Color.GREEN;
		public Tree(int x, int y, double size){
			super(x,y,size, c);
			}
		}
	
	static class Gold extends Circle {
		static final Color c = Color.GOLD;
		public Gold (int x, int y, double size){
			super(x,y,size,c);
		}
	}
    
    static class Box extends Rectangle {
		static final Color c = Color.BLACK;		
		public Box(double sizeN){
			super(sizeN, sizeN, c);			
		}
	}
}
