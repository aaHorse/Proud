package com.horse.proud.ui.common

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.*
import com.amap.api.maps.AMap.OnMarkerDragListener
import com.amap.api.maps.model.*
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import kotlinx.android.synthetic.main.activity_map.*

/**
 * 地图选点
 *
 * @author liliyuan
 * @since 2020年4月28日08:43:59
 * */
class MapActivity : BaseActivity() {

    private lateinit var aMap: AMap

    //声明AMapLocationClient类对象
    private lateinit var mLocationClient: AMapLocationClient

    //声明定位回调监听器
    private var mLocationListener: AMapLocationListener ?= null

    private lateinit var mLocationOption: AMapLocationClientOption

    private var latitude:Double = 0.0

    private var longitude:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        //创建地图
        map.onCreate(savedInstanceState)
        setUpMap()
    }

    override fun setupViews() {
        setupToolbar()
        quite.setOnClickListener {
            canceled()
        }
        sure.setOnClickListener {
            success()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    /**
     * 地图显示设置
     * */
    private fun setUpMap(){
        //显示地图
        aMap = map.map
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17f))
        configLocation()
        aMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(arg0: Marker) {
                showToast(GlobalUtil.getString(R.string.drag_map))
            }

            override fun onMarkerDragEnd(arg0: Marker) {
                //var string = "${arg0.position.latitude}"
                //showToast(string)
                latitude = arg0.position.latitude
                longitude = arg0.position.longitude
            }

            override fun onMarkerDrag(arg0: Marker) {
                showToast(GlobalUtil.getString(R.string.drag_map))
            }
        })


    }

    /**
     * 获取定位
     * */
    private fun configLocation(){
        val locationStr = intent.getStringExtra(Const.ACTIVITY_CONTENT)
        val array = locationStr?.split(",")

        //使用默认坐标
        if(array?.size == 2){
            latitude = array[0].toDouble()
            longitude = array[1].toDouble()
            setLocationMap()
            return
        }

        //使用定位坐标
        mLocationClient = AMapLocationClient(Proud.context)
        mLocationListener = AMapLocationListener {
            if(it.errorCode == 0){
                //定位成功
                latitude = it.latitude
                longitude = it.longitude
                setLocationMap()
            }else{
                //定位失败
                showToast("定位失败：${it.errorInfo}")
                logWarn(TAG,"${it.errorInfo}")
            }
            mLocationClient.stopLocation()
            map.visibility = View.VISIBLE
            mLocationListener = null
        }
        mLocationClient.setLocationListener(mLocationListener)

        mLocationOption = AMapLocationClientOption()
        mLocationOption.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption.isOnceLocationLatest = true
        //设置请求超时时间
        mLocationOption.httpTimeOut = 80000

        mLocationClient.setLocationOption(mLocationOption)
        mLocationClient.stopLocation()
        mLocationClient.startLocation()
    }

    /**
     * 设置指定坐标的地图
     * */
    private fun setLocationMap(){
        aMap.addMarker(MarkerOptions()
            .position(LatLng(latitude,longitude))
            .draggable(true)
            .alpha(0.75f)
            .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources,R.drawable.local)))
            .setFlat(true))

        aMap.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(latitude,longitude)))
    }

    private fun success(){
        var intent = Intent()
        intent.putExtra(Const.Item.POSITION_LATITUDE,latitude)
        intent.putExtra(Const.Item.POSITION_LONGITUTE,longitude)
        setResult(Activity.RESULT_OK,intent)
        finish()
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

        private const val TAG = "MapActivity"

        fun actionStartForResult(activity: Activity,requestCode:Int){
            val intent = Intent(activity, MapActivity::class.java)
            activity.startActivityForResult(intent,requestCode)
        }

        /**
         * @param locationStr 默认坐标
         * */
        fun actionStartForResult(activity: Activity,requestCode:Int,locationStr:String){
            val intent = Intent(activity, MapActivity::class.java)
            intent.putExtra(Const.ACTIVITY_CONTENT,locationStr)
            activity.startActivityForResult(intent,requestCode)
        }

    }

}
