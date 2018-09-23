package com.mendonca.tadeu.firestoreapp.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mendonca.tadeu.firestoreapp.Utilities.DATE_CREATED
import com.mendonca.tadeu.firestoreapp.R
import com.mendonca.tadeu.firestoreapp.Utilities.USERNAME
import com.mendonca.tadeu.firestoreapp.Utilities.USERS_REF
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        auth = FirebaseAuth.getInstance()
    }

    fun createCreateClicked(view : View){
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()
        val username = createUsernameText.text.toString()

        auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener { result ->
                    // user created
                    val changeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()
                    result.user.updateProfile(changeRequest)
                            .addOnFailureListener{exception ->
                                Log.e("Exception","Could not update display name: ${exception.localizedMessage}")
                            }
                    val data = HashMap<String,Any>()
                    data.put(USERNAME,username)
                    data.put(DATE_CREATED,FieldValue.serverTimestamp())

                    FirebaseFirestore.getInstance().collection(USERS_REF).document(result.user.uid)
                            .set(data)
                            .addOnSuccessListener {
                                finish()
                            }
                            .addOnFailureListener{exception ->
                                Log.e("Exception","Could not add user document: ${exception.localizedMessage}")
                            }

                }
                .addOnFailureListener{exception ->
                    Log.e("Exception","Could not create user: ${exception.localizedMessage}")
                }
    }

    fun createCancelClicked(view: View){
        finish()
    }
}
