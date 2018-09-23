package com.mendonca.tadeu.firestoreapp.Adapters

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mendonca.tadeu.firestoreapp.Model.Comment
import com.mendonca.tadeu.firestoreapp.R
import java.text.SimpleDateFormat
import java.util.*

class CommentsAdapter (val comments : ArrayList<Comment>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>(){

    override fun getItemCount(): Int {
        return comments.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindComment(comments[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_list_view, parent,false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val username = itemView.findViewById<TextView>(R.id.commentListUsername)
        val timestamp = itemView.findViewById<TextView>(R.id.commentListTimestamp)
        val commentText = itemView.findViewById<TextView>(R.id.commentlListCommentText)

        fun bindComment(comment: Comment){
            username?.text = comment.username
            commentText?.text = comment.commentText

            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(comment.timestamp)
            timestamp?.text = dateString

        }
    }
}