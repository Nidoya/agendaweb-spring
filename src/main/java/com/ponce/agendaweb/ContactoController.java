package com.ponce.agendaweb;

import java.util.ArrayList;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;


/**
 * LA CAPA DE CONTROLADOR (EL TELEOPERADOR)
 * La pegatina '@RestController' le dice a Spring Boot:
 * "Ojo, esta clase va a escuchar peticiones de Internet y va a responder con datos (JSON)".
 */
@RestController
@RequestMapping("/api") // Todas las direcciones web de esta clase empezarán por /api
@CrossOrigin(origins = "*")   // 🔥 LA MAGIA: Da permiso a cualquier diseño web para conectarse a tu API
// Nota: El * significa "permitir a todos". En producción se pone la web real, pero para aprender
// y hacer pruebas en casa es perfecto.


public class ContactoController {

    // Le decimos a Spring Boot: "Enchúfame aquí el ContactoService automáticamente"
    @Autowired
    private ContactoService servicio;

    /**
     * @GetMapping("/contactos") significa:
     * "Si alguien en Internet entra en la dirección: http://localhost:8080/api/contactos,
     * se ejecutará este metodo automáticamente".
     */
    @GetMapping("/contactos")
    public ArrayList<Contacto> enviarListaContactos() {

        System.out.println("📞 Teleoperador (Controller): 'Me han llamado desde Internet pidiendo la lista!'");
        System.out.println("📞 Teleoperador (Controller): 'Le paso la nota al Encargado (Service) para que él la gestione'.");

 // LA MAGIA: En vez de llamar al DAO, llamamos al metodo del Service que creamos hace un momento
        ArrayList<Contacto> lista = servicio.obtenerTodosLosContactos();

        // 3. Le devolvemos la bandeja a Spring Boot.
        // ¡Aquí está la magia! Spring Boot la transformará en JSON él solito.
        return lista;
    }



    /**
     * @PostMapping("/contactos") Versión Blindada.
     * Añadimos @Valid para que Spring Boot active el escáner de seguridad
     * y revise las pegatinas de Contacto.java antes de entrar.
     */
    @PostMapping("/contactos")
    public ResponseEntity<String> recibirNuevoContacto(@Valid @RequestBody Contacto nuevoContacto) {

        System.out.println("📞 Teleoperador (Controller): 'Paquete POST recibido de Internet para: " + nuevoContacto.getNombre() + "'.");

        // 1. Le pasamos el contacto al Encargado y GUARDAMOS su respuesta (true o false) en una variable
        boolean guardadoConExito = servicio.guardarNuevoContacto(nuevoContacto);

        // 2. Analizamos qué nos ha respondido el encargado
        if (guardadoConExito) {
            // SI ES TRUE: Todo ha ido de lujo, el nombre estaba libre.
            String mensajeOK = "✅ El contacto " + nuevoContacto.getNombre() + " se ha guardado correctamente.";

            // Devolvemos el sobre con el mensaje de éxito y el sello 201 CREATED
            return new ResponseEntity<>(mensajeOK, HttpStatus.CREATED);

        } else {
            // SI ES FALSE: El encargado lo ha rechazado porque ya existía en MySQL.
            String mensajeError = "❌ Error: El contacto '" + nuevoContacto.getNombre() + "' ya existe en tu agenda.";

            // LA MAGIA: Devolvemos el sobre con el mensaje de error y el sello profesional 409 CONFLICT
            return new ResponseEntity<>(mensajeError, HttpStatus.CONFLICT);
        }
    }


    /**
     * @DeleteMapping("/contactos/{nombreABorrar}")
     * Las llaves {} significan que esa parte de la dirección es una VARIABLE.
     * Puede ser /contactos/Juan, /contactos/Luis, etc.
     */
    @DeleteMapping("/contactos/{nombreABorrar}")
    public ResponseEntity<String> borrarContacto(@PathVariable String nombreABorrar) {

        System.out.println("🗑️ Petición de borrado recibida en la URL para: " + nombreABorrar);

        // Ya no hay que abrir ninguna caja, el nombre nos llega directamente como texto
        servicio.borrarContactoPorNombre(nombreABorrar);

        // Devolvemos un sobre profesional con código 200 OK
        return new ResponseEntity<>("🗑️ El contacto " + nombreABorrar + " ha sido eliminado", HttpStatus.OK);
    }


    @PutMapping("/contactos")
    public ResponseEntity<String> modificarTelefono(@RequestBody Contacto cambiarTelefono){

        System.out.println("Mensaje de modificación recibido.");
        System.out.println("Teléfono a actualizar " + cambiarTelefono.getTelefono());

        servicio.modificarTelefonoContacto(cambiarTelefono);

        return new ResponseEntity<>("El teléfono se ha actualizado correctamente en tu base de datos" , HttpStatus.OK);
    }


    /**
     * @GetMapping("/contactos/{nombreABuscar}")
     * Buscamos a una persona en concreto y devolvemos su tupper en JSON.
     */
    @GetMapping("/contactos/{nombreABuscar}")
    public ResponseEntity<Contacto> buscarUnContacto(@PathVariable String nombreABuscar) {

        System.out.println("🔍 Alguien en Internet está buscando a: " + nombreABuscar);

        // El cocinero busca en MySQL
        Contacto encontrado = servicio.buscarContactoPorNombre(nombreABuscar);

        if (encontrado != null) {
            // Si lo encuentra, le devolvemos la caja entera y el código 200 OK
            return new ResponseEntity<>(encontrado, HttpStatus.OK);
        } else {
            // Si devuelve null, significa que no existe. ¡Devolvemos el famoso error 404!
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}