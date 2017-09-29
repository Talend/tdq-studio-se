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
package org.talend.dataprofiler.core.ui.editor.preview.model.states;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.utils.SpecialValueDisplay;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableWithData;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.BenfordLawFrequencyIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.PatternChartDataEntity;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class ChartTableProviderClassSet {

    /**
     * DOC zqin ChartTableFactory class global comment. Detailled comment
     */
    public static class BaseChartTableLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            ChartDataEntity entity = (ChartDataEntity) element;

            // MOD yyi 2012-02-27 TDQ-4676:Display threshold warning separately.
            boolean isPercentThreshold = IndicatorHelper.hasPercentThreshold(entity.getIndicator());
            boolean isValueThreshold = IndicatorHelper.hasValueThreshold(entity.getIndicator());
            String currentText = getColumnText(element, columnIndex);
            if (currentText == null) {
                return null;
            }
            boolean isCurrentCol = currentText.equals(entity.getValue()) || currentText.equals(entity.getPersent());
            if (isCurrentCol && entity.isOutOfRange(currentText)) {
                if (2 == columnIndex && isPercentThreshold) {
                    return ImageLib.getImage(ImageLib.LEVEL_WARNING);
                } else if (1 == columnIndex && isValueThreshold) {
                    return ImageLib.getImage(ImageLib.LEVEL_WARNING);
                }
            }

            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            ChartDataEntity entity = (ChartDataEntity) element;
            switch (columnIndex) {
            case 0:
                return entity.getLabel();
            case 1:
                return entity.getValue();
            case 2:
                return entity.getPersent();
            default:
                return ""; //$NON-NLS-1$
            }
        }

        public Color getBackground(Object element, int columnIndex) {

            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
         */
        public Color getForeground(Object element, int columnIndex) {
            ChartDataEntity entity = (ChartDataEntity) element;

            // MOD yyi 2012-02-27 TDQ-4676:Display threshold warning separately.
            boolean isPercentThreshold = IndicatorHelper.hasPercentThreshold(entity.getIndicator());
            boolean isValueThreshold = IndicatorHelper.hasValueThreshold(entity.getIndicator());
            String currentText = getColumnText(element, columnIndex);
            boolean isCurrentCol = currentText.equals(entity.getValue()) || currentText.equals(entity.getPersent());
            if (isCurrentCol && entity.isOutOfRange(currentText)) {
                if (2 == columnIndex && isPercentThreshold) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                } else if (1 == columnIndex && isValueThreshold) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                }
            }

            return null;
        }
    }

    /**
     * DOC zqin ChartTableFactory class global comment. Detailled comment
     */
    public static class SummaryLabelProvider extends BaseChartTableLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            ChartDataEntity entity = (ChartDataEntity) element;

            switch (columnIndex) {
            case 0:
                return entity.getLabel();
            case 1:
                return entity.getValue();

            default:
                return ""; //$NON-NLS-1$
            }
        }
    }

    /**
     * DOC zqin ChartTableFactory class global comment. Detailled comment
     */
    public static class FrequencyLabelProvider extends BaseChartTableLabelProvider {

        /*
         * (non-Javadoc)
         * 
         * @seeorg.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.
         * BaseChartTableLabelProvider#getColumnText(java.lang.Object, int)
         */
        @Override
        public String getColumnText(Object element, int columnIndex) {
            ChartDataEntity entity = (ChartDataEntity) element;
            switch (columnIndex) {
            case 0:
                return entity.getLabel();
            case 1:
                return entity.getValue();
            case 2:
                return entity.getPersent();
            default:
                return ""; //$NON-NLS-1$
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @seeorg.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.
         * BaseChartTableLabelProvider#getForeground(java.lang.Object, int)
         */
        @Override
        public Color getForeground(Object element, int columnIndex) {
            ChartDataEntity entity = (ChartDataEntity) element;
            String label = entity.getLabel();
            // ADD msjian TDQ-6235 2012-10-19: when Benford law indicator, some values make the color as red
            if (entity.getIndicator() instanceof BenfordLawFrequencyIndicator) {
                if (SpecialValueDisplay.ZREO_FIELD.equals(label) || SpecialValueDisplay.INVALID_FIELD.equals(label)) {
                    if (columnIndex == 2) {
                        return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                    }
                }
            }
            // TDQ-6235~
            switch (columnIndex) {
            case 0:
                if (SpecialValueDisplay.NULL_FIELD.equals(label) || SpecialValueDisplay.EMPTY_FIELD.equals(label)) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                }
            default:
                return null;
            }
        }
    }

    /**
     * DOC mzhao FrequencyTypeStates, soundex frequency label provider.Feature: 6307.
     */
    public static class SoundexBaseChartTableLabelProvider extends BaseChartTableLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            ChartDataEntity entity = (ChartDataEntity) element;

            switch (columnIndex) {
            case 0:
                return entity.getLabel();
            case 1:
                return entity.getValue();
            case 2:
                return String.valueOf(((FrequencyIndicator) entity.getIndicator()).getCount(entity.getKey()));
            case 3:
                return entity.getPersent();
            default:
                return ""; //$NON-NLS-1$
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @seeorg.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.
         * BaseChartTableLabelProvider#getForeground(java.lang.Object, int)
         */
        @Override
        public Color getForeground(Object element, int columnIndex) {
            ChartDataEntity entity = (ChartDataEntity) element;
            String label = entity.getLabel();
            switch (columnIndex) {
            case 0:
                if (SpecialValueDisplay.NULL_FIELD.equals(label) || SpecialValueDisplay.EMPTY_FIELD.equals(label)) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                }
            default:
                return null;
            }
        }

    }

    /**
     * DOC zqin ChartTableFactory class global comment. Detailled comment
     */
    public static class PatternLabelProvider extends BaseChartTableLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            PatternChartDataEntity entity = (PatternChartDataEntity) element;

            switch (columnIndex) {
            case 0:
                return entity.getLabel();
            case 1:
                // MOD msjian TDQ-4380(TDQ-4470) 2012-1-29: set invalidate value to "N/A"
                return entity.isOutOfValue(entity.getPerMatch()) ? PluginConstant.NA_STRING : entity.getPerMatch();
            case 2:
                return entity.isOutOfValue(entity.getPerNoMatch()) ? PluginConstant.NA_STRING : entity.getPerNoMatch();
            case 3:
                return entity.getNumMatch();
            case 4:
                return entity.isOutOfValue(entity.getNumNoMatch()) ? PluginConstant.NA_STRING : entity.getNumNoMatch();
                // TDQ-4380(TDQ-4470)~

            default:
                return ""; //$NON-NLS-1$
            }
        }

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            PatternChartDataEntity entity = (PatternChartDataEntity) element;
            // MOD yyi 2012-02-27 TDQ-4676:Display threshold warning separately.
            boolean isPercentThreshold = IndicatorHelper.hasPercentThreshold(entity.getIndicator());
            boolean isValueThreshold = IndicatorHelper.hasValueThreshold(entity.getIndicator());

            String currentText = getColumnText(element, columnIndex);
            // MOD mzhao bug 8838 2009-09-08
            // MOD msjian TDQ-4380(TDQ-4470) 2012-1-29: set warning image when the value is invalidated
            boolean isCurrentCol = currentText.equals(entity.getNumMatch()) || currentText.equals(entity.getPerMatch());
            if (isCurrentCol && (entity.isOutOfRange(currentText) || entity.isOutOfValue(currentText))) {
                // TDQ-4380(TDQ-4470)~
                if (1 == columnIndex && isPercentThreshold) {
                    return ImageLib.getImage(ImageLib.LEVEL_WARNING);
                } else if (3 == columnIndex && isValueThreshold) {
                    return ImageLib.getImage(ImageLib.LEVEL_WARNING);
                }
            } else if (currentText.equals(PluginConstant.NA_STRING)) {
                return ImageLib.getImage(ImageLib.LEVEL_WARNING);
            }

            return null;
        }

        @Override
        public Color getForeground(Object element, int columnIndex) {
            PatternChartDataEntity entity = (PatternChartDataEntity) element;
            // MOD yyi 2012-02-27 TDQ-4676:Display threshold warning separately.
            boolean isPercentThreshold = IndicatorHelper.hasPercentThreshold(entity.getIndicator());
            boolean isValueThreshold = IndicatorHelper.hasValueThreshold(entity.getIndicator());

            String currentText = getColumnText(element, columnIndex);
            // MOD mzhao bug 8838 2009-09-08
            // MOD msjian TDQ-4380(TDQ-4470) 2012-1-29: set font color when the value is invalidated
            boolean isCurrentCol = currentText.equals(entity.getNumMatch()) || currentText.equals(entity.getPerMatch());
            if (isCurrentCol && (entity.isOutOfRange(currentText) || entity.isOutOfValue(currentText))) {
                // TDQ-4380(TDQ-4470)~
                if (1 == columnIndex && isPercentThreshold) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                } else if (3 == columnIndex && isValueThreshold) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                }
            } else if (currentText.equals(PluginConstant.NA_STRING)) {
                return Display.getDefault().getSystemColor(SWT.COLOR_RED);
            }

            return null;
        }
    }

    /**
     * DOC zqin ChartTableFactory class global comment. Detailled comment
     */
    public static class ModeLabelProvider extends BaseChartTableLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            ChartDataEntity entity = (ChartDataEntity) element;

            return entity.getValue();
        }

    }

    /**
     * DOC zqin ChartTableFactory class global comment. Detailled comment
     */
    public static class CommonContenteProvider implements IStructuredContentProvider {

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof TableWithData) {
                return ((TableWithData) inputElement).getEnity();
            } else {
                return new Object[0];
            }
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

    }
}
