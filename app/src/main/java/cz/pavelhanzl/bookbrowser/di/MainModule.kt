package cz.pavelhanzl.bookbrowser.di

import BooksPagingSource
import cz.pavelhanzl.bookbrowser.data.BookApiService
import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.data.BookRepositoryImpl
import cz.pavelhanzl.bookbrowser.features.booksearch.domain.GetBookListByAuthorUseCase
import cz.pavelhanzl.bookbrowser.features.booksearch.domain.GetBookListByAuthorUseCaseImpl
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
    single<BookRepository> { BookRepositoryImpl(dataSource = get()) }
    single<BooksPagingSource> { BooksPagingSource(apiService = get()) }

    //UseCase
    factory<GetBookListByAuthorUseCase> {GetBookListByAuthorUseCaseImpl(bookRepository = get()) }

    //Presentation
    viewModel { BookSearchViewModel(bookRepository = get(), getBooksByAuthorUseCase = get()) }
}