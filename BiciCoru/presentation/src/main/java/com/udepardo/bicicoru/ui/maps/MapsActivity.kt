package com.udepardo.bicicoru.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.Window
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.udepardo.bicicoru.R
import com.udepardo.bicicoru.data.model.StationViewModel
import com.udepardo.bicicoru.domain.interactor.LocationInteractorKt
import com.udepardo.bicicoru.ui.userdata.login.LoginFragment
import com.udepardo.bicicoru.ui.userdata.summary.ProfileFragment
import kotlinx.android.synthetic.main.activity_map.*


class MapsActivity : AppCompatActivity(),
        OnMapReadyCallback,
        MapsViewTranslator,
        ClusterManager.OnClusterClickListener<StationViewModel>,
        ClusterManager.OnClusterItemClickListener<StationViewModel> {

    companion object {
        const val ZOOM_LEVEL_STATION = 16.3f
        const val STATION_LIST_HEIGHT_DP = 100f
        val transition = ChangeBounds().apply { duration = 200 }


        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MapsActivity::class.java))
        }
    }

    private val firebaseAnalytics by lazy { FirebaseAnalytics.getInstance(this) }

    private lateinit var mMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<StationViewModel>
    private lateinit var presenter: MapsPresenter

    private lateinit var stationsAdapter: StationsDetailsAdapter
    private var stationsAdapterHeight = 0f
    private var mapZoomLevel = 0f



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_map)

        presenter = MapsPresenter(this)

        stationsAdapter = StationsDetailsAdapter(
            this,
            ::onListItemClicked,
            ::onNavigateClicked,
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
        stationsAdapterHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STATION_LIST_HEIGHT_DP, resources.displayMetrics)

        (mapFragment as SupportMapFragment).getMapAsync(this)

        androidx.recyclerview.widget.LinearSnapHelper().attachToRecyclerView(stationDetailsContainer)

        stationDetailsContainer.adapter = stationsAdapter

        actionButton.setOnClickListener {
            firebaseAnalytics.logEvent("mainActivityPressed", null)
            if (PreferenceManager.getDefaultSharedPreferences(this@MapsActivity).getBoolean("loggedin", false)) {
                loadProfile()
            } else {
                loadLogin()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mClusterManager = ClusterManager(this, mMap)
        mClusterManager.renderer = CustomMarkerRenderer(this, mMap, mClusterManager)

        mMap.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnCameraIdleListener(mClusterManager)
            setOnMarkerClickListener(mClusterManager)
            setOnInfoWindowClickListener(mClusterManager)
            setOnMapClickListener { hideList() }
        }


        mClusterManager.apply {
            setOnClusterClickListener(this@MapsActivity)
            setOnClusterItemClickListener(this@MapsActivity)
        }

        mapZoomLevel = mMap.cameraPosition.zoom

        mMap.setOnCameraMoveListener {
            if (mMap.cameraPosition.zoom < mapZoomLevel) {
                presenter.zoomedOut()
            }
            mapZoomLevel = mMap.cameraPosition.zoom
        }

        presenter.mapLoaded()
    }

    override fun onClusterClick(cluster: Cluster<StationViewModel>): Boolean {
        firebaseAnalytics.logEvent("onClusterClick", null)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(cluster.position))
        stationsAdapter.updateModel(cluster.items.toList())
        showList()
        return true
    }

    override fun onClusterItemClick(stationViewModel: StationViewModel): Boolean {
        firebaseAnalytics.logEvent("onClusterItemClick", null)
        stationsAdapter.updateModel(stationViewModel)
        stationDetailsContainer.smoothScrollToPosition(0)
        showList()
        return true
    }

    @SuppressLint("MissingPermission")
    override fun checkPermissions() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(
            object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    firebaseAnalytics.logEvent("onPermissionGranted", null)
                    mMap.isMyLocationEnabled = true
                    subscribeToLocationUptades()
                    presenter.permissionsGranted()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    firebaseAnalytics.logEvent("onPermissionDenied", null)
                    presenter.permissionsDenied()

                }

            }
        ).check()
    }

    override fun onListItemClicked(stationViewModel: StationViewModel) {
        firebaseAnalytics.logEvent("onListItemClicked", null)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stationViewModel.pos, ZOOM_LEVEL_STATION))
    }

    override fun onNavigateClicked(stationViewModel: StationViewModel) {
        firebaseAnalytics.logEvent("onNavigateClicked", null)

        val intent = Intent(
            android.content.Intent.ACTION_VIEW,
            Uri.parse("google.navigation:ll=" + stationViewModel.pos.latitude + "," + stationViewModel.pos.longitude + "&mode=w")
        )
        startActivity(intent)
    }

    override fun showList() {
        firebaseAnalytics.logEvent("showList", null)
        val params = stationDetailsContainer.layoutParams
        params.height = stationsAdapterHeight.toInt()
        stationDetailsContainer.layoutParams = params
        TransitionManager.beginDelayedTransition(rootView, transition)
    }

    override fun hideList() {
        firebaseAnalytics.logEvent("showList", null)
        val params = stationDetailsContainer.layoutParams
        params.height = 0
        stationDetailsContainer.layoutParams = params
        TransitionManager.beginDelayedTransition(rootView, transition)
    }

    override fun updateClusterManager(items: List<StationViewModel>) {
        with(mClusterManager) {
            clearItems()
            addItems(items)
            cluster()
        }
    }

    override fun updateList(items: List<StationViewModel>) {
        stationsAdapter.items = items.toMutableList()
        stationsAdapter.updateModel(items)
    }

    override fun updateAdapterLocation(location: Location) {
        stationsAdapter.updateLocation(location)
    }

    override fun getFirstLocation() {
        LocationInteractorKt.getInstance(this).observe(this, androidx.lifecycle.Observer {
            it?.let {
                presenter.firstLocationAcquired(it)
                LocationInteractorKt.getInstance(this@MapsActivity).removeObservers(this@MapsActivity)
            }
        })
    }

    override fun subscribeToLocationUptades() {
        LocationInteractorKt.getInstance(this).observe(this, androidx.lifecycle.Observer {
            it?.let {
                presenter.locationAcquired(it)
            }
        })
    }

    override fun unsubscribeToLocationUpdates() {
        LocationInteractorKt.getInstance(this).removeObservers(this)
    }

    override fun moveCameraTo(location: Location, zoom: Float?) {

        val cameraPosition = CameraPosition.Builder().target(LatLng(location.latitude, location.longitude))
        if (zoom != null) {
            cameraPosition.zoom(zoom)
        }

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition.build()))
    }


    override fun getLifeCycleOwner() = this

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.profielFragmentContainer) != null) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun loadProfile() {
        removeAllFragments()
        addFragment(ProfileFragment.start(::addFragment, ::removeAllFragments))
    }

    private fun loadLogin() {
        addFragment(LoginFragment.start(::loadProfile))
    }

    private fun removeAllFragments(){
        while (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStackImmediate()
    }

    private fun addFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.profielFragmentContainer, fragment, fragment.javaClass.simpleName).addToBackStack(fragment.tag).commit()
    }

}
