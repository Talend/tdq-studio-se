// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.administrator.common.persistence;

import org.hibernate.SessionFactory;
import org.talend.administrator.common.persistence.elver.HibernateFactory;
import org.talend.administrator.common.persistence.hibernate.HibernatePersistenceAdapter;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public final class PersistenceUtil {
    
    private PersistenceUtil() {
        
    }

    private static HibernatePersistenceAdapter persistentAdapter;

    private static SessionFactory sessionFactory;

    public static synchronized HibernatePersistenceAdapter getPersistenceAdapter() {
        if (persistentAdapter == null) {
            persistentAdapter = new HibernatePersistenceAdapter(PersistenceUtil.getSessionFactory());
        }
        return persistentAdapter;
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (persistentAdapter == null) {
            sessionFactory = new HibernateFactory().getPrefDataStore().getSessionFactory();
        }
        return sessionFactory;
    }

}
