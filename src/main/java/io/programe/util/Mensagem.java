/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

/**
 *
 * @author Flaviana Andrade
 */
public class Mensagem {

    //Adiciona uma mensagem ao contexto atual do JSF
    public static void msg(String mensagem) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(mensagem));
    }

}
