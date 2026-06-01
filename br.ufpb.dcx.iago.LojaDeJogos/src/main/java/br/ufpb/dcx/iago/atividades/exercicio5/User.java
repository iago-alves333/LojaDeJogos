package br.ufpb.dcx.iago.atividades.exercicio5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private double saldo;
    private List<Jogo> biblioteca;

    public User(String id, String nome, double saldoInicial) {
        this.id = id;
        this.nome = nome;
        this.saldo = saldoInicial;
        this.biblioteca = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public double getSaldo() { return saldo; }
    public List<Jogo> getBiblioteca() { return biblioteca; }

    public void adicionarSaldo(double valor) {
        this.saldo += valor;
    }

    public void debitarSaldo(double valor) throws SaldoInsuficienteException {
        if (this.saldo < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente para esta compra.");
        }
        this.saldo -= valor;
    }

    public void adicionarJogo(Jogo jogo) {
        if (!biblioteca.contains(jogo)) {
            biblioteca.add(jogo);
        }
    }
}