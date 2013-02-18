package org.everit.osgi.hibernate.adapter;

/*
 * Copyright (c) 2011, Everit Kft.
 *
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

import java.util.Dictionary;
import java.util.Hashtable;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyFactory.ClassLoaderProvider;

import javax.persistence.spi.PersistenceProvider;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.ejb.HibernatePersistence;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

/**
 * {@link BundleActivator} that registers the Hibernate DataProvider OSGI service.
 */
public class HibernatePersistenceActivator implements BundleActivator {

    /**
     * The property name of the dataprovider service class definition.
     */
    private static final String JAVAX_PERSISTENCE_PROVIDER_PROP = "javax.persistence.provider";

    /**
     * The Hibernate DataProvider service registration that is created by this activator.
     */
    private ServiceRegistration serviceRegistration;

    private ServiceTracker transactionManagerTracker;

    private ServiceTracker userTransactionTracker;

    @Override
    public void start(final BundleContext context) throws Exception {
        // ClassLoaderProvider classLoaderProvider = ProxyFactory.classLoaderProvider;

        ServiceTracker lTransactionManagerTracker = new ServiceTracker(context,
                TransactionManager.class.getName(), null);
        lTransactionManagerTracker.open();
        this.transactionManagerTracker = lTransactionManagerTracker;

        ServiceTracker lUserTransactionTracker = new ServiceTracker(context, UserTransaction.class.getName(), null);
        this.userTransactionTracker = lUserTransactionTracker;

        ClassLoaderProvider wrappedClassLoaderProvider = ProxyFactory.classLoaderProvider;
        ProxyFactoryClassLoaderProvider classLoaderProvider = new ProxyFactoryClassLoaderProvider(
                wrappedClassLoaderProvider);
        ProxyFactory.classLoaderProvider = classLoaderProvider;

        PersistenceProvider persistenceProvider = new HibernatePersistenceProvider(new HibernatePersistence(),
                transactionManagerTracker, userTransactionTracker);
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put(JAVAX_PERSISTENCE_PROVIDER_PROP, HibernatePersistence.class.getName());
        serviceRegistration = context.registerService(PersistenceProvider.class.getName(), persistenceProvider, props);
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        ClassLoaderProvider clp = ProxyFactory.classLoaderProvider;
        if (clp instanceof ProxyFactoryClassLoaderProvider) {
            ProxyFactory.classLoaderProvider = ((ProxyFactoryClassLoaderProvider) clp).getWrapped();
        }
        transactionManagerTracker.close();
        userTransactionTracker.close();
        serviceRegistration.unregister();
    }

}
