package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOProveedores {

	private int clave_p, clave_dir;
	private String nombre_empresa,telefono, direccion;
	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOProveedores> lista;
	private ControladorBitacora ce;

	public DAOProveedores(){
		this.clave_p=0;
		this.clave_p=0;
		this.nombre_empresa="";
		this.telefono="";
		this.direccion="";
		this.con =new DAOConexion();
		this.lista=FXCollections.observableArrayList();
		this.ce=new ControladorBitacora();
	}

	public int getClave_p() {
		return clave_p;
	}

	public void setClave_p(int clave_p) {
		this.clave_p = clave_p;
	}

	public int getClave_dir() {
		return clave_dir;
	}

	public void setClave_dir(int clave_dir) {
		this.clave_dir = clave_dir;
	}

	public String getNombre_empresa() {
		return nombre_empresa;
	}

	public void setNombre_empresa(String nombre_empresa) {
		this.nombre_empresa = nombre_empresa;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public DAOProveedores validarDatos(){
		DAOProveedores oProveedor=null;
		ResultSet rs=null;
		try{
			if(con.conectar()){
				String sql="select * from proveedor where nombre_empresa = '"+this.nombre_empresa+"' and telefono = '"+this.telefono+"'";
				comando = con.getConexion().prepareStatement(sql);
				rs = comando.executeQuery();

				while (rs.next()){
					oProveedor = new DAOProveedores();
					oProveedor.clave_p=rs.getInt("clave_p");
					oProveedor.clave_dir=rs.getInt("clave_dir");
					oProveedor.nombre_empresa=rs.getString("nombre_empresa");
					oProveedor.telefono=rs.getString("telefono");
					oProveedor.direccion=rs.getString("direccion");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return oProveedor;
	}

	public ObservableList<DAOProveedores> consultar(String consulta){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement(consulta);
				rs = comando.executeQuery();
				while (rs.next()){
					DAOProveedores oProveedor=new DAOProveedores();
					oProveedor.setClave_p(rs.getInt("clave_p"));
					oProveedor.setClave_dir(rs.getInt("clave_dir"));
					oProveedor.setNombre_empresa(rs.getString("nombre_empresa"));
					oProveedor.setTelefono(rs.getString("telefono"));
					oProveedor.setDireccion(rs.getString("direccion"));
					lista.add(oProveedor);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return lista;
	}

	public boolean agregar(){
		try{
			if(con.conectar()){
				String sql="select ingProveedor(?,?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setString(1,this.nombre_empresa);
				comando.setString(2, this.telefono);
				comando.execute();
				ce.imprimirAccion("Agregar", "Catalogo Proveedores");
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

	public boolean eliminar(){
		try{
			if(con.conectar()){
				String sql="select eliProveedor(?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_p);
				comando.execute();
				ce.imprimirAccion("Eliminar", "Catalogo Proveedores");
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			con.desconectar();
		}
	}

	public boolean editar(){
		try{
			if(con.conectar()){
				String sql="select editProveedor(?,?,?,?)";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_p);
				comando.setInt(2, this.clave_dir);
				comando.setString(3, this.nombre_empresa);
				comando.setString(4, this.telefono);
				comando.execute();
				ce.imprimirAccion("Editar", "Catalogo Proveedores");
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

	public boolean reactivar(){
		try{
			if(con.conectar()){
				String sql="update proveedor set estado= true where clave_p= ?";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_p);
				comando.execute();
				ce.imprimirAccion("Reactivar", "Catalogo Proveedores");
			}
			return true;
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
