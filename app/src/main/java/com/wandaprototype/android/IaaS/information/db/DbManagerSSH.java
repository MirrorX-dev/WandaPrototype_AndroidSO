package com.wandaprototype.android.IaaS.information.db;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class DbManagerSSH {

	public static List<String> partido_query = new ArrayList<String>();
	private static String knownHostPublicKey = "158.101.98.158 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDTULimuMguOvnMBPqaCbD7me4622EHZUMteOhOcKmP/puHSGFFDbXegEizQ1nJRng3coxt7lk+VYQXtmxECDuvyLvOCPng47jWttOD5ppST6xkrTquUqTwBQmnIgRPQ322KFuuL5yzr6BlzzrzlhlGGX8gsCmqzfUXMo5Pof7nXhVKl4dMIczeLeCREv9r4PCMtzLRKAI/d0Of7i/Bhfs1IYMlpTRBq3SxUoZOgRMfpo/ONyOoTIBKTWJRFPj4mf/laRv73BgnlkQzlaRCXR6Ytb3qI7CCz6ktWh53t9w0kI5wDFQiJBSQSJD74WtTSzVEIk+vYqNDjPcir0H2Br5e3Z7hPkJa4hqPNvP4CQRz9ntua/LLY5ELZrZiys2blCd1P0Fs/ZK44XLEXjkAezZ12ymjx3v+UyY1cXhhTZ34+uq8nDsJNc4jfBhH6XrgEjkv2sZZBd/rA9estQ3A+IXvwMQu9dx/il3DveMvkd77Wu9HlwUZ0Idts0Bb6l0L1Jk=";
	
	private static PreparedStatement ps = null;
	private static Statement st = null;
	private static ResultSet rs = null;
	private static Connection con;
	private static Session session;
	
	private static String databaseUsername = "dbpeople";
	private static String databasePassword = "helloworld2022";
	
	public static void conectar() throws JSchException, SQLException {
		String jumpserverHost = "158.101.98.158";
		String jumpserverUsername = "opc";

		String databaseHost = "localhost";
		int databasePort = 3306;

		JSch jsch = new JSch();
		jsch.setKnownHosts(new ByteArrayInputStream(knownHostPublicKey.getBytes()));
		jsch.addIdentity("C:\\Users\\MirrorX\\Documents\\Oracle Cloud\\ssh-key-2022-05-13.key");

		session = jsch.getSession(jumpserverUsername, jumpserverHost);
		session.connect();

		int forwardedPort = session.setPortForwardingL(0, databaseHost, databasePort);

		String url = "jdbc:mysql://localhost:" + forwardedPort;
		con = DriverManager.getConnection(url, databaseUsername, databasePassword);
	}

	
	public static void CloseDataBaseConnection() {
        try {
            if (con != null && !con.isClosed()) {
                System.out.println("Closing Database Connection");
                DbUtils.closeQuietly(rs);
                DbUtils.closeQuietly(ps);
                DbUtils.closeQuietly(st);
                DbUtils.closeQuietly(con);
                //con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            System.out.println("Closing SSH Connection");
            session.disconnect();
        }
    }
    
   // QUERYS HERE:
   
    public boolean checkConnection() throws JSchException {
        boolean exist = false;
        try {
        	conectar();
            st = con.createStatement();
            String sql = "select schema_name as database_name\r\n" + 
            		"from information_schema.schemata";
            st.setMaxRows(1);
            rs = st.executeQuery(sql);
            

            if (rs.next()) {
                exist = true;
                System.out.println("Conexi�n a la bbdd con �xito: "+exist);
            }
            
        } catch (SQLException ex) {
        	ex.printStackTrace();
            exist = false;       
            System.err.println("Error al conectar a la base de datos: "+exist+" Intentado con driver antiguo");
            checkConnection();           
        } finally {
            //DbUtils.closeQuietly(rs);
            //DbUtils.closeQuietly(ps);
            //DbUtils.closeQuietly(st);
            //DbUtils.closeQuietly(con);
            //DbManagerSSH.CloseDataBaseConnection();
    		//DbManagerSSH.CloseSSHConnection();
        }
        
        return exist;
    }
    
    public void postData(String table, String idpartido, String compe, String elocal, String evisit, String jor, String fechapa, String hora, String estad) {	
		try {
			//Comprueba la conectividad con la bbdd. #1 consulta.
			//if (checkConnection()!=false)  {
				try {
					Statement st = con.createStatement();
					st.setMaxRows(1);
					st.execute("INSERT INTO `wandaprototype`.`"+table+"` (`idpartido`, `competicion`, `equipolocal`, `equipovisit`, `jornada`, `fechapartido`, `horapartido`, `estadiopartido`) VALUES ('"+idpartido+"','"+compe+"', '"+elocal+"', '"+evisit+"', '"+jor+"','"+fechapa+"','"+hora+"','"+estad+"')");
					/*
					DbUtils.closeQuietly(rs);
		            DbUtils.closeQuietly(ps);
		            DbUtils.closeQuietly(st);
		            DbUtils.closeQuietly(con);
		            */
		            //Cerramos la conexi�n realizada debido a las multiples consultas.
		            //que se realizan y no son cerradas.
					
				} catch (Exception e) {
					System.err.println("Got an exception!");
					System.err.println(e.getMessage());
				}
			//}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            //DbUtils.closeQuietly(rs);
            //DbUtils.closeQuietly(ps);
            //DbUtils.closeQuietly(st);
            //DbUtils.closeQuietly(con);
            //DbManagerSSH.CloseDataBaseConnection();
    		//DbManagerSSH.CloseSSHConnection();
        }
	}
    
    public boolean getDataPerID(String table, String idpartido) {
		boolean output=false;			
			try {
				//Comprueba la conectividad con la bbdd. #1 consulta.
				//if (checkConnection()!=false)  {
					try {
						String query = "select idpartido from wandaprototype."+table+" where idpartido = ?";
						ps = con.prepareStatement(query);
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
					}
				//}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
	            //DbUtils.closeQuietly(rs);
	            //DbUtils.closeQuietly(ps);
	            //DbUtils.closeQuietly(st);
	            //DbUtils.closeQuietly(con);
	            //DbManagerSSH.CloseDataBaseConnection();
	    		//DbManagerSSH.CloseSSHConnection();
	        }
		
		
		return output;

	}

    public  List<String> ObtenerPartidoMasCercano() {
		try {
			partido_query.clear();
			//if (checkConnection()!=false)  {
				try {				
					st = con.createStatement();
					st.setMaxRows(1);
					String query="select competicion, equipolocal, equipovisit, jornada, fechapartido, horapartido, estadiopartido from wandaprototype.wandametropolitano_partidos where fechapartido>CURDATE() and estadiopartido = 'Wanda Metropolitano'  ORDER BY fechapartido LIMIT 2;";
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
		            /*
		            DbUtils.closeQuietly(rs);
		            DbUtils.closeQuietly(ps);
		            DbUtils.closeQuietly(st);
		            DbUtils.closeQuietly(con);
		            */
		            
				} catch (Exception e) {
					System.err.println("Got an exception!");
					System.err.println(e.getMessage());
				} finally {
		           
		        }
			//}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return partido_query;
	}
    
	public static void main(String[] args) throws JSchException, SQLException {
		//new DbManagerSSH().checkConnection();
		//new DbManagerSSH().getDataPerID("wandametropolitano_partidos", "8903u49708j942f9oipj3f");
		//new DbManagerSSH().postData("wandametropolitano_partidos", "8903u49708j942f9oipj3f", "jornada 777", "er betis", "er real madrid", "jornada puertas", "2022/10/10", "10:30:00", "santiagomanuel");
		//new DbManagerSSH().getDataPerID("wandametropolitano_partidos", "h39hgvn308vn393nvi");
		//new DbManagerSSH().postData("wandametropolitano_partidos", "h39hgvn308vn393nvi", "jornada 777", "er betis", "er real madrid", "jornada puertas", "2022/10/10", "10:30:00", "santiagomanuel");
		//new DbManagerSSH().checkConnection();
		//new DbManagerSSH().ObtenerPartidoMasCercano();
		//System.out.println(partido_query);
		//DbManagerSSH.CloseDataBaseConnection();
		//DbManagerSSH.CloseSSHConnection();
	}
}
