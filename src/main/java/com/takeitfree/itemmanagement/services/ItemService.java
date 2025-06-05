package com.takeitfree.itemmanagement.services;

import com.takeitfree.itemmanagement.dto.ItemDTO;

import java.util.List;

public interface ItemService {

    String addItem(ItemDTO itemDTO);

    List<ItemDTO> getAllItems();

    List<ItemDTO> getItemsByTitle(String title);

    List<ItemDTO> getItemsByLocalization(String localization);

    List<ItemDTO> getItemsByDistance(Float distance);

    List<ItemDTO> getItemsByTaken(boolean taken);

    String updateItem(ItemDTO itemDTO);

    String deleteItem(Long id);

    boolean markItemAsTaken(Long id);
}
