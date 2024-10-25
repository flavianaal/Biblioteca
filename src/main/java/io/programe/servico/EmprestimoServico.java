/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.servico;

import io.programe.generico.ServicoGenerico;
import io.programe.modelo.Emprestimo;
import io.programe.modelo.Leitor;
import io.programe.modelo.Livro;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

/**
 *
 * @author Flaviana Andrade
 */
@Stateless
public class EmprestimoServico extends ServicoGenerico<Emprestimo> {

    @PersistenceContext
    private EntityManager em;

    public EmprestimoServico() {
        super(Emprestimo.class);

    }

    // Método para listar empréstimos ativos 
    public List<Emprestimo> listarEmprestimosAtivos() {
        return em.createQuery("SELECT e FROM Emprestimo e WHERE e.dataDevolucao IS NULL", Emprestimo.class)
                .getResultList();
    }

    // Método para buscar empréstimos por leitor
    public List<Emprestimo> buscarPorLeitor(Leitor leitor) {
        return em.createQuery("SELECT e FROM Emprestimo e WHERE e.leitor = :leitor", Emprestimo.class)
                .setParameter("leitor", leitor)
                .getResultList();
    }

    public List<Livro> findLivro(String titulo) {
        String sql = "SELECT l FROM Livro l WHERE l.ativo = true";

        if (titulo != null && !titulo.isEmpty()) {
            sql += " AND lower(l.titulo) LIKE lower(:titulo)";
        }

        sql += " ORDER BY l.titulo ASC";

        Query query = em.createQuery(sql, Livro.class);

        if (titulo != null && !titulo.isEmpty()) {
            query.setParameter("titulo", "%" + titulo.trim() + "%");
        }

        return query.getResultList();
    }

    public List<Leitor> findLeitor(String nome) {
        String sql = "SELECT leitor FROM Leitor leitor WHERE leitor.ativo = true";

        if (nome != null && !nome.isEmpty()) {
            sql += " AND lower(leitor.nome) LIKE lower(:nome)";
        }

        sql += " ORDER BY leitor.nome ASC";

        Query query = em.createQuery(sql, Leitor.class);

        if (nome != null && !nome.isEmpty()) {
            query.setParameter("nome", "%" + nome.trim() + "%");
        }

        return query.getResultList();
    }

}
