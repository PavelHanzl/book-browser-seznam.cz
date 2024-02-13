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

        //end if there is a request in progress
        if (isMakingRequest){
            return
        }

        //set that there is a request in progress
        isMakingRequest = true

        //loading has begun
        onLoadUpdated(true)

        //get result from API/repository
        val result = onRequest(currentIndex)

        //set that there is not a request in progress
        isMakingRequest = false


        //update items or if get was unsuccessful set error and information, that loading has ended
        items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }

        //increment current page/index
        currentIndex = getNextIndex(currentIndex,maxResultsPerPage)

        onSuccess(items,currentIndex)

        //loading has ended
        onLoadUpdated(false)

    }

    //reset paginator for reuse
    override fun reset() {
        currentIndex = initialIndex
        items = emptyList()
    }
}