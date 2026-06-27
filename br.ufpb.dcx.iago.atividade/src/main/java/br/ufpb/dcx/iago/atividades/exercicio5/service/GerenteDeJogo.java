package br.ufpb.dcx.iago.atividades.exercicio5.service;

import br.ufpb.dcx.iago.atividades.exercicio5.model.Compra;
import br.ufpb.dcx.iago.atividades.exercicio5.model.User;
import br.ufpb.dcx.iago.atividades.exercicio5.model.Jogo;
import br.ufpb.dcx.iago.atividades.exercicio5.exception.UsuarioNaoEncontradoException;
import br.ufpb.dcx.iago.atividades.exercicio5.exception.JogoNaoEncontradoException;
import br.ufpb.dcx.iago.atividades.exercicio5.exception.UsuarioJaPossuiJogoException;

import java.io.IOException;
import br.ufpb.dcx.iago.atividades.exercicio5.persistence.GravadorDeDados;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GerenteDeJogo implements Serializable, SistemaLojaDeJogos {
    private static final long serialVersionUID = 1L;

    private GerenciadorDeJogo gerenciadorDeJogo;
    private GerenciadorDeUser gerenciadorDeUser;
    private List<Compra> historicoDeCompras;

    public GerenteDeJogo() {
        this.gerenciadorDeJogo = new GerenciadorDeJogo();
        this.gerenciadorDeUser = new GerenciadorDeUser();
        this.historicoDeCompras = new ArrayList<>();
    }

    public GerenciadorDeJogo getGerenciadorDeJogo() { return gerenciadorDeJogo; }
    public GerenciadorDeUser getGerenciadorDeUser() { return gerenciadorDeUser; }
    public List<Compra> getHistoricoDeCompras() { return historicoDeCompras; }

    public Compra realizarCompra(String idUser, String idJogo) throws Exception {
        // Como a interface retorna Object, é necessário o Casting explícito aqui
        User user = (User) gerenciadorDeUser.buscar(idUser);
        Jogo jogo = (Jogo) gerenciadorDeJogo.buscar(idJogo);

        if (user == null) throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        if (jogo == null) throw new JogoNaoEncontradoException("Jogo não encontrado.");
        if (user.getBiblioteca().contains(jogo)) throw new UsuarioJaPossuiJogoException("O usuário já possui este jogo.");

        // Regra de negócio: debita saldo e adiciona jogo
        user.debitarSaldo(jogo.getPreco());
        user.adicionarJogo(jogo);

        // Registra o histórico
        Compra novaCompra = new Compra(user, jogo);
        historicoDeCompras.add(novaCompra);

        return novaCompra;
    }

    @Override
    public void salvarDados() throws IOException {
        GravadorDeDados gravador = new GravadorDeDados("sistema.dat");
        gravador.gravar(this);
    }

    @Override
    public void recuperarDados() throws IOException {
        GravadorDeDados gravador = new GravadorDeDados("sistema.dat");
        try {
            GerenteDeJogo recuperado = (GerenteDeJogo) gravador.recuperar();
            this.gerenciadorDeJogo = recuperado.gerenciadorDeJogo;
            this.gerenciadorDeUser = recuperado.gerenciadorDeUser;
            this.historicoDeCompras = recuperado.historicoDeCompras;
        } catch (ClassNotFoundException e) {
            throw new IOException("Classe não encontrada ao recuperar dados", e);
        }
    }

    @Override
    public List<Jogo> pesquisarJogosPorNome(String nome) {
        return this.gerenciadorDeJogo.buscarPorNome(nome);
    }
}
