package com.kiosco;

import com.kiosco.model.Producto;
import com.kiosco.service.InventarioService;

public class Main {
    public static void main(String[] args) {
        // instanciamos nuestro servicio (gestor de kiosco)
        InventarioService kiosco = new InventarioService();

        kiosco.agregarProducto(new Producto(1L, "Alfajor", 1600.0, 2300.0, 30));

        kiosco.realizarVenta("Alfajor", 3);
        kiosco.realizarVenta("alfajor", 5);

        kiosco.mostrarReporteFinanciero();
    }
}