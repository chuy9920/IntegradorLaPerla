package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOSepomex {

	private String estado, municipio;

	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<String>listaCiudades;
	private ObservableList<String>listaColonias;

	public DAOSepomex(){
		this.estado = "";
		this.municipio = "";
		this.con =new DAOConexion();
		this.listaCiudades=FXCollections.observableArrayList();
		this.listaColonias=FXCollections.observableArrayList();
	}

	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public ObservableList<String> consultarCiudad(){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando=con.getConexion().prepareStatement("select distinct d_mnpio, d_CP from sepomex where d_estado = 'Veracruz de Ignacio de la Llave' order by D_mnpio;");
				rs = comando.executeQuery();
				while(rs.next()){
					listaCiudades.add(rs.getString("d_mnpio"));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return listaCiudades;
	}

	public ObservableList<String> consultarColonia(){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando=con.getConexion().prepareStatement("select distinct d_asenta from sepomex where d_estado = 'Veracruz de Ignacio de la Llave' and d_mnpio = '"+this.municipio+"' order by d_asenta");
				rs = comando.executeQuery();
				while(rs.next()){
					listaColonias.add(rs.getString("d_asenta"));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return listaColonias;
	}


}
