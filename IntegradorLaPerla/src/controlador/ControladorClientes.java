package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import modelo.DAOClientes;
import modelo.DAOReportes;
import modelo.Notification;


@SuppressWarnings("restriction")
public class ControladorClientes implements Initializable {


	@FXML TableView<DAOClientes> tvClientes;
	@FXML Button btnAgregar,btnGuardar, btnEliminar,btnEditar, btnCancelar,btnSalir, btnDireccion;
	@FXML TextField  txtBuscador,txtNombre, txtAPaterno, txtAMaterno, txtRFC, txtRSocial;
	@FXML CheckBox ckbInactivos;

	private DAOReportes reporteador;
	private DAOClientes clientes;
	private ObservableList <DAOClientes> lista;
	private FilteredList<DAOClientes>listaBusqueda;

	public ControladorClientes(){
		clientes =new DAOClientes();
		this.reporteador=new DAOReportes();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		validaciones();
		actualizarTabla();
		bloquear();
	}

	public void validaciones(){
		txtRFC.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[a-zA-Z0-9]{0,13}") || newValue.length() > 12){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtNombre.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[A-Za-z]{0,20}") || newValue.length() > 19){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtAPaterno.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[A-Za-z]{0,20}") || newValue.length() > 19){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtAMaterno.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[A-Za-z]{0,20}") || newValue.length() > 19){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtRSocial.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[A-Za-z]{0,20}") || newValue.length() > 19){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
	}

	public void actualizarTabla(){
		tvClientes.getItems().clear();
		lista = clientes.consultar(/*"select clave_cliente,clave_dir,nombre, apellido_paterno, apellido_materno, RFC, razon_social, "
				+ "calle ||' - '|| avenida ||' - '|| colonia ||' - '||ciudad ||' - '|| numero AS direccion  from cliente "
				+ "natural join direccion where estado=true"*/ "select * from consulta_clientes_activos;");
		tvClientes.setItems(lista);
		tvClientes.refresh();
		listaBusqueda=new FilteredList<DAOClientes>(lista);
	}

	public void bloquear(){
		txtNombre.setDisable(true);
		txtAPaterno.setDisable(true);
		txtAMaterno.setDisable(true);
		txtRFC.setDisable(true);
		txtRSocial.setDisable(true);
		btnDireccion.setDisable(true);

		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnGuardar.setDisable(true);
		btnCancelar.setDisable(true);
		btnAgregar.setDisable(false);
	}

	public void limpiar(){
		txtNombre.clear();
		txtAPaterno.clear();
		txtAMaterno.clear();
		txtRFC.clear();
		txtRSocial.clear();
	}

	@FXML public void clickAgregar(){
		txtNombre.setDisable(false);
		txtAPaterno.setDisable(false);
		txtAMaterno.setDisable(false);
		txtRFC.setDisable(false);
		txtRSocial.setDisable(false);
		btnDireccion.setDisable(false);

		btnGuardar.setDisable(false);
		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnCancelar.setDisable(false);
		btnAgregar.setDisable(true);
	}

	@FXML public void clickGuardar(){
		try{
			if(txtNombre.getText().trim().isEmpty() || txtAPaterno.getText().trim().isEmpty() || txtAMaterno.getText().trim().isEmpty()
					|| txtRFC.getText().trim().isEmpty() || txtRSocial.getText().trim().isEmpty()){
				Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
			}
			else{
				clientes.setNombre(txtNombre.getText());
				clientes.setApellido_paterno(txtAPaterno.getText());
				clientes.setApellido_materno(txtAMaterno.getText());
				clientes.setRFC(txtRFC.getText());
				clientes.setRazon_social(txtRSocial.getText());
				if(clientes.agregar()){
					Notification.Notifier.INSTANCE.notifySuccess("Success", "El cliente se agrego con éxito.");
					actualizarTabla();
					bloquear();
					limpiar();
				}
				else{
					Notification.Notifier.INSTANCE.notifyError("Error", "El cliente no se pudo guardar. Intentalo de nuevo.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@FXML public void clickTabla(){
		if(tvClientes.getSelectionModel().getSelectedItem() != null){
			clientes= tvClientes.getSelectionModel().getSelectedItem();
			txtNombre.setText(clientes.getNombre());
			txtAPaterno.setText(clientes.getApellido_paterno());
			txtAMaterno.setText(clientes.getApellido_materno());
			txtRSocial.setText(clientes.getRazon_social());
			txtRFC.setText(clientes.getRFC());

			btnEditar.setDisable(false);
			btnEliminar.setDisable(false);
			btnCancelar.setDisable(false);
			txtNombre.setDisable(false);
			txtAPaterno.setDisable(false);
			txtAMaterno.setDisable(false);
			txtRSocial.setDisable(false);
			txtRFC.setDisable(false);
			btnDireccion.setDisable(false);

			btnAgregar.setDisable(true);
		}
		else{
			Notification.Notifier.INSTANCE.notifyWarning("Warning", "Selecciona un cliente de la tabla.");
		}
	}

	@FXML public void clickEliminar(){
		if(clientes.getClave_cliente() >0){
			Notification.Notifier.INSTANCE.notifySuccess("Success", "El cliente se eliminó con éxito.");
			clientes.eliminar();
			actualizarTabla();
		}
		else{
			Notification.Notifier.INSTANCE.notifyInfo("Info", "No tienes ningun cliente seleccionado, selecciona algo para eliminar.");
		}
		limpiar();
		bloquear();
	}

	@FXML public void clickEditar(){
		if(txtNombre.getText().trim().isEmpty() || txtAPaterno.getText().trim().isEmpty() || txtAMaterno.getText().trim().isEmpty()
				|| txtRFC.getText().trim().isEmpty() || txtRSocial.getText().trim().isEmpty()){
			Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
		}
		else{
			this.clientes.setNombre(txtNombre.getText());
			this.clientes.setApellido_paterno(txtAPaterno.getText());
			this.clientes.setApellido_materno(txtAMaterno.getText());
			this.clientes.setRazon_social(txtRSocial.getText());
			this.clientes.setRFC(txtRFC.getText());
			if(clientes.editar()){
				Notification.Notifier.INSTANCE.notifySuccess("Success", "Los datos se modificaron correctamente");
				actualizarTabla();
				limpiar();
				bloquear();
			}
			else{
				Notification.Notifier.INSTANCE.notifyError("Error", "El cliente no se pudo actualizar, intenta de nuevo");
			}
		}
	}

	@FXML public void clickCancelar(){
		limpiar();
		bloquear();
	}

	@FXML public void clickDireccion(){
		ControladorVentanas cv= ControladorVentanas.getInstancia();
		cv.asignarModal("../vista/Direccion.fxml", "Direccion");
	}


	@FXML public void clickSalir(){
		ControladorVentanas cv= ControladorVentanas.getInstancia();
		cv.cerrarAcceso();
	}

	@SuppressWarnings({ "unchecked" })
	@FXML public void clickInactivos(){
		limpiar();
		try{
			tvClientes.getItems().clear();
			if(ckbInactivos.isSelected()){
				lista = clientes.consultar(/*"select clave_cliente,clave_dir,nombre, apellido_paterno, apellido_materno, RFC, razon_social, "
						+ "calle ||' - '|| avenida ||' - '|| colonia ||' - '||ciudad ||' - '|| numero AS direccion  from cliente "
						+ "natural join direccion where estado=false"*/ "select * from consulta_clientes_inactivos;");
				listaBusqueda=new FilteredList<DAOClientes>(lista);

				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvClientes.getColumns().add(0, columnaRestaurar);
				columnaRestaurar.setCellValueFactory(
						new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>(){
							@Override
							public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> param){
								return new SimpleBooleanProperty(param.getValue() !=null);
							}
						});
				columnaRestaurar.setCellFactory(
					new Callback<TableColumn<Record, Boolean>,TableCell<Record, Boolean>>(){
						@Override
						public TableCell<Record, Boolean> call(TableColumn <Record, Boolean>param){
					return new BotonActivar();
						}
					});
					}
					else{
						if(tvClientes.getColumns().size()>2){
							tvClientes.getColumns().remove(0);
						}
						lista = clientes.consultar(/*"select clave_cliente,clave_dir,nombre, apellido_paterno, apellido_materno, RFC, razon_social, "
								+ "calle ||' - '|| avenida ||' - '|| colonia ||' - '||ciudad ||' - '|| numero AS direccion  from cliente "
								+ "natural join direccion where estado=true"*/ "select * from consulta_clientes_activos;");
						listaBusqueda=new FilteredList<DAOClientes>(lista);
					}
			tvClientes.setItems(lista);
			}
		catch(Exception e){
			e.printStackTrace();

	}
}

	private class BotonActivar extends TableCell<Record, Boolean>{
		Button cellButton;
		Image imagen;
		ImageView contenedor;

		BotonActivar(){
			imagen=new Image("vista/iconos/restaurar.png");
			contenedor =new ImageView(imagen);
			contenedor.setFitWidth(15);
			contenedor.setFitHeight(15);
			cellButton=new Button ("", contenedor);

			cellButton.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent t){
					clientes=(DAOClientes) BotonActivar.this.getTableView().getItems().get(BotonActivar.this.getIndex());
					if(clientes.reactivar()){
						lista = clientes.consultar(/*"select clave_cliente,clave_dir,nombre, apellido_paterno, apellido_materno, RFC, razon_social, "
								+ "calle ||' - '|| avenida ||' - '|| colonia ||' - '||ciudad ||' - '|| numero AS direccion  from cliente "
								+ "natural join direccion where estado=false"*/ "select * from consulta_clientes_inactivos;");
						tvClientes.setItems(lista);
						tvClientes.refresh();
					}
				}
			});
		}
		@Override
		protected void updateItem(Boolean t, boolean empty){
			super.updateItem(t, empty);
			if(!empty){
				setGraphic(cellButton);
			}
		}
	}

	@FXML public void textChange_busqueda(){
		if(txtBuscador.getText().trim().isEmpty()){
			tvClientes.refresh();
			tvClientes.setItems(lista);
		}
		else{
			try{
				listaBusqueda.setPredicate(DAOClientes->{
					if(DAOClientes.getNombre().toLowerCase().contains(txtBuscador.getText().toLowerCase())){
						return true;
					}
					else{
						return false;
					}
				});
				tvClientes.refresh();
				tvClientes.setItems(listaBusqueda);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@FXML public void clickReporte() throws IOException{
		try{
			reporteador.cargarReportedeClientes();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
