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
import modelo.DAOEmpleados;
import modelo.DAOReportes;
import modelo.DAOTipoEmpleado;
import modelo.DAOUsuarios;
import modelo.Notification;

@SuppressWarnings("restriction")
public class ControladorEmpleados implements Initializable{

	ObservableList<DAOTipoEmpleado> listaTipoEmpleado;
	DAOTipoEmpleado tipoE = new DAOTipoEmpleado();
	DAOUsuarios insUsu=new DAOUsuarios();

	@FXML TableView<DAOEmpleados> tvEmpleados;
	@FXML DatePicker dpfNaci, dpfContra;
	@FXML TextField txtNombre, txtApa,txtAma, txtSueldo, txtTelefono,txtBuscador;
	@FXML ComboBox<String> cbTipoEm;
	@FXML ComboBox<String> cbSexo;
	@FXML ComboBox<String> cbUsu;
	@FXML Button btnAgregar, btnGuardar, btnEditar, btnEliminar,btnCancelar,btnDireccion,btnSalir;
	@FXML CheckBox ckbInactivos;

	private DAOReportes reporteador;
	private DAOEmpleados empleado;
	private ObservableList <DAOEmpleados> lista;
	private FilteredList<DAOEmpleados>listaBusqueda;
	private String r;

	public ControladorEmpleados(){
		empleado = new DAOEmpleados();
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
			txtApa.textProperty().addListener( (observable, oldValue, newValue) -> {
				if( !newValue.matches("[A-Za-z' ']{0,20}") || newValue.length() > 19){
					((StringProperty)observable).setValue(oldValue);
				}
				else{
					((StringProperty)observable).setValue(newValue);
				}
			});
			txtAma.textProperty().addListener( (observable, oldValue, newValue) -> {
				if( !newValue.matches("[A-Za-z' ']{0,20}") || newValue.length() > 19){
					((StringProperty)observable).setValue(oldValue);
				}
				else{
					((StringProperty)observable).setValue(newValue);
				}
			});
			txtSueldo.textProperty().addListener( (observable, oldValue, newValue) -> {
				if( !newValue.matches("[0-9'.']{0,20}") || newValue.length() > 19){
					((StringProperty)observable).setValue(oldValue);
				}
				else{
					((StringProperty)observable).setValue(newValue);
				}
			});
			txtTelefono.textProperty().addListener( (observable, oldValue, newValue) -> {
				if( !newValue.matches("[0-9'']{0,10}") || newValue.length() > 10){
					((StringProperty)observable).setValue(oldValue);
				}
				else{
					((StringProperty)observable).setValue(newValue);
				}
			});
		}

	public void cargarLista(){
		try{
			cbTipoEm.getItems().clear();
			cbTipoEm.setItems(tipoE.consultar());
			cbTipoEm.getSelectionModel().select("Selecciona tipo");
			cbSexo.getItems().clear();
			cbSexo.getItems().addAll("Hombre", "Mujer");
			cbSexo.getSelectionModel().select("Selecciona sexo");
			cbUsu.getItems().clear();
			cbUsu.setItems(insUsu.consultarUsuEmp());
		}
		catch(Exception ex){

		}
	}

	public void actualizarTabla(){
		tvEmpleados.getItems().clear();
		lista = empleado.consultar(/*"select clave_empleado, clave_dir, clave_e, nombre,apellido_paterno,apellido_materno, fecha_nacimiento, "
				+ "sexo, sueldo,telefono, fecha_contratacion, puestos from empleado natural join tipo_empleado "
				+ "natural join direccion where estado=true"*/ "select * from consulta_empleados_activos;");/* ----*/
		tvEmpleados.setItems(lista);
		tvEmpleados.refresh();
		listaBusqueda=new FilteredList<DAOEmpleados>(lista);
	}

	public void bloquear(){
		txtNombre.setDisable(true);
		txtApa.setDisable(true);
		txtAma.setDisable(true);
		dpfNaci.setDisable(true);
		btnDireccion.setDisable(true);
		txtSueldo.setDisable(true);
		txtTelefono.setDisable(true);
		cbSexo.setDisable(true);
		dpfContra.setDisable(true);
		cbTipoEm.setDisable(true);
		cbUsu.setDisable(true);

		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnGuardar.setDisable(true);
		btnCancelar.setDisable(true);
		btnAgregar.setDisable(false);
	}

	public void limpiar(){
		txtNombre.clear();
		txtApa.clear();
		txtAma.clear();
		dpfNaci.getEditor().clear();
		txtSueldo.clear();
		txtTelefono.clear();
		cbSexo.getItems().clear();
		dpfContra.getEditor().clear();
		cbTipoEm.getItems().clear();
	}

	@FXML public void clickAgregar(){
		txtNombre.setDisable(false);
		txtApa.setDisable(false);
		txtAma.setDisable(false);
		dpfNaci.setDisable(false);
		btnDireccion.setDisable(false);
		txtSueldo.setDisable(false);
		txtTelefono.setDisable(false);
		cbSexo.setDisable(false);
		dpfContra.setDisable(false);
		cbTipoEm.setDisable(false);
		cbUsu.setDisable(false);

		btnGuardar.setDisable(false);
		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnCancelar.setDisable(false);
		btnAgregar.setDisable(true);
	}

	@FXML public void clickGuardar(){
		try{
			LocalDate actual=LocalDate.now();
			LocalDate faux1=dpfNaci.getValue();
			LocalDate faux2=dpfContra.getValue();
			if(txtNombre.getText().trim().isEmpty() || txtApa.getText().trim().isEmpty() || txtAma.getText().trim().isEmpty()
					|| txtSueldo.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()
					|| dpfNaci.getValue().toString().isEmpty() || dpfContra.getValue().toString().isEmpty()/*dpfNaci, dpfContra*/
					|| cbSexo.getSelectionModel().isEmpty() || cbTipoEm.getSelectionModel().isEmpty() || faux1.compareTo(actual)>(-14) || faux2.isBefore(actual)==true){
				if(faux1.compareTo(actual)>(-14)) Notification.Notifier.INSTANCE.notifyInfo("Info", "La fecha de nacimiento es invalida");
				else if(faux2.isBefore(actual)==true) Notification.Notifier.INSTANCE.notifyInfo("Info", "La fecha de contratacion es invalida");
				else Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
			}
			else{
				empleado.setNombre(txtNombre.getText());
				empleado.setApellido_paterno(txtApa.getText());
				empleado.setApellido_materno(txtAma.getText());
				empleado.setFecha_nacimiento(String.valueOf(dpfNaci.getValue()));
				String s1=txtSueldo.getText();
				float s = Float.parseFloat(s1);
				empleado.setSueldo(s);
				empleado.setTelefono(txtTelefono.getText());

				if(cbSexo.getSelectionModel().getSelectedItem().equals("Hombre")){
					r="H";
				}else{
					r="M";
				}
				empleado.setSexo(r);


				//empleado.setFecha_nacimiento(String.valueOf(dpfNaci.getValue()));

				LocalDate dpfecha=dpfNaci.getValue();
				String fecha = dpfecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				String [] fecha1=fecha.split("/");
				String dia =fecha1[0];
				String mes =fecha1[1];
				String año =fecha1[2];

				empleado.setFecha_nacimiento(dia+"-"+mes+"-"+año);

				//empleado.setFecha_contratacion(String.valueOf(dpfContra.getValue()));

				dpfecha=dpfContra.getValue();
				fecha = dpfecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				fecha1=fecha.split("/");
				String dia1 =fecha1[0];
				String mes1 =fecha1[1];
				String año1 =fecha1[2];
				empleado.setFecha_contratacion(dia1+"-"+mes1+"-"+año1);

				this.empleado.setUsuario(cbUsu.getSelectionModel().getSelectedItem());

				if(empleado.agregar(cbTipoEm.getSelectionModel().getSelectedItem())){
					Notification.Notifier.INSTANCE.notifySuccess("Success", "El empleado se agrego con éxito.");
					actualizarTabla();
					bloquear();
					limpiar();
				}
				else{
					Notification.Notifier.INSTANCE.notifyError("Error", "El empleado no se pudo guardar. Intentalo de nuevo.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@FXML public void clickTabla(){
		cargarLista();
		if(tvEmpleados.getSelectionModel().getSelectedItem() != null){
			empleado= tvEmpleados.getSelectionModel().getSelectedItem();
			txtNombre.setText(empleado.getNombre());
			txtApa.setText(empleado.getApellido_paterno());
			txtAma.setText(empleado.getApellido_materno());
			txtTelefono.setText(empleado.getTelefono());
			float s1=empleado.getSueldo();
			String s = Float.toString(s1);


			txtSueldo.setText(s);
			cbUsu.getSelectionModel().select(empleado.getUsuario());
			System.out.println(empleado.getUsuario());
			cbTipoEm.getSelectionModel().select(empleado.getPuestos());
			cbSexo.getSelectionModel().select(empleado.getSexo());
			if(empleado.getSexo().equals("H")){
				cbSexo.getSelectionModel().select(0);
			}else{
				cbSexo.getSelectionModel().select(1);
			}

			String fecha=empleado.getFecha_nacimiento();
			/*String [] fecha1=fecha.split("-");
			String dia =fecha1[0];
			String mes =fecha1[1];
			String año =fecha1[2];*/

			LocalDate data=LocalDate.parse(fecha);
			System.out.println("Fecha 1: "+data);
			dpfNaci.setValue(data);

			String fecha2=empleado.getFecha_contratacion();
			/*String [] fecha3=fecha2.split("-");
			String dia1 =fecha3[0];
			String mes1 =fecha3[1];
			String año1 =fecha3[2];*/

			LocalDate data1=LocalDate.parse(fecha2);
			System.out.println("Fecha 2: "+data1);
			dpfContra.setValue(data1);



			btnEditar.setDisable(false);
			btnEliminar.setDisable(false);
			btnCancelar.setDisable(false);
			txtNombre.setDisable(false);
			txtApa.setDisable(false);
			txtAma.setDisable(false);
			dpfNaci.setDisable(false);
			btnDireccion.setDisable(false);
			txtSueldo.setDisable(false);
			txtTelefono.setDisable(false);
			cbSexo.setDisable(false);
			dpfContra.setDisable(false);
			cbTipoEm.setDisable(false);
			cbUsu.setDisable(false);

			btnAgregar.setDisable(true);
		}
		else{
			Notification.Notifier.INSTANCE.notifyWarning("Warning", "Selecciona un empleado de la tabla.");
		}
	}


	@FXML public void clickEliminar(){
		if(empleado.getClave_empleado() >0){
			Notification.Notifier.INSTANCE.notifySuccess("Success", "El empleado se eliminó con éxito.");
			empleado.eliminar();
			actualizarTabla();
		}
		else{
			Notification.Notifier.INSTANCE.notifyInfo("Info", "No tienes ningun empleado seleccionado, selecciona algo para eliminar");
		}
		limpiar();
		bloquear();
		cargarLista();
	}

	@FXML public void clickEditar(){
		LocalDate actual=LocalDate.now();
		LocalDate faux1=dpfNaci.getValue();
		LocalDate faux2=dpfContra.getValue();
		if(txtNombre.getText().trim().isEmpty() || txtApa.getText().trim().isEmpty() || txtAma.getText().trim().isEmpty()
				|| txtSueldo.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()
				|| dpfNaci.getValue().toString().isEmpty() || dpfContra.getValue().toString().isEmpty()/*dpfNaci, dpfContra*/
				|| cbSexo.getSelectionModel().isEmpty() || cbTipoEm.getSelectionModel().isEmpty() || faux1.compareTo(actual)>(-14) || faux2.isBefore(actual)==true){
			if(faux1.compareTo(actual)>(-14)) Notification.Notifier.INSTANCE.notifyInfo("Info", "La fecha de nacimiento es invalida");
			else if(faux2.isBefore(actual)==true) Notification.Notifier.INSTANCE.notifyInfo("Info", "La fecha de contratacion es invalida");
			else Notification.Notifier.INSTANCE.notifyInfo("Info", "Todos los campos son obligatorios.");
		}
		else{
			/*cbTipoEm.getItems().clear();
			cbSexo.getItems().clear();
			cargarLista();*/
			this.empleado.setNombre(txtNombre.getText());
			this.empleado.setApellido_paterno(txtApa.getText());
			this.empleado.setApellido_materno(txtAma.getText());
			this.empleado.setTelefono(txtTelefono.getText());

			String s1=txtSueldo.getText();
			float s = Float.parseFloat(s1);

			this.empleado.setSueldo(s);

			//empleado.setFecha_nacimiento(String.valueOf(dpfNaci.getValue()));

			LocalDate dpfecha=dpfNaci.getValue();
			String fecha = dpfecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String [] fecha1=fecha.split("/");
			String dia =fecha1[0];
			String mes =fecha1[1];
			String año =fecha1[2];

			empleado.setFecha_nacimiento(dia+"-"+mes+"-"+año);

			//empleado.setFecha_contratacion(String.valueOf(dpfContra.getValue()));

			dpfecha=dpfContra.getValue();
			fecha = dpfecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			fecha1=fecha.split("/");
			String dia1 =fecha1[0];
			String mes1 =fecha1[1];
			String año1 =fecha1[2];
			empleado.setFecha_contratacion(dia1+"-"+mes1+"-"+año1);



			if(cbSexo.getSelectionModel().getSelectedItem().equals("Hombre")){
				r="H";
			}else{
				r="M";
			}
			this.empleado.setSexo(r);

			this.empleado.setUsuario(cbUsu.getSelectionModel().getSelectedItem());

			String clasificacion= cbTipoEm.getSelectionModel().getSelectedItem();
			if(empleado.editar(clasificacion)){
				Notification.Notifier.INSTANCE.notifySuccess("Success", "Los datos se modificaron correctamente");
				actualizarTabla();
				limpiar();
				bloquear();
				cargarLista();
			}
			else{
				Notification.Notifier.INSTANCE.notifyError("Error", "El empleado no se pudo actualizar, intenta de nuevo");
			}
		}
	}

	@FXML public void clickCancelar(){
		limpiar();
		bloquear();
		cargarLista();
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
			tvEmpleados.getItems().clear();
			if(ckbInactivos.isSelected()){
				lista = empleado.consultar(/*"select clave_empleado, clave_dir, clave_e, nombre,apellido_paterno,apellido_materno, fecha_nacimiento, "
						+ "sexo, sueldo,telefono, fecha_contratacion, puestos from empleado natural join tipo_empleado "
						+ "natural join direccion where estado=false"*/ "select * from consulta_empleados_inactivos;");
				listaBusqueda=new FilteredList<DAOEmpleados>(lista);

				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvEmpleados.getColumns().add(0, columnaRestaurar);
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
						if(tvEmpleados.getColumns().size()>2){
							tvEmpleados.getColumns().remove(0);
						}
						lista = empleado.consultar(/*"select clave_empleado, clave_dir, clave_e, nombre,apellido_paterno,apellido_materno, fecha_nacimiento, "
								+ "sexo, sueldo,telefono, fecha_contratacion, puestos from empleado natural join tipo_empleado "
								+ "natural join direccion where estado=true"*/ "select * from consulta_empleados_activos;");
						listaBusqueda=new FilteredList<DAOEmpleados>(lista);
					}
			tvEmpleados.setItems(lista);
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
					empleado=(DAOEmpleados) BotonActivar.this.getTableView().getItems().get(BotonActivar.this.getIndex());
					if(empleado.reactivar()){
						lista = empleado.consultar(/*"select clave_empleado, clave_dir, clave_e, nombre,apellido_paterno,apellido_materno, fecha_nacimiento, "
								+ "sexo, sueldo,telefono, fecha_contratacion, puestos from empleado natural join tipo_empleado "
								+ "natural join direccion where estado=false"*/ "select * from consulta_empleados_inactivos;");
						tvEmpleados.setItems(lista);
						tvEmpleados.refresh();
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
			tvEmpleados.refresh();
			tvEmpleados.setItems(lista);
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
				tvEmpleados.refresh();
				tvEmpleados.setItems(listaBusqueda);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@FXML public void clickReporte() throws IOException{
		try{
			reporteador.cargarReportedeEmpleados();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
