package banco.model.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import banco.model.dao.ContaDao;
import banco.model.dao.DaoFactory;
import banco.model.dao.TransacaoDao;
import banco.model.entities.Cliente;
import banco.model.entities.Conta;
import banco.model.entities.Transacao;
import banco.model.entities.enums.TipoDeTransacao;
import db.DB;
import db.DbException;

public class TransacaoDaoJDBC implements TransacaoDao{
	
	private Connection conn;
	
	public TransacaoDaoJDBC(Connection conn) {
		this.conn= conn;
	}
	
	private Transacao instanciarTransacoes(ResultSet rs, Conta conta, Cliente cliente) throws SQLException {
		Transacao transacao = new Transacao();
		transacao.setId(rs.getInt("transacao_id"));
		transacao.setTipo(TipoDeTransacao.valueOf(rs.getString("tipo")));
		transacao.setData(rs.getTimestamp("data"));
		transacao.setContaOrigem(conta);
		transacao.setValor(rs.getDouble("valor"));
		return transacao;
	}
	private Conta instanciarConta(ResultSet rs, Cliente cliente) throws SQLException {
		Conta conta = new Conta();
		conta.setId(rs.getInt("contaId"));
		conta.setCliente(cliente);
		conta.setSaldo(rs.getDouble("saldoConta"));
		return conta;
		
	}
	
	private Cliente instanciarCliente(ResultSet rs) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.setId(rs.getInt("titularId"));
		cliente.setNome(rs.getString("Titular"));
		cliente.setEmail(rs.getString("Email"));
		cliente.setDataDeNascimento(rs.getDate("DataNascimento"));
		return cliente;
		
	}

	@Override
	public List<Transacao> listarTransacoesPorConta(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT "
				    + "t.id AS transacao_id, "
				    + "t.tipo, "
				    + "t.valor, "
				    + "t.data, "
				    + "t.contaId, "
				    + "co.saldo AS saldoConta, "
				    + "co.titular_id AS titularId, "
				    + "co_cl.nome AS titular, "
				    + "co_cl.email AS Email, "
				    + "co_cl.data_nascimento AS DataNascimento "
				    + "FROM transacoes t "
				    + "JOIN contas co ON t.contaId = co.id "
				    + "JOIN clientes co_cl ON co.titular_id = co_cl.id WHERE contaId = ?");
			
				st.setInt(1, id);
			
			rs = st.executeQuery();
			List<Transacao> transacoes = new ArrayList<>();
			while(rs.next()) {
				Cliente cliente = instanciarCliente(rs);
				Conta conta = instanciarConta(rs, cliente);
				Transacao transacao = instanciarTransacoes(rs,conta,cliente);
				transacoes.add(transacao);
				}
			return transacoes;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	
	@Override
	public List<Transacao> listarTodasTransacoes() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT "
				    + "t.id AS transacao_id, "
				    + "t.tipo, "
				    + "t.valor, "
				    + "t.data, "
				    + "t.contaId, "
				    + "co.saldo AS saldoConta, "
				    + "co.titular_id AS titularId, "
				    + "co_cl.nome AS titular, "
				    + "co_cl.email AS Email, "
				    + "co_cl.data_nascimento AS DataNascimento "
				    + "FROM transacoes t "
				    + "JOIN contas co ON t.contaId = co.id "
				    + "JOIN clientes co_cl ON co.titular_id = co_cl.id");
			
			rs = st.executeQuery();
			List<Transacao> transacoes = new ArrayList<>();
			while(rs.next()) {
				Cliente cliente = instanciarCliente(rs);
				Conta conta = instanciarConta(rs, cliente);
				Transacao transacao = instanciarTransacoes(rs,conta,cliente);
				transacoes.add(transacao);
				}
			return transacoes;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	
	@Override
	public void registrarNovaTransacao(Transacao transacao) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO transacoes (tipo, valor, data, contaId) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, transacao.getTipo().name());
			st.setDouble(2, transacao.getValor());
			st.setTimestamp(3, transacao.getData());
			st.setInt(4, transacao.getContaOrigem().getId());
			
			int linhasAfetadas = st.executeUpdate();
			if(linhasAfetadas> 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next());{
					int id = rs.getInt(1);
					transacao.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Error: Nenhuma linha foi afetada");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

}
