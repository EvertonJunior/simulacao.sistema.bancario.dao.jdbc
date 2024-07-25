package banco.model.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import banco.model.dao.ClienteDao;
import banco.model.dao.ContaDao;
import banco.model.dao.DaoFactory;
import banco.model.dao.TransacaoDao;
import banco.model.entities.Cliente;
import banco.model.entities.Conta;
import banco.model.entities.Transacao;
import banco.model.entities.TransacaoException;
import banco.model.entities.enums.TipoDeTransacao;
import db.DB;
import db.DbException;


public class ClienteDaoJDBC implements ClienteDao{

	private Connection conn;
	
	public ClienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void inserirNovoCliente(Cliente cliente) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO clientes (nome, email, data_nascimento) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, cliente.getNome());
			st.setString(2, cliente.getEmail());
			st.setDate(3, cliente.getDataDeNascimento());

			
			
			int linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas < 0 ) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					cliente.setId(id);
				}
				DB.closeResultSet(rs);
			}
			
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}
	
	@Override
	public void atualizarDados(Cliente cliente) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE clientes "
					+ "SET nome = ?, email = ?, data_nascimento = ?"
					+ "WHERE Id = ?");
			st.setString(1, cliente.getNome());
			st.setString(2, cliente.getEmail());
			st.setDate(3, cliente.getDataDeNascimento());
			st.setInt(4,  cliente.getId());
			
			st.executeUpdate();
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deletarPorID(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM clientes WHERE id = ?");
			st.setInt(1, id);
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro: Nao e possivel deletar pois cliente tem conta aberta");
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Cliente buscarClientePorEmail(String email) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM clientes WHERE email = ?");
			st.setString(1, email);

			rs = st.executeQuery();
			
			if (rs.next()) {
				Cliente cliente = new Cliente(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getDate("data_nascimento"));
				return cliente;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Cliente> listarTodosOsClientes() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT * FROM clientes");

			rs = st.executeQuery();
			List<Cliente> clientes= new ArrayList<>();
			
			while(rs.next()) {
				Cliente cliente = new Cliente(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getDate("data_nascimento"));
				clientes.add(cliente);
			}
			return clientes;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void sacar(Integer idConta, Double valor) {
		ContaDao contaDao = DaoFactory.createContaDao();
		TransacaoDao transacaoDao = DaoFactory.createTransacaoDao();
		Conta info = contaDao.buscarPorId(idConta);
	    if (valor > info.getSaldo()) {
	        throw new TransacaoException("Saldo insuficiente para realizar o saque.");
	    }
		double novoSaldo = info.getSaldo() - valor;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE contas SET saldo = ? WHERE id = ?  ");
			st.setDouble(1, novoSaldo);
			st.setInt(2, idConta);
			
			st.executeUpdate();
			
			LocalDateTime dataHora = LocalDateTime.now();
			Transacao transacao = new Transacao(null,TipoDeTransacao.saque,Timestamp.valueOf(dataHora), info,valor);
			transacaoDao.registrarNovaTransacao(transacao);
		}
		catch(SQLException e ) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void depositar(Integer idConta, Double valor) {
		ContaDao contaDao = DaoFactory.createContaDao();
		TransacaoDao transacaoDao = DaoFactory.createTransacaoDao();
		Conta info = contaDao.buscarPorId(idConta);
		double novoSaldo = info.getSaldo() + valor;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE contas SET saldo = ? WHERE id = ?  ");
			st.setDouble(1, novoSaldo);
			st.setInt(2, idConta);
			
			st.executeUpdate();
			
			LocalDateTime dataHora = LocalDateTime.now();
			Transacao transacao = new Transacao(null,TipoDeTransacao.deposito,Timestamp.valueOf(dataHora), info,valor);
			transacaoDao.registrarNovaTransacao(transacao);
		}
		catch(SQLException e ) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
		

	@Override
	public void transferir(Integer idConta, Integer idContaDestino, Double valor) {
		ContaDao contaDao = DaoFactory.createContaDao();
		TransacaoDao transacaoDao = DaoFactory.createTransacaoDao();
		Conta contaOrigem = contaDao.buscarPorId(idConta);
		 if (valor > contaOrigem.getSaldo()) {
		        throw new TransacaoException("Saldo insuficiente para realizar o transferencia.");
		    }
		Conta contaDestino = contaDao.buscarPorId(idContaDestino);
		double contaOrigemNovoSaldo = contaOrigem.getSaldo()- valor;
		double contaDestinoNovoSaldo = contaDestino.getSaldo()+valor;
		PreparedStatement st = null;
		PreparedStatement st1 = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("UPDATE contas SET saldo = ? WHERE id = ?");
			st.setDouble(1, contaOrigemNovoSaldo);
			st.setInt(2, idConta);
			st.executeUpdate();
			
			
			st1 = conn.prepareStatement("UPDATE contas SET saldo = ? WHERE id = ?");
			st1.setDouble(1, contaDestinoNovoSaldo);
			st1.setInt(2, idContaDestino);
			st1.executeUpdate();
			
			
			LocalDateTime dataHora = LocalDateTime.now();
			Transacao transacao = new Transacao(null,TipoDeTransacao.transferenciaFeita,Timestamp.valueOf(dataHora), contaOrigem, valor);
			transacaoDao.registrarNovaTransacao(transacao);
			Transacao transacao2 = new Transacao(null,TipoDeTransacao.transferenciaRecebida,Timestamp.valueOf(dataHora), contaDestino, valor);
			transacaoDao.registrarNovaTransacao(transacao2);
			conn.commit();

		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
			} 
			catch (SQLException e1) {
				throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
			}
		}
		finally {
			DB.closeStatement(st);
		}
	}

}
