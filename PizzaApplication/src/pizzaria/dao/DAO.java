package pizzaria.dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sabrina
 */
import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    int inserir(T entidade) throws SQLException;
    T buscarPorId(int id) throws SQLException;
    List<T> listarTodos() throws SQLException;
    boolean atualizar(T entidade) throws SQLException;
    boolean remover(int id) throws SQLException;
}