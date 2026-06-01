package br.ufpb.dcx.iago.atividades.exercicio5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GerenteDeJogo implements Serializable {
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

        if (user == null) throw new Exception("Usuário não encontrado.");
        if (jogo == null) throw new Exception("Jogo não encontrado.");
        if (user.getBiblioteca().contains(jogo)) throw new Exception("O usuário já possui este jogo.");

        // Regra de negócio: debita saldo e adiciona jogo
        user.debitarSaldo(jogo.getPreco());
        user.adicionarJogo(jogo);

        // Registra o histórico
        Compra novaCompra = new Compra(user, jogo);
        historicoDeCompras.add(novaCompra);

        return novaCompra;
    }
}