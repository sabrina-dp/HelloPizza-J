/*
 * Focado exclusivamente em atualizar e verificar a situação/etapa em que o pedido se encontra no fluxo de produção.
 */

package pizzaria.controller;

import Pedido;
import EstadoPedido;

public class StatusController {

    // Altera o estado atual do pedido (Aberto -> Pronto -> Entregue)
    public void atualizarStatus(Pedido pedido, EstadoPedido novoEstado) {
        if (pedido != null && novoEstado != null) {
            pedido.mudarEstado(novoEstado);
        }
    }

    // Consulta em qual etapa o pedido está
    public EstadoPedido verificarStatus(Pedido pedido) {
        if (pedido != null) {
            return pedido.getEstado();
        }
        return null;
    }
}
