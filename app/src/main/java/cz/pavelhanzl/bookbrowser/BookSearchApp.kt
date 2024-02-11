package cz.pavelhanzl.bookbrowser

import android.app.Application
import cz.pavelhanzl.bookbrowser.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BookSearchApp : Application(){

    // Start Koin DI
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookSearchApp)
            modules(appModule)
        }
    }
}