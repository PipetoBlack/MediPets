package com.example.medipets.ui.components // O la ruta donde lo tengas

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun InputText(
    valor: String,
    @StringRes error: Int?, // ACEPTA: Un ID de recurso de tipo Int (que puede ser nulo)
    label: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = error != null, // La caja se pone roja si el error no es nulo
        supportingText = {
            if (error != null) {
                // USA stringResource para "traducir" el ID al texto real
                Text(
                    text = stringResource(id = error),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}
