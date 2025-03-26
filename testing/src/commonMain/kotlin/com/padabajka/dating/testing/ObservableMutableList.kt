package com.padabajka.dating.testing

class ObservableMutableList<T>(
    private val mutableList: MutableList<T>
) : MutableList<T> by mutableList {
    fun interface Listener<T> {
        fun onAdd(element: T)
    }

    var listener: Listener<T>? = null

    override fun add(element: T): Boolean {
        listener?.onAdd(element)
        return mutableList.add(element)
    }
}

fun <T> MutableList<T>.asObservable(): ObservableMutableList<T> = ObservableMutableList(this)
