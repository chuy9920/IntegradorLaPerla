package controlador;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.DAOUsuarios;
import modelo.Notification;

public class ControladorAcceso implements Initializable{

	@FXML TextField txtUsuario;
	@FXML PasswordField pfContrasena;

	private DAOUsuarios usuario;
	private ControladorVentanas cv;

	public ControladorAcceso(){
		usuario =new DAOUsuarios();
		cv= ControladorVentanas.getInstancia();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtUsuario.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[a-zA-Z]{0,15}") || newValue.length() > 20){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
	}

	@FXML public void clickIniciarSesion(){
		try{
			if(txtUsuario.getText().trim().isEmpty() || pfContrasena.getText().trim().isEmpty())
				Notification.Notifier.INSTANCE.notifyWarning("Warning", "Existen campos vacíos");
			else{
				usuario.setUsuario(txtUsuario.getText());
				usuario.setContrasena(pfContrasena.getText());
				DAOUsuarios temp = usuario.validarDatos();
				if(temp!=null){
					cv.cerrarAcceso();
					cv.asignarMenu("../vista/Menu.fxml", "Bienvenido "+temp.getUsuario(), temp);
					Notification.Notifier.INSTANCE.notifySuccess("Success", "Entro al sistema con éxito");
				}
				else{
					Notification.Notifier.INSTANCE.notifyError("Error", "Datos de usuario no registrados");
					txtUsuario.clear();
					pfContrasena.clear();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
