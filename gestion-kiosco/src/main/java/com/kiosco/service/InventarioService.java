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
    public void realizarVenta(String nombreProducto, int cantidad){

        for (Producto p : listaProductos) {

            if (p.getNombre().equalsIgnoreCase(nombreProducto)) {
                if (p.getStock() >= cantidad) {
                    p.stock(p.getStock() - cantidad);
                    double total = p.getPrecioVenta() * cantidad;
                    System.out.println("Venta realizada: " + cantidad + "x " + nombreProducto + " | Total: $" + total);
                } else {
                    System.out.println("Error: Stock insuficiente de " + nombreProducto);
                } return;
            }
        }
        System.out.println("Error: producto no encontrado");
    }
}
