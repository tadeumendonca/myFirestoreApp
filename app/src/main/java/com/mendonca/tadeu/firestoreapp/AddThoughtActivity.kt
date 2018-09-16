package com.mendonca.tadeu.firestoreapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
        data.put("category",selectedCategory)
        data.put("numComments",0)
        data.put("numLikes",0)
        data.put("thoughtTxt",addThoughtText.text.toString())
        data.put("timestamp", FieldValue.serverTimestamp())
        data.put("username", addUsernameText.text.toString())
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
