package com.example.uvgmarket.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.R
import com.example.uvgmarket.auth.components.AuthLogo
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.buttons.PrimaryButton
import com.example.uvgmarket.ui.theme.UvgMarketTheme


@Composable
fun AuthScreen(
    onLogin: () -> Unit = {},
    onRegister: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_uvgmarket),
            contentDescription = "Fondo UVGMarket",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConstants.PADDING_LARGE.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UiConstants.PADDING_MEDIUM.dp)
        ) {
            AuthLogo(modifier = Modifier.size(300.dp))

            Spacer(Modifier.height(UiConstants.PADDING_EXTRA_LARGE.dp))

            PrimaryButton(
                text = stringResource(R.string.iniciar_sesion),
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
            )

            PrimaryButton(
                text = stringResource(R.string.registrarse),
                onClick = onRegister,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    UvgMarketTheme { AuthScreen() }
}