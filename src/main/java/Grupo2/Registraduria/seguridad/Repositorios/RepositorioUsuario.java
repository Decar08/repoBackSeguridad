package Grupo2.Registraduria.seguridad.Repositorios;
import Grupo2.Registraduria.seguridad.Modelos.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface RepositorioUsuario extends MongoRepository<Usuario,String> {

    @Query("{'correo': ?0}")
    public Usuario getUserByEmail(String correo);


    Collection<Object> findBySeudonimo(String seudonimo);

    Collection<Object> findByCorreo(String correo);
}
