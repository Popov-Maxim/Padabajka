package com.fp.padabajka.feature.ads.di

import com.fp.padabajka.core.repository.api.NativeAdRepository
import com.fp.padabajka.feature.ads.data.NativeAdRepositoryImpl
import com.fp.padabajka.feature.ads.data.fake.FakeNativeAdLoader
import com.fp.padabajka.feature.ads.data.source.CompositeListener
import com.fp.padabajka.feature.ads.data.source.NativeAdRemoteDataSource
import com.fp.padabajka.feature.ads.data.source.NativeAdRemoteDataSourceImpl
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
