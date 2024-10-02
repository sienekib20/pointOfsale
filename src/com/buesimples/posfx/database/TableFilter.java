/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.buesimples.posfx.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 *
 * @author sienekib
 */
import java.util.function.Predicate;

public class TableFilter {

    public static <T> void filtrarTabela(TableView<T> table, ObservableList<T> itemList, String filterText, Predicate<T> filterCriteria) {
        String filterName = filterText.trim().toLowerCase();

        if (filterName.isEmpty()) {
            table.setItems(itemList);
        } else {
            ObservableList<T> filteredList = FXCollections.observableArrayList();
            for (T item : itemList) {
                if (filterCriteria.test(item)) {  // Aplica o critério de filtragem
                    filteredList.add(item);
                }
            }
            table.setItems(filteredList);
        }
    }
}

//TableFilter.filtrarTabela(entidadeTable, entidadeList, inputPesquisa.getText(), entidade -> 
//    entidade.getNome().toLowerCase().contains(inputPesquisa.getText().toLowerCase()) ||
//    entidade.getNIF().toLowerCase().contains(inputPesquisa.getText().toLowerCase())
//);
//
//
//TableFilter.filtrarTabela(produtoTable, produtoList, inputPesquisa.getText(), produto -> 
//    produto.getDescricao().toLowerCase().contains(inputPesquisa.getText().toLowerCase()) ||
//    produto.getCodigo().toLowerCase().contains(inputPesquisa.getText().toLowerCase())
//);
