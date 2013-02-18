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

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

public class EntityManagerFactoryWrapper implements EntityManagerFactory {

    private EntityManagerFactory wrapped;
    
    private ClassLoader bundleClassLoader;

    public EntityManagerFactoryWrapper(EntityManagerFactory wrapped, ClassLoader bundleClassLoader) {
        super();
        this.wrapped = wrapped;
        this.bundleClassLoader = bundleClassLoader;
    }

    public EntityManager createEntityManager() {
        EntityManager entityManager = wrapped.createEntityManager();
        return new EntityManagerWrapper(entityManager, bundleClassLoader);
    }

    public EntityManager createEntityManager(Map map) {
        EntityManager entityManager = wrapped.createEntityManager(map);
        return new EntityManagerWrapper(entityManager, bundleClassLoader);
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return wrapped.getCriteriaBuilder();
    }

    public Metamodel getMetamodel() {
        return wrapped.getMetamodel();
    }

    public boolean isOpen() {
        return wrapped.isOpen();
    }

    public void close() {
        wrapped.close();
    }

    public Map<String, Object> getProperties() {
        return wrapped.getProperties();
    }

    public Cache getCache() {
        return wrapped.getCache();
    }

    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return wrapped.getPersistenceUnitUtil();
    }
    
    
}
