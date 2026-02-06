package com.kiosco.service;

import com.kiosco.model.Producto;
import java.util.ArrayList;
import java.util.List;

public class InventarioService {

    private List<Producto > listaProductos;

    public InventarioService() {
        this.listaProductos = new ArrayList<>();
    }

    public void agregarProducto(Producto p) {
        listaProductos.add(p);
        System.out.println("Producto agregado: " + p.getNombre());
    }

    public void mostrarInventario() {
        System.out.println("--- Inventario actual ---");
        for (Producto p : listaProductos) {
            System.out.println(p.getNombre() + " - Stock: " + p.getStock() + " - precio: $" + p.getPrecioVenta());
        }
    }
}
