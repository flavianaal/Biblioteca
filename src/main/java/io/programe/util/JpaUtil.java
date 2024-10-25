/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.programe.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author Flaviana Andrade
 */
public class JpaUtil {
    
     //Fábrica de EntityManager, usada para criar instâncias de EntityManager
    private static EntityManagerFactory emf;

    static {
        emf = Persistence.createEntityManagerFactory("BibliotecaPU");

    }

    public static EntityManager conexao() {
        return emf.createEntityManager();
    }

    public static void fecharConexao() {
        emf.close();
    }
    
}
