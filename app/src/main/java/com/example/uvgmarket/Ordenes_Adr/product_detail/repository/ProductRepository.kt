package com.example.uvgmarket.Ordenes_Adr.product_detail.repository

import com.example.uvgmarket.R
import com.example.uvgmarket.Ordenes_Adr.product_detail.models.ProductDetail

/**
 * Repositorio de productos para manejo de datos de demostración
 */
object ProductRepository {

    private val products = listOf(
        ProductDetail(
            id = "hamburger1",
            nombre = "Pandita Hamburguesa",
            subtitulo = "Gruesa y caliente",
            descripcion = "Rica hamburguesa libre de gluten sin ningún tipo de preservantes.",
            precio = 39.00,
            imagen = R.drawable.hamburger1,
            vendedorId = "1",
            vendedorNombre = "Hamburguesas Kawaii"
        ),
        ProductDetail(
            id = "hamburger2",
            nombre = "Mega Burger Deluxe",
            subtitulo = "Doble carne, doble sabor",
            descripcion = "Hamburguesa premium con doble carne de res, queso cheddar, tocino ahumado y vegetales frescos.",
            precio = 45.00,
            imagen = R.drawable.hamburger2,
            vendedorId = "1",
            vendedorNombre = "Hamburguesas Kawaii"
        ),
        ProductDetail(
            id = "joyeria1",
            nombre = "Collar Luna Plateada",
            subtitulo = "Elegante y artesanal",
            descripcion = "Hermoso collar hecho a mano con plata 925 y detalles de luna creciente.",
            precio = 150.00,
            imagen = R.drawable.joyeria1,
            vendedorId = "2",
            vendedorNombre = "Accesorios Luna"
        ),
        ProductDetail(
            id = "joyeria2",
            nombre = "Aretes Estrella",
            subtitulo = "Brillantes y únicos",
            descripcion = "Aretes artesanales con diseño de estrella, perfectos para cualquier ocasión.",
            precio = 85.00,
            imagen = R.drawable.joyeria2,
            vendedorId = "2",
            vendedorNombre = "Accesorios Luna"
        ),
        ProductDetail(
            id = "limpinado1",
            nombre = "Mantenimiento Laptop",
            subtitulo = "Limpieza profunda",
            descripcion = "Servicio completo de limpieza y mantenimiento preventivo para laptops.",
            precio = 120.00,
            imagen = R.drawable.limpinado1,
            vendedorId = "3",
            vendedorNombre = "TechRepair GT"
        ),
        ProductDetail(
            id = "limpiando2",
            nombre = "Reparación de Pantalla",
            subtitulo = "Servicio profesional",
            descripcion = "Reparación especializada de pantallas de celulares y tablets con garantía.",
            precio = 200.00,
            imagen = R.drawable.limpiando2,
            vendedorId = "3",
            vendedorNombre = "TechRepair GT"
        ),
        ProductDetail(
            id = "osito_hamburguesa",
            nombre = "Osito Hamburguesa",
            subtitulo = "Linda y deliciosa",
            descripcion = "Rica y bonita para ti. Hamburguesa con forma de osito kawaii.",
            precio = 25.00,
            imagen = R.drawable.osito_hamburguesa,
            vendedorId = "1",
            vendedorNombre = "Hamburguesas Kawaii"
        ),
        ProductDetail(
            id = "pandita_hamburguesa",
            nombre = "Pandita Hamburguesa",
            subtitulo = "Dulce presentación",
            descripcion = "Dulce y saludable. Hamburguesa gourmet con presentación adorable.",
            precio = 39.00,
            imagen = R.drawable.pandita_hamburguesa,
            vendedorId = "1",
            vendedorNombre = "Hamburguesas Kawaii"
        )
    )

    /**
     * Obtiene un producto por su ID
     */
    fun getProductById(productId: String): ProductDetail? {
        return products.find { it.id == productId }
    }

    /**
     * Obtiene todos los productos
     */
    fun getAllProducts(): List<ProductDetail> {
        return products
    }

    /**
     * Obtiene productos de un vendedor específico
     */
    fun getProductsByVendor(vendedorId: String): List<ProductDetail> {
        return products.filter { it.vendedorId == vendedorId }
    }
}