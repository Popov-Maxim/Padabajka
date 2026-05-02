package com.padabajka.dating.feature.auth.data.remote

import com.padabajka.dating.core.repository.api.exception.AuthCredentialError
import com.padabajka.dating.core.repository.api.exception.EmailLinkAuthException
import com.padabajka.dating.core.repository.api.model.auth.InvalidCredentialsAuthException
import com.padabajka.dating.core.repository.api.model.auth.UnexpectedAuthException
import com.padabajka.dating.feature.auth.data.model.UserDto
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.FirebaseTooManyRequestsException
import dev.gitlive.firebase.auth.ActionCodeSettings
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthActionCodeException
import dev.gitlive.firebase.auth.FirebaseAuthEmailException
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthMultiFactorException
import dev.gitlive.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import dev.gitlive.firebase.auth.FirebaseAuthWeakPasswordException
import dev.gitlive.firebase.auth.FirebaseAuthWebException
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlin.coroutines.cancellation.CancellationException

internal class FirebaseRemoteAuthDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val debugAuthApi: DebugAuthApi
) : RemoteAuthDataSource {

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

    override suspend fun loginWithoutPassword(email: String, actionCodeSettings: ActionCodeSettings) {
        _loginWithoutPassword(email, actionCodeSettings)
    }

    @Throws(EmailLinkAuthException.InvalidLink::class, AuthCredentialError::class)
    override suspend fun signInWithEmailLink(email: String, link: String) {
        if (firebaseAuth.isSignInWithEmailLink(link) && false) {
            try {
                firebaseAuth.signInWithEmailLink(email, link)
            } catch (e: FirebaseException) {
                throw mapFirebaseAuthError(e)
            }
        } else {
            throw EmailLinkAuthException.InvalidLink()
        }
    }

    override suspend fun login(token: String) = mapFirebaseAuthExceptions {
        firebaseAuth.signInWithCustomToken(token)
    }

    override suspend fun loginInWithCredential(credential: AuthCredential) {
        try {
            firebaseAuth.signInWithCredential(credential)
        } catch (e: FirebaseException) {
            throw mapFirebaseAuthError(e)
        }
    }

    override suspend fun register(email: String, password: String) = mapFirebaseAuthExceptions {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun logout() = mapFirebaseAuthExceptions {
        firebaseAuth.signOut()
    }

    override suspend fun loginDebug(uuid: String) {
        val token = debugAuthApi.login(uuid)
        firebaseAuth.signInWithCustomToken(token)
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
            isEmailVerified = isEmailVerified
        )
    }

    private fun mapFirebaseAuthError(e: FirebaseException): AuthCredentialError {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException,
            is FirebaseAuthWeakPasswordException,
            is FirebaseAuthUserCollisionException,
            is FirebaseAuthRecentLoginRequiredException,
            is FirebaseAuthEmailException,
            is FirebaseAuthMultiFactorException,
            is FirebaseAuthActionCodeException ->
                AuthCredentialError.InvalidCredentials()

            is FirebaseAuthInvalidUserException ->
                AuthCredentialError.UserNotFound()

            is FirebaseNetworkException ->
                AuthCredentialError.NetworkError()

            is FirebaseTooManyRequestsException ->
                AuthCredentialError.TooManyRequests()

            is FirebaseAuthWebException ->
                AuthCredentialError.Unknown(e)

            else ->
                AuthCredentialError.Unknown(e)
        }
    }
}

// TODO(P10): delete after updating gitlive firebase
@Suppress("FunctionName")
expect suspend fun _loginWithoutPassword(email: String, actionCodeSettings: ActionCodeSettings)
