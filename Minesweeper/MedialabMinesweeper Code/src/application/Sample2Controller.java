package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;

public class Sample2Controller   {

	@FXML
	private TextField DifficultyTextField;
	@FXML
	private TextField MinesTextField;
	@FXML
	private TextField TimeTextField;
	@FXML
	private TextField SuperMineTextField;
	@FXML
	private Button mineButton;
	
	public static int m,d,t,hm;
	
	public void SetMines(ActionEvent event) {
		
			m = Integer.parseInt(MinesTextField.getText());
			System.out.print("You Set The Number Of Mines to : ");
			System.out.println(m);
			SampleController.mines = m;
	
	}
	
	
	public void SetDifficulty(ActionEvent event) {
		
		d = Integer.parseInt(DifficultyTextField.getText());
		System.out.print("You Set The Difficulty to : ");
		System.out.println(d);
		SampleController.difficulty = d;
	}
	
	
	public void SetTime(ActionEvent event) {
		
		t = Integer.parseInt(TimeTextField.getText());
		System.out.print("You Set The Number Of Seconds to : ");
		System.out.println(t);
		SampleController.seconds1 = t;
	}
	
	
	public void SetSuperMine(ActionEvent event) {
		
		hm = Integer.parseInt(SuperMineTextField.getText());
		System.out.print("You Set The Number Of SuperMine to : ");
		System.out.println(hm);
		SampleController.supermine = hm;
	}	
	
	static void checkValues() throws InvalidValueException{
		if(d != 1 && d!=2 ||
				(d == 1 && ((m > 11) || (m < 9))) || (d == 2 && ((m > 45) || (m < 35))) ||
					(hm != 0 && hm!= 1) || ( hm ==1 && (d != 2)) || ( hm != 1 && (d == 2)) ||
					(d == 1 && ((t > 180) || (t < 120))) || (d == 2 && ((t > 360) || (t < 240)))) {
			throw new InvalidValueException("No such Values Permitted");
		}
					
				
	}
	
	public void SubmitSettings(ActionEvent event) {
		try {
			checkValues();
			SampleController.CreateFile();
			
		}
		catch(Exception e){
			System.out.println("A Problem Occured :"+e);
		}
		
	}
	
}