package Grupo2.Registraduria.seguridad.Repositorios;

import Grupo2.Registraduria.seguridad.Modelos.Permiso;
import Grupo2.Registraduria.seguridad.Modelos.PermisosRoles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface RepositorioPermiso extends MongoRepository<Permiso,String> {

    @Query("{'url':?0,'metodo':?1}")
    Permiso getPermiso(String url, String metodo);


}
