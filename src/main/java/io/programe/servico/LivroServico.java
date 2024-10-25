/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.servico;

import io.programe.generico.ServicoGenerico;
import io.programe.modelo.Livro;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Flaviana Andrade
 */
@Stateless
public class LivroServico extends ServicoGenerico<Livro> {

    @PersistenceContext
    private EntityManager em;

    public LivroServico() {
        super(Livro.class);

    }

    // Listar todos os livros disponíveis
    public List<Livro> listarLivrosDisponiveis(Livro livro) {
        StringBuilder jpql = new StringBuilder("SELECT l FROM Livro l WHERE l.ativo = true AND l.disponivel = true");
        List<String> conditions = new ArrayList<>();

        if (livro.getTitulo() != null && !livro.getTitulo().isEmpty()) {
            conditions.add("lower(l.titulo) LIKE lower(:titulo)");
        }

        if (livro.getAutor() != null && livro.getAutor().getNome() != null && !livro.getAutor().getNome().isEmpty()) {
            conditions.add("lower(l.autor.nome) LIKE lower(:autorNome)");
        }

        if (!conditions.isEmpty()) {
            jpql.append(" AND ").append(String.join(" AND ", conditions));
        }

        TypedQuery<Livro> query = em.createQuery(jpql.toString(), Livro.class);

        if (livro.getTitulo() != null && !livro.getTitulo().isEmpty()) {
            query.setParameter("titulo", "%" + livro.getTitulo() + "%");
        }

        if (livro.getAutor() != null && livro.getAutor().getNome() != null && !livro.getAutor().getNome().isEmpty()) {
            query.setParameter("autorNome", "%" + livro.getAutor().getNome() + "%");
        }

        return query.getResultList();
    }

    public Livro findLivroById(Long id) {
        return entityManager.find(Livro.class, id);
    }

    public boolean isLivroDisponivel(Long livroId) {
        Livro livro = findLivroById(livroId);
        return livro != null && livro.getDisponivel();
    }

}
