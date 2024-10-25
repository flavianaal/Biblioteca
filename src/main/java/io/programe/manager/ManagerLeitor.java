/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.manager;

import io.programe.modelo.Leitor;
import io.programe.servico.LeitorServico;
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
public class ManagerLeitor implements Serializable{
    
    @EJB
    private LeitorServico leitorServico;

    private Leitor leitor;

    private List<Leitor> leitores;

    @PostConstruct
    public void instanciar() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String visualizar = params.get("visualizar");
        String editar = params.get("editar");

        if (visualizar != null) {
            leitor = leitorServico.find(Long.valueOf(visualizar));

        } else if (editar != null) {
            leitor = leitorServico.find(Long.valueOf(editar));
            
        } else {
            leitor = new Leitor();
        }
       
    }

    // Criar um novo leitor
    public void criarLeitor() {
        if (leitor.getId() == null) {
            leitorServico.salvar(leitor);
            instanciar();
            System.out.println(leitor);
        } else {
            leitorServico.atualizar(leitor);
            
        }
        Mensagem.msg("Operação Realizada com sucesso");
       
    }

    // Atualizar informações de um leitor
    public void atualizarLeitor(Leitor leitor) {
        leitorServico.atualizar(leitor);
        Mensagem.msg("Leitor atualizado com sucesso!");
    }
    
    public void pesquisar() {
        System.out.println("Nome: " + leitor.getNome());
        leitores = leitorServico.listarLeitores(leitor);
       

    }

    
    
}
