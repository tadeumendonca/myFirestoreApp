package com.mendonca.tadeu.firestoreapp.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mendonca.tadeu.firestoreapp.*
import com.mendonca.tadeu.firestoreapp.Utilities.*
import kotlinx.android.synthetic.main.activity_add_thought.*

class AddThoughtActivity : AppCompatActivity() {

    private var selectedCategory = FUNNY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_thought)
    }

    fun addFunnyClicked(view: View) {
        if(selectedCategory == FUNNY){
            addFunnyButton.isChecked = true
            return
        }
        addSeriousButton.isChecked = false
        addCrazyButton.isChecked = false
        selectedCategory = FUNNY
    }

    fun addSeriousClicked(view: View) {
        if(selectedCategory == SERIOUS){
            addSeriousButton.isChecked = true
            return
        }
        addFunnyButton.isChecked = false
        addCrazyButton.isChecked = false
        selectedCategory = SERIOUS
    }

    fun addCrazyClicked(view: View){
        if(selectedCategory == CRAZY){
            addCrazyButton.isChecked = true
            return
        }
        addSeriousButton.isChecked = false
        addFunnyButton.isChecked = false
        selectedCategory = CRAZY
    }

    fun addPostClicked(view: View){
        val data = HashMap<String,Any>()
        data.put(CATEGORY,selectedCategory)
        data.put(NUM_COMMENTS,0)
        data.put(NUM_LIKES,0)
        data.put(THOUGHT_TXT,addThoughtText.text.toString())
        data.put(TIMESTAMP, FieldValue.serverTimestamp())
        data.put(USERNAME, FirebaseAuth.getInstance().currentUser?.displayName.toString())
        FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
                .add(data)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {
                    exception -> Log.e("Exception","Could not add post: $exception")
                }
    }


}
