package com.fp.padabajka.feature.swiper.presentation.screen

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

class SafePersistentList<T>(
    private val persistentList: PersistentList<T>,
    private val listForRemove: PersistentList<T>
) : PersistentList<T> by persistentList {

    override fun add(element: T): SafePersistentList<T> {
        return persistentList.add(element).toSafe(listForRemove)
    }

    override fun remove(element: T): SafePersistentList<T> {
        val index = indexOf(element)

        val newListForRemove = listForRemove
            .takeIf { index == 0 }?.add(element)
            ?: listForRemove

        return persistentList.remove(element).toSafe(newListForRemove)
    }

    fun getResult(): PersistentList<T> {
        return persistentList.addAll(0, listForRemove)
    }
}

fun <T> PersistentList<T>.toSafe(
    listForRemove: PersistentList<T> = persistentListOf()
): SafePersistentList<T> = SafePersistentList(this, listForRemove)
