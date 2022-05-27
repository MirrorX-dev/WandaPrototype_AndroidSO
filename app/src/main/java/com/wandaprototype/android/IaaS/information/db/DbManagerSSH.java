package com.wandaprototype.android.IaaS.information.db;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wandaprototype.android.MainActivity;
import com.wandaprototype.android.PaaS.information.db.PartidoVersionsLab;
import com.wandaprototype.android.objects.Partido;
import com.wandaprototype.android.objects.PartidoVersions;

import org.apache.commons.dbutils.DbUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DbManagerSSH {

	/** Listado para Querys: */
	public static List<String> partido_query = new ArrayList<String>();
	
	private static String knownHostPublicKey = "158.101.98.158 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDTULimuMguOvnMBPqaCbD7me4622EHZUMteOhOcKmP/puHSGFFDbXegEizQ1nJRng3coxt7lk+VYQXtmxECDuvyLvOCPng47jWttOD5ppST6xkrTquUqTwBQmnIgRPQ322KFuuL5yzr6BlzzrzlhlGGX8gsCmqzfUXMo5Pof7nXhVKl4dMIczeLeCREv9r4PCMtzLRKAI/d0Of7i/Bhfs1IYMlpTRBq3SxUoZOgRMfpo/ONyOoTIBKTWJRFPj4mf/laRv73BgnlkQzlaRCXR6Ytb3qI7CCz6ktWh53t9w0kI5wDFQiJBSQSJD74WtTSzVEIk+vYqNDjPcir0H2Br5e3Z7hPkJa4hqPNvP4CQRz9ntua/LLY5ELZrZiys2blCd1P0Fs/ZK44XLEXjkAezZ12ymjx3v+UyY1cXhhTZ34+uq8nDsJNc4jfBhH6XrgEjkv2sZZBd/rA9estQ3A+IXvwMQu9dx/il3DveMvkd77Wu9HlwUZ0Idts0Bb6l0L1Jk=";
			//System.getenv("SSH_Connections_Public_Key");

	private static PreparedStatement ps = null;
	private static Statement st = null;
	private static ResultSet rs = null;
	private static Connection con;
	private static Session session;
	
	private static String databaseUsername = "dbpeople";
	private static String databasePassword = "helloworld2022";
	
	@RequiresApi(api = Build.VERSION_CODES.R)
	public static Connection conectar(String file) throws JSchException, SQLException, IOException, ClassNotFoundException {
		String jumpserverHost = "158.101.98.158";
		String jumpserverUsername = "opc";

		String databaseHost = "localhost";
		int databasePort = 3306;

		JSch jsch = new JSch();
		jsch.setKnownHosts(new ByteArrayInputStream(knownHostPublicKey.getBytes()));
		jsch.addIdentity(file);

		session = jsch.getSession(jumpserverUsername, jumpserverHost);
		session.connect();

		int forwardedPort = session.setPortForwardingL(0, databaseHost, databasePort);
		//Class.forName("com.mysql.cj.jdbc.Driver");
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:" + forwardedPort;
		con = DriverManager.getConnection(url, databaseUsername, databasePassword);
		return con;
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
   
    @RequiresApi(api = Build.VERSION_CODES.R)
	public boolean checkConnection(String file) throws JSchException, URISyntaxException {
        boolean exist = false;
        try {
        	conectar(file);
            st = con.createStatement();
            String sql = "select schema_name as database_name\r\n" + 
            		"from information_schema.schemata";
            st.setMaxRows(1);
            rs = st.executeQuery(sql);
            

            if (rs.next()) {
                exist = true;
                System.out.println("Conexi�n a la bbdd con �xito: "+exist);
            }
            
        } catch (SQLException | IOException | ClassNotFoundException ex) {
        	ex.printStackTrace();
            exist = false;       
            System.err.println("Error al conectar a la base de datos: "+exist+" Intentado con driver antiguo");
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

	/**
	 * Comprueba mediante ciertos parametros de entrada la validación de la ultima versión
	 * basada en la fecha más reciente (tanto servidor/cliente).
	 * El número de versión no es utilizado. Su uso es principalemente para la organización
	 * de los datos.
	 * @param mifecha Debes especificar tu ultima fecha de la bbdd interna.
	 * @param table Debes especificar tu tabla referida al estadio.
	 * @return
	 */
	public boolean ComprobarUltimaVersion(Timestamp mifecha, String table) {
		boolean output = false;
		try {
			int lastVersion;
			Timestamp lastDate = null;

			String query = "SELECT * FROM wandaprototype." + table + " WHERE version_number=(SELECT MAX(version_number) FROM wandaprototype." + table + ")";
			rs = st.executeQuery(query);
			while (rs.next()) {
				lastVersion = rs.getInt("version_number");
				lastDate = rs.getTimestamp("version_date");
				System.out.println(lastVersion);
				System.out.println(lastDate);
			}

			if (lastDate.after(mifecha)) {
				// System.out.println("La fecha del �ltimo registro es m�s reciente");
				output = true;
				// output por defecto = false. No es m�s reciente
			}

		} catch (Exception a) {
			a.printStackTrace();
		}

		return output;
	}


	public int getUltimaNVersion(Timestamp mifecha, String table) {
		int output = -1;
		try {
			int lastVersion = -1;
			Timestamp lastDate = null;

			String query = "SELECT * FROM wandaprototype." + table + " WHERE version_number=(SELECT MAX(version_number) FROM wandaprototype." + table + ")";
			rs = st.executeQuery(query);
			while (rs.next()) {
				lastVersion = rs.getInt("version_number");
				lastDate = rs.getTimestamp("version_date");
				System.out.println(lastVersion);
				System.out.println(lastDate);
			}
			if (lastDate.after(mifecha)) {
				output = lastVersion;
			}

		} catch (Exception a) {
			a.printStackTrace();
		}
		return output;
	}
	
	public void DefinirObjetoPartido(String table) {
		String query = "SELECT * FROM wandaprototype." + table +"";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {				
				Partido p = new Partido(
						rs.getString("idpartido"),
						rs.getString("competicion"),
						rs.getString("equipolocal"),
						rs.getString("equipovisit"),
						rs.getString("jornada"),
						rs.getString("fechapartido"),
						rs.getString("horapartido"),
						rs.getString("estadiopartido"));
				Partido.partidos.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DefinirObjetoPartidoVersiones(String table) {
		String query = "SELECT * FROM wandaprototype." + table +"";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				PartidoVersions p = new PartidoVersions(
						rs.getInt("version_number"),
						rs.getString("version_date"));
				PartidoVersions.partidosVersions.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		
		//new DbManagerSSH().checkConnection();
		//new DbManagerSSH().DefinirObjetoPartido("wandametropolitano_partidos");
   
		//new DbManagerSSH().ComprobarUltimaVersion(41, Timestamp.valueOf("2022-05-19 18:03:17.0"),"wandametropolitano_versions");
		//DbManagerSSH.CloseDataBaseConnection();
		//DbManagerSSH.CloseSSHConnection();
	}
}
