package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOProductos {

	private int clave_producto, clave_td;
	private String nombre,fecha_caducidad, clasificacion;
	private float precio_mayoreo, precio_menudeo, precio_paquete;
	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOProductos> lista;
	private ControladorBitacora ce;

	public DAOProductos(){
		this.clave_producto=0;
		this.clave_td=0;
		this.nombre="";
		this.precio_mayoreo=0;
		this.precio_menudeo=0;
		this.precio_paquete=0;
		this.fecha_caducidad="";
		this.clasificacion="";
		this.con =new DAOConexion();
		this.lista=FXCollections.observableArrayList();
		this.ce=new ControladorBitacora();
	}

	public int getClave_producto() {
		return clave_producto;
	}

	public void setClave_producto(int clave_producto) {
		this.clave_producto = clave_producto;
	}

	public int getClave_td() {
		return clave_td;
	}

	public void setClave_td(int clave_td) {
		this.clave_td = clave_td;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getPrecio_mayoreo() {
		return precio_mayoreo;
	}

	public void setPrecio_mayoreo(float precio_mayoreo) {
		this.precio_mayoreo = precio_mayoreo;
	}

	public float getPrecio_menudeo() {
		return precio_menudeo;
	}

	public void setPrecio_menudeo(float precio_menudeo) {
		this.precio_menudeo = precio_menudeo;
	}

	public float getPrecio_paquete() {
		return precio_paquete;
	}

	public void setPrecio_paquete(float precio_paquete) {
		this.precio_paquete = precio_paquete;
	}

	public String getFecha_caducidad() {
		return fecha_caducidad;
	}

	public void setFecha_caducidad(String fecha_caducidad) {
		this.fecha_caducidad = fecha_caducidad;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public DAOProductos validarDatos(){
		DAOProductos oProducto=null;
		ResultSet rs=null;
		try{
			if(con.conectar()){
				String sql="select * from producto where nombre = '"+this.nombre+"' and precio_mayoreo = "+this.precio_mayoreo+""
						+ " and precio_mayoreo ="+this.precio_mayoreo+"' and precio_menudeo="+this.precio_menudeo+" "
								+ "and precio_paquete="+this.precio_paquete+" and fecha_caducidad= '"+this.fecha_caducidad+"'";
				comando = con.getConexion().prepareStatement(sql);
				rs = comando.executeQuery();

				while (rs.next()){
					oProducto = new DAOProductos();
					oProducto.clave_producto=rs.getInt("clave_producto");
					oProducto.clave_td=rs.getInt("clave_td");
					oProducto.nombre=rs.getString("nombre");
					oProducto.precio_mayoreo=rs.getFloat("precio_mayoreo");
					oProducto.precio_mayoreo=rs.getFloat("precio_menudeo");
					oProducto.precio_mayoreo=rs.getFloat("precio_paquete");
					oProducto.fecha_caducidad=rs.getString("fecha_caducidad");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return oProducto;
	}

	public ObservableList<DAOProductos> consultar(String consulta){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement(consulta);
				rs = comando.executeQuery();
				while (rs.next()){
					DAOProductos oProducto=new DAOProductos();
					oProducto.setClave_producto(rs.getInt("clave_producto"));
					oProducto.setClave_td(rs.getInt("clave_td"));
					oProducto.setNombre(rs.getString("nombre"));
					oProducto.setPrecio_mayoreo(rs.getFloat("precio_mayoreo"));
					oProducto.setPrecio_menudeo(rs.getFloat("precio_menudeo"));
					oProducto.setPrecio_paquete(rs.getFloat("precio_paquete"));
					oProducto.setFecha_caducidad(rs.getString("fecha_caducidad"));
					oProducto.setClasificacion(rs.getString("clasificacion"));
					lista.add(oProducto);
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

	public boolean agregar(String clasificacion){
		try{
			if(con.conectar()){
				String sql="Select inproductos(?,?,?,?,?,?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setString(1, clasificacion);
				comando.setString(2,this.nombre);
				comando.setFloat(3, this.precio_mayoreo);
				comando.setFloat(4, this.precio_menudeo);
				comando.setFloat(5, this.precio_paquete);
				SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
				Date data= formato.parse(this.fecha_caducidad);
				System.out.println("Tengo esto en data: "+data);
				@SuppressWarnings("deprecation")
				java.sql.Date datasql=new java.sql.Date(data.getYear(), data.getMonth(), data.getDay());
				System.out.println("Mandare esta fecha: "+datasql);
				comando.setDate(6, datasql);
				comando.execute();
				ce.imprimirAccion("Agregar", "Catalogo Productos");
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
				String sql="select eliproductos(?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_producto);
				comando.execute();
				ce.imprimirAccion("Eliminar", "Catalogo Productos");
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

	public boolean editar(String clasificacion){
		try{
			if(con.conectar()){
				String sql="select editProductos(?,?,?,?,?,?,?)";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_producto);
				comando.setString(2, clasificacion);
				comando.setString(3, this.nombre);
				comando.setFloat(4, this.precio_mayoreo);
				comando.setFloat(5, this.precio_menudeo);
				comando.setFloat(6, this.precio_paquete);
				SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
				Date data= formato.parse(this.fecha_caducidad);
				System.out.println("Tengo esto en data: "+data);
				@SuppressWarnings("deprecation")
				java.sql.Date datasql=new java.sql.Date(data.getYear(), data.getMonth(), data.getDay());
				System.out.println("Mandare esta fecha: "+datasql);
				comando.setDate(7, datasql);
				comando.execute();
				ce.imprimirAccion("Editar", "Catalogo Productos");
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
				String sql="update producto set estado= true where clave_producto= ?";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_producto);
				comando.execute();
				ce.imprimirAccion("Reactivar", "Catalogo Productos");
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
