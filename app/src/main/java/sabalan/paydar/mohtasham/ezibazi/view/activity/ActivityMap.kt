package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast

import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

import java.io.IOException
import java.util.Locale

import sabalan.paydar.mohtasham.ezibazi.R


class ActivityMap : AppCompatActivity(), LocationListener {

    companion object {
        private val MIN_UPDATE_TIME: Long = 0
        private val MIN_UPDATE_DISTANCE = 0.0f
    }

    private var context: Context? = null

    //osm
    private var map: MapView? = null
    private var mapController: IMapController? = null
    private var startPoint: GeoPoint? = null
    private var latitude = 0.0
    private var longitude = 0.0


    private var locationManager: LocationManager? = null
    //  private LocationListener locationListener;
    private var isGPSEnabled = false
    private var isShowAllert = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_map)
        context = this


        //init views
        val btn = findViewById<Button>(R.id.btn)
        val btn_select = findViewById<Button>(R.id.btn_select)
        map = findViewById(R.id.map)

        //init osm
        initOsm()
        goToMyLocation(5)


        //init ic_location
        initLocation()



        btn.setOnClickListener {
            if (isGPSEnabled) {
                Toast.makeText(this@ActivityMap, "click lat=$latitude\nlon=$longitude", Toast.LENGTH_SHORT).show()
                goToMyLocation(17)
            } else {
                showSettingsAlert()
            }
        }


        btn_select.setOnClickListener {
            val point = map!!.mapCenter as GeoPoint
            //        Toast.makeText(ActivityMap.this, "select lat=" + point.getLatitude() + "\nlon=" + point.getLongitude(), Toast.LENGTH_SHORT).show();
            getAddress()
        }


    }


    private fun getAddress() {
        val point = map!!.mapCenter as GeoPoint
        val geocoder: Geocoder
        var addresses: List<Address>? = null
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val address = addresses!![0].getAddressLine(0)
        if (address != null) {
            Toast.makeText(this@ActivityMap, address, Toast.LENGTH_SHORT).show()
        }
        val city = addresses[0].getAddressLine(1)
        val country = addresses[0].getAddressLine(2)
    }

    private fun initOsm() {
        map!!.setTileSource(TileSourceFactory.MAPNIK)
        //    map.setBuiltInZoomControls(true);
        map!!.setMultiTouchControls(true)
        mapController = map!!.controller
    }


    private fun initLocation() {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGPSEnabled) {
            showSettingsAlert()
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            val location1 = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val location2 = locationManager!!.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            val location3 = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location1 != null) {
                onLocationChanged(location1)
            } else if (location2 != null) {
                onLocationChanged(location2)
            } else if (location3 != null) {
                onLocationChanged(location3)
            }else {
                if (Build.VERSION.SDK_INT < 23) {
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this)
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                    } else {
                        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this)
                    }
                }
            }

            goToMyLocation(17)

        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            }
        }
    }

    fun showSettingsAlert() {
        val alertDialog = android.app.AlertDialog.Builder(context)
        alertDialog.setTitle("GPS is not Enabled!")
        alertDialog.setMessage("Do you want to turn on GPS?")

        alertDialog.setPositiveButton("Yes") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            this@ActivityMap.startActivity(intent)
        }

        alertDialog.setNegativeButton("No") { dialog, which -> dialog.cancel() }

        alertDialog.show()
    }


    private fun goToMyLocation(zoom: Int) {
        startPoint = GeoPoint(latitude, longitude)

        mapController!!.animateTo(startPoint)
        //    mapController.setCenter(startPoint);
        mapController!!.setZoom(zoom)
        //    map.getMapCenter(startPoint);


        //draw circle
        //    List<GeoPoint> circle = Polygon.pointsAsCircle(startPoint, 100);
        //    Polygon p = new Polygon(map);
        //    p.setPoints(circle);
        ////    p.setTitle("Your Location");
        //    map.getOverlayManager().add(p);
        //    map.invalidate();

        //add marker
        val marker = Marker(map!!)
        marker.position = startPoint!!
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map!!.overlays.add(marker)
        map!!.invalidate()
        marker.icon = context!!.resources.getDrawable(R.drawable.ic_home_location)
        marker.title = "your address"
    }


    override fun onLocationChanged(location: Location) {
        //    if(ic_location != null) {
        latitude = location.latitude
        longitude = location.longitude
        //    }
        Toast.makeText(this@ActivityMap, "onChange lat=$latitude\nlon=$longitude", Toast.LENGTH_SHORT).show()
        goToMyLocation(12)
    }

    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

    }

    override fun onProviderEnabled(s: String) {

    }

    override fun onProviderDisabled(s: String) {

    }


    public override fun onResume() {
        super.onResume()
        map!!.onResume()
        initLocation()
    }

    public override fun onPause() {
        super.onPause()
        map!!.onPause()
    }

    override fun onRestart() {
        super.onRestart()
        initLocation()
    }

    override fun onStart() {
        super.onStart()
        initLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initLocation()
    }


}
