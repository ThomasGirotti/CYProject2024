import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;

public class AppMorph extends Application {
	 public void start(Stage primaryStage) throws Exception {

	        /* donner un nom à la fenêtre */
		 	primaryStage.setTitle("Morphing");
	        primaryStage.setFullScreen(true);;
	        /* Création d'un bouton */
	        Button m1 = new Button("Morphing de formes unies simples");
	        Button m2 = new Button ("Morphing de formes unies arrondies");
	        Button m3 = new Button ("Morphing d'images");


	        /* création d'une fenêtre */
	       VBox morphMode = new VBox();
	       
	        m1.setPrefWidth(300);
	        m2.setPrefWidth(300);
	        m3.setPrefWidth(300);
	        /* ajouter le bouton à la scene */
	        morphMode.getChildren().add(m1);
	        morphMode.getChildren().add(m2);
	        morphMode.getChildren().add(m3);
	        
	        
	        m1.setOnAction(new BHandler());
	        
	        m2.setOnAction(new BHandler());
	        
	        m3.setOnAction(new BHandler());
	        

	        /* création d'une scene et de son association avec */
	        /* la fenêtre + taille */
	        Scene scene = new Scene(morphMode, 500, 500);
	        morphMode.setStyle("-fx-padding: 5;"+
	        		"-fx-border-color: black;" + 
	        		"-fx-padding-color: black");

	       /* Ajouter la scene */
	       primaryStage.setScene(scene);
	        
	        primaryStage.show();
	    }
	 		
	class BHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			
			//gestionnaire de  mode de morphisme
			if(e.getSource() instanceof Button) {
				Button clickedB = (Button) e.getSource();
				
				// passage au Morphing de formes Unies Simples
				if(clickedB.getText().equals("Morphing de formes unies simples")) {
					System.out.println("Morphing de formes unies simples");
				}
				
				//passage au morphing de formes unies arrondies
				if(clickedB.getText().equals("Morphing de formes unies arrondies")) {
					System.out.println("Morphing de formes unies arrondies");
				}
				
				// passage au morphing d'images
				if(clickedB.getText().equals("Morphing d'images")) {
					System.out.println("Morphing d'images");
				}
			}
		}
	}

    public static void main (String[] args) {
        launch(args);
    }

}
