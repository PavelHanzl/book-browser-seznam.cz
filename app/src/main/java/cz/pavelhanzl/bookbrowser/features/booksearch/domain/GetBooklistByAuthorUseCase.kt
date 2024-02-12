package cz.pavelhanzl.bookbrowser.features.booksearch.domain

import androidx.paging.PagingData
import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book
import cz.pavelhanzl.bookbrowser.features.booksearch.model.BookSearchResponse
import kotlinx.coroutines.flow.Flow

interface GetBookListByAuthorUseCase {

    suspend operator fun invoke(
        authorName: String
    ) :  Flow<PagingData<Book>>?

}

class GetBookListByAuthorUseCaseImpl (
    private val bookRepository: BookRepository
) : GetBookListByAuthorUseCase {

    override suspend fun invoke(authorName: String):  Flow<PagingData<Book>> {
        return bookRepository.searchBooksByAuthorStream()
    }
}