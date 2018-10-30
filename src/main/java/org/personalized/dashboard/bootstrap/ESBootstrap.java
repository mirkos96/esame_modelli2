package org.personalized.dashboard.bootstrap;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.personalized.dashboard.utils.ConfigKeys;

/**
 * Created by sudan on 5/7/15.
 */
public class ESBootstrap {

    private static Client elasticSearchClient;
    private static boolean isInitialized = false;

    public static void init() {

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", ConfigKeys.ES_CLUSTERNAME)
                .put("node.name", ConfigKeys.ES_NODENAME).build();
        if (!isInitialized) {
            elasticSearchClient = new TransportClient(settings)
                    .addTransportAddress(
                            new InetSocketTransportAddress
                                    (
                                            ConfigKeys.ES_URL,
                                            Integer.parseInt(ConfigKeys.ES_PORT)
                                    )
                    );
            isInitialized = true;
        }
    }

    public static Client getClient() {
        return elasticSearchClient;
    }
}
