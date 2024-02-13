package cz.pavelhanzl.bookbrowser.di

import cz.pavelhanzl.bookbrowser.data.BookApiService
import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.data.BookRepositoryImpl
import cz.pavelhanzl.bookbrowser.features.bookdetail.presentation.BookDetailViewModel
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.BookSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    //Data
    single {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApiService::class.java)
    }

    //Domain
    single<BookRepository> { BookRepositoryImpl(get()) }


    //Presentation
    viewModel { BookSearchViewModel(get()) }
    viewModel { BookDetailViewModel(get()) }
}