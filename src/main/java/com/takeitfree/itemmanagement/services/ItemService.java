package com.takeitfree.itemmanagement.services;

import com.takeitfree.itemmanagement.dto.ItemPublicDTO;
import com.takeitfree.itemmanagement.dto.ItemRequestDTO;

import java.util.List;

public interface ItemService {

    String addItem(ItemRequestDTO itemRequestDTO);

    List<ItemPublicDTO> getAllItems();

    List<ItemPublicDTO> getMyAllItems();

    List<ItemPublicDTO> getItemsByTitle(String title);

    List<ItemPublicDTO> getItemsByPostalCode(String postalCode);

    List<ItemPublicDTO> getItemsByTaken(boolean taken);

    String updateItem(ItemRequestDTO itemRequestDTO);

    String deleteItem(Long id);

    boolean markItemAsTaken(Long id);

    List<ItemPublicDTO> getItemsByCity(String city);
}
