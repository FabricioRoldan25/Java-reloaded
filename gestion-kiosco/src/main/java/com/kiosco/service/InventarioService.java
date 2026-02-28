package com.kiosco.service;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.*;
import com.kiosco.model.Producto;

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

    public void agregarProductoDB(Producto p) {

        System.out.println("DEBUG - Nombre: " + p.getNombre());
        System.out.println("DEBUG - Costo: " + p.getPrecioCosto());
        System.out.println("DEBUG - Venta: " + p.getPrecioVenta());
        System.out.println("DEBUG - Stock: " + p.getStock());

        //conexion al cable creado anteriormente
        String sql = "INSERT INTO productos (nombre, precio_costo, precio_venta, stock) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
        //llenamos los signos de preguntas con datos
        ps.setString(1, p.getNombre());
        ps.setDouble(2, p.getPrecioCosto());
        ps.setDouble(3, p.getPrecioVenta());
        ps.setInt(4, p.getStock());

        ps.executeUpdate();
            System.out.println("¡Producto guardado en MySQL con exito!");

        }catch (SQLException e) {
            System.out.println("Error al insertar en la base de datos: " + e.getMessage());
        }

        if (p != null) {

            p.setId(this.proximoId); //asignamos el id actual del contador al producto

            this.listaProductos.add(p);

            this.proximoId++;
            guardarDatosDB();
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
            guardarDatosDB();
        } else {
            System.out.println("Error: no se encontro el producto con ID " + id);
        }

    }

    public void eliminarProductoBD (long id) {

        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {

                boolean eliminadoDeLista = listaProductos.removeIf(p -> p.getId() == id);

                if (eliminadoDeLista) {
                    System.out.println("Eliminado.");
                    System.out.println("Producto con ID " + id + " eliminado correctamente");
                }else {
                    System.out.println("Error: no se encontro ningun producto con el ID " + id + " en la base de datos.");
                }

            }

        }catch (SQLException e) {
            System.out.println("Error critico al eliminar de la base de datos: " + e.getMessage());
        }

    }
    public void guardarDatosDB() {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("inventario.dat"))) {
            oos.writeObject(this.listaProductos);

            oos.writeLong(this.proximoId);
            System.out.println("Sincronizando datos con el disco duro...");

        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    public void cargarDatosDB() {

        this.listaProductos.clear();

        String sql = "SELECT * FROM productos";

        try (Connection con = ConexionDB.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto();

                p.setId(rs.getLong("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecioCosto(rs.getDouble("precio_costo"));
                p.setPrecioVenta(rs.getDouble("precio_venta"));
                p.setStock(rs.getInt("stock"));

                p.setId(rs.getLong("id"));

                this.listaProductos.add(p);
            }
            System.out.println("¡Datos sincronizados! se cargaron " +listaProductos.size() + " productos.");
        }catch (SQLException e) {
            System.out.println("Error al cargar datos desde MySQL: " + e.getMessage());
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

    public void realizarVenta(Long id,int cantidad) {
            Producto p = buscarPorId(id);
            if (p.getId().equals(id)) {

                double margenUnitario = p.getPrecioVenta() - p.getPrecioCosto(); //calculamos la ganancia unitaria antes de operar

                if (margenUnitario <= 0) {

                    System.out.println("Alerta: ¡el precio de venta es menor o igual al de costo!");

                    return;
                    }

                if (p.getStock() >= cantidad) {
                    p.setStock(p.getStock() - cantidad);

                String sql = "UPDATE productos SET stock = ? WHERE id = ?";

                try (Connection con = ConexionDB.getConexion();
                     PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setInt(1, p.getStock());
                    ps.setLong(2, p.getId());

                    ps.executeUpdate();
                    System.out.println("Sincronizado: El stock en la base de datos ha sido actualizado.");
                } catch (SQLException e) {
                    System.out.println("Error crítico al actualizar stock en BD: " +e.getMessage());
                }

                    double totalVenta = p.getPrecioVenta() * cantidad;
                    // calculo de esta ganancia en especifico

                    double gananciaDeEstaVenta = (p.getPrecioVenta() - p.getPrecioCosto()) * cantidad;
                    // sumamos el total de la venta a nuestra caja

                    this.cajaDiaria += totalVenta;
                    this.gananciaTotal += gananciaDeEstaVenta;


                    System.out.println("Total a cobrar: " + totalVenta);
                    System.out.println("Ganancia de esta operacion: " + gananciaDeEstaVenta);

                    guardarDatosDB();
                } else {
                    System.out.println("Error: No existe el producto con ID " + id);
                } return;
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

            guardarDatosDB(); //guardamos el cambio para nuestro archivo .dat

            System.out.println("¡Reporte generado! Buscá el archivo 'reporte.cierre.txt' en tu carpeta");

        } catch (Exception e) {
            System.out.println("Error técnico al escribir el reporte: " + e.getMessage());
        }

    }

}
