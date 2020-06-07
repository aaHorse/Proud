package com.horse.proud.ui.lost.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter
import cn.bingoogolapple.baseadapter.BGARecyclerViewHolder
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.core.proud.model.lost.LostItem
import com.horse.core.proud.model.other.CommentItem
import com.horse.proud.event.*
import com.horse.proud.ui.common.ViewLocationActivity
import com.horse.proud.ui.lost.FoundActivity
import com.horse.proud.ui.lost.LostActivity
import com.horse.proud.ui.lost.LostFragment
import com.horse.proud.ui.setting.OverViewPublishedActivity
import com.horse.proud.util.DateUtil
import com.horse.proud.widget.SeeMoreView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import kotlinx.android.synthetic.main.item_lost.view.*
import org.greenrobot.eventbus.EventBus

/**
 * 任务列表的 RecyclerView 适配器。
 *
 * @author liliyuan
 * @since 2020年4月25日15:36:50
 * */
class LostAdapter(private val fragment: LostFragment, private var recyclerView: RecyclerView?) : BGARecyclerViewAdapter<LostItem>(recyclerView, R.layout.item_lost) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BGARecyclerViewHolder {
        mInflater = LayoutInflater.from(fragment.context)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun fillData(helper: BGAViewHolderHelper, position: Int, item: LostItem) {

        var adapter:CommentAdapter ?= null

        helper.getView<LinearLayout>(R.id.ll).setOnClickListener {
            val event = ClickEvent()
            EventBus.getDefault().post(event)
        }

        Glide.with(fragment.requireContext()).load(R.drawable.avatar_default)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(helper.getImageView(R.id.avatar))

        helper.getImageView(R.id.avatar).setOnClickListener {
            OverViewPublishedActivity.actionStart(fragment.activity,item.userId)
        }

        if(!item.title.isNullOrEmpty()){
            helper.setText(R.id.text, item.title)
        }

        val done = helper.getTextView(R.id.done)
        if(item.isLost == 0){
            done.text = "寻物启事"
            done.setTextColor(Color.parseColor("#F4606C"))
        }else{
            done.text = "失物招领"
            done.setTextColor(Color.parseColor("#19CAAD"))
        }

        if(fragment.activity.flag!=0&&fragment.activity.userID == Proud.register.id){
            with(helper.getImageView(R.id.more)){
                visibility = View.VISIBLE
                setOnClickListener {
                    val items = arrayOf(
                        GlobalUtil.getString(R.string.edit_personal),
                        GlobalUtil.getString(R.string.delete_personal))
                    QMUIDialog.MenuDialogBuilder(fragment.context)
                        .addItems(items) { dialog, which ->
                            dialog.dismiss()
                            when(which){
                                0 -> {
                                    showToast("编辑")
                                    if(item.isLost == 0){
                                        LostActivity.actionStart(fragment.activity,item)
                                    }else{
                                        FoundActivity.actionStart(fragment.activity,item)
                                    }
                                }
                                1 -> {
                                    showToast("删除")
                                    val event = DeleteEvent()
                                    event.id = item.id
                                    event.category = Const.Like.LOST
                                    EventBus.getDefault().post(event)
                                }
                            }
                        }.create(R.style.MenuDialog).show()
                }
            }
        }

        if(!item.time.isNullOrEmpty()){
            helper.getTextView(R.id.publish_time).text = item.time
        }

        if(!item.content.isNullOrEmpty()){
            helper.getView<SeeMoreView>(R.id.seemore).setText(item.content)
        }

        item.image?.let {
            if(item.image.isNotEmpty()){
                val ninePhotoLayout = helper.getView<BGANinePhotoLayout>(R.id.npl_item_moment_photos)
                ninePhotoLayout.setDelegate(fragment)
                ninePhotoLayout.data = item.images
                logWarn(TAG,"${item.image}")
            }
        }

        helper.getView<LinearLayout>(R.id.ll_local).setOnClickListener {
            if(item.location.isEmpty()){
                showToast("该任务未标记地点")
            }else{
                val locations = item.location.split(",").toMutableList()
                locations -= ""
                if(locations.size == 2){
                    val latitude:Double = locations[0].toDouble()
                    val longitude:Double = locations[1].toDouble()
                    ViewLocationActivity.actionStartForResult(latitude,longitude,fragment.activity,1)
                }else{
                    showToast("该地点暂时无法查看")
                }

            }
        }

        helper.getTextView(R.id.tv_comment).text = "${item.comment}"

        helper.getTextView(R.id.tv_like).text = "${item.thumbUp}"

        helper.getView<CheckBox>(R.id.iv_like).setOnClickListener {
            if(it.iv_like.isChecked){
                showToast("！ ！~ 赞 ~ ！ ！")
                helper.getTextView(R.id.tv_like).text = "${++item.thumbUp}"
                val evnet = LikeEvent()
                evnet.category = Const.Like.LOST
                evnet.id = item.id
                EventBus.getDefault().post(evnet)
            }else{
                helper.getTextView(R.id.tv_like).text = "${--item.thumbUp}"
                showToast("取消点赞")
            }
        }

        /*
        * 嵌套类型对应的 RecyclerView
        * */
        if(!item.label.isNullOrEmpty()){
            var types:List<String> = item.label.split(",")
            types -= ""
            if(types.isNotEmpty()){
                var rvType:RecyclerView = helper.getView(R.id.rv_type)
                rvType.setHasFixedSize(true)
                var linearLayoutManager = LinearLayoutManager(fragment.context)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                rvType.layoutManager = linearLayoutManager
                rvType.adapter = TypeAdapter(types)
            }
        }

        /*
               * 嵌套评论对应的 RecyclerView
               * */
        item.comments?.let {
            val rvComment:RecyclerView = helper.getView(R.id.rv_comment)
            rvComment.setHasFixedSize(true)
            rvComment.layoutManager = LinearLayoutManager(fragment.context)
            if(it.commentList == null){
                it.commentList = ArrayList()
            }
            adapter = CommentAdapter(it.commentList!!)
            rvComment.adapter = adapter
            helper.getTextView(R.id.tv_comment).text = "${it.commentList!!.size}"
        }

        helper.getView<Button>(R.id.send).setOnClickListener {
            if(Proud.loginState != Const.Auth.COMFIR){
                showToast(GlobalUtil.getString(R.string.visitor_reminder))
            }else{
                val content:String = helper.getView<EditText>(R.id.et_comment).text.toString()
                if (!content.isBlank()){
                    val comment = CommentItem()
                    comment.id = "1"
                    comment.userId = Proud.register.id
                    comment.content = content
                    comment.time = DateUtil.nowDateTime
                    comment.itemId = item.id
                    comment.name = Proud.register.name

                    val event = CommentEvent()
                    event.category = Const.Like.LOST
                    event.comment = comment
                    EventBus.getDefault().post(event)

                    item.comments!!.commentList!!.add(comment)
                    if(adapter!=null){
                        helper.getView<EditText>(R.id.et_comment).setText("")
                        notifyItemChanged(position)
                    }
                }else{
                    showToast("评论不能为空")
                }
            }
        }
    }

    private class TypeAdapter(items:List<String>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var items:List<String> = items

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = mInflater.inflate(R.layout.item_type,parent,false)
            return TypeItemViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as TypeItemViewHolder
            holder.textView.text = items[position]
        }

        internal class TypeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textView: TextView = itemView.findViewById(R.id.tv_type)
        }

    }

    private class CommentAdapter(items: ArrayList<CommentItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var items: ArrayList<CommentItem> = items

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = mInflater.inflate(R.layout.item_comment,parent,false)
            return CommentItemViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as CommentItemViewHolder
            holder.comment_name.text = "${items[position].name}"
            //holder.comment_name.text = "${items[position].userId}"
            holder.comment_content.text = items[position].content
            holder.comment_name.setOnClickListener {
                val event = CommentToOverViewEvent()
                event.category = Const.Like.LOST
                event.userId = items[position].userId
                EventBus.getDefault().post(event)
            }
            holder.linearLayout.setOnClickListener {
                val event = ClickEvent()
                EventBus.getDefault().post(event)
            }
        }

        internal class CommentItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var comment_name: TextView = itemView.findViewById(R.id.comment_name)
            var comment_content: TextView = itemView.findViewById(R.id.comment_content)
            val linearLayout:LinearLayout = itemView.findViewById(R.id.comment_ll)
        }

    }

    companion object{

        lateinit var mInflater:LayoutInflater

        private const val TAG = "LostAdapter"

    }

}