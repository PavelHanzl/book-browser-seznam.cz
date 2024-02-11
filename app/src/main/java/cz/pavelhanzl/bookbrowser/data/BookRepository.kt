package cz.pavelhanzl.bookbrowser.data

import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book

interface BookRepository {
    fun getAllBooks() : List<Book>
}