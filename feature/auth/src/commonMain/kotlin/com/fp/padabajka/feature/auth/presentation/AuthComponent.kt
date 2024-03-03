package com.fp.padabajka.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.feature.auth.domain.LogInUseCase
import com.fp.padabajka.feature.auth.domain.RegisterUseCase
import com.fp.padabajka.feature.auth.domain.ValidateEmailUseCase
import com.fp.padabajka.feature.auth.domain.ValidatePasswordsUseCase

class AuthComponent(
    private val logInUseCase: Factory<LogInUseCase>,
    private val registerUseCase: Factory<RegisterUseCase>,
    private val validateEmailUseCase: Factory<ValidateEmailUseCase>,
    private val validatePasswordsUseCase: Factory<ValidatePasswordsUseCase>,
    context: ComponentContext
) :
    BaseComponent<AuthState>(context, LoggingIn())
