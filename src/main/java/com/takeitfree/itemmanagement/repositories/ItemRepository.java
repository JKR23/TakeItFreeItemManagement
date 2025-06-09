package com.takeitfree.itemmanagement.repositories;

import com.takeitfree.itemmanagement.models.Item;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByTitleContainingIgnoreCase(String title);

    List<Item> findByPostalCode(String postalCode);

    List<Item> findByTaken(boolean taken);

    List<Item> findByCityContainingIgnoreCase(@Size(max = 25, message = "city too long") String city);

}
