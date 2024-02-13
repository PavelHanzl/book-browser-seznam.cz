package cz.pavelhanzl.bookbrowser.features.bookdetail.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import cz.pavelhanzl.bookbrowser.R
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.VolumeInfo
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.sampleBook
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.BookSearchViewModel
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.SearchBar
import cz.pavelhanzl.bookbrowser.utils.ktx.removeHtmlFormatting
import cz.pavelhanzl.bookbrowser.utils.ktx.toCzechFormattedDate
import org.koin.androidx.compose.koinViewModel
import java.lang.Exception

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController, bookId: String?, viewModel: BookDetailViewModel = koinViewModel()
) {

    val book = viewModel.selectedBook

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(modifier = Modifier.size(55.dp), onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Zpět")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                viewModel.selectedBook?.volumeInfo?.let {
                    Text(
                        modifier = Modifier
                            .padding(end = 16.dp),
                        text = it.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp
                    )
                }
            })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            book?.volumeInfo?.let {
                BookDetailHeader(it)

                Spacer(modifier = Modifier.height(16.dp))

                BookDetailBody(it)
            }


        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookDetailHeader(
    volumeInfo: VolumeInfo
) {

    with(volumeInfo) {

        GlideImage(
            model = imageLinks?.smallThumbnailToHttps(),
            loading = placeholder(R.drawable.loading_placeholder),
            failure = placeholder(R.drawable.notfound_placeholder),
            transition = CrossFade,
            contentDescription = "Book Thumbnail",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = authors.joinToString(", "),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Text(
            text = publishedDate.toCzechFormattedDate(),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current

        OutlinedButton(onClick = {
            val urlOfBook = Uri.parse(infoLink)

            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = urlOfBook
                    setPackage("com.android.vending")
                }
                context.startActivity(intent)
            } catch (ae: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Odkaz se nepodařilo otevřít v Google Play - otevírám v prohlížeči.",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = urlOfBook
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Odkaz se nepodařilo otevřít.", Toast.LENGTH_LONG).show()
            }

        }) {
            Text(text = "Otevřít na Google Play")
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.tertiary,
            thickness = 1.dp
        )



    }


}

@Composable
fun BookDetailBody(volumeInfo: VolumeInfo) {
    volumeInfo.description?.let { description ->
        Text(
            text = description.removeHtmlFormatting(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}