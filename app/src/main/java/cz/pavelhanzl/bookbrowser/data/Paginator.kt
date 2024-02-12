package cz.pavelhanzl.bookbrowser.data

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}