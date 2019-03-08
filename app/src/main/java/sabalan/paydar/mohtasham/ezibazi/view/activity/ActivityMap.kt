package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.wang.avi.AVLoadingIndicatorView

import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.osm.OsmService
import sabalan.paydar.mohtasham.ezibazi.model.OsmSearchedPlace
import sabalan.paydar.mohtasham.ezibazi.system.hardware.Hardware
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews


class ActivityMap : AppCompatActivity(), LocationListener {

    companion object {
        private val MIN_UPDATE_TIME: Long = 0
        private val MIN_UPDATE_DISTANCE = 0.0f
    }

    private var context: Context? = null

    private lateinit var actv_search: AutoCompleteTextView
    private lateinit var btn_go_to_user_location: Button
    private lateinit var btn_select: Button
    private lateinit var avl_search: AVLoadingIndicatorView

    //osm
    private var map: MapView? = null
    private var mapController: IMapController? = null
    private var startPoint: GeoPoint? = null
    private var user_latitude = 0.0
    private var user_longitude = 0.0


    private var map_latitude: Double? = null
    private var map_longitude: Double? = null


    private var locationManager: LocationManager? = null
    private lateinit var location: Location
    private var isGPSEnabled = false
    private var isShowAlert = false;

    private var state = ""
    private var city = ""
    private var street = ""

    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var  searchedPlaces: List<OsmSearchedPlace>


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_map)
        context = this

        //get extras
        val extras = intent.extras
        if (extras != null) {
            state = extras.getString("STATE")
            city = extras.getString("CITY")
        }
        //init views
        setupViews()
        setupTypeFace()

        //init osm
        initOsm()

        //init ic_location
        initUserLocation()

        //init selected_city location
        initSelectedCityLocation()

        btn_go_to_user_location.setOnClickListener {
            if (isGPSEnabled) {
                Toast.makeText(this@ActivityMap, "click lat=$user_latitude\nlon=$user_longitude", Toast.LENGTH_SHORT).show()
                goToMyLocation(17)
            } else {
                showSettingsAlert()
            }
        }

        btn_select.setOnClickListener {
            val point = map!!.mapCenter as GeoPoint
            getAddress()
        }

        actv_search.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //p2 is position
                Hardware.hideKeyboard(this@ActivityMap)
                actv_search.setText("")
                map_latitude = searchedPlaces[p2].lat
                map_longitude = searchedPlaces[p2].lon
                goToLocation(15)
//                Toast.makeText(this@ActivityMap, ""+searchedPlaces[p2].display_name, Toast.LENGTH_SHORT).show()
            }
        })

        actv_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchLocation()
            }
            false
        }

        actv_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (actv_search.text.toString().length > 2){
//                    searchedPlaces = ArrayList<OsmSearchedPlace>()
//                    searchLocation()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        actv_search.setOnTouchListener(object :  View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if (searchedPlaces.size > 0) {
                    // show all suggestions
                    if (!actv_search.getText().toString().equals(""))
                        adapter.getFilter().filter(null);
                    actv_search.showDropDown();
                }
                return false;
            }
        })

    }


    private fun setupViews(){
        actv_search = findViewById(R.id.actv_search)
        btn_go_to_user_location = findViewById(R.id.btn_go_to_user_location)
        btn_select = findViewById(R.id.btn_select)
        map = findViewById(R.id.map)
        avl_search = findViewById(R.id.avl_search)
    }

    private fun setupTypeFace(){
        actv_search.setTypeface(MyViews.getIranSansLightFont(context!!))
        btn_go_to_user_location.setTypeface(MyViews.getIranSansLightFont(context!!))
        btn_select.setTypeface(MyViews.getIranSansLightFont(context!!))
    }

    private fun getAddress() {

    }

    private fun initOsm() {
        map!!.setTileSource(TileSourceFactory.MAPNIK)
        map!!.setBuiltInZoomControls(false);
        map!!.setMultiTouchControls(true)
        mapController = map!!.controller
    }

    private fun initUserLocation() {
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
                user_latitude = location1.latitude
                user_longitude = location1.longitude
            } else if (location2 != null) {
                onLocationChanged(location2)
                user_latitude = location2.latitude
                user_longitude = location2.longitude
            } else if (location3 != null) {
                onLocationChanged(location3)
                user_latitude = location3.latitude
                user_longitude = location3.longitude
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

            drawHomeMarker()
        }

    }

    private fun initSelectedCityLocation(){
        val osmService = OsmService(context!!)
        osmService.searchSelectedCity(1, state, city, "", object : OsmService.onOsmPlacesReceived{
            override fun onReceived(status: Int, message: String, places: List<OsmSearchedPlace>) {
                val osmLocation = places.get(0)
                if(status == 1) {
                    map_latitude = osmLocation.lat
                    map_longitude = osmLocation.lon
                    goToLocation(14)
                }
            }
        })
    }

    private fun searchLocation(){
        avl_search.visibility = View.VISIBLE
        val text = actv_search.text.toString()
        val osmService = OsmService(context!!)
        osmService.search(5, state, city, text, object : OsmService.onOsmPlacesReceived{
            override fun onReceived(status: Int, message: String, places: List<OsmSearchedPlace>) {
                avl_search.visibility = View.INVISIBLE
                this@ActivityMap.searchedPlaces = places
                fillSearchedLocationResults()
            }
        })
    }

    private fun fillSearchedLocationResults(){
        val placesList = ArrayList<String>()
        for (i in 0..searchedPlaces.size - 1){
            placesList.add(searchedPlaces[i].display_name)
        }
        adapter =  ArrayAdapter<String>(this@ActivityMap, R.layout.item_map_search_result, placesList)
        adapter.filter.filter(null)
        actv_search.setThreshold(1)
        actv_search.showDropDown()
        actv_search.setAdapter(adapter)
    }




    private fun goToLocation(zoom: Int){
        startPoint = GeoPoint(map_latitude!!, map_longitude!!)

        mapController!!.animateTo(startPoint)
        //    mapController.setCenter(startPoint);
        mapController!!.setZoom(zoom)
    }


    private fun goToMyLocation(zoom: Int) {
        startPoint = GeoPoint(user_latitude, user_longitude)
        mapController!!.animateTo(startPoint)
        //    mapController.setCenter(startPoint);
        mapController!!.setZoom(zoom)
        //    map.getMapCenter(startPoint);
    }


    private fun drawHomeMarker(){
        if (user_latitude == 0.0 || user_longitude == 0.0){
            return
        }
        startPoint = GeoPoint(user_latitude, user_longitude)
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
        user_latitude = location.latitude
        user_longitude = location.longitude

        drawHomeMarker()
    }

    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {
    }

    override fun onProviderEnabled(s: String) {

    }

    override fun onProviderDisabled(s: String) {

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
        if (isShowAlert) return
        isShowAlert = true
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








    public override fun onResume() {
        super.onResume()
        map!!.onResume()
//        initUserLocation()
    }

    public override fun onPause() {
        super.onPause()
        map!!.onPause()
    }

    override fun onRestart() {
        super.onRestart()
//        initUserLocation()
    }

    override fun onStart() {
        super.onStart()
//        initUserLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        initUserLocation()
    }


}
