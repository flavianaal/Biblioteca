/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.manager;

import io.programe.modelo.Autor;
import io.programe.servico.AutorServico;
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
public class MangerAutor implements Serializable{
    
     @EJB
    private AutorServico autorServico;

    private Autor autor;
    private List<Autor> autores;

    // Método que será executado após a construção do bean
    @PostConstruct
    public void instanciar() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String visualizar = params.get("visualizar");
        String editar = params.get("editar");

        if (visualizar != null) {
            autor = autorServico.find(Long.valueOf(visualizar));

        } else if (editar != null) {
            autor = autorServico.find(Long.valueOf(visualizar));
            
        } else {
            autor = new Autor();
        }
    }

    // Método para salvar um novo Autor
    public void salvarAutor() {
        if (autor.getId() == null) {
            autorServico.salvar(autor);
            instanciar();
            System.out.println(autor);
        } else {
            autorServico.atualizar(autor);
        }
        Mensagem.msg("Operação Realizada com sucesso");
    }
}
