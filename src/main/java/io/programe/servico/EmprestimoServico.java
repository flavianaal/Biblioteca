package io.programe.servico;

import io.programe.generico.ServicoGenerico;
import io.programe.modelo.Emprestimo;
import io.programe.modelo.Leitor;
import io.programe.modelo.Livro;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class EmprestimoServico extends ServicoGenerico<Emprestimo> {

    @PersistenceContext
    private EntityManager em;

    public EmprestimoServico() {
        super(Emprestimo.class);
    }

    // Registrar um novo empréstimo
    public void registrarEmprestimo(Long livroId, Long leitorId) {
        if (livroId == null || leitorId == null) {
            throw new IllegalArgumentException("Livro ou Leitor não fornecido.");
        }

        Livro livro = validarLivro(livroId);
        Leitor leitor = validarLeitor(leitorId);

        // Verificar se o leitor já tem um empréstimo ativo para o mesmo livro
        long emprestimosAtivos = em.createQuery(
                "SELECT COUNT(e) FROM Emprestimo e WHERE e.livro.id = :livroId AND e.leitor.id = :leitorId AND e.dataDevolucao IS NULL",
                Long.class)
                .setParameter("livroId", livroId)
                .setParameter("leitorId", leitorId)
                .getSingleResult();

        if (emprestimosAtivos > 0) {
            throw new IllegalStateException("O leitor já possui um empréstimo ativo para este livro.");
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setLeitor(leitor);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucao(null);

        livro.setDisponivel(false);

        em.persist(emprestimo);
        em.merge(livro);
    }

    public Livro validarLivro(Long livroId) {
        Livro livro = em.find(Livro.class, livroId);
        if (livro == null) {
            throw new EntityNotFoundException("Livro com ID " + livroId + " não encontrado.");
        }
        if (!livro.isDisponivel()) {
            throw new IllegalStateException("O livro '" + livro.getTitulo() + "' está indisponível para empréstimo.");
        }
        return livro;
    }

    public Leitor validarLeitor(Long leitorId) {
        Leitor leitor = em.find(Leitor.class, leitorId);
        if (leitor == null) {
            throw new EntityNotFoundException("Leitor não encontrado.");
        }
        return leitor;
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return em.createQuery("SELECT e FROM Emprestimo e WHERE e.dataDevolucao IS NULL", Emprestimo.class)
                .getResultList();
    }

    // Buscar livros disponíveis com filtro opcional por título
    public List<Livro> findLivro(String titulo) {
        String jpql = "SELECT l FROM Livro l WHERE l.disponivel = true";
        if (titulo != null && !titulo.isEmpty()) {
            jpql += " AND LOWER(l.titulo) LIKE LOWER(:titulo)";
        }
        jpql += " ORDER BY l.titulo ASC";

        var query = em.createQuery(jpql, Livro.class);
        if (titulo != null && !titulo.isEmpty()) {
            query.setParameter("titulo", "%" + titulo.trim() + "%");
        }

        return query.getResultList();
    }

    // Buscar leitores com filtro opcional por nome
    public List<Leitor> findLeitor(String nome) {
        String jpql = "SELECT leitor FROM Leitor leitor WHERE leitor.ativo = true";
        if (nome != null && !nome.isEmpty()) {
            jpql += " AND LOWER(leitor.nome) LIKE LOWER(:nome)";
        }
        jpql += " ORDER BY leitor.nome ASC";

        var query = em.createQuery(jpql, Leitor.class);
        if (nome != null && !nome.isEmpty()) {
            query.setParameter("nome", "%" + nome.trim() + "%");
        }

        return query.getResultList();
    }

    // Registrar a devolução de um empréstimo
    public void registrarDevolucao(Long emprestimoId) {
        if (emprestimoId == null) {
            throw new IllegalArgumentException("ID do empréstimo não fornecido.");
        }

        Emprestimo emprestimo = em.find(Emprestimo.class, emprestimoId);
        if (emprestimo == null) {
            throw new EntityNotFoundException("Empréstimo com ID " + emprestimoId + " não encontrado.");
        }

        if (emprestimo.getDataDevolucao() != null) {
            throw new IllegalStateException("O empréstimo já foi devolvido em " + emprestimo.getDataDevolucao() + ".");
        }

        emprestimo.setDataDevolucao(LocalDate.now());
        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);

        em.merge(emprestimo);
        em.merge(livro);
    }

}
