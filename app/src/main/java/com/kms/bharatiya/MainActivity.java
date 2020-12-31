package com.kms.bharatiya;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

//Additional imports for Markers:
//import com.mapbox.mapboxandroiddemo.R; not found
//imports for Firestore database:
//Java imports:

public class MainActivity extends AppCompatActivity {
    private String curstl= "mapbox://styles/thesalmansahel/ckh50xh0w00se19pfe4krhrxa";
    private MapView mapView;

    //For icons of map markers
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private List<Feature> HouseList = new ArrayList<>();    //Contains list of houses where markers need to be placed.
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //Contains Firestore database.


    private void getHouseCoordinates(){
        //Get Houses collection from db
        db.collection("Houses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc:task.getResult()){
                                //GeoPoint cur= doc.getGeoPoint("LatLong");
                                //Log.d("LATLONG",cur.getLongitude() + "" + cur.getLatitude());
                                //HouseList.add(Feature.fromGeometry(
                                //                    Point.fromLngLat(90.414208, 23.779395)));
                            }
                        }
                        else{
                            Log.d("Dbgeterror", String.valueOf(task.getException()));
                        }
                    }
                });
        //get lat,long

        //HouseList.add(Feature.fromGeometry(
        //                    Point.fromLngLat(90.414208, 23.779395)));
    }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                HouseList.add(Feature.fromGeometry(
                    Point.fromLngLat(90.414208, 23.779395)));    //Three points in Gulshan-1
                HouseList.add(Feature.fromGeometry(
                    Point.fromLngLat(90.416990, 23.778934)));
                HouseList.add(Feature.fromGeometry(
                    Point.fromLngLat(90.417402,23.781153)));

                //TODO: Use a getCoordinates() function here that loads the Lat/Long from the Database and adds to HouseList.
                //getHouseCoordinates();
                mapboxMap.setStyle(new Style.Builder().fromUri(curstl)
                //Note: Icons are added on a separate layer, as an overlay above the map.

                //Add icon image for marker. DO NOT CHANGE ICON_ID at the moment.
                .withImage(ICON_ID, BitmapFactory.decodeResource( MainActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))

                //Place the marker icons above the houses for rent, GeoJson translates lat long into map coordinates.
                .withSource(new GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(HouseList)))

                // Creating the actual Layer as overlay, and adding an offset to the icon so the marker arrow points at the house.

                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                    .withProperties(
                        iconImage(ICON_ID),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true)
                    )
                ),

                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                    }
                });



            }
        });

        }
}