package com.zabdev.biblioteca.controladores;

import com.zabdev.biblioteca.entidades.Biblioteca;
import com.zabdev.biblioteca.entidades.Libro;
import com.zabdev.biblioteca.repositorios.BibliotecaRepository;
import com.zabdev.biblioteca.repositorios.LibroRepository;
import java.net.URI;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRespository;

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @GetMapping
    public ResponseEntity<Page<Libro>> listarLibros(Pageable pageable) {
        return ResponseEntity.ok(libroRespository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Long id) {
        Optional<Libro> libroOptional = libroRespository.findById(id);

        //si biblioteca no tiene nada
        if (!libroOptional.isPresent()) {
            //retoramos que no se ha podido procesar esta entidad
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(libroOptional.get());
    }

    @PostMapping
    public ResponseEntity<Libro> guardarLibro(@Valid @RequestBody Libro libro) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(libro.getBiblioteca().getId());

        if (!bibliotecaOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        libro.setBiblioteca(bibliotecaOptional.get());
        Libro libroGuardado = libroRespository.save(libro);

        //del objeto biblioteca creamos una nueva uri, con su id actual, el que estamos creando
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")//le pasamos la peticion actual
                .buildAndExpand(libroGuardado.getId()).toUri();

        return ResponseEntity.created(ubicacion).body(libroGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@Valid @RequestBody Libro libro, @PathVariable Long id) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(libro.getBiblioteca().getId());

        if (!bibliotecaOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Libro> libroOptional = libroRespository.findById(id);

        if (!libroOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        libro.setBiblioteca(bibliotecaOptional.get());
        libro.setId(libroOptional.get().getId());
        libroRespository.save(libro);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Libro> eliminarLibro(@PathVariable Long id) {
        Optional<Libro> libroOptional = libroRespository.findById(id);

        if (!libroOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        libroRespository.delete(libroOptional.get());

        return ResponseEntity.noContent().build();
    }

}
