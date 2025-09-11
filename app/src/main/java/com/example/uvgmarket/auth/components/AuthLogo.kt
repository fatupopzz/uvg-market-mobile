package com.example.uvgmarket.auth.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.uvgmarket.R

@Composable
fun AuthLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo_uvmarket),
        contentDescription = "Logo UVGMarket",
        modifier = modifier
    )
}

