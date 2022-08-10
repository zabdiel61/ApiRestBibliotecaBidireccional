package com.zabdev.biblioteca.repositorios;

import com.zabdev.biblioteca.entidades.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, Integer> {

}
