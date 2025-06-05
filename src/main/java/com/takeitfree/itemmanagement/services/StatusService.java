package com.takeitfree.itemmanagement.services;

import com.takeitfree.itemmanagement.dto.ItemDTO;
import com.takeitfree.itemmanagement.dto.StatusDTO;

import java.util.List;
import java.util.Optional;

public interface StatusService {
    List<StatusDTO> getAllStatus();
    StatusDTO getStatusByName(String name);
    String addStatus(StatusDTO status);
    String updateStatus(StatusDTO status);
    String deleteStatus(Long id);

    List<ItemDTO> getItemsByStatusName(String name);
}
