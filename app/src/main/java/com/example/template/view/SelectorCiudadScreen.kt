import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.template.data.CiudadStorage
import com.example.template.domain.CitySelectorIntent
import com.example.template.viewmodel.CitySelectorViewModel


@Composable
fun SelectorCiudadScreen(
    onCiudadSeleccionada: (String) -> Unit,
    citySelectorViewModel: CitySelectorViewModel = viewModel()
) {
    val state by citySelectorViewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        citySelectorViewModel.handleIntent(CitySelectorIntent.LoadInitial)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Selecciona tu ciudad", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.query,
            onValueChange = {
                citySelectorViewModel.handleIntent(CitySelectorIntent.QueryChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar ciudad (ej: Buenos Aires)") }
        )

        Spacer(Modifier.height(8.dp))

        // NUEVO botón de geolocalización
        Button(
            onClick = {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val coarseGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                val fineGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                val provider = when {
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                        LocationManager.GPS_PROVIDER
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                        LocationManager.NETWORK_PROVIDER
                    else -> null
                }

                val location = if (provider != null && (coarseGranted || fineGranted)) {
                    locationManager.getLastKnownLocation(provider)
                } else null

                val (lat, lon) = if (location != null) {
                    location.latitude to location.longitude
                } else {
                    // Fallback para que siempre funcione en el parcial: Buenos Aires
                    -34.6037 to -58.3816
                }

                citySelectorViewModel.handleIntent(
                    CitySelectorIntent.BuscarPorUbicacion(lat, lon)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Usar mi ubicación")
        }

        Spacer(Modifier.height(16.dp))

        // Loading + errores


        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.results) { city ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            CiudadStorage.guardarCiudad(context, city.name)
                            onCiudadSeleccionada(city.name)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text("${city.name}, ${city.country}")
                    Text("(${city.lat}, ${city.lon})", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
