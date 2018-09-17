package com.mendonca.tadeu.firestoreapp

import java.util.*

data class Thought constructor(val username: String,
                               val timestamp: Date,
                               val thoughtTxt: String,
                               val numLikes: Int,
                               val NumComments: Int,
                               val documentId: String)