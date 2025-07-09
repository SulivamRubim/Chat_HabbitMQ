package com.sulivamrubim.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Producer monta JSON, habilita Publisher Confirms + menu interativo.
 */
public class Producer {
    private static final String RABBIT_HOST =
            System.getenv().getOrDefault("RABBIT_HOST", "localhost");
    private static final String RABBIT_QUEUE =
            System.getenv().getOrDefault("RABBIT_QUEUE", "srb_chat_queue");
    private static final String SENDER =
            System.getenv().getOrDefault("SENDER_NAME", "Sulivam");

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBIT_HOST);

        ObjectMapper mapper = new ObjectMapper();

        Connection connection = null;
        int tentativas = 10;

        // tenta se conectar ao RabbitMQ com atraso em caso de falha
        while (tentativas > 0) {
            try {
                connection = factory.newConnection();
                break;
            } catch (Exception e) {
                System.out.println("RabbitMQ não disponível. Tentando novamente em 5 segundos...");
                Thread.sleep(5000);
                tentativas--;
            }
        }

        if (connection == null) {
            System.err.println("Não foi possível conectar ao RabbitMQ após várias tentativas.");
            return;
        }

        try (Channel channel = connection.createChannel()) {
            channel.confirmSelect();
            channel.queueDeclare(RABBIT_QUEUE, true, false, false, null);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // menu interativo
                System.out.println("\n=== MENU ===");
                System.out.println("1) Enviar");
                System.out.println("2) Cancelar");
                System.out.print("Escolha uma opção: ");
                String opc = reader.readLine();

                if ("2".equals(opc)) {
                    System.out.println("Encerrando Producer.");
                    break;
                }

                if (!"1".equals(opc)) {
                    System.out.println("Opção inválida.");
                    continue;
                }

                System.out.print("Texto: ");
                String text = reader.readLine();
                if (text == null || text.trim().isEmpty()) {
                    System.out.println("Texto vazio, nada enviado.");
                    continue;
                }

                Map<String, Object> payload = new HashMap<>();
                payload.put("sender", SENDER);
                payload.put("timestamp", Instant.now().toString());
                payload.put("message", text);
                String json = mapper.writeValueAsString(payload);

                channel.basicPublish(
                        "", RABBIT_QUEUE,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        json.getBytes(StandardCharsets.UTF_8)
                );

                if (channel.waitForConfirms(5000)) {
                    System.out.println("Enviado com sucesso: " + json);
                } else {
                    System.err.println("Falha ao confirmar envio.");
                }
            }
        } finally {
            connection.close();
        }
    }
}
