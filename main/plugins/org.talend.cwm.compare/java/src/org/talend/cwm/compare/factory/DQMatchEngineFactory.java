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
package org.talend.cwm.compare.factory;

import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IComparisonFactory;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.talend.cwm.compare.ModelElementMatchEngine;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class DQMatchEngineFactory implements IMatchEngine.Factory {

    /** The match engine created by this factory. */
    protected IMatchEngine matchEngine;

    /** Ranking of this match engine. */
    private int ranking;

    /** A match engine needs a WeightProvider in case of this match engine do not use identifiers. */
    private WeightProvider.Descriptor.Registry weightProviderRegistry;

    /**
     * Constructor that instantiate a {@link DefaultMatchEngine}. This match engine will use a the standalone weight
     * provider registry {@link WeightProviderDescriptorRegistryImpl.createStandaloneInstance()}.
     */
    public DQMatchEngineFactory() {
        this(UseIdentifiers.WHEN_AVAILABLE, WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
    }

    /**
     * Constructor that instantiate a {@link DefaultMatchEngine} that will use identifiers as specified by the given
     * {@code useIDs} enumeration. This match engine will use a the standalone weight provider registry {@link
     * WeightProviderDescriptorRegistryImpl.createStandaloneInstance()}.
     * 
     * @param useIDs the kinds of matcher to use.
     */
    public DQMatchEngineFactory(UseIdentifiers useIDs) {
        this(useIDs, WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
    }

    /**
     * Constructor that instantiate a {@link DefaultMatchEngine} that will use identifiers as specified by the given
     * {@code useIDs} enumeration.
     * 
     * @param useIDs the kinds of matcher to use.
     * @param registry A match engine needs a WeightProvider in case of this match engine do not use identifiers.
     */
    public DQMatchEngineFactory(UseIdentifiers useIDs, WeightProvider.Descriptor.Registry registry) {
        final IComparisonFactory comparisonFactory = new DefaultComparisonFactory(new DefaultEqualityHelperFactory());
        weightProviderRegistry = registry;
        final IEObjectMatcher matcher = ModelElementMatchEngine.createDQEObjectMatcher(useIDs, weightProviderRegistry);
        matchEngine = new ModelElementMatchEngine(matcher, comparisonFactory);
    }

    /**
     * Constructor that instantiate a {@link DefaultMatchEngine} with the given parameters.
     * 
     * @param matcher The matcher that will be in charge of pairing EObjects together for this comparison process.
     * @param comparisonFactory factory that will be use to instantiate Comparison as return by match() methods.
     */
    public DQMatchEngineFactory(IEObjectMatcher matcher, IComparisonFactory comparisonFactory) {
        matchEngine = new ModelElementMatchEngine(matcher, comparisonFactory);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.emf.compare.match.IMatchEngine.Factory#getMatchEngine()
     */
    @Override
    public IMatchEngine getMatchEngine() {
        return matchEngine;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.emf.compare.match.IMatchEngine.Factory#getRanking()
     */
    @Override
    public int getRanking() {
        return ranking;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.emf.compare.match.IMatchEngine.Factory#setRanking(int)
     */
    @Override
    public void setRanking(int r) {
        ranking = r;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.emf.compare.match.IMatchEngine.Factory#isMatchEngineFactoryFor(org.eclipse.emf.compare.scope.IComparisonScope)
     */
    @Override
    public boolean isMatchEngineFactoryFor(IComparisonScope scope) {
        return true;
    }

    /**
     * The match engine needs a WeightProvider in case of this match engine do not use identifiers.
     * 
     * @param registry the registry to associate with the match engine.
     */
    void setWeightProviderRegistry(WeightProvider.Descriptor.Registry registry) {
        this.weightProviderRegistry = registry;
    }

}
