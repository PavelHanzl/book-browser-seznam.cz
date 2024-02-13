package cz.pavelhanzl.bookbrowser.features.bookdetail.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import cz.pavelhanzl.bookbrowser.R
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.VolumeInfo
import cz.pavelhanzl.bookbrowser.utils.ktx.removeHtmlFormatting
import cz.pavelhanzl.bookbrowser.utils.ktx.toCzechFormattedDate
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController, bookId: String?, viewModel: BookDetailViewModel = koinViewModel()
) {

    val book = viewModel.state.selectedBook
    val viewModel = viewModel

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(modifier = Modifier.size(55.dp), onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                viewModel.state.selectedBook?.volumeInfo?.let {
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
                BookDetailHeader(it, viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                BookDetailBody(it)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookDetailHeader(
    volumeInfo: VolumeInfo,
    viewModel: BookDetailViewModel
) {

    with(volumeInfo) {

        //Preview image
        GlideImage(
            model = imageLinks?.smallThumbnailToHttps(),
            loading = placeholder(R.drawable.loading_placeholder),
            failure = placeholder(R.drawable.notfound_placeholder),
            transition = CrossFade,
            contentDescription = stringResource(R.string.book_image_thumbnail),
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        //Book title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Book authors
        Text(
            text = authors.joinToString(stringResource(id = R.string.comma_separator)),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        //Book publish date
        Text(
            text = publishedDate.toCzechFormattedDate(),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        val urlOfBook = Uri.parse(infoLink)

        //check if page of book return 404 or success
        viewModel.checkPageContentWrapper(urlOfBook.toString())
        val hasGooglePlayRecord = viewModel.state.checkPageResult

        //Button with link to books detail on Google play
        OutlinedButton(onClick = {
            if (hasGooglePlayRecord != null) {
                if (hasGooglePlayRecord) { //book has its own google play website
                    try { //try open google play app
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = urlOfBook
                            setPackage("com.android.vending")
                        }
                        context.startActivity(intent)
                    } catch (ae: ActivityNotFoundException) { //if google play not installed, open in browser
                        Toast.makeText(
                            context,
                            R.string.cannotOpenInGooglePlay,
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = urlOfBook
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) { // else inform user about invalid link
                        Toast.makeText(
                            context,
                            R.string.link_cannot_be_opened,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else { // the book is only on google books not on google play

                    Toast.makeText(
                        context,
                        R.string.book_is_only_on_google_books,
                        Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(previewLink)
                    }
                    context.startActivity(intent)
                }

            }


        }) {
            Text(text = stringResource(R.string.open_at_google_play))
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
        //book description
        Text(
            text = description.removeHtmlFormatting(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}