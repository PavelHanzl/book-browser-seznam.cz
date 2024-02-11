package cz.pavelhanzl.bookbrowser.data

import cz.pavelhanzl.bookbrowser.features.booksearch.model.BookSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("langRestrict") language: String = "cs"
    ): Response<BookSearchResponse>
}