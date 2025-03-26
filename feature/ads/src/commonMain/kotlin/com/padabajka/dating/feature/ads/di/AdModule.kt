package com.padabajka.dating.feature.ads.di

import com.padabajka.dating.core.repository.api.NativeAdRepository
import com.padabajka.dating.feature.ads.data.NativeAdRepositoryImpl
import com.padabajka.dating.feature.ads.data.fake.FakeNativeAdLoader
import com.padabajka.dating.feature.ads.data.source.CompositeListener
import com.padabajka.dating.feature.ads.data.source.NativeAdRemoteDataSource
import com.padabajka.dating.feature.ads.data.source.NativeAdRemoteDataSourceImpl
import org.koin.dsl.module

private val dataModule = module {
    single<NativeAdRepository> {
        NativeAdRepositoryImpl(
            scope = get(),
            profileRepository = get(),
            adRemoteDataSource = get()
        )
    }

    factory<NativeAdRemoteDataSource> {
        NativeAdRemoteDataSourceImpl(
            nativeAdLoader = FakeNativeAdLoader(), // get(),
            compositeListener = get()
        )
    }

    factory<CompositeListener> {
        CompositeListener()
    }
}

val adModules = arrayOf(dataModule)
