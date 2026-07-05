package com.ponce.agendaweb;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * CAPA DE MODELO (EL TUPPER DE COMIDA)
 * Esta clase es un "POJO". No tiene lógica de bases de datos ni lee el teclado.
 * Su único objetivo es agrupar el nombre, teléfono y email de una persona
 * para que no viajen como variables sueltas por el programa.
 */
@Entity
@Table(name="contactos") // Pon aquí el nombre exacto de tu tabla en MySQL
public class Contacto {

    // --- ENCAPSULAMIENTO (Variables Ocultas) ---
    // Las ponemos 'private' para que nadie desde fuera (como el Main) las pueda
    // modificar por error. La información está protegida dentro de la "caja fuerte".

    @Id // Le dice a Spring que el nombre es la clave primaria (Primary Key)
    // El nombre no puede estar vacío ni tener solo espacios en blanco
    @NotBlank(message = "El nombre es obligatorio y no puede estar vacío.")
    private String nombre;

    // El teléfono debe tener exactamente 9 números
    @Size(min = 9, max = 9, message = "El teléfono debe tener exactamente 9 dígitos.")
    @NotBlank(message = "El teléfono es obligatorio.")
    private String telefono;

    // Valida automáticamente que tenga un formato de correo real (con @ y dominio)
    @Email(message = "El formato del correo electrónico no es válido.")
    @NotBlank(message = "El email es obligatorio.")
    private String email;

    private boolean es_vip;


    // 🔍 Constructor vacío OBLIGATORIO para que Hibernate (JPA) pueda funcionar
    public Contacto() {
    }



    // --- EL CONSTRUCTOR (La Fábrica) ---
    // Este metodo especial se ejecuta al hacer "new Contacto(...)".
    // Obliga a que, para crear un contacto, tengamos que meterle los 3 datos de golpe.
    public Contacto(String nombre, String telefono, String email, boolean es_vip){
        // 'this.nombre' significa el atributo privado de arriba.
        // '= nombre' es el texto que acaba de entrar por el paréntesis.
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.es_vip = es_vip;
    }

    // --- GETTERS Y SETTERS (Las Ventanillas de Acceso) ---
    // Como las variables son privadas, usamos estos métodos públicos para interactuar con ellas.

    // GET: Ventanilla de lectura. Sirve para "conseguir" el dato.
    public String getNombre() {
        return nombre;
    }

    // SET: Ventanilla de escritura. Sirve para "cambiar" el dato de forma controlada.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEs_vip(){
        return es_vip;
    }

    public void setEs_vip(boolean es_vip){
        this.es_vip =es_vip;
    }


    // --- EL TRADUCTOR A JSON ---
    // Este metodo coge las variables de la persona y las empaqueta en un texto
    // con el formato estricto que entienden las páginas web.
    public String aJson() {



// "  :(La primera de todas) Le dice a Java: "¡Eh, empiezo a escribir un texto largo!".
//    :Son simplemente dos espacios en blanco para que el JSON quede metido hacia la derecha (sangría) y se vea bonito.
// \" :Le dice a Java: "Imprime una comilla de verdad por la consola". (Aquí empieza la etiqueta de JSON).
// nombre :La palabra literal.
// \" :Le dice a Java: "Imprime otra comilla de verdad". (Aquí cierra la etiqueta).
// :  :Los dos puntos literales que separan la etiqueta del valor, y un espacio.
// \" :Le dice a Java: "Imprime otra comilla de verdad". (Para abrir el valor que va a tener el nombre).
// "  :(Comilla normal) Le dice a Java: "Pausa el texto aquí un momento, que voy a meter una variable".
// + this.nombre + :Java coge el nombre de verdad (ej: Ismael) y lo pega ahí en medio.
// "  :(Comilla normal) Le dice a Java: "Vale, sigo escribiendo texto literal".
// \" :Le dice a Java: "Imprime otra comilla de verdad". (Para cerrar el valor del nombre).
// ,  :La coma literal que exige JSON para separar un elemento del siguiente (el teléfono irá después).
// \n :Le dice a Java: "Pulsa la tecla ENTER aquí".
// "  :(La última) Le dice a Java: "¡Fin, ya no escribo más texto!".

        return "{\n" +
                "  \"nombre\": \"" + this.nombre + "\",\n" +
                "  \"telefono\": \"" + this.telefono + "\",\n" +
                "  \"email\": \"" + this.email + "\"\n" +
                "}";
    }
    // Nota: Usamos \" para decirle a Java que queremos imprimir unas comillas dobles de verdad dentro del texto,
    // sin que crea que estamos cerrando el String
}





