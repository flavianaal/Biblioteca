/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.servico;

import io.programe.generico.ServicoGenerico;
import io.programe.modelo.Leitor;
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
public class LeitorServico extends ServicoGenerico<Leitor> {

    @PersistenceContext
    private EntityManager em;

    public LeitorServico() {
        super(Leitor.class);

    }

    // Método para buscar leitores por nome
    public List<Leitor> buscarPorNome(String nome) {
        return em.createQuery("SELECT l FROM Leitor l WHERE l.nome LIKE :nome", Leitor.class)
                .setParameter("nome", "%" + nome + "%")
                .getResultList();
    }

    // Método para listar todos os leitores
    public List<Leitor> listarTodos() {
        return em.createQuery("SELECT l FROM Leitor l", Leitor.class).getResultList();
    }

    // Listar todos os leitores
    public List<Leitor> listarLeitores(Leitor leitor) {
        // Construindo a JPQL para buscar leitores ativos
        StringBuilder jpql = new StringBuilder("SELECT l FROM Leitor l WHERE l.ativo = true");
        List<String> conditions = new ArrayList<>();

        // Adicionando condições para buscar pelo nome do leitor
        if (leitor.getNome() != null && !leitor.getNome().isEmpty()) {
            conditions.add("lower(l.nome) LIKE lower(:nome)");
        }

        // Adicionando as condições à query
        if (!conditions.isEmpty()) {
            jpql.append(" AND ");
            jpql.append(String.join(" AND ", conditions));
        }

        // Criando a query baseada nas condições
        TypedQuery<Leitor> query = entityManager.createQuery(jpql.toString(), Leitor.class);

        // Configurando parâmetros
        if (leitor.getNome() != null && !leitor.getNome().isEmpty()) {
            query.setParameter("nome", "%" + leitor.getNome() + "%");
        }

        // Executando a consulta e retornando a lista de leitores
        return query.getResultList();

    }
    
    public Leitor findLeitorById(Long id) {
        return entityManager.find(Leitor.class, id);
    }

}