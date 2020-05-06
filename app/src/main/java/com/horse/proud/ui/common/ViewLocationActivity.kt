package com.horse.proud.ui.common

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.horse.core.proud.Const
import com.horse.proud.R
import kotlinx.android.synthetic.main.activity_view_location.*

/**
 * 查看标记的地点
 *
 * @author liliyuan
 * @since 2020年5月6日08:49:31
 * */
class ViewLocationActivity : BaseActivity() {

    private lateinit var aMap: AMap

    private var latitude:Double = 0.0

    private var longitude:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_location)
        latitude = intent.getDoubleExtra(Const.Item.POSITION_LATITUDE,-1.0)
        longitude = intent.getDoubleExtra(Const.Item.POSITION_LONGITUTE,-1.0)
        //创建地图
        map.onCreate(savedInstanceState)
        setUpMap()
    }

    override fun setupViews() {
        quite.setOnClickListener {
            canceled()
        }
    }

    private fun setUpMap(){
        //显示地图
        aMap = map.map
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17f))
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(latitude,longitude)))
        aMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude,longitude))
                .alpha(0.75f)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources,R.drawable.local)))
                .setFlat(true))
        map.visibility = View.VISIBLE
    }

    private fun canceled(){
        var intent = Intent()
        setResult(Activity.RESULT_CANCELED,intent)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        map.onSaveInstanceState(outState)
    }

    companion object{

        private const val TAG = "ViewLocationActivity"

        fun actionStartForResult(latitude:Double,longitude:Double,activity: Activity, requestCode:Int){
            val intent = Intent(activity, ViewLocationActivity::class.java)
            intent.putExtra(Const.Item.POSITION_LATITUDE,latitude)
            intent.putExtra(Const.Item.POSITION_LONGITUTE,longitude)
            activity.startActivityForResult(intent,requestCode)
        }

    }

}
