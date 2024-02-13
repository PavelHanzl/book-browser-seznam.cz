package cz.pavelhanzl.bookbrowser.data

import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import cz.pavelhanzl.bookbrowser.features.booksearch.model.BookSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("orderBy") orderBy: String = "newest",
        @Query("langRestrict") language: String = "cs",
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int
    ): Response<BookSearchResponse>

    @GET("volumes/{id}")
    suspend fun searchBookById(
        @Path("id") bookId: String
    ): Response<Book>
}