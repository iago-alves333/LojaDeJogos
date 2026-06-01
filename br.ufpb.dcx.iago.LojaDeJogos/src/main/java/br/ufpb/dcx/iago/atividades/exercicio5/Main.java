package br.ufpb.dcx.iago.atividades.exercicio5;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;



public class Main extends Application {

    private static final String ARQUIVO_DADOS = "dados_loja.dat";
    private GerenteDeJogo loja = new GerenteDeJogo();
    private GravadorDeDados gravador = new GravadorDeDados(ARQUIVO_DADOS);

    private BorderPane root;

    public static void main(String[] args) {
        launch(args); // Inicia a aplicação JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        verificarCarregamentoInicial();

        root = new BorderPane();
        root.setLeft(criarMenuLateral());
        root.setCenter(telaBoasVindas());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Sistema de Loja de Jogos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- MENU LATERAL ---
    private VBox criarMenuLateral() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #2c3e50;");
        menu.setPrefWidth(200);

        Label lblMenu = new Label("MENU");
        lblMenu.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");

        Button btnCadJogo = criarBotao("Cadastrar Jogo");
        btnCadJogo.setOnAction(e -> root.setCenter(telaCadastrarJogo()));

        Button btnCadUser = criarBotao("Cadastrar Usuário");
        btnCadUser.setOnAction(e -> root.setCenter(telaCadastrarUsuario()));

        Button btnAddSaldo = criarBotao("Adicionar Saldo");
        btnAddSaldo.setOnAction(e -> root.setCenter(telaAdicionarSaldo()));

        Button btnComprar = criarBotao("Realizar Compra");
        btnComprar.setOnAction(e -> root.setCenter(telaRealizarCompra()));

        Button btnListarJogos = criarBotao("Catálogo de Jogos");
        btnListarJogos.setOnAction(e -> root.setCenter(telaListarJogos()));

        Button btnHistorico = criarBotao("Histórico de Vendas");
        btnHistorico.setOnAction(e -> root.setCenter(telaListarHistorico()));

        Button btnSalvar = criarBotao("Salvar Dados");
        btnSalvar.setOnAction(e -> salvarDados());

        Button btnCarregar = criarBotao("Carregar Dados");
        btnCarregar.setOnAction(e -> carregarDados());

        menu.getChildren().addAll(lblMenu, btnCadJogo, btnCadUser, btnAddSaldo, btnComprar,
                btnListarJogos, btnHistorico, new Separator(), btnSalvar, btnCarregar);
        return menu;
    }

    private Button criarBotao(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    // --- TELAS (CENTRO DO BORDERPANE) ---

    private VBox telaBoasVindas() {
        VBox painel = new VBox();
        painel.setAlignment(Pos.CENTER);
        Label lbl = new Label("Bem-vindo à Loja de Jogos!\nSelecione uma opção no menu lateral.");
        lbl.setFont(new Font("Arial", 20));
        painel.getChildren().add(lbl);
        return painel;
    }

    private VBox telaCadastrarJogo() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));

        Label titulo = new Label("CADASTRAR NOVO JOGO");
        titulo.setFont(new Font("Arial", 18));

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do Jogo");

        TextField txtPreco = new TextField();
        txtPreco.setPromptText("Preço (ex: 49.90)");

        ComboBox<TipoDeJogo> comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll(TipoDeJogo.values());
        comboTipo.setPromptText("Selecione o Tipo");

        Button btnSalvar = new Button("Cadastrar");
        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText();
                double preco = Double.parseDouble(txtPreco.getText());
                TipoDeJogo tipo = comboTipo.getValue();

                if (nome.isEmpty() || tipo == null) throw new Exception("Preencha todos os campos.");

                Jogo novoJogo = new Jogo(nome, preco, tipo);
                loja.getGerenciadorDeJogo().adicionar(novoJogo);

                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogo cadastrado! ID gerado: " + novoJogo.getId());
                txtNome.clear(); txtPreco.clear(); comboTipo.getSelectionModel().clearSelection();
            } catch (NumberFormatException ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Digite um valor numérico válido para o preço.");
            } catch (Exception ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            }
        });

        painel.getChildren().addAll(titulo, new Label("Nome:"), txtNome, new Label("Preço (R$):"), txtPreco, new Label("Categoria:"), comboTipo, btnSalvar);
        return painel;
    }

    private VBox telaCadastrarUsuario() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));

        Label titulo = new Label("CADASTRAR NOVO USUÁRIO");
        titulo.setFont(new Font("Arial", 18));

        TextField txtId = new TextField();
        txtId.setPromptText("ID do Usuário (ex: U01)");

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome");

        TextField txtSaldo = new TextField();
        txtSaldo.setPromptText("Saldo Inicial");

        Button btnSalvar = new Button("Cadastrar");
        btnSalvar.setOnAction(e -> {
            try {
                String id = txtId.getText();
                String nome = txtNome.getText();
                double saldo = Double.parseDouble(txtSaldo.getText());

                if (id.isEmpty() || nome.isEmpty()) throw new Exception("Preencha todos os campos.");

                User novoUser = new User(id, nome, saldo);
                loja.getGerenciadorDeUser().adicionar(novoUser);

                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Usuário cadastrado com sucesso!");
                txtId.clear(); txtNome.clear(); txtSaldo.clear();
            } catch (Exception ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            }
        });

        painel.getChildren().addAll(titulo, new Label("ID:"), txtId, new Label("Nome:"), txtNome, new Label("Saldo Inicial (R$):"), txtSaldo, btnSalvar);
        return painel;
    }

    private VBox telaAdicionarSaldo() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));

        Label titulo = new Label("ADICIONAR SALDO");
        titulo.setFont(new Font("Arial", 18));

        ComboBox<String> comboUser = new ComboBox<>();
        List<Object> usuarios = loja.getGerenciadorDeUser().listarTodos();
        for (int i = 0; i < usuarios.size(); i++) {
            User u = (User) usuarios.get(i);
            comboUser.getItems().add(String.format("<%02d: %s | Saldo: R$%.2f>", (i + 1), u.getNome(), u.getSaldo()));
        }
        comboUser.setPromptText("Selecione o Usuário");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor a adicionar");

        Button btnAdicionar = new Button("Adicionar Saldo");
        btnAdicionar.setOnAction(e -> {
            try {
                int index = comboUser.getSelectionModel().getSelectedIndex();
                if (index < 0) throw new Exception("Selecione um usuário.");

                double valor = Double.parseDouble(txtValor.getText());
                User u = (User) usuarios.get(index);
                u.adicionarSaldo(valor);

                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Novo saldo de " + u.getNome() + ": R$" + u.getSaldo());
                root.setCenter(telaAdicionarSaldo()); // Atualiza a tela para mostrar o novo saldo na ComboBox
            } catch (Exception ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            }
        });

        painel.getChildren().addAll(titulo, comboUser, txtValor, btnAdicionar);
        return painel;
    }

    private VBox telaRealizarCompra() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));

        Label titulo = new Label("REALIZAR COMPRA");
        titulo.setFont(new Font("Arial", 18));

        ComboBox<String> comboUser = new ComboBox<>();
        List<Object> usuarios = loja.getGerenciadorDeUser().listarTodos();
        for (int i = 0; i < usuarios.size(); i++) {
            User u = (User) usuarios.get(i);
            comboUser.getItems().add(String.format("<%02d: %s | Saldo: R$%.2f>", (i + 1), u.getNome(), u.getSaldo()));
        }
        comboUser.setPromptText("Selecione o Comprador");

        ComboBox<String> comboJogo = new ComboBox<>();
        List<Object> jogos = loja.getGerenciadorDeJogo().listarTodos();
        for (int i = 0; i < jogos.size(); i++) {
            Jogo j = (Jogo) jogos.get(i);
            comboJogo.getItems().add(String.format("<%02d: %s | R$%.2f>", (i + 1), j.getNome(), j.getPreco()));
        }
        comboJogo.setPromptText("Selecione o Jogo");

        Button btnComprar = new Button("Confirmar Compra");
        btnComprar.setOnAction(e -> {
            try {
                int idxUser = comboUser.getSelectionModel().getSelectedIndex();
                int idxJogo = comboJogo.getSelectionModel().getSelectedIndex();

                if (idxUser < 0 || idxJogo < 0) throw new Exception("Selecione um usuário e um jogo.");

                User u = (User) usuarios.get(idxUser);
                Jogo j = (Jogo) jogos.get(idxJogo);

                Compra c = loja.realizarCompra(u.getId(), j.getId());
                exibirAlerta(Alert.AlertType.INFORMATION, "Compra Realizada!", c.toString());

                // Recarrega a tela para atualizar o saldo exibido
                root.setCenter(telaRealizarCompra());
            } catch (Exception ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Falha na Compra", ex.getMessage());
            }
        });

        painel.getChildren().addAll(titulo, comboUser, comboJogo, btnComprar);
        return painel;
    }

    private VBox telaListarJogos() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));

        Label titulo = new Label("CATÁLOGO DE JOGOS");
        titulo.setFont(new Font("Arial", 18));

        TextArea txtArea = new TextArea();
        txtArea.setEditable(false);
        txtArea.setPrefHeight(400);

        List<Object> jogos = loja.getGerenciadorDeJogo().listarTodos();
        if (jogos.isEmpty()) {
            txtArea.setText("Nenhum jogo cadastrado.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < jogos.size(); i++) {
                Jogo j = (Jogo) jogos.get(i);
                sb.append(String.format("<%02d: %s | Tipo: %s | Preço: R$%.2f>\n", (i + 1), j.getNome(), j.getTipo(), j.getPreco()));
            }
            txtArea.setText(sb.toString());
        }

        painel.getChildren().addAll(titulo, txtArea);
        return painel;
    }

    private VBox telaListarHistorico() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));

        Label titulo = new Label("HISTÓRICO DE COMPRAS");
        titulo.setFont(new Font("Arial", 18));

        TextArea txtArea = new TextArea();
        txtArea.setEditable(false);
        txtArea.setPrefHeight(400);

        List<Compra> compras = loja.getHistoricoDeCompras();
        if (compras.isEmpty()) {
            txtArea.setText("Nenhuma compra efetuada no sistema.");
        } else {
            StringBuilder sb = new StringBuilder();
            compras.forEach(c -> sb.append(c.toString()).append("\n"));
            txtArea.setText(sb.toString());
        }

        painel.getChildren().addAll(titulo, txtArea);
        return painel;
    }

    // --- MÉTODOS DE DADOS E ALERTAS ---

    private void salvarDados() {
        try {
            gravador.gravar(loja);
            exibirAlerta(Alert.AlertType.INFORMATION, "Salvar Dados", "Os dados foram salvos com sucesso.");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro ao Salvar", e.getMessage());
        }
    }

    private void carregarDados() {
        try {
            loja = (GerenteDeJogo) gravador.recuperar();
            exibirAlerta(Alert.AlertType.INFORMATION, "Carregar Dados", "Os dados foram carregados com sucesso.");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro ao Carregar", "Nenhum arquivo encontrado ou dados corrompidos.");
        }
    }

    private void verificarCarregamentoInicial() {
        File f = new File(ARQUIVO_DADOS);
        if (f.exists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Um arquivo de save foi encontrado. Deseja carregá-lo?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Carregar Backup");
            alert.showAndWait().ifPresent(resposta -> {
                if (resposta == ButtonType.YES) {
                    try {
                        loja = (GerenteDeJogo) gravador.recuperar();
                    } catch (Exception ignored) { }
                }
            });
        }
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}