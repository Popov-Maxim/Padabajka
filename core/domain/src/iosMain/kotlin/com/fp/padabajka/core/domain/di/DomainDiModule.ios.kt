package com.fp.padabajka.core.domain.di

import com.fp.padabajka.core.domain.IpAddressProvider
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformDomainDiModule: Module = module {
    single<IpAddressProvider> {
        IpAddressProvider()
    }
}
