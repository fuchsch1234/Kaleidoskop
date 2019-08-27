package de.fuchsch.kaleidoskop.gui.ext

import javafx.beans.binding.ListBinding
import javafx.collections.ObservableList
import tornadofx.observable

fun <T> ObservableList<T>.subtract(op: ObservableList<T>): ListBinding<T> =
    object: ListBinding<T>() {

        init {
            bind(this@subtract, op)
        }

        override fun computeValue(): ObservableList<T> = (this@subtract - op).observable()

    }