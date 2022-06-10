package com.example.organizzeclone.config

import android.provider.ContactsContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ConfiguracaoFirebase {

    companion object{
        private lateinit var autenticacao: FirebaseAuth
        private lateinit var firebaseReference: DatabaseReference

        fun getFirebaseAutenticacao(): FirebaseAuth{
            if(!this::autenticacao.isInitialized){
                autenticacao = Firebase.auth
            }
            return autenticacao
        }

        fun getFirebaseDatabase(): DatabaseReference{
            if(!this::firebaseReference.isInitialized){
                firebaseReference = FirebaseDatabase.getInstance().reference
            }
            return firebaseReference
        }
    }
}