package com.auditarol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.*;

public class RutCheckerFunction {

    @FunctionName("checkRutExists")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context
    ) {
        context.getLogger().info("🔍 Verificando si el RUT existe...");

        try {
            String body = request.getBody().orElse("{}");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> input = mapper.readValue(body, Map.class);

            String rutToCheck = input.get("rut");

            // ✅ Hardcoded list of users (simulate /api/users)
            List<String> existingRuts = List.of("12345678-9", "87654321-0", "11111111-1");

            boolean exists = existingRuts.contains(rutToCheck);

            Map<String, Object> response = new HashMap<>();
            response.put("rut", rutToCheck);
            response.put("exists", exists);

            return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(mapper.writeValueAsString(response))
                .build();

        } catch (Exception e) {
            context.getLogger().severe("❌ Error: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno: " + e.getMessage())
                .build();
        }
    }
}
