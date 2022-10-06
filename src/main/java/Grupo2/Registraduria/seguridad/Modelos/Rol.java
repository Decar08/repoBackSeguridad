package Grupo2.Registraduria.seguridad.Modelos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Rol {
    @Id
    private String _id;
    private String nombre;
    private String descripcion;

    public Rol(String nombre, String descripcion) {
        this._id = String.valueOf(System.currentTimeMillis());
        this.nombre = nombre;
        this.descripcion = descripcion;
    }


    public String get_id() {
        return this._id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
