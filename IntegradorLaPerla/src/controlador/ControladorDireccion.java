package controlador;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import modelo.DAODireccion;
import modelo.DAOSepomex;
import modelo.Notification;

public class ControladorDireccion implements Initializable {

	@FXML TextField txtCalle, txtAvenida, txtNumero;
	@FXML TableView<DAODireccion> tvDirecciones;
	@FXML ComboBox<String> cbCiudad, cbColonia;

	private DAODireccion direccion;
	private DAOSepomex sepomex;

	public ControladorDireccion(){
		direccion=new DAODireccion();
		this.sepomex=new DAOSepomex();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbCiudad.setItems(sepomex.consultarCiudad());
		cbCiudad.getSelectionModel().select("Selecciona una ciudad");
		cbColonia.setDisable(true);
		validaciones();
	}

	@FXML public void clickCiudades(){
		cbColonia.setDisable(false);
		cbColonia.getItems().clear();
		sepomex.setMunicipio(cbCiudad.getSelectionModel().getSelectedItem());
		cbColonia.setItems(sepomex.consultarColonia());
		cbColonia.getSelectionModel().select(0);
	}


	public void validaciones(){
		txtNumero.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[A-Z0-9]{0,5}") || newValue.length() > 5){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtCalle.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[A-Za-z0-9]{0,20}[A-Za-z0-9' ']{0,4}") || newValue.length() > 20){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtAvenida.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[A-Za-z0-9]{0,20}[A-Za-z0-9' ']{0,4}") || newValue.length() > 20){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
	}

	@FXML public void clickGuardar(){
		try{
			if(txtCalle.getText().trim().isEmpty() || txtAvenida.getText().trim().isEmpty() || cbCiudad.getSelectionModel().isEmpty()
					|| cbColonia.getSelectionModel().isEmpty() || txtNumero.getText().trim().isEmpty()){
				Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
			}
			else{
				direccion.setCalle(txtCalle.getText());
				direccion.setAvenida(txtAvenida.getText());
				direccion.setColonia(cbColonia.getSelectionModel().getSelectedItem());
				direccion.setCiudad(cbCiudad.getSelectionModel().getSelectedItem());
				direccion.setNumero(txtNumero.getText());
				if(direccion.agregar()){
					Notification.Notifier.INSTANCE.notifySuccess("Success", "La dirección se agrego con éxito.");
					ControladorVentanas cv= ControladorVentanas.getInstancia();
					cv.cerrarAcceso();
				}
				else{
					Notification.Notifier.INSTANCE.notifyError("Error", "La dirección no se pudo guardar. Intentalo de nuevo.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}



}
