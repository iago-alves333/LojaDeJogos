package br.ufpb.dcx.iago.atividades.exercicio5;

import br.ufpb.dcx.iago.atividades.exercicio5.service.GerenteDeJogo;
import br.ufpb.dcx.iago.atividades.exercicio5.persistence.GravadorDeDados;
import br.ufpb.dcx.iago.atividades.exercicio5.model.TipoDeJogo;
import br.ufpb.dcx.iago.atividades.exercicio5.model.Jogo;
import br.ufpb.dcx.iago.atividades.exercicio5.model.User;
import br.ufpb.dcx.iago.atividades.exercicio5.model.Compra;
import br.ufpb.dcx.iago.atividades.exercicio5.exception.DadosInvalidosException;
import br.ufpb.dcx.iago.atividades.exercicio5.exception.UsuarioNaoSelecionadoException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private static final String ARQUIVO_DADOS = "dados_loja.dat";
    private GerenteDeJogo loja = new GerenteDeJogo();
    private GravadorDeDados gravador = new GravadorDeDados(ARQUIVO_DADOS);
    private User usuarioLogado = null;

    private BorderPane root;

    public static void main(String[] args) {
        launch(args); // Inicia a aplicação JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        verificarCarregamentoInicial();
        inicializarAdmin();

        root = new BorderPane();
        root.setCenter(telaLogin());

        Scene scene = new Scene(root, 1000, 700);
        try {
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Aviso: style.css não encontrado.");
        }

        primaryStage.setTitle("Sistema de Loja de Jogos");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja fechar o programa?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmação de Saída");
            alert.setHeaderText(null);
            alert.showAndWait().ifPresent(resposta -> {
                if (resposta != ButtonType.YES) {
                    event.consume();
                }
            });
        });

        primaryStage.show();
    }

    private void inicializarAdmin() {
        boolean hasAdmin = false;
        List<Object> users = loja.getGerenciadorDeUser().listarTodos();
        for (Object obj : users) {
            User u = (User) obj;
            if (u.isAdmin()) {
                hasAdmin = true;
                break;
            }
        }
        if (!hasAdmin) {
            try {
                User admin = new User("admin", 0.0, "admin", true);
                loja.getGerenciadorDeUser().adicionar(admin);
            } catch (Exception ignored) { }
        }
    }


    // --- MENU LATERAL ---
    private VBox criarMenuLateral() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.getStyleClass().add("sidebar");
        menu.setPrefWidth(220);

        Label lblMenu = new Label("MENU");
        lblMenu.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");

        menu.getChildren().add(lblMenu);

        if (usuarioLogado != null && usuarioLogado.isAdmin()) {
            Button btnCadJogo = criarBotao("Cadastrar Jogo");
            btnCadJogo.setOnAction(e -> root.setCenter(telaCadastrarJogo()));

            Button btnCadUser = criarBotao("Cadastrar Usuário");
            btnCadUser.setOnAction(e -> root.setCenter(telaCadastrarUsuario()));

            Button btnHistorico = criarBotao("Histórico de Vendas");
            btnHistorico.setOnAction(e -> root.setCenter(telaListarHistorico()));

            Button btnSalvar = criarBotao("Salvar Dados");
            btnSalvar.setOnAction(e -> salvarDados());

            Button btnCarregar = criarBotao("Carregar Dados");
            btnCarregar.setOnAction(e -> carregarDados());

            menu.getChildren().addAll(btnCadJogo, btnCadUser, btnHistorico, new Separator(), btnSalvar, btnCarregar);
        } else {
            Button btnVitrine = criarBotao("Vitrine da Loja");
            btnVitrine.setOnAction(e -> root.setCenter(telaVitrine()));

            Button btnAddSaldo = criarBotao("Adicionar Saldo");
            btnAddSaldo.setOnAction(e -> root.setCenter(telaAdicionarSaldo()));

            menu.getChildren().addAll(btnVitrine, btnAddSaldo);
        }

        menu.getChildren().add(new Separator());
        Button btnSair = criarBotao("Sair");
        btnSair.setOnAction(e -> {
            usuarioLogado = null;
            root.setLeft(null);
            root.setCenter(telaLogin());
        });
        menu.getChildren().add(btnSair);
        return menu;
    }

    private Button criarBotao(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    // --- TELAS (CENTRO DO BORDERPANE) ---

    private VBox telaLogin() {
        VBox painel = new VBox(15);
        painel.setAlignment(Pos.CENTER);
        painel.setPadding(new Insets(40));

        Label titulo = new Label("LOGIN");
        titulo.setFont(new Font("Arial", 24));

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome de Usuário");
        txtNome.setMaxWidth(300);

        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Senha");
        txtSenha.setMaxWidth(300);

        Button btnLogin = new Button("Entrar");
        btnLogin.getStyleClass().add("button-primary");
        btnLogin.setMaxWidth(300);
        btnLogin.setOnAction(e -> {
            String nomeDigitado = txtNome.getText();
            String senhaDigitada = txtSenha.getText();

            User userValido = null;
            for (Object obj : loja.getGerenciadorDeUser().listarTodos()) {
                User u = (User) obj;
                if (u.getNome().equals(nomeDigitado) && u.getSenha() != null && u.getSenha().equals(senhaDigitada)) {
                    userValido = u;
                    break;
                }
            }

            if (userValido != null) {
                usuarioLogado = userValido;
                root.setLeft(criarMenuLateral());
                root.setCenter(telaBoasVindas());
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Login Falhou", "Usuário ou senha incorretos.");
            }
        });

        Button btnCadastrar = new Button("Criar Nova Conta");
        btnCadastrar.setMaxWidth(300);
        btnCadastrar.setOnAction(e -> root.setCenter(telaCadastrarUsuario()));

        painel.getChildren().addAll(titulo, txtNome, txtSenha, btnLogin, new Label("ou"), btnCadastrar);
        return painel;
    }

    private VBox telaBoasVindas() {
        VBox painel = new VBox();
        painel.setAlignment(Pos.CENTER);
        Label lbl = new Label("Bem-vindo à Loja de Jogos!\nSelecione uma opção no menu lateral.");
        lbl.setFont(new Font("Arial", 20));
        painel.getChildren().add(lbl);
        return painel;
    }

    private VBox telaCadastrarJogo() {
        VBox painel = new VBox(15);
        painel.setPadding(new Insets(30));

        Label titulo = new Label("CADASTRAR NOVO JOGO");
        titulo.setFont(new Font("Arial", 22));

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do Jogo");

        TextField txtPreco = new TextField();
        txtPreco.setPromptText("Preço (ex: 49.90)");

        ComboBox<TipoDeJogo> comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll(TipoDeJogo.values());
        comboTipo.setPromptText("Selecione o Tipo");

        // Seleção de Imagem
        final String[] imagemUrl = {null};
        Button btnImagem = new Button("Selecionar Capa do Jogo");
        Label lblImagem = new Label("Nenhuma imagem selecionada");
        ImageView previewImagem = new ImageView();
        previewImagem.setFitWidth(150);
        previewImagem.setFitHeight(200);
        previewImagem.setPreserveRatio(true);

        btnImagem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecione a Imagem do Jogo");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(painel.getScene().getWindow());
            if (file != null) {
                imagemUrl[0] = file.toURI().toString();
                lblImagem.setText(file.getName());
                previewImagem.setImage(new Image(imagemUrl[0]));
            }
        });

        HBox imageBox = new HBox(10, btnImagem, lblImagem);
        imageBox.setAlignment(Pos.CENTER_LEFT);

        Button btnSalvar = new Button("Cadastrar Jogo");
        btnSalvar.getStyleClass().add("button-primary");
        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText();
                double preco = Double.parseDouble(txtPreco.getText());
                TipoDeJogo tipo = comboTipo.getValue();

                if (nome.isEmpty() || tipo == null) throw new DadosInvalidosException("Preencha todos os campos essenciais.");

                Jogo novoJogo = new Jogo(nome, preco, tipo, imagemUrl[0]);
                loja.getGerenciadorDeJogo().adicionar(novoJogo);

                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogo cadastrado! ID gerado: " + novoJogo.getId());
                txtNome.clear(); txtPreco.clear(); comboTipo.getSelectionModel().clearSelection();
                imagemUrl[0] = null; lblImagem.setText("Nenhuma imagem selecionada"); previewImagem.setImage(null);
            } catch (NumberFormatException ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Digite um valor numérico válido para o preço.");
            } catch (Exception ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            }
        });

        painel.getChildren().addAll(
            titulo, 
            new Label("Nome:"), txtNome, 
            new Label("Preço (R$):"), txtPreco, 
            new Label("Categoria:"), comboTipo, 
            new Label("Capa do Jogo:"), imageBox, previewImagem,
            btnSalvar
        );
        return painel;
    }

    private VBox telaCadastrarUsuario() {
        VBox painel = new VBox(15);
        painel.setPadding(new Insets(30));

        Label titulo = new Label("CADASTRAR NOVO USUÁRIO");
        titulo.setFont(new Font("Arial", 22));

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome");

        TextField txtSaldo = new TextField();
        txtSaldo.setPromptText("Saldo Inicial");

        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Senha");

        Button btnSalvar = new Button("Cadastrar Usuário");
        btnSalvar.getStyleClass().add("button-primary");
        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText();
                double saldo = Double.parseDouble(txtSaldo.getText());
                String senha = txtSenha.getText();

                if (nome.isEmpty() || senha.isEmpty()) throw new DadosInvalidosException("Preencha todos os campos.");

                User novoUser = new User(nome, saldo, senha);
                loja.getGerenciadorDeUser().adicionar(novoUser);

                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Usuário cadastrado com sucesso! ID gerado: " + novoUser.getId());
                txtNome.clear(); txtSaldo.clear(); txtSenha.clear();
                if (usuarioLogado == null) {
                    root.setCenter(telaLogin());
                }
            } catch (NumberFormatException ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Digite um valor numérico válido para o saldo.");
            } catch (Exception ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            }
        });

        painel.getChildren().addAll(titulo, new Label("Nome:"), txtNome, new Label("Saldo Inicial (R$):"), txtSaldo, new Label("Senha:"), txtSenha, btnSalvar);
        
        if (usuarioLogado == null) {
            Button btnVoltar = new Button("Voltar para Login");
            btnVoltar.setOnAction(e -> root.setCenter(telaLogin()));
            painel.getChildren().add(btnVoltar);
        }
        
        return painel;
    }

    private VBox telaAdicionarSaldo() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(20));

        Label titulo = new Label("ADICIONAR SALDO");
        titulo.setFont(new Font("Arial", 18));

        Label infoConta = new Label(String.format("Conta: %s | Saldo Atual: R$ %.2f", usuarioLogado.getNome(), usuarioLogado.getSaldo()));
        infoConta.setStyle("-fx-font-size: 16px; -fx-text-fill: #4CAF50;");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor a adicionar");

        Button btnAdicionar = new Button("Confirmar Depósito");
        btnAdicionar.getStyleClass().add("button-primary");
        btnAdicionar.setOnAction(e -> {
            try {
                double valor = Double.parseDouble(txtValor.getText());
                
                usuarioLogado.adicionarSaldo(valor);

                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Novo saldo de " + usuarioLogado.getNome() + ": R$" + usuarioLogado.getSaldo());
                root.setCenter(telaAdicionarSaldo()); // Atualiza a tela
            } catch (NumberFormatException ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Digite um valor numérico válido.");
            } catch (Exception ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", ex.getMessage());
            }
        });

        painel.getChildren().addAll(titulo, infoConta, txtValor, btnAdicionar);
        return painel;
    }

    private VBox telaVitrine() {
        VBox painel = new VBox(15);
        painel.setPadding(new Insets(20));

        Label titulo = new Label("VITRINE DA LOJA");
        titulo.setFont(new Font("Arial", 22));

        // Info do saldo no topo
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Label infoSaldo = new Label(String.format("Seu Saldo: R$ %.2f", usuarioLogado.getSaldo()));
        infoSaldo.setStyle("-fx-font-size: 16px; -fx-text-fill: #4CAF50;");
        
        // Campo de Pesquisa
        TextField txtBusca = new TextField();
        txtBusca.setPromptText("Pesquisar jogo por nome...");
        txtBusca.getStyleClass().add("search-bar");

        topBar.getChildren().addAll(infoSaldo, txtBusca);

        // Grid de Jogos
        FlowPane gridJogos = new FlowPane();
        gridJogos.setHgap(15);
        gridJogos.setVgap(15);
        gridJogos.setPadding(new Insets(10));
        
        ScrollPane scroll = new ScrollPane(gridJogos);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        // Função para atualizar a vitrine com base na busca
        Runnable atualizarVitrine = () -> {
            gridJogos.getChildren().clear();
            String termo = txtBusca.getText().toLowerCase();
            
            List<Object> jogos = loja.getGerenciadorDeJogo().listarTodos();
            for (Object obj : jogos) {
                Jogo j = (Jogo) obj;
                if (j.getNome().toLowerCase().contains(termo)) {
                    VBox card = criarCardJogo(j);
                    gridJogos.getChildren().add(card);
                }
            }
            if (gridJogos.getChildren().isEmpty()) {
                gridJogos.getChildren().add(new Label("Nenhum jogo encontrado."));
            }
        };

        // Escuta digitação na busca
        txtBusca.textProperty().addListener((obs, oldV, newV) -> atualizarVitrine.run());
        
        // Inicializa a vitrine
        atualizarVitrine.run();

        painel.getChildren().addAll(titulo, topBar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return painel;
    }

    private VBox criarCardJogo(Jogo j) {
        VBox card = new VBox(10);
        card.getStyleClass().add("game-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);

        ImageView imgView = new ImageView();
        imgView.setFitWidth(150);
        imgView.setFitHeight(200);
        imgView.setPreserveRatio(true);
        if (j.getUrlImagem() != null && !j.getUrlImagem().isEmpty()) {
            try {
                imgView.setImage(new Image(j.getUrlImagem()));
            } catch (Exception e) {
                // Ignore se a imagem não carregar
            }
        }

        Label lblNome = new Label(j.getNome());
        lblNome.getStyleClass().add("game-title");
        lblNome.setWrapText(true);
        lblNome.setAlignment(Pos.CENTER);

        Label lblTipo = new Label(j.getTipo().toString());
        lblTipo.getStyleClass().add("game-type");

        Label lblPreco = new Label(String.format("R$ %.2f", j.getPreco()));
        lblPreco.getStyleClass().add("game-price");

        Button btnComprar = new Button("Comprar");
        btnComprar.getStyleClass().add("button-primary");
        btnComprar.setOnAction(e -> {
            try {
                Compra c = loja.realizarCompra(usuarioLogado.getId(), j.getId());
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Compra efetuada com sucesso!\n" + c.toString());
                
                // Atualizar tela para refletir saldo novo
                root.setCenter(telaVitrine());
            } catch (Exception ex) {
                exibirAlerta(Alert.AlertType.ERROR, "Falha na Compra", ex.getMessage());
            }
        });

        card.getChildren().addAll(imgView, lblNome, lblTipo, lblPreco, btnComprar);
        return card;
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
