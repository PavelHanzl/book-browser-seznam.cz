package cz.pavelhanzl.bookbrowser.data

import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book

interface BookRepository {


    suspend fun  searchBooksByAuthor(authorName: String, startIndex: Int, maxResults: Int): Result<List<Book>>
}