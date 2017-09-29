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
package org.talend.dataquality.indicators.impl;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.ResourceUtils;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.SoundexFreqIndicator;

/**
 * created by talend on Aug 12, 2014 Detailled comment
 * 
 */
public class SoundexFreqIndicatorImplTest {

    /**
     * when UsedMapDBMode Test method for
     * {@link org.talend.dataquality.indicators.impl.SoundexFreqIndicatorImpl#handle(java.lang.Object)}.
     * 
     */
    @Test
    public void testHandleObject_1() {
        Object empty = null;
        // mapDB case
        SoundexFreqIndicator soundexFreqIndicator = IndicatorsFactory.eINSTANCE.createSoundexFreqIndicator();

        ((SoundexFreqIndicatorImpl) soundexFreqIndicator).setUsedMapDBMode(true);
        ResourceUtils.createAnalysis(soundexFreqIndicator);
        soundexFreqIndicator.reset();

        soundexFreqIndicator.handle(empty);
        soundexFreqIndicator.handle("mac"); //$NON-NLS-1$
        soundexFreqIndicator.handle("mic"); //$NON-NLS-1$
        soundexFreqIndicator.handle(empty);
        soundexFreqIndicator.handle("mic"); //$NON-NLS-1$
        soundexFreqIndicator.handle("mac"); //$NON-NLS-1$
        soundexFreqIndicator.handle("machine"); //$NON-NLS-1$
        soundexFreqIndicator.handle(empty);
        soundexFreqIndicator.handle("machine"); //$NON-NLS-1$
        soundexFreqIndicator.handle("unique"); //$NON-NLS-1$

        soundexFreqIndicator.finalizeComputation();
        Assert.assertEquals(true, soundexFreqIndicator.getUniqueValues().isEmpty());
        Assert.assertEquals(2, soundexFreqIndicator.getValueToDistinctFreq().get("mic").intValue()); //$NON-NLS-1$
        Assert.assertEquals(0, soundexFreqIndicator.getValueToDistinctFreq().get(empty).intValue());
        Assert.assertEquals(1, soundexFreqIndicator.getValueToDistinctFreq().get("machine").intValue()); //$NON-NLS-1$
        Assert.assertEquals(1, soundexFreqIndicator.getValueToDistinctFreq().get("unique").intValue()); //$NON-NLS-1$

        Assert.assertEquals(4, soundexFreqIndicator.getValueToFreq().get("mic").intValue()); //$NON-NLS-1$
        Assert.assertEquals(3, soundexFreqIndicator.getValueToFreq().get(empty).intValue());
        Assert.assertEquals(2, soundexFreqIndicator.getValueToFreq().get("machine").intValue()); //$NON-NLS-1$
        Assert.assertEquals(1, soundexFreqIndicator.getValueToFreq().get("unique").intValue()); //$NON-NLS-1$

    }

    /**
     * when do not UsedMapDBMode Test method for
     * {@link org.talend.dataquality.indicators.impl.SoundexFreqIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandleObject_2() {
        Object empty = null;
        // java case
        SoundexFreqIndicator soundexFreqIndicator = IndicatorsFactory.eINSTANCE.createSoundexFreqIndicator();
        ((SoundexFreqIndicatorImpl) soundexFreqIndicator).setUsedMapDBMode(false);
        soundexFreqIndicator.reset();
        soundexFreqIndicator.handle(empty);
        soundexFreqIndicator.handle("mac"); //$NON-NLS-1$
        soundexFreqIndicator.handle("mic"); //$NON-NLS-1$
        soundexFreqIndicator.handle(empty);
        soundexFreqIndicator.handle("mic"); //$NON-NLS-1$
        soundexFreqIndicator.handle("mac"); //$NON-NLS-1$
        soundexFreqIndicator.handle("machine"); //$NON-NLS-1$
        soundexFreqIndicator.handle(empty);
        soundexFreqIndicator.handle("machine"); //$NON-NLS-1$
        soundexFreqIndicator.handle("unique"); //$NON-NLS-1$

        soundexFreqIndicator.finalizeComputation();
        Assert.assertEquals(false, soundexFreqIndicator.getUniqueValues().isEmpty());
        Assert.assertEquals(1, soundexFreqIndicator.getUniqueValues().size());
        Assert.assertEquals(2, soundexFreqIndicator.getValueToDistinctFreq().get("mic").intValue()); //$NON-NLS-1$
        Assert.assertNull(soundexFreqIndicator.getValueToDistinctFreq().get("Null field")); //$NON-NLS-1$
        Assert.assertEquals(1, soundexFreqIndicator.getValueToDistinctFreq().get("machine").intValue()); //$NON-NLS-1$
        Assert.assertEquals(1, soundexFreqIndicator.getValueToDistinctFreq().get("unique").intValue()); //$NON-NLS-1$

        Assert.assertEquals(4, soundexFreqIndicator.getValueToFreq().get("mic").intValue()); //$NON-NLS-1$
        Assert.assertNull(soundexFreqIndicator.getValueToDistinctFreq().get("Null field")); //$NON-NLS-1$
        Assert.assertEquals(2, soundexFreqIndicator.getValueToFreq().get("machine").intValue()); //$NON-NLS-1$
        Assert.assertEquals(1, soundexFreqIndicator.getValueToDistinctFreq().get("unique").intValue()); //$NON-NLS-1$
    }

}
