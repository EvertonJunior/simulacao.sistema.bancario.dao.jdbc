package banco.model.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import banco.model.dao.ContaDao;
import banco.model.entities.Cliente;
import banco.model.entities.Conta;
import db.DB;
import db.DbException;

public class ContaDaoJDBC implements ContaDao{
	
	private Connection conn;

	public ContaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void abrirConta( Conta conta) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("INSERT INTO contas (titular_id, titular_nome ) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, conta.getCliente().getId());
			st.setString(2, conta.getCliente().getNome());
			int linhasAfetadas = st.executeUpdate();

			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					conta.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void fecharConta(Integer id) {
		PreparedStatement st = null; 
		
		try {
			st = conn.prepareStatement("DELETE FROM contas WHERE id = ?");
			st.setInt(1, id);
			
			st.executeUpdate();
		} 
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Conta buscarPorId(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT contas.*,clientes.Nome as titular, clientes.data_nascimento as data_de_nascimento, "
					+ "clientes.email as email "
					+ "FROM contas INNER JOIN clientes ON contas.titular_id = clientes.Id WHERE contas.Id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if(rs.next()) {
				Cliente cliente = instanciarCliente(rs);
				Conta conta = instanciarConta(rs, cliente);
				return conta;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Conta instanciarConta(ResultSet rs, Cliente cliente) throws SQLException {
		Conta obj = new Conta();
		obj.setId(rs.getInt("Id"));
		obj.setSaldo(rs.getDouble("saldo"));
		obj.setCliente(cliente);
		return obj;
	}

	private Cliente instanciarCliente(ResultSet rs) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.setId(rs.getInt("titular_id"));
		cliente.setNome(rs.getString("titular"));
		cliente.setDataDeNascimento(rs.getDate("data_de_nascimento"));
		cliente.setEmail(rs.getString("email"));
		return cliente;
	}

	@Override
	public List<Conta> ListarContas() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT contas.*,clientes.Nome as titular, clientes.data_nascimento as data_de_nascimento, "
					+ "clientes.email as email "
					+ "FROM contas INNER JOIN clientes ON contas.titular_id = clientes.Id ORDER BY nome");
			
			rs = st.executeQuery();
			
			
			Map<Integer, Cliente> map = new HashMap<>();
			
			List<Conta>contas = new ArrayList<>();
			
			
			while (rs.next()) {
				Cliente cliente = map.get(rs.getInt("titular_id"));
				
				if(cliente ==  null) {
					cliente = instanciarCliente(rs);
					map.put(rs.getInt("titular_id"), cliente);
				}
				
				Conta conta = instanciarConta(rs,cliente);
				contas.add(conta);
				
			}
			return contas;
			
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Conta> ListarContasPorCliente(Cliente cliente) {
		PreparedStatement st = null;
		ResultSet rs= null;
		
		try {
			st = conn.prepareStatement("SELECT contas.*,clientes.Nome as titular, clientes.data_nascimento as data_de_nascimento, "
					+ "clientes.email as email "
					+ "FROM contas INNER JOIN clientes ON contas.titular_id = clientes.Id WHERE titular_id = ? ORDER BY nome");
			
			st.setInt(1, cliente.getId());
			rs = st.executeQuery();
			Map<Integer, Cliente> map = new HashMap<>();
			List<Conta>contas = new ArrayList<>();
			
			
			while (rs.next()) {
				Cliente cli = map.get(rs.getInt("titular_id"));
				
				if(cli ==  null) {
					cli = instanciarCliente(rs);
					map.put(rs.getInt("titular_id"), cli);
				}
				
				Conta conta = instanciarConta(rs,cli);
				contas.add(conta);
				
			}
			return contas;
			
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}



}
