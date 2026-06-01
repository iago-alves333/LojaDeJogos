package br.ufpb.dcx.iago.LojaDeJogos.exercicio1;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Jogo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private double preco;
    private TipoDeJogo tipo;

    public Jogo(String nome, double preco, TipoDeJogo tipo) {
        this.id = gerarIdAleatorio();
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
    }
    private String gerarIdAleatorio() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    public String getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public TipoDeJogo getTipo() { return tipo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jogo jogo = (Jogo) o;
        return Objects.equals(id, jogo.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}