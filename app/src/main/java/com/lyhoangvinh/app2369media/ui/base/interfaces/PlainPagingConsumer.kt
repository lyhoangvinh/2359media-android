package com.lyhoangvinh.app2369media.ui.base.interfaces

import io.reactivex.annotations.NonNull

interface PlainPagingConsumer<T> {
    fun accept(@NonNull t: List<T>)
}