package application;

import java.util.Random;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.*;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;




public class Main  extends Application  {
    public static  int Element_Dimensions = 40;
    public static  int Width;
    public static  int Height;
    public static  int xElements;
    public static  int yElements;
    public static  int totalbombnumber;
    public static  int totalbombnumber1;
    public static  int bombnumber;
    public static  int marknumber;
    public static  int HYPERBOMB;
    public static  int HiddenElements;
    public static  int totalseconds;
    public static  int marktime;
    public static  int endgame = 0;
    public static  int tries;
    public static int[][] minetable = new int[17][17];
    
    public static Element[][] GameArray;
    
    public static Scene scene;
    public static Stage stage1; 
    static File file1;
 

    public static Parent SetUpGame() {
        Pane root = new Pane();
        root.setPrefSize(Width, Height);
         
        bombnumber = totalbombnumber1;

         totalbombnumber = bombnumber;
        HiddenElements = xElements * yElements;
        
        for (int y = 0; y < yElements; y++) {
        	for (int x = 0; x <  xElements; x++) {
        		minetable[x][y] = 0;
        	}
        }
        
        Random rand1 = new Random();
        Random rand2 = new Random();
        
        try {			
			file1 = new File("medialab\\mines.txt");
		
			if(!file1.exists()) {
				file1.createNewFile();
			}
			PrintWriter pw1 = new PrintWriter(file1);
        
        while(bombnumber > 0) {
        	int i = rand1.nextInt(((xElements-1) - 0) + 1) + 0;
        	int j = rand2.nextInt(((yElements-1) - 0) + 1) + 0;
        	
        	if(minetable[i][j] != 1) {
        		minetable[i][j] = 1;
        		if((bombnumber == 1) && (HYPERBOMB == 1))  minetable[i][j] = 2;
        		bombnumber--;
        	}
        	pw1.println(i+" "+j+" "+(minetable[i][j]-1));
        	
        
        }
        pw1.close();
        }catch (IOException e) {
			e.printStackTrace();
			
		}
		
        
      

        for (int y = 0; y < yElements; y++) {
            for (int x = 0; x <  xElements; x++) {
            	Element el;
            	if(minetable[x][y] >= 1) {
            		 el = new Element(x, y, true);
            		
            	}
            	else {
                     el = new Element(x, y, false);
            	}
  
                GameArray[x][y] = el;
                root.getChildren().add(el);
            }
        }

        for (int y = 0; y < yElements; y++) {
            for (int x = 0; x < xElements; x++) {
                Element el = GameArray[x][y];
                long bombs = 0;

                if (el.hasBomb)
                    continue;
                
                for(Element elem : getNeighbors(el)) {
                 	if (elem.hasBomb) bombs++;
                }

                if (bombs > 0)
                    el.text.setText(String.valueOf(bombs));
            }
        }

        return root;
    }
 

 
    
    public static List<Element> getNeighbors(Element el) {
        List<Element> neighbors = new ArrayList<>();
        
        for(int k = -1; k < 2; k++){
        	for(int l = -1; l < 2; l++){
        		if(l == 0 && k == 0) continue;
        		int a =  el.x + k;
        		int b =  el.y + l;

        		if(a >=0 && a < xElements && b >= 0 && b < yElements){
        			neighbors.add(GameArray[a][b]);
        		}
        	}
        }
        
        return neighbors;
    }

    public static class Element extends StackPane {
        private int x, y;
        private boolean hasBomb;
        private boolean flagged = false;
        private boolean Open = false;

        private Rectangle border = new Rectangle(Element_Dimensions - 2, Element_Dimensions - 2);
        private Text text = new Text();
        
        
        /**
         * Class constructor of an element of the minesweeper(rectangle).The position of the element is determined
         * by the x,y parameter and the hasbomb parameter determines whether it contains a bomb or not.
         * 
         * @param x the position in the X axis of the Element(block) the we construct.
         * @param y the position in the Y axis of the Element(block) the we construct.
         * @param hasBomb the parameter that determines whether the Element has a bomb or not. 
         */
        public Element(int x, int y, boolean hasBomb) {
            this.x = x;
            this.y = y;
            this.hasBomb = hasBomb;

            border.setStroke(Color.BLUE);
            border.setFill(Color.BLACK);
         
            

            text.setFont(Font.font(18));
            text.setText(hasBomb ? "X" : "");
            text.setVisible(false);

            getChildren().addAll(border, text);

            setTranslateX(x * Element_Dimensions);
            setTranslateY(y * Element_Dimensions);

            setOnMouseClicked(e -> 
            {
            	if(e.getButton() == MouseButton.PRIMARY) {
            		if(endgame == 0 && Open != true) tries++;
            		open();
            	}
            	
            	else if(e.getButton() == MouseButton.SECONDARY) {
            		flagit();       		
            	}
     	
            });
            
        }
        
        /**
         * This method opens a block in the  minesweeper game, revealing it's content.
         * If there is a bomb inside the block,the game is over and the player loses.
         * Else, the number of neighbors containing bombs, of the block if exposed to the player.
         * 
         */

        public void open() {
        	if(endgame == 1) return;
        	
            if (Open)
                return;

            if (hasBomb) {
               System.out.println("Defeat!");
               SampleController.starter = 0;
               totalseconds = totalseconds - SampleController.seconds; 
               try {
               PrintWriter printer = new PrintWriter(new FileWriter("medialab\\details.txt", true));
               printer.println("Total mines : "+ totalbombnumber1 + "| Number of tries : " + tries + "| Game time : "+ totalseconds + "| LOSER ");
               printer.close();
               }
               catch (IOException e) {
				e.printStackTrace();
				
               }
               
               endgame = 1;
               text.setVisible(true);
               border.setFill(Color.RED);

               return;
              
            }

            else if(flagged == true) marknumber = marknumber + 1;
            Open = true;
            text.setVisible(true);
            border.setFill(null);
            HiddenElements--;
            if(HiddenElements == totalbombnumber) {
                System.out.println("Victory!");
                SampleController.starter = 0;
                totalseconds = totalseconds - SampleController.seconds; 
                try {
                PrintWriter printer = new PrintWriter(new FileWriter("medialab\\details.txt", true));
                printer.println("Total mines : "+ totalbombnumber1 + "| Number of tries : " + tries + "| Game time : "+ totalseconds + "| WINNER ");
                printer.close();
                }
                catch (IOException e) {
 				e.printStackTrace();
 				
                }
                endgame = 1;              
                return;

            }
            
            if (text.getText().isEmpty()) {
            	for(Element elem : getNeighbors(this)) {
            		elem.open();
            	}
            }
        }
        
        /**
         * This method is used to mark a block without revealing it's content.
         * If used to an unmarked block,the block becomes marked and it's color turns into Green.
         * If the block is marked,it becomes unmarked and it's color switches back to black.
         */
        
        public void flagit() {
        	
        	if (endgame == 1) return;
        	
        	if (Open)
                 return;
        	
        	if(flagged == false) {
        		if(marknumber == 0) return;
        		
        		marknumber = marknumber - 1;
        		marktime = marktime +1;
        		if(minetable[x][y] == 2 && marktime <= 4) {
        			show_row();
        			show_column();
        			totalbombnumber--;
        			HiddenElements--;
        			marknumber = marknumber + 1;
        			if(HiddenElements == totalbombnumber) {
                        System.out.println("Victory!");                    
                        SampleController.starter = 0;
                        totalseconds = totalseconds - SampleController.seconds; 
                        try {
                        PrintWriter printer = new PrintWriter(new FileWriter("medialab\\details.txt", true));
                        printer.println("Total mines : "+ totalbombnumber1 + "| Number of tries : " + tries + "| Game time : "+ totalseconds + "| WINNER ");
                        printer.close();
                        }
                        catch (IOException e) {
         				e.printStackTrace();         				
                        }                        
                        endgame = 1;
                        return;
                    }
        		}
        		else { 
        		 border.setFill(Color.GREEN);
        		 flagged = true;
        		}
             } 
        	
        	else if(flagged == true) {
        	 marknumber = marknumber + 1;
       		 border.setFill(Color.BLACK);
       		 flagged = false;
        	
        	}
        }
        
        /**
         * This method reveals all the blocks of a specific row.
         * It is used when the SuperMine is marked in the first 4 times the method flagit() is called.
         * If a block contains a bomb,the letter X is displayed,else the number of neighbors containing a bomb
         * of the block is displayed
         */
        public void show_row() {
        	for(int i = 0; i < yElements; i++) {
        		if(GameArray[x][i].flagged == true) marknumber++;

        		if(GameArray[x][i].hasBomb && !(GameArray[x][i].Open) && i!=y) {
        			totalbombnumber--;
        			HiddenElements = HiddenElements - 1;
        			
        		}
        		else if(!(GameArray[x][i].Open) && i!=y) HiddenElements = HiddenElements - 1;
     		
        		GameArray[x][i].text.setVisible(true);
            	GameArray[x][i].border.setFill(null);
        	}
        }
        
        /**
         * This method reveals all the blocks of a specific column.
         * It is used when the SuperMine is marked in the first 4 times the method flagit() is called.
         * If a block contains a bomb,the letter X is displayed,else the number of neighbors containing a bomb
         * of the block is displayed
         */
        
        public void show_column() {
        	for(int i = 0; i < xElements; i++) {
        		if(GameArray[i][y].flagged == true) marknumber++;
        		

        		if(GameArray[i][y].hasBomb && !(GameArray[i][y].hasBomb && i!=x)) {
        			totalbombnumber--;
        			HiddenElements = HiddenElements - 1;
        		}
        		else if(!GameArray[i][y].Open && i!=x) HiddenElements = HiddenElements - 1;
        		GameArray[i][y].text.setVisible(true);
            	GameArray[i][y].border.setFill(null);
        	}
        }       	
   		
    }
    
    
    public static void begin() {      	        
        scene = new Scene(SetUpGame());
        stage1.setScene(scene);
        stage1.setTitle("Medialab Minesweeper");
        stage1.show();
    	
    }
    
    public static void solving() {
    	SampleController.loader = 0;
    	
    	for (int y = 0; y < yElements; y++) {
            for (int x = 0; x <  xElements; x++) {
            	GameArray[x][y].text.setVisible(true);
            	GameArray[x][y].border.setFill(null);
            }
    	}
         totalseconds = totalseconds - SampleController.seconds; 
         try {
         PrintWriter printer = new PrintWriter(new FileWriter("medialab\\details.txt", true));
         printer.println("Total mines : "+ totalbombnumber1 + "| Number of tries : " + tries + "| Game time : "+ totalseconds + "| LOSER ");
         printer.close();
         }
         catch (IOException e) {
			e.printStackTrace();
			
         }
         
         endgame = 1;
    }
    
    public static void details() {
    	try {
    	tailFile(Paths.get("medialab\\details.txt"), 5).forEach(line -> System.out.println(line));
    	}catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    public static final List<String> tailFile(final Path source, final int noOfLines) throws IOException {
		try (Stream<String> stream = Files.lines(source)) {
			FileBuffer fileBuffer = new FileBuffer(noOfLines);
			stream.forEach(line -> fileBuffer.collect(line));
			return fileBuffer.getLines();
		}
	}

	private static final class FileBuffer {
		private int offset = 0;
		private final int noOfLines;
		private final String[] lines;

		public FileBuffer(int noOfLines) {
			this.noOfLines = noOfLines;
			this.lines = new String[noOfLines];
		}

		public void collect(String line) {
			lines[offset++ % noOfLines] = line;
		}

		public List<String> getLines() {
			return IntStream.range(offset < noOfLines ? 0 : offset - noOfLines, offset)
					.mapToObj(idx -> lines[idx % noOfLines]).collect(Collectors.toList());
		}
	}
    
    public static long countLineJava(String fileName) {

        Path path = Paths.get(fileName);
        long lines = 0;
        try {     
            lines = Files.lines(path).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }
    	

    @Override
	public void start(Stage primaryStage) {
		 
    	SampleController.loader = 0;
    	SampleController.starter = 0;
		try {
			
			stage1 = new Stage();
			FXMLLoader loader = new  FXMLLoader(getClass().getResource("Sample1.fxml"));	
			AnchorPane root = loader.load();
			Scene scene1 = new Scene(root);
			primaryStage.setScene(scene1);
			primaryStage.setTitle("Medialab Minesweeper");
	        primaryStage.show();
	    	
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
        
    }
		

    public static void main(String[] args) throws IOException { 	
        launch(args);
    }
}


	

