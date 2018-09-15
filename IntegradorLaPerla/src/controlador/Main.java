package controlador;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

	private ControladorVentanas ventanas;

	@Override
	public void start(Stage primaryStage) throws Exception {
		ventanas=ControladorVentanas.getInstancia();
		ventanas.setPrimaryStage(primaryStage);
		ventanas.asignarModal("../vista/Inicio.fxml", "Micelanea La perla");
	}

	public static void main(String[] args) {
		launch(args);
	}
}