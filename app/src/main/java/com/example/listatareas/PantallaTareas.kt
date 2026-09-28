package com.example.listatareas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listatareas.ui.theme.ListaTareasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas() {
    val tareas = remember {
        mutableStateListOf(
            Tarea(1, "Estudiar Jetpack Compose"),
            Tarea(2, "Hacer la investigación", true),
            Tarea(3, "Entregar el informe en PDF")
        )
    }
    var texto by remember { mutableStateOf("") }
    var siguienteId by remember { mutableStateOf(4) }

    fun agregar() {
        if (texto.isNotBlank()) {
            tareas.add(Tarea(siguienteId++, texto.trim()))
            texto = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { agregar() }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CampoTarea(
                valor = texto,
                onCambio = { texto = it },
                onAgregar = { agregar() }
            )
            Text(
                text = "Completadas: ${tareas.count { it.completada }} de ${tareas.size}",
                style = MaterialTheme.typography.labelLarge
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tareas, key = { it.id }) { tarea ->
                    ItemTarea(
                        tarea = tarea,
                        onCambio = { marcada ->
                            val indice = tareas.indexOf(tarea)
                            tareas[indice] = tarea.copy(completada = marcada)
                        },
                        onEliminar = { tareas.remove(tarea) }
                    )
                }
            }
        }
    }
}

@Composable
fun CampoTarea(
    valor: String,
    onCambio: (String) -> Unit,
    onAgregar: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text("Nueva tarea") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onAgregar() }),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun ItemTarea(
    tarea: Tarea,
    onCambio: (Boolean) -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = tarea.completada, onCheckedChange = onCambio)
            Text(
                text = tarea.titulo,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (tarea.completada) TextDecoration.LineThrough else null,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemTareaPreview() {
    ListaTareasTheme {
        ItemTarea(Tarea(1, "Tarea de ejemplo"), onCambio = {}, onEliminar = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaTareasPreview() {
    ListaTareasTheme {
        PantallaTareas()
    }
}
