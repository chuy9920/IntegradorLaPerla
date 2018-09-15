package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOTipoEmpleado {

	private int clave_e;
	private String puestos;

	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<String>lista;

	public DAOTipoEmpleado(){
		this.clave_e=0;
		this.puestos="";
		this.con =new DAOConexion();
		this.lista=FXCollections.observableArrayList();
	}

	public int getClave_e() {
		return clave_e;
	}

	public void setClave_e(int clave_e) {
		this.clave_e = clave_e;
	}

	public String getPuestos() {
		return puestos;
	}

	public void setPuestos(String puestos) {
		this.puestos = puestos;
	}

	public ObservableList<String> consultar(){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando=con.getConexion().prepareStatement("select distinct clave_e , puestos from tipo_empleado order by clave_e");
				rs = comando.executeQuery();
				while(rs.next()){
					lista.add(rs.getString("puestos"));
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
