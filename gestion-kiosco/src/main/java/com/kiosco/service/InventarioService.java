package com.kiosco.service;

import com.kiosco.model.Producto;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;

public class InventarioService {

    private List<Producto > listaProductos;
    private  double cajaDiaria; // atributo para el dinero acumulado.

    public InventarioService() {
        this.listaProductos = new ArrayList<>();
        this.cajaDiaria = 0.0; // se empieza el dia en cero
    }

    public void agregarProducto(Producto p) {
        if (p != null) {
            this.listaProductos.add(p);
            System.out.println("Cargadp con éxito: " + p.getNombre());
        }else {
            System.out.println("Error: el producto no puede ser nulo");
        }
    }

    public void realizarVenta(String nombreProducto,int cantidad) {
        for (Producto p : listaProductos) {
            if (p.getNombre().equalsIgnoreCase(nombreProducto)) {
                if (p.getStock() >= cantidad) {
                    p.setStock(p.getStock() - cantidad);
                    double totalVenta = p.getPrecioVenta() * cantidad;
                    // sumamos el total de la venta a nuestra caja
                    this.cajaDiaria += totalVenta;
                    System.out.println("Venta exitosa: " +nombreProducto + " x" + cantidad);
                    System.out.println("Total a cobrar: " + totalVenta);
                } else {
                    System.out.println("Error: no hay suficiente stock de " + nombreProducto);
                } return;
            }
        }
        System.out.println("Error: producto no encontrado.");
    }
    public void mostrarCaja() {
        System.out.println("---------------------------");
        System.out.println("Dinero total en caja: $" + this.cajaDiaria);
        System.out.println("---------------------------");
    }

}
