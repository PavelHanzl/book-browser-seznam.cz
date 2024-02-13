package cz.pavelhanzl.bookbrowser.utils.ktx

import java.text.SimpleDateFormat
import java.util.Locale

fun String.toCzechFormattedDate(): String {
    val regex = Regex("\\d{4}-\\d{2}-\\d{2}")

    // Check if the input string matches the expected format
    if (!this.matches(regex)) {
        // The input string is not in the expected data format, return the original string
        return this
    }

    val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val targetFormat = SimpleDateFormat("dd. MM. yyyy", Locale.getDefault())
    val date = originalFormat.parse(this)
    return targetFormat.format(date!!)
}

//remove html formatting and replace the ending </p> tag with two new lines to simulate a paragraph
fun String.removeHtmlFormatting(): String {
    return this
        .replace("<p>", "")
        .replace("</p>", "\n\n")
        .replace(Regex("<[^>]*>"), "")

}