package com.padabajka.dating.di

import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.DeeplinkHandler
import com.padabajka.dating.deeplink.DeeplinkHandlerImpl
import com.padabajka.dating.deeplink.DeeplinkParser
import com.padabajka.dating.session.LocalUserSessionDataSource
import com.padabajka.dating.session.UserSessionManager
import com.padabajka.dating.session.UserSessionOwnerDto
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedDiModule = module {
    factoryOf(::DeeplinkParser)
    singleOf(::DeeplinkHandlerImpl) {
        bind<DeeplinkHandler>()
    }
    single {
        LocalUserSessionDataSource(
            DataStoreUtils.create(
                dbName = "user_session_owner_datastore",
                delegate = UserSessionOwnerDto.serializer(),
                default = UserSessionOwnerDto()
            )
        )
    }
    singleOf(::UserSessionManager)
}
