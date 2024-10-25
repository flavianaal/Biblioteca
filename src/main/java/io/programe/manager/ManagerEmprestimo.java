/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.manager;

import io.programe.modelo.Emprestimo;
import io.programe.modelo.Leitor;
import io.programe.modelo.Livro;
import io.programe.servico.EmprestimoServico;
import io.programe.servico.LeitorServico;
import io.programe.servico.LivroServico;
import io.programe.util.Mensagem;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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
public class ManagerEmprestimo implements Serializable {

    @EJB
    private EmprestimoServico emprestimoServico;

    @EJB
    private LivroServico livroServico;
    @EJB
    private LeitorServico leitorServico;
    private Livro livro;
    private Leitor leitor;
    private Emprestimo emprestimo;

    @PostConstruct
    public void instanciar() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String visualizar = params.get("visualizar");
        String editar = params.get("editar");
        // Se o parâmetro visualizar ou editar estiver presente, carrega o funcionário e o endereço correspondentes
        if (visualizar != null) {
            leitor = leitorServico.find(Long.parseLong(visualizar));
            livro = livroServico.findLivroById(livro.getId());

        } else if (editar != null) {
            leitor = leitorServico.find(Long.parseLong(visualizar));
            livro = livroServico.findLivroById(livro.getId());
        } else {
            // Caso contrário, inicializa um novo funcionário e endereço
            livro = new Livro();
            leitor = new Leitor();
            emprestimo = new Emprestimo();
        }
        //escola.setEndereco(new Endereco());
        emprestimo.setLivro(livro);
        emprestimo.setLeitor(leitor);
    }

    public boolean validarLeitorELivro(Leitor leitor, Livro livro) {
        // Verifica se o leitor e o livro foram fornecidos e têm IDs válidos
        if (leitor == null || leitor.getId() == null) {
            Mensagem.msg("Leitor inválido ou ID do leitor não fornecido.");
            return false;
        }
        if (livro == null || livro.getId() == null) {
            Mensagem.msg("Livro inválido ou ID do livro não fornecido.");
            return false;
        }

        // Busca o leitor e o livro no banco de dados
        Leitor leitorBanco = leitorServico.findLeitorById(leitor.getId());
        Livro livroBanco = livroServico.findLivroById(livro.getId());

        // Verifica se o leitor e o livro existem e estão ativos
        if (leitorBanco == null || !leitorBanco.getAtivo()) {
            Mensagem.msg("Leitor inválido ou inativo.");
            return false;
        }
        if (livroBanco == null || !livroBanco.getAtivo()) {
            Mensagem.msg("Livro inválido ou inativo.");
            return false;
        }

        return true; // Ambos são válidos e ativos
    }

    public void registrarEmprestimo() {
        try {
            // Validar leitor e livro
            if (!validarLeitorELivro(leitor, livro)) {
                return; // Saia se a validação falhar
            }

            // Verificar disponibilidade do livro
            if (!livroServico.isLivroDisponivel(livro.getId())) {
                Mensagem.msg("Livro não está disponível para empréstimo.");
                return;
            }

            // Continua o processo de registro de empréstimo
            emprestimo.setLeitor(leitor);
            emprestimo.setLivro(livro);
            emprestimo.setDataEmprestimo(LocalDate.now());

            // Atualiza a disponibilidade do livro
            livro.setDisponivel(false);

            // Salva o empréstimo e atualiza o livro
            emprestimoServico.salvar(emprestimo);
            livroServico.atualizar(livro);

            Mensagem.msg("Empréstimo registrado com sucesso!");

        } catch (Exception e) {
            Mensagem.msg("Erro ao registrar o empréstimo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            instanciar();
        }
    }

    // Método para devolver um livro
    public void devolverLivro(Emprestimo emprestimo) {
        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true); // Marca o livro como disponível
        emprestimo.setDataDevolucao(LocalDate.now());

        // Atualiza o empréstimo e o status do livro
        emprestimoServico.atualizar(emprestimo);
        livroServico.atualizar(livro);

        Mensagem.msg("Livro devolvido com sucesso!");
    }

    public List<Livro> autocompleteLivro(String titulo) {
        return emprestimoServico.findLivro(titulo);

    }

    public List<Leitor> autocompleteLeitor(String nome) {
        return emprestimoServico.findLeitor(nome);

    }

    private Object getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
