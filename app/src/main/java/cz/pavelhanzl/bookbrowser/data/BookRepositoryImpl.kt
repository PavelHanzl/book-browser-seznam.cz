package cz.pavelhanzl.bookbrowser.data

import android.util.Log
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.sampleBook

class BookRepositoryImpl(
    private val dataSource: BookApiService
) : BookRepository {
    override suspend fun searchBooksByAuthor(
        authorName: String,
        startIndex: Int,
        maxResults: Int
    ): Result<List<Book>> {

        //used for bug fixing with Google api - to be deleted
        Log.d(
            "query-api",
            "https://www.googleapis.com/books/v1/volumes?q=inauthor:${authorName}&orderBy=newest&langRestrict=cs&startIndex=${startIndex}&maxResults=${maxResults} "
        )

        //search for every book with given author name Google Api
        val response = dataSource.searchBooks(
            query = "inauthor:${authorName}",
            startIndex = startIndex,
            maxResults = maxResults
        )

        //returns result with list of books or empty list - not null
        return if (response.isSuccessful) {
            Result.success(response.body()?.items ?: emptyList())
        } else {
            return Result.success(emptyList())
        }

    }

    override suspend fun searchBookById(bookId: String): Book {

        //search book with given id
        val response = dataSource.searchBookById(
            bookId = bookId
        )
        return if (response.isSuccessful && response.body()!=null) {
            response.body()!!
        } else {
            Book.sampleBook()
        }

    }
}