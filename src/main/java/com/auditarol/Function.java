package com.auditarol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

public class Function {

    @FunctionName("RolAuditNotifier")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<String> request,
        final ExecutionContext context) {

        context.getLogger().info("📥 Aviso recibido: cambio de rol");

        try {
            String body = request.getBody();

            if (body == null || body.trim().isEmpty()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Cuerpo vacío")
                    .build();
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            RoleAuditPayload payload = mapper.readValue(body, RoleAuditPayload.class);

            // 🎯 Mostrar datos en consola (log)
            context.getLogger().info("🔔 CAMBIO DE ROL DETECTADO:");
            context.getLogger().info("👤 Usuario: " + payload.getRut());
            context.getLogger().info("🕑 Fecha/hora: " + payload.getTimestamp());
            context.getLogger().info("👮‍♂️ Cambiado por: " + payload.getModifiedBy());
            context.getLogger().info("🎭 Rol anterior: " + payload.getOldRole());
            context.getLogger().info("🆕 Nuevo rol: " + payload.getNewRole());

            return request.createResponseBuilder(HttpStatus.OK)
                .body("✅ Aviso de auditoría recibido correctamente")
                .build();

        } catch (Exception e) {
            context.getLogger().severe("❌ Error procesando el aviso: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage())
                .build();
        }
    }
}
