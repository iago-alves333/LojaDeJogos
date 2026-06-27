package br.ufpb.dcx.iago.atividades.exercicio5.service;

import br.ufpb.dcx.iago.atividades.exercicio5.model.User;
import br.ufpb.dcx.iago.atividades.exercicio5.model.Jogo;
import br.ufpb.dcx.iago.atividades.exercicio5.model.TipoDeJogo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class GerenteDeJogoTest {

    private GerenteDeJogo gerente;

    @BeforeEach
    public void setUp() {
        gerente = new GerenteDeJogo();
    }

    @Test
    public void testPesquisarUser() throws Exception {
        User user = new User("Iago", 100.0, "senha123");
        gerente.getGerenciadorDeUser().adicionar(user);

        assertTrue(gerente.getGerenciadorDeUser().existe(user.getId()));
        User pesquisado = (User) gerente.getGerenciadorDeUser().buscar(user.getId());
        assertNotNull(pesquisado);
        assertEquals("Iago", pesquisado.getNome());
    }

    @Test
    public void testPesquisarJogo() throws Exception {
        Jogo jogo = new Jogo("Super Mario", 50.0, TipoDeJogo.AVENTURA);
        gerente.getGerenciadorDeJogo().adicionar(jogo);

        assertTrue(gerente.getGerenciadorDeJogo().existe(jogo.getId()));
        Jogo pesquisado = (Jogo) gerente.getGerenciadorDeJogo().buscar(jogo.getId());
        assertNotNull(pesquisado);
        assertEquals("Super Mario", pesquisado.getNome());
    }

    @Test
    public void testRemoverUser() throws Exception {
        User user = new User("João", 100.0, "senha123");
        gerente.getGerenciadorDeUser().adicionar(user);
        
        gerente.getGerenciadorDeUser().remover(user.getId());
        assertFalse(gerente.getGerenciadorDeUser().existe(user.getId()));
    }

    @Test
    public void testRemoverJogo() throws Exception {
        Jogo jogo = new Jogo("Zelda", 150.0, TipoDeJogo.AVENTURA);
        gerente.getGerenciadorDeJogo().adicionar(jogo);
        
        gerente.getGerenciadorDeJogo().remover(jogo.getId());
        assertFalse(gerente.getGerenciadorDeJogo().existe(jogo.getId()));
    }

    @Test
    public void testRealizarCompra() throws Exception {
        User user = new User("Maria", 200.0, "senha123");
        gerente.getGerenciadorDeUser().adicionar(user);

        Jogo jogo = new Jogo("Zelda", 150.0, TipoDeJogo.AVENTURA);
        gerente.getGerenciadorDeJogo().adicionar(jogo);

        gerente.realizarCompra(user.getId(), jogo.getId());

        User u = (User) gerente.getGerenciadorDeUser().buscar(user.getId());
        assertEquals(50.0, u.getSaldo());
        assertTrue(u.getBiblioteca().contains(jogo));
        assertEquals(1, gerente.getHistoricoDeCompras().size());
    }

    @Test
    public void testPersistencia() throws Exception {
        User user = new User("Pedro", 50.0, "senha123");
        gerente.getGerenciadorDeUser().adicionar(user);
        
        gerente.salvarDados();

        GerenteDeJogo gerenteRecuperado = new GerenteDeJogo();
        gerenteRecuperado.recuperarDados();

        assertTrue(gerenteRecuperado.getGerenciadorDeUser().existe(user.getId()));
        
        // Clean up file after test
        File f = new File("sistema.dat");
        if(f.exists()){
            f.delete();
        }
    }

    @Test
    public void testPesquisarJogosPorNome() throws Exception {
        Jogo jogo1 = new Jogo("Super Mario Bros", 50.0, TipoDeJogo.AVENTURA);
        Jogo jogo2 = new Jogo("Mario Kart", 60.0, TipoDeJogo.ESPORTE);
        Jogo jogo3 = new Jogo("Zelda Ocarina", 150.0, TipoDeJogo.AVENTURA);

        gerente.getGerenciadorDeJogo().adicionar(jogo1);
        gerente.getGerenciadorDeJogo().adicionar(jogo2);
        gerente.getGerenciadorDeJogo().adicionar(jogo3);

        java.util.List<Jogo> resultadosMario = gerente.pesquisarJogosPorNome("mario");
        assertEquals(2, resultadosMario.size());
        assertTrue(resultadosMario.contains(jogo1));
        assertTrue(resultadosMario.contains(jogo2));

        java.util.List<Jogo> resultadosZelda = gerente.pesquisarJogosPorNome("ZELDA");
        assertEquals(1, resultadosZelda.size());
        assertTrue(resultadosZelda.contains(jogo3));

        java.util.List<Jogo> resultadosVazio = gerente.pesquisarJogosPorNome("Pokemon");
        assertEquals(0, resultadosVazio.size());
    }
}
