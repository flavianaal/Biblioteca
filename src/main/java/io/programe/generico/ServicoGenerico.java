/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.generico;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Flaviana Andrade
 * @param <T>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ServicoGenerico<T> {

    // Tipo da entidade para operações genéricas
    private Class<T> entidade;

    // Gerenciador de entidades para operações de persistência
    @PersistenceContext
    public EntityManager entityManager;

    // Construtor que recebe a classe da entidade
    public ServicoGenerico(Class<T> entidade) {
        this.entidade = entidade;
    }

    // Método para salvar uma nova entidade
    public void salvar(T entidade) {
        entityManager.persist(entidade);
    }

    // Método para atualizar uma entidade existente
    public void atualizar(T entidade) {
        entityManager.merge(entidade);
    }

    // Método para excluir uma entidade
    public void delete(T entidade) {
        // Merge a entidade para garantir que está gerenciada pelo EntityManager
        entidade = entityManager.merge(entidade);
        // Remove a entidade do banco de dados
        entityManager.remove(entidade);
    }

    // Método para encontrar uma entidade pelo seu ID
    public T find(Long id) {
        try {
            // Verifica se o ID é nulo
            if (id == null) {
                throw new IllegalArgumentException("ID não pode ser nulo.");
            }

            // Encontra a entidade pelo ID
            T objeto = getEntityManager().find(entidade, id);

            // Verifica se o objeto foi encontrado
            if (objeto == null) {
                throw new EntityNotFoundException("Entidade não encontrada para o ID: " + id);
            }

            // Retorna o objeto encontrado
            return objeto;

        } catch (IllegalArgumentException e) {
            // Lida com o caso de ID nulo ou inválido
            System.out.println("Erro: " + e.getMessage());
            return null; // ou lançar a exceção novamente, dependendo do caso
        } catch (EntityNotFoundException e) {
            // Lida com o caso onde a entidade não foi encontrada
            System.out.println("Erro: " + e.getMessage());
            return null; // ou lançar a exceção novamente, dependendo do caso
        } catch (Exception e) {
            // Lida com outros erros inesperados
            System.out.println("Erro inesperado ao buscar a entidade: " + e.getMessage());
            e.printStackTrace(); // Para depuração
            return null;
        }
    }

    // Método para encontrar todas as entidades ativas
    public List<T> findAll() {
        // Cria e executa uma consulta JPQL para encontrar todas as entidades ativas
        return entityManager.createQuery("SELECT e FROM " + entidade.getSimpleName() + " e", entidade).getResultList();
    }

    public List<T> findAllAtivos() {
        // Cria e executa uma consulta JPQL para encontrar todas as entidades ativas
        return entityManager.createQuery("SELECT e FROM " + entidade.getSimpleName() + " e WHERE e.ativo = true", entidade).getResultList();
    }

}
