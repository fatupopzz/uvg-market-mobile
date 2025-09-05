package com.example.uvgmarket.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.R

data class Producto(
    val id: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val imagen: Int
)

data class PerfilUsuario(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val imagenPerfil: Int,
    val imagenPortada: Int,
    val calificacion: Float = 0f,
    //val numeroReseñas: Int = 0
)

@Composable
fun PerfilScreen(
    usuario: PerfilUsuario,
    productos: List<Producto> = emptyList(),
    onBackClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {},
    onProductoClick: (String) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen de portada del negocio
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    // Imagen de portada
                    Image(
                        painter = painterResource(id = usuario.imagenPortada),
                        contentDescription = "Portada de ${usuario.nombre}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Botón de regreso
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .background(
                                Color.White.copy(alpha = 0.8f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.Black
                        )
                    }
                }
            }

            // Información del usuario
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Foto de perfil del usuario
                            Image(
                                painter = painterResource(id = usuario.imagenPerfil),
                                contentDescription = usuario.nombre,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                // Calificación con estrellas
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    repeat(5) { index ->
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            //Color verde
                                            tint = if (index < usuario.calificacion) Color(
                                                0xFF365236
                                            ) else Color.Gray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Nombre y botón de chat
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = usuario.nombre,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF365236),
                                modifier = Modifier.weight(1f)
                            )

                            // Botón de chat
                            IconButton(
                                onClick = { onChatClick(usuario.id) },
                                modifier = Modifier
                                    .background(
                                        Color(0xFF365236).copy(alpha = 0.1f),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    painter = painterResource(id = R.drawable.chat),
                                    contentDescription = "Chat",
                                    tint = Color(0xFF4CAF50)
                                )
                            }
                        }

                        // Descripción del usuario
                        Text(
                            text = usuario.descripcion,
                            fontSize = 14.sp,
                            color = Color(0xFF365236),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Lista de productos
            items(productos) { producto ->
                ProductoCard(
                    producto = producto,
                    onClick = { onProductoClick(producto.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Espacio para el botón flotante
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Botón flotante
        FloatingActionButton(
            onClick = { /* Sin funcionalidad por ahora */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar"
            )
        }
    }
}

@Composable
private fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            // Imagen del producto
            Image(
                painter = painterResource(id = producto.imagen),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Información del producto
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Nombre del producto
                Text(
                    text = producto.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF365236),
                    maxLines = 2
                )

                // Descripción del producto
                Text(
                    text = producto.descripcion,
                    fontSize = 14.sp,
                    color = Color(0xFF365236),
                    maxLines = 2,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Precio
                Text(
                    text = "${String.format("Q. %.1f", producto.precio)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF365236)
                )
            }
        }
    }
}

@Preview
@Composable
fun PerfilScreenPreview() {
    val usuario = PerfilUsuario(
        id = "1",
        nombre = "Hamburguesas Kawaii",
        descripcion = "Tu lugar fav para comer",
        imagenPerfil = R.drawable.profile_picture,
        imagenPortada = R.drawable.portada_perfil,
        calificacion = 3f
    )

    val productos = listOf(
        Producto(
            id = "1",
            nombre = "Osito Hamburguesa",
            precio = 25.0,
            descripcion = "Un abrazo de felicidad en cada mordisco",
            imagen = R.drawable.osito_hamburguesa
        ),
        Producto(
            id = "2",
            nombre = "Pandita Hamburguesa",
            precio = 39.0,
            descripcion = "Hamburguesa con papas fritas",
            imagen = R.drawable.pandita_hamburguesa
        )
    )

    PerfilScreen(
        usuario = usuario,
        productos = productos
    )
}