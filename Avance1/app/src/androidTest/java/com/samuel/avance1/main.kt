package com.samuel.avance1

data class Cliente(
    val id: Int,
    val nombre: String
)

data class Producto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val precio: Double
)

data class ItemPedido(
    val producto: Producto,
    val cantidad: Int
)

data class Pedido(
    val id: Int,
    val cliente: Cliente,
    val items: List<ItemPedido>,
    val estatus: String
)

fun totalPedido(pedido: Pedido): Double {
    return pedido.items.sumOf {
        it.producto.precio * it.cantidad
    }
}

fun main() {

    // CLIENTES
    val clientes = listOf(
        Cliente(1, "Samuel Dominguez"),
        Cliente(2, "Christian Albrand"),
        Cliente(3, "Esteban Torres"),
        Cliente(4, "Alejandro Banderas"),
        Cliente(5, "Alan Escarcega")
    )

    // PRODUCTOS
    val productos = listOf(
        Producto(1, "Laptop", "Electronica", 15000.0),
        Producto(2, "Mouse", "Electronica", 350.0),
        Producto(3, "Teclado", "Electronica", 800.0),
        Producto(4, "Monitor", "Electronica", 4500.0),
        Producto(5, "Silla", "Hogar", 1200.0),
        Producto(6, "Mesa", "Hogar", 2500.0),
        Producto(7, "Lampara", "Hogar", 600.0),
        Producto(8, "Cuaderno", "Papeleria", 50.0),
        Producto(9, "Mochila", "Papeleria", 700.0),
        Producto(10, "Impresora", "Electronica", 3200.0)
    )

    // PEDIDOS
    val pedidos = listOf(
        Pedido(
            1,
            clientes[0],
            listOf(
                ItemPedido(productos[0], 1),
                ItemPedido(productos[1], 2)
            ),
            "Entregado"
        ),
        Pedido(
            2,
            clientes[1],
            listOf(
                ItemPedido(productos[4], 2),
                ItemPedido(productos[6], 1)
            ),
            "Pendiente"
        ),
        Pedido(
            3,
            clientes[2],
            listOf(
                ItemPedido(productos[3], 1)
            ),
            "Enviado"
        ),
        Pedido(
            4,
            clientes[3],
            listOf(
                ItemPedido(productos[7], 10),
                ItemPedido(productos[8], 2)
            ),
            "Entregado"
        ),
        Pedido(
            5,
            clientes[4],
            listOf(
                ItemPedido(productos[9], 1)
            ),
            "Cancelado"
        ),
        Pedido(
            6,
            clientes[0],
            listOf(
                ItemPedido(productos[2], 2),
                ItemPedido(productos[5], 1)
            ),
            "Entregado"
        )
    )

    println("TODOS LOS PEDIDOS: ")
    pedidos.forEach {
        println(
            "Pedido #${it.id} | Cliente: ${it.cliente.nombre} | Estatus: ${it.estatus}"
        )
    }

    println("TODOS LOS PRODUCTOS: ")

    productos.forEach {
        println(
            "${it.nombre} | Categoria: ${it.categoria} | Precio: $${it.precio}"
        )
    }

    println("PEDIDOS ENTREGADOS: ")

    pedidos
        .filter { it.estatus == "Entregado" }
        .forEach {
            println("Pedido #${it.id} - ${it.cliente.nombre}")
        }

    println("PRODUCTOS ELECTRONICOS: ")

    productos
        .filter { it.categoria == "Electronica" }
        .forEach {
            println("${it.nombre} - $${it.precio}")
        }

    println("BUSQUEDA DE PRODUCTO: Mouse")

    productos
        .filter { it.nombre.contains("Mouse", ignoreCase = true) }
        .forEach {
            println(it)
        }

    println("PRODUCTOS ORDENADOS POR PRECIO:")

    productos
        .sortedBy { it.precio }
        .forEach {
            println("${it.nombre} - $${it.precio}")
        }

    println("PEDIDOS ORDENADOS POR MONTO TOTAL:")

    pedidos
        .sortedBy { totalPedido(it) }
        .forEach {
            println(
                "Pedido #${it.id} - Cliente: ${it.cliente.nombre} - Total: $${totalPedido(it)}"
            )
        }

    println("TOTAL VENDIDO (SOLO ENTREGADOS): ")

    val totalVendido = pedidos
        .filter { it.estatus == "Entregado" }
        .sumOf { totalPedido(it) }

    println("Total vendido: $$totalVendido")

    println("CANTIDAD DE PEDIDOS POR ESTATUS: ")

    val conteo = pedidos
        .groupingBy { it.estatus }
        .eachCount()

    conteo.forEach { (estatus, cantidad) ->
        println("$estatus : $cantidad")
    }

    println("PRODUCTOS CON PRECIO MAYOR A 1000: ")

    productos
        .filter { it.precio > 1000 }
        .forEach {
            println("${it.nombre} - $${it.precio}")
        }

    println("CLIENTE ASOCIADO A CADA PEDIDO: ")

    pedidos.forEach {
        println("Pedido #${it.id} -> ${it.cliente.nombre}")
    }

    println("RESUMEN DE PEDIDOS: ")

    pedidos
        .map {
            Triple(
                it.cliente.nombre,
                totalPedido(it),
                it.estatus
            )
        }
        .forEach {
            println(
                "Cliente: ${it.first} | Total: $${it.second} | Estatus: ${it.third}"
            )
        }
}