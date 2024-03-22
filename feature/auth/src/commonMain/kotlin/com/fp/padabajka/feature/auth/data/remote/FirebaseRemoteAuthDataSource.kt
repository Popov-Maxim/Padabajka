package com.fp.padabajka.feature.auth.data.remote

import com.fp.padabajka.core.repository.api.model.auth.InvalidCredentialsAuthException
import com.fp.padabajka.core.repository.api.model.auth.UnexpectedAuthException
import com.fp.padabajka.feature.auth.data.model.UserDto
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

internal class FirebaseRemoteAuthDataSource(private val firebaseAuth: FirebaseAuth) : RemoteAuthDataSource {

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

    override suspend fun login(email: String, password: String) = mapFirebaseAuthExceptions {
        firebaseAuth.signInWithEmailAndPassword(email = email, password = password)
    }

    override suspend fun login(token: String) = mapFirebaseAuthExceptions {
        firebaseAuth.signInWithCustomToken(token)
    }

    override suspend fun register(email: String, password: String) = mapFirebaseAuthExceptions {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun logout() = mapFirebaseAuthExceptions {
        firebaseAuth.signOut()
    }

    private inline fun mapFirebaseAuthExceptions(action: () -> Unit) {
        try {
            action()
        } catch (ce: CancellationException) {
            throw ce
        } catch (fae: FirebaseAuthException) {
            throw when (fae) {
                is FirebaseAuthInvalidCredentialsException -> InvalidCredentialsAuthException
                else -> UnexpectedAuthException(fae)
            }
        }
    }
}
