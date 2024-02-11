package cz.pavelhanzl.bookbrowser.data

import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book

interface BookRepository {


    suspend fun searchBooks(authorName: String) : List<Book>?
}