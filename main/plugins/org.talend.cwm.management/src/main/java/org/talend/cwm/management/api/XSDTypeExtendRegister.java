// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.cwm.management.api;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.xsd.impl.type.XSDAnySimpleType;
import org.eclipse.xsd.impl.type.XSDDecimalType;
import org.eclipse.xsd.impl.type.XSDTypeRegister;

/**
 * 
 * DOC mzhao class global comment. Detailled comment
 */
public class XSDTypeExtendRegister extends XSDTypeRegister {

    public static Map<String, XSDAnySimpleType> getMap() {
        Map<String, XSDAnySimpleType> map = XSDTypeRegister.getMap();

        map = new HashMap<String, XSDAnySimpleType>();
        // Extend decimal
        map.put("integer", new XSDDecimalType()); //$NON-NLS-1$
        map.put("nonPositiveInteger", new XSDDecimalType()); //$NON-NLS-1$
        map.put("long", new XSDDecimalType()); //$NON-NLS-1$
        map.put("nonNegativeInteger", new XSDDecimalType()); //$NON-NLS-1$
        map.put("negativeInteger", new XSDDecimalType()); //$NON-NLS-1$
        map.put("int", new XSDDecimalType()); //$NON-NLS-1$
        map.put("unsignedLong", new XSDDecimalType()); //$NON-NLS-1$
        map.put("positiveInteger", new XSDDecimalType()); //$NON-NLS-1$
        map.put("short", new XSDDecimalType()); //$NON-NLS-1$
        map.put("unsignedInt", new XSDDecimalType()); //$NON-NLS-1$
        map.put("byte", new XSDDecimalType()); //$NON-NLS-1$
        map.put("unsignedShort", new XSDDecimalType()); //$NON-NLS-1$
        map.put("unsignedByte", new XSDDecimalType()); //$NON-NLS-1$
        return map;
    }
}
