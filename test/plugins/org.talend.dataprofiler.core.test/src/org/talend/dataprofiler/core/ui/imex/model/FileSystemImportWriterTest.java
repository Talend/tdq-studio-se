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
package org.talend.dataprofiler.core.ui.imex.model;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.*;
import static org.powermock.api.support.membermodification.MemberModifier.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import junit.framework.Assert;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.domain.pattern.PatternPackage;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.PropertiesFactory;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.dq.writer.impl.IndicatorDefinitionWriter;
import org.talend.dq.writer.impl.PatternWriter;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by yyin on 2012-8-8 Detailled comment
 * 
 */
@PrepareForTest({ IndicatorResourceFileHelper.class, DefaultMessagesImpl.class, ElementWriterFactory.class,
        ResourceManager.class, DependenciesHandler.class })
public class FileSystemImportWriterTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    FileSystemImportWriter writer;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        writer = (FileSystemImportWriter) ImportWriterFactory.create(EImexType.FILE);
    }

    /**
     * DOC yyin Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        // do nothing
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.imex.model.FileSystemImportWriter#mergeSystemIndicator(org.talend.dataprofiler.core.ui.imex.model.ItemRecord, org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    public void testMergeSystemIndicator() {
        ItemRecord mockItem = mock(ItemRecord.class);
        TDQIndicatorDefinitionItem siItem = PropertiesFactory.eINSTANCE.createTDQIndicatorDefinitionItem();
        Property siProp = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        IndicatorDefinition SIdef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        siItem.setIndicatorDefinition(SIdef);
        siItem.setProperty(siProp);
        IndicatorDefinition importedDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        // make mockItem has: one new template, one modified newer template, one modified older template.
        TdExpression e0 = BooleanExpressionHelper.createTdExpression("new", "new body"); //$NON-NLS-1$ //$NON-NLS-2$
        e0.setModificationDate("20130101"); //$NON-NLS-1$
        TdExpression e1 = BooleanExpressionHelper.createTdExpression("tem_1", "modified newer body"); //$NON-NLS-1$ //$NON-NLS-2$
        e1.setModificationDate("20130101"); //$NON-NLS-1$
        TdExpression e2 = BooleanExpressionHelper.createTdExpression("tem_2", "modified older body"); //$NON-NLS-1$ //$NON-NLS-2$
        e2.setModificationDate("20110101"); //$NON-NLS-1$
        importedDef.getSqlGenericExpression().add(e0);
        importedDef.getSqlGenericExpression().add(e1);
        importedDef.getSqlGenericExpression().add(e2);
        when(mockItem.getElement()).thenReturn(importedDef);

        TdExpression e3 = BooleanExpressionHelper.createTdExpression("tem_1", "should be replaced");//$NON-NLS-1$ //$NON-NLS-2$
        e3.setModificationDate("20120101");//$NON-NLS-1$
        TdExpression e4 = BooleanExpressionHelper.createTdExpression("tem_2", "should keep this");//$NON-NLS-1$ //$NON-NLS-2$
        e4.setModificationDate("20120101");//$NON-NLS-1$
        SIdef.getSqlGenericExpression().add(e3);
        SIdef.getSqlGenericExpression().add(e4);

        ReturnCode rc = mock(ReturnCode.class);
        when(rc.isOk()).thenReturn(true);
        ElementWriterFactory ewFactory = mock(ElementWriterFactory.class);
        IndicatorDefinitionWriter indWriter = mock(IndicatorDefinitionWriter.class);
        when(indWriter.save(siItem, false)).thenReturn(rc);
        stub(method(ElementWriterFactory.class, "getInstance")).toReturn(ewFactory); //$NON-NLS-1$
        when(ewFactory.createIndicatorDefinitionWriter()).thenReturn(indWriter);

        Assert.assertEquals(SIdef.getSqlGenericExpression().size(), 2);// before merge
        writer.mergeSystemIndicator(mockItem, siItem);
        Assert.assertEquals(SIdef.getSqlGenericExpression().size(), 3);// after merge
        for (TdExpression ex : SIdef.getSqlGenericExpression()) {
            if (ex.getLanguage().equals("new")) {//$NON-NLS-1$
                Assert.assertEquals("new body", ex.getBody());//$NON-NLS-1$
            } else if (ex.getLanguage().equals("tem_1")) {//$NON-NLS-1$
                Assert.assertEquals("modified newer body", ex.getBody());//$NON-NLS-1$
            } else if (ex.getLanguage().equals("tem_2")) {//$NON-NLS-1$
                Assert.assertEquals("should keep this", ex.getBody()); //$NON-NLS-1$
            }
        }
        // other type?not SI
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.imex.model.FileSystemImportWriter#mergePattern(org.talend.dataprofiler.core.ui.imex.model.ItemRecord, org.talend.dataquality.domain.pattern.Pattern)}
     * .
     * 
     * @throws MalformedURLException
     */
    @Test
    public void testMergePattern() throws MalformedURLException {
        // import object
        ItemRecord importItem = mock(ItemRecord.class);
        Pattern importPattern = mock(Pattern.class);
        when(importItem.getElement()).thenReturn(importPattern);

        // old object
        TDQPatternItem oldPatternItem = mock(TDQPatternItem.class);
        Pattern oldPattern = mock(Pattern.class);
        when(oldPatternItem.getPattern()).thenReturn(oldPattern);

        // for imported pattern's expression
        InternalEObject eo = mock(InternalEObject.class);
        EList<PatternComponent> importComponents = new EObjectContainmentEList<PatternComponent>(PatternComponent.class, eo,
                PatternPackage.PATTERN__COMPONENTS);
        RegularExpression re = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression td = RelationalFactory.eINSTANCE.createTdExpression();
        td.setBody("sql body");//$NON-NLS-1$
        td.setLanguage("SQL");//$NON-NLS-1$
        td.setModificationDate("20130101");//$NON-NLS-1$
        re.setExpression(td);
        importComponents.add(re);
        RegularExpression re3 = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression td3 = RelationalFactory.eINSTANCE.createTdExpression();
        td3.setBody("imported body");//$NON-NLS-1$
        td3.setLanguage("MYSQL");//$NON-NLS-1$
        td3.setModificationDate("20130101");//$NON-NLS-1$
        re3.setExpression(td3);
        importComponents.add(re3);
        RegularExpression re4 = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression td4 = RelationalFactory.eINSTANCE.createTdExpression();
        td4.setBody("imported 4 body");//$NON-NLS-1$
        td4.setLanguage("Default");//$NON-NLS-1$
        td4.setModificationDate(null);
        re4.setExpression(td4);
        importComponents.add(re4);
        when(importPattern.getComponents()).thenReturn(importComponents);

        // for imported pattern's supplierDependencies
        Analysis analysis = mock(Analysis.class);
        Dependency dependency = mock(Dependency.class);
        when(dependency.getKind()).thenReturn(DependenciesHandler.USAGE);

        EList<ModelElement> analysisList = new BasicEList<ModelElement>();
        analysisList.add(analysis);
        when(dependency.getClient()).thenReturn(analysisList);
        when(analysis.eIsProxy()).thenReturn(false);

        File file = new File("C://Users//msjian//Desktop//myusetest//AA//TDQ_Data Profiling//Analyses//k_0.1.ana");//$NON-NLS-1$
        org.eclipse.emf.common.util.URI uri = URI.createFileURI(file.toURL().getPath().substring(1));
        Resource resource = mock(Resource.class);
        when(analysis.eResource()).thenReturn(resource);
        when(resource.getURI()).thenReturn(uri);

        PowerMockito.mockStatic(ResourceManager.class);
        IProject pro = mock(IProject.class);
        when(ResourceManager.getRootProject()).thenReturn(pro);
        when(pro.getLocation()).thenReturn(new Path("D:\\worspace\\workspace_TDQEE_5.3\\A")); //$NON-NLS-1$

        EList<ModelElement> patternlist = mock(EObjectContainmentEList.class);
        patternlist.add(importPattern);
        when(dependency.getSupplier()).thenReturn(patternlist);

        EList<Dependency> supplierDependencies_old = new BasicEList<Dependency>();
        when(oldPattern.getSupplierDependency()).thenReturn(supplierDependencies_old);
        Assert.assertEquals(0, oldPattern.getSupplierDependency().size());

        EList<Dependency> supplierDependencies_import = new BasicEList<Dependency>();
        supplierDependencies_import.add(dependency);
        when(importPattern.getSupplierDependency()).thenReturn(supplierDependencies_import);
        Assert.assertEquals(1, importPattern.getSupplierDependency().size());

        PowerMockito.mockStatic(DependenciesHandler.class);
        DependenciesHandler instance = mock(DependenciesHandler.class);
        when(DependenciesHandler.getInstance()).thenReturn(instance);
        TypedReturnCode<Dependency> rc = new TypedReturnCode<Dependency>();
        rc.setObject(dependency);
        when(instance.setUsageDependencyOn(analysis, oldPattern)).thenReturn(rc);
        when(oldPattern.getSupplierDependency()).thenReturn(supplierDependencies_import);

        // for system pattern's expression
        EList<PatternComponent> components = new EObjectContainmentEList<PatternComponent>(PatternComponent.class, eo,
                PatternPackage.PATTERN__COMPONENTS);
        RegularExpression re2 = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression td2 = RelationalFactory.eINSTANCE.createTdExpression();
        td2.setBody("system 2 body");//$NON-NLS-1$
        td2.setLanguage("MYSQL");//$NON-NLS-1$
        td2.setModificationDate("20120101");//$NON-NLS-1$
        re2.setExpression(td2);
        components.add(re2);
        // for re5: should be replace by re4
        RegularExpression re5 = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression td5 = RelationalFactory.eINSTANCE.createTdExpression();
        td5.setBody("system 5 body");//$NON-NLS-1$
        td5.setLanguage("Default");//$NON-NLS-1$
        td5.setModificationDate(null);
        re5.setExpression(td5);
        components.add(re5);
        when(oldPattern.getComponents()).thenReturn(components);

        ElementWriterFactory ewFactory = mock(ElementWriterFactory.class);
        PatternWriter pw = mock(PatternWriter.class);
        when(pw.save(oldPatternItem, true)).thenReturn(null);
        stub(method(ElementWriterFactory.class, "getInstance")).toReturn(ewFactory); //$NON-NLS-1$
        when(ewFactory.createPatternWriter()).thenReturn(pw);

        writer.mergePattern(importItem, oldPatternItem);

        for (PatternComponent component : components) {
            TdExpression ex = ((RegularExpression) component).getExpression();
            if (ex.getLanguage().equals("SQL")) {//$NON-NLS-1$
                Assert.assertEquals("sql body", ex.getBody());//$NON-NLS-1$
            } else if (ex.getLanguage().equals("MYSQL")) {//$NON-NLS-1$
                Assert.assertEquals("imported body", ex.getBody());//$NON-NLS-1$
            } else if (ex.getLanguage().equals("Default")) {//$NON-NLS-1$
                Assert.assertEquals("imported 4 body", ex.getBody());//$NON-NLS-1$
            }
        }

        Assert.assertNotNull(oldPattern.getSupplierDependency());
        Assert.assertEquals(1, oldPattern.getSupplierDependency().size());
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.imex.model.FileSystemImportWriter#mergeSystemIndicator(org.talend.dataprofiler.core.ui.imex.model.ItemRecord, org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * . check: if it is a indicator and checkExist=false(overwrite), then this method should return 0 record.
     * 
     * @throws Exception
     */
    @Test
    public void testPopulate_1() throws Exception {
        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        ItemRecord item1 = mock(ItemRecord.class);
        itemRecords.add(item1);
        IndicatorDefinition importedDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        when(item1.getElement()).thenReturn(importedDef);
        when(item1.getProperty()).thenReturn(null);
        when(item1.getErrors()).thenReturn(new ArrayList<String>());

        ItemRecord[] result = this.writer.populate(itemRecords.toArray(new ItemRecord[itemRecords.size()]), false);
        Assert.assertEquals(result.length, 0);
    }

    @Test
    public void testPopulate_2() throws Exception {
        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        ItemRecord item1 = mock(ItemRecord.class);
        itemRecords.add(item1);
        IndicatorDefinition importedDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        when(item1.getElement()).thenReturn(importedDef);
        when(item1.getProperty()).thenReturn(null);
        when(item1.getErrors()).thenReturn(new ArrayList<String>());
        // if checkExist = true , should return 1
        when(item1.isValid()).thenReturn(false);
        when(item1.getConflictObject()).thenReturn(mock(IRepositoryViewObject.class));
        when(item1.getName()).thenReturn("name"); //$NON-NLS-1$

        PowerMockito.spy(item1);
        PowerMockito.doNothing().when(item1).addError(anyString());
        ResourceBundle rb2 = mock(ResourceBundle.class);
        stub(method(ResourceBundle.class, "getBundle", String.class)).toReturn(rb2); //$NON-NLS-1$
        PowerMockito.mockStatic(DefaultMessagesImpl.class);
        when(DefaultMessagesImpl.getString(anyString())).thenReturn("name"); //$NON-NLS-1$

        ItemRecord[] result2 = this.writer.populate(itemRecords.toArray(new ItemRecord[itemRecords.size()]), true);
        Assert.assertEquals(result2.length, 1);

    }
}
