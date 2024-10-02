package com.buesimples.posfx.services;

import com.buesimples.posfx.models.Artigos;
import com.buesimples.posfx.services.map.ItemData;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.HBox;

/**
 *
 * @author sienekib
 */
public class OrderControlService {
    
    private static OrderControlService instance = null;
    
    // Mapa para armazenar os itens do carrinho, usando o id do artigo como chave
    private Map<Integer, ItemData> itemMap = new HashMap<>();
    
    public OrderControlService() {
        instance = this;
    }
    
    public static OrderControlService getInstance() {
        if (instance == null) {
            new OrderControlService();
        }
        return instance;
    }
    
    // Método para adicionar ou atualizar um item no mapa
    public void adicionarOuAtualizarItem(int idArtigo, String descricao, int quantidade, double precoInicial, int idImposto, double valorImposto) {
        if (itemMap.containsKey(idArtigo)) {
            // Atualiza a quantidade se o item já existir
            ItemData itemExistente = itemMap.get(idArtigo);
            int novaQuantidade = itemExistente.getQuantidade() + quantidade;
            itemExistente.atualizarQuantidade(novaQuantidade);
        } else {
            // Cria um novo item se não existir
            double valorTotal = quantidade * precoInicial;
            ItemData novoItem = new ItemData(idArtigo, descricao, quantidade, precoInicial, idImposto, valorImposto, valorTotal);
            itemMap.put(idArtigo, novoItem);
        }
    }

    // Método para obter o mapa de itens
    public Map<Integer, ItemData> getItemMap() {
        return itemMap;
    }

    // Exemplo de método para obter um item específico
    public ItemData getItem(int idArtigo) {
        return itemMap.get(idArtigo);
    }

}
