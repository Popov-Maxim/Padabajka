package com.fp.padabajka.feature.auth.data

import dev.gitlive.firebase.auth.FirebaseAuth

class RemoteAuthDataSourceImpl(private val firebaseAuth: FirebaseAuth) : RemoteAuthDataSource {

    override suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email = email, password = password)
    }

    override suspend fun login(token: String) {
        firebaseAuth.signInWithCustomToken(token)
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}
