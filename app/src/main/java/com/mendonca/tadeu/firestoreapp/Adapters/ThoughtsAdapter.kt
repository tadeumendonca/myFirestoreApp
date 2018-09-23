package com.mendonca.tadeu.firestoreapp.Adapters

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.mendonca.tadeu.firestoreapp.Model.Thought
import com.mendonca.tadeu.firestoreapp.R
import com.mendonca.tadeu.firestoreapp.Utilities.NUM_LIKES
import com.mendonca.tadeu.firestoreapp.Utilities.THOUGHTS_REF
import java.text.SimpleDateFormat
import java.util.*

class ThoughtsAdapter(val thoughts : ArrayList<Thought>, val itemClick : (Thought) -> Unit) : RecyclerView.Adapter<ThoughtsAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return thoughts.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindThought(thoughts[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.thought_list_view, parent,false)
        return ViewHolder(view, itemClick)
    }

    inner class ViewHolder(itemView : View, val itemClick : (Thought) -> Unit) : RecyclerView.ViewHolder(itemView){
        val username = itemView.findViewById<TextView>(R.id.listViewUsername)
        val timestamp = itemView.findViewById<TextView>(R.id.listViewTimestamp)
        val thoughtTxt = itemView.findViewById<TextView>(R.id.listViewThoughtTxt)
        val numLikes = itemView.findViewById<TextView>(R.id.listViewNumLikesLabel)
        val likesImage = itemView.findViewById<AppCompatImageView>(R.id.listViewLikesImage)
        val numComments = itemView.findViewById<TextView>(R.id.numCommentsLabel)

        fun bindThought(thought: Thought){
            username?.text = thought.username
            thoughtTxt?.text = thought.thoughtTxt
            numLikes?.text = thought.numLikes.toString()
            numComments?.text = thought.NumComments.toString()
            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(thought.timestamp)
            timestamp?.text = dateString
            itemView.setOnClickListener{itemClick(thought)}
            likesImage?.setOnClickListener{
                FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thought.documentId)
                        .update(NUM_LIKES,thought.numLikes+1)
            }

        }
    }
}