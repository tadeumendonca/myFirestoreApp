package com.mendonca.tadeu.firestoreapp.Activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.mendonca.tadeu.firestoreapp.Adapters.CommentsAdapter
import com.mendonca.tadeu.firestoreapp.Model.Comment
import com.mendonca.tadeu.firestoreapp.Model.Thought
import com.mendonca.tadeu.firestoreapp.R
import com.mendonca.tadeu.firestoreapp.Utilities.*
import kotlinx.android.synthetic.main.activity_comments.*
import java.util.*

class CommentsActivity : AppCompatActivity() {

    lateinit var thoughtDocumentId : String
    lateinit var commentsAdapter: CommentsAdapter
    val comments = arrayListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        thoughtDocumentId = intent.getStringExtra(DOCUMENT_KEY)
        commentsAdapter = CommentsAdapter(comments)
        commentListView.adapter = commentsAdapter

        val layoutManager = LinearLayoutManager(this)
        commentListView.layoutManager = layoutManager

        FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
                .document(thoughtDocumentId)
                .collection(COMMENTS_REF)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .addSnapshotListener{snapshot, exception ->
                    if(exception != null)
                        Log.e("Exception","Could not retrieve comments: $exception")
                    if(snapshot != null){
                        parseData(snapshot)
                    }
                }

    }

    fun parseData(snapshot : QuerySnapshot){
        comments.clear()
        for (document in snapshot.documents){
            val data = document.data
            val username = data!![USERNAME] as String
            val timestamp = data!![TIMESTAMP] as Date
            val commentText = data!![COMMENT_TXT] as String
            val documentId = document.id
            val newComment = Comment(username, timestamp, commentText)
            comments.add(newComment)
        }
        commentsAdapter.notifyDataSetChanged()
    }

    fun addCommentClicked(view:View){
        val commentText = enterCommentText.text.toString()
        val thoughtRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocumentId)

        FirebaseFirestore.getInstance().runTransaction{ transaction ->
                val thought = transaction.get(thoughtRef)
                val numComments = thought.getLong(NUM_COMMENTS)?.plus(1)
                transaction.update(thoughtRef, NUM_COMMENTS, numComments)
                val newCommentRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
                        .document(thoughtDocumentId).collection(COMMENTS_REF).document()

                val data = HashMap<String,Any>()
                data.put(COMMENT_TXT,commentText)
                data.put(TIMESTAMP,FieldValue.serverTimestamp())
                data.put(USERNAME,FirebaseAuth.getInstance().currentUser?.displayName.toString())
                transaction.set(newCommentRef,data)

        }
                .addOnSuccessListener {
                    enterCommentText.setText("")
                    hideKeyboard()
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception","Could not add comment: $exception")
                }
    }

    private fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
        }
    }
}
