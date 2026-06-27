# Mini Sistema - Loja de Jogos (Exercício 5)

Este repositório contém a implementação do Mini Sistema com Persistência da disciplina de POO (Exercício 5).
O sistema simula uma **Loja de Jogos Digital**, onde:

- Usuários podem ser cadastrados e gerenciados.
- Jogos podem ser cadastrados na loja.
- Usuários podem realizar compras de jogos (verificando o saldo e as regras de negócio).
- O sistema grava os dados de compras e cadastros em arquivos `.dat` utilizando as classes `ObjectInputStream` e `ObjectOutputStream`, com a classe de persistência `GravadorDeDados`.

## O que foi implementado de acordo com a Atividade:

1. **Descrição do Mini Sistema:**
   Este arquivo contém a descrição principal do escopo e funcionamento.

2. **Pasta `src` com as classes e testes:**
   - As classes de modelo (`User`, `Jogo`, `Compra`) e exceções estão contidas na pasta principal `src/main/java`.
   - Há a classe `GerenteDeJogoTest` na pasta `src/test/java`, que realiza os testes das principais funcionalidades (cadastro, pesquisa, remoção, compra de jogos e persistência).

3. **Gravação e Recuperação com `IOException`:**
   - A classe `GravadorDeDados` efetua a leitura e escrita utilizando `FileInputStream`, `FileOutputStream`, `ObjectInputStream` e `ObjectOutputStream`. Seus métodos lançam explicitamente a `IOException`.

4. **Interface e Javadoc:**
   - A interface `SistemaLojaDeJogos` foi criada contendo documentação via Javadoc para todos os seus métodos, em especial `realizarCompra`, `salvarDados()` e `recuperarDados()`. A classe principal, `GerenteDeJogo`, implementa os métodos desta interface.
