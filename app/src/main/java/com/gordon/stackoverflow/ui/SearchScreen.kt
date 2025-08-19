package com.gordon.stackoverflow.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gordon.stackoverflow.data.NetworkModule
import com.gordon.stackoverflow.viewmodel.SearchViewModel
import com.gordon.stackoverflow.viewmodel.ViewModelFactory
import com.gordon.stackoverflowapp.R
import kotlinx.coroutines.launch

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.stackoverflow_orange))
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(stringResource(R.string.search)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onClear() },
                        tint = Color.Gray
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedBorderColor = colorResource(R.color.stackoverflow_light_gray),
                cursorColor = colorResource(R.color.stackoverflow_orange)
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(textDecoration = null)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = NetworkModule.provideRepository(context)
    val factory = ViewModelFactory(repository)
    val searchViewModel: SearchViewModel = viewModel(factory = factory)

    val searchResults by searchViewModel.searchResults.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()
    val errorMessage by searchViewModel.errorMessage.collectAsState()
    val showNetworkDialog by searchViewModel.showNetworkDialog.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // State for confirmation dialog
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Intercept back button
    BackHandler {
        showConfirmDialog = true
    }

    // Confirmation dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Close App") },
            text = { Text("Are you sure you want to close the app?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    (context as? android.app.Activity)?.finish()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(LocalContext.current.resources.displayMetrics.widthPixels.dp * 1 / 4)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.White)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Sample user avatar
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                colorResource(R.color.stackoverflow_title),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "GM",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Gordon Matshwane", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("gordonm87@gmail.com", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: Handle Profile click
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = colorResource(R.color.stackoverflow_gray),
                            modifier = Modifier.size(20.dp)
                        )
                        Text("Profile", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: Handle Settings click
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = colorResource(R.color.stackoverflow_gray),
                            modifier = Modifier.size(20.dp)
                        )
                        Text("Settings", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: Handle Help click
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Help",
                            tint = colorResource(R.color.stackoverflow_gray),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "Help",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(8.dp))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            // Close the drawer
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.stackoverflow_orange)
                        )
                    ) {
                        Text(
                            stringResource(R.string.logout),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.logo_stackoverflow),
                                contentDescription = "Stack Overflow Logo",
                                modifier = Modifier
                                    .size(130.dp)
                                    .padding(end = 8.dp)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = "Open navigation drawer")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        searchViewModel.searchQuestions(searchQuery)
                        keyboardController?.hide()
                    },
                    onClear = {
                        searchQuery = ""
                        searchViewModel.refreshQuestions()
                    }
                )
                com.gordon.stackoverflow.ui.components.ErrorCard(errorMessage)
                com.gordon.stackoverflow.ui.components.QuestionList(
                    questions = searchResults,
                    onQuestionClick = { question -> navController.navigate("detail/${question.question_id}") }
                )
            }
            com.gordon.stackoverflow.ui.components.LoaderOverlay(isLoading)
            if (showNetworkDialog) {
                NoNetworkDialog(onDismiss = { searchViewModel.dismissNetworkDialog() })
            }
        }
    }
}