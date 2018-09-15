package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOEmpleados {

	private int clave_empleado, clave_dir, clave_e, id_usuario;
	private String nombre, apellido_paterno, apellido_materno, fecha_nacimiento,sexo,telefono,fecha_contratacion, puestos,usuario;
	private float sueldo;

	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOEmpleados> lista;
	private ControladorBitacora ce;


	public DAOEmpleados(){
	this.clave_empleado=0;
	this.clave_dir=0;
	this.clave_e=0;
	this.id_usuario=0;
	this.nombre="";
	this.apellido_paterno="";
	this.apellido_materno="";
	this.fecha_nacimiento="";
	this.sexo="";
	this.telefono="";
	this.fecha_contratacion="";
	this.puestos="";
	this.con =new DAOConexion();
	this.lista=FXCollections.observableArrayList();
	this.ce=new ControladorBitacora();
	}

	public int getClave_empleado() {
		return clave_empleado;
	}
	public void setClave_empleado(int clave_empleado) {
		this.clave_empleado = clave_empleado;
	}
	public int getClave_dir() {
		return clave_dir;
	}
	public void setClave_dir(int clave_dir) {
		this.clave_dir = clave_dir;
	}
	public int getClave_e() {
		return clave_e;
	}
	public void setClave_e(int clave_e) {
		this.clave_e = clave_e;
	}
	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
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
	public String getFecha_nacimiento() {
		return fecha_nacimiento;
	}
	public void setFecha_nacimiento(String fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getFecha_contratacion() {
		return fecha_contratacion;
	}
	public void setFecha_contratacion(String fecha_contratacion) {
		this.fecha_contratacion = fecha_contratacion;
	}
	public float getSueldo() {
		return sueldo;
	}
	public void setSueldo(float sueldo) {
		this.sueldo = sueldo;
	}

	public String getPuestos() {
		return puestos;
	}

	public void setPuestos(String puestos) {
		this.puestos = puestos;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public DAOEmpleados validarDatos(){
		DAOEmpleados oEmpleado=null;
		ResultSet rs=null;
		try{
			if(con.conectar()){
				String sql="select * from empleado where nombre = '"+this.nombre+"' and apellido_paterno= '"+this.apellido_paterno+"' "
						+ "and apellido_materno=' "+this.apellido_materno+" ' and fecha_nacimiento=' "+this.fecha_nacimiento+" ' "
								+ "and sexo=' "+this.sexo+"' and sueldo="+this.sueldo+" and telefono='"+this.telefono+"' "
										+ "and fecha_contratacion='"+this.fecha_contratacion+"';";
				comando = con.getConexion().prepareStatement(sql);
				rs = comando.executeQuery();

				while (rs.next()){
					oEmpleado = new DAOEmpleados();
					oEmpleado.clave_empleado=rs.getInt("clave_empleado");
					oEmpleado.clave_dir=rs.getInt("clave_dir");
					oEmpleado.clave_e=rs.getInt("clave_e");
					oEmpleado.nombre=rs.getString("nombre");
					oEmpleado.apellido_paterno=rs.getString("apellido_paterno");
					oEmpleado.apellido_materno=rs.getString("apellido_materno");
					oEmpleado.fecha_nacimiento=rs.getString("fecha_nacimiento");
					oEmpleado.sexo=rs.getString("sexo");
					oEmpleado.sueldo=rs.getFloat("sueldo");
					oEmpleado.telefono=rs.getString("telefono");
					oEmpleado.fecha_contratacion=rs.getString("fecha_contratacion");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return oEmpleado;
	}

	public ObservableList<DAOEmpleados> consultar(String consulta){
		ResultSet rs=null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement(consulta);
				rs = comando.executeQuery();
				while (rs.next()){
					DAOEmpleados oEmpleado=new DAOEmpleados();
					oEmpleado.setClave_empleado(rs.getInt("clave_empleado"));
					oEmpleado.setClave_dir(rs.getInt("clave_dir"));
					oEmpleado.setClave_e(rs.getInt("clave_e"));
					oEmpleado.setNombre(rs.getString("nombre"));
					oEmpleado.setApellido_paterno(rs.getString("apellido_paterno"));
					oEmpleado.setApellido_materno(rs.getString("apellido_materno"));
					oEmpleado.setFecha_nacimiento(rs.getString("fecha_nacimiento"));
					oEmpleado.setSexo(rs.getString("sexo"));
					oEmpleado.setSueldo(rs.getFloat("sueldo"));
					oEmpleado.setTelefono(rs.getString("telefono"));
					oEmpleado.setFecha_contratacion(rs.getString("fecha_contratacion"));
					oEmpleado.setPuestos(rs.getString("puestos"));
					oEmpleado.setId_usuario(rs.getInt("idusuarios"));
					oEmpleado.setUsuario(rs.getString("usuarios"));
					lista.add(oEmpleado);
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
				String sql="Select ingrEmpleado(?,?,?,?,?,?,?,?,?,?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setString(1, clasificacion);
				comando.setString(2,this.nombre);
				comando.setString(3,this.apellido_paterno);
				comando.setString(4,this.apellido_materno);
				comando.setString(5,this.fecha_nacimiento);
				comando.setString(6,this.sexo);
				comando.setFloat(7, this.sueldo);
				comando.setString(8,this.telefono);
				comando.setString(9, this.fecha_contratacion);
				comando.setString(10, this.usuario);
				comando.execute();
				ce.imprimirAccion("Agregar", "Catalogo Empleados");
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
				String sql="select eliEmpleado(?)";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_empleado);
				comando.execute();
				ce.imprimirAccion("Eliminar", "Catalogo Empleados");
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
				String sql="select editEmpleado(?,?,?,?,?,?,?,?,?,?,?)";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_empleado);
				System.out.println(clasificacion);
				comando.setString(2, clasificacion);
				comando.setString(3,this.nombre);
				comando.setString(4,this.apellido_paterno);
				comando.setString(5,this.apellido_materno);
				comando.setString(6,this.fecha_nacimiento);
				comando.setString(7,this.sexo);
				comando.setFloat(8, this.sueldo);
				comando.setString(9,this.telefono);
				comando.setString(10, this.fecha_contratacion);
				comando.setString(11, this.usuario);
				comando.execute();
				ce.imprimirAccion("Editar", "Catalogo Empleados");
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
				String sql="update empleado set estado= true where clave_empleado= ?";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_empleado);
				comando.execute();
				ce.imprimirAccion("Reactivar", "Catalogo Empleados");
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
