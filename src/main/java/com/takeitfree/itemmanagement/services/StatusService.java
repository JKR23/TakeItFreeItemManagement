package com.takeitfree.itemmanagement.services;

import com.takeitfree.itemmanagement.dto.ItemPublicDTO;
import com.takeitfree.itemmanagement.dto.ItemRequestDTO;
import com.takeitfree.itemmanagement.dto.StatusDTO;

import java.util.List;

public interface StatusService {
    List<StatusDTO> getAllStatus();
    StatusDTO getStatusByName(String name);
    String addStatus(StatusDTO status);
    String updateStatus(StatusDTO status);
    String deleteStatus(Long id);

    List<ItemRequestDTO> getItemsByStatusName(String name);

    List<ItemRequestDTO> getItemsByStatusId(Long id);
    List<ItemPublicDTO> getAllStatusItems();
}
