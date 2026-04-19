package com.padabajka.dating.feature.infra.di

import com.padabajka.dating.feature.infra.FrameMetricsAggregator
import com.padabajka.dating.feature.infra.FrameObserver
import com.padabajka.dating.feature.infra.FrameStorage
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val infraDiModule = module {
    singleOf(::FrameObserver)
    singleOf(::FrameStorage)

    factoryOf(::FrameMetricsAggregator)
}
