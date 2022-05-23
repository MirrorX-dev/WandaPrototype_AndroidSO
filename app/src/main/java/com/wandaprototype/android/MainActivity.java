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
import com.wandaprototype.android.dataformat.DameFecha;
import com.wandaprototype.android.objects.Partido;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Date;
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
    private PartidoLab mPartidoLab;
    private Partido mPartido;

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToolBar:
        setSupportActionBar(findViewById(R.id.myToolbar));

        //Elementos Scrambler:
        Content content = new Content();
        content.execute();

        ListView listView = (ListView) findViewById(R.id.listv);
        String[] items = {};
        arrayList = new ArrayList<>(Arrays.asList(items));
        adapter= new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Maps load.
        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.TRAFFIC_DAY);

        //Calendar.
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
                collapsibleCalendar.addEventTag(2022, 3, 29);
            }

            public void onWeekChange(int i) {
            }
        });


    }


    //Life Cycle Mapbox Maps.
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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        jornadaTextView = (TextView) findViewById(R.id.jornada);
        equipolocalTextView = (TextView) findViewById(R.id.equipoLocal);
        vs_separadorTextView = (TextView) findViewById(R.id.vs_separador);
        equipovisitanteTextView = (TextView) findViewById(R.id.equipoVisitante);
        fechaTextView = (TextView) findViewById(R.id.fecha);
        hora_separador_TextView = (TextView) findViewById(R.id.hora_separador);
        horaTextView = (TextView) findViewById(R.id.hora);

        //Comprueba que haya conexión. Si no hay conexión. Se muestran los datos en blanco.
        recargarDatos();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }



    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
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
                        new DbManagerSSH().DefinirObjetoPartido("wandametropolitano_partidos");
                        System.out.println("***Pruebas***. "+Partido.partidos);

                        /**
                         * Transfiere los datos del ArrayList de Partidos a objeto DAO de partidos.
                         * En Pruebas.
                         */
                        mPartidoLab = PartidoLab.get(MainActivity.this);
                        List<Partido> partidos = mPartidoLab.getPartidos();

                        for (int i=0; i<Partido.partidos.size(); i++) {
                            for (int a=0; a<partidos.size(); a++) {
                                if (!partidos.get(a).id_partido.equals(Partido.partidos.get(i).id_partido)) {
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
                        }

                        for (int i=0; i<partidos.size(); i++) {
                            Log.e("Lectura", String.valueOf(partidos.get(i)));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
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
     * Permite comprobar la conexión, recargar los datos al entrar/salir/restaurar
     * la aplicación con el fin de proporcionar datos actualizados.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recargarDatos() {
        mPartidoLab = PartidoLab.get(MainActivity.this);
        if (isNetworkAvailable()==false) {
            //List<Partido> partidos = mPartidoLab.getPartidos();
            Partido partidos =  mPartidoLab.getMoreRecentPartido();
            jornadaTextView.setText(partidos.getJornada());
            equipolocalTextView.setText(partidos.equipolocal);
            vs_separadorTextView.setText("VS");
            equipovisitanteTextView.setText(partidos.equipovisitante);
            fechaTextView.setText(String.valueOf(new DameFecha().dameDateAqui(Date.valueOf(partidos.fechapartido))));
            hora_separador_TextView.setText("a las");
            horaTextView.setText(partidos.horapartido);
            recargarLista();
        } else {
            /**
             * Comprobar versión de la bbdd local y externa:
             */
            //List<Partido> partidos = mPartidoLab.getPartidos();
            Partido partidos =  mPartidoLab.getMoreRecentPartido();
            jornadaTextView.setText(partidos.getJornada());
            equipolocalTextView.setText(partidos.equipolocal);
            vs_separadorTextView.setText("VS");
            equipovisitanteTextView.setText(partidos.equipovisitante);
            fechaTextView.setText(String.valueOf(new DameFecha().dameDateAqui(Date.valueOf(partidos.fechapartido))));
            hora_separador_TextView.setText("a las");
            horaTextView.setText(partidos.horapartido);
            recargarLista();
        }
    }


    public void recargarLista() {
        mPartidoLab = PartidoLab.get(MainActivity.this);
        List<Partido> partidos_sel3 = mPartidoLab.getMoreRecentPartidos_limit3();
        List<Partido> partidos_sel6 = mPartidoLab.getMoreRecentPartidos_limit6();

        for (int i = 0; i < partidos_sel3.size(); i++) {
            adapter.add(
                    partidos_sel3.get(i).fechapartido + " | "
                            + partidos_sel3.get(i).competicion + " | "
                            + partidos_sel3.get(i).jornada);
        }

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