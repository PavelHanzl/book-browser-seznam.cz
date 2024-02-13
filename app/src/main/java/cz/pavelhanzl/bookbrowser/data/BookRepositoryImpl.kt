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

        Log.d(
            "query-api",
            "https://www.googleapis.com/books/v1/volumes?q=inauthor:${authorName}&orderBy=newest&langRestrict=cs&startIndex=${startIndex}&maxResults=${maxResults} "
        )
        //vyhledává všechny knihy s daným autorem pomocí Google Api
        val response = dataSource.searchBooks(
            query = "inauthor:${authorName}",
            startIndex = startIndex,
            maxResults = maxResults
        )

        return if (response.isSuccessful) {
            Result.success(response.body()?.items ?: emptyList())
        } else {
            return Result.success(emptyList())
        }

//        if (response.isSuccessful && (response.body() != null)) {
//            //TODO refactor
//            if (response.body()!!.items == null)
//                return Result.success(emptyList())
//            else
//                return Result.success(response.body()!!.items!!)
//        } else {
//            return Result.success(emptyList())
//        }
    }

    override suspend fun searchBookById(bookId: String): Book {


        //vyhledá knihu s daným Id
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