package com.github.igorchs92.winston.message.broker;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

/**
 * Created by Igor on 2-6-2017.
 */
public class WinstonMessageBroker {

    public static void main(String[] args) throws Exception {
        EmbeddedActiveMQ embedded = new EmbeddedActiveMQ();

        embedded.start();

//        BrokerService broker = new BrokerService();
//        broker.setDataDirectoryFile(Resources.getFile("active-mq"));
//        broker.addConnector("tcp://localhost:61616");
//        broker.setPersistent(false);
//        SystemUsage systemUsage = broker.getSystemUsage();
//        systemUsage.getStoreUsage().setLimit(1024 * 1024 * 8);
//        systemUsage.getTempUsage().setLimit(1024 * 1024 * 8);
//        broker.start();
    }

}
