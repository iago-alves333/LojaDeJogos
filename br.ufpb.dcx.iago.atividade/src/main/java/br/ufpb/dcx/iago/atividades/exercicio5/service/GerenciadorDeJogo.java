package br.ufpb.dcx.iago.atividades.exercicio5.service;

import br.ufpb.dcx.iago.atividades.exercicio5.model.Jogo;
import br.ufpb.dcx.iago.atividades.exercicio5.exception.JogoJaExisteException;
import br.ufpb.dcx.iago.atividades.exercicio5.exception.TipoObjetoInvalidoException;
import br.ufpb.dcx.iago.atividades.exercicio5.exception.JogoNaoEncontradoException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciadorDeJogo implements Gerenciador, Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Jogo> catalogo;

    public GerenciadorDeJogo() {
        this.catalogo = new HashMap<>();
    }

    @Override
    public void adicionar(Object entidade) throws Exception {
        if (!(entidade instanceof Jogo)) {
            throw new TipoObjetoInvalidoException("Erro: Objeto não é do tipo Jogo.");
        }

        Jogo jogo = (Jogo) entidade;
        if (existe(jogo.getId())) {
            throw new JogoJaExisteException("O jogo " + jogo.getNome() + " já está cadastrado.");
        }
        catalogo.put(jogo.getId(), jogo);
    }

    @Override
    public Object buscar(String id) {
        return catalogo.get(id);
    }

    @Override
    public List<Object> listarTodos() {
        return new ArrayList<>(catalogo.values());
    }

    @Override
    public void remover(String id) throws Exception {
        if (!existe(id)) {
            throw new JogoNaoEncontradoException("Jogo não encontrado.");
        }
        catalogo.remove(id);
    }

    @Override
    public boolean existe(String id) {
        return catalogo.containsKey(id);
    }

    /**
     * Busca jogos que contenham o nome especificado.
     * @param nome O nome ou parte do nome do jogo a ser buscado.
     * @return Uma lista de jogos que correspondem à busca.
     */
    public List<Jogo> buscarPorNome(String nome) {
        List<Jogo> resultados = new ArrayList<>();
        if (nome == null || nome.trim().isEmpty()) {
            return resultados;
        }
        
        String busca = nome.toLowerCase();
        for (Jogo jogo : catalogo.values()) {
            if (jogo.getNome().toLowerCase().contains(busca)) {
                resultados.add(jogo);
            }
        }
        return resultados;
    }
}
