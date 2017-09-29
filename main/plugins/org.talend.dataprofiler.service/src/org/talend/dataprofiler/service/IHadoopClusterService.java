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
package org.talend.dataprofiler.service;

import java.util.Map;

/**
 * created by yyin on 2015年6月25日 Detailled comment
 *
 */
public interface IHadoopClusterService {

    boolean isBigData();

    String getHadoopClusterID(Object object);

    Object getHDFSType();

    Object getHadoopClusterType();

    void initConnectionParameters(Map<String, String> initMap, Object node);

    Object createHDFSWizard(Object workbench, boolean creation, Object node, String[] existingNames);

    Object createHadoopClusterWizard(Object workbench, boolean creation, Object node, String[] existingNames);

    Map<Object, Object> createHiveTable(Object hdfsNode);

    boolean hideAction(Object node);

    Object createActionOfHiveTable(Object node);

    Object createActionOfRetrieveHDFS(Object node);
}
