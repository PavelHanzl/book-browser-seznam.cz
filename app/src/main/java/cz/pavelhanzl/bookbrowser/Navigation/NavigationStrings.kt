package cz.pavelhanzl.bookbrowser.Navigation

enum class NavigationStrings(val route:String) {
    BOOKSEARCH("booksearch"),
    BOOKDETAIL("bookdetail");

    override fun toString(): String {
        return route
    }

}
