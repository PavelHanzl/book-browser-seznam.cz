package cz.pavelhanzl.bookbrowser.data

import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book

class BookRepositoryImpl:BookRepository {
    override fun getAllBooks(): List<Book> {
        return listOf(
            Book("1", "knizka"),
            Book("2", "knizecka"),
            Book("3", "knihaaa"))
    }
}