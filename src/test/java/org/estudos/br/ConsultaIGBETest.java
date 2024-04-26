package org.estudos.br;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsultaIGBETest {


    // Teste para consulta com UF correta
    @Test
    @DisplayName("Teste para verificar o formato da resposta")
    public void testVerificarFormatoResposta() throws IOException {
        // Arrange
        String uf = "SP"; // Definir uf correta

        // Act
        String resposta = ConsultaIBGE.consultarEstado(uf); // chamar o metodo

        // Assert
        // Verificar se a resposta contem os itens necessarios
        assertTrue(resposta.contains("id") && resposta.contains("sigla") && resposta.contains("nome"),
                "A resposta deve conter os campos id, sigla e nome");
    }

    // Teste para consulta com UF de estado inválido
    @Test
    @DisplayName("Teste para consulta com UF de estado inválido")
    public void testConsultarEstadoComCodigoInvalido() throws IOException {
        // Arrange
        String uf = "xx"; // Definir uma UF inválida

        // Mock HttpURLConnection para simular o comportamento da consulta
        HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);
        Mockito.when(mockConnection.getResponseCode()).thenReturn(404); // Simulando um código de resposta 404 (não encontrado)
        Mockito.when(mockConnection.getResponseMessage()).thenReturn("Not Found"); // Mensagem de erro correspondente ao código 404

        // Inject mock
        ConsultaIBGE consultaIBGE = new ConsultaIBGE(mockConnection);

        // Act
        String resposta = consultaIBGE.consultarEstado(uf); // Chamando o método de consulta com a UF inválida

        // Assert
        // Verifica se a resposta está vazia ou contém erro
        assertTrue(resposta.isEmpty() || resposta.contains("erro"), "A resposta deve estar vazia ou indicar erro para código de estado inválido");
    }


    // ultimo teste, para consultar distrito por id valido
    @Test
    @DisplayName("Teste para consultar distrito válido")
    public void testConsultarDistritoValido() throws IOException {
        // Arrange
        int idDistritoValido = 330010005; // Definir um id de distrito valido
        String expectedResponse = "{\"id\":330010005,\"nome\":\"Distrito Válido\",\"municipio\":{\"id\":67890,\"nome\":\"Município Válido\"}}";

        // Mock HttpURLConnection
        HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);
        Mockito.when(mockConnection.getResponseCode()).thenReturn(200);
        Mockito.when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(expectedResponse.getBytes()));

        // Inject mock
        ConsultaIBGE consultaIBGE;
        consultaIBGE = new ConsultaIBGE(mockConnection);

        // Act
        String resultado = consultaIBGE.consultarDistrito(idDistritoValido); // chamar o metodo usando mock

        // Assert
        // verificar se a resposta nao esta vazia
        assertFalse(resultado.isEmpty(), "A resposta não deve estar vazia");

        // verificar se a resposta contem os itens necessarios
        assertTrue(resultado.contains("id") && resultado.contains("nome") && resultado.contains("municipio"),
                "A resposta deve conter os campos id, nome e municipio");

        // Verificar se a resposta contem o id do distrito correto
        assertTrue(resultado.contains(String.valueOf(idDistritoValido)), "A resposta deve conter o ID do distrito consultado");
    }


}

