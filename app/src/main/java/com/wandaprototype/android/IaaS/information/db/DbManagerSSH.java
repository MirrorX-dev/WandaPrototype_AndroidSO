package com.wandaprototype.android.IaaS.information.db;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wandaprototype.android.objects.Partido;
import com.wandaprototype.android.objects.PartidoVersions;

import org.apache.commons.dbutils.DbUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    /**
     * Listado para Querys:
     */
    public static List<String> partido_query = new ArrayList<>();

    private static final String knownHostPublicKey = "158.101.98.158 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDTULimuMguOvnMBPqaCbD7me4622EHZUMteOhOcKmP/puHSGFFDbXegEizQ1nJRng3coxt7lk+VYQXtmxECDuvyLvOCPng47jWttOD5ppST6xkrTquUqTwBQmnIgRPQ322KFuuL5yzr6BlzzrzlhlGGX8gsCmqzfUXMo5Pof7nXhVKl4dMIczeLeCREv9r4PCMtzLRKAI/d0Of7i/Bhfs1IYMlpTRBq3SxUoZOgRMfpo/ONyOoTIBKTWJRFPj4mf/laRv73BgnlkQzlaRCXR6Ytb3qI7CCz6ktWh53t9w0kI5wDFQiJBSQSJD74WtTSzVEIk+vYqNDjPcir0H2Br5e3Z7hPkJa4hqPNvP4CQRz9ntua/LLY5ELZrZiys2blCd1P0Fs/ZK44XLEXjkAezZ12ymjx3v+UyY1cXhhTZ34+uq8nDsJNc4jfBhH6XrgEjkv2sZZBd/rA9estQ3A+IXvwMQu9dx/il3DveMvkd77Wu9HlwUZ0Idts0Bb6l0L1Jk=";
    //System.getenv("SSH_Connections_Public_Key");

    private static PreparedStatement ps = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    private static Connection con;
    private static Session session;

    private static final String databaseUsername = "dbpeople";
    private static final String databasePassword = "helloworld2022";

    /**
     * Construye un tunel de acceso seguro mediante SSH de tipo cifrado para
     * conectarse al servidor de forma segura.
     * Inicia sesión con un usuario guardado
     *
     * @return Devuelve Connnection, permite trabajar sobre la conexión y driver de la base de datos.
     * @throws JSchException error relacionados con conexiones mediante SSH tunel.
     * @throws SQLException  error relacionados con conexiones querys SQL.
     */
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

    /**
     * Cierra el hilo de conexión con la base de datos.
     */
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

    /**
     * Cierra el hilo de conexión con el tunel SSH hacia el servidor.
     */
    public static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            System.out.println("Closing SSH Connection");
            session.disconnect();
        }
    }

    // QUERYS HERE:

    /**
     * Comprueba la conexión.
     * Se genera una conexión tunel ssh hacia el servidor y se usa
     * el driver para realizar consultas SQL.
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public boolean checkConnection(String file) throws JSchException {
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
                System.out.println("Conexi�n a la bbdd con �xito: " + exist);
            }

        } catch (SQLException | IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            exist = false;
            System.err.println("Error al conectar a la base de datos: " + exist + " Intentado con driver antiguo");
        }

        return exist;
    }

    /**
     * Obtiene mediante una consulta reutilizable para cualquier estadio
     * devuelve un estado de tipo booleano.
     * Si existe el identificador, devuelve el valor adjunto.
     *
     * @param table     tabla donde operaremos las querys
     * @param idpartido identificador del partido deseado.
     * @return true/false si existe.
     */
    public boolean getDataPerID(String table, String idpartido) {
        boolean output = false;
        try {
            try {
                String query = "select idpartido from wandaprototype." + table + " where idpartido = ?";
                ps = con.prepareStatement(query);
                ps.setString(1, idpartido);
                ps.setMaxRows(1);
                rs = ps.executeQuery();

                //The result of the select was Empty or With Data equivalent to return false or true.
                if (!rs.isBeforeFirst()) {
                    //System.out.println("empty");
                    output = false;
                } else {
                    //System.out.println("with data");
                    output = true;
                }

            } catch (Exception a) {
                a.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;

    }

    /**
     * Devuelve un listado de un solo partido mas cercano
     * frente a la fecha del evento.
     *
     * @return listado partidos
     */
    public List<String> ObtenerPartidoMasCercano() {
        try {
            partido_query.clear();
            try {
                st = con.createStatement();
                st.setMaxRows(1);
                String query = "select competicion, equipolocal, equipovisit, jornada, fechapartido, horapartido, estadiopartido from wandaprototype.wandametropolitano_partidos where fechapartido>CURDATE() and estadiopartido = 'Wanda Metropolitano'  ORDER BY fechapartido LIMIT 2;";
                rs = st.executeQuery(query);

                while (rs.next()) {
                    partido_query.add(rs.getString("competicion"));
                    partido_query.add(rs.getString("equipolocal"));
                    partido_query.add(rs.getString("equipovisit"));
                    partido_query.add(rs.getString("fechapartido"));
                    partido_query.add(rs.getString("horapartido"));
                }
            } catch (Exception e) {
                System.err.println("Got an exception!");
                System.err.println(e.getMessage());
            }
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
     *
     * @param mifecha Debes especificar tu ultima fecha de la bbdd interna.
     * @param table   Debes especificar tu tabla referida al estadio.
     * @return boolean if exist.
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

            assert lastDate != null;
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

    /**
     * Obtiene la ultima versión del servidor.
     * En el caso de obtener una versión inexistente se toma el valor
     * -1 como valor de error controlado.
     *
     * @param mifecha fecha actual indicada
     * @param table   tabla donde operaremos con operaciones querys.
     * @return nº de versión.
     */
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
            assert lastDate != null;
            if (lastDate.after(mifecha)) {
                output = lastVersion;
            }

        } catch (Exception a) {
            a.printStackTrace();
        }
        return output;
    }

    /**
     * Obtiene los elementos de la consulta de partidos de la BD
     * y los convierte a objetos.
     *
     * @param table tabla donde operaremos con operaciones querys.
     */
    public void DefinirObjetoPartido(String table) {
        String query = "SELECT * FROM wandaprototype." + table + "";
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

    /**
     * Obtiene los elementos de la consulta de partidosVersiones de la BD
     * y los convierte a objetos.
     *
     * @param table tabla donde operaremos con operaciones querys.
     */
    public void DefinirObjetoPartidoVersiones(String table) {
        String query = "SELECT * FROM wandaprototype." + table + "";
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

    public static void main(String[] args) {
    }
}
