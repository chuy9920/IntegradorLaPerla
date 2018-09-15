package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOClientes {

	private int clave_cliente, clave_dir;
	private String nombre, apellido_paterno, apellido_materno,razon_social,RFC, direccion;
	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOClientes> lista;
	private ControladorBitacora ce;


	public DAOClientes(){
		this.clave_cliente=0;
		this.clave_dir=0;
		this.nombre="";
		this.apellido_paterno="";
		this.apellido_materno="";
		this.razon_social="";
		this.RFC="";
		this.direccion="";
		this.con =new DAOConexion();
		this.lista=FXCollections.observableArrayList();
		this.ce=new ControladorBitacora();
	}

	public int getClave_cliente() {
		return clave_cliente;
	}

	public void setClave_cliente(int clave_cliente) {
		this.clave_cliente = clave_cliente;
	}

	public int getClave_dir() {
		return clave_dir;
	}

	public void setClave_dir(int clave_dir) {
		this.clave_dir = clave_dir;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido_paterno() {
		return apellido_paterno;
	}

	public void setApellido_paterno(String apellido_paterno) {
		this.apellido_paterno = apellido_paterno;
	}

	public String getApellido_materno() {
		return apellido_materno;
	}

	public void setApellido_materno(String apellido_materno) {
		this.apellido_materno = apellido_materno;
	}

	public String getRazon_social() {
		return razon_social;
	}

	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}

	public String getRFC() {
		return RFC;
	}

	public void setRFC(String rFC) {
		RFC = rFC;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public DAOClientes validarDatos(){
		DAOClientes oCliente=null;
		ResultSet rs=null;
		try{
			if(con.conectar()){
				String sql="select * from cliente where nombre = '"+this.nombre+"' and apellido_paterno = '"+this.apellido_paterno+""
						+ "' and apellido_materno="+this.apellido_materno+"' ";
				comando = con.getConexion().prepareStatement(sql);
				rs = comando.executeQuery();

				while (rs.next()){
					oCliente = new DAOClientes();
					oCliente.clave_cliente=rs.getInt("clave_cliente");
					oCliente.nombre=rs.getString("nombre");
					oCliente.apellido_paterno=rs.getString("apellido_paterno");
					oCliente.apellido_materno=rs.getString("apellido_materno");
					oCliente.razon_social=rs.getString("razon_social");
					oCliente.RFC=rs.getString("RFC");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return oCliente;
	}

	public ObservableList<DAOClientes> consultar(String consulta){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement(consulta);
				rs = comando.executeQuery();
				while (rs.next()){
					DAOClientes oCliente=new DAOClientes();
					oCliente.setClave_cliente(rs.getInt("clave_cliente"));
					oCliente.setClave_dir(rs.getInt("clave_dir"));
					oCliente.setNombre(rs.getString("nombre"));
					oCliente.setApellido_paterno(rs.getString("apellido_paterno"));
					oCliente.setApellido_materno(rs.getString("apellido_materno"));
					oCliente.setRFC(rs.getString("RFC"));
					oCliente.setRazon_social(rs.getString("razon_social"));
					oCliente.setDireccion(rs.getString("direccion"));
					lista.add(oCliente);
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
				String sql="Select ingreCliente(?,?,?,?,?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setString(1,this.nombre);
				comando.setString(2, this.apellido_paterno);
				comando.setString(3, this.apellido_materno);
				comando.setString(4, this.RFC);
				comando.setString(5, this.razon_social);
				comando.execute();
				ce.imprimirAccion("Agregar", "Catalogo Clientes");
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
				String sql="Select eliCliente(?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_cliente);
				comando.execute();
				ce.imprimirAccion("Eliminar", "Catalogo Clientes");
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
				String sql="Select editCliente(?, ?, ?, ?, ?, ?, ?)";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_cliente);
				comando.setInt(2, this.clave_dir);
				comando.setString(3,this.nombre);
				comando.setString(4, this.apellido_paterno);
				comando.setString(5, this.apellido_materno);
				comando.setString(6, this.RFC);
				comando.setString(7, this.razon_social);
				comando.execute();
				ce.imprimirAccion("Editar", "Catalogo Clientes");
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
				String sql="update cliente set estado= true where clave_cliente= ?";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_cliente);
				comando.execute();
				ce.imprimirAccion("Reactivar", "Catalogo Clientes");
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
