package org.everit.osgi.hibernate.itests.core.service;

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

import javax.persistence.EntityManager;

import org.everit.osgi.hibernate.itests.entity.CarEntity;

public class CarServiceImpl implements CarService {

    /**
     * EntityManager set by blueprint.
     */
    private EntityManager em;

    public void setEm(EntityManager em) {
        this.em = em;
    }
    
    /* (non-Javadoc)
     * @see org.everit.osgi.samples.hibernate.tests.core.service.CarService#saveCar(java.lang.String)
     */
    @Override
    public CarEntity saveCar(String carType) {
        CarEntity carEntity = new CarEntity();
        carEntity.setCarType(carType);
        em.persist(carEntity);
        return carEntity;
    }
    
}
