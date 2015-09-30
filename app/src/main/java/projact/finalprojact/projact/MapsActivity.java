package projact.finalprojact.projact;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Activity  {

    private GoogleMap googleMap;

    Double Latitude ;

    Double Longitude;
    String country ;

    String city ;

    String addressLine ;

    MarkerOptions marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        GPStrker gpsTracker = new GPStrker(this);

        if (!gpsTracker.getIsGPSTrackingEnabled()) {
            gpsTracker.showSettingsAlert();

        }

        String stringLongitude = String.valueOf(gpsTracker.longitude);
        Longitude = Double.valueOf(stringLongitude);
        String stringLatitude = String.valueOf(gpsTracker.latitude);
        Latitude = Double.valueOf(stringLatitude);

        country = gpsTracker.getCountryName(this);

        city = gpsTracker.getLocality(this);

        addressLine = gpsTracker.getAddressLine(this);

        marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title("ziv");


        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();


            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(getBaseContext(),"Me in : " +addressLine+'\n'+country+'\n'+city, Toast.LENGTH_SHORT).show();
        LatLng Me = new LatLng(Latitude, Longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Me,16));
        CircleOptions circleOptions = new CircleOptions().center(Me)
                .radius(75);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        googleMap.addMarker(marker);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        //  googleMap.setMyLocationEnabled(true);
        initilizeMap();
    }

}






