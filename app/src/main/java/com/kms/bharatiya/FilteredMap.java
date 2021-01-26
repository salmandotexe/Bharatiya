package com.kms.bharatiya;

import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class FilteredMap extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private String curstl= "mapbox://styles/thesalmansahel/ckh50xh0w00se19pfe4krhrxa";
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    private String getAttribute(Feature ft, String prop){
        if(ft.properties().get(prop)!=null){
            return ft.properties().get(prop).getAsString();
        }
        return  "<<no field exists>>";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_filteredmap);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }
    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (!features.isEmpty()) {
            Log.d("ONCLICK","marker clicked");
            Feature feature = features.get(0);

            if(feature.properties().get("Address")!=null){
                //Clicked on a valid marker
                //Toast.makeText(MainActivity.this,feature.properties().toString(), Toast.LENGTH_LONG).show();
                MainActivity.data="";

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
                MainActivity.hishouseaddr = getAttribute(feature,"Address");
                MainActivity.whoishe = getAttribute(feature, "Email");
                Log.d("EMAIL", "passing "+ MainActivity.whoishe);
                MainActivity.data = sb.toString();
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
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        FilteredMap.this.mapboxMap=mapboxMap;
        List<Feature> symbolLayerIconFeatureList = MainActivity.filterlist;
        mapboxMap.setStyle(new Style.Builder().fromUri(curstl)
                .withImage(ICON_ID, BitmapFactory.decodeResource(
                        getResources(), R.drawable.mapbox_marker_icon_default))
                .withSource(new GeoJsonSource(SOURCE_ID,
                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))
                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(
                                iconImage(ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                        )
                ), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {
                        if (handleClickIcon(mapboxMap.getProjection().toScreenLocation(point))) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

    }
}