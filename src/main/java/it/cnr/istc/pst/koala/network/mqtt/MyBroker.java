/*
 * Copyright (C) 2017 Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.pst.koala.network.mqtt;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class MyBroker extends BrokerFilter {

    private static final Logger LOG = Logger.getLogger(MyBroker.class.getName());

    public MyBroker(Broker next) {
        super(next);
    }

    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
        LOG.log(Level.INFO, "new connection: {0}", info);
        System.out.println("new !");
        super.addConnection(context, info);
    }

    @Override
    public void removeConnection(ConnectionContext context, ConnectionInfo info, Throwable error) throws Exception {
        LOG.log(Level.INFO, "lost connection: {0}", info);
        System.out.println("lost");
        super.removeConnection(context, info, error);
    }
}
