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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataquality.reports.ReportsFactory;
import org.talend.dataquality.reports.TdReport;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by xqliu on Aug 6, 2013 Detailled comment
 * 
 */
public class ContextHelperTest {

    /**
     * Test method for {@link org.talend.dq.helper.ContextHelper#isContextVar(java.lang.String)}.
     */
    @Test
    public void testIsContextVar() {
        String varName = null;
        Assert.assertFalse(ContextHelper.isContextVar(varName));

        varName = ""; //$NON-NLS-1$
        Assert.assertFalse(ContextHelper.isContextVar(varName));

        varName = "        "; //$NON-NLS-1$
        Assert.assertFalse(ContextHelper.isContextVar(varName));

        varName = "varName"; //$NON-NLS-1$
        Assert.assertFalse(ContextHelper.isContextVar(varName));

        varName = "context.varName"; //$NON-NLS-1$
        Assert.assertTrue(ContextHelper.isContextVar(varName));
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.ContextHelper#getContextValue(java.util.List, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetContextValue() {
        List<ContextType> contexts = new ArrayList<ContextType>();

        ContextType ct = TalendFileFactory.eINSTANCE.createContextType();
        ct.setName("default"); //$NON-NLS-1$

        ContextParameterType cpt = TalendFileFactory.eINSTANCE.createContextParameterType();
        cpt.setName("abc"); //$NON-NLS-1$
        cpt.setValue("123"); //$NON-NLS-1$

        ct.getContextParameter().add(cpt);
        contexts.add(ct);

        String defaultContextName = "default"; //$NON-NLS-1$
        String str = "context.abc"; //$NON-NLS-1$
        Assert.assertEquals("123", ContextHelper.getContextValue(contexts, defaultContextName, str)); //$NON-NLS-1$

        str = "context.xyz"; //$NON-NLS-1$
        Assert.assertEquals(str, ContextHelper.getContextValue(contexts, defaultContextName, str));

        str = "realValue"; //$NON-NLS-1$
        Assert.assertEquals(str, ContextHelper.getContextValue(contexts, defaultContextName, str));
    }

    /**
     * Test method for {@link org.talend.dq.helper.ContextHelper#getOutputFolderFromReports(java.util.List)}.
     */
    @Test
    public void testGetOutputFolderFromReports() {
        String empty = ""; //$NON-NLS-1$
        String blank = "      "; //$NON-NLS-1$
        String defaultStr = "default"; //$NON-NLS-1$
        String var1 = "context.outputFolder"; //$NON-NLS-1$
        String var1a = "outputFolder"; //$NON-NLS-1$
        String var2 = "context.of2"; //$NON-NLS-1$
        String var2a = "of2"; //$NON-NLS-1$
        String folder1 = "/home/user/talend/a"; //$NON-NLS-1$
        String folder2 = "/home/user/talend/b"; //$NON-NLS-1$

        Assert.assertNull(ContextHelper.getOutputFolderFromReports(null));
        Assert.assertNull(ContextHelper.getOutputFolderFromReports(new ArrayList<TdReport>()));

        List<TdReport> reports = new ArrayList<TdReport>();

        TdReport tdReport = ReportsFactory.eINSTANCE.createTdReport();
        tdReport.setDefaultContext(defaultStr);

        TaggedValue tv = CoreFactory.eINSTANCE.createTaggedValue();
        tv.setTag(TaggedValueHelper.OUTPUT_FOLDER_TAG);
        tv.setValue(empty);

        tdReport.getTaggedValue().add(tv);
        reports.add(tdReport);

        Assert.assertNull(ContextHelper.getOutputFolderFromReports(reports));

        tv.setValue(blank);
        Assert.assertNull(ContextHelper.getOutputFolderFromReports(reports));

        tv.setValue(folder1);
        Assert.assertEquals(folder1, ContextHelper.getOutputFolderFromReports(reports));

        tv.setValue(var1);
        Assert.assertEquals(var1, ContextHelper.getOutputFolderFromReports(reports));

        TdReport tdReport2 = ReportsFactory.eINSTANCE.createTdReport();
        tdReport2.setDefaultContext(defaultStr);

        TaggedValue tv2 = CoreFactory.eINSTANCE.createTaggedValue();
        tv2.setTag(TaggedValueHelper.OUTPUT_FOLDER_TAG);
        tv2.setValue(empty);

        tdReport2.getTaggedValue().add(tv2);
        reports.add(tdReport2);

        ContextType ct = TalendFileFactory.eINSTANCE.createContextType();
        ct.setName(defaultStr);
        ContextParameterType cpt = TalendFileFactory.eINSTANCE.createContextParameterType();
        cpt.setName(var1a);
        cpt.setValue(folder1);
        ct.getContextParameter().add(cpt);
        tdReport.getContext().add(ct);

        ContextType ct2 = TalendFileFactory.eINSTANCE.createContextType();
        ct2.setName(defaultStr);
        ContextParameterType cpt2 = TalendFileFactory.eINSTANCE.createContextParameterType();
        cpt2.setName(var2a);
        cpt2.setValue(folder2);
        ct2.getContextParameter().add(cpt2);
        tdReport2.getContext().add(ct2);

        // both context, different output folder
        tv.setValue(var1);
        tv2.setValue(var2);
        Assert.assertNull(ContextHelper.getOutputFolderFromReports(reports));

        // both context, same output folder
        cpt2.setValue(folder1);
        ct2.getContextParameter().clear();
        ct2.getContextParameter().add(cpt2);
        tdReport2.getContext().clear();
        tdReport2.getContext().add(ct2);
        String temp = ContextHelper.getOutputFolderFromReports(reports);
        Assert.assertTrue(var1.equals(temp) || var2.equals(temp));

        // both real folder, different output folder
        tv.setValue(folder1);
        tdReport.getTaggedValue().clear();
        tdReport.getTaggedValue().add(tv);
        tv2.setValue(folder2);
        tdReport2.getTaggedValue().clear();
        tdReport2.getTaggedValue().add(tv2);
        Assert.assertNull(ContextHelper.getOutputFolderFromReports(reports));

        // both real folder, same output folder
        tv2.setValue(folder1);
        tdReport2.getTaggedValue().clear();
        tdReport2.getTaggedValue().add(tv2);
        Assert.assertEquals(folder1, ContextHelper.getOutputFolderFromReports(reports));

        // context and real folder, different output folder
        tv2.setValue(var2);
        tdReport2.getTaggedValue().clear();
        tdReport2.getTaggedValue().add(tv2);
        cpt2.setValue(folder2);
        ct2.getContextParameter().clear();
        ct2.getContextParameter().add(cpt2);
        tdReport2.getContext().clear();
        tdReport2.getContext().add(ct2);
        Assert.assertNull(ContextHelper.getOutputFolderFromReports(reports));

        // context and real folder, same output folder
        cpt2.setValue(folder1);
        ct2.getContextParameter().clear();
        ct2.getContextParameter().add(cpt2);
        tdReport2.getContext().clear();
        tdReport2.getContext().add(ct2);
        Assert.assertEquals(folder1, ContextHelper.getOutputFolderFromReports(reports));

        // one report's output folder is empty(mean default location)
        tv2.setValue(empty);
        tdReport2.getTaggedValue().clear();
        tdReport2.getTaggedValue().add(tv2);
        Assert.assertNull(ContextHelper.getOutputFolderFromReports(reports));

        // one report's output folder is blank(mean default location)
        tv2.setValue(blank);
        tdReport2.getTaggedValue().clear();
        tdReport2.getTaggedValue().add(tv2);
        Assert.assertNull(ContextHelper.getOutputFolderFromReports(reports));
    }
}
