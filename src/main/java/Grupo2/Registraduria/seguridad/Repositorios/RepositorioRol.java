package Grupo2.Registraduria.seguridad.Repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;

import Grupo2.Registraduria.seguridad.Modelos.Rol;

public interface RepositorioRol extends MongoRepository<Rol, String> {
    
}
