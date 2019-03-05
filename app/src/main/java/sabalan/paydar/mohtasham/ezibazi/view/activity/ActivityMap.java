package sabalan.paydar.mohtasham.ezibazi.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import sabalan.paydar.mohtasham.ezibazi.R;


public class ActivityMap extends AppCompatActivity implements LocationListener {


  private static final long MIN_UPDATE_TIME = 1500;
  private static final float MIN_UPDATE_DISTANCE = 20.0f;

  private Context context;

  //osm
  private MapView map = null;
  private IMapController mapController;
  private GeoPoint startPoint;
  private double latitude = 0;
  private double longitude = 0;


  private LocationManager locationManager;
  //  private LocationListener locationListener;
  private boolean isGPSEnabled = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Context ctx = getApplicationContext();
    Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
    setContentView(R.layout.activity_map);
    context = this;


    //init views
    Button btn = findViewById(R.id.btn);
    Button btn_select = findViewById(R.id.btn_select);
    map = findViewById(R.id.map);

    //init osm
    initOsm();
    goToMyLocation(5);


    //init ic_location
    initLocation();



    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (isGPSEnabled) {
          Toast.makeText(ActivityMap.this, "click lat=" + latitude + "\nlon=" + longitude, Toast.LENGTH_SHORT).show();
          goToMyLocation(17);
        }else {
          showSettingsAlert();
        }
      }
    });


    btn_select.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        GeoPoint point = (GeoPoint) map.getMapCenter();
//        Toast.makeText(ActivityMap.this, "select lat=" + point.getLatitude() + "\nlon=" + point.getLongitude(), Toast.LENGTH_SHORT).show();
        getAddress();
      }
    });








  }



  private void getAddress(){
    GeoPoint point = (GeoPoint) map.getMapCenter();
    Geocoder geocoder;
    List<Address> addresses = null;
    geocoder = new Geocoder(this, Locale.getDefault());
    try {
      addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String address = addresses.get(0).getAddressLine(0);
    if (address != null){
      Toast.makeText(ActivityMap.this, address, Toast.LENGTH_SHORT).show();
    }
    String city = addresses.get(0).getAddressLine(1);
    String country = addresses.get(0).getAddressLine(2);
  }

  private void initOsm(){
    map.setTileSource(TileSourceFactory.MAPNIK);
//    map.setBuiltInZoomControls(true);
    map.setMultiTouchControls(true);
    mapController = map.getController();
  }


  private void initLocation(){
    locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    if (!isGPSEnabled) {
      showSettingsAlert();
    } else {

      if (android.support.v4.app.ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && android.support.v4.app.ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
      }

      Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
      Location location3 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
      if (location1 != null) {
        onLocationChanged(location1);
      }else if(location2 != null){
        onLocationChanged(location2);
      }else if(location3 != null) {
        onLocationChanged(location3);
      }

      if (Build.VERSION.SDK_INT < 23){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);
      }else {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);
        }
      }

      goToMyLocation(17);

    }

  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
      if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
      }
    }
  }

  public void showSettingsAlert() {
    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
    alertDialog.setTitle("GPS is not Enabled!");
    alertDialog.setMessage("Do you want to turn on GPS?");

    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        ActivityMap.this.startActivity(intent);
      }
    });

    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });

    alertDialog.show();
  }


  private void goToMyLocation(int zoom){
    startPoint = new GeoPoint(latitude, longitude);

    mapController.animateTo(startPoint);
//    mapController.setCenter(startPoint);
    mapController.setZoom(zoom);
//    map.getMapCenter(startPoint);


    //draw circle
//    List<GeoPoint> circle = Polygon.pointsAsCircle(startPoint, 100);
//    Polygon p = new Polygon(map);
//    p.setPoints(circle);
////    p.setTitle("Your Location");
//    map.getOverlayManager().add(p);
//    map.invalidate();

    //add marker
    Marker marker = new Marker(map);
    marker.setPosition(startPoint);
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
    map.getOverlays().add(marker);
    map.invalidate();
    marker.setIcon(context.getResources().getDrawable(R.drawable.ic_home_location));
    marker.setTitle(String.valueOf("your address"));
  }



  @Override
  public void onLocationChanged(Location location) {
//    if(ic_location != null) {
      latitude = location.getLatitude();
      longitude = location.getLongitude();
//    }
    Toast.makeText(ActivityMap.this, "onChange lat=" + latitude + "\nlon=" + longitude, Toast.LENGTH_SHORT).show();
    goToMyLocation(12);
  }

  @Override
  public void onStatusChanged(String s, int i, Bundle bundle) {

  }

  @Override
  public void onProviderEnabled(String s) {

  }

  @Override
  public void onProviderDisabled(String s) {

  }







  public void onResume(){
    super.onResume();
    map.onResume();
    initLocation();
  }

  public void onPause(){
    super.onPause();
    map.onPause();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    initLocation();
  }
}
