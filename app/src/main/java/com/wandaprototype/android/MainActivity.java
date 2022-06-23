package com.wandaprototype.android;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;
import com.wandaprototype.R;
import com.wandaprototype.android.IaaS.information.db.DbManagerSSH;
import com.wandaprototype.android.PaaS.information.db.PartidoLab;
import com.wandaprototype.android.PaaS.information.db.PartidoVersionsLab;
import com.wandaprototype.android.dataformat.DameFecha;
import com.wandaprototype.android.objects.Partido;
import com.wandaprototype.android.objects.PartidoVersions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Pantalla inicial de datos. Puede cambiar en las
 * proximas versiones y pasar a ser refactorizada por
 * un nombre m&aacute;s adecuado.
 * Su funci&oacute;n es mostrar al usuario al entrar a la
 * aplicaci&oacute;n la informaci&oacute;n de forma directa.
 * TODO: Nuevas vistas.
 */
public class MainActivity extends AppCompatActivity {
    //Fields
    private TextView jornadaTextView;
    private TextView equipolocalTextView;
    private TextView vs_separadorTextView;
    private TextView equipovisitanteTextView;
    private TextView fechaTextView;
    private TextView hora_separador_TextView;
    private TextView horaTextView;
    private TextView versionTextView;

    Context context = this;

    //ListView Adapter UI
    private ArrayAdapter<String> adapter;

    //Room
    //Tabla Partido
    private PartidoLab mPartidoLab;
    private Partido mPartido;

    //Tabla PartidoVersions
    private PartidoVersionsLab mPartidoVersionsLab;
    private PartidoVersions mPartidoVersions;

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
          Configuración ToolBar.
          Toolbar personalizado y estilizado.
         */
        setSupportActionBar(findViewById(R.id.myToolbar));

        /*
          Instancia de Tablas correspondientes a usar
          en esta actividad de Android:
          Partidos ==> Si cambia de estadio se utiliza el mismo.
          PartidosVersiones ==> Misma acción.
         */
        mPartidoLab = PartidoLab.get(MainActivity.this);
        mPartidoVersionsLab = PartidoVersionsLab.get(MainActivity.this);

        /*
          Instancia una lista de elementos, permite añadir a la lista información
          de los eventos que tengamos disponibles.
          Aplica cambios en la lista notificando que los datos han cambiando al adaptador.
         */
        ListView listView = findViewById(R.id.listv);
        String[] items = {};
        //Arrays : Elementos listados
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(items));
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.txtitem, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*
          Instancia y carga de MapBox, sustituye actualmente a GoogleMaps.
          Permite establecer configuraciones y cambiar estilo de mapa.
          TODO: Cambiar mapa a modo oscuro cuando se active la opción en el SO.
         */
        MapView mapView = findViewById(R.id.mapView);


        /*
          Instancia CalendarView, permite manejar eventos tras realizar ciertas acciones
          en la aplicación e interactuando con el mismo.
         */
        final CollapsibleCalendar collapsibleCalendar = findViewById(R.id.calendarView);
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {

            @Override
            public void onItemClick(@NonNull View view) {
            }

            @Override
            public void onDayChanged() {
            }

            @Override
            public void onClickListener() {
            }

            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                assert day != null;
                Log.i(getClass().getName(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());
            }

            public void onDataUpdate() {
            }

            public void onMonthChange() {
            }

            public void onWeekChange(int i) {
            }
        });

        /**
         * Establece configuraciones de componentes si modos de
         * "Modo nocturno" DarkMode está activado o no.
         * @author. Android stackoverflow
         */
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active on device
                mapView.getMapboxMap().loadStyleUri(Style.TRAFFIC_DAY);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active on device
                mapView.getMapboxMap().loadStyleUri(Style.TRAFFIC_NIGHT);
                break;
        }

    }


    /**
     * Ciclos de vida del módulo de mapa: MapBox.
     * Permite la llamada a funciones o acciones
     * en los ciclos correspondientes.
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Se inicia el uso del flujo principal de la aplicación.
     * Especifica y relaciona los Textviews con las variables
     * asociadas.
     */
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        jornadaTextView = findViewById(R.id.jornada);
        equipolocalTextView = findViewById(R.id.equipoLocal);
        vs_separadorTextView = findViewById(R.id.vs_separador);
        equipovisitanteTextView = findViewById(R.id.equipoVisitante);
        fechaTextView = findViewById(R.id.fecha);
        hora_separador_TextView = findViewById(R.id.hora_separador);
        horaTextView = findViewById(R.id.hora);
        FlujoPrincipal();

    }

    /**
     * Podemos utilizar onStop para finalizar todas las comunicaciones
     * o eliminar archivos en memoria caché.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * Utilizamos onResume para cargar de nuevos los datos, realizaremos el procedimiento inicial
     * para comprobar actualizaciones de los datos respetando si existe una conexión/ entre otros
     * bloques para su funcionamiento
     * TODO: Realizar funciones con onRestart
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Recopila información de servidor mediante un tunel seguro SSH.
     * Realiza las consultas correspondientes.
     * ¡Atención, no finaliza la conexión ni el tunel SSH!
     */
    private void RetrieveDatafromServer() {
        AsyncTask.execute(() -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                    /*
                      Recopila el archivo /ssh.key de la carpeta assets paquete interno:
                      TODO: Debe encriptarlo internamente.
                      Si no existe lo crea y gurada en cache:
                     */
                    String path;
                    File f = new File(getCacheDir() + "/ssh.key");
                    if (!f.exists()) {
                        try {
                            InputStream is = getAssets().open("ssh.key");
                            int size = is.available();
                            byte[] buffer = new byte[size];
                            is.read(buffer);
                            is.close();

                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(buffer);
                            //path = f.getPath();
                            fos.close();
                        } catch (Exception e) {
                            Log.e("WandaPrototype [ERROR]", "No se puede leer el fichero/permisos/localización");
                            throw new RuntimeException(e);
                        }
                    }

                    path = f.getPath();
                    Log.i("WandaPrototype [INFO]", "Ruta de información de la clave privada: " + path);
                    try {
                        new DbManagerSSH().checkConnection(path);
                        //Toast.makeText(context, "Comprobando ultima versión", Toast.LENGTH_SHORT).show();
                        /*
                          Añadimos punto de control:
                          Comprobamos si mi versión local contiene la ultima versión
                          de partidosVersions interna y externa.
                          Si no contiene ninguna versión procedemos a obtener los datos.

                          TODO: Debe borrar calendario para evitar confusiones.!!!!
                         */
                        mPartidoVersionsLab = PartidoVersionsLab.get(MainActivity.this);
                        List<PartidoVersions> partidosVersions = mPartidoVersionsLab.getPartidoVersionDao();
                        if (
                                PartidoVersions.partidosVersions.size() <= 0 ||
                                        new DbManagerSSH().ComprobarUltimaVersion(
                                                Timestamp.valueOf(partidosVersions.get(partidosVersions.size() - 1).version_date), "wandametropolitano_versions")
                        ) {
                            //Toast.makeText(context, "Actualizando información", Toast.LENGTH_SHORT).show();
                            mPartidoLab.setPartidosNuke();
                            new DbManagerSSH().DefinirObjetoPartido("wandametropolitano_partidos");
                            //TODO: Comprobar porque al recoger la versión más reciente con valor nulo, no crashea la App.
                            //TODO: Limpia versiones si tabla de la bbdd externa ha sido formateada.
                            if (partidosVersions.size() <= 0 || partidosVersions.get(partidosVersions.size() - 1).version_number >
                                    new DbManagerSSH().getUltimaNVersion(
                                            Timestamp.valueOf(partidosVersions.get(partidosVersions.size() - 1).version_date), "wandametropolitano_versions")
                            ) {
                                mPartidoVersionsLab.setPartidoVersionsNuke();
                            }

                            new DbManagerSSH().DefinirObjetoPartidoVersiones("wandametropolitano_versions");

                            /*
                              Transfiere los datos del ArrayList de Partidos a objeto DAO de partidos.
                              En Pruebas.
                             */
                            mPartidoLab = PartidoLab.get(MainActivity.this);
                            for (int i = 0; i < Partido.partidos.size(); i++) {
                                System.out.println(Partido.partidos.get(i));
                                if (mPartidoLab.isDataExist(Partido.partidos.get(i).id_partido) == 0) {
                                    mPartido = new Partido();
                                    mPartido.setId_partido(Partido.partidos.get(i).id_partido);
                                    mPartido.setCompeticion(Partido.partidos.get(i).competicion);
                                    mPartido.setEquipolocal(Partido.partidos.get(i).equipolocal);
                                    mPartido.setEquipovisitante(Partido.partidos.get(i).equipovisitante);
                                    mPartido.setJornada(Partido.partidos.get(i).jornada);
                                    mPartido.setFechapartido(Partido.partidos.get(i).fechapartido);
                                    mPartido.setHorapartido(Partido.partidos.get(i).horapartido);
                                    mPartido.setEstadiopartido(Partido.partidos.get(i).estadiopartido);
                                    mPartidoLab.addPartido(mPartido);
                                }
                            }

                            /*
                              Transfiere los datos del ArrayList de Partidos a objeto DAO de partidos.
                              En Pruebas.
                             */
                            for (int i = 0; i < PartidoVersions.partidosVersions.size(); i++) {
                                mPartidoVersions = new PartidoVersions();
                                mPartidoVersions.setVersion_number(PartidoVersions.partidosVersions.get(i).version_number);
                                mPartidoVersions.setVersion_date(PartidoVersions.partidosVersions.get(i).version_date);
                                mPartidoVersionsLab.addPartidoVersions(mPartidoVersions);
                            }

                            /*
                              Limpia y elimina lo lista y datos en el calendario
                             */
                            ReloadListView();

                            /*
                              Una vez finalice las tareas anteriores es necesario
                              llamar a las funciones para actualizar los datos
                              antes de finalizar el hilo de fondo.
                             */
                            RetrieveUIData();
                            //MostrarUltimaVersion();

                        } else {
                            Log.i("WandaPrototype [INFO]", "No se han encontrado versiones nuevas");
                            Toast.makeText(context, "Contiene la ultima versión", Toast.LENGTH_SHORT).show();
                            Log.i("WandaPrototype [INFO]", String.valueOf(partidosVersions));
                            System.out.println("Información Extra: " + partidosVersions.get(partidosVersions.size() - 1).version_number);

                            ReloadListView();
                            RetrieveUIData();
                        }
                    } catch (Exception e) {
                        Log.e("WandaPrototype [ERROR]", "Conexión/Fichero pueden estar fallando,,,");
                        e.printStackTrace();
                    }

                        /*
                          Cierra todas las conexiones. En caso de restaurar la App. No permite
                          reutilizar el mismo canal SSH.
                          TODO: Mantener SSH activo hasta finalizar App.
                         */
                    DbManagerSSH.CloseDataBaseConnection();
                    DbManagerSSH.CloseSSHConnection();

                    //} else {
                    //Log.e("WandaPrototype [ERROR]", "No existe el archivo .key");
                    //Log.e("WandaPrototype [ERROR]", String.valueOf("Ruta de información de la clave privada: "+path));
                    //throw new Exception("El archivo de acceso .key no existe");
                    //}
                } else {
                    Log.e("WandaPrototype [ERROR]", "Build.VERSION.SDK Es menor que el sistema");
                    throw new Exception("La versión SDK está desactualizada");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("WandaPrototype [ERROR]", "Error durante el procesado de datos");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void RetrieveUIData() {
        runOnUiThread(() -> {
            //TODO: Observador en el cambio de la información.
            //TODO: https://medium.com/mindorks/using-room-database-with-livedata-android-jetpack-cbf89b677b47
            //TODO: https://developer.android.com/topic/libraries/architecture/livedata?hl=es-419

            //Si no se estancian al comenzar la actividad. Devuelve valor nulo.
            mPartidoLab = PartidoLab.get(MainActivity.this);
            mPartidoVersionsLab = PartidoVersionsLab.get(MainActivity.this);
            if (!(mPartidoLab.getMoreRecentPartidos_limit6().isEmpty())) {
                MostrarDatosRecientes();
                MostrarDatosLista();
                MostrarDatosCalendario();
                System.out.println(mPartidoVersionsLab.getPartidoVersionDao());
                System.out.println(adapter);
            } else {
                jornadaTextView.setText("");
                equipolocalTextView.setText(context.getResources().getString(R.string.error_messages_002));
                vs_separadorTextView.setText("");
                equipovisitanteTextView.setText("");
                fechaTextView.setText("");
                hora_separador_TextView.setText("");
                horaTextView.setText("");
            }
        });

    }


    /**
     * Flujo caso 1: Comprueba si tienes conexión.
     * -> ¿Tienes datos locales?
     * - Recopila los datos nuevos, Muestra los nuevos datos locales, si no...
     * -> Muestra datos locales -> Comprueba versión de los datos -> Actualiza bbbdd_interna
     * -> Muestra los nuevos datos locales
     * <p>
     * Flujo caso 2: No tienes conexión.
     * -> ¿Tienes datos locales?
     * -> Muestra datos locales else
     * - Muestra error y permite al usuario volver a intentar la conexión. (Bucle/Salir ).
     * <p>
     * TODO: Añadir casos de flujo de errores internos.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void FlujoPrincipal() {
        mPartidoLab = PartidoLab.get(MainActivity.this);

        if (isNetworkAvailable()) {
            if (isEmptyPartidos()) {
                //TODO: Debe abrir un único canal SSH o cerrar la conexión.
                //TODO: No recarga los datos bucar medio.
                //TODO: Debe formatear la tabla.
                MostrarSinDatosConConexion();
                MostrarUltimaVersion();
                try {
                    RetrieveDatafromServer();
                } catch (Exception e) {
                    MostrarSinDatosConConexion();
                    MostrarUltimaVersion();
                }
            } else {
                //TODO: Debe abrir un único canal SSH o cerrar la conexión.
                //TODO: No recarga los datos bucar medio.
                //TODO: Debe formatear la tabla.
                RetrieveUIData();
                MostrarUltimaVersion();
                try {
                    RetrieveDatafromServer();
                } catch (Exception e) {
                    RetrieveUIData();
                }
            }
        } else {
            if (isEmptyPartidos()) {
                MostrarSinDatos();
                MostrarUltimaVersion();
            } else {
                MostrarDatosRecientes();
                MostrarDatosLista();
                MostrarDatosCalendario();
                MostrarUltimaVersion();
            }
        }
    }

    /**
     * Comprueba la conexión del cliente a través de los servicios de conectividad de Android.
     * Ya sean servicios que permitan recibir acceso a internet mediante Wifi ó datos moviles.
     * TODO: Probar que funciona conectado a redes sin acceso a internet.
     *
     * @return Devuelve el estado de la red, false=No hay conexión y true= Si hay Conexión.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Comprueba que la base de datos no se encuentre vacia.
     * Obtiene el número de elementos en su interior. Si
     * devuelve un número mayor a 1, se considerará "no vacia".
     *
     * @return Vacia si devuelve true. Con datos si devuelve false.
     */
    public Boolean isEmptyPartidos() {
        boolean output;
        mPartidoLab = PartidoLab.get(MainActivity.this);
        List<Partido> partidos_tabla_local = mPartidoLab.getPartidos();
        output = partidos_tabla_local.size() <= 0;
        return output;
    }

    /**
     * Establece la información en los campos Textview, recopilando los datos
     * de la base de datos local interna.
     * SI la información recopilada no contiene ningún dato resulta en error
     * de valor nulo. Comprobamos esta información y si se encuentra vacia,
     * devolvemos un mensaje indicando que no hay ningun evento cercano.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void MostrarDatosRecientes() {
        //mPartidoLab = PartidoLab.get(MainActivity.this);
        System.out.println("Último partido actual: " + mPartidoLab.getMoreRecentPartidos_limit6());
        System.out.println(mPartidoLab.getPartidos());

        Partido partidos = mPartidoLab.getMoreRecentPartido();
        if (mPartidoLab.getMoreRecentPartido() != null) {
            jornadaTextView.setText(partidos.getJornada());
            equipolocalTextView.setText(partidos.equipolocal);
            vs_separadorTextView.setText(context.getResources().getString(R.string.event_information_VS));
            equipovisitanteTextView.setText(partidos.equipovisitante);
            fechaTextView.setText(String.valueOf(new DameFecha().dameDateAqui(Date.valueOf(partidos.fechapartido))));
            hora_separador_TextView.setText(context.getResources().getString(R.string.event_information_AT));
            horaTextView.setText(partidos.horapartido);
        }
    }

    /**
     * Establece la información en los campos Textview.
     * Detalle de posible error detectado, según flujo de funcionamiento.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void MostrarSinDatos() {
        jornadaTextView.setText("");
        equipolocalTextView.setText(context.getResources().getString(R.string.error_messages_001));
        vs_separadorTextView.setText("");
        equipovisitanteTextView.setText("");
        fechaTextView.setText("");
        hora_separador_TextView.setText("");
        horaTextView.setText("");
    }

    /**
     * Establece la información en los campos Textview.
     * Detalle de posible error detectado, según flujo de funcionamiento.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void MostrarSinDatosConConexion() {
        jornadaTextView.setText("");
        equipolocalTextView.setText(context.getResources().getString(R.string.error_messages_003));
        vs_separadorTextView.setText("");
        equipovisitanteTextView.setText("");
        fechaTextView.setText("");
        hora_separador_TextView.setText("");
        horaTextView.setText("");
    }

    /**
     * Muestra la ultima versión de la información.
     */
    private void MostrarUltimaVersion() {
        mPartidoVersionsLab = PartidoVersionsLab.get(MainActivity.this);
        if (!(mPartidoVersionsLab.getPartidoVersionDao().size() <=0)) {
            Toast.makeText(context, String.valueOf("Fecha de información mostrada: "+mPartidoVersionsLab.getPartidoVersionDao().get(mPartidoVersionsLab.getPartidoVersionDao().size() - 1).version_date), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No contienes ninguna versión", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Obtiene los elementos de la base de datos interna, realizando
     * consultas que obtienen los partidos mas recientes de hasta un
     * limite de 3 para el listado.
     * TODO: Comprobar que ocurre al poner más elementos en la lista.
     */
    public void MostrarDatosLista() {
        mPartidoLab = PartidoLab.get(MainActivity.this);
        List<Partido> partidos_sel3 = mPartidoLab.getMoreRecentPartidos_limit3();
        for (int i = 0; i < partidos_sel3.size(); i++) {
            adapter.add(
                    /*
                    partidos_sel3.get(i).fechapartido + " | "
                            + partidos_sel3.get(i).competicion + " | "
                            + partidos_sel3.get(i).jornada);
                    */
                    partidos_sel3.get(i).competicion + " | "
                            + partidos_sel3.get(i).fechapartido + " | "
                            + partidos_sel3.get(i).horapartido);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Obtiene los elementos de la base de datos interna, realizando
     * consultas que obtienen los partidos mas recientes de hasta un
     * limite de 6 para marcar los partidos en el calendario.
     * TODO: Comprobar que ocurre al poner más elementos en el calendario.
     */
    public void MostrarDatosCalendario() {
        mPartidoLab = PartidoLab.get(MainActivity.this);
        List<Partido> partidos_sel6 = mPartidoLab.getMoreRecentPartidos_limit6();
        for (int i = 0; i < partidos_sel6.size(); i++) {
            StringTokenizer st = new StringTokenizer(partidos_sel6.get(i).fechapartido, "-");
            String anho = st.nextToken();
            String mes = st.nextToken();
            String dia = st.nextToken();
            final CollapsibleCalendar collapsibleCalendar = findViewById(R.id.calendarView);
            collapsibleCalendar.addEventTag(Integer.parseInt(anho), Integer.parseInt(mes) - 1, Integer.parseInt(dia));
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Realiza la limpieza de los listas
     * los elementos antiguos o recargados
     * se proceden a eliminar. Con la
     * acción de proporcionar nueva
     * información.
     * TODO: Días del calendario antiguos no se quitan ojo!
     */
    public void ReloadListView() {
        runOnUiThread(() -> {
            // Stuff that updates the UI
            adapter.clear();
            adapter.notifyDataSetChanged();
        });
    }

}