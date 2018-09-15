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

import modelo.DAOProveedores;
import modelo.DAOReportes;
import modelo.Notification;

@SuppressWarnings("restriction")
public class ControladorProveedores implements Initializable {

	@FXML TableView<DAOProveedores> tvProveedor;
	@FXML TextField txtNombre_empresa, txtTelefono, txtBuscador;
	@FXML Button btnAgregar, btnGuardar, btnEditar, btnEliminar,btnCancelar,btnSalir, btnDireccion;
	@FXML CheckBox ckbInactivos;

	private DAOProveedores proveedor;
	private ObservableList <DAOProveedores> lista;
	private FilteredList<DAOProveedores>listaBusqueda;
	private DAOReportes reporteador;

	public ControladorProveedores(){
		proveedor=new DAOProveedores();
		this.reporteador=new DAOReportes();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		validaciones();
		actualizarTabla();
		bloquear();
	}
	public void actualizarTabla(){
		tvProveedor.getItems().clear();
		lista = proveedor.consultar(/*"select clave_p, clave_dir, nombre_empresa, telefono,"
				+ " calle ||' - '|| avenida ||' - '|| colonia ||' - '||ciudad ||' - '|| numero AS direccion "
				+ "from proveedor natural join direccion where estado=true"*/ "select * from consulta_proveedores_activos;");
		tvProveedor.setItems(lista);
		tvProveedor.refresh();
		listaBusqueda=new FilteredList<DAOProveedores>(lista);
	}
	public void validaciones(){
		txtTelefono.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[0-9]{0,10}") || newValue.length() > 10){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtNombre_empresa.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[a-zA-z' ']{0,10}") || newValue.length() > 10){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
	}

	public void bloquear(){
		txtNombre_empresa.setDisable(true);
		txtTelefono.setDisable(true);
		btnDireccion.setDisable(true);

		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnGuardar.setDisable(true);
		btnCancelar.setDisable(true);
		btnAgregar.setDisable(false);
	}

	public void limpiar(){
		txtNombre_empresa.clear();
		txtTelefono.clear();
	}

	@FXML public void clickDireccion(){
		ControladorVentanas cv= ControladorVentanas.getInstancia();
		cv.asignarModal("../vista/Direccion.fxml", "Direccion");
	}

	@FXML public void clickAgregar(){
		txtNombre_empresa.setDisable(false);
		txtTelefono.setDisable(false);
		btnDireccion.setDisable(false);

		btnGuardar.setDisable(false);
		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnCancelar.setDisable(false);
		btnAgregar.setDisable(true);
	}

	@FXML public void clickGuardar(){
		try{
			if(txtNombre_empresa.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()){
				Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
			}
			else{
				proveedor.setNombre_empresa(txtNombre_empresa.getText());
				proveedor.setTelefono(txtTelefono.getText());
				if(proveedor.agregar()){
					Notification.Notifier.INSTANCE.notifySuccess("Success", "El proveedor se agrego con éxito.");
					actualizarTabla();
					bloquear();
					limpiar();
				}
				else{
					Notification.Notifier.INSTANCE.notifyError("Error", "El proveedor no se pudo guardar. Intentalo de nuevo.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@FXML public void clickTabla(){
		if(tvProveedor.getSelectionModel().getSelectedItem() != null){
			proveedor= tvProveedor.getSelectionModel().getSelectedItem();
			txtNombre_empresa.setText(proveedor.getNombre_empresa());
			txtTelefono.setText(proveedor.getTelefono());

			btnEditar.setDisable(false);
			btnEliminar.setDisable(false);
			btnCancelar.setDisable(false);
			txtNombre_empresa.setDisable(false);
			txtTelefono.setDisable(false);
			btnDireccion.setDisable(false);

			btnAgregar.setDisable(true);
		}
		else{
			Notification.Notifier.INSTANCE.notifyWarning("Warning", "Selecciona un proveedor de la tabla.");
		}
	}

	@FXML public void clickEliminar(){
		if(proveedor.getClave_p() >0){
			Notification.Notifier.INSTANCE.notifySuccess("Success", "El proveedor se eliminó con éxito.");
			proveedor.eliminar();
			actualizarTabla();
		}
		else{
			Notification.Notifier.INSTANCE.notifyInfo("Info", "No tienes ningun proveedor seleccionado, selecciona algo para eliminar");
		}
		limpiar();
		bloquear();
	}

	@FXML public void clickEditar(){
		if(txtNombre_empresa.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()){
			Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
		}
		else{
			this.proveedor.setNombre_empresa(txtNombre_empresa.getText());
			this.proveedor.setTelefono(txtTelefono.getText());
			if(proveedor.editar()){
				Notification.Notifier.INSTANCE.notifySuccess("Success", "Los datos se modificaron correctamente");
				actualizarTabla();
				limpiar();
				bloquear();
			}
			else{
				Notification.Notifier.INSTANCE.notifyError("Error", "El proveedor no se pudo actualizar, intenta de nuevo");
			}
		}
	}

	@FXML public void clickCancelar(){
		limpiar();
		bloquear();
	}

	@FXML public void clickReporte() throws IOException{
		try{
			reporteador.cargarReportedeProveedores();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@FXML public void clickSalir(){
		ControladorVentanas cv= ControladorVentanas.getInstancia();
		cv.cerrarAcceso();
	}

	@SuppressWarnings({ "unchecked" })
	@FXML public void clickInactivos(){
		limpiar();
		try{
			tvProveedor.getItems().clear();
			if(ckbInactivos.isSelected()){
				lista = proveedor.consultar(/*"select clave_p, clave_dir, nombre_empresa, telefono,"
						+ " calle ||' - '|| avenida ||' - '|| colonia ||' - '||ciudad ||' - '|| numero AS direccion "
						+ "from proveedor natural join direccion where estado=false"*/ "select * from consulta_proveedores_inactivos;");
				listaBusqueda=new FilteredList<DAOProveedores>(lista);

				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvProveedor.getColumns().add(0, columnaRestaurar);
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
						if(tvProveedor.getColumns().size()>2){
							tvProveedor.getColumns().remove(0);
						}
						lista = proveedor.consultar(/*"select clave_p, clave_dir, nombre_empresa, telefono,"
								+ " calle ||' - '|| avenida ||' - '|| colonia ||' - '||ciudad ||' - '|| numero AS direccion "
								+ "from proveedor natural join direccion where estado=true"*/ "select * from consulta_proveedores_activos; ");
						listaBusqueda=new FilteredList<DAOProveedores>(lista);
					}
			tvProveedor.setItems(lista);
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
					proveedor=(DAOProveedores) BotonActivar.this.getTableView().getItems().get(BotonActivar.this.getIndex());
					if(proveedor.reactivar()){
						lista = proveedor.consultar(/*"select clave_p, clave_dir, nombre_empresa, telefono,"
								+ " calle ||' - '|| avenida ||' - '|| colonia ||' - '||ciudad ||' - '|| numero AS direccion "
								+ "from proveedor natural join direccion where estado=false"*/ "select * from consulta_proveedores_inactivos; ");
						tvProveedor.setItems(lista);
						tvProveedor.refresh();
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
			tvProveedor.refresh();
			tvProveedor.setItems(lista);
		}
		else{
			try{
				listaBusqueda.setPredicate(DAOClientes->{
					if(DAOClientes.getNombre_empresa().toLowerCase().contains(txtBuscador.getText().toLowerCase())){
						return true;
					}
					else{
						return false;
					}
				});
				tvProveedor.refresh();
				tvProveedor.setItems(listaBusqueda);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
