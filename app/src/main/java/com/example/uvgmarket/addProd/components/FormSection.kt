package com.example.uvgmarket.addProd.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.R

/**
 * Sección del formulario con todos los campos de entrada
 *
 * @param nombre Valor actual del campo nombre
 * @param onNombreChange Callback cuando cambia el nombre
 * @param descripcion Valor actual del campo descripción
 * @param onDescripcionChange Callback cuando cambia la descripción
 * @param precio Valor actual del campo precio
 * @param onPrecioChange Callback cuando cambia el precio
 */
@Composable
fun FormSection(
    nombre: String,
    onNombreChange: (String) -> Unit,
    descripcion: String,
    onDescripcionChange: (String) -> Unit,
    precio: String,
    onPrecioChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        CustomTextField(
            label = stringResource(R.string.nombre),
            value = nombre,
            onValueChange = onNombreChange,
            placeholder = stringResource(R.string.hint_nombre),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextField(
            label = stringResource(R.string.descripcion),
            value = descripcion,
            onValueChange = onDescripcionChange,
            placeholder = stringResource(R.string.hint_descripcion),
            singleLine = false,
            minLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextField(
            label = stringResource(R.string.precio),
            value = precio,
            onValueChange = onPrecioChange,
            placeholder = stringResource(R.string.hint_precio),
            singleLine = true
        )
    }
}