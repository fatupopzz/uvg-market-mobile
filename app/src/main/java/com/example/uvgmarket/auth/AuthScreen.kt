package com.example.uvgmarket.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.R
import com.example.uvgmarket.auth.components.AuthButtons
import com.example.uvgmarket.auth.components.AuthLogo
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
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthLogo(modifier = Modifier.size(160.dp))
            Spacer(Modifier.height(48.dp))
            AuthButtons(
                onLoginClick = onLogin,
                onRegisterClick = onRegister,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    UvgMarketTheme { AuthScreen() }
}
