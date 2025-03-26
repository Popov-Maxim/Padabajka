package com.padabajka.dating.core.domain.di

import com.padabajka.dating.core.domain.IpAddressProvider
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformDomainDiModule: Module = module {
    single<IpAddressProvider> {
        IpAddressProvider()
    }
}
