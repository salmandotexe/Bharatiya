package com.kms.bharatiya;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

//Additional imports for Markers:
//import com.mapbox.mapboxandroiddemo.R; not found
//imports for Firestore database:
//Java imports:


public class MainActivity extends AppCompatActivity implements FilterDialog.filterdialoglistener {
    private String curstl= "mapbox://styles/thesalmansahel/ckh50xh0w00se19pfe4krhrxa";
    public interface filterdialoglistener{
        void Getvals(double val1, double val2);
    }
    //For messaging part
    public static String data;
    public static String whoami;
    public static String hishouseaddr;
    public static String whoishe;


    private MapView mapView;
    private MapboxMap mapboxMap;
    public FloatingActionButton ft;
    //For icons of map markers
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private Point center;

    private List<Feature> HouseList = new ArrayList<>();    //Contains list of houses where markers need to be placed.

    //Firebase realtime DB:
    DatabaseReference dbroot = FirebaseDatabase.getInstance().getReference();
    //DatabaseReference dbhouses =dbroot.child("House");

    //For dealing with Cursor placement:
    private boolean placedHousemarker=false;
    public static LatLng currentPosition = new LatLng(64.900932, -18.167040);
    private GeoJsonSource gjs;

    //For search menu:
    SearchView searchbar;

    //For the messages
    FloatingActionButton messagebtn;
    public static ArrayList<String> messages;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //For the Filter part
    public static List<Feature> filterlist = new ArrayList<>();
    FloatingActionButton filterbtn;
    @Override
    protected void onStart() {

        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    private String getAttribute(DataSnapshot item, String prop){
        if(item.child(prop).getValue()!=null){
            return item.child(prop).getValue().toString();
        }
        return "-1";
    }

    private String getAttribute(Feature ft, String prop){
        if(ft.properties().get(prop)!=null){
            return ft.properties().get(prop).getAsString();
        }
        return  "<<no field exists>>";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            int h;
            whoami =(String) b.get("Mail");
            Toast.makeText(MainActivity.this,"Welcome " + whoami, Toast.LENGTH_LONG).show();
        }

        center = Point.fromLngLat(90.4125,23.8103);
        searchbar = (SearchView)findViewById(R.id.searchView);
        messagebtn =findViewById(R.id.msgbtn);
        ft = findViewById(R.id.floatingActionButton);
        filterbtn=findViewById(R.id.filterbtn);
        messages = new ArrayList<String>();
        filterlist= new ArrayList<>();

        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilterdialog();
            }
        });
        messagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.clear();
                PerformMessageQuery();
            }
        });

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Submit
                //MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                        //.accessToken(R.string.mapbox_access_token)
                        //.query("1600 Pennsylvania Ave NW")
                        //.build();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Suggest places: i.e B for banani.
                //need to implement Arraylist, adapter,etc.
                return false;
            }
        });

        ft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!placedHousemarker){
                    Toast.makeText(MainActivity.this,"Please place marker first.", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(MainActivity.this, RegistrationV2.class);
                    i.putExtra("Mail",whoami);
                    startActivity(i);
                }
            }
        });



        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MainActivity.this.mapboxMap=mapboxMap;
                loadFromDatabase();
                gjs = new GeoJsonSource("source-id",
                        Feature.fromGeometry(Point.fromLngLat(currentPosition.getLongitude(),
                                currentPosition.getLatitude())));

            }
            /*
            private void loadFromDatabase(){
                db.collection("Houses")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot doc:task.getResult()){

                                    GeoPoint cur= doc.getGeoPoint("LatLong");
                                    Log.d("LATLONG",cur.getLongitude() + " " + cur.getLatitude());

                                    Feature ft=Feature.fromGeometry(Point.fromLngLat(cur.getLongitude(),cur.getLatitude()));
                                    //Adding Features here:
                                    Log.d("FEATX",ft.properties().toString());
                                    ft.properties().addProperty("Area",(Number)doc.getData().get("Area"));
                                    ft.properties().addProperty("Rent",(Number)doc.getData().get("Rent"));
                                    ft.properties().addProperty("Number of Rooms",(Number)doc.getData().get("NumberOfRooms"));

                                    //private property to identify unique user that posted it for notification system:
                                    //ft.properties().addProperty("userid",(String)doc.getData().get("userID"));
                                    HouseList.add(ft);
                                }
                                mapboxMap.setStyle(new Style.Builder().fromUri(curstl)
                                    //Note: Icons are added on a separate layer, as an overlay above the map.
                                    .withImage(ICON_ID, BitmapFactory.decodeResource( MainActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))

                                    //Place the marker icons above the houses for rent, GeoJson translates lat long into map coordinates.
                                    .withSource(new GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(HouseList)))

                                    //Creating the actual Layer as overlay, and adding an offset to the icon so the marker arrow points at the house.

                                    .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                    .withProperties(iconImage(ICON_ID), iconAllowOverlap(true), iconIgnorePlacement(true))),

                                        new Style.OnStyleLoaded() {
                                            @Override
                                            public void onStyleLoaded(@NonNull Style style) {

                                            }
                                        }
                                    );
                                }
                                else{
                                    Log.d("Dbgeterror", String.valueOf(task.getException()));
                                }
                            }
                        });
            }
            */


            private void loadFromDatabase(){
                dbroot.child("House").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                        while(items.hasNext()){
                            DataSnapshot item = items.next();
                            double lat = Double.parseDouble(getAttribute(item, "LAT"));
                            double lng = Double.parseDouble(getAttribute(item, "LONG"));

                            Log.d("LATLONG",lat + " " + lng);
                            Feature ft=Feature.fromGeometry(Point.fromLngLat(lng,lat));
                            ft.properties().addProperty("Address", getAttribute(item,"Address")); //Add rent, area, USERID (important)
                            ft.properties().addProperty("Flat Size", getAttribute(item,"Flat Size"));
                            ft.properties().addProperty("Bedroom", getAttribute(item,"Bedroom"));
                            ft.properties().addProperty("Bathroom", getAttribute(item,"Bathroom"));
                            ft.properties().addProperty("Rent", getAttribute(item,"Rent"));
                            ft.properties().addProperty("Email", getAttribute(item,"Email"));
                            HouseList.add(ft);
                        }
                        mapboxMap.setStyle(new Style.Builder().fromUri(curstl)
                                        //Note: Icons are added on a separate layer, as an overlay above the map.
                                        .withImage(ICON_ID, BitmapFactory.decodeResource( MainActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))

                                        //Place the marker icons above the houses for rent, GeoJson translates lat long into map coordinates.
                                        .withSource(new GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(HouseList)))

                                        //Creating the actual Layer as overlay, and adding an offset to the icon so the marker arrow points at the house.
                                        .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)

                                                .withProperties(iconImage(ICON_ID), iconAllowOverlap(true), iconIgnorePlacement(true))),

                                new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {
                                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                                            @Override
                                            public boolean onMapClick(@NonNull LatLng point) {
                                                if (handleClickIcon(mapboxMap.getProjection().toScreenLocation(point))) {
                                                    //clicked on some marker.
                                                    return true;
                                                }
                                                else{
                                                    if(placedHousemarker){
                                                        style.removeLayer("cursorlayer-id");
                                                        style.removeSource(gjs);
                                                        placedHousemarker=false;
                                                        return false;
                                                    }

                                                    currentPosition = point;
                                                    gjs = new GeoJsonSource("source-id",
                                                            Feature.fromGeometry(Point.fromLngLat(currentPosition.getLongitude(),
                                                                    currentPosition.getLatitude())));
                                                    style.addImage(("cursor"), BitmapFactory.decodeResource(
                                                            getResources(), R.drawable.mapbox_marker_icon_default));
                                                    style.addSource(gjs);
                                                    style.addLayer(new SymbolLayer("cursorlayer-id", "source-id")
                                                            .withProperties(iconImage(ICON_ID), iconAllowOverlap(true), iconIgnorePlacement(true)));

                                                    placedHousemarker=true;
                                                    return true;
                                                }
                                            }
                                        });

                                    }
                                }
                        );
                    }
                    private boolean handleClickIcon(PointF screenPoint) {
                        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
                        if (!features.isEmpty()) {
                            Log.d("ONCLICK","marker clicked");
                            Feature feature = features.get(0);

                            if(feature.properties().get("Address")!=null){
                                //Clicked on a valid marker
                                //Toast.makeText(MainActivity.this,feature.properties().toString(), Toast.LENGTH_LONG).show();
                                data="";

                                StringBuilder sb = new StringBuilder();
                                sb.append("Address: ");
                                sb.append(feature.properties().get("Address").getAsString());
                                sb.append("\n");

                                sb.append("Bedrooms: ");
                                sb.append(feature.properties().get("Bedroom").getAsString());
                                sb.append("\n");

                                sb.append("Bathrooms: ");
                                sb.append(feature.properties().get("Bathroom").getAsString());
                                sb.append("\n");

                                sb.append("Flat Size: ");
                                sb.append(feature.properties().get("Flat Size").getAsString());
                                sb.append("\n");

                                sb.append("Rent: ");
                                sb.append(feature.properties().get("Rent").getAsString());
                                sb.append("\n");

                                sb.append("\nAre you Interested?");
                                hishouseaddr = getAttribute(feature,"Address");
                                whoishe = getAttribute(feature, "Email");
                                Log.d("EMAIL", "passing "+ whoishe);
                                data = sb.toString();
                                InterestedDialog dd = new InterestedDialog();
                                dd.show(getFragmentManager(),"fk");

                            }
                            else{
                                //Degenerate marker.
                            }

                        } else {
                            Log.d("ONCLICK","map clicked");
                            return false;
                        }
                        return true;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }



        });



}

    private void openfilterdialog() {
        FilterDialog fd = new FilterDialog();
        fd.show(getSupportFragmentManager(),"example");
    }

    private boolean isDouble(String s){
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return  false;
        }
    }
    private double eps=1e-8;

    @Override
    public void Getvals(double v1, double v2){
        filterlist=new ArrayList<>();
        if(v1>v2) return;
        Log.d("PARSER","v1= "+v1 + " v2= "+v2 );
        for (Feature ft : HouseList){
            if(isDouble(getAttribute(ft,"Rent"))){
                double rent=-1;
                rent = Double.parseDouble(getAttribute(ft,"Rent"));
                Log.d("PARSER",""+rent);

                if(rent<eps) continue;
                if(v1-eps<=rent && rent <=v2+eps) filterlist.add(ft);
            }
        }
        Log.d("PARSER",filterlist.toString());
        Intent i = new Intent(MainActivity.this, FilteredMap.class);
        startActivity(i);
        return;
    }


    private boolean PerformMessageQuery() {
        String str="users/"+whoami+"/messages";
        db.collection(str).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc:task.getResult()){
                                String msg=doc.getString("msg");
                                Log.d("DBMSG",msg);
                                messages.add(msg);
                            }

                            StringBuilder txt = new StringBuilder();
                            for (String s : messages){
                                txt.append(s);
                                txt.append("\n");
                                txt.append("\n");
                            }
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.activity_notificationform,
                                    (ViewGroup) findViewById(R.id.msgcontainer));
                            NotificationForm nf = new NotificationForm();
                            nf.show(getSupportFragmentManager(),"dialog");
                        }
                        else{
                            Log.d("DBGETERROR","Unable to retrieve messages.");
                            Map<String,Object> msg = new HashMap<>();
                            msg.put("temp","errored once");
                            db.collection("users").document(whoami).set(msg);
                        }
                    }
                });
        return true;
    }
}