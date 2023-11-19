package practica5_6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Ventana extends JFrame {
	protected static JTextArea tArea = new JTextArea();
	protected JScrollPane sPane;
	protected JFileChooser fChooser = new JFileChooser();
	private static final String PROP_FILE = "config.properties";
    private static final String LAST_FILE_PROP = "lastFile";
    protected static JInternalFrame fInterno = new JInternalFrame();
    protected static JProgressBar pBar = new JProgressBar();
    protected static JLabel nombreProceso = new JLabel(" ");
    protected static JScrollPane sPaneTabla;
    protected static DefaultTableModel modelo;
    protected static JTable tabla;
    protected static JTextField tFieldEtiqueta = new JTextField();
    protected static JTree tree = new JTree();
    protected static JScrollPane sPaneTree = new JScrollPane();
    protected static int filaEnTabla = -1;
	public Ventana(){
		GestionTwitter.consola =false;
		tArea.setEditable(false);
		sPane= new JScrollPane(tArea);
		setSize(1200,800);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		add(sPane, BorderLayout.CENTER);
		fInterno.add(nombreProceso, BorderLayout.NORTH);
		fInterno.add(pBar, BorderLayout.CENTER);
		add(fInterno,BorderLayout.NORTH);
		fInterno.setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		
		
		Properties prop = new Properties();
        File lastFile = null;
        
        try (InputStream input = new FileInputStream(PROP_FILE)) {
            prop.load(input);
            String lastFilePath = prop.getProperty(LAST_FILE_PROP);
            if (lastFilePath != null) {
                lastFile = new File(lastFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		if (lastFile != null) {
	           fChooser.setCurrentDirectory(lastFile);
		}
		
		int respuesta = fChooser.showOpenDialog(null);
		while (respuesta != JFileChooser.APPROVE_OPTION) {
			respuesta = fChooser.showOpenDialog(null);
			}
		if (respuesta == JFileChooser.APPROVE_OPTION) {
			prop.setProperty(LAST_FILE_PROP, fChooser.getSelectedFile().getPath());
			try (OutputStream output = new FileOutputStream(PROP_FILE)) {
                prop.store(output, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
			procesoPractica(fChooser.getSelectedFile().getPath());
		}
		
		add(tFieldEtiqueta, BorderLayout.NORTH);
		tFieldEtiqueta.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				tabla.repaint();
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				tabla.repaint();
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});;
		setTabla();
		add(sPaneTabla, BorderLayout.EAST);
		revalidate();
		
		
		
	}
	
	public static void procesoPractica(String fileName) {
		try {
			CSV.processCSV( new File( fileName ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
//		tArea.append(GestionTwitter.usuariosPorID+"\n");
//		tArea.setCaretPosition(tArea.getDocument().getLength());
		if(CSV.repetidos(GestionTwitter.usuariosPorID)) {
			tArea.append("Hay usuarios repetidos\n");
			tArea.setCaretPosition(tArea.getDocument().getLength());
		}else {
			tArea.append("No hay usuarios repetidos\n");
			tArea.setCaretPosition(tArea.getDocument().getLength());
		}
		GestionTwitter.putUsuarioPorNick();
//		tArea.append(GestionTwitter.usuariosPorNick+"\n");
//		tArea.setCaretPosition(tArea.getDocument().getLength());
		if(CSV.repetidos(GestionTwitter.usuariosPorNick)) {
			tArea.append("Hay usuarios repetidos\n");
			tArea.setCaretPosition(tArea.getDocument().getLength());
		}else {
			tArea.append("No hay usuarios repetidos\n");
			tArea.setCaretPosition(tArea.getDocument().getLength());
		}
		GestionTwitter.amigosUsuarios();
		nombreProceso.setText("Creando TreeSet...");
		int i = 0;
		for(UsuarioTwitter u:GestionTwitter.usuariosConAmigosEnSistema) {
			i++;
			CSV.progress = (int)(((double)i/((double)GestionTwitter.usuariosConAmigosEnSistema.size()))*100);
			pBar.setValue(CSV.progress);
			tArea.append(u.screenName+" - "+u.amigosEnSistema+" amigos.\n");
			tArea.setCaretPosition(tArea.getDocument().getLength());
		}
		fInterno.setVisible(false);
	}
	
	protected void setTabla() {
		final String[] cabeceras = {"id", "screeName", "followersCount", "friendsCount", "lang", "lastSeen" };
		modelo = new DefaultTableModel(cabeceras, 0){
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        // Todas las celdas son no editables
		        return false;
		    }
		};
		for (UsuarioTwitter usuario:GestionTwitter.usuariosConAmigosEnSistema) {
			//Quiero añadir todos los usuarios a la tabla
			modelo.addRow(new Object[] {usuario.id, usuario.screenName, usuario.followersCount, usuario.friendsCount, usuario.lang, usuario.lastSeen});
		}
		tabla = new JTable(modelo);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column );
				if (GestionTwitter.usuariosPorID.get((String)modelo.getValueAt(row, 0)).getTags().contains(tFieldEtiqueta.getText())) {
					c.setBackground(Color.GREEN);
				}else {
					c.setBackground(Color.WHITE);
				}
				return c;
			}
		});
		tabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filaEnTabla = tabla.rowAtPoint( e.getPoint() );
				if(filaEnTabla!=-1) {
					setTree();
				}
			}
		});
		sPaneTabla = new JScrollPane(tabla);
	}
	protected void setTree() {
		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(tabla.getValueAt(filaEnTabla, 1));
		DefaultTreeModel modelo = new DefaultTreeModel(raiz);
		tree.setModel(modelo);
		UsuarioTwitter usuarioRaiz = GestionTwitter.usuariosPorNick.get(tabla.getValueAt(filaEnTabla, 1));
		for(String idUsuario : usuarioRaiz.friends) {
			//
		}
		tree.addTreeSelectionListener(new TreeSelectionListener() {
		
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				
			}
		});
		
	}
	
	public static void main(String[] args) {
		new Ventana();
	}
}

