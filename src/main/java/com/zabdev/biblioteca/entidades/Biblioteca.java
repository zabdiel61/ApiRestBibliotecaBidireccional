package com.zabdev.biblioteca.entidades;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "biblioteca")
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String nombre;

    //cascada ya que si elimino una biblioteca se tienen que eliminar sus libros
    @OneToMany(mappedBy = "biblioteca", cascade = CascadeType.ALL)//mapped no es la propietaria de la relacion
    private Set<Libro> libros = new HashSet<>();

    public Biblioteca() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
        //cada que establezca libro, le indico que la que establezca es la actual, el objeto actual en el contexto
        for(Libro libro: libros){
            libro.setBiblioteca(this);
        }
    }

}
