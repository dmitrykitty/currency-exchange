package com.dnikitin.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface used by DAO implementations to map rows of a {@link java.sql.ResultSet} on a per-row basis.
 * Implementations of this interface perform the actual work of mapping each row to a result object.
 *
 * @param <T> The type of the result object.
 */
public interface RowMapper <T>{
    /**
     * Maps a single row of data from a ResultSet to an object of type T.
     *
     * @param rs The ResultSet to map (pre-initialized to the current row).
     * @return The result object for the current row.
     * @throws java.sql.SQLException If an error occurs while accessing ResultSet data.
     */
    T mapRow(ResultSet rs) throws SQLException;
}
