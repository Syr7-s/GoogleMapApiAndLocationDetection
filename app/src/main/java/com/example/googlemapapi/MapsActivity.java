package com.example.googlemapapi;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
//AIzaSyCG9-L4w5FnIVt5ZTHPW9F2VaoS2PPmgss
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    Button btn;
    Button btnGo;
    Button btnUydu;
    EditText konum;
    String yer;
    ZoomControls zoom;
    private final static int REQUEST_lOCATION=90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast toast = Toast.makeText(getApplicationContext(), "Haritaya Hosgeldiniz", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        tanimla();
        zoomIslemi();
        sonlandir();
        editKonum();
    }

    private void tanimla(){
        btn=(Button) findViewById(R.id.btnCık);
        btnGo=(Button) findViewById(R.id.btn_Go);
        konum=(EditText)findViewById(R.id.konum);
        btnUydu=(Button) findViewById(R.id.uyduDeg);
        zoom=(ZoomControls)findViewById(R.id.zoom);
    }
   private void zoomIslemi(){


        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
    }

    private void sonlandir(){
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        btnUydu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btnUydu.setText("UYDU");
                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btnUydu.setText("NORMAL");
                }
            }
        });

    }
    private void editKonum(){
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 yer=konum.getText().toString();
                if(yer!=null && !yer.equals("")){
                    List<Address> adressList1=null;
                    Geocoder geocoder=new Geocoder(MapsActivity.this);
                    try {
                        adressList1= geocoder.getFromLocationName(yer,1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Address address=adressList1.get(0);
                        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Burası "+yer));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                    catch (Exception ex) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Konum Bilgisi Hatasi", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                       System.err.println(ex.getMessage());

                    }
                }
            }
        });
    }
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);

    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng istanbul = new LatLng(41.0056544, 28.9718102);
        mMap.addMarker(new MarkerOptions().position(istanbul).title("İstanbul Ayasofya"));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.999232, 28.6481823))
                .title("Buradasınız").snippet("Beylikdüzü population 352,412"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.1126131,29.0073348))
                .title("Maslak").snippet("Sarıyer"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.1083881, 28.7741495))
                .title("Stadyum").snippet("Başakşehir"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(istanbul,12));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

    //   mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setTrafficEnabled(true);
        getInfoWindow( mMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.999232, 28.6481823))
                .title("Buradasınız").snippet("Beylikdüzü population 352,412")));
       /* PolygonOptions polygonOptions=new PolygonOptions()
                .add(new LatLng(40.999232, 28.6481823))
                .add(new LatLng(41.0056544, 28.9718102))
                .add(new LatLng(41.1126131,29.0073348))// Maslak
                .add(new LatLng(41.1083881,28.7741495));//Başakşehir Stadyumu
        Polygon polygon=mMap.addPolygon(polygonOptions);
        polygon.setStrokeColor(Color.GREEN);*/
        Polyline polyline=mMap.addPolyline(new PolylineOptions()
                .add(
                        new LatLng(40.999232, 28.6481823),
                        new LatLng(41.0056544, 28.9718102),
                        new LatLng(41.1126131,29.0073348),
                        new LatLng(41.1083881,28.7741495))
                .width(25)
                .color(COLOR_BLACK_ARGB)
                .geodesic(true)
        );
        Polygon polygon=mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(40.999232, 28.6481823),
                        new LatLng(41.0056544, 28.9718102),
                        new LatLng(41.1126131,29.0073348),
                        new LatLng(41.1083881,28.7741495)
                )
        );
        polygon.setTag("alpha");
        stylePolygon(polygon);
        //39.7483755,36.9258695 //sivas
        //40.1663712,38.0701429
        LatLng sivas=new LatLng(40.1663712,38.0701429);
        mMap.addMarker(new MarkerOptions().position(sivas).title("Sivas Suşehri Hoşgeldiniz"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sivas));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            mMap.setMyLocationEnabled(true);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_lOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_lOCATION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                    mMap.setMyLocationEnabled(true);
                }
            }else{
                Toast.makeText(getApplicationContext(),"Kullanıcı konum iznini vermedi",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}

