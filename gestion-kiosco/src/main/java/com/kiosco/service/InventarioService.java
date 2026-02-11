package com.kiosco.service;

import com.kiosco.model.Producto;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;

public class InventarioService {

    private List<Producto > listaProductos;
    private double cajaDiaria; // atributo para el dinero acumulado.
    private double gananciaTotal;

    private long proximoId = 1;

    public InventarioService() {
        this.listaProductos = new ArrayList<>();
        this.cajaDiaria = 0.0; // se empieza el dia en cero
        this.gananciaTotal = 0.0;
    }

    public void agregarProducto(Producto p) {
        if (p != null) {

            p.setId(this.proximoId); //asignamos el id actual del contador al producto

            this.listaProductos.add(p);

            this.proximoId++;
            System.out.println("Cargado con éxito: " + p.getNombre() + " (ID: " + p.getId() + ")");
        }else {
            System.out.println("Error: el producto no puede ser nulo");
        }
    }

    public Producto buscarPorId (long idBuscar) {
        //recorremos lista de productos
        for (Producto p : listaProductos) {
            //comparamos el id del producto actual con el q queremos.
            if (p.getId() == idBuscar) {
                return p; //si se encuentra, se devuelve y se corta el metodo
            }

        }
        return null;
    }

    public void mostrarInventario() {
        System.out.println("\n========= INVENTARIO ACTUAL =========");

        // 1. Lógica defensiva: ¿La lista está vacía?
        if (this.listaProductos.isEmpty()) {
            System.out.println("El inventario está vacío. Carga algún producto primero");
        } else {
            // 2. Recorremos la lista con un for-each
            for (Producto p : listaProductos) {
                System.out.println("ID: " + p.getId() +
                        " | Nombre: " + p.getNombre() +
                        " | Stock: " + p.getStock() +
                        " | Precio Venta: $" + p.getPrecioVenta());
            }
        }
        System.out.println("=====================================\n");
    }

    public void realizarVenta(String nombreProducto,int cantidad) {
        for (Producto p : listaProductos) {
            if (p.getNombre().equalsIgnoreCase(nombreProducto)) {

                double margenUnitario = p.getPrecioVenta() - p.getPrecioCosto(); //calculamos la ganancia unitaria antes de operar

                if (margenUnitario <= 0) {

                    System.out.println("Alerta: ¡el precio de venta es menor o igual al de costo!");

                    return;
                    }

                if (p.getStock() >= cantidad) {
                    p.setStock(p.getStock() - cantidad);

                    double totalVenta = p.getPrecioVenta() * cantidad;
                    // calculo de esta ganancia en especifico

                    double gananciaDeEstaVenta = (p.getPrecioVenta() - p.getPrecioCosto()) * cantidad;
                    // sumamos el total de la venta a nuestra caja

                    this.cajaDiaria += totalVenta;
                    this.gananciaTotal += gananciaDeEstaVenta;

                    System.out.println("Venta exitosa: " +nombreProducto + " x" + cantidad);
                    System.out.println("Total a cobrar: " + totalVenta);
                    System.out.println("Ganancia de esta operacion: " + gananciaDeEstaVenta);
                } else {
                    System.out.println("Error: no hay suficiente stock de " + nombreProducto);
                } return;
            }
        }
        System.out.println("Error: producto no encontrado.");
    }
    public void mostrarReporteFinanciero() {
        System.out.println("---------------------------");
        System.out.println("Dinero total en caja: $" + this.cajaDiaria);
        System.out.println("Ganancia neta: " + this.gananciaTotal);
        System.out.println("---------------------------");
    }

}
