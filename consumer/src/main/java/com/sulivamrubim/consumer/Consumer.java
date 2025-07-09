package com.sulivamrubim.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

/**
 * Consumer lê JSONs, faz ack manual e grava em chat.log.
 */
public class Consumer {
    private static final String RABBIT_HOST  =
        System.getenv().getOrDefault("RABBIT_HOST", "localhost");
    private static final String RABBIT_QUEUE =
        System.getenv().getOrDefault("RABBIT_QUEUE", "srb_chat_queue");
    private static final String LOG_FILE = "chat.log";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBIT_HOST);
        ObjectMapper mapper = new ObjectMapper();

        try (Connection conn = factory.newConnection();
             Channel channel = conn.createChannel()) {

            channel.queueDeclare(RABBIT_QUEUE, true, false, false, null);
            System.out.printf("Consumer iniciado em %s, fila='%s'%n", RABBIT_HOST, RABBIT_QUEUE);

            DeliverCallback callback = (tag, delivery) -> {
                String json = new String(delivery.getBody(), "UTF-8");
                try {
                    Map<String,Object> payload = mapper.readValue(json, Map.class);
                    String entry = String.format("[%s] %s: %s%n",
                        payload.get("timestamp"),
                        payload.get("sender"),
                        payload.get("message")
                    );
                    System.out.print("Recebido: " + entry);
                    Files.write(
                      Paths.get(LOG_FILE),
                      entry.getBytes("UTF-8"),
                      StandardOpenOption.CREATE, StandardOpenOption.APPEND
                    );
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            channel.basicConsume(RABBIT_QUEUE, false, callback, tag -> {});
            
            Thread.currentThread().join();
        }
    }
}
