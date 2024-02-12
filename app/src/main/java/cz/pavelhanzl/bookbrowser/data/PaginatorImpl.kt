package cz.pavelhanzl.bookbrowser.data

class PaginatorImpl<Key, Item>(
    private val initialIndex:Key,
    private val maxResultsPerPage: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextIndex: Key) -> Result<List<Item>>,
    private inline val getNextIndex: suspend (currentIndex: Key, maxResults:Key) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item>{

    private var currentIndex: Key = initialIndex
    private var isMakingRequest = false
    private var items:List<Item> = emptyList()
    override suspend fun loadNextItems() {
        if (isMakingRequest){
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentIndex)
        isMakingRequest = false



        items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }

        currentIndex = getNextIndex(currentIndex,maxResultsPerPage)

        onSuccess(items,currentIndex)
        onLoadUpdated(false)

    }

    override fun reset() {
        currentIndex = initialIndex
        items = emptyList()
    }
}