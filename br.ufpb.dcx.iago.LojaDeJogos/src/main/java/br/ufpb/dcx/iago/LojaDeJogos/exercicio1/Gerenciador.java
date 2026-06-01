package br.ufpb.dcx.iago.LojaDeJogos.exercicio1;
import java.util.List;

public interface Gerenciador {
    void adicionar(Object entidade) throws Exception;
    Object buscar(String id);
    List<Object> listarTodos();
    void remover(String id) throws Exception;
    boolean existe(String id);
}