package cz.pavelhanzl.bookbrowser.utils.ktx

import java.text.SimpleDateFormat
import java.util.Locale

fun String.toCzechFormattedDate(): String {
    val regex = Regex("\\d{4}-\\d{2}-\\d{2}")

    // Kontrola, zda vstupní řetězec odpovídá očekávanému formátu
    if (!this.matches(regex)) {
        // Vstupní řetězec není v očekáváném formátu data, vrátíme původní řetězec
        return this
    }

    val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val targetFormat = SimpleDateFormat("dd. MM. yyyy", Locale.getDefault())
    val date = originalFormat.parse(this)
    return targetFormat.format(date!!)
}

//odstraní html formatovani
fun String.removeHtmlFormatting(): String {
    return this
        .replace("<p>", "")
        .replace("</p>", "\n\n")
        .replace(Regex("<[^>]*>"), "")

}