package com.example.uvgmarket.ordenes_Adr.product_detail.models

data class ProductDetail(
    val id: String,
    val nombre: String,
    val subtitulo: String,
    val descripcion: String,
    val precio: Double,
    val imagen: Int,
    val vendedorId: String,
    val vendedorNombre: String
)