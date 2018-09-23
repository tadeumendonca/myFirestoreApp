package com.mendonca.tadeu.firestoreapp.Activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.firestore.FirebaseFirestore
import com.mendonca.tadeu.firestoreapp.R
import com.mendonca.tadeu.firestoreapp.Utilities.*
import kotlinx.android.synthetic.main.activity_update_comment.*

class UpdateCommentActivity : AppCompatActivity() {

    lateinit var thoughtDocId : String
    lateinit var  commentDocId : String
    lateinit var commentText : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_comment)

        thoughtDocId = intent.getStringExtra(THOUGHT_DOC_ID_EXTRA)
        commentDocId = intent.getStringExtra(COMMENT_DOC_ID_EXTRA)
        commentText = intent.getStringExtra(COMMENT_TEXT_EXTRA)

        updateCommentText.setText(commentText)
    }

    fun updateCommentClicked(view: View){
        FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocId)
                .collection(COMMENTS_REF).document(commentDocId)
                .update(COMMENT_TXT,updateCommentText.text.toString())
                .addOnSuccessListener {
                    hideKeyboard()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception","Could not edit comment: ${exception.localizedMessage}")
                }
    }

    private fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
        }
    }
}
