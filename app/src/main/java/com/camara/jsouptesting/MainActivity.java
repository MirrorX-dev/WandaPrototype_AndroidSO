package com.camara.jsouptesting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

//GoogleMaps:
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//MapBox:
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

//Calendar
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;


//public class MainActivity extends AppCompatActivity implements OnMapReadyCallback  {
public class MainActivity extends AppCompatActivity  {
    private TextView jornadaTextView;
    private TextView equipolocalTextView;
    private TextView vs_separadorTextView;
    private TextView equipovisitanteTextView;
    private TextView fechaTextView;
    private TextView hora_separador_TextView;
    private TextView horaTextView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    //private GoogleMap mMap;
    private MapView mapView = null;
    private CollapsibleCalendar collapsibleCalendar;


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
        Toast.makeText(this, "Recopilando", Toast.LENGTH_LONG).show();

        ListView listView = (ListView) findViewById(R.id.listv);
        String[] items = {};
        arrayList = new ArrayList<>(Arrays.asList(items));
        adapter= new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Maps load.
        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.TRAFFIC_DAY);
        //mapView.getMapboxMap().loadStyleUri("mapbox://styles/jbdeveloper/cl2m84iwv002o14qxou66cpvr");
        //mapbox://styles/jbdeveloper/cl2m84iwv002o14qxou66cpvr


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // Comentado debido a uso de otras herramientas.
        /*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         */

        //Calendar.
        //collapsibleCalendar.findViewById(R.id.calendarView);
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

    // Uso de mapas de Google.
    /*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng wanda = new LatLng(40.436362876100354, -3.5994575206557715);
        mMap.addMarker(new MarkerOptions()
                .position(wanda)
                .title("Marker in Wanda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(wanda));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        mMap.setIndoorEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setMinZoomPreference(14);
    }
     */


    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //if.
                if (Partido.partidos.size()==0) {
                    new ScramblerMatchs_Atletico_Madrid().ExtractDataFromURL();
                    new ScramblerMatchs_Atletico_Madrid().ExtractFirstDatedMatchFromURL();
                    new ScramblerMatchs_Atletico_Madrid().IdentifDatosParticionados();
                    new ScramblerMatchs_Atletico_Madrid().ClasifDatosFirstDatedMatch();
                    new ScramblerMatchs_Atletico_Madrid().ClasifDatos();
                    new ScramblerMatchs_Atletico_Madrid().LecturaDatosParticionadosSimple();
                    new ScramblerMatchs_Atletico_Madrid().agregarCamposConfirmardos();
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
            try {
                if (isNetworkAvailable() != false) {
                    jornadaTextView.setText(Partido.partidos.get(StandAloneObjectQuerys.ObtenerDatosPartido_MasCercano2()).competicion);
                    equipolocalTextView.setText(Partido.partidos.get(StandAloneObjectQuerys.ObtenerDatosPartido_MasCercano2()).equipolocal);
                    vs_separadorTextView.setText("VS");
                    equipovisitanteTextView.setText(Partido.partidos.get(StandAloneObjectQuerys.ObtenerDatosPartido_MasCercano2()).equipovisitante);
                    fechaTextView.setText(Partido.partidos.get(StandAloneObjectQuerys.ObtenerDatosPartido_MasCercano2()).fechapartido);
                    hora_separador_TextView.setText(" a las ");
                    horaTextView.setText(Partido.partidos.get(StandAloneObjectQuerys.ObtenerDatosPartido_MasCercano2()).horapartido);

                    StandAloneObjectQuerys.ObtenerDatosPartidos_MasCercanos2();
                    for (int i = 0; i < StandAloneObjectQuerys.identificadores.size(); i++) {
                        adapter.add(
                                Partido.partidos.get(StandAloneObjectQuerys.identificadores.get(i)).fechapartido + " | "
                                        + Partido.partidos.get(StandAloneObjectQuerys.identificadores.get(i)).competicion + " | "
                                        + Partido.partidos.get(StandAloneObjectQuerys.identificadores.get(i)).jornada);
                    }

                    StandAloneObjectQuerys.ObtenerDatosPartidos_MasCercanos();
                    for (int i = 0; i < StandAloneObjectQuerys.identificadores.size(); i++) {
                        StringTokenizer st = new StringTokenizer(Partido.partidos.get(StandAloneObjectQuerys.identificadores.get(i)).fechapartido, "/");
                        String dia = st.nextToken();
                        String mes = st.nextToken();
                        String anho = st.nextToken();
                        final CollapsibleCalendar collapsibleCalendar = findViewById(R.id.calendarView);
                        collapsibleCalendar.addEventTag(Integer.valueOf(anho), Integer.valueOf(mes)-1, Integer.valueOf(dia));
                    }
                    adapter.notifyDataSetChanged();


                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                StandAloneObjectQuerys.LimpiarListaIdentificadores();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        //Partido.partidos.clear();
        //StandAloneObjectQuerys.LimpiarListaIdentificadores();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Partido.partidos.size()==0) {
            Toast.makeText(this, "Hola he vuelto", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Huevos", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recargarDatos() {
        if (isNetworkAvailable()==false) {
            jornadaTextView.setText("");
            equipolocalTextView.setText("");
            vs_separadorTextView.setText("");
            equipovisitanteTextView.setText("Sin conexión a internet");
            fechaTextView.setText("");
            hora_separador_TextView.setText("");
            horaTextView.setText("");
        }
    }
}