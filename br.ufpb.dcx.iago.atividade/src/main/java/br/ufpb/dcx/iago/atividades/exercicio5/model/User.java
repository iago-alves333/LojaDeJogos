package br.ufpb.dcx.iago.atividades.exercicio5.model;

import br.ufpb.dcx.iago.atividades.exercicio5.exception.SaldoInsuficienteException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private double saldo;
    private String senha;
    private boolean isAdmin;
    private List<Jogo> biblioteca;

    public User(String nome, double saldoInicial, String senha, boolean isAdmin) {
        this.id = gerarIdAleatorio();
        this.nome = nome;
        this.saldo = saldoInicial;
        this.senha = senha;
        this.isAdmin = isAdmin;
        this.biblioteca = new ArrayList<>();
    }

    public User(String nome, double saldoInicial, String senha) {
        this(nome, saldoInicial, senha, false);
    }

    private String gerarIdAleatorio() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public double getSaldo() { return saldo; }
    public String getSenha() { return senha; }
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
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
