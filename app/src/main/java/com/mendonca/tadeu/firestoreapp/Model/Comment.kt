package com.mendonca.tadeu.firestoreapp.Model

import java.util.*

class Comment constructor(val username:String, val timestamp: Date, val commentText: String,
                          val documentId: String, val userId: String)