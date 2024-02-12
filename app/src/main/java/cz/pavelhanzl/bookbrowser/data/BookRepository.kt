package cz.pavelhanzl.bookbrowser.data

import androidx.paging.PagingData
import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {


    suspend fun searchBooksByAuthor(authorName: String) : List<Book>?
    fun searchBooksByAuthorStream(): Flow<PagingData<Book>>
}