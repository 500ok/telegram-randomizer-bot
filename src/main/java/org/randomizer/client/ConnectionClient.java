package org.randomizer.client;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionClient {

    private static ConnectionClient instance;
    private static HttpClientConnectionManager connectionManager;
    private static ConcurrentHashMap<String, HttpClient> clients;
    private static Lock lock = new ReentrantLock();

    private ConnectionClient() {}

    public static ConnectionClient getInstance() {

        if (instance == null) {
            lock.lock();
                if (instance == null) {
                    instance = new ConnectionClient();
                    connectionManager = new PoolingHttpClientConnectionManager();
                    clients = new ConcurrentHashMap<>();
                }
            lock.unlock();
        }

        return instance;
    }

    public String get(URI uri, Map<String, String> headers) {
        String host = uri.getHost();
        HttpClient client = clients.get(host);
        if (client == null) {
            client = addClient(host);
        }

        HttpGet get = new HttpGet(uri);
        for (Map.Entry<String, String> header: headers.entrySet()) {
            get.setHeader(header.getKey(), header.getValue());
        }

        System.out.println("GetRequest: " + get.toString());

        try {
            HttpResponse response = client.execute(get);

            System.out.println("GetResponse: " + response.getEntity().toString());

            String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            System.out.println("Content: " + content);

            EntityUtils.consume(response.getEntity());

            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpClient addClient(String host) {
        HttpClient client = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
        clients.put(host, client);
        return client;
    }
}
