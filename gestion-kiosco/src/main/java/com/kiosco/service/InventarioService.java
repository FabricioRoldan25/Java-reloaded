package com.kiosco.service;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.*;
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
            guardarDatos();
            System.out.println("Cargado con éxito.");
            System.out.println("Cargado con éxito: " + p.getNombre() + " (ID: " + p.getId() + ")");
        }else {
            System.out.println("Error: el producto no puede ser nulo");
        }
    }

    public void actualizarPrecio (long id, double nuevoPrecio){
        // reutilizamos el metodo de busqueda que ya tenemos
        Producto p = buscarPorId(id);

        if (p != null) {
            p.setPrecioVenta(nuevoPrecio); //cambiamos el dato en memoria
            System.out.println("Precio actualizado: " + p.getNombre() + " ahora cuesta $" + nuevoPrecio);
            guardarDatos();
        } else {
            System.out.println("Error: no se encontro el producto con ID " + id);
        }

    }

    public void eliminarProducto (long id) {
        //vamos a remover el id del producto p si es igual al id que le vamos a pasar
        boolean eliminado = listaProductos.removeIf(p -> p.getId() == id);
        if (eliminado) {
            guardarDatos();
            System.out.println("Eliminado.");
            System.out.println("Producto con ID " + id + " eliminado correctamente." );
        }else {
            System.out.println("Error: no se encontro ningun producto con el ID " + id);
        }

    }
    public void guardarDatos() {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("inventario.dat"))) {
            oos.writeObject(this.listaProductos);

            oos.writeLong(this.proximoId);
            System.out.println("Sincronizando datos con el disco duro...");

        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    public void cargarDatos() {

        File archivo = new File("inventario.dat"); //verificamos si el archivo existe antes de intentar leerlo
        if (!archivo.exists()){
            System.out.println("No se encontro historial previo. Iniciando sistema limpio");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream( new FileInputStream(archivo))) {
            this.listaProductos = (List<Producto>) ois.readObject();
            this.proximoId = ois.readLong();
            System.out.println("¡Datos recuperados con exito!");
        }catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
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

                    guardarDatos();
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

    public void generarReporteCierre () {
        String nombreArchivo = "reporte_cierre.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo, true))) {
        // el true dentro del filewriter permite que cada cierre se guarde abajo del anterior
            writer.println("====== REPORTE DE CIERRE DE CAJA ======");
            writer.println("Ventas del dia: $" + this.cajaDiaria);
            writer.println("Ganancia estimada: $" + this.gananciaTotal);
            writer.println("Productos totales en inventario: " + this.listaProductos.size());
            writer.println("---------------------------------------");
            writer.println("");

            this.cajaDiaria = 0; //reseteamos la caja para el proximo turno

            guardarDatos(); //guardamos el cambio para nuestro archivo .dat

            System.out.println("¡Reporte generado! Buscá el archivo 'reporte.cierre.txt' en tu carpeta");

        } catch (Exception e) {
            System.out.println("Error técnico al escribir el reporte: " + e.getMessage());
        }

    }

}
