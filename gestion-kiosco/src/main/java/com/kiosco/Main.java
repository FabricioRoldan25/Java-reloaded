package com.kiosco;

import com.kiosco.model.Producto;
import com.kiosco.service.InventarioService;

public class Main {
    public static void main(String[] args) {
        // instanciamos nuestro servicio (gestor de kiosco)
        InventarioService kiosco = new InventarioService();
        //creamos productos y usamos el constructor de lombok
        Producto p1 = new Producto(1L, "Alfajor Oreo", 1300.0, 1750.0, 10);
        Producto p2 = new Producto(2L, "Coca Cola 1L", 1500.0, 2300.0, 20);
        Producto p3 = new Producto(3L, "Bhramah 730ml", 2250.0, 3150.0, 30);

        kiosco.agregarProducto(p1);
        kiosco.agregarProducto(p2);
        kiosco.agregarProducto(p3);

        kiosco.mostrarInventario();

        kiosco.realizarVenta("Coca Cola 1L", 2);
    }
}