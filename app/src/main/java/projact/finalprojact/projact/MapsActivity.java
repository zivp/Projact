package projact.finalprojact.projact;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MapsActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    String Latitude;
    String Longitude;
    Double Latitude2;
    Double Longitude2;


    String country;
    String city;
    String addressLine;
    String KidNameForTitle;
    EditText cityKid;
    EditText addressKid;

    String cityloc;
    String addrasloc;
    String longloc;
    String latitloc;
    String TitleForMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        cityKid=(EditText)findViewById(R.id.editTextCity);
        addressKid=(EditText)findViewById(R.id.editTextAddress);


       final  ParseQuery<ParseObject> Query1 = ParseQuery.getQuery("LocKid");
        Query1.whereEqualTo("objectId", "B7ud9Cp9Yb");
        Query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                if (results.size() > 0) {
                    for (int index = 0; index < results.size(); index++) {

                        Latitude = (String.valueOf(results.get(index).get("Latitude")));
                        Longitude = (String.valueOf(results.get(index).get("Longitude")));
                        city = results.get(index).getString("City");
                        addressLine = results.get(index).getString("AddressLine");
                        KidNameForTitle=results.get(index).getString("NameKid");
                    }



                    SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
                    SharedPreferences.Editor editor = prefernces.edit();
                    editor.putString("City", city).apply();
                    editor.putString("LATITUDE", Latitude).apply();
                    editor.putString("LONGITUDE", Longitude).apply();
                    editor.putString("ADDRESS",addressLine).apply();
                    editor.putString("NAMEKIDFORTITLE",KidNameForTitle).apply();

                } else {
                    Toast.makeText(getBaseContext(), "no", Toast.LENGTH_SHORT).show();
                }


            }


        });


        SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
        cityloc = prefernces.getString("City", "");
        addrasloc = prefernces.getString("ADDRESS", "");
        longloc = prefernces.getString("LONGITUDE", "");
        latitloc = prefernces.getString("LATITUDE", "");
        TitleForMap=prefernces.getString("NAMEKIDFORTITLE","");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

       Longitude2 = Double.valueOf(longloc);
       Latitude2 = Double.valueOf(latitloc);
        LatLng KidLocation = new LatLng(Latitude2, Longitude2);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KidLocation, 16));

        googleMap.addMarker(new MarkerOptions()
                .title(TitleForMap)
                        //  .snippet(addrasloc + '\n' + cityloc)
                .position(KidLocation));
         cityKid.setText(cityloc);
        addressKid.setText(addrasloc);

    }
}






