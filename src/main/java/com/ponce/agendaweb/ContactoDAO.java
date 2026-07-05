package com.ponce.agendaweb;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;

/**
 * CAPA DE ACCESO A DATOS (EL COCINERO DE LA COCINA)
 * DAO significa 'Data Access Object'. Es el único archivo autorizado a tocar MySQL.
 * No tiene ni un solo 'Scanner' porque el cocinero no habla con los clientes del comedor.
 */

// En Spring Boot, a las clases que hablan con bases de datos se les pone la pegatina
// @Repository en vez de @Service).
@Repository
public class ContactoDAO {

    // --- METODO: GUARDAR (CREATE) ---
    public void guardarContacto(Contacto nuevoContacto) {
        try {
            // 1. Abrimos la tubería con el servidor XAMPP y nuestra BD 'smart_agenda'
            String url = "jdbc:mysql://localhost:3306/smart_agenda";
            Connection conexion = DriverManager.getConnection(url, "root", "");

            // 2. Preparamos la plantilla SQL. Las '?' evitan hackeos por "Inyección SQL".
            String sql = "INSERT INTO contactos (nombre, telefono, email, es_vip) VALUES (?, ?, ?, ?)";
            PreparedStatement consultaPreparada = conexion.prepareStatement(sql);

            // 3. Cargamos la consulta leyendo los datos del Tupper usando los GETTERS
            consultaPreparada.setString(1, nuevoContacto.getNombre());
            consultaPreparada.setString(2, nuevoContacto.getTelefono());
            consultaPreparada.setString(3, nuevoContacto.getEmail());
            consultaPreparada.setBoolean(4, nuevoContacto.isEs_vip());

            // 4. Ejecutamos la inserción en las tablas
            consultaPreparada.executeUpdate();
            System.out.println("¡Contacto guardado permanentemente desde el DAO!");

            // 5. Cerramos las conexiones de dentro hacia afuera
            consultaPreparada.close();
            conexion.close();

        } catch (SQLException error) {
            System.out.println("Error de SQL al guardar: " + error.getMessage());
        }
    }

    // --- METODO: MOSTRAR TODOS (READ) ---
    // Promete devolver una bandeja (ArrayList) llena de tuppers (Contacto)
    public ArrayList<Contacto> mostrarTodosLosContactos() {

        // Creamos la bandeja vacía
        ArrayList<Contacto> bandeja = new ArrayList<>();

        try {
            String url = "jdbc:mysql://localhost:3306/smart_agenda";
            Connection conexion = DriverManager.getConnection(url, "root", "");

            String sql = "SELECT * FROM contactos";
            PreparedStatement consultaPreparada = conexion.prepareStatement(sql);

            // executeQuery() se usa para los SELECT. Devuelve un ResultSet (las filas que lee de MySQL)
            ResultSet resultado = consultaPreparada.executeQuery();

            // Mientras la flecha del ResultSet encuentre una fila "siguiente" en la tabla...
            while (resultado.next()) {
                // Extraemos los textos de las columnas de MySQL
                String nombre = resultado.getString("nombre");
                String telefono = resultado.getString("telefono");
                String email = resultado.getString("email");
                boolean es_vip = resultado.getBoolean("es_vip");

                // Fabricamos un tupper con los datos de esta fila concreta
                Contacto ticket = new Contacto(nombre, telefono, email, es_vip);

                // Añadimos el tupper a la bandeja
                bandeja.add(ticket);
            }

            resultado.close();
            consultaPreparada.close();
            conexion.close();

        } catch (SQLException error) {
            System.out.println("Fallo de SQL al listar: " + error.getMessage());
        }

        // Le pasamos la bandeja llena al camarero (Main)
        return bandeja;
    }

    // --- METODO: BORRAR (DELETE) ---
    public boolean quitarContacto(String nombreABorrar){

        boolean encontrado = false; // Empezamos asumiendo que no va a existir

        try {
            String url = "jdbc:mysql://localhost:3306/smart_agenda";
            Connection conexion = DriverManager.getConnection(url, "root", "");

            // Sentencia mortal. ¡NUNCA OLVIDAR EL WHERE EN UN DELETE!
            String sql = "DELETE FROM contactos WHERE nombre = ?";
            PreparedStatement consultaPreparada = conexion.prepareStatement(sql);

            consultaPreparada.setString(1, nombreABorrar);

            // executeUpdate() devuelve un número entero con las filas afectadas.
            // Si borró 1 fila, significa que el contacto existía.
            int filasBorradas = consultaPreparada.executeUpdate();

            if (filasBorradas > 0){
                encontrado = true; // Avisamos que fue un éxito
            }

            consultaPreparada.close();
            conexion.close();

        } catch (SQLException error) {
            System.out.println("Fallo al borrar el contacto: " + error.getMessage());
        }

        return encontrado;
    }

    // --- METODO: ACTUALIZAR TELÉFONO (UPDATE) ---
    public boolean actualizarTelefono(Contacto telefonoModificado){

        boolean exito = false;

        try {
            String url = "jdbc:mysql://localhost:3306/smart_agenda";
            Connection conexion = DriverManager.getConnection(url, "root", "");

            // Orden de actualización controlada por el WHERE nombre
            String sql = "UPDATE contactos SET telefono = ? WHERE nombre = ?";
            PreparedStatement consultaPreparada = conexion.prepareStatement(sql);

            // ⚠️ OJO AL ORDEN DE LAS INTERROGACIONES:
            // La 1ª '?' es el nuevo teléfono. La leemos del tupper que modificó el Main.
            consultaPreparada.setString(1, telefonoModificado.getTelefono());
            // La 2ª '?' es el nombre de la persona que usamos en el WHERE.
            consultaPreparada.setString(2, telefonoModificado.getNombre());

            int filasModificadas = consultaPreparada.executeUpdate();

            if (filasModificadas > 0){
                exito = true;
            }

            consultaPreparada.close();
            conexion.close();

        } catch (SQLException error) {
            System.out.println("Fallo al actualizar el contacto: " + error.getMessage());
        }
        return exito;
    }

    // --- METODO: BUSCAR POR NOMBRE (READ INDIVIDUAL) ---
    // Promete devolver un solo tupper (o null si la persona no existe)
    public Contacto buscarContacto(String nombreABuscar) {

        Contacto contactoEncontrado = null; // Empezamos asumiendo que no está

        try {
            String url = "jdbc:mysql://localhost:3306/smart_agenda";
            Connection conexion = DriverManager.getConnection(url, "root", "");

            String sql = "SELECT * FROM contactos WHERE nombre = ?";
            PreparedStatement consultaPreparada = conexion.prepareStatement(sql);
            consultaPreparada.setString(1, nombreABuscar);

            ResultSet resultado = consultaPreparada.executeQuery();

            // Usamos 'if' en vez de 'while' porque el nombre es único y solo esperamos 0 o 1 filas
            if (resultado.next()) {
                String nombre = resultado.getString("nombre");
                String telefono = resultado.getString("telefono");
                String email = resultado.getString("email");
                boolean es_vip = resultado.getBoolean("es_vip");

                // Rellenamos el tupper con los datos reales que nos ha dado MySQL
                contactoEncontrado = new Contacto(nombre, telefono, email, es_vip);
            }

            resultado.close();
            consultaPreparada.close();
            conexion.close();

        } catch (SQLException error) {
            System.out.println("Error al buscar: " + error.getMessage());
        }

        return contactoEncontrado;
    }

    public boolean borrarTodosContactos(String eliminarATodos){
        boolean borrarTodo = false;

        try {
            String url = "jdbc:mysql://localhost:3306/smart_agenda";
            Connection conexion = DriverManager.getConnection(url, "root", "");

            // Sentencia mortal. ¡NUNCA OLVIDAR EL WHERE EN UN DELETE!
            String sql = "TRUNCATE TABLE contactos";
            PreparedStatement consultaPreparada = conexion.prepareStatement(sql);

            int filasBorradas = consultaPreparada.executeUpdate();

            borrarTodo = true;

            consultaPreparada.close();
            conexion.close();
        }catch (SQLException error){
            System.out.println("Error al intentar borrar toda la agenda " + error.getMessage());
        }

        return borrarTodo;
    }



    /**
     * Busca un contacto en MySQL por su nombre.
     * @return El objeto Contacto si lo encuentra, o null si no existe.
     */
    public Contacto buscarContactoPorNombre(String nombreABuscar) {
        try {
            // 1. Usamos tu forma exacta de conectar a la base de datos
            String url = "jdbc:mysql://localhost:3306/smart_agenda";
            Connection conexion = DriverManager.getConnection(url, "root", "");

            String sql = "SELECT * FROM contactos WHERE nombre = ?";
            PreparedStatement consultaPreparada = conexion.prepareStatement(sql);
            consultaPreparada.setString(1, nombreABuscar);

            ResultSet resultado = consultaPreparada.executeQuery();

            // Si hay resultados...
            if (resultado.next()) {
                // 2. Extraemos los datos del ResultSet
                String nombre = resultado.getString("nombre");
                String telefono = resultado.getString("telefono");
                String email = resultado.getString("email");
                boolean esVip = resultado.getBoolean("es_vip");

                // 3. Fabricamos el tupper usando tu constructor de 4 ingredientes
                Contacto encontrado = new Contacto(nombre, telefono, email, esVip);
                return encontrado;
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return null; // Si no lo encuentra, devuelve null
    }
}