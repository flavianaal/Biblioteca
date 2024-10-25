/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.generico;

import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Flaviana Andrade
 */
@MappedSuperclass
public abstract class EntidadeGenerica implements Serializable {
     // Atributo para indicar se a entidade está ativa
    public Boolean ativo;

    // Construtor padrão que define o estado inicial de 'ativo' como verdadeiro

    /**
     *
     */
    public EntidadeGenerica() {
        ativo = Boolean.TRUE; // Inicializa 'ativo' como verdadeiro
        setAtivo(ativo); // Define 'ativo' através do método setter

    }

    //Getter para o atributo 'ativo'
    public Boolean getAtivo() {
        return ativo;
    }

    //Setter para o atributo 'ativo'
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    //Sobrescrição do método hashCode para garantir a consistência do hash
    //com base no atributo 'ativo'
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.ativo);
        return hash;
    }

    // Sobrescrição do método equals para comparar se duas instâncias
    // são iguais com base no atributo 'ativo'
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntidadeGenerica other = (EntidadeGenerica) obj;
        return Objects.equals(this.ativo, other.ativo);
    }

    @Override
    public String toString() {
        return "EntidadeGenerica{" + "ativo=" + ativo + '}';
    }
    
}
