package com.example.template.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.template.domain.ClimaIntent
import com.example.template.domain.ClimaState
import com.example.template.viewmodel.ClimaViewModel
import com.example.template.viewmodel.TemaViewModel


//composable raíz de la app - función de "View" en MVI.

@Composable
fun ClimaView(temaViewModel: TemaViewModel, ciudad: String) {

    // creamos la instancia de
    val climaViewModel: ClimaViewModel = viewModel()

    // vemos el state del ViewModel.
    // state se actualiza automáticamente cada vez que el VM emita uno nuevo.
    val state by climaViewModel.state.collectAsState()

    //intent inicial
    LaunchedEffect(ciudad) {
        climaViewModel.handleIntent(ClimaIntent.CargarClima(ciudad))
    }

    // llamado a UI
    ClimaScreen(
        state = state,
        onIntent = { intent ->
            climaViewModel.handleIntent(intent)
        }
    )
}

//layout

@Composable
fun ClimaScreen(state: ClimaState, onIntent: (ClimaIntent) -> Unit) {

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        // cargando...
        if (state.isLoading) {
            //spinner
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
            }

        //en caso de error
        } else if (state.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error,
                    color = Color.Red,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            // si no está cargando y no hay error
        } else {
            //datos del clima
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                //día Actual
                Text(
                    text = state.ciudad,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${state.temperatura}°C",
                    fontSize = 56.sp
                )
                Text(
                    text = state.descripcion,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(32.dp))

                //placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color.Gray)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Acá iría el pronostico",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}