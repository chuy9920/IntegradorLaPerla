package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import modelo.DAOProductos;
import modelo.DAOReportes;
import modelo.DAOTipoProducto;
import modelo.Notification;

@SuppressWarnings("restriction")
public class ControladorProductos implements Initializable {

	@FXML TableView<DAOProductos> tvProductos;
	@FXML DatePicker dpCaducidad;
	@FXML TextField txtNombre, txtPrecioMa,txtPrecioMe,txtPrecioPa, txtBuscador;
	@FXML Button btnAgregar, btnGuardar, btnEditar, btnEliminar,btnCancelar,btnSalir;
	@FXML ComboBox<String> cbTipoPo;
	@FXML CheckBox ckbInactivos;

	private DAOTipoProducto tipoP;
	private DAOProductos producto;
	private ObservableList <DAOProductos> lista;
	private FilteredList<DAOProductos>listaBusqueda;
	private DAOReportes reporteador;

	public ControladorProductos(){
		producto =new DAOProductos();
		tipoP=new DAOTipoProducto();
		this.reporteador=new DAOReportes();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cargarLista();
		actualizarTabla();
		bloquear();
		validaciones();
	}

	public void validaciones(){
		txtNombre.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[A-Za-z' ']{0,20}") || newValue.length() > 19){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtPrecioMa.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[0-9'.']{0,20}") || newValue.length() > 19){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtPrecioMe.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[0-9'.']{0,20}") || newValue.length() > 19){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
		txtPrecioPa.textProperty().addListener( (observable, oldValue, newValue) -> {
			if( !newValue.matches("[0-9'.']{0,20}") || newValue.length() > 19){
				((StringProperty)observable).setValue(oldValue);
			}
			else{
				((StringProperty)observable).setValue(newValue);
			}
		});
	}

	public void cargarLista(){
		try{
			cbTipoPo.getItems().clear();
			cbTipoPo.setItems(tipoP.consultar());
			cbTipoPo.getSelectionModel().select("Selecciona tipo");
		}
		catch(Exception ex){

		}
	}

	public void actualizarTabla(){
		tvProductos.getItems().clear();
		lista = producto.consultar(/*"select clave_producto, clave_td,nombre, precio_mayoreo, precio_menudeo,precio_paquete, fecha_caducidad, clasificacion "
				+ "from producto natural join tipo_producto "
				+ "where estado = TRUE"*/ "select * from consulta_productos_activos;");
		tvProductos.setItems(lista);
		tvProductos.refresh();
		listaBusqueda=new FilteredList<DAOProductos>(lista);
	}

	public void bloquear(){
		txtNombre.setDisable(true);
		txtPrecioMa.setDisable(true);
		txtPrecioMe.setDisable(true);
		txtPrecioPa.setDisable(true);
		dpCaducidad.setDisable(true);
		cbTipoPo.setDisable(true);

		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnGuardar.setDisable(true);
		btnCancelar.setDisable(true);
		btnAgregar.setDisable(false);
	}

	public void limpiar(){
		txtNombre.clear();
		txtPrecioMa.clear();
		txtPrecioMe.clear();
		txtPrecioPa.clear();
		cbTipoPo.getItems().clear();
		dpCaducidad.getEditor().clear();
	}

	@FXML public void clickAgregar(){
		cargarLista();
		txtNombre.setDisable(false);
		txtPrecioMa.setDisable(false);
		txtPrecioMe.setDisable(false);
		txtPrecioPa.setDisable(false);
		dpCaducidad.setDisable(false);
		cbTipoPo.setDisable(false);

		btnGuardar.setDisable(false);
		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnCancelar.setDisable(false);
		btnAgregar.setDisable(true);
	}

	@FXML public void clickGuardar(){
		try{
			LocalDate actual=LocalDate.now();
			LocalDate faux=dpCaducidad.getValue();
			if(txtNombre.getText().trim().isEmpty() || txtPrecioMa.getText().trim().isEmpty() || txtPrecioMe.getText().trim().isEmpty()
					|| txtPrecioPa.getText().trim().isEmpty() || cbTipoPo.getSelectionModel().isEmpty()
					|| dpCaducidad.getValue().toString().isEmpty() || faux.isBefore(actual)==true){
					if(faux.isBefore(actual)==true) Notification.Notifier.INSTANCE.notifyInfo("Info", "La fecha de caducidad no puede ser antigua.");
					else Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
			}
			else{
				producto.setNombre(txtNombre.getText());

				String ma1=txtPrecioMa.getText();
				float ma = Float.parseFloat(ma1);

				String me1=txtPrecioMe.getText();
				float me = Float.parseFloat(me1);

				String pa1=txtPrecioPa.getText();
				float pa = Float.parseFloat(pa1);

				producto.setPrecio_mayoreo(ma);
				producto.setPrecio_menudeo(me);
				producto.setPrecio_paquete(pa);

				//producto.setFecha_caducidad(String.valueOf(dpCaducidad.getValue()));
				LocalDate dpfecha=dpCaducidad.getValue();
				String fecha = dpfecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				String [] fecha1=fecha.split("/");
				String dia =fecha1[0];
				String mes =fecha1[1];
				String año =fecha1[2];


				producto.setFecha_caducidad(año+"-"+mes+"-"+dia);

				if(producto.agregar(cbTipoPo.getSelectionModel().getSelectedItem())){
					Notification.Notifier.INSTANCE.notifySuccess("Success", "El producto se agrego con éxito.");
					actualizarTabla();
					bloquear();
					limpiar();
				}
				else{
					Notification.Notifier.INSTANCE.notifyError("Error", "El producto no se pudo guardar. Intentalo de nuevo.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@FXML public void clickTabla(){
		cargarLista();
		if(tvProductos.getSelectionModel().getSelectedItem() != null){
			producto= tvProductos.getSelectionModel().getSelectedItem();
			txtNombre.setText(producto.getNombre());
			float ma1=producto.getPrecio_mayoreo();
			String ma = Float.toString(ma1);
			float me1=producto.getPrecio_menudeo();
			String me = Float.toString(me1);

			float pa1=producto.getPrecio_paquete();
			String pa = Float.toString(pa1);

			txtPrecioMa.setText(ma);
			txtPrecioMe.setText(me);
			txtPrecioPa.setText(pa);

			cbTipoPo.getSelectionModel().select(producto.getClasificacion());

			String fecha=producto.getFecha_caducidad();
			/*String [] fecha1=fecha.split("-");
			String dia =fecha1[0];
			String mes =fecha1[1];
			String año =fecha1[2];*/

			LocalDate data=LocalDate.parse(fecha);

			dpCaducidad.setValue(data);

			btnEditar.setDisable(false);
			btnEliminar.setDisable(false);
			btnCancelar.setDisable(false);
			txtNombre.setDisable(false);
			txtPrecioMa.setDisable(false);
			txtPrecioMe.setDisable(false);
			txtPrecioPa.setDisable(false);
			dpCaducidad.setDisable(false);
			cbTipoPo.setDisable(false);

			btnAgregar.setDisable(true);
		}
		else{
			Notification.Notifier.INSTANCE.notifyWarning("Warning", "Selecciona un producto de la tabla.");
		}
	}


	@FXML public void clickEliminar(){
		if(producto.getClave_producto() >0){
			Notification.Notifier.INSTANCE.notifySuccess("Success", "El producto se eliminó con éxito.");
			producto.eliminar();
			actualizarTabla();
		}
		else{
			Notification.Notifier.INSTANCE.notifyInfo("Info", "No tienes ningun producto seleccionado, selecciona algo para eliminar");
		}
		limpiar();
		bloquear();
	}

	@FXML public void clickEditar(){
		LocalDate actual=LocalDate.now();
		LocalDate faux=dpCaducidad.getValue();
		if(txtNombre.getText().trim().isEmpty() || txtPrecioMa.getText().trim().isEmpty() || txtPrecioMe.getText().trim().isEmpty()
				|| txtPrecioPa.getText().trim().isEmpty() || cbTipoPo.getSelectionModel().isEmpty()
				|| dpCaducidad.getValue().toString().isEmpty() || faux.isBefore(actual)==true){
			if(faux.isBefore(actual)==true) Notification.Notifier.INSTANCE.notifyInfo("Info", "La fecha de caducidad no puede ser antigua.");
			else Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
		}
		else{
			//cargarLista();
			this.producto.setNombre(txtNombre.getText());

			String ma1=txtPrecioMa.getText();
			float ma = Float.parseFloat(ma1);

			String me1=txtPrecioMe.getText();
			float me = Float.parseFloat(me1);

			String pa1=txtPrecioPa.getText();
			float pa = Float.parseFloat(pa1);

			this.producto.setPrecio_mayoreo(ma);
			this.producto.setPrecio_menudeo(me);
			this.producto.setPrecio_paquete(pa);


			LocalDate dpfecha=dpCaducidad.getValue();
			String fecha = dpfecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String [] fecha1=fecha.split("/");
			String dia =fecha1[0];
			String mes =fecha1[1];
			String año =fecha1[2];

			this.producto.setFecha_caducidad( dia+"-"+mes+"-"+año);

			String clasificacion="";

			clasificacion=cbTipoPo.getSelectionModel().getSelectedItem();


			if(producto.editar(clasificacion)){
				Notification.Notifier.INSTANCE.notifySuccess("Success", "Los datos se modificaron correctamente");
				actualizarTabla();
				limpiar();
				bloquear();
				cargarLista();
			}
			else{
				Notification.Notifier.INSTANCE.notifyError("Error", "El producto no se pudo actualizar, intenta de nuevo");
				System.out.print("El tipo recuperado: "+clasificacion);
			}
		}
	}

	@FXML public void clickCancelar(){
		limpiar();
		bloquear();
		cargarLista();
	}

	@FXML public void clickReporte() throws IOException{
		try{
			reporteador.cargarReportedeProductos();
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
			tvProductos.getItems().clear();
			if(ckbInactivos.isSelected()){
				lista = producto.consultar(/*"select clave_producto, clave_td,nombre, precio_mayoreo, precio_menudeo,precio_paquete, fecha_caducidad, clasificacion "
						+ "from producto natural join tipo_producto "
						+ "where estado = false"*/ "select * from consulta_productos_inactivos;");
				listaBusqueda=new FilteredList<DAOProductos>(lista);

				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvProductos.getColumns().add(0, columnaRestaurar);
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
						if(tvProductos.getColumns().size()>2){
							tvProductos.getColumns().remove(0);
						}
						lista = producto.consultar(/*"select clave_producto, clave_td,nombre, precio_mayoreo, precio_menudeo,precio_paquete, fecha_caducidad, clasificacion "
								+ "from producto natural join tipo_producto "
								+ "where estado = TRUE"*/ "select * from consulta_productos_activos;");
						listaBusqueda=new FilteredList<DAOProductos>(lista);
					}
			tvProductos.setItems(lista);
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
					producto=(DAOProductos) BotonActivar.this.getTableView().getItems().get(BotonActivar.this.getIndex());
					if(producto.reactivar()){
						lista = producto.consultar(/*"select clave_producto, clave_td,nombre, precio_mayoreo, precio_menudeo,precio_paquete, fecha_caducidad, clasificacion "
								+ "from producto natural join tipo_producto "
								+ "where estado = false"*/ "select * from consulta_productos_inactivos;");
						tvProductos.setItems(lista);
						tvProductos.refresh();
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
			tvProductos.refresh();
			tvProductos.setItems(lista);
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
				tvProductos.refresh();
				tvProductos.setItems(listaBusqueda);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
