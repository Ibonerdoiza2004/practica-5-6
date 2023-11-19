package practica5_6;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class GestionTwitter {
	public static TreeMap <String, UsuarioTwitter>usuariosPorID= new TreeMap<>();
	public static TreeMap <String, UsuarioTwitter>usuariosPorNick= new TreeMap<>();
	public static TreeSet <UsuarioTwitter>usuariosConAmigosEnSistema= new TreeSet<>();
	public static boolean consola;
	public static void putUsuarioPorID(UsuarioTwitter usuario) {
		if (!usuariosPorID.containsKey(usuario.id)) {	
			usuariosPorID.put(usuario.id, usuario);	
		}else {
			if (consola) {
				System.out.println("Usuario con ID "+usuario.id+" repetido.");
			}else {
				Ventana.tArea.append("Usuario con ID "+usuario.id+" repetido.\n");
				Ventana.tArea.setCaretPosition(Ventana.tArea.getDocument().getLength());
			}
			
		}
		
	}
	public static void putUsuarioPorNick() {
		Ventana.nombreProceso.setText("Creando mapa por nicks...");
		int i =0;
		for (UsuarioTwitter usuario:usuariosPorID.values()) {
			i++;
			if(!GestionTwitter.consola) {
				CSV.progress = (int)(((double)i/((double)CSV.numLin))*100);
				Ventana.pBar.setValue(CSV.progress);
				Ventana.tArea.append(usuario.screenName+"\n");
				Ventana.tArea.setCaretPosition(Ventana.tArea.getDocument().getLength());
			}
			if (!usuariosPorNick.containsKey(usuario.screenName)) {
				usuariosPorNick.put(usuario.screenName, usuario);
			}else {
				if (consola) {
					System.out.println("Usuario con nombre "+usuario.screenName+" repetido.");
				}else {
					Ventana.tArea.append("Usuario con nombre "+usuario.screenName+" repetido.\n");
					Ventana.tArea.setCaretPosition(Ventana.tArea.getDocument().getLength());
				}
				
			}
			
		}
	}
	public static void amigosUsuarios() {
		int total = 0;
		int i = 0;
		Ventana.nombreProceso.setText("Buscando usuarios con amigos en nuestro sistema...");
		for (UsuarioTwitter usuario:usuariosPorNick.values()) {
			i++;
			int amigosUsuario = 0;
			for (String idAmigo: usuario.friends) {
				CSV.progress = (int)(((double)i/((double)CSV.numLin))*100);
				Ventana.pBar.setValue(CSV.progress);
				if (usuariosPorID.containsKey(idAmigo)) {
					amigosUsuario++;
				}
			}
			if((amigosUsuario!=0&&consola)||(amigosUsuario>=10)) {
				int fuera = usuario.friends.size()-amigosUsuario;
				usuario.setAmigosEnSistema(amigosUsuario);
				if (consola) {
					System.out.println("Usuario "+usuario.screenName+" tiene "+fuera+" amigos fuera de nuestro sistema y "+amigosUsuario+" dentro.");
				}else {
					Ventana.tArea.append("Usuario "+usuario.screenName+" tiene "+fuera+" amigos fuera de nuestro sistema y "+amigosUsuario+" dentro.\n");
					Ventana.tArea.setCaretPosition(Ventana.tArea.getDocument().getLength());
					}
				
				usuariosConAmigosEnSistema.add(usuario);
				total++;
			}
		}
		if (consola) {
			System.out.println("Hay "+total+" con algunos amigos dentro de nuestro sistema.");
		}else {
			Ventana.tArea.append("Hay "+total+" con algunos amigos dentro de nuestro sistema.\n");
			Ventana.tArea.setCaretPosition(Ventana.tArea.getDocument().getLength());
			
		}
		
	}
	
	public static void main(String[] args) {
			consola = true;
		try {
			// TODO Configurar el path y ruta del fichero a cargar
			String fileName = "src/practica5_6/data.csv";
			CSV.processCSV( new File( fileName ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(usuariosPorID);
		if(CSV.repetidos(usuariosPorID)) {
			System.out.println("Hay usuarios repetidos");
		}else {
			System.out.println("No hay usuarios repetidos");
		}
		putUsuarioPorNick();
//		System.out.println(usuariosPorNick);
		if(CSV.repetidos(usuariosPorNick)) {
			System.out.println("Hay usuarios repetidos");
		}else {
			System.out.println("No hay usuarios repetidos");
		}
		amigosUsuarios();
		for(UsuarioTwitter u:usuariosConAmigosEnSistema) {
			System.out.println(u.screenName+" - "+u.amigosEnSistema+" amigos.");
		}
	}
	
}
