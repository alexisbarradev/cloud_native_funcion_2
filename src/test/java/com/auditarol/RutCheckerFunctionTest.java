package com.auditarol;

import com.microsoft.azure.functions.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RutCheckerFunctionTest {

    @Test
    public void testRutExists() throws Exception {
        String rut = "12345678-9";
        String json = "{ \"rut\": \"" + rut + "\" }";

        HttpRequestMessage<Optional<String>> request = mock(HttpRequestMessage.class);
        when(request.getBody()).thenReturn(Optional.of(json));
        when(request.createResponseBuilder(HttpStatus.OK))
            .thenReturn(new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(HttpStatus.OK));

        ExecutionContext context = mock(ExecutionContext.class);
        when(context.getLogger()).thenReturn(Logger.getGlobal());

        RutCheckerFunction function = new RutCheckerFunction();
        HttpResponseMessage response = function.run(request, context);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(response.getBody().toString().contains("true"));
    }

    @Test
    public void testRutDoesNotExist() throws Exception {
        String rut = "99999999-9";
        String json = "{ \"rut\": \"" + rut + "\" }";

        HttpRequestMessage<Optional<String>> request = mock(HttpRequestMessage.class);
        when(request.getBody()).thenReturn(Optional.of(json));
        when(request.createResponseBuilder(HttpStatus.OK))
            .thenReturn(new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(HttpStatus.OK));

        ExecutionContext context = mock(ExecutionContext.class);
        when(context.getLogger()).thenReturn(Logger.getGlobal());

        RutCheckerFunction function = new RutCheckerFunction();
        HttpResponseMessage response = function.run(request, context);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(response.getBody().toString().contains("false"));
    }
}
