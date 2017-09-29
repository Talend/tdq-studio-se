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
package org.talend.dq.indicators.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.utils.collections.MultipleKey;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.NumberEdgeValue;
import edu.uci.ics.jung.graph.decorators.NumberVertexValue;
import edu.uci.ics.jung.graph.decorators.UserDatumNumberEdgeValue;
import edu.uci.ics.jung.graph.decorators.UserDatumNumberVertexValue;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseVertex;
import edu.uci.ics.jung.utils.UserData;

/**
 * @author scorreia
 * 
 * Class to help building a graph from data.
 */
public class GraphBuilder {

    /**
     * The key to set the index of a column used to identify instances of a same column.
     */
    public static final String COLUMN_IDX_KEY = "COLUMNID"; //$NON-NLS-1$

    /**
     * The key to set the label of a vertex (instance of a column).
     */
    public static final String V_LABEL_KEY = "V_LABEL"; //$NON-NLS-1$

    /**
     * The key to set the index of the row in the edge.
     */
    public static final String E_ROWNUM_KEY = "ROWID"; //$NON-NLS-1$

    /**
     * The key to set the weight of the edge (function of the row count).
     */
    public static final String E_WEIGHT_KEY = "WEIGHT"; //$NON-NLS-1$

    /**
     * The key to set the label of the edge.
     */
    public static final String E_LABEL_KEY = "E_LABEL"; //$NON-NLS-1$

    /**
     * the graph.
     */
    private Graph graph;

    protected NumberEdgeValue edgeWeight;

    private long totalWeight;
    
    boolean proportionalWidth = false;

    public void setProportionalWidth(boolean proportionalWidth) {
        this.proportionalWidth = proportionalWidth;
    }

    /**
     * Getter for totalWeight.
     * 
     * @return the totalWeight
     */
    public long getTotalWeight() {
        return this.totalWeight;
    }

    /**
     * Sets the totalWeight.
     * 
     * @param totalWeight the totalWeight to set
     */
    public void setTotalWeight(long totalWeight) {
        this.totalWeight = totalWeight;
    }

    /**
     * Getter for edgeWeight.
     * 
     * @return the edgeWeight
     */
    public NumberEdgeValue getEdgeWeight() {
        return this.edgeWeight;
    }

    /**
     * Getter for vertexWeight.
     * 
     * @return the vertexWeight
     */
    public NumberVertexValue getVertexWeight() {
        return this.vertexWeight;
    }

    protected NumberVertexValue vertexWeight;

    /**
     * Method "createMultiGraph".
     * 
     * @param listOfRows a list of rows. Each row contains instance values. The last column gives the number of
     * instances of the row.
     * @return the graph
     */
    public Graph createMultiGraph(List<Object[]> listOfRows) {
        initAttributes();
        createVertices(listOfRows);
        return graph;
    }

    private void initAttributes() {
        graph = new UndirectedSparseGraph();
        graph.getEdgeConstraints().remove(Graph.NOT_PARALLEL_EDGE); // allow multigraph
        edgeWeight = new UserDatumNumberEdgeValue("edge_weight"); //$NON-NLS-1$
        vertexWeight = new UserDatumNumberVertexValue("vertex_weight"); //$NON-NLS-1$
    }

    /**
     * create vertices from given list of rows.
     * 
     * @param listOfRows list of rows
     * @return true
     */
    private boolean createVertices(List<Object[]> listOfRows) {
        // map [key -> vertex] to store created vertices
        Map<MultipleKey, Vertex> key2Vertex = new HashMap<MultipleKey, Vertex>();
        // link each vertex of the row together
        Map<MultipleKey, Edge> v1v2ToEdge = new HashMap<MultipleKey, Edge>();

        // loop on each row
        for (int rowIdx = 0; rowIdx < listOfRows.size(); rowIdx++) {
            Object[] row = listOfRows.get(rowIdx);
            createVertices(key2Vertex, v1v2ToEdge, row, rowIdx);
        }
        return true;
    }

    /**
     * DOC scorreia Comment method "createVertices".
     * 
     * @param key2Vertex
     * @param toEdge
     * @param row
     * @param rowIdx
     * @return
     */
    private Vertex[] createVertices(Map<MultipleKey, Vertex> key2Vertex, Map<MultipleKey, Edge> toEdge, Object[] row, int rowIdx) {

        // last column is the result of count(*)
        final int nbNominalColumn = row.length - 1;

        // vertices in one row
        Vertex[] verticesInRow = new Vertex[nbNominalColumn];
        for (int colIdx = 0; colIdx < nbNominalColumn; colIdx++) {
            final Object value = row[colIdx];
            Object[] key = { value, String.valueOf(colIdx) };
            MultipleKey mulkey = new MultipleKey(key, 2);
            // search for already created vertex
            Vertex myVertex = key2Vertex.get(mulkey);
            // create the new vertex
            if (myVertex == null) {
                myVertex = newVertex(key2Vertex, colIdx, value, mulkey);
                vertexWeight.setNumber(myVertex, getIntegerValue(row[nbNominalColumn]));
            } else { // existing vertex
                vertexWeight.setNumber(myVertex, vertexWeight.getNumber(myVertex).intValue()
                        + getIntegerValue(row[nbNominalColumn]));
            }
            verticesInRow[colIdx] = myVertex;
        }
        for (int i = 0; i < verticesInRow.length; i++) {
            Vertex vi = verticesInRow[i];
            for (int j = 0; j < i; j++) {
                Vertex vj = verticesInRow[j];
                MultipleKey mulkey = new MultipleKey(new Object[] { vi, vj }, 2);
                Edge edge = toEdge.get(mulkey);
                if (edge == null) {
                    // either allow parallel edges or avoid create a new edge when it already exists
                    edge = newEdge(row, rowIdx, nbNominalColumn, vi, vj);
                    toEdge.put(mulkey, edge);
                } else {
                    updateEdge(edge, row, rowIdx, nbNominalColumn, vi, vj);
                }
            }
        }
        return verticesInRow;
    }

    /**
     * DOC scorreia Comment method "getIntegerValue".
     * 
     * @param object
     * @return
     */
    private Integer getIntegerValue(Object object) {
        assert object != null;
        return (object instanceof Integer) ? (Integer) object : IndicatorHelper.getIntegerFromObject(String.valueOf(object));
    }

    /**
     * DOC scorreia Comment method "newEdge".
     * 
     * @param row
     * @param rowIdx
     * @param countColumnIndex
     * @param vertexFrom
     * @param vertexTo
     * @return
     */
    private Edge newEdge(Object[] row, int rowIdx, final int countColumnIndex, Vertex vertexFrom, Vertex vertexTo) {
        final Edge edge = graph.addEdge(new UndirectedSparseEdge(vertexFrom, vertexTo));
        String fullRow = StringUtils.join(row, " , "); //$NON-NLS-1$
        edge.addUserDatum(GraphBuilder.E_ROWNUM_KEY, rowIdx, UserData.CLONE);
        // TODO remove this line ?
        edge.addUserDatum(GraphBuilder.E_WEIGHT_KEY, getIntegerValue(row[countColumnIndex]), UserData.CLONE);
        edge.addUserDatum(E_LABEL_KEY, fullRow, UserData.SHARED);
        edgeWeight.setNumber(edge, getIntegerValue(row[countColumnIndex]));
        return edge;
    }

    /**
     * DOC scorreia Comment method "updateEdge".
     * 
     * @param edge
     * @param row
     * @param rowIdx
     * @param countColumnIndex
     * @param vi
     * @param vj
     */
    private void updateEdge(Edge edge, Object[] row, int rowIdx, int countColumnIndex, Vertex vi, Vertex vj) {
        edgeWeight.setNumber(edge, edgeWeight.getNumber(edge).intValue() + getIntegerValue(row[countColumnIndex]));
    }

    /**
     * DOC scorreia Comment method "newVertex".
     * 
     * @param key2Vertex
     * @param columnIndex
     * @param value
     * @param multikey
     * @return
     */
    private Vertex newVertex(Map<MultipleKey, Vertex> key2Vertex, int columnIndex, final Object value, MultipleKey multikey) {
        Vertex myVertex;
        myVertex = new UndirectedSparseVertex();
        myVertex.addUserDatum(GraphBuilder.COLUMN_IDX_KEY, columnIndex, UserData.CLONE);
        myVertex.addUserDatum(GraphBuilder.V_LABEL_KEY, String.valueOf(value), UserData.SHARED);
        key2Vertex.put(multikey, myVertex);
        graph.addVertex(myVertex);
        return myVertex;
    }

    /**
     * Getter for graph.
     * 
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }
    
    /**
     * Method "computeEdgeWidth".
     * 
     * @param weight the weight of an edge
     * @return the width to be used for the edge
     */
    public float getEdgeWidth(int weight) {
        return (proportionalWidth) ? (float) (10 * Math.pow((double) weight / this.getTotalWeight(), 1.0d / 2))
                : 10.0f / weight;
    }
}
