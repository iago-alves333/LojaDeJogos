package br.ufpb.dcx.iago.atividades.exercicio5.service;

import br.ufpb.dcx.iago.atividades.exercicio5.model.Compra;
import br.ufpb.dcx.iago.atividades.exercicio5.model.Jogo;
import java.io.IOException;
import java.util.List;

/**
 * Interface que define as operações principais do sistema de Loja de Jogos.
 */
public interface SistemaLojaDeJogos {
    
    /**
     * Realiza a compra de um jogo para um usuário.
     * @param idUser O ID do usuário.
     * @param idJogo O ID do jogo a ser comprado.
     * @return O objeto Compra gerado.
     * @throws Exception Caso o usuário ou jogo não existam, o saldo seja insuficiente, etc.
     */
    public Compra realizarCompra(String idUser, String idJogo) throws Exception;
    
    /**
     * Salva os dados do sistema em arquivo.
     * @throws IOException Se ocorrer um erro durante a gravação dos dados.
     */
    public void salvarDados() throws IOException;
    
    /**
     * Recupera os dados do sistema a partir de um arquivo.
     * @throws IOException Se ocorrer um erro durante a leitura dos dados.
     */
    public void recuperarDados() throws IOException;
    
    /**
     * Busca jogos pelo nome ou parte do nome.
     * @param nome O nome ou termo de busca.
     * @return Uma lista com os jogos encontrados.
     */
    public List<Jogo> pesquisarJogosPorNome(String nome);
}
