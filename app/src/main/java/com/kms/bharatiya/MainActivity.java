package com.kms.bharatiya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.*;

//Additional imports for Markers:

import android.graphics.BitmapFactory;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
//import com.mapbox.mapboxandroiddemo.R; not found
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;



//Java imports:
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String curstl= "mapbox://styles/thesalmansahel/ckh50xh0w00se19pfe4krhrxa";
    private MapView mapView;

    //For icons of map markers
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private List<Feature> HouseList = new ArrayList<>();    //Contains list of houses where markers need to be placed.
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