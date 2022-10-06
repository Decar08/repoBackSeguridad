package Grupo2.Registraduria.seguridad.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Grupo2.Registraduria.seguridad.Modelos.Rol;
import Grupo2.Registraduria.seguridad.Repositorios.RepositorioRol;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/rol")
public class ControladorRol {
    @Autowired
    private RepositorioRol miRepositorioRol;


    @GetMapping("")
    public List<Rol> index(){
        return this.miRepositorioRol.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Rol> create(@RequestBody  Rol infoRol){
        return new ResponseEntity<>(this.miRepositorioRol.save(infoRol),HttpStatus.OK);
    }
    @GetMapping("{id}")
    public Rol show(@PathVariable String id){
        Rol rolActual=this.miRepositorioRol
                .findById(id)
                .orElse(null);
        return rolActual;
    }
    @PutMapping("/{id}")
    public ResponseEntity<Rol> update(@PathVariable String id,@RequestBody  Rol infoRol){
        Rol rolActual=this.miRepositorioRol
                .findById(id)
                .orElse(null);
        if (rolActual!=null){
            rolActual.setNombre(infoRol.getNombre());
            rolActual.setDescripcion(infoRol.getDescripcion());
            return new ResponseEntity<>(this.miRepositorioRol.save(rolActual), HttpStatus.OK);
        }else{
            return  new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Rol rolActual=this.miRepositorioRol.findById(id).orElse(null);
        if (rolActual!=null){
            this.miRepositorioRol.delete(rolActual);
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "El Rol fue elmininado.");
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El Id del Rol ingresado no existe");
        }
    }
}
