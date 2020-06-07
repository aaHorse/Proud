package com.horse.proud.ui.common

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.bingoogolapple.baseadapter.BGAOnItemChildClickListener
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.postDelayed
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.AndroidVersion
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.callback.InfiniteScrollListener
import com.horse.proud.callback.LoadDataListener
import com.horse.proud.event.*
import com.horse.proud.ui.home.MainActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 任务发布、失物招领、物品租赁对应的列表加载基类。
 *
 *
 * 加载状态的切换
 * 在BaseFragment中，有服务器错误状态、网络错误状态、空布局、加载状态，调用就可以进行切换
 * 在本类中，有刷新状态
 *
 * @author liliyuan
 * @since 2020年4月25日05:26:22
 * */
abstract class BaseItemsFragment : BaseFragment(){

    /**
     * 判断是否正在加载更多
     * */
    internal var isLoadingMore = false

    /**
     * 加载失败
     * */
    var isLoadFailed = false

    /**
     * 判断服务器是否还有更多数据。如果没有了，为 true，否则为 false。
     * */
    var isNoMoreData = false

    lateinit var activity: MainActivity

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var recyclerView: RecyclerView

    internal lateinit var adapter: BGARecyclerViewAdapter<*>

    private lateinit var loadDataListener: LoadDataListener

    internal lateinit var layoutManager: RecyclerView.LayoutManager

    internal fun initViews(rootView: View) {
        activity = getActivity() as MainActivity
        recyclerView = rootView.findViewById(R.id.recyclerView)
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadDataListener = this as LoadDataListener
        setupRecyclerView()
        //swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        recyclerView.addOnScrollListener(object: InfiniteScrollListener(layoutManager) {
            override fun onLoadMore() {
                //loadDataListener.onLoad()
            }

            override fun isDataLoading() = isLoadingMore

            override fun isNoMoreData() = isNoMoreData
        })
        scrollChangeListener()
        swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }
        refresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent){
        if(messageEvent is RefreshMainActivityFeedsEvent){
            if(isLoadFailed){
                //只有在加载失败的时候，才会相应
                Proud.handler.postDelayed(300){
                    reload()
                }
            }
        }else if(messageEvent is CleanCacheEvent){
            reload()
        }else if(messageEvent is UnknownErrorEvent){
            activity.runOnUiThread {
                logWarn(TAG,TAG)
                loadFailed(GlobalUtil.getString(R.string.unknown_error) + ": " + messageEvent.response?.status)
            }
        }
    }

    override fun loadFinished() {
        super.loadFinished()
        isLoadFailed = false
        recyclerView.visibility = View.VISIBLE
        swipeRefreshLayout.visibility = View.VISIBLE
        if(swipeRefreshLayout.isRefreshing){
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun loadFailed(msg: String?) {
        super.loadFailed(msg)
        isLoadFailed = true
        if(dataSetSize() == 0){
            if(msg == null){
                swipeRefreshLayout.visibility = View.GONE
                showBadNetworkView(View.OnClickListener {
                    val event = RefreshEvent()
                    EventBus.getDefault().post(event)
                    hideBadNetworkView()
                })
            }
        }else{
            showToast(GlobalUtil.getString(R.string.network_connect_error))
            adapter.notifyItemChanged(adapter.itemCount - 1)
        }
        swipeRefreshLayout.isRefreshing = false
    }

    private fun reload() {
        if (adapter.itemCount <= 1) {
            startLoading()
        } else {
            swipeRefreshLayout.isRefreshing = true
        }
        refresh()
    }

    fun scrollToTop() {
        if (adapter.itemCount != 0) {
            //recyclerView.smoothScrollToPosition(0)
        }
    }

    /**
     * 滑动监听
     * */
    private fun scrollChangeListener(){
        if (AndroidVersion.hasMarshmallow()) {
            recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                var event = ScrollEvent()
                if (v != null) {
                    event.view = v
                }
                event.oldScrollX = oldScrollX
                event.oldScrollY = oldScrollY
                event.scrollX = scrollX
                event.scrollY = scrollY
                EventBus.getDefault().post(event)
            }
        }
    }

    internal abstract fun setupRecyclerView()

    internal abstract fun loadItems()

    internal abstract fun refresh()

    internal abstract fun dataSetSize(): Int

    companion object {

        private const val TAG = "BaseFeedsFragment"
    }
}