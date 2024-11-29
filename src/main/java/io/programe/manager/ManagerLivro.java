/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.manager;

import io.programe.modelo.Autor;
import io.programe.modelo.Livro;
import io.programe.servico.AutorServico;
import io.programe.servico.LivroServico;
import io.programe.util.Mensagem;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Flaviana Andrade
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Named
@ViewScoped
public class ManagerLivro implements Serializable {

    @EJB
    private LivroServico livroServico;

    @EJB
    private AutorServico autorServico;

    private Livro livro;
    private List<Livro> livrosDisponiveis;
    private Autor autor;

    @PostConstruct
    public void inicializar() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String visualizar = params.get("visualizar");
        String editar = params.get("editar");

        if (visualizar != null) {
            livro = livroServico.find(Long.valueOf(visualizar));
        } else if (editar != null) {
            livro = livroServico.find(Long.valueOf(editar));
        } else {
            // Limpa os dados do livro
            livro = new Livro();
            livro.setDisponivel(true); // Define o livro como disponível por padrão
        }

        // Inicializa o autor se estiver nulo
        if (livro.getAutor() == null) {
            livro.setAutor(new Autor()); // Inicializa um novo autor
        }
    }

    // Criar um novo livro
    public void criarLivro() {

        if (livro.getId() == null) {
            livroServico.salvar(livro);
            inicializar();
            System.out.println(livro);
        } else {
            livroServico.atualizar(livro);
            autorServico.atualizar(autor);
        }
        Mensagem.msg("Operação Realizada com sucesso");

    }

    // Atualizar informações de um livro
    public void atualizarLivro(Livro livro) {
        livroServico.atualizar(livro);
        Mensagem.msg("Livro atualizado com sucesso!");
    }

    public void pesquisar() {
        System.out.println("Titulo: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor().getNome());
        livrosDisponiveis = livroServico.listarLivrosDisponiveis(livro);
        if (livrosDisponiveis.isEmpty()) {
            Mensagem.msg("Nenhum livro disponível encontrado.");
        }

    }
    
    
    // Atualizar status de livro para disponível após devolução
    public void marcarComoDisponivel(Livro livro) {
        livro.setDisponivel(true);
        livroServico.atualizar(livro);
        pesquisar();
        Mensagem.msg("Livro marcado como disponível!");
    }


    public void emprestarLivro(Livro livro) {
        livro.setDisponivel(false);
        livroServico.atualizar(livro);
        pesquisar(); // Atualiza a lista para refletir a alteração
        Mensagem.msg("Livro emprestado com sucesso!");
    }

}
