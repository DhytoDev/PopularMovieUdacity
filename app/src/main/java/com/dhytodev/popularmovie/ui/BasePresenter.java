package com.dhytodev.popularmovie.ui;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by izadalab on 7/8/17.
 */

public class BasePresenter {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable() ;

    protected void detachView() {
        compositeDisposable.dispose();
    }

    protected void attachView() {
        compositeDisposable = new CompositeDisposable();
    }
}
