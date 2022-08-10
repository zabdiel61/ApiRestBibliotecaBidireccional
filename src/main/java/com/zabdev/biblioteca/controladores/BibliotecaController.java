package com.zabdev.biblioteca.controladores;

import com.zabdev.biblioteca.entidades.Biblioteca;
import com.zabdev.biblioteca.repositorios.BibliotecaRepository;
import java.net.URI;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @GetMapping
    public ResponseEntity<Page<Biblioteca>> listarBibliotecas(Pageable pageable) {
        return ResponseEntity.ok(bibliotecaRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Biblioteca> obtenerBibliotecaPorId(@PathVariable Long id, @RequestBody Biblioteca biblioteca) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id);

        //si biblioteca no tiene nada
        if (!bibliotecaOptional.isPresent()) {
            //retoramos que no se ha podido procesar esta entidad
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(bibliotecaOptional.get());
    }

    @PostMapping
    public ResponseEntity<Biblioteca> guardarBiblioteca(@Valid @RequestBody Biblioteca biblioteca) {
        Biblioteca bibliotecaGuardada = bibliotecaRepository.save(biblioteca);

        //del objeto biblioteca creamos una nueva uri, con su id actual, el que estamos creando
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")//le pasamos la peticion actual
                .buildAndExpand(bibliotecaGuardada.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(bibliotecaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Biblioteca> actualizarBiblioteca(@PathVariable Long id, @RequestBody Biblioteca biblioteca) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id);

        //si biblioteca no tiene nada
        if (!bibliotecaOptional.isPresent()) {
            //retoramos que no se ha podido procesar esta entidad
            return ResponseEntity.unprocessableEntity().build();
        }

        //obtenemos y guardamos en la bd
        biblioteca.setId(bibliotecaOptional.get().getId());
        bibliotecaRepository.save(biblioteca);

        //retornamos nada
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Biblioteca> eliminarBiblioteca(@PathVariable Long id) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id);

        if (!bibliotecaOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        bibliotecaRepository.delete(bibliotecaOptional.get());

        //retornamos nada
        return ResponseEntity.noContent().build();
    }

}
