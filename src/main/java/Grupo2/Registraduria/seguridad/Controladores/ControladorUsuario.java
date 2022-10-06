package Grupo2.Registraduria.seguridad.Controladores;
import Grupo2.Registraduria.seguridad.Modelos.Rol;
import Grupo2.Registraduria.seguridad.Modelos.Usuario;
import Grupo2.Registraduria.seguridad.Repositorios.RepositorioRol;
import Grupo2.Registraduria.seguridad.Repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class ControladorUsuario {
    @Autowired
    private RepositorioUsuario miRepositorioUsuario;

    @Autowired
    private RepositorioRol miRepositorioRol;

    @GetMapping("")
    public List<Usuario> index(){
        return this.miRepositorioUsuario.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Usuario create(@RequestBody  Usuario infoUsuario){

        if(infoUsuario.getSeudonimo() == null || infoUsuario.getSeudonimo().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo seudonimo es requerido.");
        }
        if (infoUsuario.getContrasena() == null || infoUsuario.getContrasena().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo contraseña es requerido.");
        }
        if (infoUsuario.getCorreo() == null || infoUsuario.getCorreo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo correo es requerido.");
        }

        String seudonimo = infoUsuario.getSeudonimo();

        if(this.miRepositorioUsuario.findBySeudonimo(seudonimo).size() > 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Este seudonimo ya existe, intenta con otro.");
        }

        String correo = infoUsuario.getCorreo();

        if(this.miRepositorioUsuario.findByCorreo(correo).size() > 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Este correo ya existe, intenta con otro.");
        }

        infoUsuario.setContrasena(convertirSHA256(infoUsuario.getContrasena()));
        if(infoUsuario.getContrasena() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo contraseña es requerido.");
        } else {
            return this.miRepositorioUsuario.save(infoUsuario);
        }

    }
    @GetMapping("{id}")
    public Usuario show(@PathVariable String id){
        Usuario usuarioActual=this.miRepositorioUsuario.findById(id).orElse(null);
        if(usuarioActual==null){
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "El usuario no fue encontrado");
        }
        return usuarioActual;
    }
    @PutMapping("{id}")
    public Usuario update(@PathVariable String id,@RequestBody  Usuario infoUsuario){

        if(infoUsuario.getSeudonimo() == null || infoUsuario.getSeudonimo().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo seudonimo es requerido.");
        }
        if (infoUsuario.getContrasena() == null || infoUsuario.getContrasena().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo contraseña es requerido.");
        }
        if (infoUsuario.getCorreo() == null || infoUsuario.getCorreo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo correo es requerido.");
        }

        String seudonimo = infoUsuario.getSeudonimo();

        if(this.miRepositorioUsuario.findBySeudonimo(seudonimo).size() > 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Este seudonimo ya existe, intenta con otro.");
        }

        String correo = infoUsuario.getCorreo();

        if(this.miRepositorioUsuario.findByCorreo(correo).size() > 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Este correo ya existe, intenta con otro.");
        }

        Usuario usuarioActual=this.miRepositorioUsuario.findById(id).orElse(null);
        if (usuarioActual!=null){
            usuarioActual.setSeudonimo(infoUsuario.getSeudonimo());
            usuarioActual.setCorreo(infoUsuario.getCorreo());
            usuarioActual.setContrasena(convertirSHA256(infoUsuario.getContrasena()));
            return this.miRepositorioUsuario.save(usuarioActual);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo actualizar usuario, Id no existe");
            //return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Usuario usuarioActual=this.miRepositorioUsuario.findById(id).orElse(null);
        if (usuarioActual!=null){
            this.miRepositorioUsuario.delete(usuarioActual);
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "El usuario fue elmininado.");
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El Id del usuario ingresado no existe");
        }
    }
    @PutMapping("{id}/rol/{id_rol}")
    public Usuario asignarRolAUsuario(@PathVariable String id,@PathVariable String id_rol){
        Usuario usuarioActual=this.miRepositorioUsuario
                .findById(id)
                .orElse(null);
        if (usuarioActual == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");
        }
        Rol rolActual=this.miRepositorioRol.findById(id_rol).orElse(null);
        usuarioActual.setRol(rolActual);
        return this.miRepositorioUsuario.save(usuarioActual);
    }
    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @PostMapping("/validar")
    public Usuario validate(@RequestBody  Usuario infoUsuario,
                            final HttpServletResponse response) throws IOException {
        Usuario usuarioActual=this.miRepositorioUsuario.getUserByEmail(infoUsuario.getCorreo());
        if (usuarioActual!=null && usuarioActual.getContrasena().equals(convertirSHA256(infoUsuario.getContrasena()))) {
            usuarioActual.setContrasena("");
            return usuarioActual;
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario o contraseña incorecto, vuelve a intentar.");
            return null;
        }
    }
}
