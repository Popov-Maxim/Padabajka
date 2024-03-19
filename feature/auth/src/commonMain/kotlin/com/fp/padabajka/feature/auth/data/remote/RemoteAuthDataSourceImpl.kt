package com.fp.padabajka.feature.auth.data.remote

import com.fp.padabajka.feature.auth.data.model.UserDto
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RemoteAuthDataSourceImpl(private val firebaseAuth: FirebaseAuth) : RemoteAuthDataSource {

    override val user: Flow<UserDto?>
        get() = firebaseAuth.authStateChanged.map {
            it?.let { user ->
                UserDto(
                    id = user.uid,
                    email = user.email,
                    isEmailVerified = user.isEmailVerified
                )
            }
        }

    override suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email = email, password = password)
    }

    override suspend fun login(token: String) {
        firebaseAuth.signInWithCustomToken(token)
    }

    override suspend fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}
