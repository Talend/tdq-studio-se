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
package org.talend.dataprofiler.core.ui.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dq.factory.ModelElementFileFactory;
import org.talend.dq.helper.RepositoryNodeComparator;
import org.talend.dq.indicators.ext.FrequencyExt;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * The factory to build comparator. DOC zqin class global comment. Detailled comment
 */
public final class ComparatorsFactory {

    private ComparatorsFactory() {
    }

    public static final int FILEMODEL_COMPARATOR_ID = 0;

    public static final int MODELELEMENT_COMPARATOR_ID = 1;

    public static final int TEXT_STATISTICS_COMPARATOR_ID = 2;

    public static final int FILE_RESOURCE_COMPARATOR_ID = 3;

    public static final int FREQUENCY_COMPARATOR_ID = 4;

    public static final int LOW_FREQUENCY_COMPARATOR_ID = 5;

    // MOD mzhao 2009-03-23 Feature 6307
    public static final int SOUNDEX_FREQUENCY_COMPARATOR_ID = 6;

    public static final int SOUNDEX_LOW_FREQUENCY_COMPARATOR_ID = 7;

    public static final int IREPOSITORYVIEWOBJECT_COMPARATOR_ID = 8;

    public static final int REPOSITORY_NODE_COMPARATOR_ID = 10;

    // ADDED yyin 2012-08-28 TDQ_5076
    public static final int BENFORDLAW_FREQUENCY_COMPARATOR_ID = 11;

    /**
     * DOC zqin Comment method "sort".
     * 
     * @param objects
     * @param comparatorId
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object[] sort(Object[] objects, int comparatorId) {
        if (objects == null || objects.length <= 1) {
            return objects;
        }
        Arrays.sort(objects, ComparatorsFactory.buildComparator(comparatorId));
        return objects;
    }

    /**
     * DOC zqin Comment method "sort".
     * 
     * @param objects
     * @param comparatorId
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Collection sort(List objects, int comparatorId) {
        if (objects == null || objects.size() <= 1) {
            return objects;
        }
        Collections.sort(objects, ComparatorsFactory.buildComparator(comparatorId));
        return objects;
    }

    @SuppressWarnings("unchecked")
    public static Comparator buildComparator(int comparatorId) {
        switch (comparatorId) {
        case FILEMODEL_COMPARATOR_ID:
            return new FileModelComparator();
        case MODELELEMENT_COMPARATOR_ID:
            return new ModelElementComparator();
        case TEXT_STATISTICS_COMPARATOR_ID:
            return new TextStatisticsComparator();
        case FILE_RESOURCE_COMPARATOR_ID:
            return new FileResourceComparator();
        case FREQUENCY_COMPARATOR_ID:
            return new FrequencyIndicatorComparator();
        case LOW_FREQUENCY_COMPARATOR_ID:
            return new LowFrequencyIndicatorComparator();
            // MOD mzhao 2009-03-23 Feature 6307
        case SOUNDEX_FREQUENCY_COMPARATOR_ID:
            return new FrequencyIndicatorComparator();
        case SOUNDEX_LOW_FREQUENCY_COMPARATOR_ID:
            return new LowFrequencyIndicatorComparator();
        case IREPOSITORYVIEWOBJECT_COMPARATOR_ID:
            return new IRepositoryViewObjectComparator();
        case REPOSITORY_NODE_COMPARATOR_ID:
            return new RepositoryNodeComparator();
        case BENFORDLAW_FREQUENCY_COMPARATOR_ID:// ADDED yyin 2012-08-28 TDQ_5076
            return new BenfordLawIndicatorComparator();
        default:
            return new ModelElementComparator();
        }
    }

    /**
     * Implements Comparator to implement custom sorting of {@link IResource} which contains {@link ModelElement}
     * elements.
     */
    static class FileResourceComparator implements Comparator<IResource> {

        public int compare(IResource arg0, IResource arg1) {

            if (arg0 == null || arg1 == null) {
                return 0;
            }
            String name0;
            String name1;
            if (arg0.getType() == IResource.FILE) {
                name0 = ((IFile) arg0).getName();
            } else {
                name0 = arg0.getName();
            }
            if (arg1.getType() == IResource.FILE) {
                name1 = ((IFile) arg1).getName();
            } else {
                name1 = arg1.getName();
            }
            if (name0 == null || name1 == null) {
                return 0;
            }

            return name0.compareTo(name1);
        }

    }

    /**
     * Implements Comparator to implement custom sorting of {@link IResource} which contains {@link ModelElement}
     * elements.
     */
    static class FileModelComparator implements Comparator<IResource> {

        public int compare(IResource arg0, IResource arg1) {

            if (arg0 == null || arg1 == null) {
                return 0;
            }
            String name0;
            String name1;
            if (arg0.getType() == IResource.FILE) {
                ModelElement modelElement0 = ModelElementFileFactory.getModelElement((IFile) arg0);
                name0 = modelElement0 == null ? arg0.getName() : modelElement0.getName();
            } else {
                name0 = arg0.getName();
            }
            if (arg1.getType() == IResource.FILE) {
                ModelElement modelElement1 = ModelElementFileFactory.getModelElement((IFile) arg1);
                name1 = modelElement1 == null ? arg1.getName() : modelElement1.getName();
            } else {
                name1 = arg1.getName();
            }
            if (name0 == null || name1 == null) {
                return 0;
            }

            return name0.compareTo(name1);
        }

    }

    /**
     * Implements Comparator to implement custom sorting of {@link ModelElement}.
     */
    static class ModelElementComparator implements Comparator<Object> {

        public int compare(Object arg0, Object arg1) {

            if (arg0 == null || arg1 == null) {
                return 0;
            }
            String name0 = ((ModelElement) arg0).getName();
            String name1 = ((ModelElement) arg1).getName();

            if (name0 == null || name1 == null) {
                return 0;
            }

            return name0.compareTo(name1);
        }
    }

    /**
     * DOC zqin ComparatorsFactory class global comment. Detailled comment
     */
    static class TextStatisticsComparator implements Comparator<IndicatorUnit> {

        public int compare(IndicatorUnit o1, IndicatorUnit o2) {

            if (o1.isExcuted() && o2.isExcuted()) {
                double value1 = Double.parseDouble(String.valueOf(o1.getValue()));
                double value2 = Double.parseDouble(String.valueOf(o2.getValue()));
                if (value1 <= value2) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return 0;
            }

        }

    }

    /**
     * DOC Zqin ComparatorsFactory class global comment. Detailled comment
     */
    static class FrequencyIndicatorComparator implements Comparator<FrequencyExt> {

        public int compare(FrequencyExt o1, FrequencyExt o2) {
            if (o1.getValue() == o2.getValue()) {
                return 0;
            }
            if (o1.getValue() < o2.getValue()) {
                return 1;
            }
            return -1;
        }

    }

    // ADDED yyin 2012-08-28 TDQ_5076
    static class BenfordLawIndicatorComparator implements Comparator<FrequencyExt> {

        public int compare(FrequencyExt o1, FrequencyExt o2) {
            if (o1.getKey() == null || o2.getKey() == null) {
                return -1;
            }
            if ("0".equals(o1.getKey().toString())) {
                return 1;
            }
            if ("0".equals(o2.getKey().toString())) {
                return -1;
            }
            if (o1.getKey().toString().compareTo(o2.getKey().toString()) > 0) {
                return 1;
            }
            if (o1.getKey().toString().compareTo(o2.getKey().toString()) == 0) {
                return 0;
            }
            return -1;
        }
    }// ~

    /**
     * DOC Zqin ComparatorsFactory class global comment. Detailled comment
     */
    static class LowFrequencyIndicatorComparator implements Comparator<FrequencyExt> {

        public int compare(FrequencyExt o1, FrequencyExt o2) {
            if (o1.getValue() == o2.getValue()) {
                return 0;
            }
            if (o1.getValue() > o2.getValue()) {
                return 1;
            }
            return -1;
        }

    }

    /**
     * DOC zshen ComparatorsFactory class global comment. Detailled comment
     */
    static class IRepositoryViewObjectComparator implements Comparator<Object> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */

        // MOD qiongli 2010-12-28 bug 17922.handle folder under connection.
        public int compare(Object o1, Object o2) {
            if (o1 == null || o2 == null) {
                return 0;
            }
            String name1 = null;
            String name2 = null;
            if (o1 instanceof IRepositoryViewObject) {
                name1 = ((IRepositoryViewObject) o1).getLabel();
            } else if (o1 instanceof IResource) {
                name1 = ((IResource) o1).getName();
            }
            if (o2 instanceof IRepositoryViewObject) {
                name2 = ((IRepositoryViewObject) o2).getLabel();
            } else if (o2 instanceof IResource) {
                name2 = ((IResource) o2).getName();
            }

            // FIXME name1 might be null.
            return name1.compareTo(name2);
        }
    }

}
