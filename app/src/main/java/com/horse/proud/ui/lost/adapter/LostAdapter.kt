package com.horse.proud.ui.lost.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter
import cn.bingoogolapple.baseadapter.BGARecyclerViewHolder
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.horse.proud.R
import com.horse.proud.data.model.lost.LostItem
import com.horse.proud.data.model.other.CommentItem
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.lost.LostFragment
import com.horse.proud.widget.SeeMoreView

/**
 * 任务列表的 RecyclerView 适配器。
 *
 * @author liliyuan
 * @since 2020年4月25日15:36:50
 * */
class LostAdapter(private val lostFragment:LostFragment, private var recyclerView: RecyclerView?) : BGARecyclerViewAdapter<LostItem>(recyclerView, R.layout.item_lost) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BGARecyclerViewHolder {
        mInflater = LayoutInflater.from(lostFragment.context)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun fillData(helper: BGAViewHolderHelper, position: Int, taskItem: LostItem) {
        helper.setText(R.id.text, taskItem.name)
        taskItem.content?.let { helper.getView<SeeMoreView>(R.id.seemore).setText(it) }
        val ninePhotoLayout = helper.getView<BGANinePhotoLayout>(R.id.npl_item_moment_photos)
        ninePhotoLayout.setDelegate(lostFragment)
        ninePhotoLayout.data = taskItem.photos


        /*
        * 嵌套类型对应的 RecyclerView
        * */
        var rv_type:RecyclerView = helper.getView(R.id.rv_type)
        rv_type.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(lostFragment.context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_type.layoutManager = linearLayoutManager
        rv_type.adapter = TypeAdapter(taskItem.type)

        /*
        * 嵌套评论对应的 RecyclerView
        * */
        var rv_comment:RecyclerView = helper.getView(R.id.rv_comment)
        rv_comment.setHasFixedSize(true)
        rv_comment.layoutManager = LinearLayoutManager(lostFragment.context)
        rv_comment.adapter = CommentAdapter(taskItem.comments)
    }

    private class TypeAdapter(items:ArrayList<String>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var items:ArrayList<String> = items

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
            holder.comment_name.text = items[position].name
            holder.comment_content.text = items[position].content
        }

        internal class CommentItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var comment_name: TextView = itemView.findViewById(R.id.comment_name)
            var comment_content: TextView = itemView.findViewById(R.id.comment_content)
        }

    }

    companion object{

        lateinit var mInflater:LayoutInflater

    }

}