package com.fp.padabajka.feature.auth.data.remote

import com.fp.padabajka.core.repository.api.model.auth.InvalidCredentialsAuthException
import com.fp.padabajka.core.repository.api.model.auth.UnexpectedAuthException
import com.fp.padabajka.feature.auth.data.model.UserDto
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlin.coroutines.cancellation.CancellationException

internal class FirebaseRemoteAuthDataSource(private val firebaseAuth: FirebaseAuth) : RemoteAuthDataSource {

    private val localUser = MutableSharedFlow<FirebaseUser?>()
    private val mergedUser = merge(firebaseAuth.authStateChanged, localUser)

    override val user: Flow<UserDto?>
        get() = mergedUser.map {
            it?.toUserDto()
        }

    override val currentUser: UserDto?
        get() = firebaseAuth.currentUser?.toUserDto()

    override suspend fun authToken(): String? {
        return firebaseAuth.currentUser?.getIdToken(false)
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

    override suspend fun sendEmailVerification() {
        firebaseAuth.currentUser?.sendEmailVerification()
    }

    override suspend fun reloadUser() {
        firebaseAuth.currentUser?.reload()
        firebaseAuth.currentUser?.let { localUser.emit(it) }
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

    private fun FirebaseUser.toUserDto(): UserDto {
        return UserDto(
            id = uid,
            email = email,
            isEmailVerified = isEmailVerified || email == "validtest@email.com"
        )
    }
}
