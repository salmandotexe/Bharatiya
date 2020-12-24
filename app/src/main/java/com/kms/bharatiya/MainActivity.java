package com.kms.bharatiya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.*;

public class MainActivity extends AppCompatActivity {
    private String curstl= "mapbox://styles/thesalmansahel/ckh50xh0w00se19pfe4krhrxa";
    private MapView mapView;
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

                /*
                    Add markers using this:
                    List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
                    symbolLayerIconFeatureList.add(Feature.fromGeometry(
                    Point.fromLngLat(-57.225365, -33.213144)));
                    symbolLayerIconFeatureList.add(Feature.fromGeometry(
                    Point.fromLngLat(-54.14164, -33.981818)));
                    symbolLayerIconFeatureList.add(Feature.fromGeometry(
                    Point.fromLngLat(-56.990533, -30.583266)));

                 */

                mapboxMap.setStyle(new Style.Builder().fromUri(curstl), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                    }
                });

            }
        });

        }
}