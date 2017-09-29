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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.common.util.EList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.dataprofiler.core.ui.action.actions.ImportObject;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.IndicatorDefinitionParameter;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.string.StringUtilities;
import org.talend.utils.sugars.ReturnCode;

/**
 * Junit test case for the class org.talend.dataprofiler.core.pattern.ImportFactory.
 */
public class ImportFactoryTest {

    private static final String UDI_NAME = "UDI1"; //$NON-NLS-1$

    private static final String IND_DEF_PARA_STRING = "email__PARA_SEP_1__a@b.cn; x@y.zn__PARA_SEP_2__int__PARA_SEP_1__100__PARA_SEP_2__name1__PARA_SEP_1__value1__PARA_SEP_2__key__PARA_SEP_1__ `1234567890-=~!@#$%^&*()_+[]{}|;':,./<>?qwertyuiopasdfghjklzxcvbnmMNBVCXZLKJHGFDSAPOIUYTREWQ __PARA_SEP_2__"; //$NON-NLS-1$

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
     * DOC xqliu Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        // some code here
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ImportFactory#importParserRuleToStucture(org.talend.dataprofiler.core.ui.action.actions.ImportObject, org.eclipse.core.resources.IFolder, boolean, boolean, java.lang.String)}
     * None Empty With Double Quote.
     */
    @Test
    public void testImportParserRuleToStuctureNoneEmptyWithDoubleQuote() throws IOException {
        // the parser rule name, use it to get the imported file name
        String parserRuleName = "parserRule"; //$NON-NLS-1$
        String parserRuleFileName = parserRuleName + "_0.1.rules"; //$NON-NLS-1$

        // the expect expression's values: name and body will add double quote after import, lang will not add double
        // quote after import
        String name = "\"a\""; //$NON-NLS-1$
        String lang = "Enumeration"; //$NON-NLS-1$
        String body = "\"'a'|'b'\""; //$NON-NLS-1$

        File dict = new File(""); //$NON-NLS-1$
        File importFile = new File(dict.getAbsolutePath() + File.separator + "paser_rule.csv"); //$NON-NLS-1$
        if (importFile.exists()) {
            importFile.delete();
        }
        importFile.createNewFile();
        // set the csv file's head
        String content = "\"Label\"\t\"Purpose\"\t\"Description\"\t\"Author\"\t\"Relative_Path\"\t\"Name\"\t\"Type\"\t\"Value\"\n"; //$NON-NLS-1$
        // set the csv file's content: the clumn's value is none empty with double quote
        content += "\"" + parserRuleName //$NON-NLS-1$
                + "\"\t\"\"\t\"\"\t\"trunk@talend.com\"\t\"\"\t\"\\\"a\\\"\"\t\"\\\"Enumeration\\\"\"\t\"\\\"'a'|'b'\\\"\""; //$NON-NLS-1$
        BufferedWriter output = new BufferedWriter(new FileWriter(importFile));
        output.write(content);
        output.close();

        List<File> pJarfilesMock = new ArrayList<File>();
        ImportObject importObject = ImportObject.createImportObject(importFile, pJarfilesMock);

        IFolder parserRuleFolder = ResourceManager.getOneFolder(EResourceConstant.RULES_PARSER);

        boolean skip = false;
        boolean rename = true;

        String importItemName = "importItemName"; //$NON-NLS-1$

        List<ReturnCode> importParserRuleToStucture = ImportFactory.importParserRuleToStucture(importObject, parserRuleFolder,
                skip, rename, importItemName);

        for (ReturnCode rc : importParserRuleToStucture) {
            assertTrue(rc.isOk());
        }

        // the imported parser rule's name is a
        IFile parserRuleFile = parserRuleFolder.getFile(parserRuleFileName);
        ParserRule parserRule = (ParserRule) DQRuleResourceFileHelper.getInstance().findDQRule(parserRuleFile);
        List<TdExpression> expressions = parserRule.getExpression();
        for (TdExpression expression : expressions) {
            assertEquals(name, expression.getName());
            assertEquals(lang, expression.getLanguage());
            assertEquals(body, expression.getBody());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ImportFactory#importParserRuleToStucture(org.talend.dataprofiler.core.ui.action.actions.ImportObject, org.eclipse.core.resources.IFolder, boolean, boolean, java.lang.String)}
     * None Empty Without Double Quote.
     */
    @Test
    public void testImportParserRuleToStuctureNoneEmptyWithoutDoubleQuote() throws IOException {
        // the parser rule name, use it to get the imported file name
        String parserRuleName = "parserRule"; //$NON-NLS-1$
        String parserRuleFileName = parserRuleName + "_0.1.rules"; //$NON-NLS-1$

        // the expect expression's values: name and body will add double quote after import, lang will not add double
        // quote after import
        String name = "\"a\""; //$NON-NLS-1$
        String lang = "Enumeration"; //$NON-NLS-1$
        String body = "\"'a'|'b'\""; //$NON-NLS-1$

        File dict = new File(""); //$NON-NLS-1$
        File importFile = new File(dict.getAbsolutePath() + File.separator + "paser_rule.csv"); //$NON-NLS-1$
        if (importFile.exists()) {
            importFile.delete();
        }
        importFile.createNewFile();
        // set the csv file's head
        String content = "\"Label\"\t\"Purpose\"\t\"Description\"\t\"Author\"\t\"Relative_Path\"\t\"Name\"\t\"Type\"\t\"Value\"\n"; //$NON-NLS-1$
        // set the csv file's content: the clumn's value is none empty without double quote
        content += "\"" + parserRuleName //$NON-NLS-1$
                + "\"\t\"\"\t\"\"\t\"trunk@talend.com\"\t\"\"\t\"a\"\t\"Enumeration\"\t\"'a'|'b'\""; //$NON-NLS-1$
        BufferedWriter output = new BufferedWriter(new FileWriter(importFile));
        output.write(content);
        output.close();

        List<File> pJarfilesMock = new ArrayList<File>();
        ImportObject importObject = ImportObject.createImportObject(importFile, pJarfilesMock);

        IFolder parserRuleFolder = ResourceManager.getOneFolder(EResourceConstant.RULES_PARSER);

        boolean skip = false;
        boolean rename = true;

        String importItemName = "importItemName"; //$NON-NLS-1$

        List<ReturnCode> importParserRuleToStucture = ImportFactory.importParserRuleToStucture(importObject, parserRuleFolder,
                skip, rename, importItemName);

        for (ReturnCode rc : importParserRuleToStucture) {
            assertTrue(rc.isOk());
        }

        // the imported parser rule's name is a
        IFile parserRuleFile = parserRuleFolder.getFile(parserRuleFileName);
        ParserRule parserRule = (ParserRule) DQRuleResourceFileHelper.getInstance().findDQRule(parserRuleFile);
        List<TdExpression> expressions = parserRule.getExpression();
        for (TdExpression expression : expressions) {
            assertEquals(name, expression.getName());
            assertEquals(lang, expression.getLanguage());
            assertEquals(body, expression.getBody());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ImportFactory#importParserRuleToStucture(org.talend.dataprofiler.core.ui.action.actions.ImportObject, org.eclipse.core.resources.IFolder, boolean, boolean, java.lang.String)}
     * Empty With Double Quote.
     */
    @Test
    public void testImportParserRuleToStuctureEmptyWithDoubleQuote() throws IOException {
        // the parser rule name, use it to get the imported file name
        String parserRuleName = "parserRule1"; //$NON-NLS-1$
        String parserRuleFileName = parserRuleName + "_0.1.rules"; //$NON-NLS-1$

        // the expect expression's values: name and body will add double quote after import, lang will remove any double
        // quote after import(why???)
        String name = "\"\"\"\""; //$NON-NLS-1$
        String lang = ""; //$NON-NLS-1$
        String body = "\"\"\"\""; //$NON-NLS-1$

        File dict = new File(""); //$NON-NLS-1$
        File importFile = new File(dict.getAbsolutePath() + File.separator + "paser_rule.csv"); //$NON-NLS-1$
        if (importFile.exists()) {
            importFile.delete();
        }
        importFile.createNewFile();
        // set the csv file's head
        String content = "\"Label\"\t\"Purpose\"\t\"Description\"\t\"Author\"\t\"Relative_Path\"\t\"Name\"\t\"Type\"\t\"Value\"\n"; //$NON-NLS-1$
        // set the csv file's content: the clumn's value is empty with double quote
        content += "\"" + parserRuleName //$NON-NLS-1$
                + "\"\t\"\"\t\"\"\t\"trunk@talend.com\"\t\"\"\t\"\\\"\\\"\"\t\"\\\"\\\"\"\t\"\\\"\\\"\""; //$NON-NLS-1$
        BufferedWriter output = new BufferedWriter(new FileWriter(importFile));
        output.write(content);
        output.close();

        List<File> pJarfilesMock = new ArrayList<File>();
        ImportObject importObject = ImportObject.createImportObject(importFile, pJarfilesMock);

        IFolder parserRuleFolder = ResourceManager.getOneFolder(EResourceConstant.RULES_PARSER);

        boolean skip = false;
        boolean rename = true;

        String importItemName = "importItemName"; //$NON-NLS-1$

        List<ReturnCode> importParserRuleToStucture = ImportFactory.importParserRuleToStucture(importObject, parserRuleFolder,
                skip, rename, importItemName);

        for (ReturnCode rc : importParserRuleToStucture) {
            assertTrue(rc.isOk());
        }

        // the imported parser rule's name is a
        IFile parserRuleFile = parserRuleFolder.getFile(parserRuleFileName);
        ParserRule parserRule = (ParserRule) DQRuleResourceFileHelper.getInstance().findDQRule(parserRuleFile);
        List<TdExpression> expressions = parserRule.getExpression();
        for (TdExpression expression : expressions) {
            assertEquals(name, expression.getName());
            assertEquals(lang, expression.getLanguage());
            assertEquals(body, expression.getBody());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ImportFactory#importParserRuleToStucture(org.talend.dataprofiler.core.ui.action.actions.ImportObject, org.eclipse.core.resources.IFolder, boolean, boolean, java.lang.String)}
     * Empty Without Double Quote.
     */
    @Test
    public void testImportParserRuleToStuctureEmptyWithoutDoubleQuote() throws IOException {
        // the parser rule name, use it to get the imported file name
        String parserRuleName = "parserRule2"; //$NON-NLS-1$
        String parserRuleFileName = parserRuleName + "_0.1.rules"; //$NON-NLS-1$

        // the expect expression's values: name and body will add double quote after import, lang will not add double
        // quote after import
        String name = "\"\""; //$NON-NLS-1$
        String lang = ""; //$NON-NLS-1$
        String body = "\"\""; //$NON-NLS-1$

        File dict = new File(""); //$NON-NLS-1$
        File importFile = new File(dict.getAbsolutePath() + File.separator + "paser_rule.csv"); //$NON-NLS-1$
        if (importFile.exists()) {
            importFile.delete();
        }
        importFile.createNewFile();
        // set the csv file's head
        String content = "\"Label\"\t\"Purpose\"\t\"Description\"\t\"Author\"\t\"Relative_Path\"\t\"Name\"\t\"Type\"\t\"Value\"\n"; //$NON-NLS-1$
        // set the csv file's content: the clumn's value is empty without double quote
        content += "\"" + parserRuleName //$NON-NLS-1$
                + "\"\t\"\"\t\"\"\t\"trunk@talend.com\"\t\"\"\t\"\"\t\"\"\t\"\""; //$NON-NLS-1$
        BufferedWriter output = new BufferedWriter(new FileWriter(importFile));
        output.write(content);
        output.close();

        List<File> pJarfilesMock = new ArrayList<File>();
        ImportObject importObject = ImportObject.createImportObject(importFile, pJarfilesMock);

        IFolder parserRuleFolder = ResourceManager.getOneFolder(EResourceConstant.RULES_PARSER);

        boolean skip = false;
        boolean rename = true;

        String importItemName = "importItemName"; //$NON-NLS-1$

        List<ReturnCode> importParserRuleToStucture = ImportFactory.importParserRuleToStucture(importObject, parserRuleFolder,
                skip, rename, importItemName);

        for (ReturnCode rc : importParserRuleToStucture) {
            assertTrue(rc.isOk());
        }

        // the imported parser rule's name is a
        IFile parserRuleFile = parserRuleFolder.getFile(parserRuleFileName);
        ParserRule parserRule = (ParserRule) DQRuleResourceFileHelper.getInstance().findDQRule(parserRuleFile);
        List<TdExpression> expressions = parserRule.getExpression();
        for (TdExpression expression : expressions) {
            assertTrue(name.equals(expression.getName()));
            assertTrue(lang.equals(expression.getLanguage()));
            assertTrue(body.equals(expression.getBody()));
        }
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ImportFactory#importIndicatorToStucture(org.talend.dataprofiler.core.ui.action.actions.ImportObject, org.eclipse.core.resources.IFolder, boolean, boolean, java.lang.String)}
     * normal condition: the import file's extension is csv.
     */
    @Test
    public void testImportIndicatorToStucture() throws Exception {
        File importFile = createImportFile(UDI_NAME, StringUtilities.getRandomString(8) + ".csv"); //$NON-NLS-1$
        assertTrue(importFile.exists());
        assertTrue(importFile.isFile());
        assertTrue(importFile.length() > 0);
        List<File> pJarfiles = new ArrayList<File>();
        ImportObject importObject = ImportObject.createImportObject(importFile, pJarfiles);

        IFolder udiFolder = ResourceManager.getUDIFolder();

        boolean skip = false;
        boolean rename = true;

        List<ReturnCode> rc = ImportFactory.importIndicatorToStucture(importObject, udiFolder, skip, rename, UDI_NAME);
        assertTrue(rc.size() == 1);
        assertTrue(rc.get(0).isOk());

        IndicatorDefinition indicatorDefinition = null;
        RootContainer<String, IRepositoryViewObject> tdqViewObjects = ProxyRepositoryFactory.getInstance()
                .getTdqRepositoryViewObjects(ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS,
                        ERepositoryObjectType.getFolderName(ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS));
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            IndicatorDefinition indDefTemp = ((TDQIndicatorDefinitionItem) viewObject.getProperty().getItem())
                    .getIndicatorDefinition();
            if (UDI_NAME.equals(indDefTemp.getName())) {
                indicatorDefinition = indDefTemp;
                break;
            }
        }
        assertNotNull(indicatorDefinition);

        if (indicatorDefinition != null) {
            EList<IndicatorDefinitionParameter> indDefParas = indicatorDefinition.getIndicatorDefinitionParameter();
            assertFalse(indDefParas.isEmpty());
            //String paraString = StringUtils.replace(IND_DEF_PARA_STRING, "\"\"", "\""); //$NON-NLS-1$ //$NON-NLS-2$
            for (IndicatorDefinitionParameter indDefPara : indDefParas) {
                String temp = indDefPara.getKey() + UDIHelper.PARA_SEPARATE_1 + indDefPara.getValue() + UDIHelper.PARA_SEPARATE_2;
                assertTrue(IND_DEF_PARA_STRING.indexOf(temp) > -1);
            }
        }
        importFile.delete();
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ImportFactory#importIndicatorToStucture(org.talend.dataprofiler.core.ui.action.actions.ImportObject, org.eclipse.core.resources.IFolder, boolean, boolean, java.lang.String)}
     * non-normal condition: the import file's extension is not csv.
     */
    @Test
    public void testImportIndicatorToStuctureImportFileExtensionIsNotCsv() throws Exception {
        File importFile = createImportFile(UDI_NAME, StringUtilities.getRandomString(8) + ".nonCsv"); //$NON-NLS-1$
        List<File> pJarfiles = new ArrayList<File>();
        ImportObject importObject = ImportObject.createImportObject(importFile, pJarfiles);

        IFolder udiFolder = ResourceManager.getUDIFolder();

        boolean skip = false;
        boolean rename = true;

        List<ReturnCode> rc = ImportFactory.importIndicatorToStucture(importObject, udiFolder, skip, rename, UDI_NAME);
        assertTrue(rc.size() == 1);
        assertFalse(rc.get(0).isOk());
        importFile.delete();
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ImportFactory#importIndicatorToStucture(org.talend.dataprofiler.core.ui.action.actions.ImportObject, org.eclipse.core.resources.IFolder, boolean, boolean, java.lang.String)}
     * non-normal condition: the import file is not exist.
     */
    @Test
    public void testImportIndicatorToStuctureImportFileIsNotExist() throws Exception {
        File importFile = new File(File.separator + StringUtilities.getRandomString(8) + ".csv"); //$NON-NLS-1$
        List<File> pJarfiles = new ArrayList<File>();
        ImportObject importObject = ImportObject.createImportObject(importFile, pJarfiles);

        IFolder udiFolder = ResourceManager.getUDIFolder();

        boolean skip = false;
        boolean rename = true;

        List<ReturnCode> rc = ImportFactory.importIndicatorToStucture(importObject, udiFolder, skip, rename, UDI_NAME);
        assertTrue(rc.size() == 1);
        assertFalse(rc.get(0).isOk());
        importFile.delete();
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.pattern.ImportFactory#importIndicatorToStucture(org.talend.dataprofiler.core.ui.action.actions.ImportObject, org.eclipse.core.resources.IFolder, boolean, boolean, java.lang.String)}
     * non-normal condition: the import file is empty.
     */
    @Test
    public void testImportIndicatorToStuctureImportFileIsEmpty() throws Exception {
        File importFile = new File(
                System.getProperty("java.io.tmpdir") + File.separator + StringUtilities.getRandomString(8) + ".csv"); //$NON-NLS-1$ //$NON-NLS-2$
        if (!importFile.exists()) {
            if (!importFile.getParentFile().exists()) {
                importFile.getParentFile().mkdirs();
            }
            importFile.createNewFile();
        }
        List<File> pJarfiles = new ArrayList<File>();
        ImportObject importObject = ImportObject.createImportObject(importFile, pJarfiles);

        IFolder udiFolder = ResourceManager.getUDIFolder();

        boolean skip = false;
        boolean rename = true;

        List<ReturnCode> rc = ImportFactory.importIndicatorToStucture(importObject, udiFolder, skip, rename, UDI_NAME);
        assertTrue(rc.size() == 1);
        assertFalse(rc.get(0).isOk());
        importFile.delete();
    }

    /**
     * create the import file for test.
     * 
     * @param udiName
     * @return
     */
    private File createImportFile(String udiName, String fileName) throws Exception {
        String header = "\"Label\"\t\"Purpose\"\t\"Description\"\t\"Author\"\t\"Relative_Path\"\t\"All_DB_Regexp\"\t\"DB2_Regexp\"\t\"MySQL_Regexp\"\t\"Oracle_Regexp\"\t\"PostgreSQL_Regexp\"\t\"SQL_Server_Regexp\"\t\"Sybase_Regexp\"\t\"Ingres_Regexp\"\t\"Informix_Regexp\"\t\"MDM_Informix_Regexp\"\t\"SQLite3_Regexp\"\t\"Teradata_Regexp\"\t\"Java_Regexp\"\t\"Category\"\t\"Access\"\t\"AS400\"\t\"CLASS_NAME_TEXT\"\t\"JAR_FILE_PATH\"\t\"Hive\"\t\"IndicatorDefinitionParameter\""; //$NON-NLS-1$
        String record = "\"" + UDI_NAME + "\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"User Defined Count\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"\"\t\"" + IND_DEF_PARA_STRING + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName); //$NON-NLS-1$
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        if (file.exists()) {
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(header);
            pw.println(record);
            pw.close();
        }
        return file;
    }
}
