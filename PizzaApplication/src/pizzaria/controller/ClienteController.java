/*
 * Gerencia o cadastro e a busca de clientes.
 */
package pizzaria.controller;

import java.util.ArrayList;
import java.util.List;
import Cliente; // Remova se estiver no mesmo pacote raiz

public class ClienteController {
    private List<Cliente> clientesDatabase;

    public ClienteController() {
        this.clientesDatabase = new ArrayList<>();
    }

    // Cria e salva um cliente
    public Cliente cadastrarCliente(int id, String nome, String sobrenome, String telefone) {
        Cliente novoCliente = new Cliente(id, nome, sobrenome, telefone);
        clientesDatabase.add(novoCliente);
        return novoCliente;
    }

    // Busca cliente pelo ID
    public Cliente buscarPorId(int id) {
        for (Cliente c : clientesDatabase) {
            if (c.GetID() == id) {
                return c;
            }
        }
        return null;
    }

    public List<Cliente> listarClientes() {
        return clientesDatabase;
    }
}
