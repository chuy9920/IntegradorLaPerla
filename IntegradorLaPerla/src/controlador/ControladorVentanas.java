package controlador;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.DAOUsuarios;

public class ControladorVentanas {
	private static ControladorVentanas instancia;
	private Stage primaryStage, secondStage, escenarioProgreso;
	private Scene escena;
	private AnchorPane contenedorMenu, subcontenedor;
	private ProgressIndicator progress;

	private ControladorVentanas(){
		progress=new ProgressIndicator();
		progress.setPrefWidth(100);
		progress.setPrefHeight(100);
	}

	//Patron Singleton: permite asegurar que solo exista un unico objeto de una clase
	public static ControladorVentanas getInstancia(){
		if(instancia==null)
			instancia=new ControladorVentanas();
		return instancia;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	//Asignar Menu Principal
	public void asignarMenu(String ruta, String titulo, DAOUsuarios usuario){
		try{
			primaryStage.setUserData(usuario);
			FXMLLoader parent=new FXMLLoader(getClass().getResource(ruta));
			contenedorMenu=(AnchorPane)parent.load();
			escena=new Scene(contenedorMenu);
			primaryStage.setScene(escena);
			primaryStage.setTitle(titulo);
			primaryStage.setResizable(false);
			primaryStage.getIcons().add(new Image("vista/img/logo.png"));
			primaryStage.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void asignarModal(String ruta, String titulo){
		try{

			FXMLLoader vista=new FXMLLoader(getClass().getResource(ruta));
			subcontenedor=(AnchorPane) vista.load();
			secondStage=new Stage();
			escena=new Scene(subcontenedor);
			secondStage.setScene(escena);
			secondStage.setTitle(titulo);
			secondStage.centerOnScreen();
			secondStage.initModality(Modality.WINDOW_MODAL);
			secondStage.initOwner(primaryStage);
			secondStage.setResizable(false);
			secondStage.getIcons().add(new Image("vista/img/logo.png"));
			//secondStage.show();
			cargando();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void cerrar(){
		primaryStage.close();
	}

	public void cerrarAcceso(){
		secondStage.close();
	}

	public void cargando(){
		VBox updatePane =new VBox();
		ColorAdjust ajuste=new ColorAdjust();
		ajuste.setHue(-.6);
		progress.setEffect(ajuste);
		updatePane.getChildren().add(progress);
		updatePane.setStyle("-fx-background-insets: 50");
		escenarioProgreso=new Stage(StageStyle.TRANSPARENT);
		escena=new Scene(updatePane);
		escena.setFill(Color.TRANSPARENT);
		escenarioProgreso.setScene(escena);
		escenarioProgreso.show();
		Task<Void> tarea=new Task<Void>() {
			@Override
			protected Void call() throws Exception{
				int max=10;
				for(int i=1; i<=max; i++){
					if(isCancelled()){
						break;
					}
					updateProgress(i, max);
					updateMessage(String.valueOf(i));
					Thread.sleep(100);
				}
				return null;
			}
		};
		tarea.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event){
				escenarioProgreso.hide();
				secondStage.show();
			}
		});

		progress.progressProperty().bind(tarea.progressProperty());
		new Thread(tarea).start();
	}
	public void cargando2(){
		VBox updatePane =new VBox();
		ColorAdjust ajuste=new ColorAdjust();
		ajuste.setHue(-.6);
		progress.setEffect(ajuste);
		updatePane.getChildren().add(progress);
		updatePane.setStyle("-fx-background-insets: 50");
		escenarioProgreso=new Stage(StageStyle.TRANSPARENT);
		escena=new Scene(updatePane);
		escena.setFill(Color.TRANSPARENT);
		escenarioProgreso.setScene(escena);
		escenarioProgreso.show();
		Task<Void> tarea=new Task<Void>() {
			@Override
			protected Void call() throws Exception{
				int max=10;
				for(int i=1; i<=max; i++){
					if(isCancelled()){
						break;
					}
					updateProgress(i, max);
					updateMessage(String.valueOf(i));
					Thread.sleep(100);
				}
				return null;
			}
		};
		tarea.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event){
				escenarioProgreso.hide();
				primaryStage.show();
			}
		});

		progress.progressProperty().bind(tarea.progressProperty());
		new Thread(tarea).start();
	}
}
