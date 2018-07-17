package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapper implements org.springframework.jdbc.core.RowMapper<Wife> {
    @Override
    public Wife mapRow(ResultSet resultSet, int i) throws SQLException {
        Wife wife = new Wife();
        wife.setId(resultSet.getInt(1));
        wife.setName(resultSet.getString(2));

        return wife;
    }
}
