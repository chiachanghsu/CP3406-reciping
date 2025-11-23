package com.example.myapp.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapp.data.Repository
import com.example.myapp.data.model.Meal
import kotlinx.coroutines.launch

enum class ProfileTab {
    SAVED, EDIT, SHARE
}

@Composable
fun ProfileScreen(repo: Repository, onOpenDetail: (String) -> Unit) {
    var selectedTab by remember { mutableStateOf(ProfileTab.SAVED) }
    val context = LocalContext.current

    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            Tab(
                selected = selectedTab == ProfileTab.SAVED,
                onClick = { selectedTab = ProfileTab.SAVED },
                text = { Text("Saved") },
                icon = { Icon(Icons.Default.Favorite, null) }
            )
            Tab(
                selected = selectedTab == ProfileTab.EDIT,
                onClick = { selectedTab = ProfileTab.EDIT },
                text = { Text("Edit") },
                icon = { Icon(Icons.Default.Edit, null) }
            )
            Tab(
                selected = selectedTab == ProfileTab.SHARE,
                onClick = { selectedTab = ProfileTab.SHARE },
                text = { Text("Share") },
                icon = { Icon(Icons.Default.Share, null) }
            )
        }

        when (selectedTab) {
            ProfileTab.SAVED -> SavedRecipesTab(repo, onOpenDetail)
            ProfileTab.EDIT -> EditRecipesTab(repo, onOpenDetail)
            ProfileTab.SHARE -> ShareRecipesTab(repo, context)
        }
    }
}

@Composable
private fun SavedRecipesTab(repo: Repository, onOpenDetail: (String) -> Unit) {
    val saved = remember { mutableStateListOf<Meal>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            saved.clear()
            saved.addAll(repo.savedList())
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Saved recipes", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        if (saved.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Nothing saved yet")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(saved, key = { it.id }) { meal ->
                    Card(Modifier.fillMaxWidth().clickable { onOpenDetail(meal.id) }) {
                        Row(Modifier.padding(12.dp)) {
                            if (meal.thumb.isNotEmpty()) {
                                AsyncImage(
                                    model = meal.thumb,
                                    contentDescription = meal.name,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(MaterialTheme.shapes.small),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            MaterialTheme.shapes.small
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "📷",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.fillMaxWidth()) {
                                Text(meal.name, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    (meal.area ?: "") + (if (!meal.category.isNullOrBlank()) " • ${meal.category}" else "")
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditRecipesTab(repo: Repository, onOpenDetail: (String) -> Unit) {
    val saved = remember { mutableStateListOf<Meal>() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedMeal by remember { mutableStateOf<Meal?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            saved.clear()
            saved.addAll(repo.savedList())
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Edit Saved Recipes", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            if (saved.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No saved recipes to edit")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(saved, key = { it.id }) { meal ->
                        EditableMealCard(
                            meal = meal,
                            onOpenDetail = { onOpenDetail(meal.id) },
                            onEdit = {
                                selectedMeal = meal
                                showEditDialog = true
                            },
                            onDelete = {
                                scope.launch {
                                    repo.remove(meal.id)
                                    saved.remove(meal)
                                    snackbarHostState.showSnackbar(
                                        message = "Recipe deleted",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    selectedMeal?.let { meal ->
        if (showEditDialog) {
            EditRecipeDialog(
                meal = meal,
                repo = repo,
                onDismiss = {
                    showEditDialog = false
                    selectedMeal = null
                },
                onSave = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Notes saved!",
                            duration = SnackbarDuration.Short
                        )
                        showEditDialog = false
                        selectedMeal = null
                    }
                }
            )
        }
    }
}

@Composable
private fun EditableMealCard(
    meal: Meal,
    onOpenDetail: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (meal.thumb.isNotEmpty()) {
                AsyncImage(
                    model = meal.thumb,
                    contentDescription = meal.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "📷",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(meal.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    (meal.area ?: "") + (if (!meal.category.isNullOrBlank()) " • ${meal.category}" else "")
                )
            }
            TextButton(onClick = onEdit) {
                Text("Edit")
            }
            TextButton(onClick = onOpenDetail) {
                Text("View")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun EditRecipeDialog(
    meal: Meal,
    repo: Repository,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var notes by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(meal.id) {
        scope.launch {
            val savedRecipe = repo.getSavedRecipeWithNotes(meal.id)
            notes = savedRecipe?.notes ?: ""
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Comment: ${meal.name}") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Add your comment or notes:", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Add your comment, tips, or modifications here...") },
                    minLines = 6,
                    maxLines = 12
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    scope.launch {
                        repo.updateNotes(meal.id, notes)
                        onSave()
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ShareRecipesTab(repo: Repository, context: Context) {
    val saved = remember { mutableStateListOf<Meal>() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        scope.launch {
            saved.clear()
            saved.addAll(repo.savedList())
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Share Recipes", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            if (saved.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No saved recipes to share")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(saved, key = { it.id }) { meal ->
                        ShareableMealCard(
                            meal = meal,
                            onShare = {
                                scope.launch {
                                    val shareText = buildShareText(meal)
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                        putExtra(Intent.EXTRA_SUBJECT, "Recipe: ${meal.name}")
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Share Recipe"))
                                }
                            },
                            onCopy = {
                                scope.launch {
                                    val shareText = buildShareText(meal)
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Recipe", shareText)
                                    clipboard.setPrimaryClip(clip)
                                    snackbarHostState.showSnackbar(
                                        message = "Recipe copied to clipboard!",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShareableMealCard(
    meal: Meal,
    onShare: () -> Unit,
    onCopy: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (meal.thumb.isNotEmpty()) {
                AsyncImage(
                    model = meal.thumb,
                    contentDescription = meal.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "📷",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(meal.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    (meal.area ?: "") + (if (!meal.category.isNullOrBlank()) " • ${meal.category}" else "")
                )
            }
            TextButton(onClick = onCopy) {
                Text("Copy")
            }
            IconButton(onClick = onShare) {
                Icon(Icons.Default.Share, "Share")
            }
        }
    }
}

private fun buildShareText(meal: Meal): String {
    val ingredientsText = meal.ingredients.entries.joinToString("\n") { (ing, meas) ->
        "• $ing${if (meas.isNotBlank()) ": $meas" else ""}"
    }
    return """
        ${meal.name}
        ${if (meal.area != null) "Cuisine: ${meal.area}" else ""}
        ${if (meal.category != null) "Category: ${meal.category}" else ""}
        
        Ingredients:
        $ingredientsText
        
        Instructions:
        ${meal.instructions ?: "No instructions available"}
    """.trimIndent()
}
