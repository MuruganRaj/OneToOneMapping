package com.example.demo;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class DemoService  {

    @Autowired
    JdbcTemplate jdbcTemplate;



    public List<Wife> findAll(){

        return jdbcTemplate.query("select [DeptCode] ,[DesigCode]\n" +
                "      ,[JobType] from [EmpHistory]",new RowMapper());

    }


    public Wife create(Wife wife){

        try {
            String sql = "insert into wife() values(?,?,?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, wife.getId());
                ps.setString(2, wife.getName());

                return ps;
            }, keyHolder);

            int id = keyHolder.getKey().intValue();
            wife.setId(id);

        }catch (JDBCConnectionException e){
            e.printStackTrace();
        }
        return wife;

    }


}
