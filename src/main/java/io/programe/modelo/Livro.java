/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.io.Serializable;
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
public class Livro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_livro")
    @SequenceGenerator(name = "seq_livro", sequenceName = "seq_livro", initialValue = 1)
    private Long id;
    private String titulo;
    private Integer anoPublicacao;
    private Boolean disponivel;
    // Muitos livros podem ter um mesmo autor
    @ManyToOne(cascade = CascadeType.ALL)
    private Autor autor;
    @Column(nullable = false)
    private Boolean ativo = true;

    public Livro() {
    }

    public Livro(Long id, String titulo, Integer anoPublicacao, Boolean disponivel, Autor autor, Boolean ativo) {
        this.id = id;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.disponivel = disponivel;
        this.autor = autor;
        this.ativo = ativo;
    }

}
