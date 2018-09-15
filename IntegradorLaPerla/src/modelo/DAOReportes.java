package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class DAOReportes {
	DAOConexion con;
	JasperPrint impreso;

	public  DAOReportes(){
		con = new DAOConexion();
	}

	public JasperPrint cargarReportedeUsuarios() throws JRException, IOException{
		try{
			if(con.conectar()){
				InputStream input=new FileInputStream(new File(getClass().getResource("C:/Users/Alejandro/Documents/Proyecto Integrador/La Perla/src/vista/reportes").getPath()));
				JasperDesign diseno= JRXmlLoader.load(input);
				JasperReport reporte=JasperCompileManager.compileReport(diseno);
				impreso =JasperFillManager.fillReport(reporte, null, con.getConexion());
				JasperViewer vista=new JasperViewer(impreso,false);
				vista.setVisible(true);
				input.close();
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return impreso;
	}

	public JasperPrint cargarReportedeProveedores() throws JRException, IOException{
		try{
			if(con.conectar()){
				InputStream input=new FileInputStream(new File(getClass().getResource("../vista/reportes/Proveedores.jrxml").getPath()));
				JasperDesign diseno= JRXmlLoader.load(input);
				JasperReport reporte=JasperCompileManager.compileReport(diseno);
				impreso =JasperFillManager.fillReport(reporte, null, con.getConexion());
				JasperViewer vista=new JasperViewer(impreso,false);
				vista.setVisible(true);
				input.close();
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return impreso;
	}

	public JasperPrint cargarReportedeProductos() throws JRException, IOException{
		try{
			if(con.conectar()){
				InputStream input=new FileInputStream(new File(getClass().getResource("../vista/reportes/Productos.jrxml").getPath()));
				JasperDesign diseno= JRXmlLoader.load(input);
				JasperReport reporte=JasperCompileManager.compileReport(diseno);
				impreso =JasperFillManager.fillReport(reporte, null, con.getConexion());
				JasperViewer vista=new JasperViewer(impreso,false);
				vista.setVisible(true);
				input.close();
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return impreso;
	}

	public JasperPrint cargarReportedeClientes() throws JRException, IOException{
		try{
			if(con.conectar()){
				InputStream input=new FileInputStream(new File(getClass().getResource("../vista/reportes/Clientes.jrxml").getPath()));
				JasperDesign diseno= JRXmlLoader.load(input);
				JasperReport reporte=JasperCompileManager.compileReport(diseno);
				impreso =JasperFillManager.fillReport(reporte, null, con.getConexion());
				JasperViewer vista=new JasperViewer(impreso,false);
				vista.setVisible(true);
				input.close();
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return impreso;
	}

	public JasperPrint cargarReportedeEmpleados() throws JRException, IOException{
		try{
			if(con.conectar()){
				InputStream input=new FileInputStream(new File(getClass().getResource("C:/Users/Alejandro/Documents/Proyecto Integrador/La Perla/src/vista/reportes/Empleados.jrxml").getPath()));
				JasperDesign diseno= JRXmlLoader.load(input);
				JasperReport reporte=JasperCompileManager.compileReport(diseno);
				impreso =JasperFillManager.fillReport(reporte, null, con.getConexion());
				JasperViewer vista=new JasperViewer(impreso,false);
				vista.setVisible(true);
				input.close();
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return impreso;
	}
}


