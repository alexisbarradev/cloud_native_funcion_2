package com.auditarol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class RoleAuditDAO {

    public void insertAudit(RoleAuditPayload payload) throws Exception {
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASS");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
            String sql = "INSERT INTO role_audit (rut, old_role, new_role, modified_by, timestamp) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, payload.getRut());
                stmt.setString(2, payload.getOldRole());
                stmt.setString(3, payload.getNewRole());
                stmt.setString(4, payload.getModifiedBy());

                // ✅ Convertir OffsetDateTime a java.sql.Timestamp
                OffsetDateTime timestamp = payload.getTimestamp();
                if (timestamp != null) {
                    stmt.setTimestamp(5, Timestamp.from(timestamp.toInstant()));
                } else {
                    stmt.setTimestamp(5, null); // O manejar el caso nulo de otra manera si es necesario
                }


                stmt.executeUpdate();
            }
        }
    }
}