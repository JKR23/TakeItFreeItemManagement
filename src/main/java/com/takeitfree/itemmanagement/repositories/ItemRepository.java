package com.takeitfree.itemmanagement.repositories;

import com.takeitfree.itemmanagement.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByTitleContainingIgnoreCase(String title);

    List<Item> findByLocalization(String localization);

    List<Item> findByDistance(Float distance);

    List<Item> findByTaken(boolean taken);

}
