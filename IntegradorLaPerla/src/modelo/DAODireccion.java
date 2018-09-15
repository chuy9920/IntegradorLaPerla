package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAODireccion {

	private int clave_dir;
	private String calle;
	private String avenida;
	private String colonia;
	private String ciudad;
	private String numero;
	private DAOConexion con;
	private PreparedStatement comando;

	public DAODireccion(){
		this.clave_dir=0;
		this.calle="";
		this.avenida="";
		this.colonia="";
		this.ciudad="";
		this.numero="";
		this.con =new DAOConexion();
	}

	public int getClave_dir() {
		return clave_dir;
	}
	public void setClave_dir(int clave_dir) {
		this.clave_dir = clave_dir;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getAvenida() {
		return avenida;
	}
	public void setAvenida(String avenida) {
		this.avenida = avenida;
	}
	public String getColonia() {
		return colonia;
	}
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public DAODireccion validarDatos(){
		DAODireccion oDireccion=null;
		ResultSet rs=null;
		try{
			if(con.conectar()){
				String sql="select * from direccion where calle = '"+this.calle+"' and avenida = '"+this.avenida+"' and colonia=' "+this.colonia+"' and ciudad=' "+this.ciudad+" ' and numero='"+this.numero+"'";
				comando = con.getConexion().prepareStatement(sql);
				rs = comando.executeQuery();

				while (rs.next()){
					oDireccion = new DAODireccion();
					oDireccion.clave_dir=rs.getInt("clave_dir");
					oDireccion.calle=rs.getString("calle");
					oDireccion.avenida=rs.getString("avenida");
					oDireccion.colonia=rs.getString("colonia");
					oDireccion.ciudad=rs.getString("ciudad");
					oDireccion.numero=rs.getString("numero");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return oDireccion;
	}

	public boolean agregar(){
		try{
			if(con.conectar()){
				String sql="Select inDireccion (?, ?, ?, ?, ?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setString(1,this.calle);
				comando.setString(2, this.avenida);
				comando.setString(3, this.colonia);
				comando.setString(4, this.ciudad);
				comando.setString(5, this.numero);
				comando.execute();
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
		con.desconectar();
		}
	}

}
