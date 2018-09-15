package modelo;

import java.sql.Connection;
import java.sql.DriverManager;

import controlador.ControladorBitacora;

public class DAOConexion {
	private String servidor, usuario, contrasena,puerto, base_datos;
	private Connection conexion;
	private ControladorBitacora ce;


	public DAOConexion(){
		this.servidor="localhost";
		this.usuario="postgres";
		this.contrasena="power123";
		this.puerto="5432";
		this.base_datos="la_perla";
	}

	public boolean conectar(){
		try{
			Class.forName("org.postgresql.Driver");
			conexion = DriverManager.getConnection("jdbc:postgresql://"+servidor+":"+puerto+"/"+base_datos,usuario,contrasena);
			System.out.println("Conectado a: "+conexion.getCatalog());
			return true;
		}
		catch (Exception e){
			ce.imprimirError(e.getMessage(), "DAOConexion" );
			return false;
		}
	}

	public boolean desconectar(){
		try{
			conexion.close();
			System.out.println("Conexion cerrada");
			return true;
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public Connection getConexion(){
		return conexion;
	}

}
