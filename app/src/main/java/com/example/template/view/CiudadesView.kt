package com.example.template.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.template.domain.ClimaIntent
import com.example.template.viewmodel.ClimaViewModel
import com.example.template.viewmodel.TemaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CiudadesView(
    temaViewModel: TemaViewModel,
    ciudadInicial: String,
    onNavigateBack: () -> Unit,
    climaViewModel: ClimaViewModel = viewModel()
) {
    val state by climaViewModel.state.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(ciudadInicial) {
        climaViewModel.handleIntent(ClimaIntent.CargarClima(ciudadInicial))
    }


    LaunchedEffect(state.shareText) {
        val text = state.shareText ?: return@LaunchedEffect
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(sendIntent, "Compartir pronóstico"))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clima") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        climaViewModel.handleIntent(ClimaIntent.Compartir)
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Compartir")
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }

                state.error != null -> {
                    Text(
                        text = state.error ?: "Error desconocido",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Text(text = state.ciudad, fontSize = 24.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(text = "${state.temperatura}°C", fontSize = 32.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(text = state.descripcion, fontSize = 18.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(text = "Humedad: ${state.humedad}%", fontSize = 14.sp)

                        Spacer(Modifier.height(24.dp))

                        Button(onClick = {
                            climaViewModel.handleIntent(ClimaIntent.Refrescar)
                        }) {
                            Text("Actualizar clima")
                        }

                        Spacer(Modifier.height(24.dp))


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray)
                                .padding(16.dp)
                        ) {

                            if (state.pronostico.isEmpty()) {

                                Text(
                                    text = "Sin pronóstico extendido disponible",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                            } else {

                                Column {

                                    Text(
                                        text = "Próximos días:",
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )


                                    val maxTemp = state.pronostico.maxOf { it.tempMax }
                                    val minTemp = state.pronostico.minOf { it.tempMin }
                                    val range = (maxTemp - minTemp).takeIf { it > 0 } ?: 1.0

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                            .padding(bottom = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        state.pronostico.forEach { day ->

                                            val normalizedMax =
                                                ((day.tempMax - minTemp) / range)
                                                    .toFloat().coerceIn(0f, 1f)

                                            val normalizedMin =
                                                ((day.tempMin - minTemp) / range)
                                                    .toFloat().coerceIn(0f, 1f)

                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Bottom,
                                                modifier = Modifier.width(56.dp)
                                            ) {

                                                Box(
                                                    modifier = Modifier
                                                        .height(100.dp)
                                                        .fillMaxWidth()
                                                        .border(1.dp, Color.LightGray)
                                                ) {


                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .fillMaxHeight(normalizedMax)
                                                            .align(Alignment.BottomCenter)
                                                            .background(
                                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                                            )
                                                    )


                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .fillMaxHeight(normalizedMin)
                                                            .align(Alignment.BottomCenter)
                                                            .background(MaterialTheme.colorScheme.primary)
                                                    )
                                                }

                                                Spacer(Modifier.height(4.dp))

                                                Text(day.date, fontSize = 10.sp, textAlign = TextAlign.Center)
                                                Text(
                                                    "${day.tempMin.toInt()}° / ${day.tempMax.toInt()}°",
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }


                                    state.pronostico.forEach { day ->
                                        Text(
                                            text = "- ${day.date}: ${day.tempMin}°C / ${day.tempMax}°C, ${day.description}",
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
