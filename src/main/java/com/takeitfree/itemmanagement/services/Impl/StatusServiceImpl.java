package com.takeitfree.itemmanagement.services.Impl;

import com.takeitfree.itemmanagement.dto.ItemDTO;
import com.takeitfree.itemmanagement.dto.StatusDTO;
import com.takeitfree.itemmanagement.models.Status;
import com.takeitfree.itemmanagement.repositories.StatusRepository;
import com.takeitfree.itemmanagement.services.StatusService;
import com.takeitfree.itemmanagement.validators.ObjectValidator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final ObjectValidator objectValidator;
    private final StatusRepository statusRepository;

    @Override
    public List<StatusDTO> getAllStatus() {
        return StatusDTO.toDTO(statusRepository.findAll());
    }

    @Override
    public StatusDTO getStatusByName(String name) {
        try {
            objectValidator.validate(name);
            Optional<Status> optionalStatus = Optional.ofNullable(statusRepository.findByName(name));

            if (optionalStatus.isEmpty()) {
                throw new EntityNotFoundException("Status not found");
            }

            return StatusDTO.toDTO(optionalStatus.get());

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public String addStatus(StatusDTO status) {
        try {
            objectValidator.validate(status);

            Optional<Status> optionalStatus = Optional.ofNullable(statusRepository.findByName(status.getName()));

            if (optionalStatus.isPresent()) {
                throw new EntityExistsException("Status already exists");
            }

            statusRepository.save(StatusDTO.toEntity(status));

            return "Status added successfully";
        } catch (EntityExistsException e) {
            throw new EntityExistsException(e.getMessage());
        }
    }

    @Override
    public String updateStatus(StatusDTO status) {
        try {
            objectValidator.validate(status);

            Optional<Status> optionalStatus = statusRepository.findById(status.getId());

            if (optionalStatus.isEmpty()) {
                throw new EntityNotFoundException("Status doesn't exists");
            }

            optionalStatus.get().setName(status.getName());

            statusRepository.save(optionalStatus.get());

            return "Status updated successfully";

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public String deleteStatus(Long id) {
        try {
            objectValidator.validate(id);

            Optional<Status> optionalStatus = statusRepository.findById(id);

            if (optionalStatus.isEmpty()) {
                throw new EntityNotFoundException("Status doesn't exists");
            }

            statusRepository.delete(optionalStatus.get());

            return "Status deleted successfully";

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ItemDTO> getItemsByStatusName(String name) {

        try {
            objectValidator.validate(name);

            Optional<Status> statusOptional = Optional.ofNullable(statusRepository.findByName(name));

            if (statusOptional.isEmpty()) {
                throw new EntityNotFoundException("Status doesn't exists");
            }

            return ItemDTO.toDTO(statusOptional.get().getItemList());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
