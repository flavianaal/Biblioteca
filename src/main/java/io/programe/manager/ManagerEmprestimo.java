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
import jakarta.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private Long livroId;
    private Long leitorId;
    private Emprestimo emprestimo;
    private List<Emprestimo> emprestimosAtivos;

    @PostConstruct
    public void instanciar() {

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String visualizar = params.get("visualizar");
        String editar = params.get("editar");

        if (visualizar != null) {
            emprestimo = emprestimoServico.find(Long.valueOf(visualizar));
        } else if (editar != null) {
            emprestimo = emprestimoServico.find(Long.valueOf(editar));
        } else {
            emprestimo = new Emprestimo();
        }
        emprestimo.setLivro(new Livro());
        emprestimo.setLeitor(new Leitor());

        listarEmprestimosAtivos();
    }

    public void salvarEmprestimo() {
        try {
            if (!emprestimo.getLivro().isDisponivel()) {
                Mensagem.msg("Este livro já está emprestado e não pode ser emprestado novamente.");
                return;
            }

            if (emprestimo.getId() == null) {
                emprestimo.setAtivo(true); // Marca o empréstimo como ativo
                emprestimo.getLivro().setDisponivel(false); // Marca o livro como indisponível
                livroServico.atualizar(emprestimo.getLivro()); // Certifique-se de que o livro seja atualizado no banco
                emprestimoServico.salvar(emprestimo);
                Mensagem.msg("Empréstimo realizado com sucesso!");
            } else {
                emprestimoServico.atualizar(emprestimo);
                Mensagem.msg("Empréstimo atualizado com sucesso!");
            }
        } catch (Exception e) {
            Mensagem.msg("Erro ao salvar empréstimo: " + e.getMessage());
        } finally {
            instanciar(); // Recarrega estado
        }
    }

    public void devolverLivro(Emprestimo emprestimo) {
        try {
            emprestimoServico.registrarDevolucao(emprestimo.getId());
            Mensagem.msg("Livro devolvido com sucesso!");
        } catch (EntityNotFoundException e) {
            Mensagem.msg("Erro: Empréstimo não encontrado.");
        } catch (IllegalStateException e) {
            Mensagem.msg(e.getMessage());
        } catch (Exception e) {
            Mensagem.msg("Erro inesperado ao devolver livro: " + e.getMessage());
        } finally {
            listarEmprestimosAtivos();
        }
    }

    public void listarEmprestimosAtivos() {
        emprestimosAtivos = emprestimoServico.listarEmprestimosAtivos();
    }

    public List<Livro> autocompleteLivro(String titulo) {
        return emprestimoServico.findLivro(titulo);

    }

    public List<Leitor> autocompleteLeitor(String nome) {
        return emprestimoServico.findLeitor(nome);

    }
}
