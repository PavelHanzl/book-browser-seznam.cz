package cz.pavelhanzl.bookbrowser.features.bookdetail.model

data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
){
    companion object{}
}

data class VolumeInfo(
    val title: String,
    val authors: List<String>,
    val publishedDate: String,
    val description: String?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
){
    fun smallThumbnailToHttps():String{
        return if (smallThumbnail.startsWith("http://")){
            smallThumbnail.replace("http://", "https://")
        } else smallThumbnail

    }

    fun thumbnailToHttps():String{
        return if (thumbnail.startsWith("http://")){
            thumbnail.replace("http://", "https://")
        } else thumbnail
    }

}

fun Book.Companion.sampleBook():Book{
    return Book(
        "121",
        VolumeInfo(
            "Testovací kniha",
            listOf("Petr Novák", "Jan Čerdný"),
            "12.2.2003",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nullam justo enim, consectetuer nec, ullamcorper ac, vestibulum in, elit. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. In enim a arcu imperdiet malesuada. Pellentesque arcu. Duis ante orci, molestie vitae vehicula venenatis, tincidunt ac pede.",
            ImageLinks(
                "https://books.google.com/books/content?id=hZZBEAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
                "https://books.google.com/books/content?id=hZZBEAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"
            )
        )
    )
}
