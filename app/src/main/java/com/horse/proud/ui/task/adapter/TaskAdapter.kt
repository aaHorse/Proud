package com.horse.proud.ui.task.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter
import cn.bingoogolapple.baseadapter.BGARecyclerViewHolder
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.horse.proud.R
import com.horse.proud.data.model.other.CommentItem
import com.horse.proud.data.model.task.TaskItem
import com.horse.proud.ui.task.TaskFragment
import com.horse.proud.widget.SeeMoreView

/**
 * @author liliyuan
 * @since 2020年4月30日19:00:15
 * */
class TaskAdapter(private val taskFragment:TaskFragment, private var recyclerView: RecyclerView?) : BGARecyclerViewAdapter<TaskItem>(recyclerView, R.layout.item_task) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BGARecyclerViewHolder {
        mInflater = LayoutInflater.from(taskFragment.context)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun fillData(helper: BGAViewHolderHelper, position: Int, taskItem: TaskItem) {
        helper.setText(R.id.text, taskItem.title)
        taskItem.content?.let { helper.getView<SeeMoreView>(R.id.seemore).setText(it) }
        val ninePhotoLayout = helper.getView<BGANinePhotoLayout>(R.id.npl_item_moment_photos)
        ninePhotoLayout.setDelegate(taskFragment)
        val photos = ArrayList<String>()
        photos.add(taskItem.image)
        ninePhotoLayout.data = photos


        /*
        * 嵌套类型对应的 RecyclerView
        * */
        var rv_type:RecyclerView = helper.getView(R.id.rv_type)
        rv_type.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(taskFragment.context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_type.layoutManager = linearLayoutManager
        var types = ArrayList<String>()
        types.add("类型1")
        types.add("类型2")
        rv_type.adapter = TypeAdapter(types)

        /*
        * 嵌套评论对应的 RecyclerView
        * */
        var rv_comment:RecyclerView = helper.getView(R.id.rv_comment)
        rv_comment.setHasFixedSize(true)
        rv_comment.layoutManager = LinearLayoutManager(taskFragment.context)
        var comments = ArrayList<CommentItem>()
        var item = CommentItem()
        item.name = "会飞的鱼"
        item.content = "评论111111111111111111111"
        comments.add(item)
        var item2 = CommentItem()
        item2.name = "会飞的鱼"
        item2.content = "评论222222222222222222222222222"
        comments.add(item2)
        rv_comment.adapter = CommentAdapter(comments)
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