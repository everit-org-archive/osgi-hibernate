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

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyFactory.ClassLoaderProvider;

public class ProxyFactoryClassLoaderProvider implements ClassLoaderProvider {

    ClassLoaderProvider wrapped = null;

    @Override
    public ClassLoader get(ProxyFactory pf) {
        ClassLoader parent = null;
        if (wrapped != null) {
            parent = wrapped.get(pf);
        }
        
        if (parent == null) {
            parent = this.getClass().getClassLoader();
        }
        return new ProxyFactoryClassLoader(parent, pf.getInterfaces());
    }

    public ProxyFactoryClassLoaderProvider(ClassLoaderProvider wrapped) {
        this.wrapped = wrapped;
    }
    
    public ClassLoaderProvider getWrapped() {
        return wrapped;
    }

}
