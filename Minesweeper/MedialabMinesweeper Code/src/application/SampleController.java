package application;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import application.Main.Element;


public class SampleController   {
	
	@FXML
	private Label MarkLabel;
	@FXML
	private Label BombLabel;
	@FXML
	private Label ClockLabel;
	@FXML
	private AnchorPane myPane; 
	
	
	public static int difficulty;
	public static int mines;
	public static int seconds;
	public static int seconds1;
	public static int supermine;
	public static int loader;
	public static int starter;

	public static boolean begin = false;
	static File file;
	public static Scene scene2;
	public static Stage stage2;
	
	Timer timer;
	int second = 0;
	
	
	public void GiveNumbers() throws IOException {
			
			FXMLLoader loader1 = new  FXMLLoader(getClass().getResource("Sample2.fxml"));	
			AnchorPane root2 = loader1.load();
			scene2 = new Scene(root2);
			stage2 = new Stage();
			stage2.setScene(scene2);
			stage2.setTitle("Medialab Minesweeper Settings");
	        stage2.show();	
	}

	
	public static void CreateFile() {	
	        stage2.close();
	        
		try {			
			file = new File("medialab\\SCENARIO-ID.txt");		
			if(!file.exists()) {
				file.createNewFile();
			}
			
			PrintWriter pw = new PrintWriter(file);
			pw.println(difficulty);
			pw.println(mines);
			pw.println(seconds1);
			pw.print(supermine);
			pw.close();			
			}
			catch (IOException e) {
				e.printStackTrace();
				
			}	
	}
	
	
	public  void Load() throws InvalidDescriptionException  {		
		
		long ints = 0;
		int o;
		Scanner scanner = null;	
		seconds = seconds1;
		try {
            scanner = new Scanner(new File("medialab\\SCENARIO-ID.txt"));
        } catch (FileNotFoundException e) { 
        	System.out.println("You Need to Create A Scenario!");
        	return;
        }
		
		
		try {
			long lines = Main.countLineJava("medialab\\SCENARIO-ID.txt");
			if(lines != 4) 
				throw new InvalidDescriptionException("No such File Permitted!"); 
			
			Scanner checker = new Scanner(new File("medialab\\SCENARIO-ID.txt"));
			while (checker.hasNextInt())  {
				o = checker.nextInt();
			     ints++;
			  } 
			checker.close();
			if(ints != 4) throw new InvalidDescriptionException("No such File Permitted!"); 	
		}
		catch(Exception e){
			System.out.println("A Problem Occured :"+e);
			return;
		}
		
		if(Main.endgame == 0 && this.timer != null) timer.cancel();
	
		
        
        
        	int J = scanner.nextInt();
        	if(J == 1) {
        		Main.Width = 360;
        		Main.Height = 360;
        	}
        	if( J == 2) {
        		Main.Width = 640;
        		Main.Height = 640;
        	}
        	   	
        	Main.totalbombnumber1 = scanner.nextInt();
        	Main.marknumber = Main.totalbombnumber1;  
        	
        	seconds = scanner.nextInt();   
        	
        	Main.HYPERBOMB = scanner.nextInt();
       	
        	Main.xElements = Main.Width / Main.Element_Dimensions;
        	Main.yElements = Main.Height / Main.Element_Dimensions;
        	Main.GameArray = new Element[Main.xElements][Main.yElements];       	

        	loader = 1;
        	System.out.println("Game Loaded");
     	
	}
	
	public void StartGame() throws InvalidDescriptionException {
		
		long ints = 0;
		int o;
		
		try {
          Scanner  checkIFitExists = new Scanner(new File("medialab\\SCENARIO-ID.txt"));
        } catch (FileNotFoundException e) {
        	System.out.println("You Need to Create A Scenario!");
        	return;
        }
		
		try {
			long lines = Main.countLineJava("medialab\\SCENARIO-ID.txt");
			if(lines != 4) 
				throw new InvalidDescriptionException("No such File Permitted!"); 
			
			Scanner checker = new Scanner(new File("medialab\\SCENARIO-ID.txt"));
			while (checker.hasNextInt())  {
				o = checker.nextInt();
			     ints++;
			  } 
			checker.close();
			if(ints != 4) throw new InvalidDescriptionException("No such File Permitted!"); 
			
			if(ints != 4) return;
			
		}
		catch(Exception e){
			System.out.println("A Problem Occured :"+e);
			return;
		}
		
		if(loader == 0) {
			System.out.println("YOU NEED TO LOAD A SCENARIO FIRST!");
			return;
		}
		
		else loader = 0;
		starter = 1;
		
		System.out.println("Game Started");
		Main.endgame = 0;
		timer = new Timer();
		Main.totalseconds = seconds;
		Main.tries = 0;
		Main.marktime = 0;
	    Main.begin();
	    BombLabel.setText(String.valueOf(Main.totalbombnumber1));
	    MarkLabel.setText(String.valueOf( (Main.totalbombnumber1 - Main.marknumber)));
	    ClockLabel.setText(String.valueOf(seconds));
	    timer.schedule(new PrintSeconds(), 0, 1000);  
	}


	class PrintSeconds extends TimerTask {
		public void run() {
			seconds--;
			Platform.runLater(() -> MarkLabel.setText(String.valueOf((Main.totalbombnumber1 - Main.marknumber))));
			Platform.runLater(() -> ClockLabel.setText(String.valueOf(seconds)));
			if(Main.endgame == 1) {
				timer.cancel();
			}
			if(seconds == 0) {
				Main.totalseconds = Main.totalseconds - seconds; 
	            try {
	               PrintWriter printer = new PrintWriter(new FileWriter("medialab\\details.txt", true));
	               printer.println("Total mines : "+ Main.totalbombnumber1 + "| Number of tries : " + Main.tries + "| Game time : "+ Main.totalseconds + "| LOSER ");
	               printer.close();
	            }
	            catch (IOException e) {
					e.printStackTrace();
					
	            }
	            Main.endgame = 1;
	            timer.cancel();
				starter = 0;
				System.out.println("TIME'S UP, YOU LOST, CREATE NEW GAME IF YOU WISH TO CONTINUE PLAYING!");
				
			}
		}
	}
	

	public void ExitGame() {
		System.out.println("Exiting Game");
		System.exit(0);
	}
	

	public void Solve() {
		if(starter == 0) {
			System.out.println("No Active Game to Solve!");
			return;
		}
		System.out.println("Solution Provided,You Lost.");	
		starter = 0;
		Main.solving();
		timer.cancel();
	}
	
	public void Round() {
		System.out.println("Your Last 5 Games : ");
		Main.details();
	}

	
}
