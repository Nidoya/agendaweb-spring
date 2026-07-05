package com.ponce.agendaweb;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class ContactoService {

    // Le decimos a Spring Boot: "Búscame el ContactoDAO que está en la caja de herramientas
    // y conéctalo aquí"
    @Autowired
    private ContactoRepository automatico;


    public ArrayList<Contacto> obtenerTodosLosContactos(){
        System.out.println("El Encargado (Service) dice: 'Pidiendo la lista al repositorio moderno'.");

        // repo.findAll() nos devuelve una Lista de Java.
        // Lo envolvemos en un new ArrayList para que encaje perfectamente con lo que espera tu controlador.
        return new ArrayList<>(automatico.findAll());
    }


    /**
     * El encargado recibe el contacto desde el controlador,
     * lo procesa (aquí se harían las validaciones en el futuro)
     * y le dice al cocinero (DAO) que lo guarde en MySQL.
     */
    public boolean guardarNuevoContacto(Contacto nuevoContacto) {
        System.out.println("🧠 Encargado (Service): 'Me ha llegado un contacto para guardar a:" + nuevoContacto.getNombre() + "'.");

        // --- 🧼 COMIENZA LA LIMPIEZA DE DATOS ---
        String nombreSocio = nuevoContacto.getNombre();


        if(nombreSocio !=null && !nombreSocio.isEmpty()){
            // Cogemos la primera letra y la pasamos a Mayúscula + le pegamos el resto del nombre en minúsculas
            String nombreFormateado = nombreSocio.substring(0,1).toUpperCase() + nombreSocio.substring(1).toLowerCase();

            //Si introduces "elena", substring(0,1) coge la "e", la hace mayúscula ("E"), y luego substring(1) coge
            //desde la segunda letra hasta el final ("lena"), uniéndolo todo para que quede "Elena".


            // Le cambiamos el nombre feo al contacto por el nombre limpio y ordenado
            nuevoContacto.setNombre(nombreFormateado);
            System.out.println("🧼 Encargado (Service): 'He formateado el nombre de forma profesional: " + nuevoContacto.getNombre() + "'.");
        }
        // --- 🧼 FIN DE LA LIMPIEZA ---

        // 1. REGLA: Comprobamos si ya existe usando el metodo automático de Spring: repo.existsById()
        // Le pasamos el nombre y él mira en MySQL si esa clave primaria ya está ocupada. ¡Así de fácil!
        boolean existe = automatico.existsById(nuevoContacto.getNombre());

        if (existe){
            System.out.println("🧠 Encargado (Service): '¡ALERTA! El contacto ya existe. Rechazado'.");
            return false;
        }else{
            System.out.println("🧠 Encargado (Service): 'Nombre libre. Guardando automáticamente...'.");

            // Usamos repo.save() para meterlo en MySQL sin escribir ni una línea de SQL
            automatico.save(nuevoContacto);
            return true;
        }
    }



    /**
     * El encargado recibe el nombre de la persona que se quiere borrar desde el controlador.
     * No necesita recibir una caja entera, solo el texto con el nombre.
     * Le pasa la orden directa al cocinero (DAO) para que lo elimine de MySQL.
     */
    public void borrarContactoPorNombre(String nombre) {
        System.out.println("🧠 Encargado (Service): 'Me han pedido borrar a: " + nombre + "'.");

        // Mágico: JPA busca por la clave primaria (el nombre) y lo elimina directamente
        automatico.deleteById(nombre);
    }



    /**
     * El encargado recibe la caja completa con el contacto que queremos modificar.
     * Dentro viajan el nombre (para el WHERE) y el teléfono nuevo (para el SET).
     * Se lo entrega al cocinero (DAO) para que actualice MySQL.
     */
    public void modificarTelefonoContacto(Contacto contactoModificado) {
        System.out.println("🧠 Encargado (Service): 'Petición de modificación recibida'.");

        // En JPA, el metodo .save() es inteligente:
        // Si el nombre NO existe, lo inserta. Si el nombre YA existe, hace un UPDATE automático.
        automatico.save(contactoModificado);
    }



    /**
     * El encargado recibe el nombre que queremos buscar.
     * Le pide al cocinero (DAO) que lo busque en MySQL.
     * @return Devuelve el objeto Contacto encontrado, o null si no existe.
     */
    public Contacto buscarContactoPorNombre(String nombre) {
        System.out.println("🧠 Encargado (Service): 'Me han pedido buscar a: " + nombre + "'.");

        // findById() busca por nombre. Le ponemos .orElse(null) para que si no
        // encuentra nada en MySQL, devuelva un 'null' idéntico a como lo hacías antes.
        return automatico.findById(nombre).orElse(null);
    }


}
