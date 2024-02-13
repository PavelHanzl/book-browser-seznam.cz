package cz.pavelhanzl.bookbrowser.data

import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import cz.pavelhanzl.bookbrowser.features.booksearch.model.BookSearchResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface BookApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("orderBy") orderBy: String = "newest", //if not sorted Google Api can return different response for same API call, for example you can try reloading this endpoint a you might get different responses for each api call https://www.googleapis.com/books/v1/volumes?q=inauthor:Nesbo&langRestrict=cs&startIndex=10&maxResults=3
        @Query("langRestrict") language: String = "cs",
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int
    ): Response<BookSearchResponse>

    @GET("volumes/{id}")
    suspend fun searchBookById(
        @Path("id") bookId: String
    ): Response<Book>

    @GET
    suspend fun getPage(@Url url: String): Response<ResponseBody>

}