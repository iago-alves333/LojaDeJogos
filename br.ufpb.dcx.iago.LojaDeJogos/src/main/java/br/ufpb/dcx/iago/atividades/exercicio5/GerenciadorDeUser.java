package br.ufpb.dcx.iago.atividades.exercicio5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciadorDeUser implements Gerenciador, Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, User> usuarios;

    public GerenciadorDeUser() {
        this.usuarios = new HashMap<>();
    }

    @Override
    public void adicionar(Object entidade) throws Exception {
        if (!(entidade instanceof User)) {
            throw new Exception("Erro: Objeto não é do tipo User.");
        }

        User user = (User) entidade;
        if (existe(user.getId())) {
            throw new Exception("O usuário " + user.getNome() + " já está cadastrado.");
        }
        usuarios.put(user.getId(), user);
    }

    @Override
    public Object buscar(String id) {
        return usuarios.get(id);
    }

    @Override
    public List<Object> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public void remover(String id) throws Exception {
        if (!existe(id)) {
            throw new Exception("Usuário não encontrado.");
        }
        usuarios.remove(id);
    }

    @Override
    public boolean existe(String id) {
        return usuarios.containsKey(id);
    }
}