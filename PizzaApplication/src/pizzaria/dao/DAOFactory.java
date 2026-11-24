package pizzaria.dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sabrina
 */
public class DAOFactory {
    public static ClienteDAO getClienteDAO(DAOType tipo) {
        return switch (tipo) {
            case SQL -> new ClienteDAOSql();
        };
    }
    public static PedidoDAO getPedidoDAO(DAOType tipo) {
        return switch (tipo) {
            case SQL -> new PedidoDAOSql();
        };
    }
    public static SaborDAO getSaborDAO(DAOType tipo) {
        return switch (tipo) {
            case SQL -> new SaborDAOSql();
        };
    }
    public static PrecoDAO getPrecoDAO(DAOType tipo) {
        return switch (tipo) {
            case SQL -> new PrecoDAOSql();
        };
    }
}