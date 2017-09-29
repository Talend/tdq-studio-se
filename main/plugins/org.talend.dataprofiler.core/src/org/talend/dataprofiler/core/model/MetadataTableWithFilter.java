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
package org.talend.dataprofiler.core.model;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl;
import orgomg.cwm.analysis.businessnomenclature.VocabularyElement;
import orgomg.cwm.analysis.informationvisualization.RenderedObject;
import orgomg.cwm.analysis.transformation.ClassifierFeatureMap;
import orgomg.cwm.analysis.transformation.ClassifierMap;
import orgomg.cwm.analysis.transformation.DataObjectSet;
import orgomg.cwm.foundation.businessinformation.Description;
import orgomg.cwm.foundation.businessinformation.Document;
import orgomg.cwm.foundation.businessinformation.ResponsibleParty;
import orgomg.cwm.foundation.datatypes.TypeAlias;
import orgomg.cwm.foundation.expressions.ElementNode;
import orgomg.cwm.foundation.expressions.ExpressionNode;
import orgomg.cwm.foundation.keysindexes.Index;
import orgomg.cwm.foundation.typemapping.TypeMapping;
import orgomg.cwm.management.warehouseoperation.ChangeRequest;
import orgomg.cwm.management.warehouseoperation.Measurement;
import orgomg.cwm.objectmodel.behavioral.Parameter;
import orgomg.cwm.objectmodel.core.Constraint;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.Feature;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.Stereotype;
import orgomg.cwm.objectmodel.core.StructuralFeature;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.objectmodel.core.VisibilityKind;
import orgomg.cwm.objectmodel.instance.Instance;
import orgomg.cwm.objectmodel.relationships.Generalization;
import orgomg.cwmx.foundation.er.Domain;
import orgomg.cwmx.resource.dmsii.DASDLProperty;
import orgomg.cwmx.resource.express.SimpleDimension;

/**
 * DOC talend class global comment. Detailled comment
 */
public class MetadataTableWithFilter extends MetadataTableImpl {

    List<String> filterNames = null;

    MetadataTable metadataTable = null;

    public MetadataTableWithFilter(List<String> filterNames, MetadataTable metadataTable) {
        this.filterNames = filterNames;
        this.metadataTable = metadataTable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl#getColumns()
     */
    @Override
    public EList<MetadataColumn> getColumns() {
        EList<MetadataColumn> columns = metadataTable.getColumns();
        EList<MetadataColumn> columnList = new BasicEList<MetadataColumn>(filterNames.size());
        for (MetadataColumn column : columns) {
            if (filterNames.contains(column.getName())) {
                columnList.add(column);
            }
        }
        return columnList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#getProperties()
     */
    @Override
    public HashMap getProperties() {
        return metadataTable.getProperties();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#setProperties(java.util.HashMap)
     */
    @Override
    public void setProperties(HashMap value) {
        metadataTable.setProperties(value);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#getId()
     */
    @Override
    public String getId() {
        return metadataTable.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#setId(java.lang.String)
     */
    @Override
    public void setId(String value) {
        metadataTable.setId(value);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#getComment()
     */
    @Override
    public String getComment() {
        return metadataTable.getComment();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#setComment(java.lang.String)
     */
    @Override
    public void setComment(String value) {
        metadataTable.setComment(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#getLabel()
     */
    @Override
    public String getLabel() {
        return metadataTable.getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#setLabel(java.lang.String)
     */
    @Override
    public void setLabel(String value) {
        metadataTable.setLabel(value);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#isReadOnly()
     */
    @Override
    public boolean isReadOnly() {
        return metadataTable.isReadOnly();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#setReadOnly(boolean)
     */
    @Override
    public void setReadOnly(boolean value) {
        metadataTable.setReadOnly(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#isSynchronised()
     */
    @Override
    public boolean isSynchronised() {
        return metadataTable.isSynchronised();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#setSynchronised(boolean)
     */
    @Override
    public void setSynchronised(boolean value) {
        metadataTable.setSynchronised(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#isDivergency()
     */
    @Override
    public boolean isDivergency() {
        return metadataTable.isDivergency();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.AbstractMetadataObject#setDivergency(boolean)
     */
    @Override
    public void setDivergency(boolean value) {
        metadataTable.setDivergency(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getName()
     */
    @Override
    public String getName() {
        return metadataTable.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#setName(java.lang.String)
     */
    @Override
    public void setName(String value) {
        metadataTable.setName(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getVisibility()
     */
    @Override
    public VisibilityKind getVisibility() {
        return metadataTable.getVisibility();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#setVisibility(orgomg.cwm.objectmodel.core.VisibilityKind)
     */
    @Override
    public void setVisibility(VisibilityKind value) {
        metadataTable.setVisibility(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getClientDependency()
     */
    @Override
    public EList<Dependency> getClientDependency() {
        return metadataTable.getClientDependency();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getSupplierDependency()
     */
    @Override
    public EList<Dependency> getSupplierDependency() {
        return metadataTable.getSupplierDependency();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getConstraint()
     */
    @Override
    public EList<Constraint> getConstraint() {
        return metadataTable.getConstraint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getNamespace()
     */
    @Override
    public Namespace getNamespace() {
        return metadataTable.getNamespace();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#setNamespace(orgomg.cwm.objectmodel.core.Namespace)
     */
    @Override
    public void setNamespace(Namespace value) {
        metadataTable.setNamespace(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getImporter()
     */
    @Override
    public EList<Package> getImporter() {
        return metadataTable.getImporter();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getStereotype()
     */
    @Override
    public Stereotype getStereotype() {
        return metadataTable.getStereotype();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#setStereotype(orgomg.cwm.objectmodel.core.Stereotype)
     */
    @Override
    public void setStereotype(Stereotype value) {
        metadataTable.setStereotype(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getTaggedValue()
     */
    @Override
    public EList<TaggedValue> getTaggedValue() {
        return metadataTable.getTaggedValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getDocument()
     */
    @Override
    public EList<Document> getDocument() {
        return metadataTable.getDocument();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getDescription()
     */
    @Override
    public EList<Description> getDescription() {
        return metadataTable.getDescription();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getResponsibleParty()
     */
    @Override
    public EList<ResponsibleParty> getResponsibleParty() {
        return metadataTable.getResponsibleParty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getElementNode()
     */
    @Override
    public EList<ElementNode> getElementNode() {
        return metadataTable.getElementNode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getSet()
     */
    @Override
    public EList<DataObjectSet> getSet() {
        return metadataTable.getSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getRenderedObject()
     */
    @Override
    public EList<RenderedObject> getRenderedObject() {
        return metadataTable.getRenderedObject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getVocabularyElement()
     */
    @Override
    public EList<VocabularyElement> getVocabularyElement() {
        return metadataTable.getVocabularyElement();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getMeasurement()
     */
    @Override
    public EList<Measurement> getMeasurement() {
        return metadataTable.getMeasurement();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getChangeRequest()
     */
    @Override
    public EList<ChangeRequest> getChangeRequest() {
        return metadataTable.getChangeRequest();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.ModelElement#getDasdlProperty()
     */
    @Override
    public EList<DASDLProperty> getDasdlProperty() {
        return metadataTable.getDasdlProperty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eClass()
     */
    @Override
    public EClass eClass() {
        return metadataTable.eClass();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eResource()
     */
    @Override
    public Resource eResource() {
        return metadataTable.eResource();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eContainer()
     */
    @Override
    public EObject eContainer() {
        return metadataTable.eContainer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eContainingFeature()
     */
    @Override
    public EStructuralFeature eContainingFeature() {
        return metadataTable.eContainingFeature();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eContainmentFeature()
     */
    @Override
    public EReference eContainmentFeature() {
        return metadataTable.eContainmentFeature();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eContents()
     */
    @Override
    public EList<EObject> eContents() {
        return metadataTable.eContents();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eAllContents()
     */
    @Override
    public TreeIterator<EObject> eAllContents() {
        return metadataTable.eAllContents();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eIsProxy()
     */
    @Override
    public boolean eIsProxy() {
        return metadataTable.eIsProxy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
     */
    @Override
    public EList<EObject> eCrossReferences() {
        return metadataTable.eCrossReferences();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    @Override
    public Object eGet(EStructuralFeature feature) {
        return metadataTable.eGet(feature);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature, boolean)
     */
    @Override
    public Object eGet(EStructuralFeature feature, boolean resolve) {
        return metadataTable.eGet(feature, resolve);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
     */
    @Override
    public void eSet(EStructuralFeature feature, Object newValue) {
        metadataTable.eSet(feature, newValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    @Override
    public boolean eIsSet(EStructuralFeature feature) {
        return metadataTable.eIsSet(feature);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
     */
    @Override
    public void eUnset(EStructuralFeature feature) {
        metadataTable.eUnset(feature);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.ecore.EObject#eInvoke(org.eclipse.emf.ecore.EOperation, org.eclipse.emf.common.util.EList)
     */
    @Override
    public Object eInvoke(EOperation operation, EList<?> arguments) throws InvocationTargetException {
        return metadataTable.eInvoke(operation, arguments);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
     */
    @Override
    public EList<Adapter> eAdapters() {
        return metadataTable.eAdapters();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.common.notify.Notifier#eDeliver()
     */
    @Override
    public boolean eDeliver() {
        return metadataTable.eDeliver();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.common.notify.Notifier#eSetDeliver(boolean)
     */
    @Override
    public void eSetDeliver(boolean deliver) {
        metadataTable.eSetDeliver(deliver);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
     */
    @Override
    public void eNotify(Notification notification) {
        metadataTable.eNotify(notification);
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Class#getIndex()
     */
    @Override
    public EList<Index> getIndex() {
        return metadataTable.getIndex();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#isIsAbstract()
     */
    @Override
    public boolean isIsAbstract() {
        return metadataTable.isIsAbstract();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#setIsAbstract(boolean)
     */
    @Override
    public void setIsAbstract(boolean value) {
        metadataTable.setIsAbstract(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getFeature()
     */
    @Override
    public EList<Feature> getFeature() {
        return metadataTable.getFeature();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getStructuralFeature()
     */
    @Override
    public EList<StructuralFeature> getStructuralFeature() {
        return metadataTable.getStructuralFeature();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getParameter()
     */
    @Override
    public EList<Parameter> getParameter() {
        return metadataTable.getParameter();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getGeneralization()
     */
    @Override
    public EList<Generalization> getGeneralization() {
        return metadataTable.getGeneralization();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getSpecialization()
     */
    @Override
    public EList<Generalization> getSpecialization() {
        return metadataTable.getSpecialization();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getInstance()
     */
    @Override
    public EList<Instance> getInstance() {
        return metadataTable.getInstance();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getAlias()
     */
    @Override
    public EList<TypeAlias> getAlias() {
        return metadataTable.getAlias();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getExpressionNode()
     */
    @Override
    public EList<ExpressionNode> getExpressionNode() {
        return metadataTable.getExpressionNode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getMappingFrom()
     */
    @Override
    public EList<TypeMapping> getMappingFrom() {
        return metadataTable.getMappingFrom();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getMappingTo()
     */
    @Override
    public EList<TypeMapping> getMappingTo() {
        return metadataTable.getMappingTo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getClassifierMap()
     */
    @Override
    public EList<ClassifierMap> getClassifierMap() {
        return metadataTable.getClassifierMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getCfMap()
     */
    @Override
    public EList<ClassifierFeatureMap> getCfMap() {
        return metadataTable.getCfMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getDomain()
     */
    @Override
    public EList<Domain> getDomain() {
        return metadataTable.getDomain();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Classifier#getSimpleDimension()
     */
    @Override
    public EList<SimpleDimension> getSimpleDimension() {
        return metadataTable.getSimpleDimension();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.Namespace#getOwnedElement()
     */
    @Override
    public EList<ModelElement> getOwnedElement() {
        return metadataTable.getOwnedElement();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#getSourceName()
     */
    @Override
    public String getSourceName() {
        return metadataTable.getSourceName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#setSourceName(java.lang.String)
     */
    @Override
    public void setSourceName(String value) {
        metadataTable.setSourceName(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#getConnection()
     */
    @Override
    public Connection getConnection() {
        return metadataTable.getConnection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#getAdditionalProperties()
     */
    @Override
    public EMap<String, String> getAdditionalProperties() {
        return metadataTable.getAdditionalProperties();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#getTableType()
     */
    @Override
    public String getTableType() {
        return metadataTable.getTableType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#setTableType(java.lang.String)
     */
    @Override
    public void setTableType(String value) {
        metadataTable.setTableType(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#isAttachedCDC()
     */
    @Override
    public boolean isAttachedCDC() {
        return metadataTable.isAttachedCDC();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#setAttachedCDC(boolean)
     */
    @Override
    public void setAttachedCDC(boolean value) {
        metadataTable.setAttachedCDC(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#isActivatedCDC()
     */
    @Override
    public boolean isActivatedCDC() {
        return metadataTable.isActivatedCDC();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.builder.connection.MetadataTable#setActivatedCDC(boolean)
     */
    @Override
    public void setActivatedCDC(boolean value) {
        metadataTable.setActivatedCDC(value);
    }

}
