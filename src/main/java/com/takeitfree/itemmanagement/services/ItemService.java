package com.takeitfree.itemmanagement.services;

import com.takeitfree.itemmanagement.dto.ItemRequestDTO;

import java.util.List;

public interface ItemService {

    String addItem(ItemRequestDTO itemRequestDTO);

    List<ItemRequestDTO> getAllItems();

    List<ItemRequestDTO> getItemsByTitle(String title);

    List<ItemRequestDTO> getItemsByLocalization(String localization);

    List<ItemRequestDTO> getItemsByDistance(Float distance);

    List<ItemRequestDTO> getItemsByTaken(boolean taken);

    String updateItem(ItemRequestDTO itemRequestDTO);

    String deleteItem(Long id);

    boolean markItemAsTaken(Long id);
}
