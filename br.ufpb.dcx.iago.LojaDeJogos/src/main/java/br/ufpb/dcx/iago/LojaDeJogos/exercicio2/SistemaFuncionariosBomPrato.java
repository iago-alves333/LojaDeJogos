package br.ufpb.dcx.iago.LojaDeJogos.exercicio2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaFuncionariosBomPrato implements SistemaFuncionarios {
    private Map<String, Funcionario> funcionarios;

    public SistemaFuncionariosBomPrato() {
        this.funcionarios = new HashMap<String, Funcionario>();
    }

    @Override
    public void cadastrarFuncionario(Funcionario funcionario) throws FuncionarioJaExisteException {
        if (this.funcionarios.containsKey(funcionario.getCpf())) {
            throw new FuncionarioJaExisteException("Já existe funcionário com o cpf " + funcionario.getCpf());
        }
        this.funcionarios.put(funcionario.getCpf(), funcionario);
    }

    public void cadastrarFuncionario(String cpf, String nome, TipoFuncionario tipo, double salario) throws FuncionarioJaExisteException {
        this.cadastrarFuncionario(new Funcionario(cpf, nome, tipo, salario));
    }

    @Override
    public void alterarSalarioDeFuncionario(String cpfFuncionario, double novoSalario) throws FuncionarioInexistenteException {
        if (!this.funcionarios.containsKey(cpfFuncionario)) {
            throw new FuncionarioInexistenteException("Funcionario não existe");
        }
        Funcionario funcionario = this.funcionarios.get(cpfFuncionario);
        funcionario.setSalario(novoSalario);
    }

    @Override
    public int contarFuncionariosDoTipo(TipoFuncionario tipo) {
        int cont = 0;
        for (Funcionario funcionario : this.funcionarios.values()) {
            if (funcionario.getTipo().equals(tipo)) {
                cont += 1;
            }
        }
        return cont;
    }

    @Override
    public boolean funcionarioJaExiste(String cpfFuncionario) {
        return this.funcionarios.containsKey(cpfFuncionario);
    }

    // Mantido para não quebrar o seu teste atual caso ele use esse nome específico
    public boolean funcionarioExistente(String cpfFuncionario) {
        return this.funcionarioJaExiste(cpfFuncionario);
    }

    @Override
    public List<Funcionario> pesquisarFuncionariosPorTipo(TipoFuncionario tipo) {
        List<Funcionario> funcionariosPesquisados = new ArrayList<Funcionario>();
        for (Funcionario funcionario : this.funcionarios.values()) {
            if (funcionario.getTipo().equals(tipo)) {
                funcionariosPesquisados.add(funcionario);
            }
        }
        return funcionariosPesquisados;
    }

    @Override
    public Funcionario pesquisarFuncionario(String cpfFuncionario) throws FuncionarioInexistenteException {
        if (!this.funcionarios.containsKey(cpfFuncionario)) {
            throw new FuncionarioInexistenteException("Funcionario não existe");
        }
        return this.funcionarios.get(cpfFuncionario);
    }

    @Override
    public List<Funcionario> pesquisarFuncionariosComSalarioMaiorQue(double valor) {
        List<Funcionario> listaPesquisada = new ArrayList<>();
        for (Funcionario funcionario : this.funcionarios.values()) {
            if (funcionario.getSalario() > valor) {
                listaPesquisada.add(funcionario);
            }
        }
        return listaPesquisada;
    }
}