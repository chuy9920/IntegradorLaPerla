package controlador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import modelo.DAOUsuarios;

public class ControladorBitacora {
	private DateFormat formato;
	private Date fecha;
	private ControladorVentanas instancia;

	public ControladorBitacora(){
		formato =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		fecha=new Date();
		instancia=ControladorVentanas.getInstancia();
	}

	public void imprimirError(String mensaje, String clase){
		FileWriter fw=null;
		BufferedWriter bw=null;
		try {
			File archivo=new File ("src/log.txt");
			fw =new FileWriter(archivo, true);
			bw =new BufferedWriter(fw);
			bw.write("BITACORA DE ERRORES");
			bw.newLine();
			bw.write("Error en: "+clase);
			bw.newLine();
			bw.write("Fecha: "+formato.format(fecha));
			bw.newLine();
			bw.write("Descripcion del error: "+mensaje);
			bw.newLine();
			bw.write("---------------------------------------------------------------------");
			bw.newLine();
			bw.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void imprimirAccion(String mensaje, String clase){
		FileWriter fw=null;
		BufferedWriter bw=null;
		DAOUsuarios usuario=(DAOUsuarios) instancia.getPrimaryStage().getUserData();
		try {

			File archivo=new File ("src/bitacora.txt");
			fw =new FileWriter(archivo, true);
			bw =new BufferedWriter(fw);
			bw.write("BITACORA DE OPERACIONES");
			bw.newLine();
			bw.write("Accion en: "+clase);
			bw.newLine();
			bw.write("Fecha: "+formato.format(fecha));
			bw.newLine();
			bw.write("Descripcion de la accion: "+mensaje);
			bw.newLine();
			bw.write("Realizada por: "+usuario.getUsuario());
			bw.newLine();
			bw.write("---------------------------------------------------------------------");
			bw.newLine();
			bw.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
