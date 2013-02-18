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

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link ClassLoader} that iterates through all of the classloaders and classloaders of classes specified.
 */
public class ProxyFactoryClassLoader extends ClassLoader {

    private Map<String, Class<?>> classesByNames = new ConcurrentHashMap<String, Class<?>>();;

    public ProxyFactoryClassLoader(ClassLoader parent, Class<?>[] classes) {
        super(parent);
        if (classes != null) {
            for (Class<?> clazz : classes) {
                classesByNames.put(clazz.getName(), clazz);
            }
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            Class<?> clazz = classesByNames.get(name);
            if (clazz == null) {
                Class<?> result = null;
                Iterator<Class<?>> iterator = classesByNames.values().iterator();
                while (iterator.hasNext() && result == null) {
                    Class<?> currentClazz = iterator.next();
                    ClassLoader currentClazzLoader = currentClazz.getClassLoader();
                    try {
                        result = currentClazzLoader.loadClass(name);
                    } catch (ClassNotFoundException e1) {
                        result = null;
                    }
                }
                if (result != null) {
                    return result;
                } else {
                    throw e;
                }
            } else {
                return clazz;
            }
        }
    }

}
