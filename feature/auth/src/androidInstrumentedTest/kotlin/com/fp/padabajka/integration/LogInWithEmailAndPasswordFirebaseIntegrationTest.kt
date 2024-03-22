package com.fp.padabajka.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fp.padabajka.auth.domain.AuthStateProvider
import com.fp.padabajka.auth.domain.LogInWithEmailAndPasswordUseCase
import com.fp.padabajka.auth.domain.LogOutUseCase
import com.fp.padabajka.core.repository.api.model.auth.LoggedIn
import com.fp.padabajka.core.repository.api.model.auth.LoggedOut
import com.fp.padabajka.core.repository.api.model.auth.UserId
import com.fp.padabajka.feature.auth.data.AuthRepositoryImpl
import com.fp.padabajka.feature.auth.data.remote.FirebaseRemoteAuthDataSource
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogInWithEmailAndPasswordFirebaseIntegrationTest {

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    private fun initFirebase() {
        val googleJson = appContext.assets.open("google-services.json")
            .use { file ->
                JSONObject(String(file.readBytes()))
            }

        val options = FirebaseOptions.Builder()
            .setApiKey(
                googleJson.getJSONArray("client")
                    .getJSONObject(0)
                    .getJSONArray("api_key")
                    .getJSONObject(0)
                    .getString("current_key")
            )
            .setApplicationId(
                googleJson.getJSONArray("client")
                    .getJSONObject(0)
                    .getJSONObject("client_info")
                    .getString("mobilesdk_app_id")
            )
            .setProjectId(
                googleJson.getJSONObject("project_info").getString("project_id")
            )
            .setStorageBucket(
                googleJson.getJSONObject("project_info").getString("storage_bucket")
            )
            .build()

        FirebaseApp.initializeApp(appContext, options)
    }

    private data class LoginLogoutAndAuthStateProvider(
        val logInWithEmailAndPasswordUseCase: LogInWithEmailAndPasswordUseCase,
        val logOutUseCase: LogOutUseCase,
        val authStateProvider: AuthStateProvider
    )

    private fun getLoginLogoutAndAuthStateProvider(): LoginLogoutAndAuthStateProvider {
        initFirebase()
        val dataSource = FirebaseRemoteAuthDataSource(Firebase.auth)
        val repository = AuthRepositoryImpl(dataSource)
        return LoginLogoutAndAuthStateProvider(
            LogInWithEmailAndPasswordUseCase(repository),
            LogOutUseCase(repository),
            AuthStateProvider(repository)
        )
    }

    @Test
    fun loginWithValidEmailAndPassword() = runBlocking {
        val (login, logout, state) = getLoginLogoutAndAuthStateProvider()

        val authStateLog = async {
            withTimeout(20_000) {
                state.authState.take(3).toList()
            }
        }

        login.invoke("validTest@email.com", "Password1")
        logout.invoke()

        val (preLogIn, postLogIn, postLogOut) = authStateLog.await()

        assertEquals(LoggedOut, preLogIn)
        assertEquals(LoggedIn(UserId("VvPQVZ6XO5gM0j70TGPXu4vxSpq1")), postLogIn)
        assertEquals(LoggedOut, postLogOut)
    }
}
