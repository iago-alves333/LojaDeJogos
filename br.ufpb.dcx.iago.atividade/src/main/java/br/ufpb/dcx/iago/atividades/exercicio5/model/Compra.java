package br.ufpb.dcx.iago.atividades.exercicio5.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Compra implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private User user;
    private Jogo jogo;
    private LocalDateTime dataCompra;
    private double valorPago;

    public Compra(User user, Jogo jogo) {
        this.id = UUID.randomUUID().toString().substring(0, 8); // ID gerado automaticamente
        this.user = user;
        this.jogo = jogo;
        this.valorPago = jogo.getPreco();
        this.dataCompra = LocalDateTime.now();
    }

    public String getId() { return id; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("[ID da Compra: %s | %s] %s comprou %s por R$%.2f",
                id, dataCompra.format(formatter), user.getNome(), jogo.getNome(), valorPago);
    }
}
