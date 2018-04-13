/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.indicators;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import orgomg.cwm.objectmodel.core.CorePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.talend.dataquality.indicators.IndicatorsFactory
 * @model kind="package"
 * @generated
 */
public interface IndicatorsPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "indicators";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://dataquality.indicators";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "dataquality.indicators";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    IndicatorsPackage eINSTANCE = org.talend.dataquality.indicators.impl.IndicatorsPackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.IndicatorImpl <em>Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getIndicator()
     * @generated
     */
    int INDICATOR = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__NAME = CorePackage.MODEL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__VISIBILITY = CorePackage.MODEL_ELEMENT__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__CLIENT_DEPENDENCY = CorePackage.MODEL_ELEMENT__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__SUPPLIER_DEPENDENCY = CorePackage.MODEL_ELEMENT__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__CONSTRAINT = CorePackage.MODEL_ELEMENT__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__NAMESPACE = CorePackage.MODEL_ELEMENT__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__IMPORTER = CorePackage.MODEL_ELEMENT__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__STEREOTYPE = CorePackage.MODEL_ELEMENT__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__TAGGED_VALUE = CorePackage.MODEL_ELEMENT__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__DOCUMENT = CorePackage.MODEL_ELEMENT__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__DESCRIPTION = CorePackage.MODEL_ELEMENT__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__RESPONSIBLE_PARTY = CorePackage.MODEL_ELEMENT__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__ELEMENT_NODE = CorePackage.MODEL_ELEMENT__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__SET = CorePackage.MODEL_ELEMENT__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__RENDERED_OBJECT = CorePackage.MODEL_ELEMENT__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__VOCABULARY_ELEMENT = CorePackage.MODEL_ELEMENT__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__MEASUREMENT = CorePackage.MODEL_ELEMENT__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__CHANGE_REQUEST = CorePackage.MODEL_ELEMENT__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__DASDL_PROPERTY = CorePackage.MODEL_ELEMENT__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__COUNT = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__NULL_COUNT = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__PARAMETERS = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__ANALYZED_ELEMENT = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__DATAMINING_TYPE = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__INDICATOR_DEFINITION = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__INSTANTIATED_EXPRESSIONS = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__COMPUTED = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__JOIN_CONDITIONS = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__MAX_NUMBER_ROWS = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__VALID_ROW = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__IN_VALID_ROW = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__STORE_DATA = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 12;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR__BUILT_IN_INDICATOR_DEFINITION = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 13;

    /**
     * The number of structural features of the '<em>Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR_FEATURE_COUNT = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 14;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.RowCountIndicatorImpl <em>Row Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.RowCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getRowCountIndicator()
     * @generated
     */
    int ROW_COUNT_INDICATOR = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The number of structural features of the '<em>Row Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROW_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MeanIndicatorImpl <em>Mean Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MeanIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMeanIndicator()
     * @generated
     */
    int MEAN_INDICATOR = 2;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.SumIndicatorImpl <em>Sum Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.SumIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getSumIndicator()
     * @generated
     */
    int SUM_INDICATOR = 3;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Sum Str</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__SUM_STR = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Datatype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR__DATATYPE = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Sum Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUM_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__NAME = SUM_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__VISIBILITY = SUM_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__CLIENT_DEPENDENCY = SUM_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__SUPPLIER_DEPENDENCY = SUM_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__CONSTRAINT = SUM_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__NAMESPACE = SUM_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__IMPORTER = SUM_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__STEREOTYPE = SUM_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__TAGGED_VALUE = SUM_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__DOCUMENT = SUM_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__DESCRIPTION = SUM_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__RESPONSIBLE_PARTY = SUM_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__ELEMENT_NODE = SUM_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__SET = SUM_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__RENDERED_OBJECT = SUM_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__VOCABULARY_ELEMENT = SUM_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__MEASUREMENT = SUM_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__CHANGE_REQUEST = SUM_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__DASDL_PROPERTY = SUM_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__COUNT = SUM_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__NULL_COUNT = SUM_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__PARAMETERS = SUM_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__ANALYZED_ELEMENT = SUM_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__DATAMINING_TYPE = SUM_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__INDICATOR_DEFINITION = SUM_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__INSTANTIATED_EXPRESSIONS = SUM_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__COMPUTED = SUM_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__JOIN_CONDITIONS = SUM_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__MAX_NUMBER_ROWS = SUM_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__VALID_ROW = SUM_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__IN_VALID_ROW = SUM_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__STORE_DATA = SUM_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = SUM_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Sum Str</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__SUM_STR = SUM_INDICATOR__SUM_STR;

    /**
     * The feature id for the '<em><b>Datatype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR__DATATYPE = SUM_INDICATOR__DATATYPE;

    /**
     * The number of structural features of the '<em>Mean Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEAN_INDICATOR_FEATURE_COUNT = SUM_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.CompositeIndicatorImpl <em>Composite Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.CompositeIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCompositeIndicator()
     * @generated
     */
    int COMPOSITE_INDICATOR = 4;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The number of structural features of the '<em>Composite Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSITE_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.RangeIndicatorImpl <em>Range Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.RangeIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getRangeIndicator()
     * @generated
     */
    int RANGE_INDICATOR = 5;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__NAME = COMPOSITE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__VISIBILITY = COMPOSITE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__CLIENT_DEPENDENCY = COMPOSITE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__SUPPLIER_DEPENDENCY = COMPOSITE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__CONSTRAINT = COMPOSITE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__NAMESPACE = COMPOSITE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__IMPORTER = COMPOSITE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__STEREOTYPE = COMPOSITE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__TAGGED_VALUE = COMPOSITE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__DOCUMENT = COMPOSITE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__DESCRIPTION = COMPOSITE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__RESPONSIBLE_PARTY = COMPOSITE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__ELEMENT_NODE = COMPOSITE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__SET = COMPOSITE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__RENDERED_OBJECT = COMPOSITE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__VOCABULARY_ELEMENT = COMPOSITE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__MEASUREMENT = COMPOSITE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__CHANGE_REQUEST = COMPOSITE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__DASDL_PROPERTY = COMPOSITE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__COUNT = COMPOSITE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__NULL_COUNT = COMPOSITE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__PARAMETERS = COMPOSITE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__ANALYZED_ELEMENT = COMPOSITE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__DATAMINING_TYPE = COMPOSITE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__INDICATOR_DEFINITION = COMPOSITE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__INSTANTIATED_EXPRESSIONS = COMPOSITE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__COMPUTED = COMPOSITE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__JOIN_CONDITIONS = COMPOSITE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__MAX_NUMBER_ROWS = COMPOSITE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__VALID_ROW = COMPOSITE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__IN_VALID_ROW = COMPOSITE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__STORE_DATA = COMPOSITE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = COMPOSITE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Lower Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__LOWER_VALUE = COMPOSITE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Upper Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__UPPER_VALUE = COMPOSITE_INDICATOR_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Datatype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__DATATYPE = COMPOSITE_INDICATOR_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Range</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR__RANGE = COMPOSITE_INDICATOR_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Range Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_INDICATOR_FEATURE_COUNT = COMPOSITE_INDICATOR_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.BoxIndicatorImpl <em>Box Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.BoxIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBoxIndicator()
     * @generated
     */
    int BOX_INDICATOR = 6;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__NAME = COMPOSITE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__VISIBILITY = COMPOSITE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__CLIENT_DEPENDENCY = COMPOSITE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__SUPPLIER_DEPENDENCY = COMPOSITE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__CONSTRAINT = COMPOSITE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__NAMESPACE = COMPOSITE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__IMPORTER = COMPOSITE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__STEREOTYPE = COMPOSITE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__TAGGED_VALUE = COMPOSITE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__DOCUMENT = COMPOSITE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__DESCRIPTION = COMPOSITE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__RESPONSIBLE_PARTY = COMPOSITE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__ELEMENT_NODE = COMPOSITE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__SET = COMPOSITE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__RENDERED_OBJECT = COMPOSITE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__VOCABULARY_ELEMENT = COMPOSITE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__MEASUREMENT = COMPOSITE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__CHANGE_REQUEST = COMPOSITE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__DASDL_PROPERTY = COMPOSITE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__COUNT = COMPOSITE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__NULL_COUNT = COMPOSITE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__PARAMETERS = COMPOSITE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__ANALYZED_ELEMENT = COMPOSITE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__DATAMINING_TYPE = COMPOSITE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__INDICATOR_DEFINITION = COMPOSITE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__INSTANTIATED_EXPRESSIONS = COMPOSITE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__COMPUTED = COMPOSITE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__JOIN_CONDITIONS = COMPOSITE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__MAX_NUMBER_ROWS = COMPOSITE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__VALID_ROW = COMPOSITE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__IN_VALID_ROW = COMPOSITE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__STORE_DATA = COMPOSITE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = COMPOSITE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>IQR</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__IQR = COMPOSITE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Range Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__RANGE_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Mean Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__MEAN_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Median Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR__MEDIAN_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Box Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOX_INDICATOR_FEATURE_COUNT = COMPOSITE_INDICATOR_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl <em>Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getFrequencyIndicator()
     * @generated
     */
    int FREQUENCY_INDICATOR = 7;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__UNIQUE_VALUES = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = INDICATOR_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = INDICATOR_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR__VALUE_TO_FREQ = INDICATOR_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FREQUENCY_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.TextIndicatorImpl <em>Text Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.TextIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getTextIndicator()
     * @generated
     */
    int TEXT_INDICATOR = 20;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.BlankCountIndicatorImpl <em>Blank Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.BlankCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBlankCountIndicator()
     * @generated
     */
    int BLANK_COUNT_INDICATOR = 8;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Blank Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR__BLANK_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Blank Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLANK_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.IndicatorParametersImpl <em>Indicator Parameters</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.IndicatorParametersImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getIndicatorParameters()
     * @generated
     */
    int INDICATOR_PARAMETERS = 9;

    /**
     * The feature id for the '<em><b>Indicator Valid Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR_PARAMETERS__INDICATOR_VALID_DOMAIN = 0;

    /**
     * The feature id for the '<em><b>Data Valid Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR_PARAMETERS__DATA_VALID_DOMAIN = 1;

    /**
     * The feature id for the '<em><b>Bins</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR_PARAMETERS__BINS = 2;

    /**
     * The feature id for the '<em><b>Text Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR_PARAMETERS__TEXT_PARAMETER = 3;

    /**
     * The feature id for the '<em><b>Date Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR_PARAMETERS__DATE_PARAMETERS = 4;

    /**
     * The feature id for the '<em><b>Top N</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR_PARAMETERS__TOP_N = 5;

    /**
     * The number of structural features of the '<em>Indicator Parameters</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDICATOR_PARAMETERS_FEATURE_COUNT = 6;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MedianIndicatorImpl <em>Median Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MedianIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMedianIndicator()
     * @generated
     */
    int MEDIAN_INDICATOR = 10;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Median</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__MEDIAN = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Frequence Table</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__FREQUENCE_TABLE = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Date Median</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR__DATE_MEDIAN = INDICATOR_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Median Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEDIAN_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.ValueIndicatorImpl <em>Value Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.ValueIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getValueIndicator()
     * @generated
     */
    int VALUE_INDICATOR = 11;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__VALUE = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Datatype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR__DATATYPE = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Value Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MinValueIndicatorImpl <em>Min Value Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MinValueIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinValueIndicator()
     * @generated
     */
    int MIN_VALUE_INDICATOR = 12;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__NAME = VALUE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__VISIBILITY = VALUE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__CLIENT_DEPENDENCY = VALUE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__SUPPLIER_DEPENDENCY = VALUE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__CONSTRAINT = VALUE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__NAMESPACE = VALUE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__IMPORTER = VALUE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__STEREOTYPE = VALUE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__TAGGED_VALUE = VALUE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__DOCUMENT = VALUE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__DESCRIPTION = VALUE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__RESPONSIBLE_PARTY = VALUE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__ELEMENT_NODE = VALUE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__SET = VALUE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__RENDERED_OBJECT = VALUE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__VOCABULARY_ELEMENT = VALUE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__MEASUREMENT = VALUE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__CHANGE_REQUEST = VALUE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__DASDL_PROPERTY = VALUE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__COUNT = VALUE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__NULL_COUNT = VALUE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__PARAMETERS = VALUE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__ANALYZED_ELEMENT = VALUE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__DATAMINING_TYPE = VALUE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__INDICATOR_DEFINITION = VALUE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__INSTANTIATED_EXPRESSIONS = VALUE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__COMPUTED = VALUE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__JOIN_CONDITIONS = VALUE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__MAX_NUMBER_ROWS = VALUE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__VALID_ROW = VALUE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__IN_VALID_ROW = VALUE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__STORE_DATA = VALUE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = VALUE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__VALUE = VALUE_INDICATOR__VALUE;

    /**
     * The feature id for the '<em><b>Datatype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR__DATATYPE = VALUE_INDICATOR__DATATYPE;

    /**
     * The number of structural features of the '<em>Min Value Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_VALUE_INDICATOR_FEATURE_COUNT = VALUE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MaxValueIndicatorImpl <em>Max Value Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MaxValueIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxValueIndicator()
     * @generated
     */
    int MAX_VALUE_INDICATOR = 13;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__NAME = VALUE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__VISIBILITY = VALUE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__CLIENT_DEPENDENCY = VALUE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__SUPPLIER_DEPENDENCY = VALUE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__CONSTRAINT = VALUE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__NAMESPACE = VALUE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__IMPORTER = VALUE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__STEREOTYPE = VALUE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__TAGGED_VALUE = VALUE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__DOCUMENT = VALUE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__DESCRIPTION = VALUE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__RESPONSIBLE_PARTY = VALUE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__ELEMENT_NODE = VALUE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__SET = VALUE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__RENDERED_OBJECT = VALUE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__VOCABULARY_ELEMENT = VALUE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__MEASUREMENT = VALUE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__CHANGE_REQUEST = VALUE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__DASDL_PROPERTY = VALUE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__COUNT = VALUE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__NULL_COUNT = VALUE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__PARAMETERS = VALUE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__ANALYZED_ELEMENT = VALUE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__DATAMINING_TYPE = VALUE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__INDICATOR_DEFINITION = VALUE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__INSTANTIATED_EXPRESSIONS = VALUE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__COMPUTED = VALUE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__JOIN_CONDITIONS = VALUE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__MAX_NUMBER_ROWS = VALUE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__VALID_ROW = VALUE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__IN_VALID_ROW = VALUE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__STORE_DATA = VALUE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = VALUE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__VALUE = VALUE_INDICATOR__VALUE;

    /**
     * The feature id for the '<em><b>Datatype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR__DATATYPE = VALUE_INDICATOR__DATATYPE;

    /**
     * The number of structural features of the '<em>Max Value Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_VALUE_INDICATOR_FEATURE_COUNT = VALUE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.ModeIndicatorImpl <em>Mode Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.ModeIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getModeIndicator()
     * @generated
     */
    int MODE_INDICATOR = 14;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The feature id for the '<em><b>Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR__MODE = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Mode Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODE_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.NullCountIndicatorImpl <em>Null Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.NullCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getNullCountIndicator()
     * @generated
     */
    int NULL_COUNT_INDICATOR = 15;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The number of structural features of the '<em>Null Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.DistinctCountIndicatorImpl <em>Distinct Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.DistinctCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDistinctCountIndicator()
     * @generated
     */
    int DISTINCT_COUNT_INDICATOR = 16;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR__DISTINCT_VALUE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Distinct Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTINCT_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.UniqueCountIndicatorImpl <em>Unique Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.UniqueCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getUniqueCountIndicator()
     * @generated
     */
    int UNIQUE_COUNT_INDICATOR = 17;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR__UNIQUE_VALUE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Unique Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIQUE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.DuplicateCountIndicatorImpl <em>Duplicate Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.DuplicateCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDuplicateCountIndicator()
     * @generated
     */
    int DUPLICATE_COUNT_INDICATOR = 18;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR__DUPLICATE_VALUE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Duplicate Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DUPLICATE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.IQRIndicatorImpl <em>IQR Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.IQRIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getIQRIndicator()
     * @generated
     */
    int IQR_INDICATOR = 19;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__NAME = RANGE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__VISIBILITY = RANGE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__CLIENT_DEPENDENCY = RANGE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__SUPPLIER_DEPENDENCY = RANGE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__CONSTRAINT = RANGE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__NAMESPACE = RANGE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__IMPORTER = RANGE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__STEREOTYPE = RANGE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__TAGGED_VALUE = RANGE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__DOCUMENT = RANGE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__DESCRIPTION = RANGE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__RESPONSIBLE_PARTY = RANGE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__ELEMENT_NODE = RANGE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__SET = RANGE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__RENDERED_OBJECT = RANGE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__VOCABULARY_ELEMENT = RANGE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__MEASUREMENT = RANGE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__CHANGE_REQUEST = RANGE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__DASDL_PROPERTY = RANGE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__COUNT = RANGE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__NULL_COUNT = RANGE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__PARAMETERS = RANGE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__ANALYZED_ELEMENT = RANGE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__DATAMINING_TYPE = RANGE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__INDICATOR_DEFINITION = RANGE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__INSTANTIATED_EXPRESSIONS = RANGE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__COMPUTED = RANGE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__JOIN_CONDITIONS = RANGE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__MAX_NUMBER_ROWS = RANGE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__VALID_ROW = RANGE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__IN_VALID_ROW = RANGE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__STORE_DATA = RANGE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = RANGE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Lower Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__LOWER_VALUE = RANGE_INDICATOR__LOWER_VALUE;

    /**
     * The feature id for the '<em><b>Upper Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__UPPER_VALUE = RANGE_INDICATOR__UPPER_VALUE;

    /**
     * The feature id for the '<em><b>Datatype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__DATATYPE = RANGE_INDICATOR__DATATYPE;

    /**
     * The feature id for the '<em><b>Range</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR__RANGE = RANGE_INDICATOR__RANGE;

    /**
     * The number of structural features of the '<em>IQR Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IQR_INDICATOR_FEATURE_COUNT = RANGE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__NAME = COMPOSITE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__VISIBILITY = COMPOSITE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__CLIENT_DEPENDENCY = COMPOSITE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__SUPPLIER_DEPENDENCY = COMPOSITE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__CONSTRAINT = COMPOSITE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__NAMESPACE = COMPOSITE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__IMPORTER = COMPOSITE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__STEREOTYPE = COMPOSITE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__TAGGED_VALUE = COMPOSITE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__DOCUMENT = COMPOSITE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__DESCRIPTION = COMPOSITE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__RESPONSIBLE_PARTY = COMPOSITE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__ELEMENT_NODE = COMPOSITE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__SET = COMPOSITE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__RENDERED_OBJECT = COMPOSITE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__VOCABULARY_ELEMENT = COMPOSITE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MEASUREMENT = COMPOSITE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__CHANGE_REQUEST = COMPOSITE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__DASDL_PROPERTY = COMPOSITE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__COUNT = COMPOSITE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__NULL_COUNT = COMPOSITE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__PARAMETERS = COMPOSITE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__ANALYZED_ELEMENT = COMPOSITE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__DATAMINING_TYPE = COMPOSITE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__INDICATOR_DEFINITION = COMPOSITE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__INSTANTIATED_EXPRESSIONS = COMPOSITE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__COMPUTED = COMPOSITE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__JOIN_CONDITIONS = COMPOSITE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MAX_NUMBER_ROWS = COMPOSITE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__VALID_ROW = COMPOSITE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__IN_VALID_ROW = COMPOSITE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__STORE_DATA = COMPOSITE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = COMPOSITE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Average Length Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__AVERAGE_LENGTH_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Max Length Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MAX_LENGTH_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Min Length Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MIN_LENGTH_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Min Length With Blank Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MIN_LENGTH_WITH_BLANK_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Min Length With Null Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MIN_LENGTH_WITH_NULL_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Min Length With Blank Null Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MIN_LENGTH_WITH_BLANK_NULL_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Max Length With Blank Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MAX_LENGTH_WITH_BLANK_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Max Length With Null Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MAX_LENGTH_WITH_NULL_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Max Length With Blank Null Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__MAX_LENGTH_WITH_BLANK_NULL_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Avg Length With Blank Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__AVG_LENGTH_WITH_BLANK_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Avg Length With Null Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__AVG_LENGTH_WITH_NULL_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Avg Length With Blank Null Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR__AVG_LENGTH_WITH_BLANK_NULL_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 11;

    /**
     * The number of structural features of the '<em>Text Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_INDICATOR_FEATURE_COUNT = COMPOSITE_INDICATOR_FEATURE_COUNT + 12;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.LengthIndicatorImpl <em>Length Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.LengthIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getLengthIndicator()
     * @generated
     */
    int LENGTH_INDICATOR = 33;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR__LENGTH = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Length Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LENGTH_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MinLengthIndicatorImpl <em>Min Length Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MinLengthIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinLengthIndicator()
     * @generated
     */
    int MIN_LENGTH_INDICATOR = 21;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__NAME = LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__VISIBILITY = LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__CLIENT_DEPENDENCY = LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY = LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__CONSTRAINT = LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__NAMESPACE = LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__IMPORTER = LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__STEREOTYPE = LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__TAGGED_VALUE = LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__DOCUMENT = LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__DESCRIPTION = LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__RESPONSIBLE_PARTY = LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__ELEMENT_NODE = LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__SET = LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__RENDERED_OBJECT = LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__VOCABULARY_ELEMENT = LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__MEASUREMENT = LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__CHANGE_REQUEST = LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__DASDL_PROPERTY = LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__COUNT = LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__NULL_COUNT = LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__PARAMETERS = LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__ANALYZED_ELEMENT = LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__DATAMINING_TYPE = LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__INDICATOR_DEFINITION = LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS = LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__COMPUTED = LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__JOIN_CONDITIONS = LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__MAX_NUMBER_ROWS = LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__VALID_ROW = LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__IN_VALID_ROW = LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__STORE_DATA = LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR__LENGTH = LENGTH_INDICATOR__LENGTH;

    /**
     * The number of structural features of the '<em>Min Length Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_INDICATOR_FEATURE_COUNT = LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MinLengthWithNullIndicatorImpl <em>Min Length With Null Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MinLengthWithNullIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinLengthWithNullIndicator()
     * @generated
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR = 22;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__NAME = MIN_LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__VISIBILITY = MIN_LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__CLIENT_DEPENDENCY = MIN_LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__SUPPLIER_DEPENDENCY = MIN_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__CONSTRAINT = MIN_LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__NAMESPACE = MIN_LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__IMPORTER = MIN_LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__STEREOTYPE = MIN_LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__TAGGED_VALUE = MIN_LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__DOCUMENT = MIN_LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__DESCRIPTION = MIN_LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__RESPONSIBLE_PARTY = MIN_LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__ELEMENT_NODE = MIN_LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__SET = MIN_LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__RENDERED_OBJECT = MIN_LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__VOCABULARY_ELEMENT = MIN_LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__MEASUREMENT = MIN_LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__CHANGE_REQUEST = MIN_LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__DASDL_PROPERTY = MIN_LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__COUNT = MIN_LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__NULL_COUNT = MIN_LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__PARAMETERS = MIN_LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__ANALYZED_ELEMENT = MIN_LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__DATAMINING_TYPE = MIN_LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__INDICATOR_DEFINITION = MIN_LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__INSTANTIATED_EXPRESSIONS = MIN_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__COMPUTED = MIN_LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__JOIN_CONDITIONS = MIN_LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__MAX_NUMBER_ROWS = MIN_LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__VALID_ROW = MIN_LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__IN_VALID_ROW = MIN_LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__STORE_DATA = MIN_LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = MIN_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR__LENGTH = MIN_LENGTH_INDICATOR__LENGTH;

    /**
     * The number of structural features of the '<em>Min Length With Null Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_NULL_INDICATOR_FEATURE_COUNT = MIN_LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MinLengthWithBlankIndicatorImpl <em>Min Length With Blank Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MinLengthWithBlankIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinLengthWithBlankIndicator()
     * @generated
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR = 23;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__NAME = MIN_LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__VISIBILITY = MIN_LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__CLIENT_DEPENDENCY = MIN_LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__SUPPLIER_DEPENDENCY = MIN_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__CONSTRAINT = MIN_LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__NAMESPACE = MIN_LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__IMPORTER = MIN_LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__STEREOTYPE = MIN_LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__TAGGED_VALUE = MIN_LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__DOCUMENT = MIN_LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__DESCRIPTION = MIN_LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__RESPONSIBLE_PARTY = MIN_LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__ELEMENT_NODE = MIN_LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__SET = MIN_LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__RENDERED_OBJECT = MIN_LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__VOCABULARY_ELEMENT = MIN_LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__MEASUREMENT = MIN_LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__CHANGE_REQUEST = MIN_LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__DASDL_PROPERTY = MIN_LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__COUNT = MIN_LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__NULL_COUNT = MIN_LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__PARAMETERS = MIN_LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__ANALYZED_ELEMENT = MIN_LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__DATAMINING_TYPE = MIN_LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__INDICATOR_DEFINITION = MIN_LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__INSTANTIATED_EXPRESSIONS = MIN_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__COMPUTED = MIN_LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__JOIN_CONDITIONS = MIN_LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__MAX_NUMBER_ROWS = MIN_LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__VALID_ROW = MIN_LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__IN_VALID_ROW = MIN_LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__STORE_DATA = MIN_LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = MIN_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR__LENGTH = MIN_LENGTH_INDICATOR__LENGTH;

    /**
     * The number of structural features of the '<em>Min Length With Blank Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_INDICATOR_FEATURE_COUNT = MIN_LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MinLengthWithBlankNullIndicatorImpl <em>Min Length With Blank Null Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MinLengthWithBlankNullIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinLengthWithBlankNullIndicator()
     * @generated
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR = 24;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__NAME = MIN_LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__VISIBILITY = MIN_LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__CLIENT_DEPENDENCY = MIN_LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__SUPPLIER_DEPENDENCY = MIN_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__CONSTRAINT = MIN_LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__NAMESPACE = MIN_LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__IMPORTER = MIN_LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__STEREOTYPE = MIN_LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__TAGGED_VALUE = MIN_LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__DOCUMENT = MIN_LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__DESCRIPTION = MIN_LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__RESPONSIBLE_PARTY = MIN_LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__ELEMENT_NODE = MIN_LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__SET = MIN_LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__RENDERED_OBJECT = MIN_LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__VOCABULARY_ELEMENT = MIN_LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__MEASUREMENT = MIN_LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__CHANGE_REQUEST = MIN_LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__DASDL_PROPERTY = MIN_LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__COUNT = MIN_LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__NULL_COUNT = MIN_LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__PARAMETERS = MIN_LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__ANALYZED_ELEMENT = MIN_LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__DATAMINING_TYPE = MIN_LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__INDICATOR_DEFINITION = MIN_LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__INSTANTIATED_EXPRESSIONS = MIN_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__COMPUTED = MIN_LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__JOIN_CONDITIONS = MIN_LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__MAX_NUMBER_ROWS = MIN_LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__VALID_ROW = MIN_LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__IN_VALID_ROW = MIN_LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__STORE_DATA = MIN_LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = MIN_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR__LENGTH = MIN_LENGTH_INDICATOR__LENGTH;

    /**
     * The number of structural features of the '<em>Min Length With Blank Null Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIN_LENGTH_WITH_BLANK_NULL_INDICATOR_FEATURE_COUNT = MIN_LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MaxLengthIndicatorImpl <em>Max Length Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MaxLengthIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxLengthIndicator()
     * @generated
     */
    int MAX_LENGTH_INDICATOR = 25;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__NAME = LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__VISIBILITY = LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__CLIENT_DEPENDENCY = LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY = LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__CONSTRAINT = LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__NAMESPACE = LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__IMPORTER = LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__STEREOTYPE = LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__TAGGED_VALUE = LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__DOCUMENT = LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__DESCRIPTION = LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__RESPONSIBLE_PARTY = LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__ELEMENT_NODE = LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__SET = LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__RENDERED_OBJECT = LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__VOCABULARY_ELEMENT = LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__MEASUREMENT = LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__CHANGE_REQUEST = LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__DASDL_PROPERTY = LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__COUNT = LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__NULL_COUNT = LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__PARAMETERS = LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__ANALYZED_ELEMENT = LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__DATAMINING_TYPE = LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__INDICATOR_DEFINITION = LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS = LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__COMPUTED = LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__JOIN_CONDITIONS = LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__MAX_NUMBER_ROWS = LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__VALID_ROW = LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__IN_VALID_ROW = LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__STORE_DATA = LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR__LENGTH = LENGTH_INDICATOR__LENGTH;

    /**
     * The number of structural features of the '<em>Max Length Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_INDICATOR_FEATURE_COUNT = LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MaxLengthWithNullIndicatorImpl <em>Max Length With Null Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MaxLengthWithNullIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxLengthWithNullIndicator()
     * @generated
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR = 26;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__NAME = MAX_LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__VISIBILITY = MAX_LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__CLIENT_DEPENDENCY = MAX_LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__SUPPLIER_DEPENDENCY = MAX_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__CONSTRAINT = MAX_LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__NAMESPACE = MAX_LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__IMPORTER = MAX_LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__STEREOTYPE = MAX_LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__TAGGED_VALUE = MAX_LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__DOCUMENT = MAX_LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__DESCRIPTION = MAX_LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__RESPONSIBLE_PARTY = MAX_LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__ELEMENT_NODE = MAX_LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__SET = MAX_LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__RENDERED_OBJECT = MAX_LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__VOCABULARY_ELEMENT = MAX_LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__MEASUREMENT = MAX_LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__CHANGE_REQUEST = MAX_LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__DASDL_PROPERTY = MAX_LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__COUNT = MAX_LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__NULL_COUNT = MAX_LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__PARAMETERS = MAX_LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__ANALYZED_ELEMENT = MAX_LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__DATAMINING_TYPE = MAX_LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__INDICATOR_DEFINITION = MAX_LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__INSTANTIATED_EXPRESSIONS = MAX_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__COMPUTED = MAX_LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__JOIN_CONDITIONS = MAX_LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__MAX_NUMBER_ROWS = MAX_LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__VALID_ROW = MAX_LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__IN_VALID_ROW = MAX_LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__STORE_DATA = MAX_LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = MAX_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR__LENGTH = MAX_LENGTH_INDICATOR__LENGTH;

    /**
     * The number of structural features of the '<em>Max Length With Null Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_NULL_INDICATOR_FEATURE_COUNT = MAX_LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MaxLengthWithBlankIndicatorImpl <em>Max Length With Blank Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MaxLengthWithBlankIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxLengthWithBlankIndicator()
     * @generated
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR = 27;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__NAME = MAX_LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__VISIBILITY = MAX_LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__CLIENT_DEPENDENCY = MAX_LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__SUPPLIER_DEPENDENCY = MAX_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__CONSTRAINT = MAX_LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__NAMESPACE = MAX_LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__IMPORTER = MAX_LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__STEREOTYPE = MAX_LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__TAGGED_VALUE = MAX_LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__DOCUMENT = MAX_LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__DESCRIPTION = MAX_LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__RESPONSIBLE_PARTY = MAX_LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__ELEMENT_NODE = MAX_LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__SET = MAX_LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__RENDERED_OBJECT = MAX_LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__VOCABULARY_ELEMENT = MAX_LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__MEASUREMENT = MAX_LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__CHANGE_REQUEST = MAX_LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__DASDL_PROPERTY = MAX_LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__COUNT = MAX_LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__NULL_COUNT = MAX_LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__PARAMETERS = MAX_LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__ANALYZED_ELEMENT = MAX_LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__DATAMINING_TYPE = MAX_LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__INDICATOR_DEFINITION = MAX_LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__INSTANTIATED_EXPRESSIONS = MAX_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__COMPUTED = MAX_LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__JOIN_CONDITIONS = MAX_LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__MAX_NUMBER_ROWS = MAX_LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__VALID_ROW = MAX_LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__IN_VALID_ROW = MAX_LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__STORE_DATA = MAX_LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = MAX_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR__LENGTH = MAX_LENGTH_INDICATOR__LENGTH;

    /**
     * The number of structural features of the '<em>Max Length With Blank Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_INDICATOR_FEATURE_COUNT = MAX_LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MaxLengthWithBlankNullIndicatorImpl <em>Max Length With Blank Null Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MaxLengthWithBlankNullIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxLengthWithBlankNullIndicator()
     * @generated
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR = 28;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__NAME = MAX_LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__VISIBILITY = MAX_LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__CLIENT_DEPENDENCY = MAX_LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__SUPPLIER_DEPENDENCY = MAX_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__CONSTRAINT = MAX_LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__NAMESPACE = MAX_LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__IMPORTER = MAX_LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__STEREOTYPE = MAX_LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__TAGGED_VALUE = MAX_LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__DOCUMENT = MAX_LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__DESCRIPTION = MAX_LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__RESPONSIBLE_PARTY = MAX_LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__ELEMENT_NODE = MAX_LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__SET = MAX_LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__RENDERED_OBJECT = MAX_LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__VOCABULARY_ELEMENT = MAX_LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__MEASUREMENT = MAX_LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__CHANGE_REQUEST = MAX_LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__DASDL_PROPERTY = MAX_LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__COUNT = MAX_LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__NULL_COUNT = MAX_LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__PARAMETERS = MAX_LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__ANALYZED_ELEMENT = MAX_LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__DATAMINING_TYPE = MAX_LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__INDICATOR_DEFINITION = MAX_LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__INSTANTIATED_EXPRESSIONS = MAX_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__COMPUTED = MAX_LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__JOIN_CONDITIONS = MAX_LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__MAX_NUMBER_ROWS = MAX_LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__VALID_ROW = MAX_LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__IN_VALID_ROW = MAX_LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__STORE_DATA = MAX_LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = MAX_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR__LENGTH = MAX_LENGTH_INDICATOR__LENGTH;

    /**
     * The number of structural features of the '<em>Max Length With Blank Null Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAX_LENGTH_WITH_BLANK_NULL_INDICATOR_FEATURE_COUNT = MAX_LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.AverageLengthIndicatorImpl <em>Average Length Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.AverageLengthIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getAverageLengthIndicator()
     * @generated
     */
    int AVERAGE_LENGTH_INDICATOR = 29;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__NAME = LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__VISIBILITY = LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__CLIENT_DEPENDENCY = LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY = LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__CONSTRAINT = LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__NAMESPACE = LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__IMPORTER = LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__STEREOTYPE = LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__TAGGED_VALUE = LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__DOCUMENT = LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__DESCRIPTION = LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__RESPONSIBLE_PARTY = LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__ELEMENT_NODE = LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__SET = LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__RENDERED_OBJECT = LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__VOCABULARY_ELEMENT = LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__MEASUREMENT = LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__CHANGE_REQUEST = LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__DASDL_PROPERTY = LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__COUNT = LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__NULL_COUNT = LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__PARAMETERS = LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__ANALYZED_ELEMENT = LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__DATAMINING_TYPE = LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__INDICATOR_DEFINITION = LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS = LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__COMPUTED = LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__JOIN_CONDITIONS = LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__MAX_NUMBER_ROWS = LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__VALID_ROW = LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__IN_VALID_ROW = LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__STORE_DATA = LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__LENGTH = LENGTH_INDICATOR__LENGTH;

    /**
     * The feature id for the '<em><b>Sum Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR__SUM_LENGTH = LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Average Length Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVERAGE_LENGTH_INDICATOR_FEATURE_COUNT = LENGTH_INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.AvgLengthWithNullIndicatorImpl <em>Avg Length With Null Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.AvgLengthWithNullIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getAvgLengthWithNullIndicator()
     * @generated
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR = 30;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__NAME = AVERAGE_LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__VISIBILITY = AVERAGE_LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__CLIENT_DEPENDENCY = AVERAGE_LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__SUPPLIER_DEPENDENCY = AVERAGE_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__CONSTRAINT = AVERAGE_LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__NAMESPACE = AVERAGE_LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__IMPORTER = AVERAGE_LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__STEREOTYPE = AVERAGE_LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__TAGGED_VALUE = AVERAGE_LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__DOCUMENT = AVERAGE_LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__DESCRIPTION = AVERAGE_LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__RESPONSIBLE_PARTY = AVERAGE_LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__ELEMENT_NODE = AVERAGE_LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__SET = AVERAGE_LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__RENDERED_OBJECT = AVERAGE_LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__VOCABULARY_ELEMENT = AVERAGE_LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__MEASUREMENT = AVERAGE_LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__CHANGE_REQUEST = AVERAGE_LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__DASDL_PROPERTY = AVERAGE_LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__COUNT = AVERAGE_LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__NULL_COUNT = AVERAGE_LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__PARAMETERS = AVERAGE_LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__ANALYZED_ELEMENT = AVERAGE_LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__DATAMINING_TYPE = AVERAGE_LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__INDICATOR_DEFINITION = AVERAGE_LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__INSTANTIATED_EXPRESSIONS = AVERAGE_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__COMPUTED = AVERAGE_LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__JOIN_CONDITIONS = AVERAGE_LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__MAX_NUMBER_ROWS = AVERAGE_LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__VALID_ROW = AVERAGE_LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__IN_VALID_ROW = AVERAGE_LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__STORE_DATA = AVERAGE_LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = AVERAGE_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__LENGTH = AVERAGE_LENGTH_INDICATOR__LENGTH;

    /**
     * The feature id for the '<em><b>Sum Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR__SUM_LENGTH = AVERAGE_LENGTH_INDICATOR__SUM_LENGTH;

    /**
     * The number of structural features of the '<em>Avg Length With Null Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_NULL_INDICATOR_FEATURE_COUNT = AVERAGE_LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.AvgLengthWithBlankIndicatorImpl <em>Avg Length With Blank Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.AvgLengthWithBlankIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getAvgLengthWithBlankIndicator()
     * @generated
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR = 31;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__NAME = AVERAGE_LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__VISIBILITY = AVERAGE_LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__CLIENT_DEPENDENCY = AVERAGE_LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__SUPPLIER_DEPENDENCY = AVERAGE_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__CONSTRAINT = AVERAGE_LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__NAMESPACE = AVERAGE_LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__IMPORTER = AVERAGE_LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__STEREOTYPE = AVERAGE_LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__TAGGED_VALUE = AVERAGE_LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__DOCUMENT = AVERAGE_LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__DESCRIPTION = AVERAGE_LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__RESPONSIBLE_PARTY = AVERAGE_LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__ELEMENT_NODE = AVERAGE_LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__SET = AVERAGE_LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__RENDERED_OBJECT = AVERAGE_LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__VOCABULARY_ELEMENT = AVERAGE_LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__MEASUREMENT = AVERAGE_LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__CHANGE_REQUEST = AVERAGE_LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__DASDL_PROPERTY = AVERAGE_LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__COUNT = AVERAGE_LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__NULL_COUNT = AVERAGE_LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__PARAMETERS = AVERAGE_LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__ANALYZED_ELEMENT = AVERAGE_LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__DATAMINING_TYPE = AVERAGE_LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__INDICATOR_DEFINITION = AVERAGE_LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__INSTANTIATED_EXPRESSIONS = AVERAGE_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__COMPUTED = AVERAGE_LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__JOIN_CONDITIONS = AVERAGE_LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__MAX_NUMBER_ROWS = AVERAGE_LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__VALID_ROW = AVERAGE_LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__IN_VALID_ROW = AVERAGE_LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__STORE_DATA = AVERAGE_LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = AVERAGE_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__LENGTH = AVERAGE_LENGTH_INDICATOR__LENGTH;

    /**
     * The feature id for the '<em><b>Sum Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR__SUM_LENGTH = AVERAGE_LENGTH_INDICATOR__SUM_LENGTH;

    /**
     * The number of structural features of the '<em>Avg Length With Blank Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_INDICATOR_FEATURE_COUNT = AVERAGE_LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.AvgLengthWithBlankNullIndicatorImpl <em>Avg Length With Blank Null Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.AvgLengthWithBlankNullIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getAvgLengthWithBlankNullIndicator()
     * @generated
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR = 32;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__NAME = AVERAGE_LENGTH_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__VISIBILITY = AVERAGE_LENGTH_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__CLIENT_DEPENDENCY = AVERAGE_LENGTH_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__SUPPLIER_DEPENDENCY = AVERAGE_LENGTH_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__CONSTRAINT = AVERAGE_LENGTH_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__NAMESPACE = AVERAGE_LENGTH_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__IMPORTER = AVERAGE_LENGTH_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__STEREOTYPE = AVERAGE_LENGTH_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__TAGGED_VALUE = AVERAGE_LENGTH_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__DOCUMENT = AVERAGE_LENGTH_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__DESCRIPTION = AVERAGE_LENGTH_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__RESPONSIBLE_PARTY = AVERAGE_LENGTH_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__ELEMENT_NODE = AVERAGE_LENGTH_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__SET = AVERAGE_LENGTH_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__RENDERED_OBJECT = AVERAGE_LENGTH_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__VOCABULARY_ELEMENT = AVERAGE_LENGTH_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__MEASUREMENT = AVERAGE_LENGTH_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__CHANGE_REQUEST = AVERAGE_LENGTH_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__DASDL_PROPERTY = AVERAGE_LENGTH_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__COUNT = AVERAGE_LENGTH_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__NULL_COUNT = AVERAGE_LENGTH_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__PARAMETERS = AVERAGE_LENGTH_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__ANALYZED_ELEMENT = AVERAGE_LENGTH_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__DATAMINING_TYPE = AVERAGE_LENGTH_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__INDICATOR_DEFINITION = AVERAGE_LENGTH_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__INSTANTIATED_EXPRESSIONS = AVERAGE_LENGTH_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__COMPUTED = AVERAGE_LENGTH_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__JOIN_CONDITIONS = AVERAGE_LENGTH_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__MAX_NUMBER_ROWS = AVERAGE_LENGTH_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__VALID_ROW = AVERAGE_LENGTH_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__IN_VALID_ROW = AVERAGE_LENGTH_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__STORE_DATA = AVERAGE_LENGTH_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = AVERAGE_LENGTH_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__LENGTH = AVERAGE_LENGTH_INDICATOR__LENGTH;

    /**
     * The feature id for the '<em><b>Sum Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR__SUM_LENGTH = AVERAGE_LENGTH_INDICATOR__SUM_LENGTH;

    /**
     * The number of structural features of the '<em>Avg Length With Blank Null Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVG_LENGTH_WITH_BLANK_NULL_INDICATOR_FEATURE_COUNT = AVERAGE_LENGTH_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.TextParametersImpl <em>Text Parameters</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.TextParametersImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getTextParameters()
     * @generated
     */
    int TEXT_PARAMETERS = 34;

    /**
     * The feature id for the '<em><b>Use Blank</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PARAMETERS__USE_BLANK = 0;

    /**
     * The feature id for the '<em><b>Matching Algorithm</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PARAMETERS__MATCHING_ALGORITHM = 1;

    /**
     * The feature id for the '<em><b>Ignore Case</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PARAMETERS__IGNORE_CASE = 2;

    /**
     * The feature id for the '<em><b>Use Nulls</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PARAMETERS__USE_NULLS = 3;

    /**
     * The feature id for the '<em><b>Characters To Replace</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PARAMETERS__CHARACTERS_TO_REPLACE = 4;

    /**
     * The feature id for the '<em><b>Replacement Characters</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PARAMETERS__REPLACEMENT_CHARACTERS = 5;

    /**
     * The feature id for the '<em><b>Country Code</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PARAMETERS__COUNTRY_CODE = 6;

    /**
     * The number of structural features of the '<em>Text Parameters</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PARAMETERS_FEATURE_COUNT = 7;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.LowerQuartileIndicatorImpl <em>Lower Quartile Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.LowerQuartileIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getLowerQuartileIndicator()
     * @generated
     */
    int LOWER_QUARTILE_INDICATOR = 35;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__NAME = MIN_VALUE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__VISIBILITY = MIN_VALUE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__CLIENT_DEPENDENCY = MIN_VALUE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__SUPPLIER_DEPENDENCY = MIN_VALUE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__CONSTRAINT = MIN_VALUE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__NAMESPACE = MIN_VALUE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__IMPORTER = MIN_VALUE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__STEREOTYPE = MIN_VALUE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__TAGGED_VALUE = MIN_VALUE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__DOCUMENT = MIN_VALUE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__DESCRIPTION = MIN_VALUE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__RESPONSIBLE_PARTY = MIN_VALUE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__ELEMENT_NODE = MIN_VALUE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__SET = MIN_VALUE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__RENDERED_OBJECT = MIN_VALUE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__VOCABULARY_ELEMENT = MIN_VALUE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__MEASUREMENT = MIN_VALUE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__CHANGE_REQUEST = MIN_VALUE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__DASDL_PROPERTY = MIN_VALUE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__COUNT = MIN_VALUE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__NULL_COUNT = MIN_VALUE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__PARAMETERS = MIN_VALUE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__ANALYZED_ELEMENT = MIN_VALUE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__DATAMINING_TYPE = MIN_VALUE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__INDICATOR_DEFINITION = MIN_VALUE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__INSTANTIATED_EXPRESSIONS = MIN_VALUE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__COMPUTED = MIN_VALUE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__JOIN_CONDITIONS = MIN_VALUE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__MAX_NUMBER_ROWS = MIN_VALUE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__VALID_ROW = MIN_VALUE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__IN_VALID_ROW = MIN_VALUE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__STORE_DATA = MIN_VALUE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = MIN_VALUE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__VALUE = MIN_VALUE_INDICATOR__VALUE;

    /**
     * The feature id for the '<em><b>Datatype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR__DATATYPE = MIN_VALUE_INDICATOR__DATATYPE;

    /**
     * The number of structural features of the '<em>Lower Quartile Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_QUARTILE_INDICATOR_FEATURE_COUNT = MIN_VALUE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.UpperQuartileIndicatorImpl <em>Upper Quartile Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.UpperQuartileIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getUpperQuartileIndicator()
     * @generated
     */
    int UPPER_QUARTILE_INDICATOR = 36;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__NAME = MAX_VALUE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__VISIBILITY = MAX_VALUE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__CLIENT_DEPENDENCY = MAX_VALUE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__SUPPLIER_DEPENDENCY = MAX_VALUE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__CONSTRAINT = MAX_VALUE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__NAMESPACE = MAX_VALUE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__IMPORTER = MAX_VALUE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__STEREOTYPE = MAX_VALUE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__TAGGED_VALUE = MAX_VALUE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__DOCUMENT = MAX_VALUE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__DESCRIPTION = MAX_VALUE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__RESPONSIBLE_PARTY = MAX_VALUE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__ELEMENT_NODE = MAX_VALUE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__SET = MAX_VALUE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__RENDERED_OBJECT = MAX_VALUE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__VOCABULARY_ELEMENT = MAX_VALUE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__MEASUREMENT = MAX_VALUE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__CHANGE_REQUEST = MAX_VALUE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__DASDL_PROPERTY = MAX_VALUE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__COUNT = MAX_VALUE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__NULL_COUNT = MAX_VALUE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__PARAMETERS = MAX_VALUE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__ANALYZED_ELEMENT = MAX_VALUE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__DATAMINING_TYPE = MAX_VALUE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__INDICATOR_DEFINITION = MAX_VALUE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__INSTANTIATED_EXPRESSIONS = MAX_VALUE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__COMPUTED = MAX_VALUE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__JOIN_CONDITIONS = MAX_VALUE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__MAX_NUMBER_ROWS = MAX_VALUE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__VALID_ROW = MAX_VALUE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__IN_VALID_ROW = MAX_VALUE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__STORE_DATA = MAX_VALUE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = MAX_VALUE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__VALUE = MAX_VALUE_INDICATOR__VALUE;

    /**
     * The feature id for the '<em><b>Datatype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR__DATATYPE = MAX_VALUE_INDICATOR__DATATYPE;

    /**
     * The number of structural features of the '<em>Upper Quartile Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_QUARTILE_INDICATOR_FEATURE_COUNT = MAX_VALUE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.CountsIndicatorImpl <em>Counts Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.CountsIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCountsIndicator()
     * @generated
     */
    int COUNTS_INDICATOR = 37;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__NAME = COMPOSITE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__VISIBILITY = COMPOSITE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__CLIENT_DEPENDENCY = COMPOSITE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__SUPPLIER_DEPENDENCY = COMPOSITE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__CONSTRAINT = COMPOSITE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__NAMESPACE = COMPOSITE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__IMPORTER = COMPOSITE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__STEREOTYPE = COMPOSITE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__TAGGED_VALUE = COMPOSITE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__DOCUMENT = COMPOSITE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__DESCRIPTION = COMPOSITE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__RESPONSIBLE_PARTY = COMPOSITE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__ELEMENT_NODE = COMPOSITE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__SET = COMPOSITE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__RENDERED_OBJECT = COMPOSITE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__VOCABULARY_ELEMENT = COMPOSITE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__MEASUREMENT = COMPOSITE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__CHANGE_REQUEST = COMPOSITE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__DASDL_PROPERTY = COMPOSITE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__COUNT = COMPOSITE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__NULL_COUNT = COMPOSITE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__PARAMETERS = COMPOSITE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__ANALYZED_ELEMENT = COMPOSITE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__DATAMINING_TYPE = COMPOSITE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__INDICATOR_DEFINITION = COMPOSITE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__INSTANTIATED_EXPRESSIONS = COMPOSITE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__COMPUTED = COMPOSITE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__JOIN_CONDITIONS = COMPOSITE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__MAX_NUMBER_ROWS = COMPOSITE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__VALID_ROW = COMPOSITE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__IN_VALID_ROW = COMPOSITE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__STORE_DATA = COMPOSITE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = COMPOSITE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Blank Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__BLANK_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Row Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__ROW_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Null Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__NULL_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Unique Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__UNIQUE_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Distinct Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__DISTINCT_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Duplicate Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__DUPLICATE_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Default Value Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR__DEFAULT_VALUE_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Counts Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COUNTS_INDICATOR_FEATURE_COUNT = COMPOSITE_INDICATOR_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.DateParametersImpl <em>Date Parameters</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.DateParametersImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDateParameters()
     * @generated
     */
    int DATE_PARAMETERS = 38;

    /**
     * The feature id for the '<em><b>Date Aggregation Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PARAMETERS__DATE_AGGREGATION_TYPE = 0;

    /**
     * The number of structural features of the '<em>Date Parameters</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PARAMETERS_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MatchingIndicatorImpl <em>Matching Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MatchingIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMatchingIndicator()
     * @generated
     */
    int MATCHING_INDICATOR = 42;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Matching Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__MATCHING_VALUE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Not Matching Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR__NOT_MATCHING_VALUE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Matching Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCHING_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.PatternMatchingIndicatorImpl <em>Pattern Matching Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.PatternMatchingIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPatternMatchingIndicator()
     * @generated
     */
    int PATTERN_MATCHING_INDICATOR = 39;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__NAME = MATCHING_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__VISIBILITY = MATCHING_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__CLIENT_DEPENDENCY = MATCHING_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__SUPPLIER_DEPENDENCY = MATCHING_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__CONSTRAINT = MATCHING_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__NAMESPACE = MATCHING_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__IMPORTER = MATCHING_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__STEREOTYPE = MATCHING_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__TAGGED_VALUE = MATCHING_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__DOCUMENT = MATCHING_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__DESCRIPTION = MATCHING_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__RESPONSIBLE_PARTY = MATCHING_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__ELEMENT_NODE = MATCHING_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__SET = MATCHING_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__RENDERED_OBJECT = MATCHING_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__VOCABULARY_ELEMENT = MATCHING_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__MEASUREMENT = MATCHING_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__CHANGE_REQUEST = MATCHING_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__DASDL_PROPERTY = MATCHING_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__COUNT = MATCHING_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__NULL_COUNT = MATCHING_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__PARAMETERS = MATCHING_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__ANALYZED_ELEMENT = MATCHING_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__DATAMINING_TYPE = MATCHING_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__INDICATOR_DEFINITION = MATCHING_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__INSTANTIATED_EXPRESSIONS = MATCHING_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__COMPUTED = MATCHING_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__JOIN_CONDITIONS = MATCHING_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__MAX_NUMBER_ROWS = MATCHING_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__VALID_ROW = MATCHING_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__IN_VALID_ROW = MATCHING_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__STORE_DATA = MATCHING_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = MATCHING_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Matching Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__MATCHING_VALUE_COUNT = MATCHING_INDICATOR__MATCHING_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Not Matching Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR__NOT_MATCHING_VALUE_COUNT = MATCHING_INDICATOR__NOT_MATCHING_VALUE_COUNT;

    /**
     * The number of structural features of the '<em>Pattern Matching Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_MATCHING_INDICATOR_FEATURE_COUNT = MATCHING_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.SqlPatternMatchingIndicatorImpl <em>Sql Pattern Matching Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.SqlPatternMatchingIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getSqlPatternMatchingIndicator()
     * @generated
     */
    int SQL_PATTERN_MATCHING_INDICATOR = 40;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__NAME = PATTERN_MATCHING_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__VISIBILITY = PATTERN_MATCHING_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__CLIENT_DEPENDENCY = PATTERN_MATCHING_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__SUPPLIER_DEPENDENCY = PATTERN_MATCHING_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__CONSTRAINT = PATTERN_MATCHING_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__NAMESPACE = PATTERN_MATCHING_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__IMPORTER = PATTERN_MATCHING_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__STEREOTYPE = PATTERN_MATCHING_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__TAGGED_VALUE = PATTERN_MATCHING_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__DOCUMENT = PATTERN_MATCHING_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__DESCRIPTION = PATTERN_MATCHING_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__RESPONSIBLE_PARTY = PATTERN_MATCHING_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__ELEMENT_NODE = PATTERN_MATCHING_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__SET = PATTERN_MATCHING_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__RENDERED_OBJECT = PATTERN_MATCHING_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__VOCABULARY_ELEMENT = PATTERN_MATCHING_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__MEASUREMENT = PATTERN_MATCHING_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__CHANGE_REQUEST = PATTERN_MATCHING_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__DASDL_PROPERTY = PATTERN_MATCHING_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__COUNT = PATTERN_MATCHING_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__NULL_COUNT = PATTERN_MATCHING_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__PARAMETERS = PATTERN_MATCHING_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__ANALYZED_ELEMENT = PATTERN_MATCHING_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__DATAMINING_TYPE = PATTERN_MATCHING_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__INDICATOR_DEFINITION = PATTERN_MATCHING_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__INSTANTIATED_EXPRESSIONS = PATTERN_MATCHING_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__COMPUTED = PATTERN_MATCHING_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__JOIN_CONDITIONS = PATTERN_MATCHING_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__MAX_NUMBER_ROWS = PATTERN_MATCHING_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__VALID_ROW = PATTERN_MATCHING_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__IN_VALID_ROW = PATTERN_MATCHING_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__STORE_DATA = PATTERN_MATCHING_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = PATTERN_MATCHING_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Matching Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__MATCHING_VALUE_COUNT = PATTERN_MATCHING_INDICATOR__MATCHING_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Not Matching Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR__NOT_MATCHING_VALUE_COUNT = PATTERN_MATCHING_INDICATOR__NOT_MATCHING_VALUE_COUNT;

    /**
     * The number of structural features of the '<em>Sql Pattern Matching Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SQL_PATTERN_MATCHING_INDICATOR_FEATURE_COUNT = PATTERN_MATCHING_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.RegexpMatchingIndicatorImpl <em>Regexp Matching Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.RegexpMatchingIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getRegexpMatchingIndicator()
     * @generated
     */
    int REGEXP_MATCHING_INDICATOR = 41;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__NAME = PATTERN_MATCHING_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__VISIBILITY = PATTERN_MATCHING_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__CLIENT_DEPENDENCY = PATTERN_MATCHING_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__SUPPLIER_DEPENDENCY = PATTERN_MATCHING_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__CONSTRAINT = PATTERN_MATCHING_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__NAMESPACE = PATTERN_MATCHING_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__IMPORTER = PATTERN_MATCHING_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__STEREOTYPE = PATTERN_MATCHING_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__TAGGED_VALUE = PATTERN_MATCHING_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__DOCUMENT = PATTERN_MATCHING_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__DESCRIPTION = PATTERN_MATCHING_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__RESPONSIBLE_PARTY = PATTERN_MATCHING_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__ELEMENT_NODE = PATTERN_MATCHING_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__SET = PATTERN_MATCHING_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__RENDERED_OBJECT = PATTERN_MATCHING_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__VOCABULARY_ELEMENT = PATTERN_MATCHING_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__MEASUREMENT = PATTERN_MATCHING_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__CHANGE_REQUEST = PATTERN_MATCHING_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__DASDL_PROPERTY = PATTERN_MATCHING_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__COUNT = PATTERN_MATCHING_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__NULL_COUNT = PATTERN_MATCHING_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__PARAMETERS = PATTERN_MATCHING_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__ANALYZED_ELEMENT = PATTERN_MATCHING_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__DATAMINING_TYPE = PATTERN_MATCHING_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__INDICATOR_DEFINITION = PATTERN_MATCHING_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__INSTANTIATED_EXPRESSIONS = PATTERN_MATCHING_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__COMPUTED = PATTERN_MATCHING_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__JOIN_CONDITIONS = PATTERN_MATCHING_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__MAX_NUMBER_ROWS = PATTERN_MATCHING_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__VALID_ROW = PATTERN_MATCHING_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__IN_VALID_ROW = PATTERN_MATCHING_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__STORE_DATA = PATTERN_MATCHING_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = PATTERN_MATCHING_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Matching Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__MATCHING_VALUE_COUNT = PATTERN_MATCHING_INDICATOR__MATCHING_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Not Matching Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR__NOT_MATCHING_VALUE_COUNT = PATTERN_MATCHING_INDICATOR__NOT_MATCHING_VALUE_COUNT;

    /**
     * The number of structural features of the '<em>Regexp Matching Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REGEXP_MATCHING_INDICATOR_FEATURE_COUNT = PATTERN_MATCHING_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.LowFrequencyIndicatorImpl <em>Low Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.LowFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getLowFrequencyIndicator()
     * @generated
     */
    int LOW_FREQUENCY_INDICATOR = 43;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Low Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOW_FREQUENCY_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.PatternFreqIndicatorImpl <em>Pattern Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.PatternFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPatternFreqIndicator()
     * @generated
     */
    int PATTERN_FREQ_INDICATOR = 44;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Pattern Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_FREQ_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.PatternLowFreqIndicatorImpl <em>Pattern Low Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.PatternLowFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPatternLowFreqIndicator()
     * @generated
     */
    int PATTERN_LOW_FREQ_INDICATOR = 45;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__NAME = LOW_FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__VISIBILITY = LOW_FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__CLIENT_DEPENDENCY = LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__CONSTRAINT = LOW_FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__NAMESPACE = LOW_FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__IMPORTER = LOW_FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__STEREOTYPE = LOW_FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__TAGGED_VALUE = LOW_FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__DOCUMENT = LOW_FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__DESCRIPTION = LOW_FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__RESPONSIBLE_PARTY = LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__ELEMENT_NODE = LOW_FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__SET = LOW_FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__RENDERED_OBJECT = LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__VOCABULARY_ELEMENT = LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__MEASUREMENT = LOW_FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__CHANGE_REQUEST = LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__DASDL_PROPERTY = LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__COUNT = LOW_FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__NULL_COUNT = LOW_FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__PARAMETERS = LOW_FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__ANALYZED_ELEMENT = LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__DATAMINING_TYPE = LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__COMPUTED = LOW_FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__JOIN_CONDITIONS = LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__MAX_NUMBER_ROWS = LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__VALID_ROW = LOW_FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__IN_VALID_ROW = LOW_FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__STORE_DATA = LOW_FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUES = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR__VALUE_TO_FREQ = LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Pattern Low Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATTERN_LOW_FREQ_INDICATOR_FEATURE_COUNT = LOW_FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.DefValueCountIndicatorImpl <em>Def Value Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.DefValueCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDefValueCountIndicator()
     * @generated
     */
    int DEF_VALUE_COUNT_INDICATOR = 46;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Default Val Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR__DEFAULT_VAL_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Def Value Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEF_VALUE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.SoundexFreqIndicatorImpl <em>Soundex Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.SoundexFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getSoundexFreqIndicator()
     * @generated
     */
    int SOUNDEX_FREQ_INDICATOR = 47;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The feature id for the '<em><b>Value To Distinct Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR__VALUE_TO_DISTINCT_FREQ = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Soundex Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_FREQ_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.SoundexLowFreqIndicatorImpl <em>Soundex Low Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.SoundexLowFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getSoundexLowFreqIndicator()
     * @generated
     */
    int SOUNDEX_LOW_FREQ_INDICATOR = 48;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__NAME = SOUNDEX_FREQ_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__VISIBILITY = SOUNDEX_FREQ_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__CLIENT_DEPENDENCY = SOUNDEX_FREQ_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = SOUNDEX_FREQ_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__CONSTRAINT = SOUNDEX_FREQ_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__NAMESPACE = SOUNDEX_FREQ_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__IMPORTER = SOUNDEX_FREQ_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__STEREOTYPE = SOUNDEX_FREQ_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__TAGGED_VALUE = SOUNDEX_FREQ_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__DOCUMENT = SOUNDEX_FREQ_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__DESCRIPTION = SOUNDEX_FREQ_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__RESPONSIBLE_PARTY = SOUNDEX_FREQ_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__ELEMENT_NODE = SOUNDEX_FREQ_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__SET = SOUNDEX_FREQ_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__RENDERED_OBJECT = SOUNDEX_FREQ_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__VOCABULARY_ELEMENT = SOUNDEX_FREQ_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__MEASUREMENT = SOUNDEX_FREQ_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__CHANGE_REQUEST = SOUNDEX_FREQ_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__DASDL_PROPERTY = SOUNDEX_FREQ_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__COUNT = SOUNDEX_FREQ_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__NULL_COUNT = SOUNDEX_FREQ_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__PARAMETERS = SOUNDEX_FREQ_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__ANALYZED_ELEMENT = SOUNDEX_FREQ_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__DATAMINING_TYPE = SOUNDEX_FREQ_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__INDICATOR_DEFINITION = SOUNDEX_FREQ_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = SOUNDEX_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__COMPUTED = SOUNDEX_FREQ_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__JOIN_CONDITIONS = SOUNDEX_FREQ_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__MAX_NUMBER_ROWS = SOUNDEX_FREQ_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__VALID_ROW = SOUNDEX_FREQ_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__IN_VALID_ROW = SOUNDEX_FREQ_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__STORE_DATA = SOUNDEX_FREQ_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = SOUNDEX_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__UNIQUE_VALUES = SOUNDEX_FREQ_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = SOUNDEX_FREQ_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = SOUNDEX_FREQ_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = SOUNDEX_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__VALUE_TO_FREQ = SOUNDEX_FREQ_INDICATOR__VALUE_TO_FREQ;

    /**
     * The feature id for the '<em><b>Value To Distinct Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR__VALUE_TO_DISTINCT_FREQ = SOUNDEX_FREQ_INDICATOR__VALUE_TO_DISTINCT_FREQ;

    /**
     * The number of structural features of the '<em>Soundex Low Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SOUNDEX_LOW_FREQ_INDICATOR_FEATURE_COUNT = SOUNDEX_FREQ_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.DatePatternFreqIndicatorImpl <em>Date Pattern Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.DatePatternFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDatePatternFreqIndicator()
     * @generated
     */
    int DATE_PATTERN_FREQ_INDICATOR = 49;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Date Pattern Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_PATTERN_FREQ_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.DateFrequencyIndicatorImpl <em>Date Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.DateFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDateFrequencyIndicator()
     * @generated
     */
    int DATE_FREQUENCY_INDICATOR = 50;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Date Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_FREQUENCY_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.WeekFrequencyIndicatorImpl <em>Week Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.WeekFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWeekFrequencyIndicator()
     * @generated
     */
    int WEEK_FREQUENCY_INDICATOR = 51;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Week Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_FREQUENCY_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MonthFrequencyIndicatorImpl <em>Month Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MonthFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMonthFrequencyIndicator()
     * @generated
     */
    int MONTH_FREQUENCY_INDICATOR = 52;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Month Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_FREQUENCY_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.QuarterFrequencyIndicatorImpl <em>Quarter Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.QuarterFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getQuarterFrequencyIndicator()
     * @generated
     */
    int QUARTER_FREQUENCY_INDICATOR = 53;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Quarter Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_FREQUENCY_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.YearFrequencyIndicatorImpl <em>Year Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.YearFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getYearFrequencyIndicator()
     * @generated
     */
    int YEAR_FREQUENCY_INDICATOR = 54;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Year Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_FREQUENCY_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.BinFrequencyIndicatorImpl <em>Bin Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.BinFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBinFrequencyIndicator()
     * @generated
     */
    int BIN_FREQUENCY_INDICATOR = 55;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Bin Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_FREQUENCY_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.DateLowFrequencyIndicatorImpl <em>Date Low Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.DateLowFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDateLowFrequencyIndicator()
     * @generated
     */
    int DATE_LOW_FREQUENCY_INDICATOR = 56;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__NAME = LOW_FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__VISIBILITY = LOW_FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__CONSTRAINT = LOW_FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__NAMESPACE = LOW_FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__IMPORTER = LOW_FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__STEREOTYPE = LOW_FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__TAGGED_VALUE = LOW_FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__DOCUMENT = LOW_FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__DESCRIPTION = LOW_FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__ELEMENT_NODE = LOW_FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__SET = LOW_FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT = LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__MEASUREMENT = LOW_FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST = LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY = LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__COUNT = LOW_FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__NULL_COUNT = LOW_FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__PARAMETERS = LOW_FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE = LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__COMPUTED = LOW_FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS = LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__VALID_ROW = LOW_FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__IN_VALID_ROW = LOW_FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__STORE_DATA = LOW_FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ = LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Date Low Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATE_LOW_FREQUENCY_INDICATOR_FEATURE_COUNT = LOW_FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.WeekLowFrequencyIndicatorImpl <em>Week Low Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.WeekLowFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWeekLowFrequencyIndicator()
     * @generated
     */
    int WEEK_LOW_FREQUENCY_INDICATOR = 57;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__NAME = LOW_FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__VISIBILITY = LOW_FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__CONSTRAINT = LOW_FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__NAMESPACE = LOW_FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__IMPORTER = LOW_FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__STEREOTYPE = LOW_FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__TAGGED_VALUE = LOW_FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__DOCUMENT = LOW_FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__DESCRIPTION = LOW_FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__ELEMENT_NODE = LOW_FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__SET = LOW_FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT = LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__MEASUREMENT = LOW_FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST = LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY = LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__COUNT = LOW_FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__NULL_COUNT = LOW_FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__PARAMETERS = LOW_FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE = LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__COMPUTED = LOW_FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS = LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__VALID_ROW = LOW_FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__IN_VALID_ROW = LOW_FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__STORE_DATA = LOW_FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ = LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Week Low Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WEEK_LOW_FREQUENCY_INDICATOR_FEATURE_COUNT = LOW_FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.MonthLowFrequencyIndicatorImpl <em>Month Low Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.MonthLowFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMonthLowFrequencyIndicator()
     * @generated
     */
    int MONTH_LOW_FREQUENCY_INDICATOR = 58;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__NAME = LOW_FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__VISIBILITY = LOW_FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__CONSTRAINT = LOW_FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__NAMESPACE = LOW_FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__IMPORTER = LOW_FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__STEREOTYPE = LOW_FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__TAGGED_VALUE = LOW_FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__DOCUMENT = LOW_FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__DESCRIPTION = LOW_FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__ELEMENT_NODE = LOW_FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__SET = LOW_FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT = LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__MEASUREMENT = LOW_FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST = LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY = LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__COUNT = LOW_FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__NULL_COUNT = LOW_FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__PARAMETERS = LOW_FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE = LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__COMPUTED = LOW_FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS = LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__VALID_ROW = LOW_FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__IN_VALID_ROW = LOW_FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__STORE_DATA = LOW_FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ = LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Month Low Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONTH_LOW_FREQUENCY_INDICATOR_FEATURE_COUNT = LOW_FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.QuarterLowFrequencyIndicatorImpl <em>Quarter Low Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.QuarterLowFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getQuarterLowFrequencyIndicator()
     * @generated
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR = 59;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__NAME = LOW_FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__VISIBILITY = LOW_FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__CONSTRAINT = LOW_FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__NAMESPACE = LOW_FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__IMPORTER = LOW_FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__STEREOTYPE = LOW_FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__TAGGED_VALUE = LOW_FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__DOCUMENT = LOW_FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__DESCRIPTION = LOW_FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__ELEMENT_NODE = LOW_FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__SET = LOW_FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT = LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__MEASUREMENT = LOW_FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST = LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY = LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__COUNT = LOW_FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__NULL_COUNT = LOW_FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__PARAMETERS = LOW_FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE = LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__COMPUTED = LOW_FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS = LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__VALID_ROW = LOW_FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__IN_VALID_ROW = LOW_FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__STORE_DATA = LOW_FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ = LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Quarter Low Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUARTER_LOW_FREQUENCY_INDICATOR_FEATURE_COUNT = LOW_FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.YearLowFrequencyIndicatorImpl <em>Year Low Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.YearLowFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getYearLowFrequencyIndicator()
     * @generated
     */
    int YEAR_LOW_FREQUENCY_INDICATOR = 60;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__NAME = LOW_FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__VISIBILITY = LOW_FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__CONSTRAINT = LOW_FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__NAMESPACE = LOW_FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__IMPORTER = LOW_FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__STEREOTYPE = LOW_FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__TAGGED_VALUE = LOW_FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__DOCUMENT = LOW_FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__DESCRIPTION = LOW_FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__ELEMENT_NODE = LOW_FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__SET = LOW_FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT = LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__MEASUREMENT = LOW_FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST = LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY = LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__COUNT = LOW_FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__NULL_COUNT = LOW_FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__PARAMETERS = LOW_FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE = LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__COMPUTED = LOW_FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS = LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__VALID_ROW = LOW_FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__IN_VALID_ROW = LOW_FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__STORE_DATA = LOW_FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ = LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Year Low Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int YEAR_LOW_FREQUENCY_INDICATOR_FEATURE_COUNT = LOW_FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.BinLowFrequencyIndicatorImpl <em>Bin Low Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.BinLowFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBinLowFrequencyIndicator()
     * @generated
     */
    int BIN_LOW_FREQUENCY_INDICATOR = 61;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__NAME = LOW_FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__VISIBILITY = LOW_FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = LOW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = LOW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__CONSTRAINT = LOW_FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__NAMESPACE = LOW_FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__IMPORTER = LOW_FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__STEREOTYPE = LOW_FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__TAGGED_VALUE = LOW_FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__DOCUMENT = LOW_FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__DESCRIPTION = LOW_FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = LOW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__ELEMENT_NODE = LOW_FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__SET = LOW_FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT = LOW_FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = LOW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__MEASUREMENT = LOW_FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST = LOW_FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY = LOW_FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__COUNT = LOW_FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__NULL_COUNT = LOW_FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__PARAMETERS = LOW_FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = LOW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE = LOW_FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = LOW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__COMPUTED = LOW_FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS = LOW_FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = LOW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__VALID_ROW = LOW_FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__IN_VALID_ROW = LOW_FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__STORE_DATA = LOW_FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = LOW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = LOW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ = LOW_FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Bin Low Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BIN_LOW_FREQUENCY_INDICATOR_FEATURE_COUNT = LOW_FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.ValidPhoneCountIndicatorImpl <em>Valid Phone Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.ValidPhoneCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getValidPhoneCountIndicator()
     * @generated
     */
    int VALID_PHONE_COUNT_INDICATOR = 62;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Valid Phone Num Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR__VALID_PHONE_NUM_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Valid Phone Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_PHONE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.PossiblePhoneCountIndicatorImpl <em>Possible Phone Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.PossiblePhoneCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPossiblePhoneCountIndicator()
     * @generated
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR = 63;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Possible Phone Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR__POSSIBLE_PHONE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Possible Phone Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSSIBLE_PHONE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.ValidRegCodeCountIndicatorImpl <em>Valid Reg Code Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.ValidRegCodeCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getValidRegCodeCountIndicator()
     * @generated
     */
    int VALID_REG_CODE_COUNT_INDICATOR = 64;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Valid Reg Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR__VALID_REG_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Valid Reg Code Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALID_REG_CODE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.InvalidRegCodeCountIndicatorImpl <em>Invalid Reg Code Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.InvalidRegCodeCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getInvalidRegCodeCountIndicator()
     * @generated
     */
    int INVALID_REG_CODE_COUNT_INDICATOR = 65;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Invalid Reg Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR__INVALID_REG_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Invalid Reg Code Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVALID_REG_CODE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.WellFormNationalPhoneCountIndicatorImpl <em>Well Form National Phone Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.WellFormNationalPhoneCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWellFormNationalPhoneCountIndicator()
     * @generated
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR = 66;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Well Form Nati Phone Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__WELL_FORM_NATI_PHONE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Well Form National Phone Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.WellFormIntePhoneCountIndicatorImpl <em>Well Form Inte Phone Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.WellFormIntePhoneCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWellFormIntePhoneCountIndicator()
     * @generated
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR = 67;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Well Form Inte Phone Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR__WELL_FORM_INTE_PHONE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Well Form Inte Phone Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_INTE_PHONE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl <em>Well Form E164 Phone Count Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWellFormE164PhoneCountIndicator()
     * @generated
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR = 68;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__NAME = INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__VISIBILITY = INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__CLIENT_DEPENDENCY = INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__SUPPLIER_DEPENDENCY = INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__CONSTRAINT = INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__NAMESPACE = INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__IMPORTER = INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__STEREOTYPE = INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__TAGGED_VALUE = INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__DOCUMENT = INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__DESCRIPTION = INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__RESPONSIBLE_PARTY = INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__ELEMENT_NODE = INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__SET = INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__RENDERED_OBJECT = INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__VOCABULARY_ELEMENT = INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__MEASUREMENT = INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__CHANGE_REQUEST = INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__DASDL_PROPERTY = INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__COUNT = INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__NULL_COUNT = INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__PARAMETERS = INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__ANALYZED_ELEMENT = INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__DATAMINING_TYPE = INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__INDICATOR_DEFINITION = INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__INSTANTIATED_EXPRESSIONS = INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__COMPUTED = INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__JOIN_CONDITIONS = INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__MAX_NUMBER_ROWS = INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__VALID_ROW = INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__IN_VALID_ROW = INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__STORE_DATA = INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Well Form E164 Phone Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR__WELL_FORM_E164_PHONE_COUNT = INDICATOR_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Well Form E164 Phone Count Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WELL_FORM_E164_PHONE_COUNT_INDICATOR_FEATURE_COUNT = INDICATOR_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl <em>Phone Numb Statistics Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPhoneNumbStatisticsIndicator()
     * @generated
     */
    int PHONE_NUMB_STATISTICS_INDICATOR = 69;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__NAME = COMPOSITE_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__VISIBILITY = COMPOSITE_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__CLIENT_DEPENDENCY = COMPOSITE_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__SUPPLIER_DEPENDENCY = COMPOSITE_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__CONSTRAINT = COMPOSITE_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__NAMESPACE = COMPOSITE_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__IMPORTER = COMPOSITE_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__STEREOTYPE = COMPOSITE_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__TAGGED_VALUE = COMPOSITE_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__DOCUMENT = COMPOSITE_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__DESCRIPTION = COMPOSITE_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__RESPONSIBLE_PARTY = COMPOSITE_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__ELEMENT_NODE = COMPOSITE_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__SET = COMPOSITE_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__RENDERED_OBJECT = COMPOSITE_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__VOCABULARY_ELEMENT = COMPOSITE_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__MEASUREMENT = COMPOSITE_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__CHANGE_REQUEST = COMPOSITE_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__DASDL_PROPERTY = COMPOSITE_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__COUNT = COMPOSITE_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__NULL_COUNT = COMPOSITE_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__PARAMETERS = COMPOSITE_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__ANALYZED_ELEMENT = COMPOSITE_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__DATAMINING_TYPE = COMPOSITE_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__INDICATOR_DEFINITION = COMPOSITE_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__INSTANTIATED_EXPRESSIONS = COMPOSITE_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__COMPUTED = COMPOSITE_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__JOIN_CONDITIONS = COMPOSITE_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__MAX_NUMBER_ROWS = COMPOSITE_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__VALID_ROW = COMPOSITE_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__IN_VALID_ROW = COMPOSITE_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__STORE_DATA = COMPOSITE_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = COMPOSITE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Well Form National Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Well Form Inte Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Well Form E164 Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Invalid Reg Code Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Possible Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Valid Reg Code Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Valid Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Format Freq Pie Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR = COMPOSITE_INDICATOR_FEATURE_COUNT + 7;

    /**
     * The number of structural features of the '<em>Phone Numb Statistics Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PHONE_NUMB_STATISTICS_INDICATOR_FEATURE_COUNT = COMPOSITE_INDICATOR_FEATURE_COUNT + 8;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.FormatFreqPieIndicatorImpl <em>Format Freq Pie Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.FormatFreqPieIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getFormatFreqPieIndicator()
     * @generated
     */
    int FORMAT_FREQ_PIE_INDICATOR = 70;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The feature id for the '<em><b>Well Form E164 Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__WELL_FORM_E164_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Well Form Inte Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__WELL_FORM_INTE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Well Form Nati Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__WELL_FORM_NATI_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Invalid Form Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__INVALID_FORM_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Current Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR__CURRENT_KEY = FREQUENCY_INDICATOR_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Format Freq Pie Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FORMAT_FREQ_PIE_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.BenfordLawFrequencyIndicatorImpl <em>Benford Law Frequency Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.BenfordLawFrequencyIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBenfordLawFrequencyIndicator()
     * @generated
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR = 71;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>Benford Law Frequency Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BENFORD_LAW_FREQUENCY_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.EastAsiaPatternFreqIndicatorImpl <em>East Asia Pattern Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.EastAsiaPatternFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getEastAsiaPatternFreqIndicator()
     * @generated
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR = 72;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__NAME = PATTERN_FREQ_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__VISIBILITY = PATTERN_FREQ_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__CLIENT_DEPENDENCY = PATTERN_FREQ_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = PATTERN_FREQ_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__CONSTRAINT = PATTERN_FREQ_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__NAMESPACE = PATTERN_FREQ_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__IMPORTER = PATTERN_FREQ_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__STEREOTYPE = PATTERN_FREQ_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__TAGGED_VALUE = PATTERN_FREQ_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__DOCUMENT = PATTERN_FREQ_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__DESCRIPTION = PATTERN_FREQ_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__RESPONSIBLE_PARTY = PATTERN_FREQ_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__ELEMENT_NODE = PATTERN_FREQ_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__SET = PATTERN_FREQ_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__RENDERED_OBJECT = PATTERN_FREQ_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__VOCABULARY_ELEMENT = PATTERN_FREQ_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__MEASUREMENT = PATTERN_FREQ_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__CHANGE_REQUEST = PATTERN_FREQ_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__DASDL_PROPERTY = PATTERN_FREQ_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__COUNT = PATTERN_FREQ_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__NULL_COUNT = PATTERN_FREQ_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__PARAMETERS = PATTERN_FREQ_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__ANALYZED_ELEMENT = PATTERN_FREQ_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__DATAMINING_TYPE = PATTERN_FREQ_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__INDICATOR_DEFINITION = PATTERN_FREQ_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = PATTERN_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__COMPUTED = PATTERN_FREQ_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__JOIN_CONDITIONS = PATTERN_FREQ_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__MAX_NUMBER_ROWS = PATTERN_FREQ_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__VALID_ROW = PATTERN_FREQ_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__IN_VALID_ROW = PATTERN_FREQ_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__STORE_DATA = PATTERN_FREQ_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = PATTERN_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__UNIQUE_VALUES = PATTERN_FREQ_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = PATTERN_FREQ_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = PATTERN_FREQ_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = PATTERN_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR__VALUE_TO_FREQ = PATTERN_FREQ_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>East Asia Pattern Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_FREQ_INDICATOR_FEATURE_COUNT = PATTERN_FREQ_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.EastAsiaPatternLowFreqIndicatorImpl <em>East Asia Pattern Low Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.EastAsiaPatternLowFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getEastAsiaPatternLowFreqIndicator()
     * @generated
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR = 73;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__NAME = PATTERN_LOW_FREQ_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__VISIBILITY = PATTERN_LOW_FREQ_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__CLIENT_DEPENDENCY = PATTERN_LOW_FREQ_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = PATTERN_LOW_FREQ_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__CONSTRAINT = PATTERN_LOW_FREQ_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__NAMESPACE = PATTERN_LOW_FREQ_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__IMPORTER = PATTERN_LOW_FREQ_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__STEREOTYPE = PATTERN_LOW_FREQ_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__TAGGED_VALUE = PATTERN_LOW_FREQ_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__DOCUMENT = PATTERN_LOW_FREQ_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__DESCRIPTION = PATTERN_LOW_FREQ_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__RESPONSIBLE_PARTY = PATTERN_LOW_FREQ_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__ELEMENT_NODE = PATTERN_LOW_FREQ_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__SET = PATTERN_LOW_FREQ_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__RENDERED_OBJECT = PATTERN_LOW_FREQ_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__VOCABULARY_ELEMENT = PATTERN_LOW_FREQ_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__MEASUREMENT = PATTERN_LOW_FREQ_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__CHANGE_REQUEST = PATTERN_LOW_FREQ_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__DASDL_PROPERTY = PATTERN_LOW_FREQ_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__COUNT = PATTERN_LOW_FREQ_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__NULL_COUNT = PATTERN_LOW_FREQ_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__PARAMETERS = PATTERN_LOW_FREQ_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__ANALYZED_ELEMENT = PATTERN_LOW_FREQ_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__DATAMINING_TYPE = PATTERN_LOW_FREQ_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__INDICATOR_DEFINITION = PATTERN_LOW_FREQ_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = PATTERN_LOW_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__COMPUTED = PATTERN_LOW_FREQ_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__JOIN_CONDITIONS = PATTERN_LOW_FREQ_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__MAX_NUMBER_ROWS = PATTERN_LOW_FREQ_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__VALID_ROW = PATTERN_LOW_FREQ_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__IN_VALID_ROW = PATTERN_LOW_FREQ_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__STORE_DATA = PATTERN_LOW_FREQ_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = PATTERN_LOW_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUES = PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = PATTERN_LOW_FREQ_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = PATTERN_LOW_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR__VALUE_TO_FREQ = PATTERN_LOW_FREQ_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>East Asia Pattern Low Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR_FEATURE_COUNT = PATTERN_LOW_FREQ_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.CSWordPatternFreqIndicatorImpl <em>CS Word Pattern Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.CSWordPatternFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCSWordPatternFreqIndicator()
     * @generated
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR = 74;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>CS Word Pattern Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_FREQ_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.CSWordPatternLowFreqIndicatorImpl <em>CS Word Pattern Low Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.CSWordPatternLowFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCSWordPatternLowFreqIndicator()
     * @generated
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR = 75;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>CS Word Pattern Low Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CS_WORD_PATTERN_LOW_FREQ_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.CIWordPatternFreqIndicatorImpl <em>CI Word Pattern Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.CIWordPatternFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCIWordPatternFreqIndicator()
     * @generated
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR = 76;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>CI Word Pattern Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_FREQ_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.impl.CIWordPatternLowFreqIndicatorImpl <em>CI Word Pattern Low Freq Indicator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.CIWordPatternLowFreqIndicatorImpl
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCIWordPatternLowFreqIndicator()
     * @generated
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR = 77;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__NAME = FREQUENCY_INDICATOR__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__VISIBILITY = FREQUENCY_INDICATOR__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__CLIENT_DEPENDENCY = FREQUENCY_INDICATOR__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__SUPPLIER_DEPENDENCY = FREQUENCY_INDICATOR__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__CONSTRAINT = FREQUENCY_INDICATOR__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__NAMESPACE = FREQUENCY_INDICATOR__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__IMPORTER = FREQUENCY_INDICATOR__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__STEREOTYPE = FREQUENCY_INDICATOR__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__TAGGED_VALUE = FREQUENCY_INDICATOR__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__DOCUMENT = FREQUENCY_INDICATOR__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__DESCRIPTION = FREQUENCY_INDICATOR__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__RESPONSIBLE_PARTY = FREQUENCY_INDICATOR__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__ELEMENT_NODE = FREQUENCY_INDICATOR__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__SET = FREQUENCY_INDICATOR__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__RENDERED_OBJECT = FREQUENCY_INDICATOR__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__VOCABULARY_ELEMENT = FREQUENCY_INDICATOR__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__MEASUREMENT = FREQUENCY_INDICATOR__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__CHANGE_REQUEST = FREQUENCY_INDICATOR__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__DASDL_PROPERTY = FREQUENCY_INDICATOR__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__COUNT = FREQUENCY_INDICATOR__COUNT;

    /**
     * The feature id for the '<em><b>Null Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__NULL_COUNT = FREQUENCY_INDICATOR__NULL_COUNT;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__PARAMETERS = FREQUENCY_INDICATOR__PARAMETERS;

    /**
     * The feature id for the '<em><b>Analyzed Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__ANALYZED_ELEMENT = FREQUENCY_INDICATOR__ANALYZED_ELEMENT;

    /**
     * The feature id for the '<em><b>Datamining Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__DATAMINING_TYPE = FREQUENCY_INDICATOR__DATAMINING_TYPE;

    /**
     * The feature id for the '<em><b>Indicator Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__INDICATOR_DEFINITION = FREQUENCY_INDICATOR__INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Instantiated Expressions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__INSTANTIATED_EXPRESSIONS = FREQUENCY_INDICATOR__INSTANTIATED_EXPRESSIONS;

    /**
     * The feature id for the '<em><b>Computed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__COMPUTED = FREQUENCY_INDICATOR__COMPUTED;

    /**
     * The feature id for the '<em><b>Join Conditions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__JOIN_CONDITIONS = FREQUENCY_INDICATOR__JOIN_CONDITIONS;

    /**
     * The feature id for the '<em><b>Max Number Rows</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__MAX_NUMBER_ROWS = FREQUENCY_INDICATOR__MAX_NUMBER_ROWS;

    /**
     * The feature id for the '<em><b>Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__VALID_ROW = FREQUENCY_INDICATOR__VALID_ROW;

    /**
     * The feature id for the '<em><b>In Valid Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__IN_VALID_ROW = FREQUENCY_INDICATOR__IN_VALID_ROW;

    /**
     * The feature id for the '<em><b>Store Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__STORE_DATA = FREQUENCY_INDICATOR__STORE_DATA;

    /**
     * The feature id for the '<em><b>Built In Indicator Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__BUILT_IN_INDICATOR_DEFINITION = FREQUENCY_INDICATOR__BUILT_IN_INDICATOR_DEFINITION;

    /**
     * The feature id for the '<em><b>Unique Values</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUES = FREQUENCY_INDICATOR__UNIQUE_VALUES;

    /**
     * The feature id for the '<em><b>Distinct Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__DISTINCT_VALUE_COUNT = FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Unique Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__UNIQUE_VALUE_COUNT = FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Duplicate Value Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__DUPLICATE_VALUE_COUNT = FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT;

    /**
     * The feature id for the '<em><b>Value To Freq</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR__VALUE_TO_FREQ = FREQUENCY_INDICATOR__VALUE_TO_FREQ;

    /**
     * The number of structural features of the '<em>CI Word Pattern Low Freq Indicator</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CI_WORD_PATTERN_LOW_FREQ_INDICATOR_FEATURE_COUNT = FREQUENCY_INDICATOR_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.EnumStatistics <em>Enum Statistics</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.EnumStatistics
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getEnumStatistics()
     * @generated
     */
    int ENUM_STATISTICS = 78;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.DataminingType <em>Datamining Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.DataminingType
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDataminingType()
     * @generated
     */
    int DATAMINING_TYPE = 79;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.DateGrain <em>Date Grain</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.DateGrain
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDateGrain()
     * @generated
     */
    int DATE_GRAIN = 80;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.MatchingAlgorithm <em>Matching Algorithm</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.MatchingAlgorithm
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMatchingAlgorithm()
     * @generated
     */
    int MATCHING_ALGORITHM = 81;

    /**
     * The meta object id for the '{@link org.talend.dataquality.indicators.IndicatorValueType <em>Indicator Value Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.IndicatorValueType
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getIndicatorValueType()
     * @generated
     */
    int INDICATOR_VALUE_TYPE = 82;

    /**
     * The meta object id for the '<em>Java Set</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.Set
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getJavaSet()
     * @generated
     */
    int JAVA_SET = 83;

    /**
     * The meta object id for the '<em>Java Hash Map</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.HashMap
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getJavaHashMap()
     * @generated
     */
    int JAVA_HASH_MAP = 84;

    /**
     * The meta object id for the '<em>Java Tree Map</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.TreeMap
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getJavaTreeMap()
     * @generated
     */
    int JAVA_TREE_MAP = 85;

    /**
     * The meta object id for the '<em>Object Array</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getObjectArray()
     * @generated
     */
    int OBJECT_ARRAY = 86;

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.Indicator <em>Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Indicator</em>'.
     * @see org.talend.dataquality.indicators.Indicator
     * @generated
     */
    EClass getIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.Indicator#getCount <em>Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Count</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getCount()
     * @see #getIndicator()
     * @generated
     */
    EAttribute getIndicator_Count();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.Indicator#getNullCount <em>Null Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Null Count</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getNullCount()
     * @see #getIndicator()
     * @generated
     */
    EAttribute getIndicator_NullCount();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.Indicator#getParameters <em>Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Parameters</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getParameters()
     * @see #getIndicator()
     * @generated
     */
    EReference getIndicator_Parameters();

    /**
     * Returns the meta object for the reference '{@link org.talend.dataquality.indicators.Indicator#getAnalyzedElement <em>Analyzed Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Analyzed Element</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getAnalyzedElement()
     * @see #getIndicator()
     * @generated
     */
    EReference getIndicator_AnalyzedElement();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.Indicator#getDataminingType <em>Datamining Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Datamining Type</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getDataminingType()
     * @see #getIndicator()
     * @generated
     */
    EAttribute getIndicator_DataminingType();

    /**
     * Returns the meta object for the reference '{@link org.talend.dataquality.indicators.Indicator#getIndicatorDefinition <em>Indicator Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Indicator Definition</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getIndicatorDefinition()
     * @see #getIndicator()
     * @generated
     */
    EReference getIndicator_IndicatorDefinition();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.indicators.Indicator#getInstantiatedExpressions <em>Instantiated Expressions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Instantiated Expressions</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getInstantiatedExpressions()
     * @see #getIndicator()
     * @generated
     */
    EReference getIndicator_InstantiatedExpressions();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.Indicator#isComputed <em>Computed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Computed</em>'.
     * @see org.talend.dataquality.indicators.Indicator#isComputed()
     * @see #getIndicator()
     * @generated
     */
    EAttribute getIndicator_Computed();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.indicators.Indicator#getJoinConditions <em>Join Conditions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Join Conditions</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getJoinConditions()
     * @see #getIndicator()
     * @generated
     */
    EReference getIndicator_JoinConditions();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.Indicator#getMaxNumberRows <em>Max Number Rows</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max Number Rows</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getMaxNumberRows()
     * @see #getIndicator()
     * @generated
     */
    EAttribute getIndicator_MaxNumberRows();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.Indicator#isValidRow <em>Valid Row</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Valid Row</em>'.
     * @see org.talend.dataquality.indicators.Indicator#isValidRow()
     * @see #getIndicator()
     * @generated
     */
    EAttribute getIndicator_ValidRow();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.Indicator#isInValidRow <em>In Valid Row</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>In Valid Row</em>'.
     * @see org.talend.dataquality.indicators.Indicator#isInValidRow()
     * @see #getIndicator()
     * @generated
     */
    EAttribute getIndicator_InValidRow();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.Indicator#isStoreData <em>Store Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Store Data</em>'.
     * @see org.talend.dataquality.indicators.Indicator#isStoreData()
     * @see #getIndicator()
     * @generated
     */
    EAttribute getIndicator_StoreData();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.Indicator#getBuiltInIndicatorDefinition <em>Built In Indicator Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Built In Indicator Definition</em>'.
     * @see org.talend.dataquality.indicators.Indicator#getBuiltInIndicatorDefinition()
     * @see #getIndicator()
     * @generated
     */
    EReference getIndicator_BuiltInIndicatorDefinition();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.RowCountIndicator <em>Row Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Row Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.RowCountIndicator
     * @generated
     */
    EClass getRowCountIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MeanIndicator <em>Mean Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Mean Indicator</em>'.
     * @see org.talend.dataquality.indicators.MeanIndicator
     * @generated
     */
    EClass getMeanIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.SumIndicator <em>Sum Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sum Indicator</em>'.
     * @see org.talend.dataquality.indicators.SumIndicator
     * @generated
     */
    EClass getSumIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.SumIndicator#getSumStr <em>Sum Str</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sum Str</em>'.
     * @see org.talend.dataquality.indicators.SumIndicator#getSumStr()
     * @see #getSumIndicator()
     * @generated
     */
    EAttribute getSumIndicator_SumStr();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.SumIndicator#getDatatype <em>Datatype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Datatype</em>'.
     * @see org.talend.dataquality.indicators.SumIndicator#getDatatype()
     * @see #getSumIndicator()
     * @generated
     */
    EAttribute getSumIndicator_Datatype();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.CompositeIndicator <em>Composite Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Composite Indicator</em>'.
     * @see org.talend.dataquality.indicators.CompositeIndicator
     * @generated
     */
    EClass getCompositeIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.RangeIndicator <em>Range Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Range Indicator</em>'.
     * @see org.talend.dataquality.indicators.RangeIndicator
     * @generated
     */
    EClass getRangeIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.RangeIndicator#getLowerValue <em>Lower Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Lower Value</em>'.
     * @see org.talend.dataquality.indicators.RangeIndicator#getLowerValue()
     * @see #getRangeIndicator()
     * @generated
     */
    EReference getRangeIndicator_LowerValue();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.RangeIndicator#getUpperValue <em>Upper Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Upper Value</em>'.
     * @see org.talend.dataquality.indicators.RangeIndicator#getUpperValue()
     * @see #getRangeIndicator()
     * @generated
     */
    EReference getRangeIndicator_UpperValue();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.RangeIndicator#getDatatype <em>Datatype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Datatype</em>'.
     * @see org.talend.dataquality.indicators.RangeIndicator#getDatatype()
     * @see #getRangeIndicator()
     * @generated
     */
    EAttribute getRangeIndicator_Datatype();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.RangeIndicator#getRange <em>Range</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Range</em>'.
     * @see org.talend.dataquality.indicators.RangeIndicator#getRange()
     * @see #getRangeIndicator()
     * @generated
     */
    EAttribute getRangeIndicator_Range();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.BoxIndicator <em>Box Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Box Indicator</em>'.
     * @see org.talend.dataquality.indicators.BoxIndicator
     * @generated
     */
    EClass getBoxIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.BoxIndicator#getIQR <em>IQR</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>IQR</em>'.
     * @see org.talend.dataquality.indicators.BoxIndicator#getIQR()
     * @see #getBoxIndicator()
     * @generated
     */
    EReference getBoxIndicator_IQR();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.BoxIndicator#getRangeIndicator <em>Range Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Range Indicator</em>'.
     * @see org.talend.dataquality.indicators.BoxIndicator#getRangeIndicator()
     * @see #getBoxIndicator()
     * @generated
     */
    EReference getBoxIndicator_RangeIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.BoxIndicator#getMeanIndicator <em>Mean Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Mean Indicator</em>'.
     * @see org.talend.dataquality.indicators.BoxIndicator#getMeanIndicator()
     * @see #getBoxIndicator()
     * @generated
     */
    EReference getBoxIndicator_MeanIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.BoxIndicator#getMedianIndicator <em>Median Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Median Indicator</em>'.
     * @see org.talend.dataquality.indicators.BoxIndicator#getMedianIndicator()
     * @see #getBoxIndicator()
     * @generated
     */
    EReference getBoxIndicator_MedianIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.FrequencyIndicator <em>Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.FrequencyIndicator
     * @generated
     */
    EClass getFrequencyIndicator();

    /**
     * Returns the meta object for the attribute list '{@link org.talend.dataquality.indicators.FrequencyIndicator#getUniqueValues <em>Unique Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Unique Values</em>'.
     * @see org.talend.dataquality.indicators.FrequencyIndicator#getUniqueValues()
     * @see #getFrequencyIndicator()
     * @generated
     */
    EAttribute getFrequencyIndicator_UniqueValues();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.FrequencyIndicator#getDistinctValueCount <em>Distinct Value Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Distinct Value Count</em>'.
     * @see org.talend.dataquality.indicators.FrequencyIndicator#getDistinctValueCount()
     * @see #getFrequencyIndicator()
     * @generated
     */
    EAttribute getFrequencyIndicator_DistinctValueCount();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.FrequencyIndicator#getUniqueValueCount <em>Unique Value Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Unique Value Count</em>'.
     * @see org.talend.dataquality.indicators.FrequencyIndicator#getUniqueValueCount()
     * @see #getFrequencyIndicator()
     * @generated
     */
    EAttribute getFrequencyIndicator_UniqueValueCount();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.FrequencyIndicator#getDuplicateValueCount <em>Duplicate Value Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Duplicate Value Count</em>'.
     * @see org.talend.dataquality.indicators.FrequencyIndicator#getDuplicateValueCount()
     * @see #getFrequencyIndicator()
     * @generated
     */
    EAttribute getFrequencyIndicator_DuplicateValueCount();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.FrequencyIndicator#getValueToFreq <em>Value To Freq</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value To Freq</em>'.
     * @see org.talend.dataquality.indicators.FrequencyIndicator#getValueToFreq()
     * @see #getFrequencyIndicator()
     * @generated
     */
    EAttribute getFrequencyIndicator_ValueToFreq();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.BlankCountIndicator <em>Blank Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Blank Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.BlankCountIndicator
     * @generated
     */
    EClass getBlankCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.BlankCountIndicator#getBlankCount <em>Blank Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Blank Count</em>'.
     * @see org.talend.dataquality.indicators.BlankCountIndicator#getBlankCount()
     * @see #getBlankCountIndicator()
     * @generated
     */
    EAttribute getBlankCountIndicator_BlankCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.IndicatorParameters <em>Indicator Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Indicator Parameters</em>'.
     * @see org.talend.dataquality.indicators.IndicatorParameters
     * @generated
     */
    EClass getIndicatorParameters();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.IndicatorParameters#getIndicatorValidDomain <em>Indicator Valid Domain</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Indicator Valid Domain</em>'.
     * @see org.talend.dataquality.indicators.IndicatorParameters#getIndicatorValidDomain()
     * @see #getIndicatorParameters()
     * @generated
     */
    EReference getIndicatorParameters_IndicatorValidDomain();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.IndicatorParameters#getDataValidDomain <em>Data Valid Domain</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Valid Domain</em>'.
     * @see org.talend.dataquality.indicators.IndicatorParameters#getDataValidDomain()
     * @see #getIndicatorParameters()
     * @generated
     */
    EReference getIndicatorParameters_DataValidDomain();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.IndicatorParameters#getBins <em>Bins</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Bins</em>'.
     * @see org.talend.dataquality.indicators.IndicatorParameters#getBins()
     * @see #getIndicatorParameters()
     * @generated
     */
    EReference getIndicatorParameters_Bins();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.IndicatorParameters#getTextParameter <em>Text Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Text Parameter</em>'.
     * @see org.talend.dataquality.indicators.IndicatorParameters#getTextParameter()
     * @see #getIndicatorParameters()
     * @generated
     */
    EReference getIndicatorParameters_TextParameter();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.IndicatorParameters#getDateParameters <em>Date Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Date Parameters</em>'.
     * @see org.talend.dataquality.indicators.IndicatorParameters#getDateParameters()
     * @see #getIndicatorParameters()
     * @generated
     */
    EReference getIndicatorParameters_DateParameters();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.IndicatorParameters#getTopN <em>Top N</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Top N</em>'.
     * @see org.talend.dataquality.indicators.IndicatorParameters#getTopN()
     * @see #getIndicatorParameters()
     * @generated
     */
    EAttribute getIndicatorParameters_TopN();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MedianIndicator <em>Median Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Median Indicator</em>'.
     * @see org.talend.dataquality.indicators.MedianIndicator
     * @generated
     */
    EClass getMedianIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.MedianIndicator#getMedian <em>Median</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Median</em>'.
     * @see org.talend.dataquality.indicators.MedianIndicator#getMedian()
     * @see #getMedianIndicator()
     * @generated
     */
    EAttribute getMedianIndicator_Median();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.MedianIndicator#getFrequenceTable <em>Frequence Table</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Frequence Table</em>'.
     * @see org.talend.dataquality.indicators.MedianIndicator#getFrequenceTable()
     * @see #getMedianIndicator()
     * @generated
     */
    EAttribute getMedianIndicator_FrequenceTable();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.MedianIndicator#getDateMedian <em>Date Median</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Date Median</em>'.
     * @see org.talend.dataquality.indicators.MedianIndicator#getDateMedian()
     * @see #getMedianIndicator()
     * @generated
     */
    EAttribute getMedianIndicator_DateMedian();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.ValueIndicator <em>Value Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Value Indicator</em>'.
     * @see org.talend.dataquality.indicators.ValueIndicator
     * @generated
     */
    EClass getValueIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.ValueIndicator#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.talend.dataquality.indicators.ValueIndicator#getValue()
     * @see #getValueIndicator()
     * @generated
     */
    EAttribute getValueIndicator_Value();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.ValueIndicator#getDatatype <em>Datatype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Datatype</em>'.
     * @see org.talend.dataquality.indicators.ValueIndicator#getDatatype()
     * @see #getValueIndicator()
     * @generated
     */
    EAttribute getValueIndicator_Datatype();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MinValueIndicator <em>Min Value Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Min Value Indicator</em>'.
     * @see org.talend.dataquality.indicators.MinValueIndicator
     * @generated
     */
    EClass getMinValueIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MaxValueIndicator <em>Max Value Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Max Value Indicator</em>'.
     * @see org.talend.dataquality.indicators.MaxValueIndicator
     * @generated
     */
    EClass getMaxValueIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.ModeIndicator <em>Mode Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Mode Indicator</em>'.
     * @see org.talend.dataquality.indicators.ModeIndicator
     * @generated
     */
    EClass getModeIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.ModeIndicator#getMode <em>Mode</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mode</em>'.
     * @see org.talend.dataquality.indicators.ModeIndicator#getMode()
     * @see #getModeIndicator()
     * @generated
     */
    EAttribute getModeIndicator_Mode();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.NullCountIndicator <em>Null Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Null Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.NullCountIndicator
     * @generated
     */
    EClass getNullCountIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.DistinctCountIndicator <em>Distinct Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Distinct Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.DistinctCountIndicator
     * @generated
     */
    EClass getDistinctCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.DistinctCountIndicator#getDistinctValueCount <em>Distinct Value Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Distinct Value Count</em>'.
     * @see org.talend.dataquality.indicators.DistinctCountIndicator#getDistinctValueCount()
     * @see #getDistinctCountIndicator()
     * @generated
     */
    EAttribute getDistinctCountIndicator_DistinctValueCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.UniqueCountIndicator <em>Unique Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unique Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.UniqueCountIndicator
     * @generated
     */
    EClass getUniqueCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.UniqueCountIndicator#getUniqueValueCount <em>Unique Value Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Unique Value Count</em>'.
     * @see org.talend.dataquality.indicators.UniqueCountIndicator#getUniqueValueCount()
     * @see #getUniqueCountIndicator()
     * @generated
     */
    EAttribute getUniqueCountIndicator_UniqueValueCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.DuplicateCountIndicator <em>Duplicate Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Duplicate Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.DuplicateCountIndicator
     * @generated
     */
    EClass getDuplicateCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.DuplicateCountIndicator#getDuplicateValueCount <em>Duplicate Value Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Duplicate Value Count</em>'.
     * @see org.talend.dataquality.indicators.DuplicateCountIndicator#getDuplicateValueCount()
     * @see #getDuplicateCountIndicator()
     * @generated
     */
    EAttribute getDuplicateCountIndicator_DuplicateValueCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.IQRIndicator <em>IQR Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>IQR Indicator</em>'.
     * @see org.talend.dataquality.indicators.IQRIndicator
     * @generated
     */
    EClass getIQRIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.TextIndicator <em>Text Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Text Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator
     * @generated
     */
    EClass getTextIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getAverageLengthIndicator <em>Average Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Average Length Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getAverageLengthIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_AverageLengthIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getMaxLengthIndicator <em>Max Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Max Length Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getMaxLengthIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_MaxLengthIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getMinLengthIndicator <em>Min Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Min Length Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getMinLengthIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_MinLengthIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getMinLengthWithBlankIndicator <em>Min Length With Blank Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Min Length With Blank Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getMinLengthWithBlankIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_MinLengthWithBlankIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getMinLengthWithNullIndicator <em>Min Length With Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Min Length With Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getMinLengthWithNullIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_MinLengthWithNullIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getMinLengthWithBlankNullIndicator <em>Min Length With Blank Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Min Length With Blank Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getMinLengthWithBlankNullIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_MinLengthWithBlankNullIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getMaxLengthWithBlankIndicator <em>Max Length With Blank Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Max Length With Blank Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getMaxLengthWithBlankIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_MaxLengthWithBlankIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getMaxLengthWithNullIndicator <em>Max Length With Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Max Length With Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getMaxLengthWithNullIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_MaxLengthWithNullIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getMaxLengthWithBlankNullIndicator <em>Max Length With Blank Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Max Length With Blank Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getMaxLengthWithBlankNullIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_MaxLengthWithBlankNullIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getAvgLengthWithBlankIndicator <em>Avg Length With Blank Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Avg Length With Blank Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getAvgLengthWithBlankIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_AvgLengthWithBlankIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getAvgLengthWithNullIndicator <em>Avg Length With Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Avg Length With Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getAvgLengthWithNullIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_AvgLengthWithNullIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.TextIndicator#getAvgLengthWithBlankNullIndicator <em>Avg Length With Blank Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Avg Length With Blank Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.TextIndicator#getAvgLengthWithBlankNullIndicator()
     * @see #getTextIndicator()
     * @generated
     */
    EReference getTextIndicator_AvgLengthWithBlankNullIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MinLengthIndicator <em>Min Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Min Length Indicator</em>'.
     * @see org.talend.dataquality.indicators.MinLengthIndicator
     * @generated
     */
    EClass getMinLengthIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MinLengthWithNullIndicator <em>Min Length With Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Min Length With Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.MinLengthWithNullIndicator
     * @generated
     */
    EClass getMinLengthWithNullIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MinLengthWithBlankIndicator <em>Min Length With Blank Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Min Length With Blank Indicator</em>'.
     * @see org.talend.dataquality.indicators.MinLengthWithBlankIndicator
     * @generated
     */
    EClass getMinLengthWithBlankIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MinLengthWithBlankNullIndicator <em>Min Length With Blank Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Min Length With Blank Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.MinLengthWithBlankNullIndicator
     * @generated
     */
    EClass getMinLengthWithBlankNullIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MaxLengthIndicator <em>Max Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Max Length Indicator</em>'.
     * @see org.talend.dataquality.indicators.MaxLengthIndicator
     * @generated
     */
    EClass getMaxLengthIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MaxLengthWithNullIndicator <em>Max Length With Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Max Length With Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.MaxLengthWithNullIndicator
     * @generated
     */
    EClass getMaxLengthWithNullIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MaxLengthWithBlankIndicator <em>Max Length With Blank Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Max Length With Blank Indicator</em>'.
     * @see org.talend.dataquality.indicators.MaxLengthWithBlankIndicator
     * @generated
     */
    EClass getMaxLengthWithBlankIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MaxLengthWithBlankNullIndicator <em>Max Length With Blank Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Max Length With Blank Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.MaxLengthWithBlankNullIndicator
     * @generated
     */
    EClass getMaxLengthWithBlankNullIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.AverageLengthIndicator <em>Average Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Average Length Indicator</em>'.
     * @see org.talend.dataquality.indicators.AverageLengthIndicator
     * @generated
     */
    EClass getAverageLengthIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.AverageLengthIndicator#getSumLength <em>Sum Length</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sum Length</em>'.
     * @see org.talend.dataquality.indicators.AverageLengthIndicator#getSumLength()
     * @see #getAverageLengthIndicator()
     * @generated
     */
    EAttribute getAverageLengthIndicator_SumLength();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.AvgLengthWithNullIndicator <em>Avg Length With Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Avg Length With Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.AvgLengthWithNullIndicator
     * @generated
     */
    EClass getAvgLengthWithNullIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.AvgLengthWithBlankIndicator <em>Avg Length With Blank Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Avg Length With Blank Indicator</em>'.
     * @see org.talend.dataquality.indicators.AvgLengthWithBlankIndicator
     * @generated
     */
    EClass getAvgLengthWithBlankIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.AvgLengthWithBlankNullIndicator <em>Avg Length With Blank Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Avg Length With Blank Null Indicator</em>'.
     * @see org.talend.dataquality.indicators.AvgLengthWithBlankNullIndicator
     * @generated
     */
    EClass getAvgLengthWithBlankNullIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.LengthIndicator <em>Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Length Indicator</em>'.
     * @see org.talend.dataquality.indicators.LengthIndicator
     * @generated
     */
    EClass getLengthIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.LengthIndicator#getLength <em>Length</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Length</em>'.
     * @see org.talend.dataquality.indicators.LengthIndicator#getLength()
     * @see #getLengthIndicator()
     * @generated
     */
    EAttribute getLengthIndicator_Length();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.TextParameters <em>Text Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Text Parameters</em>'.
     * @see org.talend.dataquality.indicators.TextParameters
     * @generated
     */
    EClass getTextParameters();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.TextParameters#isUseBlank <em>Use Blank</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Use Blank</em>'.
     * @see org.talend.dataquality.indicators.TextParameters#isUseBlank()
     * @see #getTextParameters()
     * @generated
     */
    EAttribute getTextParameters_UseBlank();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.TextParameters#getMatchingAlgorithm <em>Matching Algorithm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Matching Algorithm</em>'.
     * @see org.talend.dataquality.indicators.TextParameters#getMatchingAlgorithm()
     * @see #getTextParameters()
     * @generated
     */
    EAttribute getTextParameters_MatchingAlgorithm();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.TextParameters#isIgnoreCase <em>Ignore Case</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Ignore Case</em>'.
     * @see org.talend.dataquality.indicators.TextParameters#isIgnoreCase()
     * @see #getTextParameters()
     * @generated
     */
    EAttribute getTextParameters_IgnoreCase();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.TextParameters#isUseNulls <em>Use Nulls</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Use Nulls</em>'.
     * @see org.talend.dataquality.indicators.TextParameters#isUseNulls()
     * @see #getTextParameters()
     * @generated
     */
    EAttribute getTextParameters_UseNulls();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.TextParameters#getCharactersToReplace <em>Characters To Replace</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Characters To Replace</em>'.
     * @see org.talend.dataquality.indicators.TextParameters#getCharactersToReplace()
     * @see #getTextParameters()
     * @generated
     */
    EAttribute getTextParameters_CharactersToReplace();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.TextParameters#getReplacementCharacters <em>Replacement Characters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Replacement Characters</em>'.
     * @see org.talend.dataquality.indicators.TextParameters#getReplacementCharacters()
     * @see #getTextParameters()
     * @generated
     */
    EAttribute getTextParameters_ReplacementCharacters();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.TextParameters#getCountryCode <em>Country Code</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Country Code</em>'.
     * @see org.talend.dataquality.indicators.TextParameters#getCountryCode()
     * @see #getTextParameters()
     * @generated
     */
    EAttribute getTextParameters_CountryCode();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.LowerQuartileIndicator <em>Lower Quartile Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Lower Quartile Indicator</em>'.
     * @see org.talend.dataquality.indicators.LowerQuartileIndicator
     * @generated
     */
    EClass getLowerQuartileIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.UpperQuartileIndicator <em>Upper Quartile Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Upper Quartile Indicator</em>'.
     * @see org.talend.dataquality.indicators.UpperQuartileIndicator
     * @generated
     */
    EClass getUpperQuartileIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.CountsIndicator <em>Counts Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Counts Indicator</em>'.
     * @see org.talend.dataquality.indicators.CountsIndicator
     * @generated
     */
    EClass getCountsIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.CountsIndicator#getBlankCountIndicator <em>Blank Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Blank Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.CountsIndicator#getBlankCountIndicator()
     * @see #getCountsIndicator()
     * @generated
     */
    EReference getCountsIndicator_BlankCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.CountsIndicator#getRowCountIndicator <em>Row Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Row Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.CountsIndicator#getRowCountIndicator()
     * @see #getCountsIndicator()
     * @generated
     */
    EReference getCountsIndicator_RowCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.CountsIndicator#getNullCountIndicator <em>Null Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Null Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.CountsIndicator#getNullCountIndicator()
     * @see #getCountsIndicator()
     * @generated
     */
    EReference getCountsIndicator_NullCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.CountsIndicator#getUniqueCountIndicator <em>Unique Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Unique Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.CountsIndicator#getUniqueCountIndicator()
     * @see #getCountsIndicator()
     * @generated
     */
    EReference getCountsIndicator_UniqueCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.CountsIndicator#getDistinctCountIndicator <em>Distinct Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Distinct Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.CountsIndicator#getDistinctCountIndicator()
     * @see #getCountsIndicator()
     * @generated
     */
    EReference getCountsIndicator_DistinctCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.CountsIndicator#getDuplicateCountIndicator <em>Duplicate Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Duplicate Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.CountsIndicator#getDuplicateCountIndicator()
     * @see #getCountsIndicator()
     * @generated
     */
    EReference getCountsIndicator_DuplicateCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.CountsIndicator#getDefaultValueIndicator <em>Default Value Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default Value Indicator</em>'.
     * @see org.talend.dataquality.indicators.CountsIndicator#getDefaultValueIndicator()
     * @see #getCountsIndicator()
     * @generated
     */
    EReference getCountsIndicator_DefaultValueIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.DateParameters <em>Date Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Date Parameters</em>'.
     * @see org.talend.dataquality.indicators.DateParameters
     * @generated
     */
    EClass getDateParameters();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.DateParameters#getDateAggregationType <em>Date Aggregation Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Date Aggregation Type</em>'.
     * @see org.talend.dataquality.indicators.DateParameters#getDateAggregationType()
     * @see #getDateParameters()
     * @generated
     */
    EAttribute getDateParameters_DateAggregationType();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.PatternMatchingIndicator <em>Pattern Matching Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Pattern Matching Indicator</em>'.
     * @see org.talend.dataquality.indicators.PatternMatchingIndicator
     * @generated
     */
    EClass getPatternMatchingIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.SqlPatternMatchingIndicator <em>Sql Pattern Matching Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sql Pattern Matching Indicator</em>'.
     * @see org.talend.dataquality.indicators.SqlPatternMatchingIndicator
     * @generated
     */
    EClass getSqlPatternMatchingIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.RegexpMatchingIndicator <em>Regexp Matching Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Regexp Matching Indicator</em>'.
     * @see org.talend.dataquality.indicators.RegexpMatchingIndicator
     * @generated
     */
    EClass getRegexpMatchingIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MatchingIndicator <em>Matching Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Matching Indicator</em>'.
     * @see org.talend.dataquality.indicators.MatchingIndicator
     * @generated
     */
    EClass getMatchingIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.MatchingIndicator#getMatchingValueCount <em>Matching Value Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Matching Value Count</em>'.
     * @see org.talend.dataquality.indicators.MatchingIndicator#getMatchingValueCount()
     * @see #getMatchingIndicator()
     * @generated
     */
    EAttribute getMatchingIndicator_MatchingValueCount();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.MatchingIndicator#getNotMatchingValueCount <em>Not Matching Value Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Not Matching Value Count</em>'.
     * @see org.talend.dataquality.indicators.MatchingIndicator#getNotMatchingValueCount()
     * @see #getMatchingIndicator()
     * @generated
     */
    EAttribute getMatchingIndicator_NotMatchingValueCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.LowFrequencyIndicator <em>Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Low Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.LowFrequencyIndicator
     * @generated
     */
    EClass getLowFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.PatternFreqIndicator <em>Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Pattern Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.PatternFreqIndicator
     * @generated
     */
    EClass getPatternFreqIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.PatternLowFreqIndicator <em>Pattern Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Pattern Low Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.PatternLowFreqIndicator
     * @generated
     */
    EClass getPatternLowFreqIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.DefValueCountIndicator <em>Def Value Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Def Value Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.DefValueCountIndicator
     * @generated
     */
    EClass getDefValueCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.DefValueCountIndicator#getDefaultValCount <em>Default Val Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Default Val Count</em>'.
     * @see org.talend.dataquality.indicators.DefValueCountIndicator#getDefaultValCount()
     * @see #getDefValueCountIndicator()
     * @generated
     */
    EAttribute getDefValueCountIndicator_DefaultValCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.SoundexFreqIndicator <em>Soundex Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Soundex Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.SoundexFreqIndicator
     * @generated
     */
    EClass getSoundexFreqIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.SoundexFreqIndicator#getValueToDistinctFreq <em>Value To Distinct Freq</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value To Distinct Freq</em>'.
     * @see org.talend.dataquality.indicators.SoundexFreqIndicator#getValueToDistinctFreq()
     * @see #getSoundexFreqIndicator()
     * @generated
     */
    EAttribute getSoundexFreqIndicator_ValueToDistinctFreq();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.SoundexLowFreqIndicator <em>Soundex Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Soundex Low Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.SoundexLowFreqIndicator
     * @generated
     */
    EClass getSoundexLowFreqIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.DatePatternFreqIndicator <em>Date Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Date Pattern Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.DatePatternFreqIndicator
     * @generated
     */
    EClass getDatePatternFreqIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.DateFrequencyIndicator <em>Date Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Date Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.DateFrequencyIndicator
     * @generated
     */
    EClass getDateFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.WeekFrequencyIndicator <em>Week Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Week Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.WeekFrequencyIndicator
     * @generated
     */
    EClass getWeekFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MonthFrequencyIndicator <em>Month Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Month Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.MonthFrequencyIndicator
     * @generated
     */
    EClass getMonthFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.QuarterFrequencyIndicator <em>Quarter Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Quarter Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.QuarterFrequencyIndicator
     * @generated
     */
    EClass getQuarterFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.YearFrequencyIndicator <em>Year Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Year Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.YearFrequencyIndicator
     * @generated
     */
    EClass getYearFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.BinFrequencyIndicator <em>Bin Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bin Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.BinFrequencyIndicator
     * @generated
     */
    EClass getBinFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.DateLowFrequencyIndicator <em>Date Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Date Low Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.DateLowFrequencyIndicator
     * @generated
     */
    EClass getDateLowFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.WeekLowFrequencyIndicator <em>Week Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Week Low Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.WeekLowFrequencyIndicator
     * @generated
     */
    EClass getWeekLowFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.MonthLowFrequencyIndicator <em>Month Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Month Low Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.MonthLowFrequencyIndicator
     * @generated
     */
    EClass getMonthLowFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.QuarterLowFrequencyIndicator <em>Quarter Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Quarter Low Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.QuarterLowFrequencyIndicator
     * @generated
     */
    EClass getQuarterLowFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.YearLowFrequencyIndicator <em>Year Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Year Low Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.YearLowFrequencyIndicator
     * @generated
     */
    EClass getYearLowFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.BinLowFrequencyIndicator <em>Bin Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bin Low Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.BinLowFrequencyIndicator
     * @generated
     */
    EClass getBinLowFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.ValidPhoneCountIndicator <em>Valid Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Valid Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.ValidPhoneCountIndicator
     * @generated
     */
    EClass getValidPhoneCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.ValidPhoneCountIndicator#getValidPhoneNumCount <em>Valid Phone Num Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Valid Phone Num Count</em>'.
     * @see org.talend.dataquality.indicators.ValidPhoneCountIndicator#getValidPhoneNumCount()
     * @see #getValidPhoneCountIndicator()
     * @generated
     */
    EAttribute getValidPhoneCountIndicator_ValidPhoneNumCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.PossiblePhoneCountIndicator <em>Possible Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Possible Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.PossiblePhoneCountIndicator
     * @generated
     */
    EClass getPossiblePhoneCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.PossiblePhoneCountIndicator#getPossiblePhoneCount <em>Possible Phone Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Possible Phone Count</em>'.
     * @see org.talend.dataquality.indicators.PossiblePhoneCountIndicator#getPossiblePhoneCount()
     * @see #getPossiblePhoneCountIndicator()
     * @generated
     */
    EAttribute getPossiblePhoneCountIndicator_PossiblePhoneCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.ValidRegCodeCountIndicator <em>Valid Reg Code Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Valid Reg Code Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.ValidRegCodeCountIndicator
     * @generated
     */
    EClass getValidRegCodeCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.ValidRegCodeCountIndicator#getValidRegCount <em>Valid Reg Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Valid Reg Count</em>'.
     * @see org.talend.dataquality.indicators.ValidRegCodeCountIndicator#getValidRegCount()
     * @see #getValidRegCodeCountIndicator()
     * @generated
     */
    EAttribute getValidRegCodeCountIndicator_ValidRegCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.InvalidRegCodeCountIndicator <em>Invalid Reg Code Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Invalid Reg Code Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.InvalidRegCodeCountIndicator
     * @generated
     */
    EClass getInvalidRegCodeCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.InvalidRegCodeCountIndicator#getInvalidRegCount <em>Invalid Reg Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Invalid Reg Count</em>'.
     * @see org.talend.dataquality.indicators.InvalidRegCodeCountIndicator#getInvalidRegCount()
     * @see #getInvalidRegCodeCountIndicator()
     * @generated
     */
    EAttribute getInvalidRegCodeCountIndicator_InvalidRegCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator <em>Well Form National Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Well Form National Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator
     * @generated
     */
    EClass getWellFormNationalPhoneCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator#getWellFormNatiPhoneCount <em>Well Form Nati Phone Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Well Form Nati Phone Count</em>'.
     * @see org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator#getWellFormNatiPhoneCount()
     * @see #getWellFormNationalPhoneCountIndicator()
     * @generated
     */
    EAttribute getWellFormNationalPhoneCountIndicator_WellFormNatiPhoneCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator <em>Well Form Inte Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Well Form Inte Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator
     * @generated
     */
    EClass getWellFormIntePhoneCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator#getWellFormIntePhoneCount <em>Well Form Inte Phone Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Well Form Inte Phone Count</em>'.
     * @see org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator#getWellFormIntePhoneCount()
     * @see #getWellFormIntePhoneCountIndicator()
     * @generated
     */
    EAttribute getWellFormIntePhoneCountIndicator_WellFormIntePhoneCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator <em>Well Form E164 Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Well Form E164 Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator
     * @generated
     */
    EClass getWellFormE164PhoneCountIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator#getWellFormE164PhoneCount <em>Well Form E164 Phone Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Well Form E164 Phone Count</em>'.
     * @see org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator#getWellFormE164PhoneCount()
     * @see #getWellFormE164PhoneCountIndicator()
     * @generated
     */
    EAttribute getWellFormE164PhoneCountIndicator_WellFormE164PhoneCount();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator <em>Phone Numb Statistics Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Phone Numb Statistics Indicator</em>'.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator
     * @generated
     */
    EClass getPhoneNumbStatisticsIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormNationalPhoneCountIndicator <em>Well Form National Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Well Form National Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormNationalPhoneCountIndicator()
     * @see #getPhoneNumbStatisticsIndicator()
     * @generated
     */
    EReference getPhoneNumbStatisticsIndicator_WellFormNationalPhoneCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormIntePhoneCountIndicator <em>Well Form Inte Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Well Form Inte Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormIntePhoneCountIndicator()
     * @see #getPhoneNumbStatisticsIndicator()
     * @generated
     */
    EReference getPhoneNumbStatisticsIndicator_WellFormIntePhoneCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormE164PhoneCountIndicator <em>Well Form E164 Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Well Form E164 Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormE164PhoneCountIndicator()
     * @see #getPhoneNumbStatisticsIndicator()
     * @generated
     */
    EReference getPhoneNumbStatisticsIndicator_WellFormE164PhoneCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getInvalidRegCodeCountIndicator <em>Invalid Reg Code Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Invalid Reg Code Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getInvalidRegCodeCountIndicator()
     * @see #getPhoneNumbStatisticsIndicator()
     * @generated
     */
    EReference getPhoneNumbStatisticsIndicator_InvalidRegCodeCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getPossiblePhoneCountIndicator <em>Possible Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Possible Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getPossiblePhoneCountIndicator()
     * @see #getPhoneNumbStatisticsIndicator()
     * @generated
     */
    EReference getPhoneNumbStatisticsIndicator_PossiblePhoneCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidRegCodeCountIndicator <em>Valid Reg Code Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Valid Reg Code Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidRegCodeCountIndicator()
     * @see #getPhoneNumbStatisticsIndicator()
     * @generated
     */
    EReference getPhoneNumbStatisticsIndicator_ValidRegCodeCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidPhoneCountIndicator <em>Valid Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Valid Phone Count Indicator</em>'.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidPhoneCountIndicator()
     * @see #getPhoneNumbStatisticsIndicator()
     * @generated
     */
    EReference getPhoneNumbStatisticsIndicator_ValidPhoneCountIndicator();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getFormatFreqPieIndicator <em>Format Freq Pie Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Format Freq Pie Indicator</em>'.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getFormatFreqPieIndicator()
     * @see #getPhoneNumbStatisticsIndicator()
     * @generated
     */
    EReference getPhoneNumbStatisticsIndicator_FormatFreqPieIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.FormatFreqPieIndicator <em>Format Freq Pie Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Format Freq Pie Indicator</em>'.
     * @see org.talend.dataquality.indicators.FormatFreqPieIndicator
     * @generated
     */
    EClass getFormatFreqPieIndicator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.FormatFreqPieIndicator#getWellFormE164Count <em>Well Form E164 Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Well Form E164 Count</em>'.
     * @see org.talend.dataquality.indicators.FormatFreqPieIndicator#getWellFormE164Count()
     * @see #getFormatFreqPieIndicator()
     * @generated
     */
    EAttribute getFormatFreqPieIndicator_WellFormE164Count();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.FormatFreqPieIndicator#getWellFormInteCount <em>Well Form Inte Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Well Form Inte Count</em>'.
     * @see org.talend.dataquality.indicators.FormatFreqPieIndicator#getWellFormInteCount()
     * @see #getFormatFreqPieIndicator()
     * @generated
     */
    EAttribute getFormatFreqPieIndicator_WellFormInteCount();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.FormatFreqPieIndicator#getWellFormNatiCount <em>Well Form Nati Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Well Form Nati Count</em>'.
     * @see org.talend.dataquality.indicators.FormatFreqPieIndicator#getWellFormNatiCount()
     * @see #getFormatFreqPieIndicator()
     * @generated
     */
    EAttribute getFormatFreqPieIndicator_WellFormNatiCount();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.FormatFreqPieIndicator#getInvalidFormCount <em>Invalid Form Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Invalid Form Count</em>'.
     * @see org.talend.dataquality.indicators.FormatFreqPieIndicator#getInvalidFormCount()
     * @see #getFormatFreqPieIndicator()
     * @generated
     */
    EAttribute getFormatFreqPieIndicator_InvalidFormCount();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.indicators.FormatFreqPieIndicator#getCurrentKey <em>Current Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Current Key</em>'.
     * @see org.talend.dataquality.indicators.FormatFreqPieIndicator#getCurrentKey()
     * @see #getFormatFreqPieIndicator()
     * @generated
     */
    EAttribute getFormatFreqPieIndicator_CurrentKey();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.BenfordLawFrequencyIndicator <em>Benford Law Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Benford Law Frequency Indicator</em>'.
     * @see org.talend.dataquality.indicators.BenfordLawFrequencyIndicator
     * @generated
     */
    EClass getBenfordLawFrequencyIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.EastAsiaPatternFreqIndicator <em>East Asia Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>East Asia Pattern Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.EastAsiaPatternFreqIndicator
     * @generated
     */
    EClass getEastAsiaPatternFreqIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.EastAsiaPatternLowFreqIndicator <em>East Asia Pattern Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>East Asia Pattern Low Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.EastAsiaPatternLowFreqIndicator
     * @generated
     */
    EClass getEastAsiaPatternLowFreqIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.CSWordPatternFreqIndicator <em>CS Word Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>CS Word Pattern Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.CSWordPatternFreqIndicator
     * @generated
     */
    EClass getCSWordPatternFreqIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.CSWordPatternLowFreqIndicator <em>CS Word Pattern Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>CS Word Pattern Low Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.CSWordPatternLowFreqIndicator
     * @generated
     */
    EClass getCSWordPatternLowFreqIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.CIWordPatternFreqIndicator <em>CI Word Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>CI Word Pattern Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.CIWordPatternFreqIndicator
     * @generated
     */
    EClass getCIWordPatternFreqIndicator();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.indicators.CIWordPatternLowFreqIndicator <em>CI Word Pattern Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>CI Word Pattern Low Freq Indicator</em>'.
     * @see org.talend.dataquality.indicators.CIWordPatternLowFreqIndicator
     * @generated
     */
    EClass getCIWordPatternLowFreqIndicator();

    /**
     * Returns the meta object for enum '{@link org.talend.dataquality.indicators.EnumStatistics <em>Enum Statistics</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Enum Statistics</em>'.
     * @see org.talend.dataquality.indicators.EnumStatistics
     * @generated
     */
    EEnum getEnumStatistics();

    /**
     * Returns the meta object for enum '{@link org.talend.dataquality.indicators.DataminingType <em>Datamining Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Datamining Type</em>'.
     * @see org.talend.dataquality.indicators.DataminingType
     * @generated
     */
    EEnum getDataminingType();

    /**
     * Returns the meta object for enum '{@link org.talend.dataquality.indicators.DateGrain <em>Date Grain</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Date Grain</em>'.
     * @see org.talend.dataquality.indicators.DateGrain
     * @generated
     */
    EEnum getDateGrain();

    /**
     * Returns the meta object for enum '{@link org.talend.dataquality.indicators.MatchingAlgorithm <em>Matching Algorithm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Matching Algorithm</em>'.
     * @see org.talend.dataquality.indicators.MatchingAlgorithm
     * @generated
     */
    EEnum getMatchingAlgorithm();

    /**
     * Returns the meta object for enum '{@link org.talend.dataquality.indicators.IndicatorValueType <em>Indicator Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Indicator Value Type</em>'.
     * @see org.talend.dataquality.indicators.IndicatorValueType
     * @generated
     */
    EEnum getIndicatorValueType();

    /**
     * Returns the meta object for data type '{@link java.util.Set <em>Java Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Java Set</em>'.
     * @see java.util.Set
     * @model instanceClass="java.util.Set<java.lang.Object>"
     * @generated
     */
    EDataType getJavaSet();

    /**
     * Returns the meta object for data type '{@link java.util.HashMap <em>Java Hash Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Java Hash Map</em>'.
     * @see java.util.HashMap
     * @model instanceClass="java.util.HashMap<java.lang.Object, java.lang.Long>"
     * @generated
     */
    EDataType getJavaHashMap();

    /**
     * Returns the meta object for data type '{@link java.util.TreeMap <em>Java Tree Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Java Tree Map</em>'.
     * @see java.util.TreeMap
     * @model instanceClass="java.util.TreeMap<java.lang.Object, java.lang.Long>"
     * @generated
     */
    EDataType getJavaTreeMap();

    /**
     * Returns the meta object for data type '<em>Object Array</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Object Array</em>'.
     * @model instanceClass="java.util.List<java.lang.Object[]>"
     * @generated
     */
    EDataType getObjectArray();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    IndicatorsFactory getIndicatorsFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.IndicatorImpl <em>Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.IndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getIndicator()
         * @generated
         */
        EClass INDICATOR = eINSTANCE.getIndicator();

        /**
         * The meta object literal for the '<em><b>Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDICATOR__COUNT = eINSTANCE.getIndicator_Count();

        /**
         * The meta object literal for the '<em><b>Null Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDICATOR__NULL_COUNT = eINSTANCE.getIndicator_NullCount();

        /**
         * The meta object literal for the '<em><b>Parameters</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR__PARAMETERS = eINSTANCE.getIndicator_Parameters();

        /**
         * The meta object literal for the '<em><b>Analyzed Element</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR__ANALYZED_ELEMENT = eINSTANCE.getIndicator_AnalyzedElement();

        /**
         * The meta object literal for the '<em><b>Datamining Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDICATOR__DATAMINING_TYPE = eINSTANCE.getIndicator_DataminingType();

        /**
         * The meta object literal for the '<em><b>Indicator Definition</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR__INDICATOR_DEFINITION = eINSTANCE.getIndicator_IndicatorDefinition();

        /**
         * The meta object literal for the '<em><b>Instantiated Expressions</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR__INSTANTIATED_EXPRESSIONS = eINSTANCE.getIndicator_InstantiatedExpressions();

        /**
         * The meta object literal for the '<em><b>Computed</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDICATOR__COMPUTED = eINSTANCE.getIndicator_Computed();

        /**
         * The meta object literal for the '<em><b>Join Conditions</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR__JOIN_CONDITIONS = eINSTANCE.getIndicator_JoinConditions();

        /**
         * The meta object literal for the '<em><b>Max Number Rows</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDICATOR__MAX_NUMBER_ROWS = eINSTANCE.getIndicator_MaxNumberRows();

        /**
         * The meta object literal for the '<em><b>Valid Row</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDICATOR__VALID_ROW = eINSTANCE.getIndicator_ValidRow();

        /**
         * The meta object literal for the '<em><b>In Valid Row</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDICATOR__IN_VALID_ROW = eINSTANCE.getIndicator_InValidRow();

        /**
         * The meta object literal for the '<em><b>Store Data</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDICATOR__STORE_DATA = eINSTANCE.getIndicator_StoreData();

        /**
         * The meta object literal for the '<em><b>Built In Indicator Definition</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR__BUILT_IN_INDICATOR_DEFINITION = eINSTANCE.getIndicator_BuiltInIndicatorDefinition();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.RowCountIndicatorImpl <em>Row Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.RowCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getRowCountIndicator()
         * @generated
         */
        EClass ROW_COUNT_INDICATOR = eINSTANCE.getRowCountIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MeanIndicatorImpl <em>Mean Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MeanIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMeanIndicator()
         * @generated
         */
        EClass MEAN_INDICATOR = eINSTANCE.getMeanIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.SumIndicatorImpl <em>Sum Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.SumIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getSumIndicator()
         * @generated
         */
        EClass SUM_INDICATOR = eINSTANCE.getSumIndicator();

        /**
         * The meta object literal for the '<em><b>Sum Str</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SUM_INDICATOR__SUM_STR = eINSTANCE.getSumIndicator_SumStr();

        /**
         * The meta object literal for the '<em><b>Datatype</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SUM_INDICATOR__DATATYPE = eINSTANCE.getSumIndicator_Datatype();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.CompositeIndicatorImpl <em>Composite Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.CompositeIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCompositeIndicator()
         * @generated
         */
        EClass COMPOSITE_INDICATOR = eINSTANCE.getCompositeIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.RangeIndicatorImpl <em>Range Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.RangeIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getRangeIndicator()
         * @generated
         */
        EClass RANGE_INDICATOR = eINSTANCE.getRangeIndicator();

        /**
         * The meta object literal for the '<em><b>Lower Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_INDICATOR__LOWER_VALUE = eINSTANCE.getRangeIndicator_LowerValue();

        /**
         * The meta object literal for the '<em><b>Upper Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_INDICATOR__UPPER_VALUE = eINSTANCE.getRangeIndicator_UpperValue();

        /**
         * The meta object literal for the '<em><b>Datatype</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RANGE_INDICATOR__DATATYPE = eINSTANCE.getRangeIndicator_Datatype();

        /**
         * The meta object literal for the '<em><b>Range</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RANGE_INDICATOR__RANGE = eINSTANCE.getRangeIndicator_Range();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.BoxIndicatorImpl <em>Box Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.BoxIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBoxIndicator()
         * @generated
         */
        EClass BOX_INDICATOR = eINSTANCE.getBoxIndicator();

        /**
         * The meta object literal for the '<em><b>IQR</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BOX_INDICATOR__IQR = eINSTANCE.getBoxIndicator_IQR();

        /**
         * The meta object literal for the '<em><b>Range Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BOX_INDICATOR__RANGE_INDICATOR = eINSTANCE.getBoxIndicator_RangeIndicator();

        /**
         * The meta object literal for the '<em><b>Mean Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BOX_INDICATOR__MEAN_INDICATOR = eINSTANCE.getBoxIndicator_MeanIndicator();

        /**
         * The meta object literal for the '<em><b>Median Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BOX_INDICATOR__MEDIAN_INDICATOR = eINSTANCE.getBoxIndicator_MedianIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl <em>Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getFrequencyIndicator()
         * @generated
         */
        EClass FREQUENCY_INDICATOR = eINSTANCE.getFrequencyIndicator();

        /**
         * The meta object literal for the '<em><b>Unique Values</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FREQUENCY_INDICATOR__UNIQUE_VALUES = eINSTANCE.getFrequencyIndicator_UniqueValues();

        /**
         * The meta object literal for the '<em><b>Distinct Value Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT = eINSTANCE.getFrequencyIndicator_DistinctValueCount();

        /**
         * The meta object literal for the '<em><b>Unique Value Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT = eINSTANCE.getFrequencyIndicator_UniqueValueCount();

        /**
         * The meta object literal for the '<em><b>Duplicate Value Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT = eINSTANCE.getFrequencyIndicator_DuplicateValueCount();

        /**
         * The meta object literal for the '<em><b>Value To Freq</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FREQUENCY_INDICATOR__VALUE_TO_FREQ = eINSTANCE.getFrequencyIndicator_ValueToFreq();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.BlankCountIndicatorImpl <em>Blank Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.BlankCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBlankCountIndicator()
         * @generated
         */
        EClass BLANK_COUNT_INDICATOR = eINSTANCE.getBlankCountIndicator();

        /**
         * The meta object literal for the '<em><b>Blank Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BLANK_COUNT_INDICATOR__BLANK_COUNT = eINSTANCE.getBlankCountIndicator_BlankCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.IndicatorParametersImpl <em>Indicator Parameters</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.IndicatorParametersImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getIndicatorParameters()
         * @generated
         */
        EClass INDICATOR_PARAMETERS = eINSTANCE.getIndicatorParameters();

        /**
         * The meta object literal for the '<em><b>Indicator Valid Domain</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR_PARAMETERS__INDICATOR_VALID_DOMAIN = eINSTANCE.getIndicatorParameters_IndicatorValidDomain();

        /**
         * The meta object literal for the '<em><b>Data Valid Domain</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR_PARAMETERS__DATA_VALID_DOMAIN = eINSTANCE.getIndicatorParameters_DataValidDomain();

        /**
         * The meta object literal for the '<em><b>Bins</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR_PARAMETERS__BINS = eINSTANCE.getIndicatorParameters_Bins();

        /**
         * The meta object literal for the '<em><b>Text Parameter</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR_PARAMETERS__TEXT_PARAMETER = eINSTANCE.getIndicatorParameters_TextParameter();

        /**
         * The meta object literal for the '<em><b>Date Parameters</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDICATOR_PARAMETERS__DATE_PARAMETERS = eINSTANCE.getIndicatorParameters_DateParameters();

        /**
         * The meta object literal for the '<em><b>Top N</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDICATOR_PARAMETERS__TOP_N = eINSTANCE.getIndicatorParameters_TopN();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MedianIndicatorImpl <em>Median Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MedianIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMedianIndicator()
         * @generated
         */
        EClass MEDIAN_INDICATOR = eINSTANCE.getMedianIndicator();

        /**
         * The meta object literal for the '<em><b>Median</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MEDIAN_INDICATOR__MEDIAN = eINSTANCE.getMedianIndicator_Median();

        /**
         * The meta object literal for the '<em><b>Frequence Table</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MEDIAN_INDICATOR__FREQUENCE_TABLE = eINSTANCE.getMedianIndicator_FrequenceTable();

        /**
         * The meta object literal for the '<em><b>Date Median</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MEDIAN_INDICATOR__DATE_MEDIAN = eINSTANCE.getMedianIndicator_DateMedian();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.ValueIndicatorImpl <em>Value Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.ValueIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getValueIndicator()
         * @generated
         */
        EClass VALUE_INDICATOR = eINSTANCE.getValueIndicator();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUE_INDICATOR__VALUE = eINSTANCE.getValueIndicator_Value();

        /**
         * The meta object literal for the '<em><b>Datatype</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUE_INDICATOR__DATATYPE = eINSTANCE.getValueIndicator_Datatype();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MinValueIndicatorImpl <em>Min Value Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MinValueIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinValueIndicator()
         * @generated
         */
        EClass MIN_VALUE_INDICATOR = eINSTANCE.getMinValueIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MaxValueIndicatorImpl <em>Max Value Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MaxValueIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxValueIndicator()
         * @generated
         */
        EClass MAX_VALUE_INDICATOR = eINSTANCE.getMaxValueIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.ModeIndicatorImpl <em>Mode Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.ModeIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getModeIndicator()
         * @generated
         */
        EClass MODE_INDICATOR = eINSTANCE.getModeIndicator();

        /**
         * The meta object literal for the '<em><b>Mode</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MODE_INDICATOR__MODE = eINSTANCE.getModeIndicator_Mode();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.NullCountIndicatorImpl <em>Null Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.NullCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getNullCountIndicator()
         * @generated
         */
        EClass NULL_COUNT_INDICATOR = eINSTANCE.getNullCountIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.DistinctCountIndicatorImpl <em>Distinct Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.DistinctCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDistinctCountIndicator()
         * @generated
         */
        EClass DISTINCT_COUNT_INDICATOR = eINSTANCE.getDistinctCountIndicator();

        /**
         * The meta object literal for the '<em><b>Distinct Value Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DISTINCT_COUNT_INDICATOR__DISTINCT_VALUE_COUNT = eINSTANCE.getDistinctCountIndicator_DistinctValueCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.UniqueCountIndicatorImpl <em>Unique Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.UniqueCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getUniqueCountIndicator()
         * @generated
         */
        EClass UNIQUE_COUNT_INDICATOR = eINSTANCE.getUniqueCountIndicator();

        /**
         * The meta object literal for the '<em><b>Unique Value Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNIQUE_COUNT_INDICATOR__UNIQUE_VALUE_COUNT = eINSTANCE.getUniqueCountIndicator_UniqueValueCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.DuplicateCountIndicatorImpl <em>Duplicate Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.DuplicateCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDuplicateCountIndicator()
         * @generated
         */
        EClass DUPLICATE_COUNT_INDICATOR = eINSTANCE.getDuplicateCountIndicator();

        /**
         * The meta object literal for the '<em><b>Duplicate Value Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DUPLICATE_COUNT_INDICATOR__DUPLICATE_VALUE_COUNT = eINSTANCE.getDuplicateCountIndicator_DuplicateValueCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.IQRIndicatorImpl <em>IQR Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.IQRIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getIQRIndicator()
         * @generated
         */
        EClass IQR_INDICATOR = eINSTANCE.getIQRIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.TextIndicatorImpl <em>Text Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.TextIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getTextIndicator()
         * @generated
         */
        EClass TEXT_INDICATOR = eINSTANCE.getTextIndicator();

        /**
         * The meta object literal for the '<em><b>Average Length Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__AVERAGE_LENGTH_INDICATOR = eINSTANCE.getTextIndicator_AverageLengthIndicator();

        /**
         * The meta object literal for the '<em><b>Max Length Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__MAX_LENGTH_INDICATOR = eINSTANCE.getTextIndicator_MaxLengthIndicator();

        /**
         * The meta object literal for the '<em><b>Min Length Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__MIN_LENGTH_INDICATOR = eINSTANCE.getTextIndicator_MinLengthIndicator();

        /**
         * The meta object literal for the '<em><b>Min Length With Blank Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__MIN_LENGTH_WITH_BLANK_INDICATOR = eINSTANCE.getTextIndicator_MinLengthWithBlankIndicator();

        /**
         * The meta object literal for the '<em><b>Min Length With Null Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__MIN_LENGTH_WITH_NULL_INDICATOR = eINSTANCE.getTextIndicator_MinLengthWithNullIndicator();

        /**
         * The meta object literal for the '<em><b>Min Length With Blank Null Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__MIN_LENGTH_WITH_BLANK_NULL_INDICATOR = eINSTANCE.getTextIndicator_MinLengthWithBlankNullIndicator();

        /**
         * The meta object literal for the '<em><b>Max Length With Blank Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__MAX_LENGTH_WITH_BLANK_INDICATOR = eINSTANCE.getTextIndicator_MaxLengthWithBlankIndicator();

        /**
         * The meta object literal for the '<em><b>Max Length With Null Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__MAX_LENGTH_WITH_NULL_INDICATOR = eINSTANCE.getTextIndicator_MaxLengthWithNullIndicator();

        /**
         * The meta object literal for the '<em><b>Max Length With Blank Null Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__MAX_LENGTH_WITH_BLANK_NULL_INDICATOR = eINSTANCE.getTextIndicator_MaxLengthWithBlankNullIndicator();

        /**
         * The meta object literal for the '<em><b>Avg Length With Blank Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__AVG_LENGTH_WITH_BLANK_INDICATOR = eINSTANCE.getTextIndicator_AvgLengthWithBlankIndicator();

        /**
         * The meta object literal for the '<em><b>Avg Length With Null Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__AVG_LENGTH_WITH_NULL_INDICATOR = eINSTANCE.getTextIndicator_AvgLengthWithNullIndicator();

        /**
         * The meta object literal for the '<em><b>Avg Length With Blank Null Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEXT_INDICATOR__AVG_LENGTH_WITH_BLANK_NULL_INDICATOR = eINSTANCE.getTextIndicator_AvgLengthWithBlankNullIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MinLengthIndicatorImpl <em>Min Length Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MinLengthIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinLengthIndicator()
         * @generated
         */
        EClass MIN_LENGTH_INDICATOR = eINSTANCE.getMinLengthIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MinLengthWithNullIndicatorImpl <em>Min Length With Null Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MinLengthWithNullIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinLengthWithNullIndicator()
         * @generated
         */
        EClass MIN_LENGTH_WITH_NULL_INDICATOR = eINSTANCE.getMinLengthWithNullIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MinLengthWithBlankIndicatorImpl <em>Min Length With Blank Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MinLengthWithBlankIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinLengthWithBlankIndicator()
         * @generated
         */
        EClass MIN_LENGTH_WITH_BLANK_INDICATOR = eINSTANCE.getMinLengthWithBlankIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MinLengthWithBlankNullIndicatorImpl <em>Min Length With Blank Null Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MinLengthWithBlankNullIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMinLengthWithBlankNullIndicator()
         * @generated
         */
        EClass MIN_LENGTH_WITH_BLANK_NULL_INDICATOR = eINSTANCE.getMinLengthWithBlankNullIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MaxLengthIndicatorImpl <em>Max Length Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MaxLengthIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxLengthIndicator()
         * @generated
         */
        EClass MAX_LENGTH_INDICATOR = eINSTANCE.getMaxLengthIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MaxLengthWithNullIndicatorImpl <em>Max Length With Null Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MaxLengthWithNullIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxLengthWithNullIndicator()
         * @generated
         */
        EClass MAX_LENGTH_WITH_NULL_INDICATOR = eINSTANCE.getMaxLengthWithNullIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MaxLengthWithBlankIndicatorImpl <em>Max Length With Blank Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MaxLengthWithBlankIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxLengthWithBlankIndicator()
         * @generated
         */
        EClass MAX_LENGTH_WITH_BLANK_INDICATOR = eINSTANCE.getMaxLengthWithBlankIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MaxLengthWithBlankNullIndicatorImpl <em>Max Length With Blank Null Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MaxLengthWithBlankNullIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMaxLengthWithBlankNullIndicator()
         * @generated
         */
        EClass MAX_LENGTH_WITH_BLANK_NULL_INDICATOR = eINSTANCE.getMaxLengthWithBlankNullIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.AverageLengthIndicatorImpl <em>Average Length Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.AverageLengthIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getAverageLengthIndicator()
         * @generated
         */
        EClass AVERAGE_LENGTH_INDICATOR = eINSTANCE.getAverageLengthIndicator();

        /**
         * The meta object literal for the '<em><b>Sum Length</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AVERAGE_LENGTH_INDICATOR__SUM_LENGTH = eINSTANCE.getAverageLengthIndicator_SumLength();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.AvgLengthWithNullIndicatorImpl <em>Avg Length With Null Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.AvgLengthWithNullIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getAvgLengthWithNullIndicator()
         * @generated
         */
        EClass AVG_LENGTH_WITH_NULL_INDICATOR = eINSTANCE.getAvgLengthWithNullIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.AvgLengthWithBlankIndicatorImpl <em>Avg Length With Blank Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.AvgLengthWithBlankIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getAvgLengthWithBlankIndicator()
         * @generated
         */
        EClass AVG_LENGTH_WITH_BLANK_INDICATOR = eINSTANCE.getAvgLengthWithBlankIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.AvgLengthWithBlankNullIndicatorImpl <em>Avg Length With Blank Null Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.AvgLengthWithBlankNullIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getAvgLengthWithBlankNullIndicator()
         * @generated
         */
        EClass AVG_LENGTH_WITH_BLANK_NULL_INDICATOR = eINSTANCE.getAvgLengthWithBlankNullIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.LengthIndicatorImpl <em>Length Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.LengthIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getLengthIndicator()
         * @generated
         */
        EClass LENGTH_INDICATOR = eINSTANCE.getLengthIndicator();

        /**
         * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LENGTH_INDICATOR__LENGTH = eINSTANCE.getLengthIndicator_Length();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.TextParametersImpl <em>Text Parameters</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.TextParametersImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getTextParameters()
         * @generated
         */
        EClass TEXT_PARAMETERS = eINSTANCE.getTextParameters();

        /**
         * The meta object literal for the '<em><b>Use Blank</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_PARAMETERS__USE_BLANK = eINSTANCE.getTextParameters_UseBlank();

        /**
         * The meta object literal for the '<em><b>Matching Algorithm</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_PARAMETERS__MATCHING_ALGORITHM = eINSTANCE.getTextParameters_MatchingAlgorithm();

        /**
         * The meta object literal for the '<em><b>Ignore Case</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_PARAMETERS__IGNORE_CASE = eINSTANCE.getTextParameters_IgnoreCase();

        /**
         * The meta object literal for the '<em><b>Use Nulls</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_PARAMETERS__USE_NULLS = eINSTANCE.getTextParameters_UseNulls();

        /**
         * The meta object literal for the '<em><b>Characters To Replace</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_PARAMETERS__CHARACTERS_TO_REPLACE = eINSTANCE.getTextParameters_CharactersToReplace();

        /**
         * The meta object literal for the '<em><b>Replacement Characters</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_PARAMETERS__REPLACEMENT_CHARACTERS = eINSTANCE.getTextParameters_ReplacementCharacters();

        /**
         * The meta object literal for the '<em><b>Country Code</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_PARAMETERS__COUNTRY_CODE = eINSTANCE.getTextParameters_CountryCode();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.LowerQuartileIndicatorImpl <em>Lower Quartile Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.LowerQuartileIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getLowerQuartileIndicator()
         * @generated
         */
        EClass LOWER_QUARTILE_INDICATOR = eINSTANCE.getLowerQuartileIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.UpperQuartileIndicatorImpl <em>Upper Quartile Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.UpperQuartileIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getUpperQuartileIndicator()
         * @generated
         */
        EClass UPPER_QUARTILE_INDICATOR = eINSTANCE.getUpperQuartileIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.CountsIndicatorImpl <em>Counts Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.CountsIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCountsIndicator()
         * @generated
         */
        EClass COUNTS_INDICATOR = eINSTANCE.getCountsIndicator();

        /**
         * The meta object literal for the '<em><b>Blank Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COUNTS_INDICATOR__BLANK_COUNT_INDICATOR = eINSTANCE.getCountsIndicator_BlankCountIndicator();

        /**
         * The meta object literal for the '<em><b>Row Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COUNTS_INDICATOR__ROW_COUNT_INDICATOR = eINSTANCE.getCountsIndicator_RowCountIndicator();

        /**
         * The meta object literal for the '<em><b>Null Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COUNTS_INDICATOR__NULL_COUNT_INDICATOR = eINSTANCE.getCountsIndicator_NullCountIndicator();

        /**
         * The meta object literal for the '<em><b>Unique Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COUNTS_INDICATOR__UNIQUE_COUNT_INDICATOR = eINSTANCE.getCountsIndicator_UniqueCountIndicator();

        /**
         * The meta object literal for the '<em><b>Distinct Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COUNTS_INDICATOR__DISTINCT_COUNT_INDICATOR = eINSTANCE.getCountsIndicator_DistinctCountIndicator();

        /**
         * The meta object literal for the '<em><b>Duplicate Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COUNTS_INDICATOR__DUPLICATE_COUNT_INDICATOR = eINSTANCE.getCountsIndicator_DuplicateCountIndicator();

        /**
         * The meta object literal for the '<em><b>Default Value Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COUNTS_INDICATOR__DEFAULT_VALUE_INDICATOR = eINSTANCE.getCountsIndicator_DefaultValueIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.DateParametersImpl <em>Date Parameters</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.DateParametersImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDateParameters()
         * @generated
         */
        EClass DATE_PARAMETERS = eINSTANCE.getDateParameters();

        /**
         * The meta object literal for the '<em><b>Date Aggregation Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DATE_PARAMETERS__DATE_AGGREGATION_TYPE = eINSTANCE.getDateParameters_DateAggregationType();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.PatternMatchingIndicatorImpl <em>Pattern Matching Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.PatternMatchingIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPatternMatchingIndicator()
         * @generated
         */
        EClass PATTERN_MATCHING_INDICATOR = eINSTANCE.getPatternMatchingIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.SqlPatternMatchingIndicatorImpl <em>Sql Pattern Matching Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.SqlPatternMatchingIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getSqlPatternMatchingIndicator()
         * @generated
         */
        EClass SQL_PATTERN_MATCHING_INDICATOR = eINSTANCE.getSqlPatternMatchingIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.RegexpMatchingIndicatorImpl <em>Regexp Matching Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.RegexpMatchingIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getRegexpMatchingIndicator()
         * @generated
         */
        EClass REGEXP_MATCHING_INDICATOR = eINSTANCE.getRegexpMatchingIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MatchingIndicatorImpl <em>Matching Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MatchingIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMatchingIndicator()
         * @generated
         */
        EClass MATCHING_INDICATOR = eINSTANCE.getMatchingIndicator();

        /**
         * The meta object literal for the '<em><b>Matching Value Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCHING_INDICATOR__MATCHING_VALUE_COUNT = eINSTANCE.getMatchingIndicator_MatchingValueCount();

        /**
         * The meta object literal for the '<em><b>Not Matching Value Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCHING_INDICATOR__NOT_MATCHING_VALUE_COUNT = eINSTANCE.getMatchingIndicator_NotMatchingValueCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.LowFrequencyIndicatorImpl <em>Low Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.LowFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getLowFrequencyIndicator()
         * @generated
         */
        EClass LOW_FREQUENCY_INDICATOR = eINSTANCE.getLowFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.PatternFreqIndicatorImpl <em>Pattern Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.PatternFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPatternFreqIndicator()
         * @generated
         */
        EClass PATTERN_FREQ_INDICATOR = eINSTANCE.getPatternFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.PatternLowFreqIndicatorImpl <em>Pattern Low Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.PatternLowFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPatternLowFreqIndicator()
         * @generated
         */
        EClass PATTERN_LOW_FREQ_INDICATOR = eINSTANCE.getPatternLowFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.DefValueCountIndicatorImpl <em>Def Value Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.DefValueCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDefValueCountIndicator()
         * @generated
         */
        EClass DEF_VALUE_COUNT_INDICATOR = eINSTANCE.getDefValueCountIndicator();

        /**
         * The meta object literal for the '<em><b>Default Val Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DEF_VALUE_COUNT_INDICATOR__DEFAULT_VAL_COUNT = eINSTANCE.getDefValueCountIndicator_DefaultValCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.SoundexFreqIndicatorImpl <em>Soundex Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.SoundexFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getSoundexFreqIndicator()
         * @generated
         */
        EClass SOUNDEX_FREQ_INDICATOR = eINSTANCE.getSoundexFreqIndicator();

        /**
         * The meta object literal for the '<em><b>Value To Distinct Freq</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SOUNDEX_FREQ_INDICATOR__VALUE_TO_DISTINCT_FREQ = eINSTANCE.getSoundexFreqIndicator_ValueToDistinctFreq();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.SoundexLowFreqIndicatorImpl <em>Soundex Low Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.SoundexLowFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getSoundexLowFreqIndicator()
         * @generated
         */
        EClass SOUNDEX_LOW_FREQ_INDICATOR = eINSTANCE.getSoundexLowFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.DatePatternFreqIndicatorImpl <em>Date Pattern Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.DatePatternFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDatePatternFreqIndicator()
         * @generated
         */
        EClass DATE_PATTERN_FREQ_INDICATOR = eINSTANCE.getDatePatternFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.DateFrequencyIndicatorImpl <em>Date Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.DateFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDateFrequencyIndicator()
         * @generated
         */
        EClass DATE_FREQUENCY_INDICATOR = eINSTANCE.getDateFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.WeekFrequencyIndicatorImpl <em>Week Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.WeekFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWeekFrequencyIndicator()
         * @generated
         */
        EClass WEEK_FREQUENCY_INDICATOR = eINSTANCE.getWeekFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MonthFrequencyIndicatorImpl <em>Month Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MonthFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMonthFrequencyIndicator()
         * @generated
         */
        EClass MONTH_FREQUENCY_INDICATOR = eINSTANCE.getMonthFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.QuarterFrequencyIndicatorImpl <em>Quarter Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.QuarterFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getQuarterFrequencyIndicator()
         * @generated
         */
        EClass QUARTER_FREQUENCY_INDICATOR = eINSTANCE.getQuarterFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.YearFrequencyIndicatorImpl <em>Year Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.YearFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getYearFrequencyIndicator()
         * @generated
         */
        EClass YEAR_FREQUENCY_INDICATOR = eINSTANCE.getYearFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.BinFrequencyIndicatorImpl <em>Bin Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.BinFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBinFrequencyIndicator()
         * @generated
         */
        EClass BIN_FREQUENCY_INDICATOR = eINSTANCE.getBinFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.DateLowFrequencyIndicatorImpl <em>Date Low Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.DateLowFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDateLowFrequencyIndicator()
         * @generated
         */
        EClass DATE_LOW_FREQUENCY_INDICATOR = eINSTANCE.getDateLowFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.WeekLowFrequencyIndicatorImpl <em>Week Low Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.WeekLowFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWeekLowFrequencyIndicator()
         * @generated
         */
        EClass WEEK_LOW_FREQUENCY_INDICATOR = eINSTANCE.getWeekLowFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.MonthLowFrequencyIndicatorImpl <em>Month Low Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.MonthLowFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMonthLowFrequencyIndicator()
         * @generated
         */
        EClass MONTH_LOW_FREQUENCY_INDICATOR = eINSTANCE.getMonthLowFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.QuarterLowFrequencyIndicatorImpl <em>Quarter Low Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.QuarterLowFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getQuarterLowFrequencyIndicator()
         * @generated
         */
        EClass QUARTER_LOW_FREQUENCY_INDICATOR = eINSTANCE.getQuarterLowFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.YearLowFrequencyIndicatorImpl <em>Year Low Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.YearLowFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getYearLowFrequencyIndicator()
         * @generated
         */
        EClass YEAR_LOW_FREQUENCY_INDICATOR = eINSTANCE.getYearLowFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.BinLowFrequencyIndicatorImpl <em>Bin Low Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.BinLowFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBinLowFrequencyIndicator()
         * @generated
         */
        EClass BIN_LOW_FREQUENCY_INDICATOR = eINSTANCE.getBinLowFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.ValidPhoneCountIndicatorImpl <em>Valid Phone Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.ValidPhoneCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getValidPhoneCountIndicator()
         * @generated
         */
        EClass VALID_PHONE_COUNT_INDICATOR = eINSTANCE.getValidPhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Valid Phone Num Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALID_PHONE_COUNT_INDICATOR__VALID_PHONE_NUM_COUNT = eINSTANCE.getValidPhoneCountIndicator_ValidPhoneNumCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.PossiblePhoneCountIndicatorImpl <em>Possible Phone Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.PossiblePhoneCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPossiblePhoneCountIndicator()
         * @generated
         */
        EClass POSSIBLE_PHONE_COUNT_INDICATOR = eINSTANCE.getPossiblePhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Possible Phone Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute POSSIBLE_PHONE_COUNT_INDICATOR__POSSIBLE_PHONE_COUNT = eINSTANCE.getPossiblePhoneCountIndicator_PossiblePhoneCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.ValidRegCodeCountIndicatorImpl <em>Valid Reg Code Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.ValidRegCodeCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getValidRegCodeCountIndicator()
         * @generated
         */
        EClass VALID_REG_CODE_COUNT_INDICATOR = eINSTANCE.getValidRegCodeCountIndicator();

        /**
         * The meta object literal for the '<em><b>Valid Reg Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALID_REG_CODE_COUNT_INDICATOR__VALID_REG_COUNT = eINSTANCE.getValidRegCodeCountIndicator_ValidRegCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.InvalidRegCodeCountIndicatorImpl <em>Invalid Reg Code Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.InvalidRegCodeCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getInvalidRegCodeCountIndicator()
         * @generated
         */
        EClass INVALID_REG_CODE_COUNT_INDICATOR = eINSTANCE.getInvalidRegCodeCountIndicator();

        /**
         * The meta object literal for the '<em><b>Invalid Reg Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INVALID_REG_CODE_COUNT_INDICATOR__INVALID_REG_COUNT = eINSTANCE.getInvalidRegCodeCountIndicator_InvalidRegCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.WellFormNationalPhoneCountIndicatorImpl <em>Well Form National Phone Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.WellFormNationalPhoneCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWellFormNationalPhoneCountIndicator()
         * @generated
         */
        EClass WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR = eINSTANCE.getWellFormNationalPhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Well Form Nati Phone Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR__WELL_FORM_NATI_PHONE_COUNT = eINSTANCE.getWellFormNationalPhoneCountIndicator_WellFormNatiPhoneCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.WellFormIntePhoneCountIndicatorImpl <em>Well Form Inte Phone Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.WellFormIntePhoneCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWellFormIntePhoneCountIndicator()
         * @generated
         */
        EClass WELL_FORM_INTE_PHONE_COUNT_INDICATOR = eINSTANCE.getWellFormIntePhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Well Form Inte Phone Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute WELL_FORM_INTE_PHONE_COUNT_INDICATOR__WELL_FORM_INTE_PHONE_COUNT = eINSTANCE.getWellFormIntePhoneCountIndicator_WellFormIntePhoneCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl <em>Well Form E164 Phone Count Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getWellFormE164PhoneCountIndicator()
         * @generated
         */
        EClass WELL_FORM_E164_PHONE_COUNT_INDICATOR = eINSTANCE.getWellFormE164PhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Well Form E164 Phone Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute WELL_FORM_E164_PHONE_COUNT_INDICATOR__WELL_FORM_E164_PHONE_COUNT = eINSTANCE.getWellFormE164PhoneCountIndicator_WellFormE164PhoneCount();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl <em>Phone Numb Statistics Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getPhoneNumbStatisticsIndicator()
         * @generated
         */
        EClass PHONE_NUMB_STATISTICS_INDICATOR = eINSTANCE.getPhoneNumbStatisticsIndicator();

        /**
         * The meta object literal for the '<em><b>Well Form National Phone Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR = eINSTANCE.getPhoneNumbStatisticsIndicator_WellFormNationalPhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Well Form Inte Phone Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR = eINSTANCE.getPhoneNumbStatisticsIndicator_WellFormIntePhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Well Form E164 Phone Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR = eINSTANCE.getPhoneNumbStatisticsIndicator_WellFormE164PhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Invalid Reg Code Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR = eINSTANCE.getPhoneNumbStatisticsIndicator_InvalidRegCodeCountIndicator();

        /**
         * The meta object literal for the '<em><b>Possible Phone Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR = eINSTANCE.getPhoneNumbStatisticsIndicator_PossiblePhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Valid Reg Code Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR = eINSTANCE.getPhoneNumbStatisticsIndicator_ValidRegCodeCountIndicator();

        /**
         * The meta object literal for the '<em><b>Valid Phone Count Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR = eINSTANCE.getPhoneNumbStatisticsIndicator_ValidPhoneCountIndicator();

        /**
         * The meta object literal for the '<em><b>Format Freq Pie Indicator</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR = eINSTANCE.getPhoneNumbStatisticsIndicator_FormatFreqPieIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.FormatFreqPieIndicatorImpl <em>Format Freq Pie Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.FormatFreqPieIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getFormatFreqPieIndicator()
         * @generated
         */
        EClass FORMAT_FREQ_PIE_INDICATOR = eINSTANCE.getFormatFreqPieIndicator();

        /**
         * The meta object literal for the '<em><b>Well Form E164 Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FORMAT_FREQ_PIE_INDICATOR__WELL_FORM_E164_COUNT = eINSTANCE.getFormatFreqPieIndicator_WellFormE164Count();

        /**
         * The meta object literal for the '<em><b>Well Form Inte Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FORMAT_FREQ_PIE_INDICATOR__WELL_FORM_INTE_COUNT = eINSTANCE.getFormatFreqPieIndicator_WellFormInteCount();

        /**
         * The meta object literal for the '<em><b>Well Form Nati Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FORMAT_FREQ_PIE_INDICATOR__WELL_FORM_NATI_COUNT = eINSTANCE.getFormatFreqPieIndicator_WellFormNatiCount();

        /**
         * The meta object literal for the '<em><b>Invalid Form Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FORMAT_FREQ_PIE_INDICATOR__INVALID_FORM_COUNT = eINSTANCE.getFormatFreqPieIndicator_InvalidFormCount();

        /**
         * The meta object literal for the '<em><b>Current Key</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FORMAT_FREQ_PIE_INDICATOR__CURRENT_KEY = eINSTANCE.getFormatFreqPieIndicator_CurrentKey();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.BenfordLawFrequencyIndicatorImpl <em>Benford Law Frequency Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.BenfordLawFrequencyIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getBenfordLawFrequencyIndicator()
         * @generated
         */
        EClass BENFORD_LAW_FREQUENCY_INDICATOR = eINSTANCE.getBenfordLawFrequencyIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.EastAsiaPatternFreqIndicatorImpl <em>East Asia Pattern Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.EastAsiaPatternFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getEastAsiaPatternFreqIndicator()
         * @generated
         */
        EClass EAST_ASIA_PATTERN_FREQ_INDICATOR = eINSTANCE.getEastAsiaPatternFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.EastAsiaPatternLowFreqIndicatorImpl <em>East Asia Pattern Low Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.EastAsiaPatternLowFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getEastAsiaPatternLowFreqIndicator()
         * @generated
         */
        EClass EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR = eINSTANCE.getEastAsiaPatternLowFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.CSWordPatternFreqIndicatorImpl <em>CS Word Pattern Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.CSWordPatternFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCSWordPatternFreqIndicator()
         * @generated
         */
        EClass CS_WORD_PATTERN_FREQ_INDICATOR = eINSTANCE.getCSWordPatternFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.CSWordPatternLowFreqIndicatorImpl <em>CS Word Pattern Low Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.CSWordPatternLowFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCSWordPatternLowFreqIndicator()
         * @generated
         */
        EClass CS_WORD_PATTERN_LOW_FREQ_INDICATOR = eINSTANCE.getCSWordPatternLowFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.CIWordPatternFreqIndicatorImpl <em>CI Word Pattern Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.CIWordPatternFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCIWordPatternFreqIndicator()
         * @generated
         */
        EClass CI_WORD_PATTERN_FREQ_INDICATOR = eINSTANCE.getCIWordPatternFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.impl.CIWordPatternLowFreqIndicatorImpl <em>CI Word Pattern Low Freq Indicator</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.CIWordPatternLowFreqIndicatorImpl
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getCIWordPatternLowFreqIndicator()
         * @generated
         */
        EClass CI_WORD_PATTERN_LOW_FREQ_INDICATOR = eINSTANCE.getCIWordPatternLowFreqIndicator();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.EnumStatistics <em>Enum Statistics</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.EnumStatistics
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getEnumStatistics()
         * @generated
         */
        EEnum ENUM_STATISTICS = eINSTANCE.getEnumStatistics();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.DataminingType <em>Datamining Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.DataminingType
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDataminingType()
         * @generated
         */
        EEnum DATAMINING_TYPE = eINSTANCE.getDataminingType();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.DateGrain <em>Date Grain</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.DateGrain
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getDateGrain()
         * @generated
         */
        EEnum DATE_GRAIN = eINSTANCE.getDateGrain();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.MatchingAlgorithm <em>Matching Algorithm</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.MatchingAlgorithm
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getMatchingAlgorithm()
         * @generated
         */
        EEnum MATCHING_ALGORITHM = eINSTANCE.getMatchingAlgorithm();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.indicators.IndicatorValueType <em>Indicator Value Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.IndicatorValueType
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getIndicatorValueType()
         * @generated
         */
        EEnum INDICATOR_VALUE_TYPE = eINSTANCE.getIndicatorValueType();

        /**
         * The meta object literal for the '<em>Java Set</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.Set
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getJavaSet()
         * @generated
         */
        EDataType JAVA_SET = eINSTANCE.getJavaSet();

        /**
         * The meta object literal for the '<em>Java Hash Map</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.HashMap
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getJavaHashMap()
         * @generated
         */
        EDataType JAVA_HASH_MAP = eINSTANCE.getJavaHashMap();

        /**
         * The meta object literal for the '<em>Java Tree Map</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.TreeMap
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getJavaTreeMap()
         * @generated
         */
        EDataType JAVA_TREE_MAP = eINSTANCE.getJavaTreeMap();

        /**
         * The meta object literal for the '<em>Object Array</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.indicators.impl.IndicatorsPackageImpl#getObjectArray()
         * @generated
         */
        EDataType OBJECT_ARRAY = eINSTANCE.getObjectArray();

    }

} //IndicatorsPackage
