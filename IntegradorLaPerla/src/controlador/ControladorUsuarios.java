package controlador;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.prism.impl.Disposer.Record;

import modelo.Notification;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import modelo.DAOReportes;
import modelo.DAOUsuarios;

@SuppressWarnings("restriction")
public class ControladorUsuarios implements Initializable {

	@FXML TableView<DAOUsuarios> tvUsuarios;
	@FXML Button btnAgregar,btnGuardar, btnEliminar,btnEditar, btnCancelar, btnNotas,btnSalir;
	@FXML TextField txtUsuario, txtBuscador;
	@FXML PasswordField pfContrasena;
	@FXML CheckBox ckbVer, ckbInactivos;
	@FXML ComboBox<String> cbTipo;

	private ObservableList <DAOUsuarios> lista;
	private ObservableList<String> tipos;
	private DAOUsuarios usuario;
	private FilteredList<DAOUsuarios>listaBusqueda;
	private ControladorVentanas instancia;
	private DAOReportes reporteador;

	public ControladorUsuarios() {
		usuario =new DAOUsuarios();
		this.reporteador=new DAOReportes();
		instancia = ControladorVentanas.getInstancia();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tipos=FXCollections.observableArrayList();
		tipos.add("administrador");
		tipos.add("empleado");
		cbTipo.setItems(tipos);
		cbTipo.getSelectionModel().select(1);
		actualizarTabla();
		bloquear();
		validaciones();
	}

	public void validaciones(){
		txtUsuario.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[a-zA-Z' ']{0,20}") || newValue.length() > 20){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		pfContrasena.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[a-zA-Z0-9' ']{0,20}") || newValue.length() > 20){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
	}

	public void actualizarTabla(){
		DAOUsuarios usuario=(DAOUsuarios) instancia.getPrimaryStage().getUserData();
		tvUsuarios.getItems().clear();
		lista = usuario.consultar(/*"select * from usuarios where estado = TRUE and id_usuario!="+usuario.getId_usuario()*/ "select * from consulta_usuarios_activos;");
		tvUsuarios.setItems(lista);
		tvUsuarios.refresh();
		listaBusqueda=new FilteredList<DAOUsuarios>(lista);
	}

	public void bloquear(){
		txtUsuario.setDisable(true);
		pfContrasena.setDisable(true);
		ckbVer.setDisable(true);
		cbTipo.setDisable(true);

		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnGuardar.setDisable(true);
		btnCancelar.setDisable(true);
		btnAgregar.setDisable(false);
	}

	public void limpiar(){
		txtUsuario.clear();
		pfContrasena.clear();
		cbTipo.getSelectionModel().select(1);
	}

	@FXML public void clickAgregar(){
		txtUsuario.setDisable(false);
		pfContrasena.setDisable(false);
		ckbVer.setDisable(false);
		cbTipo.setDisable(false);

		btnGuardar.setDisable(false);
		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnCancelar.setDisable(false);
		btnAgregar.setDisable(true);
	}

	@FXML public void clickGuardar(){
		try{
			if(txtUsuario.getText().trim().isEmpty() || pfContrasena.getText().trim().isEmpty()){
				Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
			}
			else{
				usuario.setUsuario(txtUsuario.getText());
				usuario.setContrasena(pfContrasena.getText());
				usuario.setTipo(cbTipo.getSelectionModel().getSelectedItem());
				if(usuario.agregar()){
					Notification.Notifier.INSTANCE.notifySuccess("Success", "El usuario se agrego con éxito.");
					actualizarTabla();
					bloquear();
					limpiar();
				}
				else{
					Notification.Notifier.INSTANCE.notifyError("Error", "El usuario no se pudo guardar. Intentalo de nuevo.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@FXML public void clickTabla(){
		if(tvUsuarios.getSelectionModel().getSelectedItem() != null){
			usuario= tvUsuarios.getSelectionModel().getSelectedItem();
			txtUsuario.setText(usuario.getUsuario());
			pfContrasena.setText(usuario.getContrasena());
			cbTipo.getSelectionModel().select(usuario.getTipo());

			btnEditar.setDisable(false);
			btnEliminar.setDisable(false);
			btnCancelar.setDisable(false);
			txtUsuario.setDisable(false);
			pfContrasena.setDisable(false);
			ckbVer.setDisable(false);
			cbTipo.setDisable(false);
			btnAgregar.setDisable(true);
		}
		else{
			Notification.Notifier.INSTANCE.notifyWarning("Warning", "Selecciona un usuario de la tabla.");
		}
	}

	@FXML public void clickEliminar(){
		if(usuario.getId_usuario() >0){
			Notification.Notifier.INSTANCE.notifySuccess("Success", "El usuario se eliminó con éxito.");
			usuario.eliminar();
			actualizarTabla();
		}
		else{
			Notification.Notifier.INSTANCE.notifyInfo("Info", "No tienes ningun usuario seleccionado, selecciona algo para eliminar");
		}
		limpiar();
		bloquear();
	}

	@FXML public void clickEditar(){
		if(txtUsuario.getText().trim().isEmpty() || pfContrasena.getText().trim().isEmpty()){
			Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
		}
		else{
			this.usuario.setUsuario(txtUsuario.getText());
			this.usuario.setContrasena(pfContrasena.getText());
			this.usuario.setTipo(cbTipo.getSelectionModel().getSelectedItem());
			if(usuario.editar()){
				Notification.Notifier.INSTANCE.notifySuccess("Success", "Los datos se modificaron correctamente");
				actualizarTabla();
				limpiar();
				bloquear();
			}
			else{
				Notification.Notifier.INSTANCE.notifyError("Error", "El usuario no se pudo actualizar, intenta de nuevo");
			}
		}
	}

	@FXML public void clickCancelar(){
		bloquear();
		limpiar();
	}

	@FXML public void clickReporte() throws IOException{
		try{
			reporteador.cargarReportedeUsuarios();
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
			tvUsuarios.getItems().clear();
			if(ckbInactivos.isSelected()){
				lista=usuario.consultar(/*"select * from usuarios where estado=false"*/ "select * from consulta_usuarios_inactivos;");
				listaBusqueda=new FilteredList<DAOUsuarios>(lista);

				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvUsuarios.getColumns().add(0, columnaRestaurar);
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
						if(tvUsuarios.getColumns().size()>2){
							tvUsuarios.getColumns().remove(0);
						}
						lista=usuario.consultar(/*"select * from usuarios where estado= true"*/ "select * from consulta_usuarios_activos;");
						listaBusqueda=new FilteredList<DAOUsuarios>(lista);
					}
			tvUsuarios.setItems(lista);
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
					usuario=(DAOUsuarios) BotonActivar.this.getTableView().getItems().get(BotonActivar.this.getIndex());
					if(usuario.reactivar()){
						lista=usuario.consultar(/*"select * from usuarios where estado=false"*/ "select * from consulta_usuarios_inactivos;");
						tvUsuarios.setItems(lista);
						tvUsuarios.refresh();
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
			tvUsuarios.refresh();
			tvUsuarios.setItems(lista);
		}
		else{
			try{
				listaBusqueda.setPredicate(DAOUsuarios->{
					if(DAOUsuarios.getUsuario().toLowerCase().contains(txtBuscador.getText().toLowerCase())){
						return true;
					}
					else{
						return false;
					}
				});
				tvUsuarios.refresh();
				tvUsuarios.setItems(listaBusqueda);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
