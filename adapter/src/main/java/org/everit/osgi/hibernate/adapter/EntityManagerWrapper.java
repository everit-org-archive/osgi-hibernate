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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

/**
 * A wrapper for the created entitymanager to fix issues of Hibernate like class loading ones.
 */
public class EntityManagerWrapper implements EntityManager {

    /**
     * The wrapped entity manager.
     */
    private EntityManager entityManager;

    /**
     * The classloader of the bundle that contains the PU.
     */
    private ClassLoader bundleClassLoader;

    public EntityManagerWrapper(final EntityManager entityManager, final ClassLoader bundleClassLoader) {
        super();
        this.entityManager = entityManager;
        this.bundleClassLoader = bundleClassLoader;
    }

    public void persist(Object entity) {
        entityManager.persist(entity);
    }

    public <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

    public void remove(Object entity) {
        entityManager.remove(entity);
    }

    public <T> T find(Class<T> entityClass, Object primaryKey) {
        return entityManager.find(entityClass, primaryKey);
    }

    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return entityManager.find(entityClass, primaryKey, properties);
    }

    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return entityManager.find(entityClass, primaryKey, lockMode);
    }

    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        return entityManager.find(entityClass, primaryKey, lockMode, properties);
    }

    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return entityManager.getReference(entityClass, primaryKey);
    }

    public void flush() {
        entityManager.flush();
    }

    public void setFlushMode(FlushModeType flushMode) {
        entityManager.setFlushMode(flushMode);
    }

    public FlushModeType getFlushMode() {
        return entityManager.getFlushMode();
    }

    public void lock(Object entity, LockModeType lockMode) {
        entityManager.lock(entity, lockMode);
    }

    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        entityManager.lock(entity, lockMode, properties);
    }

    public void refresh(Object entity) {
        entityManager.refresh(entity);
    }

    public void refresh(Object entity, Map<String, Object> properties) {
        entityManager.refresh(entity, properties);
    }

    public void refresh(Object entity, LockModeType lockMode) {
        entityManager.refresh(entity, lockMode);
    }

    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        entityManager.refresh(entity, lockMode, properties);
    }

    public void clear() {
        entityManager.clear();
    }

    public void detach(Object entity) {
        entityManager.detach(entity);
    }

    public boolean contains(Object entity) {
        return entityManager.contains(entity);
    }

    public LockModeType getLockMode(Object entity) {
        return entityManager.getLockMode(entity);
    }

    public void setProperty(String propertyName, Object value) {
        entityManager.setProperty(propertyName, value);
    }

    public Map<String, Object> getProperties() {
        return entityManager.getProperties();
    }

    public Query createQuery(String qlString) {
        return entityManager.createQuery(qlString);
    }

    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        Thread currentThread = Thread.currentThread();
        ClassLoader previousClassLoader = currentThread.getContextClassLoader();
        currentThread.setContextClassLoader(bundleClassLoader);
        try {
            return entityManager.createQuery(criteriaQuery);
        } finally {
            if (previousClassLoader != null) {
                currentThread.setContextClassLoader(previousClassLoader);
            }
        }
        
    }

    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        return entityManager.createQuery(qlString, resultClass);
    }

    public Query createNamedQuery(String name) {
        return entityManager.createNamedQuery(name);
    }

    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return entityManager.createNamedQuery(name, resultClass);
    }

    public Query createNativeQuery(String sqlString) {
        return entityManager.createNativeQuery(sqlString);
    }

    public Query createNativeQuery(String sqlString, Class resultClass) {
        return entityManager.createNativeQuery(sqlString, resultClass);
    }

    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return entityManager.createNativeQuery(sqlString, resultSetMapping);
    }

    public void joinTransaction() {
        entityManager.joinTransaction();
    }

    public <T> T unwrap(Class<T> cls) {
        return entityManager.unwrap(cls);
    }

    public Object getDelegate() {
        return entityManager.getDelegate();
    }

    public void close() {
        entityManager.close();
    }

    public boolean isOpen() {
        return entityManager.isOpen();
    }

    public EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManager.getEntityManagerFactory();
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    public Metamodel getMetamodel() {
        return entityManager.getMetamodel();
    }

}
