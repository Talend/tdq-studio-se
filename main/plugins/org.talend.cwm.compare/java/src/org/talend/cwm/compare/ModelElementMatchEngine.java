// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.cwm.compare;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IComparisonFactory;
import org.eclipse.emf.compare.match.eobject.CachingDistance;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.IdentifierEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.SwitchHelpers;

import com.google.common.collect.Iterators;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class ModelElementMatchEngine extends DefaultMatchEngine {

    /**
     * DOC qiongli ModelElementMatchEngine constructor comment.
     * 
     * @param matcher
     * @param comparisonFactory
     */
    public ModelElementMatchEngine(IEObjectMatcher matcher, IComparisonFactory comparisonFactory) {
        super(matcher, comparisonFactory);
    }

    /**
     * Creates and configures an {@link IEObjectMatcher} with the strategy given by {@code useIDs}. The {@code cache}
     * will be used to cache some expensive computation (should better a LoadingCache).
     * 
     * @param useIDs which strategy the return IEObjectMatcher must follow.
     * @return a new IEObjectMatcher.
     */
    public static IEObjectMatcher createDQEObjectMatcher(UseIdentifiers useIDs) {
        return createDQEObjectMatcher(useIDs, WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
    }

    /**
     * Creates and configures an {@link IEObjectMatcher} with the strategy given by {@code useIDs}. The {@code cache}
     * will be used to cache some expensive computation (should better a LoadingCache).
     * 
     * @param useIDs which strategy the return IEObjectMatcher must follow.
     * @param weightProviderRegistry the match engine needs a WeightProvider in case of this match engine do not use
     * identifiers.
     * @return a new IEObjectMatcher.
     */
    public static IEObjectMatcher createDQEObjectMatcher(UseIdentifiers useIDs,
            WeightProvider.Descriptor.Registry weightProviderRegistry) {
        final IEObjectMatcher matcher;
        final ModelElementEditonDistance editionDistance = new ModelElementEditonDistance(weightProviderRegistry);
        final CachingDistance cachedDistance = new CachingDistance(editionDistance);
        switch (useIDs) {
        case NEVER:
            matcher = new ProximityEObjectMatcher(cachedDistance);
            break;
        case ONLY:
            matcher = new IdentifierEObjectMatcher();
            break;
        case WHEN_AVAILABLE:
            // fall through to default
        default:
            // Use an ID matcher, delegating to proximity when no ID is available
            final IEObjectMatcher contentMatcher = new ProximityEObjectMatcher(cachedDistance);
            matcher = new IdentifierEObjectMatcher(contentMatcher);
            break;

        }

        return matcher;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.compare.match.DefaultMatchEngine#match(org.eclipse.emf.compare.Comparison,
     * org.eclipse.emf.compare.scope.IComparisonScope, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject,
     * org.eclipse.emf.ecore.EObject, org.eclipse.emf.common.util.Monitor)
     */
    @Override
    protected void match(Comparison comparison, IComparisonScope scope, EObject left, EObject right, EObject origin,
            Monitor monitor) {
        DatabaseConnection dbConnLeft = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(left);
        DatabaseConnection dbConnRight = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(right);
        if (dbConnLeft != null && dbConnRight != null) {
            matchForDBConn(comparison, scope, left, right, origin, monitor);
            return;
        }

        super.match(comparison, scope, left, right, origin, monitor);
    }

    /**
     * 
     * if the scope is DBConnection,should use scope.getCoveredEObjects(EResource res),instead of
     * scope.getChildren(EObject left).
     * 
     * @param comparison
     * @param scope
     * @param left
     * @param right
     * @param origin
     * @param monitor
     */
    private void matchForDBConn(Comparison comparison, IComparisonScope scope, EObject left, EObject right, EObject origin,
            Monitor monitor) {
        if (left == null || right == null) {
            throw new IllegalArgumentException();
        }

        final Iterator<? extends EObject> leftEObjects = Iterators.concat(Iterators.singletonIterator(left),
                scope.getCoveredEObjects(left.eResource()));
        final Iterator<? extends EObject> rightEObjects = Iterators.concat(Iterators.singletonIterator(right),
                scope.getCoveredEObjects(right.eResource()));
        final Iterator<? extends EObject> originEObjects;
        if (origin != null) {
            originEObjects = Iterators.concat(Iterators.singletonIterator(origin), scope.getCoveredEObjects(origin.eResource()));
        } else {
            originEObjects = Collections.EMPTY_SET.iterator();
        }

        getEObjectMatcher().createMatches(comparison, leftEObjects, rightEObjects, originEObjects, monitor);
    }
}
