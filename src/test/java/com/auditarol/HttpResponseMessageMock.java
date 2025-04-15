package com.auditarol;

import com.microsoft.azure.functions.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase simulada para pruebas unitarias.
 */
public class HttpResponseMessageMock implements HttpResponseMessage {
    private final int status;
    private final Object body;
    private final Map<String, String> headers;

    public HttpResponseMessageMock(int status, Object body, Map<String, String> headers) {
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.valueOf(status);
    }

    @Override
    public int getStatusCode() {
        return status;
    }

    @Override
    public Object getBody() {
        return body;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    
    public Map<String, String> getHeaders() {
        return headers;
    }

    // ✅ Clase interna Builder para usar en tests
    public static class HttpResponseMessageBuilderMock implements HttpResponseMessage.Builder {
        private int status;
        private Object body;
        private final Map<String, String> headers = new HashMap<>();

        
        public Builder status(HttpStatus status) {
            this.status = status.value();
            return this;
        }

        @Override
        public Builder status(HttpStatusType status) {
            this.status = status.value();
            return this;
        }

        @Override
        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        @Override
        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        @Override
        public HttpResponseMessage build() {
            return new HttpResponseMessageMock(status, body, headers);
        }
    }
}
