package com.ponce.agendaweb; // Tu paquete exacto

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




// Al poner extends JpaRepository, estás usando la herencia de Java. Le estás diciendo a Spring Boot:
// “Quiero que este archivo herede todos los superpoderes de la gran herramienta de bases de datos de Spring”.

// Dentro de los "cheurones" <Contacto, String> le estás dando las dos únicas instrucciones que necesita:

// 1. Contacto: Le dices qué tipo de objeto va a guardar en la tabla.
// 2. String: Le dices de qué tipo es su llave primaria (la variable @Id, que en tu caso es el nombre, un texto).
@Repository
public interface ContactoRepository extends JpaRepository<Contacto, String> {
    // 🔥 ¡SÍ, ESTÁ VACÍO! No se escribe nada aquí dentro.
    // Al heredar de JpaRepository, Spring inventa automáticamente los métodos
    // de guardar, borrar, buscar y listar en segundo plano. ¡Cero SQL a mano!
}


//A partir de ahora, sin haber escrito nada, tu ContactoRepository ya tiene dentro estos métodos automáticos listos para usar:

// findAll(): Hace un SELECT * FROM contactos automático y te devuelve la lista entera.

// save(objeto): Hace el INSERT INTO... o el UPDATE... de SQL de forma inteligente según si el contacto ya existe o no.

// findById(id): Hace el SELECT * FROM contactos WHERE nombre = ....

// deleteById(id): Hace el DELETE FROM contactos WHERE nombre = ....