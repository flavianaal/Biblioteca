/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.servico;


import io.programe.generico.ServicoGenerico;
import io.programe.modelo.Autor;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author Flaviana Andrade
 */
@Stateless
public class AutorServico extends ServicoGenerico<Autor>{
    
    @PersistenceContext
    private EntityManager em;

     public AutorServico() {
        super(Autor.class);
        
    }
    // Método para listar todos os autores
    public List<Autor> listarTodos() {
        return em.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
    }
    
    
    
    
}
