package com.lyhoangvinh.app2369media.ui.base.interfaces

import com.lyhoangvinh.app2369media.data.entities.Entities
import io.reactivex.annotations.NonNull

interface PlainEntitiesPagingConsumer<E,T : Entities<E>> {
    fun accept(@NonNull t: List<E>)
}