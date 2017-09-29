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
package org.talend.dq.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.repository.model.IRepositoryFactory;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.RepositoryFactoryProvider;
import org.talend.dq.helper.ReportFileHelper.ReportListParameters;
import org.talend.repository.model.RepositoryConstants;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class ReportFileHelperTest {

    @Before
    public void setUp() throws Exception {
        IRepositoryFactory localRepository = RepositoryFactoryProvider
                .getRepositoriyById(RepositoryConstants.REPOSITORY_LOCAL_ID);
        ProxyRepositoryFactory.getInstance().setRepositoryFactoryFromProvider(localRepository);
    }

    @Test
    public void testSaveReportListFile() throws PersistenceException, URISyntaxException, IOException {
        URL fileUrl = this.getClass().getResource("/data/test.report.list"); //$NON-NLS-1$
        File file = new File(FileLocator.toFileURL(fileUrl).toURI().getPath().toString());
        assertTrue(file.exists());
        List<ReportListParameters> repList = ReportFileHelper.getReportListParameters(file);
        int size = repList.size();
        ReportListParameters reportListParameters = repList.get(size - 1);
        String insertFileName = reportListParameters.getName();
        String insertFilePath = reportListParameters.getPath();
        Long valueOf = Long.valueOf(reportListParameters.getCreateTime());
        long a = valueOf + 1l;
        String insertCreateTime = String.valueOf(a);

        // add new report file record
        ReportListParameters buildRepListParams = ReportFileHelper.buildRepListParams(insertFileName, insertFilePath,
                insertCreateTime);
        repList.add(buildRepListParams);
        // save report list
        ReportFileHelper.saveReportListFile(file, repList);

        List<ReportListParameters> repList2 = ReportFileHelper.getReportListParameters(file);
        assertEquals(size + 1, repList2.size());
        ReportListParameters insert = repList2.get(repList2.size() - 1);
        assertEquals(insertFileName, insert.getName());
        assertEquals(insertFilePath, insert.getPath());
        assertEquals(insertCreateTime, insert.getCreateTime());
    }

    @Test
    public void testGetReportListParameters() throws URISyntaxException, IOException {
        URL fileUrl = this.getClass().getResource("/data/test.report.list"); //$NON-NLS-1$
        File file = new File(FileLocator.toFileURL(fileUrl).toURI().getPath().toString());
        assertTrue(file.exists());
        List<ReportListParameters> reportListParameters = ReportFileHelper.getReportListParameters(file);
        ReportListParameters a0 = reportListParameters.get(0);
        assertEquals("asbbd-20150710-1713-00032", a0.getName()); //$NON-NLS-1$
        assertEquals("D:\\360Downloads\\HotFix\\asbbd-20150710-1713-00032.pdf", a0.getPath()); //$NON-NLS-1$
        assertEquals("1436519613043", a0.getCreateTime()); //$NON-NLS-1$

        ReportListParameters a1 = reportListParameters.get(1);
        assertEquals("asbbd-20150710-1715-00053", a1.getName()); //$NON-NLS-1$
        assertEquals("D:\\360Downloads\\HotFix\\asbbd-20150710-1715-00053.pdf", a1.getPath()); //$NON-NLS-1$
        assertEquals("1436519753917", a1.getCreateTime()); //$NON-NLS-1$

        ReportListParameters a2 = reportListParameters.get(2);
        assertEquals("asbbd-20150710-1719-00014", a2.getName()); //$NON-NLS-1$
        assertEquals("D:\\360Downloads\\HotFix\\asbbd-20150710-1719-00014.pdf", a2.getPath()); //$NON-NLS-1$
        assertEquals("1436519955281", a2.getCreateTime()); //$NON-NLS-1$

    }
}
