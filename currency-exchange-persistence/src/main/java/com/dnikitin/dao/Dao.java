package com.dnikitin.dao;

import java.util.List;
import java.util.Optional;
/**
 * Generic Data Access Object (DAO) interface.
 * Defines standard persistence operations for entities.
 *
 * @param <K> The type of the key used to identify the entity (e.g., String, Long, or a Value Object).
 * @param <E> The type of the entity being persisted.
 */
public interface Dao<K, E> {
    /**
     * Retrieves all entities from the database.
     *
     * @return A list containing all found entities.
     */
    List<E> findAll();

    /**
     * Finds a specific entity by its unique identifier.
     *
     * @param id The unique key of the entity to find.
     * @return An Optional containing the found entity, or empty if no match exists.
     */
    Optional<E> findById(K id);

    /**
     * Persists a new entity into the database.
     *
     * @param entity The entity object to be saved.
     * @return The saved entity, typically including any database-generated identifiers.
     */
    E save(E entity);

}
