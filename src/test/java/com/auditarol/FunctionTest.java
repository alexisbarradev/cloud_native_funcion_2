package com.auditarol;

import com.microsoft.azure.functions.*;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionTest {

    private final Function function = new Function();

    @Test
    public void testPayloadValido() {
        String validJson = "{\n" +
                "  \"userRut\": \"12345678-9\",\n" +
                "  \"previousRole\": \"user\",\n" +
                "  \"currentRole\": \"admin\",\n" +
                "  \"modifier\": \"admin\",\n" +
                "  \"auditTime\": \"2025-03-31T19:00:00.000-04:00\"\n" +
                "}";

        HttpRequestMessage<String> request = new MockHttpRequest(HttpMethod.POST, validJson);
        ExecutionContext context = new MockExecutionContext();

        HttpResponseMessage response = function.run(request, context);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("✅ Aviso de auditoría recibido correctamente", response.getBody());
    }

    @Test
    public void testCuerpoVacio() {
        HttpRequestMessage<String> request = new MockHttpRequest(HttpMethod.POST, "");
        ExecutionContext context = new MockExecutionContext();

        HttpResponseMessage response = function.run(request, context);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Cuerpo vacío", response.getBody());
    }

    // ================= MOCKS =================

    private static class MockHttpRequest implements HttpRequestMessage<String> {
        private final HttpMethod method;
        private final String body;

        public MockHttpRequest(HttpMethod method, String body) {
            this.method = method;
            this.body = body;
        }

        @Override public HttpMethod getHttpMethod() { return method; }

        @Override public String getBody() { return body; }


        @Override public Map<String, String> getQueryParameters() { return new HashMap<>(); }

        @Override public URI getUri() { return URI.create("http://localhost"); }

        @Override public Map<String, String> getHeaders() { return new HashMap<>(); }

        @Override
        public HttpResponseMessage.Builder createResponseBuilder(HttpStatus status) {
            return new HttpResponseBuilderMock().status(status);
        }

        @Override
        public HttpResponseMessage.Builder createResponseBuilder(HttpStatusType status) {
            return new HttpResponseBuilderMock().status(status);
        }
    }

    private static class MockExecutionContext implements ExecutionContext {
        @Override public Logger getLogger() {
            return Logger.getLogger("TestLogger");
        }

        @Override public String getInvocationId() {
            return UUID.randomUUID().toString();
        }

        @Override public String getFunctionName() {
            return "RolAuditNotifier";
        }
    }

    // ✅ Builder + Response mock incluidos aquí
    private static class HttpResponseBuilderMock implements HttpResponseMessage.Builder {
        private HttpStatusType status;
        private Object body;


        @Override public HttpResponseMessage.Builder status(HttpStatusType status) {
            this.status = status;
            return this;
        }

        @Override public HttpResponseMessage.Builder header(String key, String value) {
            return this; // No hace falta para este test
        }

        @Override public HttpResponseMessage.Builder body(Object body) {
            this.body = body;
            return this;
        }

        @Override public HttpResponseMessage build() {
            return new HttpResponseMessage() {
                @Override public int getStatusCode() { return status.value(); }

                @Override public HttpStatusType getStatus() { return status; }

                @Override public Object getBody() { return body; }

                @Override public String getHeader(String key) { return null; }

                
            };
        }
    }
}
