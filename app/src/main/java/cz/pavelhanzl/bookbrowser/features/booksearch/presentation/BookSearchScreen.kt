package cz.pavelhanzl.bookbrowser.features.booksearch.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.integration.ktx.ExperimentGlideFlows
import cz.pavelhanzl.bookbrowser.R
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import cz.pavelhanzl.bookbrowser.navigation.NavigationStrings
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreen(
    navController: NavController,
    viewModel: BookSearchViewModel = koinViewModel()
) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            SearchBar(viewModel)
        }
    ) { it ->

        when { //determines what is displayed on a blank screen without books

            state.isLoading && state.items.isEmpty() -> { //show loading progress indicator
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.resultExpected && state.items.isEmpty() -> { //show there was nothing found for given author
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(R.string.no_books_found_for_specified_author),
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            state.items.isNotEmpty() -> { //if book list is not empty, show returned books
                BookList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .imePadding(),
                    viewModel = viewModel,
                    navController
                )
            }

            else -> { //else default state - begin your search.
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(R.string.start_searching),
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }


    }

}

@Composable
fun SearchBar(viewModel: BookSearchViewModel) {

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .padding(10.dp)
            .padding(bottom = 20.dp)
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.Bottom
    ) {

        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = viewModel.state.searchText,
            onValueChange = { newText ->
                viewModel.onSearchTextChanged(newText)
            },
            singleLine = true,
            label = { Text(stringResource(R.string.enter_author_name)) }
        )

        Spacer(modifier = Modifier.size(8.dp))

        OutlinedIconButton(
            modifier = Modifier
                .size(55.dp),
            onClick = {
                focusManager.clearFocus()
                viewModel.onSearchButtonClick()
            }
        ) {
            Icon(Icons.Outlined.Search, contentDescription = stringResource(R.string.search))
        }
    }
}

@Composable
fun BookList(
    modifier: Modifier = Modifier,
    viewModel: BookSearchViewModel,
    navController: NavController
) {
    val state = viewModel.state

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        items(state.items.size) { i ->
            BookItem(state.items[i],
                onClick = {
                    navController.navigate("${NavigationStrings.BOOKDETAIL}/${state.items[i].id}")
                })

            //loads next page of books
            if (i >= state.items.size - 1 && !state.endReached && !state.isLoading) {
                viewModel.loadNextBooks()
            }
        }

        //shows loading indicator if loading of next page is in progress
        item {
            if (state.isLoading) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Book image preview
        GlideImage(
            modifier = Modifier
                .size(100.dp),
            model = book.volumeInfo.imageLinks?.smallThumbnailToHttps(),
            contentDescription = stringResource(R.string.book_image_thumbnail),
            loading = placeholder(R.drawable.loading_placeholder),
            failure = placeholder(R.drawable.notfound_placeholder),
            transition = CrossFade
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {

            // Book name
            Text(
                text = book.volumeInfo.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            // Book authors
            Text(
                text = (book.volumeInfo?.authors?.take(2)
                    ?.joinToString(stringResource(R.string.comma_separator))) + if ((book.volumeInfo.authors.size ?: 0) > 2
                ) stringResource(R.string.and_more) else "", //shows only first two authors and if there are >2 authors then it shows "and more..."
                fontSize = 14.sp,
                color = Color.Gray
            )

            // Preview of book description
            Text(
                text = book.volumeInfo.description ?: stringResource(R.string.description_is_not_available),
                maxLines = 3,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                overflow = TextOverflow.Ellipsis,
                fontStyle = if (book.volumeInfo.description == null) FontStyle.Italic else FontStyle.Normal

            )

        }
    }
}