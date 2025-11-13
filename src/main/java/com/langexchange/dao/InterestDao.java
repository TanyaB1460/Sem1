package com.langexchange.dao;

import com.langexchange.model.Interest;
import com.langexchange.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterestDao {

    public List<Interest> findAll() throws SQLException {
        String sql = "SELECT * FROM interests ORDER BY name";

        List<Interest> interests = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Interest interest = new Interest();
                interest.setId(rs.getLong("id"));
                interest.setName(rs.getString("name"));
                interest.setCategory(rs.getString("category"));
                interests.add(interest);
            }
        }
        return interests;
    }

    public Interest findById(Long id) throws SQLException {
        String sql = "SELECT * FROM interests WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Interest interest = new Interest();
                interest.setId(rs.getLong("id"));
                interest.setName(rs.getString("name"));
                interest.setCategory(rs.getString("category"));
                return interest;
            }
            return null;
        }
    }
}