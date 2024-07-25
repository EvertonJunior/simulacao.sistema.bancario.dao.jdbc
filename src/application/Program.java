package application;

import java.sql.Date;
import java.util.List;

import banco.model.dao.ClienteDao;
import banco.model.dao.ContaDao;
import banco.model.dao.DaoFactory;
import banco.model.dao.TransacaoDao;
import banco.model.entities.Cliente;
import banco.model.entities.Conta;
import banco.model.entities.Transacao;

public class Program {

	public static void main(String[] args) {
		
		ContaDao contaDao = DaoFactory.createContaDao();
		ClienteDao clienteDao = DaoFactory.createClienteDao();
		TransacaoDao transacaoDao= DaoFactory.createTransacaoDao();
		
		
		System.out.println("Teste 1 - inserir novo cliente ");
		Cliente cliente = new Cliente(null,"Jose", "jose@gmail.com", Date.valueOf("1997-05-31"));
		clienteDao.inserirNovoCliente(cliente);
		System.out.println("novo Cliente inserido! ");
		
		System.out.println("\nTeste 2 - BuscarClientePorEmail");
		Cliente buscarClientePorEmail = clienteDao.buscarClientePorEmail("jose@gmail.com");
		System.out.println(buscarClientePorEmail);
		

		System.out.println("\nteste 3 - deletarCliente");
		clienteDao.deletarPorID(2);
		System.out.println("Delete completado");
		
		
		System.out.println("\nteste 4 - AtualizarDados");
		cliente.setNome("Jose Junior");
		cliente.setEmail("josejunior@gmail.com");
		clienteDao.atualizarDados(cliente);
		
		
		System.out.println("\nteste 5 - Listar todos clientes");
		List<Cliente> listaClientes = clienteDao.listarTodosOsClientes();
		for(Cliente c : listaClientes) {
			System.out.println(c);
		}
		
		
		System.out.println("\nTeste contas - 1 - abrir conta");
		
		Conta conta = new Conta(null,null, cliente);
		contaDao.abrirConta(conta);
		
		System.out.println("\nTeste contas - 2 - buscar por id");
		Conta info = contaDao.buscarPorId(2);
		System.out.println(info);
		
		
		System.out.println("\nTeste contas - 3 - Listar todas as contas");
		List<Conta> contas = contaDao.ListarContas();
		for(Conta c : contas) {
			System.out.println(c);
		}
		
		
		System.out.println("\nTeste contas - 4 -  fechar conta");
		contaDao.fecharConta(7);
		System.out.println("Conta fechada com sucesso");
		
		System.out.println("\nTeste contas - 5 - Listar contas por cliente");
		List<Conta>contas2 = contaDao.ListarContasPorCliente(cliente);
		for (Conta c : contas2) {
			System.out.println(c);
		}
		
		
		System.out.println("\n TESTES METODOS SACAR, DEPOSITAR, TRANSFERIR");

		System.out.println("\nteste 1 - depositar");
		clienteDao.depositar(1, 1000.00);
		Conta infoDeposit = contaDao.buscarPorId(1);
		System.out.println("deposito feito");
		System.out.println(infoDeposit);
		
		System.out.println("\nteste 2 - sacar");
		clienteDao.sacar(1, 50000.00);
		Conta infoSaque = contaDao.buscarPorId(1);
		System.out.println(infoSaque);
		
		
		System.out.println("\nteste 3 - transferir");
		clienteDao.transferir(1, 2, 600.00);
		
		System.out.println("\nteste 4 - listarTransferenciasPorConta");
		List<Transacao> transacoesPorConta = transacaoDao.listarTransacoesPorConta(2);
		
		for (Transacao c : transacoesPorConta) {
			System.out.println(c);
		}
		
		System.out.println("\nteste 4 - listarTodasTransacoes");
		List<Transacao> transacoes = transacaoDao.listarTodasTransacoes();
		for (Transacao c : transacoes) {
			System.out.println(c);
		}
	}

}
