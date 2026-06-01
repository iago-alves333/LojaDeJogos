package br.ufpb.dcx.iago.atividades.exercicio6;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SistemaFuncionariosBomPratoTest   {
    @Test
    public void testaCadastroEPesquisa(){
        SistemaFuncionariosBomPrato sistema = new SistemaFuncionariosBomPrato();
        try{
            sistema.cadastrarFuncionario(new Funcionario("333.333.333-33", "AYla Rebouças", TipoFuncionario.GERENTE,3000));

            assertTrue(sistema.funcionarioExistente("333.333.333-33"));
            Funcionario f1 = sistema.pesquisarFuncionario("333.333.333-33");


            sistema.cadastrarFuncionario(new Funcionario("222.222.222-22", "João Paulo Silva", TipoFuncionario.COZINHEIRO,5000));
            Funcionario f2 = sistema.pesquisarFuncionario("222.222.222-22");
            List<Funcionario> ListaDeFuncionarios = sistema.pesquisarFuncionariosComSalarioMaiorQue(2000);
            assertEquals(2,ListaDeFuncionarios.size());
            int qntCozinheiro = sistema.contarFuncionariosDoTipo(TipoFuncionario.COZINHEIRO);
            assertEquals(1, qntCozinheiro);
        }catch(FuncionarioJaExisteException | FuncionarioInexistenteException e){
            fail(e.getMessage());
        }
    }

}
