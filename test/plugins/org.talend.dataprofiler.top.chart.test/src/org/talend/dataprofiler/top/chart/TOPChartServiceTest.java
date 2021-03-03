// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.top.chart;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.dataprofiler.chart.TOPChartService;
import org.talend.dataprofiler.chart.util.EncapsulationCumstomerDataset;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dq.indicators.preview.table.ChartDataEntity;


public class TOPChartServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dataprofiler.chart.TOPChartService#clearDataset(java.lang.Object)}.
     */
    @Test
    public void testClearDatasetForEncapsulationCumstomerDatasetCase() {
        CustomerDefaultCategoryDataset customerDefaultCategoryDataset = new CustomerDefaultCategoryDataset();
        customerDefaultCategoryDataset.addValue(100, "1", "id"); //$NON-NLS-1$ //$NON-NLS-2$
        ChartDataEntity chartDataEntity = new ChartDataEntity();
        customerDefaultCategoryDataset.addDataEntity(chartDataEntity);
        
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        defaultCategoryDataset.addValue(100, "1", "id"); //$NON-NLS-1$ //$NON-NLS-2$
        SlidingCategoryDataset slidingCategoryDataset = new SlidingCategoryDataset(defaultCategoryDataset, 0, 1);
        EncapsulationCumstomerDataset encapsulationCumstomerDataset =
                new EncapsulationCumstomerDataset(slidingCategoryDataset, customerDefaultCategoryDataset);
        TOPChartService topService = new TOPChartService();
        topService.clearDataset(encapsulationCumstomerDataset);

        Assert
                .assertEquals("The count of defaultCategoryDataset should be clear", 0, //$NON-NLS-1$
                        defaultCategoryDataset.getRowCount());
        Assert
                .assertEquals("The count of customerDefaultCategoryDataset value should be clear", 0, //$NON-NLS-1$
                        ((DefaultCategoryDataset)customerDefaultCategoryDataset.getDataset()).getRowCount());
        Assert
                .assertEquals("The length of customerDefaultCategoryDataset entity should be clear", 0, //$NON-NLS-1$
                        customerDefaultCategoryDataset.getDataEntities().length);
    }

    /**
     * Test method for {@link org.talend.dataprofiler.chart.TOPChartService#clearDataset(java.lang.Object)}.
     */
    @Test
    public void testClearDatasetForDefaultCategoryDatasetCase() {

        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        defaultCategoryDataset.addValue(100, "1", "id"); //$NON-NLS-1$ //$NON-NLS-2$
        TOPChartService topService = new TOPChartService();
        topService.clearDataset(defaultCategoryDataset);
        Assert.assertEquals("The count of dataset should be clear", 0, defaultCategoryDataset.getRowCount()); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dataprofiler.chart.TOPChartService#clearDataset(java.lang.Object)}.
     */
    @Test
    public void testClearDatasetForExceptionCase() {
        try {
            CustomerDefaultCategoryDataset customerDefaultCategoryDataset = new CustomerDefaultCategoryDataset();
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
            EncapsulationCumstomerDataset encapsulationCumstomerDataset =
                    new EncapsulationCumstomerDataset(defaultCategoryDataset, customerDefaultCategoryDataset);
        TOPChartService topService = new TOPChartService();
            topService.clearDataset(encapsulationCumstomerDataset);
            Assert.assertTrue("No any exception throw", true); //$NON-NLS-1$
        } catch (Exception e) {
            Assert.assertFalse("The exception should not throw", true); //$NON-NLS-1$
        }
    }

}
