package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOTipoProducto {

	private int clave_td;
	private String clasificacion, descripcion;

	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<String>lista;

	public DAOTipoProducto(){
		this.clave_td=0;
		this.clasificacion="";
		this.descripcion="";
		this.con =new DAOConexion();
		this.lista=FXCollections.observableArrayList();
	}

	public int getClave_td() {
		return clave_td;
	}

	public void setClave_td(int clave_td) {
		this.clave_td = clave_td;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ObservableList<String> consultar(){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando=con.getConexion().prepareStatement("select distinct clasificacion from tipo_producto order by clasificacion");
				rs = comando.executeQuery();
				while(rs.next()){
					lista.add(rs.getString("clasificacion"));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return lista;
	}
}
