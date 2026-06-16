package br.ufpb.dcx.iago.atividades.exercicio5.service;

import java.util.List;

public interface Gerenciador {
    void adicionar(Object entidade) throws Exception;
    Object buscar(String id);
    List<Object> listarTodos();
    void remover(String id) throws Exception;
    boolean existe(String id);
}
