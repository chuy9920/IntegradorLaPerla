package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOUsuarios {

	private int id_usuario;
	private String usuario, contrasena, tipo;
	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOUsuarios> lista;
	private ObservableList<String> listaUsu;
	private ControladorBitacora ce;

	public DAOUsuarios(){
		this.id_usuario=0;
		this.usuario="";
		this.contrasena="";
		this.tipo="";
		this.con =new DAOConexion();
		this.lista=FXCollections.observableArrayList();
		this.listaUsu=FXCollections.observableArrayList();
		this.ce=new ControladorBitacora();
	}

	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public DAOUsuarios validarDatos(){
		DAOUsuarios oUsuario=null;
		ResultSet rs=null;
		try{
			if(con.conectar()){
				String sql="select * from usuarios where usuario = '"+this.usuario+"' and contrasena = '"+this.contrasena+"' and "
						+ "estado=true";
				comando = con.getConexion().prepareStatement(sql);
				rs = comando.executeQuery();

				while (rs.next()){
					oUsuario = new DAOUsuarios();
					oUsuario.id_usuario=rs.getInt("id_usuario");
					oUsuario.usuario=rs.getString("usuario");
					oUsuario.tipo=rs.getString("tipo");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return oUsuario;
	}

	public ObservableList<DAOUsuarios> consultar(String consulta){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement(consulta);
				rs = comando.executeQuery();
				while (rs.next()){
					DAOUsuarios oUsuarios=new DAOUsuarios();
					oUsuarios.setId_usuario(rs.getInt("id_usuario"));
					oUsuarios.setUsuario(rs.getString("usuario"));
					oUsuarios.setContrasena(rs.getString("contrasena"));
					oUsuarios.setTipo(rs.getString("tipo"));
					lista.add(oUsuarios);
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

	public ObservableList<String> consultarUsuEmp(){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement("select * from empleado;");
				rs = comando.executeQuery();
				while (rs.next()){
					listaUsu.add(rs.getString("usuario"));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return listaUsu;
	}

	public boolean agregar(){
		try{
			if(con.conectar()){
				String sql="Select inUsuario (?, ?, ?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setString(1,this.usuario);
				comando.setString(2, this.contrasena);
				comando.setString(3, this.tipo);
				comando.execute();
				ce.imprimirAccion("Agregar", "Catalogo Usuarios");
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
				String sql="Select eliUsuario(?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.id_usuario);
				comando.execute();
				ce.imprimirAccion("Eliminar", "Catalogo Usuarios");
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
				String sql="Select editUsuario(?, ?, ?, ?)";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.id_usuario);
				comando.setString(2, this.usuario);
				comando.setString(3, this.contrasena);
				comando.setString(4, this.tipo);
				comando.execute();
				ce.imprimirAccion("Editar", "Catalogo Usuarios");
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
				String sql="update usuarios set estado= true where id_usuario= ?";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.id_usuario);
				comando.execute();
				ce.imprimirAccion("Reactivar", "Catalogo Usuarios");
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
