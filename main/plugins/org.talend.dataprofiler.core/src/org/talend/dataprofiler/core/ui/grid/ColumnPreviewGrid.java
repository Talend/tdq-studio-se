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
package org.talend.dataprofiler.core.ui.grid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.TalendGridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.grid.utils.Observerable;
import org.talend.dataprofiler.core.ui.grid.utils.TDQObserver;
import org.talend.dataprofiler.core.ui.grid.utils.events.ObserverEvent;
import org.talend.dataprofiler.core.ui.grid.utils.events.ObserverEventEnum;
import org.talend.dataquality.PluginConstant;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * The table which is used be preivew data
 * 
 */
public class ColumnPreviewGrid extends AbstractIndicatorSelectGrid implements TDQObserver<ObserverEvent>,
        Observerable<ObserverEvent> {

    private List<TDQObserver<ObserverEvent>> observers = null;

    /**
     * ColumnPreviewGrid constructor comment.
     * 
     * @param dialog
     * @param parent
     * @param style
     * @param modelElementIndicators
     */
    public ColumnPreviewGrid(IndicatorSelectDialog dialog, Composite parent, int style,
            ModelElementIndicator[] modelElementIndicators, int limit, List<Object[]> previewData) {
        super(dialog, parent, style, modelElementIndicators, limit, previewData);
        initializeGrid();
        addVscrollBarListener();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.AbstractIndicatorSelectGrid#createTableContent()
     */
    @Override
    protected void createTableContent() {
        try {
            TalendGridItem PreviewItem = new TalendGridItem(this, SWT.NONE);
            for (int i = 0; i < this.getColumnCount(); i++) {
                PreviewItem.setCheckable(i, false);
            }
            PreviewItem.setText(DefaultMessagesImpl.getString("ColumnPreviewGrid.PreviewItemLabel")); //$NON-NLS-1$
            processNodePrivew(null, PreviewItem);
            PreviewItem.setExpanded(true);
        } catch (SQLException e) {

        }
    }

    /**
     * DOC talend Comment method "processNodePrivew".
     * 
     * @param object
     * @param previewItem
     * @throws SQLException
     */
    private void processNodePrivew(Object object, GridItem parentItem) throws SQLException {
        if (!checkSameTable()) {
            return;
        }
        for (int j = 0; j < limitNumber; j++) {
            TalendGridItem currentItem = new TalendGridItem(parentItem, SWT.NONE);
            currentItem.setCheckable(0, false);
            currentItem.setCheckable(1, false);
            for (int i = 2; i < this.getColumnCount(); i++) {
                GridColumn column = getColumn(i);
                currentItem.setCheckable(i, false);
                String columnValue = getColumnValue(i - 2, currentItem);
                if (columnValue == null) {
                    this.remove(this.indexOf(currentItem));
                    return;
                }
                int minWith = columnValue.length() * 7 > 100 ? 100 : columnValue.length() * 7;
                column.setWidth(COLUMN_WIDTH < minWith ? minWith : COLUMN_WIDTH);
                currentItem.setText(i, columnValue);
            }
        }
    }

    /**
     * DOC talend Comment method "checkSameTable".
     * 
     * @return
     */
    private boolean checkSameTable() {
        List<MetadataColumn> columnsList = ModelElementIndicatorHelper.getColumns(_modelElementIndicators);
        return ColumnHelper.checkSameTable(columnsList.toArray(new ModelElement[columnsList.size()]));
    }

    /**
     * DOC talend Comment method "getColumnValue".
     * 
     * @param column
     * @param currentItem
     * @return
     * @throws SQLException
     */
    private String getColumnValue(int columnNumber, GridItem currentItem) throws SQLException {
        // row index
        int indexOfRow = this.indexOf(currentItem);
        if (indexOfRow - 1 < previewData.size()) {
            Object[] objects = previewData.get(indexOfRow - 1);
            Object columnValue = objects[columnNumber];
            return columnValue == null ? PluginConstant.NULL_STRING : columnValue.toString();
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.utils.TalendObserver#Update(org.talend.dataprofiler.core.ui.grid.utils.
     * Observerable)
     */
    public void update(ObserverEvent observer) {
        switch (observer.getEventType()) {
        case ItemHeaderWidth:
            Object width = observer.getData(ObserverEvent.ITEM_HEADER_WIDTH);
            if (width == null) {
                return;
            }
            this.setItemHeaderWidth(Integer.parseInt(width.toString()));
            break;
        case HSrcollMove:
            Object selection = observer.getData(ObserverEvent.HORIZONTAL_SCROLLBAR_MOVE);
            if (selection == null) {
                return;
            }
            this.getHorizontalBar().setSelection(Integer.parseInt(selection.toString()));
            redraw(getClientArea().x, getClientArea().y, getClientArea().width, getClientArea().height, false);
            break;
        case VSrcollVisible:
            Object show = observer.getData(ObserverEvent.VERTICAL_SRCOLL_VISABLE);

            if (show == null || getVerticalBar() == null) {
                return;
            }
            if (!getVerticalBar().isVisible() && Boolean.parseBoolean(show.toString())) {
                // make current table bounds change to small
                GridData previewGridData = (GridData) this.getLayoutData();
                previewGridData.widthHint = this.getBounds().width - 50 - getVerticalBar().getSize().x;
                previewGridData.minimumWidth = this.getBounds().width - 50 - getVerticalBar().getSize().x;
                previewGridData.horizontalAlignment = SWT.BEGINNING;
                this.getParent().layout();
            }
            if (!getVerticalBar().isVisible() && !Boolean.parseBoolean(show.toString())) {
                // make current table bounds change to big
                GridData previewGridData = (GridData) this.getLayoutData();
                if (previewGridData.horizontalAlignment == SWT.FILL) {
                    return;
                }
                previewGridData.minimumWidth = 650;
                previewGridData.horizontalAlignment = SWT.FILL;
                notifyVerticalBarShown(false);
                this.getParent().layout();
            }

            break;
        case ColumnHighlight:
            Object data = observer.getData(ObserverEvent.COLUMN_HIGH_LIGHT);
            handleColumnHighlight(((Integer) data));
            break;
        default:
            break;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.AbstractIndicatorSelectGrid#getCellRenderer()
     */
    @Override
    protected GridCellRenderer getCellRenderer() {
        return new AbstractTdCellRenderer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.AbstractIndicatorSelectGrid#redrawTable()
     */
    @Override
    protected void redrawTable() {
        GridData previewGridData = (GridData) this.getLayoutData();
        if (previewGridData.minimumHeight == this.getHeaderHeight() + this.getItemHeight() * 2) {
            previewGridData.minimumHeight = this.getHeaderHeight() + this.getItemHeight() * 10;
            previewGridData.heightHint = this.getHeaderHeight() + this.getItemHeight() * 10;
        } else {
            previewGridData.minimumHeight = this.getHeaderHeight() + this.getItemHeight() * 2;
            previewGridData.heightHint = this.getHeaderHeight() + this.getItemHeight() * 2;
        }
        this.getParent().layout();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.grid.utils.Observerable#addObserver(org.talend.dataprofiler.core.ui.grid.utils
     * .TDQObserver)
     */
    public boolean addObserver(TDQObserver<ObserverEvent> observer) {
        initObserverable();
        return observers.add(observer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.grid.utils.Observerable#removeObserver(org.talend.dataprofiler.core.ui.grid.utils
     * .TDQObserver)
     */
    public boolean removeObserver(TDQObserver<ObserverEvent> observer) {
        if (observers == null) {
            return false;
        }
        return observers.remove(observer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.utils.Observerable#clearObserver()
     */
    public void clearObserver() {
        if (observers == null) {
            return;
        }
        observers.clear();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.utils.Observerable#notifyObservers()
     */
    public void notifyObservers() {
        // no need to implement

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.utils.Observerable#initObserverable()
     */
    public void initObserverable() {
        if (observers == null) {
            observers = new ArrayList<TDQObserver<ObserverEvent>>();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.grid.AbstractIndicatorSelectGrid#notifyObservers(org.eclipse.nebula.widgets.grid
     * .GridColumn)
     */
    @Override
    protected void notifyObservers(Event event) {
        if (observers == null) {
            return;
        }
        GridColumn sourceColumn = null;
        if (GridColumn.class.isInstance(event.item)) {
            sourceColumn = (GridColumn) event.item;
        } else {
            return;
        }

        for (TDQObserver<ObserverEvent> observer : observers) {
            ObserverEvent observerEvent = null;
            if (SWT.Resize == event.type) {
                observerEvent = new ObserverEvent(ObserverEventEnum.ColumnResize);
                observerEvent.putData(ObserverEvent.COLUMN_HEADER_RESIZE, sourceColumn);
            } else if (SWT.Move == event.type) {
                observerEvent = new ObserverEvent(ObserverEventEnum.MoveColumn);
                observerEvent.putData(ObserverEvent.COLUMN_HEADER_MOVE, this.getColumnOrder());
            } else {
                continue;
            }
            observer.update(observerEvent);
        }

    }

    /**
     * DOC talend Comment method "notifyVerticalBarVisible".
     * 
     * @param observer
     */
    @Override
    protected void notifyVerticalBarShown(boolean show) {
        if (observers == null) {
            return;
        }
        for (TDQObserver<ObserverEvent> observer : observers) {
            ObserverEvent observerEvent = new ObserverEvent(ObserverEventEnum.VSrcollVisible);
            observerEvent.putData(ObserverEvent.VERTICAL_SRCOLL_VISABLE, show);
            observer.update(observerEvent);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.AbstractIndicatorSelectGrid#notifyHscrollSelectionChange()
     */
    @Override
    protected void notifyHscrollSelectionChange() {
        if (observers == null) {
            return;
        }
        for (TDQObserver<ObserverEvent> observer : observers) {
            ObserverEvent observerEvent = new ObserverEvent(ObserverEventEnum.HSrcollMove);
            observerEvent.putData(ObserverEvent.HORIZONTAL_SCROLLBAR_MOVE, this.getHorizontalBar().getSelection());
            observer.update(observerEvent);
        }
    }

    @Override
    protected void notifyhandleColumnHighlight(MouseEvent e) {
        if (observers == null) {
            return;
        }
        for (TDQObserver<ObserverEvent> observer : observers) {
            ObserverEvent observerEvent = new ObserverEvent(ObserverEventEnum.ColumnHighlight);
            GridColumn currentColumn = this.getColumn(new Point(e.x, e.y));
            if (currentColumn != null) {
                observerEvent.putData(ObserverEvent.COLUMN_HIGH_LIGHT, this.indexOf(currentColumn));
                observer.update(observerEvent);
            }
        }
    }

    public int[] getColumnsWidth() {
        int[] result = new int[this.getColumnCount()];
        for (int index = 0; index < this.getColumnCount(); index++) {
            result[index] = this.getColumn(index).getWidth();
        }
        return result;
    }

    @Override
    protected int getPreferWidth(int index) {
        return COLUMN_WIDTH;
    }

}
