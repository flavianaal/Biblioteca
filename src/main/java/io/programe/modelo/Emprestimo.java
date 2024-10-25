/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import java.io.Serializable;
import java.time.LocalDate;
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
@Entity
public class Emprestimo implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_emprestimo")
    @SequenceGenerator(name = "seq_emprestimo", sequenceName = "seq_emprestimo", initialValue = 1)
    private Long id;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    @ManyToOne
    private Livro livro;
    @OneToOne(cascade = CascadeType.ALL)
    private Leitor leitor;

    public Emprestimo() {
    }

    public Emprestimo(Long id, LocalDate dataEmprestimo, LocalDate dataDevolucao, Livro livro, Leitor leitor) {
        this.id = id;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.livro = livro;
        this.leitor = leitor;
    }
    
    
    
}
