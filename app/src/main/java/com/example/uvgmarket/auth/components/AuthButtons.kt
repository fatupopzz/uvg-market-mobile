package com.example.uvgmarket.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.R
import com.example.uvgmarket.auth.style.ButtonWhite
import com.example.uvgmarket.auth.style.TextDark

@Composable
fun AuthButtons(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonWhite,
                contentColor = TextDark
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(
                text = stringResource(R.string.iniciar_sesion),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
                )
        }
        Button(
            onClick = onRegisterClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonWhite,
                contentColor = TextDark
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(
                text = stringResource(R.string.registrarse),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
