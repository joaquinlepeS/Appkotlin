import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_kotlin.viewmodel.HospitalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalesScreen(viewModel: HospitalViewModel = viewModel()) {

    LaunchedEffect(Unit) { viewModel.cargarHospitales() }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Hospitales Cercanos") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Column (modifier = Modifier.padding(padding).padding(16.dp)) {

            if (viewModel.isLoading) {
                CircularProgressIndicator()
            }

            viewModel.hospitales.forEach { hos ->
                Text("• ${hos.name ?: "Hospital sin nombre"}")
            }
        }
    }
}
