package com.wandaprototype.android;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class MainActivity extends AppCompatActivity  {
    //Fields
    private TextView jornadaTextView;
    private TextView equipolocalTextView;
    private TextView vs_separadorTextView;
    private TextView equipovisitanteTextView;
    private TextView fechaTextView;
    private TextView hora_separador_TextView;
    private TextView horaTextView;

    //Arrays : Elementos listados
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    //Mapbox
    private MapView mapView = null;
    private CollapsibleCalendar collapsibleCalendar;

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

        /**
         * Configuración ToolBar.
         * Toolbar personalizado y estilizado.
         */
        setSupportActionBar(findViewById(R.id.myToolbar));

        /**
         * Instancia una lista de elementos, permite añadir a la lista información
         * de los eventos que tengamos disponibles.
         * Aplica cambios en la lista notificando que los datos han cambiando al adaptador.
         */
        ListView listView = (ListView) findViewById(R.id.listv);
        String[] items = {};
        arrayList = new ArrayList<>(Arrays.asList(items));
        adapter= new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /**
         * Instancia y carga de MapBox, sustituye actualmente a GoogleMaps.
         * Permite establecer configuraciones y cambiar estilo de mapa.
         * TODO: Cambiar mapa a modo oscuro cuando se active la opción en el SO.
         */
        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.TRAFFIC_DAY);

        /**
         * Instancia CalendarView, permite manejar eventos tras realizar ciertas acciones
         * en la aplicación e interactuando con el mismo.
         */
        final CollapsibleCalendar collapsibleCalendar = findViewById(R.id.calendarView);
        View calendarIcon = collapsibleCalendar.getRootView().findViewById(R.id.today_icon);
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


    }


    /**
     * Ciclos de vida del módulo de mapa: MapBox.
     * Permite la llamada a funciones o acciones
     * en los ciclos correspondientes.
     */
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    /**
     * Se inicia el uso del flujo principal de la aplicación.
     * Especifica y relaciona los Textviews con las variables
     * asociadas.
     */
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        jornadaTextView = (TextView) findViewById(R.id.jornada);
        equipolocalTextView = (TextView) findViewById(R.id.equipoLocal);
        vs_separadorTextView = (TextView) findViewById(R.id.vs_separador);
        equipovisitanteTextView = (TextView) findViewById(R.id.equipoVisitante);
        fechaTextView = (TextView) findViewById(R.id.fecha);
        hora_separador_TextView = (TextView) findViewById(R.id.hora_separador);
        horaTextView = (TextView) findViewById(R.id.hora);
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
        if (Partido.partidos.size()==0) {
            Toast.makeText(this, "Hola he vuelto", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Huevos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Recopila información de servidor mediante un tunel seguro SSH.
     * Realiza las consultas correspondientes.
     * ¡Atención, no finaliza la conexión ni el tunel SSH!
     */
    private void RetrieveDatafromServer() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        File f = new File(getCacheDir()+"/ssh.key");
                        if (!f.exists()) try {

                            InputStream is = getAssets().open("ssh.key");
                            int size = is.available();
                            byte[] buffer = new byte[size];
                            is.read(buffer);
                            is.close();


                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(buffer);
                            fos.close();
                        } catch (Exception e) { throw new RuntimeException(e); }
                        String path = f.getPath();
                        System.out.println("***Pruebas***. "+path);
                        new DbManagerSSH().checkConnection(path);



                        /**
                         * Añadimos punto de control:
                         * Comprobamos si mi versión local contiene la ultima versión
                         * de partidosVersions interna y externa.
                         * TODO: Actualizar datos al recuperar información nueva del servidor
                         * TODO: Cerrar conexiones una vez finalizadas serie de consultas.
                         */
                        mPartidoVersionsLab = PartidoVersionsLab.get(MainActivity.this);
                        List<PartidoVersions> partidosVersions = mPartidoVersionsLab.getPartidoVersionDao();
                        if (new DbManagerSSH().ComprobarUltimaVersion(
                                partidosVersions.get(partidosVersions.size() - 1).version_number,
                                Timestamp.valueOf(partidosVersions.get(partidosVersions.size() - 1).version_date),
                                "wandametropolitano_versions")) {
                            //Elimina los datos locales:
                            mPartidoLab.setPartidosNuke();
                            new DbManagerSSH().DefinirObjetoPartido("wandametropolitano_partidos");
                            new DbManagerSSH().DefinirObjetoPartidoVersiones("wandametropolitano_versions");
                            System.out.println("***Pruebas2***. "+PartidoVersions.partidosVersions);

                            /**
                             * Transfiere los datos del ArrayList de Partidos a objeto DAO de partidos.
                             * En Pruebas.
                             */
                            mPartidoLab = PartidoLab.get(MainActivity.this);
                            List<Partido> partidos = mPartidoLab.getPartidos();

                            for (int i=0; i<Partido.partidos.size(); i++) {
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

                            /**
                             * Transfiere los datos del ArrayList de Partidos a objeto DAO de partidos.
                             * En Pruebas.
                             */
                            //Inicializado arriba: mPartidoVersionsLab = PartidoVersionsLab.get(MainActivity.this);
                            //List<PartidoVersions> partidosVersions = mPartidoVersionsLab.getPartidoVersionDao();

                            for (int i=0; i<PartidoVersions.partidosVersions.size(); i++) {
                                mPartidoVersions = new PartidoVersions();
                                mPartidoVersions.setVersion_number(PartidoVersions.partidosVersions.get(i).version_number);
                                mPartidoVersions.setVersion_date(PartidoVersions.partidosVersions.get(i).version_date);
                                mPartidoVersionsLab.addPartidoVersions(mPartidoVersions);
                            }
                        } else {
                            System.out.println("Ni hay novedades");
                        }

                        System.out.println("Versiones: "+partidosVersions);
                        for (int i=0; i<partidosVersions.size(); i++) {
                            System.out.println("Versiones: "+partidosVersions.get(i));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Flujo caso 1: Comprueba si tienes conexión.
     * -> ¿Tienes datos locales?
     *      - Recopila los datos nuevos, Muestra los nuevos datos locales, si no...
     * -> Muestra datos locales -> Comprueba versión de los datos -> Actualiza bbbdd_interna
     * -> Muestra los nuevos datos locales
     *
     * Flujo caso 2: No tienes conexión.
     * -> ¿Tienes datos locales?
     * -> Muestra datos locales else
     *      - Muestra error y permite al usuario volver a intentar la conexión. (Bucle/Salir ).
     *
     * TODO: Añadir casos de flujo de errores internos.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void FlujoPrincipal() {
        if (isNetworkAvailable()) {
            if (isEmptyPartidos()==true) {
                RetrieveDatafromServer();
                MostrarDatosRecientes();
                MostrarDatosLista();
                MostrarDatosCalendario();
            } else {
                MostrarDatosRecientes();
                MostrarDatosLista();
                MostrarDatosCalendario();
            }
        } else {
            if (isEmptyPartidos()==true) {
                MostrarSinDatos();
            } else {
                MostrarDatosRecientes();
                MostrarDatosLista();
                MostrarDatosCalendario();
            }
        }
    }

    /**
     * Comprueba la conexión del cliente a través de los servicios de conectividad de Android.
     * Ya sean servicios que permitan recibir acceso a internet mediante Wifi ó datos moviles.
     * TODO: Probar que funciona conectado a redes sin acceso a internet.
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
     * @return Vacia si devuelve true. Con datos si devuelve false.
     */
    public Boolean isEmptyPartidos() {
        Boolean output=true;
        mPartidoLab = PartidoLab.get(MainActivity.this);
        List<Partido> partidos_tabla_local = mPartidoLab.getPartidos();
        if (partidos_tabla_local.size()<=0) {
            output = true;
        } else {
            output = false;
        }
        return output;
    }

    /**
     * Establece la información en los campos Textview, recopilando los datos
     * de la base de datos local interna.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void MostrarDatosRecientes() {
        mPartidoLab = PartidoLab.get(MainActivity.this);
        Partido partidos =  mPartidoLab.getMoreRecentPartido();
        jornadaTextView.setText(partidos.getJornada());
        equipolocalTextView.setText(partidos.equipolocal);
        vs_separadorTextView.setText("VS");
        equipovisitanteTextView.setText(partidos.equipovisitante);
        fechaTextView.setText(String.valueOf(new DameFecha().dameDateAqui(Date.valueOf(partidos.fechapartido))));
        hora_separador_TextView.setText("a las");
        horaTextView.setText(partidos.horapartido);
    }

    /**
     * Establece la información en los campos Textview.
     * Detalle de posible error detectado, según flujo de funcionamiento.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void MostrarSinDatos() {
        jornadaTextView.setText("Error: 0001");
        equipolocalTextView.setText("Sin acceso a internet");
        vs_separadorTextView.setText("Compruebe que está haciendo uso");
        equipovisitanteTextView.setText("de datos móviles o");
        fechaTextView.setText("");
        hora_separador_TextView.setText("conexión Wi-Fi");
        horaTextView.setText("");
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
                    partidos_sel3.get(i).fechapartido + " | "
                            + partidos_sel3.get(i).competicion + " | "
                            + partidos_sel3.get(i).jornada);
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
            collapsibleCalendar.addEventTag(Integer.valueOf(anho), Integer.valueOf(mes)-1, Integer.valueOf(dia));
        }
        adapter.notifyDataSetChanged();
    }

}