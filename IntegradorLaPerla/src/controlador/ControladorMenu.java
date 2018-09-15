package controlador;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import modelo.DAOUsuarios;

public class ControladorMenu implements Initializable{

	private ControladorVentanas instancia;

	@FXML Label lblMensaje, lblHora, lblBienvenida;
	@FXML Button btnEmpleados,btnProductos, btnProveedor, btnClientes, btnUsuarios,btnCerrar;

	public ControladorMenu(){
		instancia=ControladorVentanas.getInstancia();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		DAOUsuarios usuario=(DAOUsuarios) instancia.getPrimaryStage().getUserData();
//		lblMensaje.setText("Usuario: "+usuario.getUsuario());
		lblHora.setText("Hora de acceso: "+(new Date()).toString());
//		lblBienvenida.setText("Bienvenido al Sistema "+usuario.getUsuario());

		/*switch(usuario.getTipo()){
		case "administrador":
			btnEmpleados.setDisable(false);
			btnProductos.setDisable(false);
			btnProveedor.setDisable(false);
			btnClientes.setDisable(false);
			btnCerrar.setDisable(false);
			break;
		case "empleado":
			btnEmpleados.setDisable(true);
			btnProductos.setDisable(true);
			btnProveedor.setDisable(false);
			btnClientes.setDisable(false);
			btnUsuarios.setDisable(true);
			btnCerrar.setDisable(false);
			break;
		default:
			btnEmpleados.setDisable(true);
			btnProductos.setDisable(true);
			btnProveedor.setDisable(true);
			btnClientes.setDisable(true);
			btnCerrar.setDisable(false);
			break;
		}*/

	}

	@FXML public void clickEmpleados(){
		instancia.asignarModal("../vista/Empleados.fxml", "Empleados");
	}

	@FXML public void clickClientes(){
		instancia.asignarModal("../vista/Cliente.fxml", "Clientes");
	}

	@FXML public void clickProductos(){
		instancia.asignarModal("../vista/Productos.fxml", "Productos");
	}

	@FXML public void clickProveedor(){
		instancia.asignarModal("../vista/Proveedores.fxml", "Proveedoress");
	}

	@FXML public void clickUsuarios(){
		instancia.asignarModal("../vista/Usuarios.fxml", "Usuarios");
	}

	@FXML public void clickCerrar(){
		Alert alerta =new Alert (AlertType.CONFIRMATION);
		alerta.setTitle("Salir sesión");
		alerta.setHeaderText("¿Estas seguro de que quieres cerrar sesión?");

		Optional<ButtonType> result = alerta.showAndWait();
		if (result.get() == ButtonType.OK){
			instancia.cerrar();
			instancia.asignarModal("../vista/Inicio.fxml", "Micelanea La perla");
			}
	}

}
