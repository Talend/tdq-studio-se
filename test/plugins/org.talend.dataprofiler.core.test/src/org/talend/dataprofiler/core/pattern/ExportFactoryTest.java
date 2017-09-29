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
package org.talend.dataprofiler.core.pattern;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.common.util.EList;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.IndicatorDefinitionParameter;
import org.talend.dq.helper.FileUtils;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;
import org.talend.utils.string.StringUtilities;

import com.talend.csv.CSVReader;

/**
 * Junit test case for the class org.talend.dataprofiler.core.pattern.ExportFactoryTest.
 */
public class ExportFactoryTest {

    private String getRandomUdiName() {
        return StringUtilities.getRandomString(8);
    }

    /**
     * DOC xqliu Comment method "setUpBeforeClass".
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // some code here
    }

    /**
     * DOC xqliu Comment method "tearDownAfterClass".
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // some code here
    }

    /**
     * DOC xqliu Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure();
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ExportFactory#export(java.io.File, org.eclipse.core.resources.IFolder, org.talend.dataquality.indicators.definition.IndicatorDefinition[])}
     * normal condition: exportFile is a file(not a folder).
     */
    @Test
    public void testExportFileIFolderIndicatorDefinitionArray() throws Exception {
        File exportFile = new File(
                System.getProperty("java.io.tmpdir") + File.separator + StringUtilities.getRandomString(8) + ".csv"); //$NON-NLS-1$ //$NON-NLS-2$
        IFolder udiFolder = ResourceManager.getUDIFolder();
        IndicatorDefinition indDef = createExportIndicaorDefinition(udiFolder, getRandomUdiName());
        ExportFactory.export(exportFile, udiFolder, indDef);

        assertTrue(exportFile.exists());
        assertTrue(exportFile.isFile());
        assertTrue(exportFile.length() > 0);

        CSVReader reader = FileUtils.createCSVReader(exportFile, FileUtils.TEXT_QUAL, FileUtils.TEXT_QUAL);
        reader.setSkipEmptyRecords(true);
        reader.readHeaders();

        boolean haveRecord = false;
        while (reader.readNext()) {
            haveRecord = true;
            String name = reader.get(PatternToExcelEnum.Label.getLiteral());
            assertEquals(name, indDef.getName());

            String paraString = reader.get(PatternToExcelEnum.IndicatorDefinitionParameter.getLiteral());
            EList<IndicatorDefinitionParameter> indDefParas = indDef.getIndicatorDefinitionParameter();
            for (IndicatorDefinitionParameter indDefPara : indDefParas) {
                String temp = indDefPara.getKey() + UDIHelper.PARA_SEPARATE_1 + indDefPara.getValue() + UDIHelper.PARA_SEPARATE_2;
                assertTrue(paraString.indexOf(temp) > -1);
            }
        }
        reader.close();
        assertTrue(haveRecord);
        exportFile.delete();
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ExportFactory#export(java.io.File, org.eclipse.core.resources.IFolder, org.talend.dataquality.indicators.definition.IndicatorDefinition[])}
     * normal condition: exportFile is a folder(not a file).
     */
    @Test
    public void testExportFolderIFolderIndicatorDefinitionArray() throws Exception {
        String udiName = getRandomUdiName();
        File exportFolder = new File(System.getProperty("java.io.tmpdir") + File.separator + StringUtilities.getRandomString(8)); //$NON-NLS-1$
        if (!exportFolder.exists()) {
            exportFolder.mkdirs();
        }
        IFolder udiFolder = ResourceManager.getUDIFolder();
        IndicatorDefinition indDef = createExportIndicaorDefinition(udiFolder, udiName);
        ExportFactory.export(exportFolder, udiFolder, indDef);

        File exportFile = new File(exportFolder.getAbsolutePath() + File.separator + udiName + ".csv"); //$NON-NLS-1$
        assertTrue(exportFile.exists());
        assertTrue(exportFile.isFile());
        assertTrue(exportFile.length() > 0);

        CSVReader reader = FileUtils.createCSVReader(exportFile, FileUtils.TEXT_QUAL, FileUtils.TEXT_QUAL);
        reader.setSkipEmptyRecords(true);
        reader.readHeaders();

        boolean haveRecord = false;
        while (reader.readNext()) {
            haveRecord = true;
            String name = reader.get(PatternToExcelEnum.Label.getLiteral());
            assertEquals(name, indDef.getName());

            String paraString = reader.get(PatternToExcelEnum.IndicatorDefinitionParameter.getLiteral());
            EList<IndicatorDefinitionParameter> indDefParas = indDef.getIndicatorDefinitionParameter();
            for (IndicatorDefinitionParameter indDefPara : indDefParas) {
                String temp = indDefPara.getKey() + UDIHelper.PARA_SEPARATE_1 + indDefPara.getValue() + UDIHelper.PARA_SEPARATE_2;
                assertTrue(paraString.indexOf(temp) > -1);
            }
        }
        reader.close();
        assertTrue(haveRecord);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ExportFactory#export(java.io.File, org.eclipse.core.resources.IFolder, org.talend.dataquality.indicators.definition.IndicatorDefinition[])}
     * non-normal condition: exportFile is a folder and this folder does not exist.
     */
    @Test
    public void testExportFolderIFolderIndicatorDefinitionArrayNonNormalFolderNotExist() throws Exception {
        String udiName = getRandomUdiName();
        File exportFolder = new File(File.separator + StringUtilities.getRandomString(8));
        IFolder udiFolder = ResourceManager.getUDIFolder();
        IndicatorDefinition indDef = createExportIndicaorDefinition(udiFolder, udiName);
        ExportFactory.export(exportFolder, udiFolder, indDef);

        File exportFile = new File(exportFolder.getAbsolutePath() + File.separator + udiName + ".csv"); //$NON-NLS-1$
        assertFalse(exportFile.exists());
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ExportFactory#export(java.io.File, org.eclipse.core.resources.IFolder, org.talend.dataquality.indicators.definition.IndicatorDefinition[])}
     * non-normal condition: exportFile is a file, but the extension is not csv.
     */
    @Test
    public void testExportFileIFolderIndicatorDefinitionArrayextEnsionIsNotCsv() throws Exception {
        File exportFile = new File(
                System.getProperty("java.io.tmpdir") + File.separator + StringUtilities.getRandomString(8) + ".abc"); //$NON-NLS-1$ //$NON-NLS-2$
        IFolder udiFolder = ResourceManager.getUDIFolder();
        IndicatorDefinition indDef = createExportIndicaorDefinition(udiFolder, getRandomUdiName());
        ExportFactory.export(exportFile, udiFolder, indDef);

        assertFalse(exportFile.exists());

    }

    /**
     * create the IndicaorDefinition for test.
     * 
     * @return
     */
    private IndicatorDefinition createExportIndicaorDefinition(IFolder folder, String udiName) {
        IndicatorDefinition indDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        indDef.setName(udiName);

        Map<String, String> indDefParaMap = getIndDefParaMap();
        for (String key : indDefParaMap.keySet()) {
            IndicatorDefinitionParameter indDefPara = DefinitionFactory.eINSTANCE.createIndicatorDefinitionParameter();
            indDefPara.setKey(key);
            indDefPara.setValue(indDefParaMap.get(key));
            indDef.getIndicatorDefinitionParameter().add(indDefPara);
        }

        UDIHelper.setUDICategory(indDef, DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory());
        ElementWriterFactory.getInstance().createIndicatorDefinitionWriter().create(indDef, folder);
        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        return indDef;
    }

    /**
     * get the IndicatorDefinitionParameter's key value map for test.
     * 
     * @return
     */
    private Map<String, String> getIndDefParaMap() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("name1", "value1"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put("int", "100"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put("email", "a@b.cn; x@y.zn"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put("key", " `1234567890-=~!@#$%^&*()_+[]\\{}|;':\",./<>?qwertyuiopasdfghjklzxcvbnmMNBVCXZLKJHGFDSAPOIUYTREWQ   "); //$NON-NLS-1$ //$NON-NLS-2$

        return map;
    }
}
