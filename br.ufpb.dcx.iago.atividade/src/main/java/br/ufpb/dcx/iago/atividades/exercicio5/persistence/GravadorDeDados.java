package br.ufpb.dcx.iago.atividades.exercicio5.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GravadorDeDados {
    private String nomeArquivo;

    public GravadorDeDados(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public void gravar(Object objeto) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(objeto);
        }
    }

    public Object recuperar() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            return ois.readObject();
        }
    }
}
