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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.validator.HibernateValidatorFactory;
import org.osgi.util.tracker.ServiceTracker;

public class HibernatePersistenceProvider implements PersistenceProvider {

    private HibernatePersistence delegate;

    private ServiceTracker transactionManagerTracker;

    private ServiceTracker userTransactionTracker;

    public HibernatePersistenceProvider(final HibernatePersistence delegate,
            final ServiceTracker transactionManagerTracker,
            final ServiceTracker userTransactionTracker) {
        super();
        this.delegate = delegate;
        this.transactionManagerTracker = transactionManagerTracker;
        this.userTransactionTracker = userTransactionTracker;
    }

    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(final PersistenceUnitInfo info, final Map map) {
        Map extendedMap = generateExtendedSettings(map);
        EntityManagerFactory emf = delegate.createContainerEntityManagerFactory(info, extendedMap);
        return new EntityManagerFactoryWrapper(emf, info.getClassLoader());
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory(final String emName, final Map map) {
        Map extendedMap = generateExtendedSettings(map);
        return delegate.createEntityManagerFactory(emName, extendedMap);
    }

    protected Map<?, ?> generateExtendedSettings(final Map<?, ?> map) {
        Map<Object, Object> result = null;
        if (map == null) {
            result = new ConcurrentHashMap<Object, Object>();
        } else {
            result = new ConcurrentHashMap<Object, Object>(map);
        }
        result.put("javax.persistence.validation.factory", getValidatorFactory());
        result.put(AvailableSettings.JTA_CACHE_TM, false);
        result.put(AvailableSettings.JTA_CACHE_UT, false);

        OsgiJtaPlatform osgiJtaPlatform = new OsgiJtaPlatform(transactionManagerTracker, userTransactionTracker);
        osgiJtaPlatform.configure(result);
        result.put(AvailableSettings.JTA_PLATFORM, osgiJtaPlatform);
        return result;
    }

    @Override
    public ProviderUtil getProviderUtil() {
        return delegate.getProviderUtil();
    }

    protected ValidatorFactory getValidatorFactory() {
        Thread currentThread = Thread.currentThread();
        ClassLoader oldClassLoader = currentThread.getContextClassLoader();
        try {
            ClassLoader hibernateValidatorClassLoader = HibernateValidatorFactory.class.getClassLoader();
            currentThread.setContextClassLoader(hibernateValidatorClassLoader);
            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            return validatorFactory;
        } finally {
            currentThread.setContextClassLoader(oldClassLoader);
        }

    }
}
