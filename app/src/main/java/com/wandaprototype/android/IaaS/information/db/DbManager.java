package com.wandaprototype.android.IaaS.information.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

public class DbManager {
	
	String global_driver = "com.mysql.cj.jdbc.Driver", global_driver_old = "com.mysql.jdbc.Driver", 
			//global_db = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11485175?useSSL=true";
			global_db="jdbc:mysql://192.168.1.10:3306/wandaprototype";
	String authorized_user = "MatchsScrambler", auth_password = "wandaprototype0";
	//String authorized_user = "sql11485175", auth_password = "LvS831F1chHXgebH";

	Connection conn = null;
	PreparedStatement ps = null;
	Statement st = null;
	ResultSet rs = null;
	// 	Se realizan un total de 204 conexiones secuenciales.
	//	El n�mero de conexiones concurrentes est� limitado a 1.
	//	204/3 68. 
	//	-	Conexiones de GET datos == id existente?. 34
	//	-	Conexiones de POST datos. 34
	//	-	Duplicaci�n de confirmaci�n de conexi�n a bbdd en todos los m�todos. 34
	//	Conexiones �tiles: 102.
	//	Este programa ejecuta el servicio cada 1 d�a.
	
	public static List<String> partido_query=new ArrayList<String>();  

	
	public boolean checkConnection() {
        boolean exist = false;
        try {
			Class.forName(global_driver);
			conn = DriverManager.getConnection(global_db, authorized_user, auth_password);
            st = conn.createStatement();
            String sql = "select schema_name as database_name\r\n" + 
            		"from information_schema.schemata";
            st.setMaxRows(1);
            rs = st.executeQuery(sql);
            

            if (rs.next()) {
                exist = true;
                System.out.println("Conexi�n a la bbdd con �xito: "+exist);
            }
            
        } catch (ClassNotFoundException ex) {

        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(main, "La base de datos no existe.");
        	ex.printStackTrace();
            exist = false;
            global_driver = global_driver_old;
            
            System.err.println("Error al conectar a la base de datos: "+exist+" Intentado con driver antiguo");
            
            
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conn);
        }
        
        return exist;
    }
	
	public void postData(String idpartido, String compe, String elocal, String evisit, String jor, String fechapa, String hora, String estad) {	
		try {
			if (checkConnection()!=false)  {
				try {
					Class.forName(global_driver);
					Connection conn = DriverManager.getConnection(global_db, authorized_user, auth_password);
					Statement st = conn.createStatement();
					st.setMaxRows(1);
					st.execute("INSERT INTO `wandaprototype`.`partido` (`idpartido`, `competicion`, `equipolocal`, `equipovisit`, `jornada`, `fechapartido`, `horapartido`, `estadiopartido`) VALUES ('"+idpartido+"','"+compe+"', '"+elocal+"', '"+evisit+"', '"+jor+"','"+fechapa+"','"+hora+"','"+estad+"')");
					DbUtils.closeQuietly(rs);
		            DbUtils.closeQuietly(ps);
		            DbUtils.closeQuietly(st);
		            DbUtils.closeQuietly(conn);
		            //Cerramos la conexi�n realizada debido a las multiples consultas.
		            //que se realizan y no son cerradas.
					
				} catch (Exception e) {
					System.err.println("Got an exception!");
					System.err.println(e.getMessage());
				} finally {
		            DbUtils.closeQuietly(rs);
		            DbUtils.closeQuietly(ps);
		            DbUtils.closeQuietly(st);
		            DbUtils.closeQuietly(conn);
		        }
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getDataPerID(String idpartido) {
		boolean output=false;
		
		try {
			if (checkConnection()!=false)  {
				try {
					Class.forName(global_driver);
					conn = DriverManager.getConnection(global_db, authorized_user, auth_password);
					String query = "select idpartido from partido where idpartido = ?";
					ps = conn.prepareStatement(query);
					ps.setString(1, idpartido);
					ps.setMaxRows(1);
					rs = ps.executeQuery();
					
					//The result of the select was Empty or With Data equivalent to return false or true.
					if (!rs.isBeforeFirst() ) {    
				          //System.out.println("empty"); 
				          output = false;
				    } else {
				          //System.out.println("with data"); 
				          output = true;
				    }
						
				} catch (Exception a) {
					a.printStackTrace();
				} finally {
		            DbUtils.closeQuietly(rs);
		            DbUtils.closeQuietly(ps);
		            DbUtils.closeQuietly(st);
		            DbUtils.closeQuietly(conn);
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return output;

	}
	
	
	public  List<String> ObtenerPartidoMasCercano() {
		try {
			partido_query.clear();
			if (checkConnection()!=false)  {
				try {
					Class.forName(global_driver);
					conn = DriverManager.getConnection(global_db, authorized_user, auth_password);						
					st = conn.createStatement();
					st.setMaxRows(1);
					String query="select competicion, equipolocal, equipovisit, jornada, fechapartido, horapartido, estadiopartido from partido where fechapartido>CURDATE() and estadiopartido = 'Wanda Metropolitano'  ORDER BY fechapartido LIMIT 1;";
					rs = st.executeQuery(query);
					
		            while (rs.next())
		            {
		                partido_query.add(rs.getString("competicion"));
		                partido_query.add(rs.getString("equipolocal"));
		                partido_query.add(rs.getString("equipovisit"));
		                partido_query.add(rs.getString("fechapartido"));
		                partido_query.add(rs.getString("horapartido"));
		            }
		            //Cerramos la conexi�n realizada debido a las multiples consultas.
		            //que se realizan y no son cerradas.
		            DbUtils.closeQuietly(rs);
		            DbUtils.closeQuietly(ps);
		            DbUtils.closeQuietly(st);
		            DbUtils.closeQuietly(conn);
		            
				} catch (Exception e) {
					System.err.println("Got an exception!");
					System.err.println(e.getMessage());
				} finally {
		            DbUtils.closeQuietly(rs);
		            DbUtils.closeQuietly(ps);
		            DbUtils.closeQuietly(st);
		            DbUtils.closeQuietly(conn);
		        }
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return partido_query;
	}
	
	
	// 	Crear funci�n que mantenga la revisi�n de la conexi�n al servidor.
	//	hasta que este est� operativo. Una vez est� operativo realice
	//	funci�n de actualizaci�n de los datos.
	//	Loop hasta que la conexi�n se reestablezca.
	//	-- Timeout. callback.

	
	public static void main(String[] args) {
		//new PostPartido().postData("9b67a35e-ab46-386e-a6f6-6e1040ab5ba8", "Primera Competici�n", "Primera Jornada", "2020-10-01", "10:00", "San Ejemplo");
		//new DbManager().getDataPerID("0196c2fd-e8b2-3831-9f8d-c9d9d13109e00");
		new DbManager().checkConnection();
		//System.out.println(partido_query);
		//new DbManager().ObtenerPartidoMasCercano();
		//System.out.println(partido_query);
	}
}
