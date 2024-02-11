package cz.pavelhanzl.bookbrowser.features.bookdetail.Model

data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)

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
