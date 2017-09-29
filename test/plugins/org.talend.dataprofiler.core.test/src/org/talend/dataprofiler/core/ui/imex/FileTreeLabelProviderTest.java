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
package org.talend.dataprofiler.core.ui.imex;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecord;
import org.talend.dataquality.properties.PropertiesFactory;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.RulesFactory;

/**
 * created by zshen on Mar 10, 2014 Detailled comment
 * 
 */
public class FileTreeLabelProviderTest {

    static TDQBusinessRuleItem createTDQSQLRuleItem = null;

    static TDQBusinessRuleItem createTDQParserRuleItem = null;

    static TDQMatchRuleItem createTDQMatchRuleItem = null;

    /**
     * DOC zshen Comment method "setUpBeforeClass".
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        DQRule createSqlRule = RulesFactory.eINSTANCE.createWhereRule();
        ParserRule createParserRule = RulesFactory.eINSTANCE.createParserRule();
        MatchRuleDefinition createMatchRuleDefinition = RulesFactory.eINSTANCE.createMatchRuleDefinition();

        createSqlRule.setName("sqlRule1"); //$NON-NLS-1$
        createParserRule.setName("parserRule1"); //$NON-NLS-1$
        createMatchRuleDefinition.setName("matchRule"); //$NON-NLS-1$
        createTDQSQLRuleItem = PropertiesFactory.eINSTANCE.createTDQBusinessRuleItem();
        createTDQSQLRuleItem.setDqrule(createSqlRule);
        createTDQParserRuleItem = PropertiesFactory.eINSTANCE.createTDQBusinessRuleItem();
        createTDQParserRuleItem.setDqrule(createParserRule);
        createTDQMatchRuleItem = PropertiesFactory.eINSTANCE.createTDQMatchRuleItem();
        createTDQMatchRuleItem.setMatchRule(createMatchRuleDefinition);

        Property createSQLRuleProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        createSQLRuleProperty.setItem(createTDQSQLRuleItem);
        createSQLRuleProperty.setLabel(createSqlRule.getName());
        Property createParserRuleProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        createParserRuleProperty.setItem(createTDQParserRuleItem);
        createParserRuleProperty.setLabel(createParserRule.getName());
        Property createMatchRuleProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        createMatchRuleProperty.setItem(createTDQMatchRuleItem);
        createMatchRuleProperty.setLabel(createMatchRuleDefinition.getName());

        ProxyRepositoryFactory.getInstance().create(createTDQSQLRuleItem, Path.EMPTY);

        ProxyRepositoryFactory.getInstance().create(createTDQParserRuleItem, Path.EMPTY);

        ProxyRepositoryFactory.getInstance().create(createTDQMatchRuleItem, Path.EMPTY);
    }

    /**
     * DOC zshen Comment method "tearDownAfterClass".
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC zshen Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC zshen Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dataprofiler.core.ui.imex.FileTreeLabelProvider#getImage(java.lang.Object)}.
     * case 1 :element is match rule
     */
    @Test
    public void testGetImageObjectCase1() {
        File file = ResourcesPlugin.getWorkspace().getRoot()
                .getFolder(new Path(createTDQMatchRuleItem.getMatchRule().eResource().getURI().toPlatformString(false)))
                .getLocation().toFile();
        ItemRecord itemRecord = new ItemRecord(file);
        FileTreeLabelProvider fileTreeLabelProvider = new FileTreeLabelProvider();
        Image image = fileTreeLabelProvider.getImage(itemRecord);
        Assert.assertEquals(ImageLib.getImage(ImageLib.MATCH_RULE_ICON), image);
    }

    /**
     * Test method for {@link org.talend.dataprofiler.core.ui.imex.FileTreeLabelProvider#getImage(java.lang.Object)}.
     * case 2 :element is parser rule
     */
    @Test
    public void testGetImageObjectCase2() {
        File file = ResourcesPlugin.getWorkspace().getRoot()
                .getFolder(new Path(createTDQParserRuleItem.getDqrule().eResource().getURI().toPlatformString(false)))
                .getLocation().toFile();
        ItemRecord itemRecord = new ItemRecord(file);
        FileTreeLabelProvider fileTreeLabelProvider = new FileTreeLabelProvider();
        Image image = fileTreeLabelProvider.getImage(itemRecord);
        Assert.assertEquals(ImageLib.getImage(ImageLib.DQ_RULE), image);
    }

    /**
     * Test method for {@link org.talend.dataprofiler.core.ui.imex.FileTreeLabelProvider#getImage(java.lang.Object)}.
     * case 2 :element is sql rule
     */
    @Test
    public void testGetImageObjectCase3() {
        File file = ResourcesPlugin.getWorkspace().getRoot()
                .getFolder(new Path(createTDQSQLRuleItem.getDqrule().eResource().getURI().toPlatformString(false))).getLocation()
                .toFile();
        ItemRecord itemRecord = new ItemRecord(file);
        FileTreeLabelProvider fileTreeLabelProvider = new FileTreeLabelProvider();
        Image image = fileTreeLabelProvider.getImage(itemRecord);
        Assert.assertEquals(ImageLib.getImage(ImageLib.DQ_RULE), image);
    }

}
