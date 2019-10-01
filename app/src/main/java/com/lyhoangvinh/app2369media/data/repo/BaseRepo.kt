package com.lyhoangvinh.app2369media.data.repo

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



abstract class BaseRepo {

    /**
     * Excute room
    </T> */

    fun execute(action: () -> Unit) {
        Completable.fromAction {
            action.invoke()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

}


