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

/***********************************************************************************************************************
 * Copyright (c) 2006, 2007 Obeo. All rights reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Obeo - initial API and implementation
 **********************************************************************************************************************/
package org.talend.cwm.compare;

import org.apache.log4j.Logger;
import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeEditorInput;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IComparisonFactory;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.talend.commons.emf.EMFUtil;

/**
 * This application will try and launch an headless model comparison.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class ExampleLauncher {

    protected static Logger log = Logger.getLogger(ExampleLauncher.class);

    private static String fileName1 = "F:/tmp/mysql_0.1.item";

    private static String fileName2 = "F:/tmp/aa_0.1.item";

    /**
     * This class doesn't need to be instantiated.
     */
    public ExampleLauncher() {
        // prevents instantiation
    }

    /**
     * Launcher of this application.
     * 
     * @param args Arguments of the launch.
     */
    public static void main(String[] args) {
        new EMFUtil();
        compare();

    }

    private static void load(String absolutePath, ResourceSet resourceSet) {
        URI uri = URI.createFileURI(absolutePath);

        // Resource will be loaded within the resource set
        resourceSet.getResource(uri, true);
    }

    public static void compare() {
        // Load the two input models
        ResourceSet resourceSet1 = new ResourceSetImpl();
        ResourceSet resourceSet2 = new ResourceSetImpl();
        // String xmi1 = "path/to/first/model.xmi";
        // String xmi2 = "path/to/second/model.xmi";
        load(fileName1, resourceSet1);
        load(fileName2, resourceSet2);

        // Configure EMF Compare
        IEObjectMatcher matcher = DefaultMatchEngine.createDefaultEObjectMatcher(UseIdentifiers.NEVER);
        IComparisonFactory comparisonFactory = new DefaultComparisonFactory(new DefaultEqualityHelperFactory());
        IMatchEngine.Factory matchEngineFactory = new MatchEngineFactoryImpl(matcher, comparisonFactory);
        matchEngineFactory.setRanking(20);
        IMatchEngine.Factory.Registry matchEngineRegistry = new MatchEngineFactoryRegistryImpl();
        matchEngineRegistry.add(matchEngineFactory);
        EMFCompare comparator = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineRegistry).build();

        // Compare the two models
        IComparisonScope scope = new DefaultComparisonScope(resourceSet1, resourceSet2, null);
        Comparison compare = comparator.compare(scope);
        comparator.compare(scope);

        EList<Diff> differences = compare.getDifferences();

        ICompareEditingDomain editingDomain = EMFCompareEditingDomain.create(scope.getLeft(), scope.getRight(), null);
        AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
        EMFCompareConfiguration configuration = new EMFCompareConfiguration(new CompareConfiguration());
        CompareEditorInput input = new ComparisonScopeEditorInput(configuration, editingDomain, adapterFactory, comparator, scope);

        // CompareUI.openCompareDialog(input); // or CompareUI.openCompareEditor(input);
        CompareUI.openCompareEditor(input);
    }
}
