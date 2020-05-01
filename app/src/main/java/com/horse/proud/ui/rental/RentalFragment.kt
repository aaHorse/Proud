package com.horse.proud.ui.rental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.callback.LoadDataListener
import com.horse.proud.databinding.FragmentRentalBindingImpl
import com.horse.proud.ui.common.BaseItemsFragment
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.rental.adapter.RentalAdapter
import kotlinx.android.synthetic.main.fragment_rental.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject

/**
 * 物品租赁列表界面
 *
 * @author liliyuan
 * @since 2020年4月26日15:39:09
 * */
class RentalFragment : BaseItemsFragment(),LoadDataListener, BGANinePhotoLayout.Delegate {

    lateinit var rentalFragment: RentalFragment

    val viewModelFactory by inject<RentalFragmentViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(RentalFragmentViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rental, container, false)
        super.onCreateView(view)
        val binding = DataBindingUtil.bind<FragmentRentalBindingImpl>(view)
        binding?.viewModel = viewModel
        initViews(view)
        rentalFragment = activity.rentalFragment!!
        observe()
        EventBus.getDefault().register(this)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(activity)
        adapter = RentalAdapter(rentalFragment,recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun loadItems() {
        viewModel.getTask()
    }

    override fun refresh() {
        viewModel.getTask()
    }

    override fun dataSetSize(): Int {
        return viewModel.rentalItems.size
    }

    override fun onLoad() {
        if (!isLoadingMore) {
            if (viewModel.rentalItems.isNotEmpty()) {
                isLoadingMore = true
                isLoadFailed = false
                isNoMoreData = false
                loadItems()
            }
        }
    }

    override fun loadFinished() {
        super.loadFinished()
        if (viewModel.rentalItems.isEmpty()) {
            //swipeRefresh.visibility = View.GONE
            recyclerView.visibility = View.GONE
            showNoContentViewWithButton(GlobalUtil.getString(R.string.app_name),
                GlobalUtil.getString(R.string.app_name),
                View.OnClickListener { MainActivity.actionStart(activity) })
        } else {
            hideNoContentView()
        }
    }

    /**
     * 观察 ViewModel 中的数据变化
     * */
    private fun observe(){
        viewModel.dataChanged.observe(activity, Observer {
            if(viewModel.rentalItems.isEmpty()){
                //swipeRefresh.visibility = View.GONE
                recyclerView.visibility = View.GONE
                //暂时回到主界面
                showNoContentViewWithButton(
                    GlobalUtil.getString(R.string.task_items_null),
                    GlobalUtil.getString(R.string.task_items_null_click),
                    View.OnClickListener { MainActivity.actionStart(activity) })
            }else{
                loadFinished()
                hideNoContentView()
                adapter.data = viewModel.rentalItems
                adapter.notifyDataSetChanged()
                scrollToTop()
            }
        })

        viewModel.isLoadingMore.observe(activity, Observer {
            isLoadingMore = viewModel.isLoadingMore.value!!
        })

        viewModel.loadFailed.observe(activity, Observer {
            loadFailed(null)
        })

        viewModel.isNoMoreData.observe(activity, Observer {
            logWarn(TAG,"被修改了")
            isNoMoreData = viewModel.isNoMoreData.value!!
        })
    }

    override fun onClickNinePhotoItem(
        ninePhotoLayout: BGANinePhotoLayout?,
        view: View?,
        position: Int,
        model: String?,
        models: MutableList<String>?
    ) {
        var mCurrentClickNpl: BGANinePhotoLayout? = ninePhotoLayout
        var photoPreviewIntentBuilder = BGAPhotoPreviewActivity.IntentBuilder(Proud.getContext())
        photoPreviewIntentBuilder.previewPhotos(mCurrentClickNpl!!.data)
            .currentPosition(mCurrentClickNpl!!.currentClickItemPosition)
        startActivity(photoPreviewIntentBuilder.build())

    }

    override fun onClickExpand(
        ninePhotoLayout: BGANinePhotoLayout?,
        view: View?,
        position: Int,
        model: String?,
        models: MutableList<String>?
    ) {
        ninePhotoLayout!!.setIsExpand(true)
        ninePhotoLayout.flushItems()
    }

    companion object{

        private const val TAG = "RentalFragment"

    }

}