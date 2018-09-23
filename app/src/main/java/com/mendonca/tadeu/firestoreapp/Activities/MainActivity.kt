package com.mendonca.tadeu.firestoreapp.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.mendonca.tadeu.firestoreapp.*
import com.mendonca.tadeu.firestoreapp.Adapters.ThoughtsAdapter
import com.mendonca.tadeu.firestoreapp.Model.Thought
import com.mendonca.tadeu.firestoreapp.Utilities.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var selectedCategory = FUNNY
    lateinit var thoughtsAdapter: ThoughtsAdapter
    val thoughts = arrayListOf<Thought>()
    val thoughtsCollectionRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
    lateinit var thoughtsListener : ListenerRegistration
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val addThoughtIntent = Intent(this, AddThoughtActivity::class.java)
            startActivity(addThoughtIntent)
        }

        thoughtsAdapter = ThoughtsAdapter(thoughts) { thought ->
            val commentsActivity = Intent(this,CommentsActivity::class.java)
            commentsActivity.putExtra(DOCUMENT_KEY,thought.documentId)
            startActivity(commentsActivity)
        }
        thoughtListView.adapter = thoughtsAdapter
        val layoutManager = LinearLayoutManager(this)
        thoughtListView.layoutManager = layoutManager

        auth = FirebaseAuth.getInstance()

        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.getItem(0)
        if( auth.currentUser == null){
            menuItem.title = "Login"
        }
        else{
            menuItem.title = "Logout"
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_login){
            if(auth.currentUser == null){
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            } else {
                auth.signOut()
                updateUI()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateUI(){
        if(auth.currentUser == null){
            mainCrazyButton.isEnabled = false
            mainPopularButton.isEnabled = false
            mainFunnyButton.isEnabled = false
            mainSeriousButton.isEnabled = false
            fab.isEnabled = false
            thoughts.clear()
            thoughtsAdapter.notifyDataSetChanged()
        } else {
            mainCrazyButton.isEnabled = true
            mainPopularButton.isEnabled = true
            mainFunnyButton.isEnabled = true
            mainSeriousButton.isEnabled = true
            fab.isEnabled = true
            setListener()
        }
    }

    fun mainFunnyClicked(view: View) {
        if(selectedCategory == FUNNY){
            mainFunnyButton.isChecked = true
            return
        }
        mainSeriousButton.isChecked = false
        mainCrazyButton.isChecked = false
        mainPopularButton.isChecked = false
        selectedCategory = FUNNY

        // Reset Documents Listener
        thoughtsListener.remove()
        setListener()
    }

    fun mainSeriousClicked(view: View) {
        if(selectedCategory == SERIOUS){
            mainSeriousButton.isChecked = true
            return
        }
        mainFunnyButton.isChecked = false
        mainCrazyButton.isChecked = false
        mainPopularButton.isChecked = false
        selectedCategory = SERIOUS

        // Reset Documents Listener
        thoughtsListener.remove()
        setListener()
    }

    fun mainCrazyClicked(view: View){
        if(selectedCategory == CRAZY){
            mainCrazyButton.isChecked = true
            return
        }
        mainSeriousButton.isChecked = false
        mainFunnyButton.isChecked = false
        mainPopularButton.isChecked = false
        selectedCategory = CRAZY

        // Reset Documents Listener
        thoughtsListener.remove()
        setListener()
    }

    fun mainPopularClicked(view: View){
        if(selectedCategory == POPULAR){
            mainPopularButton.isChecked = true
            return
        }
        mainSeriousButton.isChecked = false
        mainFunnyButton.isChecked = false
        mainCrazyButton.isChecked = false
        selectedCategory = POPULAR

        // Reset Documents Listener
        thoughtsListener.remove()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    fun setListener(){
        if(selectedCategory == POPULAR){
            // show popular documents
            thoughtsListener = thoughtsCollectionRef
                    .orderBy(NUM_LIKES, Query.Direction.DESCENDING)
                    .addSnapshotListener(this){ snapshot, exception ->
                        if(exception != null)
                            Log.e("Exception","Could not retrieve documents: $exception")
                        if(snapshot != null){
                            parseData(snapshot)
                        }
                    }
        } else {
            thoughtsListener = thoughtsCollectionRef
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .whereEqualTo(CATEGORY, selectedCategory)
                .addSnapshotListener(this){ snapshot, exception ->
                    if(exception != null)
                        Log.e("Exception","Could not retrieve documents: $exception")
                    if(snapshot != null){
                        parseData(snapshot)
                    }
                }
        }

    }

    fun parseData(snapshot : QuerySnapshot){
        thoughts.clear()
        for (document in snapshot.documents){
            val data = document.data
            val name = data!![USERNAME] as String
            val timestamp = data!![TIMESTAMP] as Date
            val thoughtTxt = data!![THOUGHT_TXT] as String
            val numLikes = data!![NUM_LIKES] as Long
            val numComments = data!![NUM_COMMENTS] as Long
            val documentId = document.id
            val newThought = Thought(name, timestamp, thoughtTxt, numLikes.toInt(), numComments.toInt(), documentId)
            thoughts.add(newThought)
        }
        thoughtsAdapter.notifyDataSetChanged()
    }

}