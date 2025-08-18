package com.takeitfree.itemmanagement.config.utils;

import com.takeitfree.itemmanagement.models.Status;
import com.takeitfree.itemmanagement.repositories.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StatusRepository statusRepository;

    @Override
    public void run(String... args) {
        // Create status if none role present
        if (statusRepository.count() == 0) {

            //Create status
            statusRepository.save(Status.builder().name("Broken").build());
            statusRepository.save(Status.builder().name("Works").build());
            statusRepository.save(Status.builder().name("In_Good_Condition").build());
        }

    }
}

