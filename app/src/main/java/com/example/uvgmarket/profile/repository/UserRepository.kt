package com.example.uvgmarket.profile.repository

import com.example.uvgmarket.R
import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.models.Producto

/**
 * Repositorio de usuarios para manejo de datos de demostración
 */
object UserRepository {

    // ID del usuario actual (el propio perfil)
    const val CURRENT_USER_ID = "current_user"

    private val usuarios = mapOf(
        CURRENT_USER_ID to Usuario(
            id = CURRENT_USER_ID,
            nombre = "Harry Méndez",
            descripcion = "Estudiante de computación",
            imagenPerfil = R.drawable.fotodeperfilindu,
            imagenPortada = R.drawable.encabezado_usuario,
            calificacion = 2f
        ),
        "1" to Usuario(
            id = "1",
            nombre = "Hamburguesa Kawaii",
            descripcion = "Las mejores hamburguesas de la ciudad",
            imagenPerfil = R.drawable.fotodeperfilhamburger,
            imagenPortada = R.drawable.portada_perfil,
            calificacion = 3f
        ),
        "2" to Usuario(
            id = "2",
            nombre = "Accesorios Luna",
            descripcion = "Joyería artesanal hecha a mano",
            imagenPerfil = R.drawable.fotodeperfiljoyeria,
            imagenPortada = R.drawable.portada_perfil,
            calificacion = 5f
        ),
        "3" to Usuario(
            id = "3",
            nombre = "TechRepair GT",
            descripcion = "Reparación de celulares y laptops",
            imagenPerfil = R.drawable.imagendeperfilcomputadora,
            imagenPortada = R.drawable.encabezado_compu,
            calificacion = 4f
        )
    )

    private val productosPropio = listOf(
        Producto(
            id = "osito_hamburguesa",
            nombre = "Osito Hamburguesa",
            precio = 25.0,
            descripcion = "Rica y bonita para ti",
            imagen = R.drawable.osito_hamburguesa
        ),
        Producto(
            id = "pandita_hamburguesa",
            nombre = "Pandita Hamburguesa",
            precio = 39.0,
            descripcion = "Dulce y saludable",
            imagen = R.drawable.pandita_hamburguesa
        )
    )

    private val productosHamburguesas = listOf(
        Producto(
            id = "hamburger1",
            nombre = "Pandita Hamburguesa",
            precio = 39.0,
            descripcion = "Gruesa y caliente",
            imagen = R.drawable.hamburger1
        ),
        Producto(
            id = "hamburger2",
            nombre = "Mega Burger Deluxe",
            precio = 45.0,
            descripcion = "Doble carne, doble sabor",
            imagen = R.drawable.hamburger2
        )
    )

    private val productosJoyeria = listOf(
        Producto(
            id = "joyeria1",
            nombre = "Collar Luna Plateada",
            precio = 150.0,
            descripcion = "Elegante y artesanal",
            imagen = R.drawable.joyeria1
        ),
        Producto(
            id = "joyeria2",
            nombre = "Aretes Estrella",
            precio = 85.0,
            descripcion = "Brillantes y únicos",
            imagen = R.drawable.joyeria2
        )
    )

    private val productosTech = listOf(
        Producto(
            id = "limpinado1",
            nombre = "Mantenimiento Laptop",
            precio = 120.0,
            descripcion = "Limpieza profunda",
            imagen = R.drawable.limpinado1
        ),
        Producto(
            id = "limpiando2",
            nombre = "Reparación de Pantalla",
            precio = 200.0,
            descripcion = "Servicio profesional",
            imagen = R.drawable.limpiando2
        )
    )

    /**
     * Obtiene el usuario actual (perfil propio)
     */
    fun getCurrentUser(): Usuario {
        return usuarios[CURRENT_USER_ID]!!
    }

    /**
     * Obtiene un usuario por su ID
     */
    fun getUserById(userId: String): Usuario? {
        return usuarios[userId]
    }

    /**
     * Obtiene los productos del usuario actual
     */
    fun getCurrentUserProducts(): List<Producto> {
        return productosPropio
    }

    /**
     * Obtiene los productos de un usuario específico
     */
    fun getProductsByUserId(userId: String): List<Producto> {
        return when (userId) {
            CURRENT_USER_ID -> productosPropio
            "1" -> productosHamburguesas
            "2" -> productosJoyeria
            "3" -> productosTech
            else -> emptyList()
        }
    }

    /**
     * Obtiene todos los usuarios excepto el actual
     */
    fun getAllOtherUsers(): List<Usuario> {
        return usuarios.values.filter { it.id != CURRENT_USER_ID }
    }
}