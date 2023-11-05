package com.example.frompet.ui.map

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.frompet.R
import com.example.frompet.data.model.User
import com.example.frompet.data.model.UserLocation
import com.example.frompet.databinding.FragmentMapBinding
import com.example.frompet.ui.chat.activity.ChatUserDetailActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.checkerframework.checker.nullness.qual.NonNull
import java.io.File

class NaverMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

//    private val ONE_MEGABYTE : Long = 1024 * 1024

    private lateinit var naverMap: NaverMap
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private lateinit var locationSource: FusedLocationSource
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!


    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                initMapView()
            }
        }

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "" // 현재 uid 갖고 옴

    // onCreateView 에서 권한 확인하며 위치 권한 없을 시, 사용자에게 권한 요청
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasPermission()) {
            requestMultiplePermissions.launch(PERMISSIONS)
        } else {
            initMapView()
        }

    }

    private fun initMapView() {
        val fm = childFragmentManager
        val mapFragment = fm?.findFragmentById(R.id.naver_map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.naver_map_fragment, it).commit()

            }

        // fragment의 getMapAsync() 메서드로 OnMapReadyCallBack 콜백을 등록하면, 비동기로 NaverMap 객체를 얻을 수 있음
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    //hasPermission()에서는 위치 권한 있 -> true , 없 -> false
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) }
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 현 위치
        naverMap.locationSource = locationSource
        // 현 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = true
        // 위치를 추적하면서 카메라도 같이 움직임
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        Log.d("sooj", "onmapready")

        // 줌
        naverMap.maxZoom = 17.0  // (최대 21)
        naverMap.minZoom = 5.0

        // Firebase
        val database = Firebase.database
        val locationRef = database.getReference("location")

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext()) // 초기화
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                // 사용자 현재 위치 파베에 업로드
                val userLocation = UserLocation(location.latitude, location.longitude) // 사용자 위도 경도
                location?.let {
                    UserLocation(latitude = location.latitude, longitude = it.longitude)
                    locationRef.child(currentUserId).setValue(userLocation)
                    Log.d(
                        "sooj",
                        "${UserLocation(latitude = location.latitude, longitude = it.longitude)}"
                    )
                    Log.d("sooj", "${locationRef.child(currentUserId).setValue(userLocation)}")
                }
            }
        }
        locationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshots in snapshot.children) {
                    Log.d("sooj", "$snapshot")
                    val location = snapshots.getValue(UserLocation::class.java)
                    val userUid = snapshots.key // 사용자 uid

                    // 다른 사용자 위치 마커 표시
                    if (location != null && userUid != null && userUid != currentUserId) {
                        setMark(userUid,location) //사용자 uid 셋마크로 넘겨주고
                        Log.d("sooj", "$location")
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun setMark(userUid:String,location: UserLocation) = lifecycleScope.launch {
        if (!isAdded) return@launch //프래그먼트에서 액티비티가 연결되어 있는지 확인 만약 연결되어 있지 않다면 빠르게 종료해서requireContext호출을 방지
        val marker = Marker()
        // 마커 위치
        if (location != null) {
            marker.position = LatLng(location.latitude, location.longitude)
        }
        // 마커 우선순위
        marker.zIndex = 10
        // 마커 표시
        marker.map = naverMap
        // 원근감 표시
        marker.isIconPerspectiveEnabled = true
        // 마커의 투명도
//        marker.alpha = 0.8f

        marker.width = 150
        marker.height = 150

        marker.onClickListener = Overlay.OnClickListener {
            lifecycleScope.launch {
            val userDocument = firestore.collection("User").document(userUid).get().await() //컬렉셕에 사용자 uid로 접근하고 비동기로 동작 데이터 가져올때까지 기달
            val user = userDocument.toObject(User::class.java) //위에서 얻은 문서들을 user클래스의 인스턴스로 변환
            val intent = Intent(context, MapUserDetailActivity::class.java)
            intent.putExtra(MapUserDetailActivity.USER, user)
            startActivity(intent)
            }
            true
        }

        val userDocument = firestore.collection("User").document(userUid).get()
            .await() //컬렉셕에 사용자 uid로 접근하고 비동기로 동작 데이터 가져올때까지 기달
        val user = userDocument.toObject(User::class.java) //위에서 얻은 문서들을 user클래스의 인스턴스로 변환
        val profileUrl = user?.petProfile //유저인스턴스에 해당 사용자들의 프로필 사진 변수
        if (profileUrl != null) { //이미지가 널값이 아닐때
                val imageLoader = context?.let { Coil.imageLoader(it) }
                val request = ImageRequest.Builder(requireActivity())
                    .data(profileUrl)
                    .size(200,200)
                    .transformations(CircleCropTransformation(),MapMakerBorder(requireContext(),15f)) //이미지동그랗게
                    .target {
                        val bitmap = (it as BitmapDrawable).bitmap
                        val imageOverlay = OverlayImage.fromBitmap(bitmap)
                        marker.icon = imageOverlay
                    }.build()

                imageLoader?.execute(request)
            }
        }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}