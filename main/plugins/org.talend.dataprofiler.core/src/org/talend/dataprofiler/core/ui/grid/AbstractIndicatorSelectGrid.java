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

import java.util.Arrays;
import java.util.List;

import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.TalendGrid;
import org.eclipse.nebula.widgets.grid.TalendGridColumn;
import org.eclipse.nebula.widgets.grid.TalendGridItem;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.utils.TableUtils;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.indicator.IIndicatorNode;
import org.talend.dq.nodes.indicator.IndicatorTreeModelBuilder;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;

/**
 * Abstract class for Grid control
 * 
 */
public abstract class AbstractIndicatorSelectGrid extends TalendGrid {

    private static final int FONT_HEIGHT = GUIHelper.DEFAULT_FONT.getFontData()[0].getHeight() > 8 ? (org.apache.commons.lang3.SystemUtils.IS_OS_MAC ? 12
            : 8)
            : GUIHelper.DEFAULT_FONT.getFontData()[0].getHeight();

    protected ModelElementIndicator[] _modelElementIndicators;

    protected IIndicatorSelectDialog _dialog;

    // the header(column label) font, its size is a little bigger than itemFont
    static final Font headerFont = new Font(Display.getCurrent(), GUIHelper.DEFAULT_FONT.getFontData()[0].getName(),
            FONT_HEIGHT + 1, SWT.NONE);

    static final Color gray = new Color(Display.getCurrent(), 240, 240, 240);

    static final Color highlightBlue = TableUtils.highlightBlue;

    static final Color blue = new Color(Display.getCurrent(), 90, 184, 235);

    static final Color lineColor = new Color(Display.getCurrent(), 180, 200, 220);

    // static final Image tickImage = ImageLib.getImage(ImageLib.TICK_IMAGE);

    // static final Image indImage = ImageLib.getImage(ImageLib.IND_DEFINITION);

    // static final Image patternImage = ImageLib.getImage(ImageLib.PATTERN_REG);

    // static final Image pkImage = ImageLib.getImage(ImageLib.PK_ICON);

    static final int COLUMN_WIDTH = 50;

    static final int COLUMN_HEADER_ROTATION = 35;

    static final int LIMIT_NUMBER_DEFAULT = 20;

    // TDQ-9725: set the item font with a fixed size. the same to DataSampleTable.font
    static final Font itemFont = new Font(Display.getCurrent(), GUIHelper.DEFAULT_FONT.getFontData()[0].getName(), FONT_HEIGHT,
            SWT.NONE);

    private double tanRotation;

    protected boolean isColumnMoved = false;

    protected ModelElementIndicator[] result;

    protected int limitNumber = LIMIT_NUMBER_DEFAULT;

    private int lastHScrollSelectionLocation = 0;

    protected List<Object[]> previewData;

    /**
     * IndicatorSelectionGrid constructor.
     * 
     * @param parent
     * @param style
     * @param modelElementIndicators
     * @param modelElementIndicators
     */
    public AbstractIndicatorSelectGrid(IndicatorSelectDialog dialog, Composite parent, int style,
            ModelElementIndicator[] modelElementIndicators, int limit, List<Object[]> previewData) {
        super(parent, style);
        limitNumber = limit;
        this.previewData = previewData;
        _dialog = dialog;
        _modelElementIndicators = modelElementIndicators;
        addExtraListeners();
        tanRotation = Math.tan(Math.PI * COLUMN_HEADER_ROTATION / 180);

    }

    /**
     * DOC talend Comment method "addVscrollBarListener".
     */
    protected void addVscrollBarListener() {
        getVerticalBar().addListener(SWT.Show, new Listener() {

            public void handleEvent(Event event) {
                fillWidth();
                if (getVerticalBar().isVisible()) {
                    notifyVerticalBarShown(true);
                }
                // notify the gridIndicator vScrollBar has shown

            }

        });
        getVerticalBar().addListener(SWT.Hide, new Listener() {

            public void handleEvent(Event event) {
                beginningWidth();
                if (!getVerticalBar().isVisible()) {
                    notifyVerticalBarShown(false);
                }
                // notify the gridIndicator vScrollBar has shown

            }

        });

    }

    /**
     * Fill the content when width is changed
     */
    protected void fillWidth() {
        GridData previewGridData = (GridData) this.getLayoutData();
        previewGridData.horizontalAlignment = SWT.FILL;
        previewGridData.minimumWidth = 650;
        this.getParent().layout();

    }

    /**
     * The width of begin
     */
    protected void beginningWidth() {
        GridData previewGridData = (GridData) this.getLayoutData();
        previewGridData.widthHint = this.getBounds().width - 50 - getVerticalBar().getSize().x;
        previewGridData.minimumWidth = this.getBounds().width - 50 - getVerticalBar().getSize().x;
        previewGridData.horizontalAlignment = SWT.BEGINNING;
        this.getParent().layout();
    }

    protected void notifyVerticalBarShown(boolean b) {

    }

    /**
     * IndicatorSelectionGrid constructor.
     * 
     * @param parent
     * @param style
     * @param modelElementIndicators
     * @param modelElementIndicators
     */
    public AbstractIndicatorSelectGrid(IndicatorSelectDialog dialog, Composite parent, int style,
            ModelElementIndicator[] modelElementIndicators, List<Object[]> previewData) {
        this(dialog, parent, style, modelElementIndicators, LIMIT_NUMBER_DEFAULT, previewData);
    }

    protected void initializeGrid() {

        // first column is for indicator labels, it is hided from the cells but shown as row header.
        createIndicatorLabelColumn();
        // column

        // select all column
        createRowSelectColumn();

        // database columns
        for (int index = 0; index < _modelElementIndicators.length; index++) {
            ModelElementIndicator _modelElementIndicator = _modelElementIndicators[index];
            final GridColumn newCol = new TalendGridColumn(this, SWT.CHECK);
            AbstractColumnHerderRenderer headerRenderer = getColumnHeaderRenderer();
            headerRenderer.setRotation(COLUMN_HEADER_ROTATION);
            newCol.setHeaderRenderer(headerRenderer);
            newCol.setCellRenderer(getCellRenderer());
            newCol.setText(ModelElementIndicatorHelper.getModelElementDisplayName(_modelElementIndicator));
            newCol.setWidth(getPreferWidth(index));
            newCol.setData(_modelElementIndicator);
            newCol.setMoveable(true);
            newCol.setResizeable(true);
            newCol.setHeaderFont(headerFont);
            IRepositoryNode repNode = _modelElementIndicator.getModelElementRepositoryNode();
            if (repNode instanceof DBColumnRepNode && ((DBColumnRepNode) repNode).isKey()) {
                newCol.setImage(ImageLib.getImage(ImageLib.PK_ICON));
            }
            newCol.addListener(SWT.Resize, new Listener() {

                public void handleEvent(Event event) {
                    notifyObservers(event);
                }

            });
            newCol.addListener(SWT.Move, new Listener() {

                public void handleEvent(Event event) {
                    notifyObservers(event);
                    isColumnMoved = true;
                }

            });
        }
        recalculateHeader();
        // initialize grid contents
        createTableContent();

        // show fixed column header
        setHeaderVisible(true);
        setTopLeftRenderer(new TdTopLeftRenderer());
        // setCellHeaderSelectionBackground(IndicatorSelectGrid.standardYellow);

        setEmptyColumnHeaderRenderer(new TdEmptyColumnHeaderRenderer());
        setEmptyRowHeaderRenderer(new TdEmptyCellRenderer());
        setEmptyCellRenderer(new TdEmptyCellRenderer());

        // show fixed row header
        TdRowHeaderRenderer rowHeaderRenderer = new TdRowHeaderRenderer();
        setRowHeaderRenderer(rowHeaderRenderer);
        rowHeaderRenderer.setTree(true);
        rowHeaderRenderer.setWordWrap(false);
        setRowHeaderVisible(true);

        setLinesVisible(true);
        setColumnScrolling(true);
        setSelectionEnabled(false);
        setCellSelectionEnabled(false);

        setRowsResizeable(false);
        setItemHeight(21);
        setLineColor(lineColor);
        setFont(itemFont);
        setFocusRenderer(null);

        for (GridItem gridItem : getItems()) {
            gridItem.setBackground(0, gray);
        }
    }

    protected abstract int getPreferWidth(int index);

    /**
     * 
     * Notify the column has been resized
     * 
     * @param newCol
     */
    protected void notifyObservers(Event event) {
        // Implement it in the sub class
    }

    /**
     * DOC talend Comment method "getCellRenderer".
     * 
     * @return
     */
    protected GridCellRenderer getCellRenderer() {
        return new TdCellRenderer();
    }

    /**
     * Create the Content of table
     */
    protected void createTableContent() {
        IIndicatorNode[] branchNodes = IndicatorTreeModelBuilder.buildIndicatorCategory();
        for (IIndicatorNode indicatorNode : branchNodes) {
            TalendGridItem item = new TalendGridItem(this, SWT.NONE);
            item.setText(indicatorNode.getLabel());
            item.setData(indicatorNode);
            createChildNodes(null, item, indicatorNode);
            processNodeSelection(null, item);
        }
    }

    /**
     * DOC talend Comment method "getColumnHeaderRenderer".
     * 
     * @return
     */
    protected AbstractColumnHerderRenderer getColumnHeaderRenderer() {
        return new TdColumnHeaderRenderer();
    }

    /**
     * This column just used to take in space so that visible is false
     */
    protected void createRowSelectColumn() {
        GridColumn rowSelectCol = new TalendGridColumn(this, SWT.CHECK);
        rowSelectCol.setHeaderRenderer(getColumnHeaderRenderer());
        rowSelectCol.setCellRenderer(getCellRenderer());
        rowSelectCol.setText("Select All"); //$NON-NLS-1$
        rowSelectCol.setWidth(COLUMN_WIDTH);
        rowSelectCol.setWordWrap(true);
        rowSelectCol.setCellSelectionEnabled(true);
        rowSelectCol.setVisible(false); // hide the row select column, it is also visible in the fixed column.
    }

    /**
     * This column just used to take in space so that visible is false
     */
    protected void createIndicatorLabelColumn() {
        GridColumn indicatorLabelColumn = new TalendGridColumn(this, SWT.NONE);
        indicatorLabelColumn.setHeaderRenderer(getColumnHeaderRenderer());
        indicatorLabelColumn.setTree(true);
        indicatorLabelColumn.setWidth(200);
        indicatorLabelColumn.setText("Indicators"); //$NON-NLS-1$
        indicatorLabelColumn.setVisible(false); // hide the label column, but it is actually visible in the fixed
    }

    /**
     * DOC root Comment method "addExtraListener".
     */
    private void addExtraListeners() {
        // remove double click event from parent class
        this.removeListener(SWT.MouseDoubleClick, this.getListeners(SWT.MouseDoubleClick)[0]);
        // addMouseTrackListener(new MouseTrackListener() {
        //
        // public void mouseEnter(MouseEvent e) {
        // }
        //
        // public void mouseExit(MouseEvent e) {
        // if (isScrolling) {
        // isScrolling = false;
        // Display.getDefault().timerExec(-1, thread); // interrupt the thread
        // }
        // }
        //
        // public void mouseHover(MouseEvent e) {
        // }
        //
        // });

        addMouseMoveListener(new MouseMoveListener() {

            public void mouseMove(MouseEvent e) {
                onMouseMove(e);
            }
        });

        addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
                onMouseDown(e);
            }

            public void mouseUp(MouseEvent e) {

            }

        });

        // addDisposeListener(new DisposeListener() {
        //
        // public void widgetDisposed(DisposeEvent e) {
        // if (isScrolling) {
        // isScrolling = false;
        // Display.getDefault().timerExec(-1, thread); // interrupt the thread
        // }
        // }
        // });
    }

    private void onMouseDown(MouseEvent e) {
        Point cell = getCell(new Point(e.x, e.y));
        if (cell != null) {
            boolean checked = getItem(cell.y).getChecked(cell.x);
            boolean grayed = getItem(cell.y).getGrayed(cell.x);
            tickCell(cell, !checked || grayed);
            GridItem parent = getItem(cell.y);
            while (parent.getParentItem() != null) {
                parent = parent.getParentItem();
            }
            processNodeSelection(null, parent);
        } else {
            GridItem item = getItem(new Point(e.x, e.y));
            if (e.button == 1 && item != null) {
                if (overRowSelect(item, new Point(e.x, e.y))) {
                    boolean rowChecked = item.getChecked(1);
                    boolean rowGrayed = item.getGrayed(1);
                    tickCell(new Point(1, getIndexOfItem(item)), !rowChecked || rowGrayed);
                    GridItem parent = item;
                    while (parent.getParentItem() != null) {
                        parent = parent.getParentItem();
                    }
                    processNodeSelection(null, parent);
                    if (item.getParentItem() == null && rowChecked && !rowGrayed) {
                        item.setExpanded(false);
                    }
                } else {
                    if (item.hasChildren()) {
                        item.setExpanded(!item.isExpanded());
                    }
                    TdRowHeaderRenderer renderer = ((TdRowHeaderRenderer) getRowHeaderRenderer());
                    e.x = renderer.getSize().x - 1; // Move into row select cell
                    onMouseMove(e);
                    _dialog.updateIndicatorInfo(item);
                    if (item.getParentItem() == null) {
                        redrawTable();
                    }
                }
            }
        }
    }

    /**
     * DOC talend Comment method "redrawPreviewTable".
     */
    protected void redrawTable() {
        this.getParent().layout();

    }

    private void onMouseMove(MouseEvent e) {
        // if (handleMouseScroll(e)) { // when the grid is scrolling, do not handle mouse move highlight.
        // return;
        // }

        GridVisibleRange range = getVisibleRange();
        if (handleCellHighlight(e, range)) {
            notifyhandleColumnHighlight(e);
            return;
        }

        if (handleRowHeaderHighlight(e, range)) {
            return;
        }

        handleColumnHeaderHighlight(e, range);
        notifyhandleColumnHighlight(e);
    }

    protected void notifyhandleColumnHighlight(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    private class HoverScrollThread extends Thread {

        int _step = 0;

        int delay = 350;

        int accelaration = 0;

        private ScrollBar _hScrollBar;

        public HoverScrollThread(int step, ScrollBar hScrollBar) {
            _step = step;
            _hScrollBar = hScrollBar;
        }

        @Override
        public void run() {
            _hScrollBar.setSelection(_hScrollBar.getSelection() + _step);
            notifyHscrollSelectionChange();
            redraw();
            Display.getDefault().timerExec(delay - accelaration * 2, this);
        }

        public void setAccelaration(int x) {
            accelaration = x;
        }
    }

    // private boolean handleMouseScroll(MouseEvent e) {
    // if (e.x > getRowHeaderWidth() && e.x < getRowHeaderWidth() + 150) {
    // ScrollBar hScrollBar = getHorizontalBar();
    // if (hScrollBar.getSelection() == hScrollBar.getMinimum()) {
    // return false;
    // }
    // if (!isScrolling && e.x < getRowHeaderWidth() + 100) {
    // isScrolling = true;
    // thread = new HoverScrollThread(-1, hScrollBar);
    // Display.getDefault().timerExec(200, thread);
    // handleCellHighlight(e, getVisibleRange());
    // }
    // if (isScrolling) {
    // thread.setAccelaration(getRowHeaderWidth() + 150 - e.x);
    // startColumnIndex = -1;
    // endColumnIndex = -1;
    // return true;
    // }
    // } else if (e.x > getBounds().width - 150 && e.x < getBounds().width) {
    // ScrollBar hScrollBar = getHorizontalBar();
    // if (!isScrolling && e.x > getBounds().width - 100) {
    // isScrolling = true;
    // thread = new HoverScrollThread(1, hScrollBar);
    // Display.getDefault().timerExec(200, thread);
    // handleCellHighlight(e, getVisibleRange());
    // }
    // if (isScrolling) {
    // thread.setAccelaration(e.x + 150 - getBounds().width);
    // startColumnIndex = -1;
    // endColumnIndex = -1;
    // return true;
    // }
    // } else {
    // if (isScrolling) {
    // isScrolling = false;
    // Display.getDefault().timerExec(-1, thread); // interrupt the thread
    // }
    // }
    // return false;
    // }

    private boolean handleCellHighlight(MouseEvent e, GridVisibleRange range) {
        Point cell = getCell(new Point(e.x, e.y));
        if (cell != null) { // any cell except the row select cells
            List<GridColumn> columnList = Arrays.asList(range.getColumns());
            // replace cell.x with the current position in case the column has been moved.
            cell.x = columnList.indexOf(getColumn(cell.x)) + 2;
            for (GridItem item : range.getItems()) {
                int i = indexOf(item);
                // set background for row headers
                if (i == cell.y) {
                    item.setBackground(0, highlightBlue);
                    item.setBackground(1, highlightBlue);
                } else {
                    item.setBackground(0, gray);
                    item.setBackground(1, getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                    if (item.getCheckable(1)) {
                        item.setBackground(highlightBlue);
                    }
                }

                // set background for cells
                for (GridColumn column : range.getColumns()) {
                    int realIdx = columnList.indexOf(column) + 2; // real index in current visible range.
                    int j = indexOf(column); // the original index to be colored.
                    if (i == cell.y && realIdx == cell.x) {
                        item.setBackground(j, highlightBlue);
                    } else if (i == cell.y && realIdx < cell.x || realIdx == cell.x && i < cell.y) {
                        item.setBackground(j, highlightBlue);
                    } else {
                        item.setBackground(j, null);
                    }
                }
            }
            // set background for column headers
            for (GridColumn column : range.getColumns()) {
                int realIdx = columnList.indexOf(column) + 2; // real index in current visible range.
                column.getHeaderRenderer().setSelected(realIdx == cell.x);
            }
            return true;
        }
        return false;
    }

    private boolean handleRowHeaderHighlight(MouseEvent e, GridVisibleRange range) {
        GridItem currentItem = getItem(new Point(e.x, e.y));
        if (currentItem != null) { // row header
            if (overRowSelect(currentItem, new Point(e.x, e.y))) { // handle hover event of row select cell
                for (GridItem item : range.getItems()) {
                    int i = indexOf(item);
                    if (item.getCheckable(0)) {
                        if (i == indexOf(currentItem)) {
                            item.setBackground(0, highlightBlue);
                            item.setBackground(1, highlightBlue);
                        } else {
                            item.setBackground(0, gray);
                            item.setBackground(1, getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                        }
                    }

                    for (GridColumn column : range.getColumns()) {
                        int j = indexOf(column);
                        if (i == indexOf(currentItem)) {
                            item.setBackground(j, highlightBlue);
                        } else {
                            item.setBackground(j, getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                        }
                    }
                }
                for (GridColumn column : range.getColumns()) {
                    column.getHeaderRenderer().setSelected(false);
                }
            }
            return true;
        }
        return false;
    }

    private void handleColumnHeaderHighlight(MouseEvent e, GridVisibleRange range) {
        GridColumn currentColumn = getColumn(new Point(e.x, e.y));
        if (currentColumn != null && !isDraggingColumn()) {
            int currentColumnIndex = indexOf(currentColumn);

            for (GridItem item : range.getItems()) {
                for (GridColumn column : range.getColumns()) {
                    int j = indexOf(column);
                    if (j == currentColumnIndex) {
                        item.setBackground(j, highlightBlue);
                    } else {
                        item.setBackground(j, getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                    }
                }
                item.setBackground(0, gray);
                item.setBackground(1, null);
            }

            for (GridColumn column : range.getColumns()) {
                int j = indexOf(column);
                column.getHeaderRenderer().setSelected(j == currentColumnIndex);
            }
        }
    }

    protected void handleColumnHighlight(int columnIndex) {
        GridColumn currentColumn = getColumn(columnIndex);
        GridVisibleRange range = this.getVisibleRange();
        if (currentColumn != null && !isDraggingColumn()) {
            int currentColumnIndex = columnIndex;

            for (GridItem item : range.getItems()) {
                for (GridColumn column : range.getColumns()) {
                    int j = indexOf(column);
                    if (j == currentColumnIndex) {
                        item.setBackground(j, highlightBlue);
                    } else {
                        item.setBackground(j, getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                    }
                }
                item.setBackground(0, gray);
                item.setBackground(1, null);
            }

            for (GridColumn column : range.getColumns()) {
                int j = indexOf(column);
                column.getHeaderRenderer().setSelected(j == currentColumnIndex);
            }
        }
    }

    private boolean overRowSelect(GridItem item, Point point) {

        point = new Point(point.x, point.y);
        point.y -= item.getBounds(0).y;

        int x = getRowHeaderWidth();
        if (point.x > x - 50 && point.x < x) {
            if (point.y > 0 && point.y < getItemHeight()) {
                return true;
            }
        }

        return false;
    }

    private void tickCell(Point cell, boolean tick) {
        if (!getItem(cell.y).getCheckable(cell.x)) {
            return;
        }

        if (cell.x == 1) {
            for (int i = 2; i < getColumnCount(); i++) {
                tickCell(new Point(i, cell.y), tick);
            }
        }

        getItem(cell.y).setChecked(cell.x, tick);
        getItem(cell.y).setGrayed(cell.x, false);

        IIndicatorNode indicatorNode = (IIndicatorNode) getItem(cell.y).getData();
        IndicatorEnum indicatorEnum = indicatorNode.getIndicatorEnum();
        ModelElementIndicator meIndicator = (ModelElementIndicator) getColumn(cell.x).getData();
        if (meIndicator != null && indicatorEnum != null) {
            if (tick) {
                if (IndicatorEnum.isSpecialIndicatorEnum(indicatorEnum)) {

                    meIndicator.addTempSpecialIndicator(indicatorEnum, indicatorNode.createNewIndicatorInstance());
                } else {
                    meIndicator.addTempIndicatorEnum(indicatorEnum);
                }
            } else {
                if (IndicatorEnum.isSpecialIndicatorEnum(indicatorEnum)) {
                    meIndicator.removeTempSpecialIndicator(indicatorNode.getIndicatorInstance());
                } else {
                    meIndicator.removeTempIndicatorEnum(indicatorEnum);
                }
            }
        }
        // select the entire indicator category
        if (getItem(cell.y).hasChildren()) {
            for (GridItem child : getItem(cell.y).getItems()) {
                tickCell(new Point(cell.x, indexOf(child)), tick);
            }
        }
    }

    /**
     * recursively create tree nodes and checked the existing indicators.
     * 
     * @param grid
     * @param currentItem
     * @param indicatorNode
     */
    void createChildNodes(GridItem parentItem, GridItem currentItem, IIndicatorNode indicatorNode) {

        Boolean hasCheckableInColumn[] = new Boolean[getColumnCount()];

        for (int j = 1; j < getColumnCount(); j++) {
            hasCheckableInColumn[j] = false;
        }

        for (int i = 0; i < indicatorNode.getChildren().length; i++) {
            IIndicatorNode childNode = indicatorNode.getChildren()[i];
            TalendGridItem childItem = new TalendGridItem(currentItem, SWT.NONE);
            childItem.setImage(ImageLib.getImage(childNode.getImageName()));

            if (childNode.hasChildren()) {
                createChildNodes(currentItem, childItem, childNode);
            }

            childItem.setText(childNode.getLabel());
            childItem.setData(childNode);
            if (parentItem == null) {
                childItem.setExpanded(true);
            }

            boolean hasCheckableInRow = false;

            for (int j = 2; j < getColumnCount(); j++) { // ignore indicator label column and row select column

                IndicatorEnum indicatorEnum = childNode.getIndicatorEnum();

                // DB columns
                ModelElementIndicator meIndicator = null;
                if (j - 2 < _modelElementIndicators.length) {
                    meIndicator = _modelElementIndicators[j - 2];
                } else {
                    meIndicator = _modelElementIndicators[0];
                }

                // Enable/disable the check box
                boolean isMatch = _dialog.isMatchCurrentIndicator(meIndicator, childNode);
                childItem.setCheckable(j, isMatch);

                if (isMatch) {
                    hasCheckableInRow = true;
                    hasCheckableInColumn[j] = true;
                    // Check the box if it is already selected
                    if (meIndicator != null
                            && (meIndicator.tempContains(indicatorEnum) || meIndicator.specialTempContains(childNode
                                    .getIndicatorInstance()))) {
                        childItem.setChecked(j, true);
                    }
                }
            }

            childItem.setCheckable(1, hasCheckableInRow);
        }

        boolean entireCategoryCheckable = false;
        for (int j = 2; j < getColumnCount(); j++) {
            if (hasCheckableInColumn[j]) {
                entireCategoryCheckable = true;
            } else {
                currentItem.setCheckable(j, false);
            }
        }
        currentItem.setCheckable(1, entireCategoryCheckable);
    }

    /**
     * recursively check if a entire row/column is selected/
     * 
     * @param grid
     * @param parentItem
     * @param currentItem
     */
    void processNodeSelection(GridItem parentItem, GridItem currentItem) {
        if (currentItem.hasChildren()) {
            // declare and initialize variables
            Boolean allCheckedInColumn[] = new Boolean[getColumnCount()];
            Boolean hasCheckedInColumn[] = new Boolean[getColumnCount()];
            for (int j = 1; j < getColumnCount(); j++) {
                allCheckedInColumn[j] = true;
                hasCheckedInColumn[j] = false;
            }

            for (int i = 0; i < currentItem.getItemCount(); i++) {
                GridItem childItem = currentItem.getItem(i);
                // process the children of current item, this must be done before handling the current item
                processNodeSelection(currentItem, childItem);

                boolean allCheckedInRow = true;
                boolean hasCheckedInRow = false;
                boolean expanded = false;

                for (int j = 2; j < getColumnCount(); j++) {
                    if (childItem.getChecked(j)) {
                        hasCheckedInRow = true;
                        hasCheckedInColumn[j] = true;
                        expanded = true;
                        if (childItem.getGrayed(j)) {
                            allCheckedInRow = false;
                            allCheckedInColumn[j] = false;
                        }
                    } else {
                        if (childItem.getCheckable(j)) {
                            allCheckedInRow = false;
                            allCheckedInColumn[j] = false;
                        }
                    }
                }
                childItem.setChecked(1, hasCheckedInRow);
                childItem.setGrayed(1, hasCheckedInRow && !allCheckedInRow);

                if (expanded) {
                    currentItem.setExpanded(true);
                }

            }

            // process the selections of indicator category row
            boolean entireCategoryChecked = true;
            for (int j = 2; j < getColumnCount(); j++) {
                if (currentItem.getCheckable(j)) {
                    if (hasCheckedInColumn[j]) {
                        hasCheckedInColumn[1] = true;
                        currentItem.setChecked(j, true);
                    } else {
                        currentItem.setChecked(j, false);
                    }
                    if (!allCheckedInColumn[j]) {
                        currentItem.setGrayed(j, hasCheckedInColumn[j]);
                        entireCategoryChecked = false;
                    } else {
                        currentItem.setGrayed(j, false);
                    }
                    // MOD qiongli 2012-11-28 TDQ-6211 we should have the range indicator as soon as the min AND the max
                    // are selected
                    if (hasCheckedInColumn[j] && allCheckedInColumn[j]) {
                        IIndicatorNode indicatorNode = (IIndicatorNode) currentItem.getData();
                        IndicatorEnum indicatorEnum = indicatorNode.getIndicatorEnum();
                        ModelElementIndicator meIndicator = (ModelElementIndicator) getColumn(j).getData();
                        if (indicatorEnum != null
                                && meIndicator != null
                                && (indicatorEnum == IndicatorEnum.RangeIndicatorEnum || indicatorEnum == IndicatorEnum.IQRIndicatorEnum)) {
                            meIndicator.addTempIndicatorEnum(indicatorEnum);
                        }
                    }
                }
            }
            if (currentItem.getCheckable(1)) {
                currentItem.setChecked(1, hasCheckedInColumn[1]);
                currentItem.setGrayed(1, hasCheckedInColumn[1] && !entireCategoryChecked);
            }
        }
    }

    /**
     * select/deselect all enabled indicators when Ctrl+Shift+[A|N] is down.
     * 
     * @param select
     */
    public void setAllIndicators(boolean select) {
        for (int i = 0; i < getItemCount(); i++) {
            for (int j = 1; j < getColumnCount(); j++) {
                if (getItem(i).getParentItem() == null) {
                    tickCell(new Point(j, i), select);
                    getItem(i).setExpanded(select);
                }
            }
        }
    }

    @Override
    public GridColumn getColumn(Point point) {
        if (point.y < getHeaderHeight()) {
            return super.getColumn(new Point(point.x - (int) ((getHeaderHeight() - point.y) / tanRotation), point.y));
        } else {
            return super.getColumn(point);
        }
    }

    @Override
    protected boolean handleColumnDragging(int x, int y) {
        if (y < getHeaderHeight()) {
            return super.handleColumnDragging(x - (int) ((getHeaderHeight() - y) / tanRotation), y);
        } else {
            return super.handleColumnDragging(x, y);
        }
    }

    @Override
    protected void handleColumnDrop() {
        super.handleColumnDrop();
        if (result == null) {
            result = new ModelElementIndicator[getColumnCount() - 2];
        }
        int[] order = getColumnOrder();
        int j = 0;
        for (int columnIndex : order) {
            if (columnIndex > 1) {
                result[j] = _modelElementIndicators[columnIndex - 2]; // indicator selection starts from the 3rd column
                j++;
            }
        }
    }

    @Override
    protected void paintDraggingColumn(GC gc, int offset, int alpha) {
        super.paintDraggingColumn(gc, 0, 180);
    }

    @Override
    protected int getHScrollSelectionInPixels() {

        ScrollBar hScrollBar = getHorizontalBar();
        int selection = hScrollBar.getSelection();
        if (getColumnScrolling()) {
            int pixels = 0;
            for (int i = 0; i < selection; i++) {
                int[] columnDisOrder = this.getColumnOrder();
                pixels += this.getColumn(columnDisOrder[i + 2]).getWidth();
            }
            selection = pixels;
        }
        return selection;
    }

    public ModelElementIndicator[] getResult() {
        if (result == null) {
            return _modelElementIndicators;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.grid.Grid#refreshHoverState()
     */
    @Override
    protected void refreshHoverState() {
        super.refreshHoverState();
        if (IsHScrollInUsed()) {
            notifyHscrollSelectionChange();
        }
        this.lastHScrollSelectionLocation = this.getHorizontalBar().getSelection();
    }

    /**
     * DOC talend Comment method "notifyHscrollSelectionChange".
     */
    protected void notifyHscrollSelectionChange() {
        // no need to implement

    }

    /**
     * Judge whether is HScroll is in used
     * 
     * @return
     */
    private boolean IsHScrollInUsed() {
        return lastHScrollSelectionLocation != this.getHorizontalBar().getSelection();
    }
}
