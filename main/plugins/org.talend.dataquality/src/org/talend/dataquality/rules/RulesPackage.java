/**
 * // ============================================================================
 * //
 * // Copyright (C) 2006-2017 Talend Inc. - www.talend.com
 * //
 * // This source code is available under agreement available at
 * // %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 * //
 * // You should have received a copy of the agreement
 * // along with this program; if not, write to Talend SA
 * // 9 rue Pages 92150 Suresnes, France
 * //
 * // ============================================================================
 * 
 *
 * $Id$
 */
package org.talend.dataquality.rules;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.talend.dataquality.indicators.definition.DefinitionPackage;
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
 * @see org.talend.dataquality.rules.RulesFactory
 * @model kind="package"
 * @generated
 */
public interface RulesPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "rules";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://dataquality.rules";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "dataquality.rules";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    RulesPackage eINSTANCE = org.talend.dataquality.rules.impl.RulesPackageImpl.init();

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.DQRuleImpl <em>DQ Rule</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.DQRuleImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getDQRule()
     * @generated
     */
    int DQ_RULE = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__NAME = DefinitionPackage.INDICATOR_DEFINITION__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__VISIBILITY = DefinitionPackage.INDICATOR_DEFINITION__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__CLIENT_DEPENDENCY = DefinitionPackage.INDICATOR_DEFINITION__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__SUPPLIER_DEPENDENCY = DefinitionPackage.INDICATOR_DEFINITION__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__CONSTRAINT = DefinitionPackage.INDICATOR_DEFINITION__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__NAMESPACE = DefinitionPackage.INDICATOR_DEFINITION__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__IMPORTER = DefinitionPackage.INDICATOR_DEFINITION__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__STEREOTYPE = DefinitionPackage.INDICATOR_DEFINITION__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__TAGGED_VALUE = DefinitionPackage.INDICATOR_DEFINITION__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__DOCUMENT = DefinitionPackage.INDICATOR_DEFINITION__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__DESCRIPTION = DefinitionPackage.INDICATOR_DEFINITION__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__RESPONSIBLE_PARTY = DefinitionPackage.INDICATOR_DEFINITION__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__ELEMENT_NODE = DefinitionPackage.INDICATOR_DEFINITION__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__SET = DefinitionPackage.INDICATOR_DEFINITION__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__RENDERED_OBJECT = DefinitionPackage.INDICATOR_DEFINITION__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__VOCABULARY_ELEMENT = DefinitionPackage.INDICATOR_DEFINITION__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__MEASUREMENT = DefinitionPackage.INDICATOR_DEFINITION__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__CHANGE_REQUEST = DefinitionPackage.INDICATOR_DEFINITION__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__DASDL_PROPERTY = DefinitionPackage.INDICATOR_DEFINITION__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__CATEGORIES = DefinitionPackage.INDICATOR_DEFINITION__CATEGORIES;

    /**
     * The feature id for the '<em><b>Aggregated Definitions</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__AGGREGATED_DEFINITIONS = DefinitionPackage.INDICATOR_DEFINITION__AGGREGATED_DEFINITIONS;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__LABEL = DefinitionPackage.INDICATOR_DEFINITION__LABEL;

    /**
     * The feature id for the '<em><b>Sub Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__SUB_CATEGORIES = DefinitionPackage.INDICATOR_DEFINITION__SUB_CATEGORIES;

    /**
     * The feature id for the '<em><b>Sql Generic Expression</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__SQL_GENERIC_EXPRESSION = DefinitionPackage.INDICATOR_DEFINITION__SQL_GENERIC_EXPRESSION;

    /**
     * The feature id for the '<em><b>Aggregate1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__AGGREGATE1ARG_FUNCTIONS = DefinitionPackage.INDICATOR_DEFINITION__AGGREGATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Date1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__DATE1ARG_FUNCTIONS = DefinitionPackage.INDICATOR_DEFINITION__DATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Characters Mapping</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__CHARACTERS_MAPPING = DefinitionPackage.INDICATOR_DEFINITION__CHARACTERS_MAPPING;

    /**
     * The feature id for the '<em><b>Indicator Definition Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DQ_RULE__INDICATOR_DEFINITION_PARAMETER = DefinitionPackage.INDICATOR_DEFINITION__INDICATOR_DEFINITION_PARAMETER;

				/**
     * The feature id for the '<em><b>Criticality Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__CRITICALITY_LEVEL = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Elements</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE__ELEMENTS = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>DQ Rule</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DQ_RULE_FEATURE_COUNT = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.SpecifiedDQRuleImpl <em>Specified DQ Rule</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.SpecifiedDQRuleImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getSpecifiedDQRule()
     * @generated
     */
    int SPECIFIED_DQ_RULE = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__NAME = DQ_RULE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__VISIBILITY = DQ_RULE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__CLIENT_DEPENDENCY = DQ_RULE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__SUPPLIER_DEPENDENCY = DQ_RULE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__CONSTRAINT = DQ_RULE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__NAMESPACE = DQ_RULE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__IMPORTER = DQ_RULE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__STEREOTYPE = DQ_RULE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__TAGGED_VALUE = DQ_RULE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__DOCUMENT = DQ_RULE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__DESCRIPTION = DQ_RULE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__RESPONSIBLE_PARTY = DQ_RULE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__ELEMENT_NODE = DQ_RULE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__SET = DQ_RULE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__RENDERED_OBJECT = DQ_RULE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__VOCABULARY_ELEMENT = DQ_RULE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__MEASUREMENT = DQ_RULE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__CHANGE_REQUEST = DQ_RULE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__DASDL_PROPERTY = DQ_RULE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__CATEGORIES = DQ_RULE__CATEGORIES;

    /**
     * The feature id for the '<em><b>Aggregated Definitions</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__AGGREGATED_DEFINITIONS = DQ_RULE__AGGREGATED_DEFINITIONS;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__LABEL = DQ_RULE__LABEL;

    /**
     * The feature id for the '<em><b>Sub Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__SUB_CATEGORIES = DQ_RULE__SUB_CATEGORIES;

    /**
     * The feature id for the '<em><b>Sql Generic Expression</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__SQL_GENERIC_EXPRESSION = DQ_RULE__SQL_GENERIC_EXPRESSION;

    /**
     * The feature id for the '<em><b>Aggregate1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__AGGREGATE1ARG_FUNCTIONS = DQ_RULE__AGGREGATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Date1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__DATE1ARG_FUNCTIONS = DQ_RULE__DATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Characters Mapping</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__CHARACTERS_MAPPING = DQ_RULE__CHARACTERS_MAPPING;

    /**
     * The feature id for the '<em><b>Indicator Definition Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int SPECIFIED_DQ_RULE__INDICATOR_DEFINITION_PARAMETER = DQ_RULE__INDICATOR_DEFINITION_PARAMETER;

				/**
     * The feature id for the '<em><b>Criticality Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__CRITICALITY_LEVEL = DQ_RULE__CRITICALITY_LEVEL;

    /**
     * The feature id for the '<em><b>Elements</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE__ELEMENTS = DQ_RULE__ELEMENTS;

    /**
     * The number of structural features of the '<em>Specified DQ Rule</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFIED_DQ_RULE_FEATURE_COUNT = DQ_RULE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.InferredDQRuleImpl <em>Inferred DQ Rule</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.InferredDQRuleImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getInferredDQRule()
     * @generated
     */
    int INFERRED_DQ_RULE = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__NAME = DQ_RULE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__VISIBILITY = DQ_RULE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__CLIENT_DEPENDENCY = DQ_RULE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__SUPPLIER_DEPENDENCY = DQ_RULE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__CONSTRAINT = DQ_RULE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__NAMESPACE = DQ_RULE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__IMPORTER = DQ_RULE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__STEREOTYPE = DQ_RULE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__TAGGED_VALUE = DQ_RULE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__DOCUMENT = DQ_RULE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__DESCRIPTION = DQ_RULE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__RESPONSIBLE_PARTY = DQ_RULE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__ELEMENT_NODE = DQ_RULE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__SET = DQ_RULE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__RENDERED_OBJECT = DQ_RULE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__VOCABULARY_ELEMENT = DQ_RULE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__MEASUREMENT = DQ_RULE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__CHANGE_REQUEST = DQ_RULE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__DASDL_PROPERTY = DQ_RULE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__CATEGORIES = DQ_RULE__CATEGORIES;

    /**
     * The feature id for the '<em><b>Aggregated Definitions</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__AGGREGATED_DEFINITIONS = DQ_RULE__AGGREGATED_DEFINITIONS;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__LABEL = DQ_RULE__LABEL;

    /**
     * The feature id for the '<em><b>Sub Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__SUB_CATEGORIES = DQ_RULE__SUB_CATEGORIES;

    /**
     * The feature id for the '<em><b>Sql Generic Expression</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__SQL_GENERIC_EXPRESSION = DQ_RULE__SQL_GENERIC_EXPRESSION;

    /**
     * The feature id for the '<em><b>Aggregate1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__AGGREGATE1ARG_FUNCTIONS = DQ_RULE__AGGREGATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Date1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__DATE1ARG_FUNCTIONS = DQ_RULE__DATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Characters Mapping</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__CHARACTERS_MAPPING = DQ_RULE__CHARACTERS_MAPPING;

    /**
     * The feature id for the '<em><b>Indicator Definition Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int INFERRED_DQ_RULE__INDICATOR_DEFINITION_PARAMETER = DQ_RULE__INDICATOR_DEFINITION_PARAMETER;

				/**
     * The feature id for the '<em><b>Criticality Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__CRITICALITY_LEVEL = DQ_RULE__CRITICALITY_LEVEL;

    /**
     * The feature id for the '<em><b>Elements</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE__ELEMENTS = DQ_RULE__ELEMENTS;

    /**
     * The number of structural features of the '<em>Inferred DQ Rule</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFERRED_DQ_RULE_FEATURE_COUNT = DQ_RULE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.WhereRuleImpl <em>Where Rule</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.WhereRuleImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getWhereRule()
     * @generated
     */
    int WHERE_RULE = 3;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__NAME = SPECIFIED_DQ_RULE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__VISIBILITY = SPECIFIED_DQ_RULE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__CLIENT_DEPENDENCY = SPECIFIED_DQ_RULE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__SUPPLIER_DEPENDENCY = SPECIFIED_DQ_RULE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__CONSTRAINT = SPECIFIED_DQ_RULE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__NAMESPACE = SPECIFIED_DQ_RULE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__IMPORTER = SPECIFIED_DQ_RULE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__STEREOTYPE = SPECIFIED_DQ_RULE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__TAGGED_VALUE = SPECIFIED_DQ_RULE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__DOCUMENT = SPECIFIED_DQ_RULE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__DESCRIPTION = SPECIFIED_DQ_RULE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__RESPONSIBLE_PARTY = SPECIFIED_DQ_RULE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__ELEMENT_NODE = SPECIFIED_DQ_RULE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__SET = SPECIFIED_DQ_RULE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__RENDERED_OBJECT = SPECIFIED_DQ_RULE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__VOCABULARY_ELEMENT = SPECIFIED_DQ_RULE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__MEASUREMENT = SPECIFIED_DQ_RULE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__CHANGE_REQUEST = SPECIFIED_DQ_RULE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__DASDL_PROPERTY = SPECIFIED_DQ_RULE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__CATEGORIES = SPECIFIED_DQ_RULE__CATEGORIES;

    /**
     * The feature id for the '<em><b>Aggregated Definitions</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__AGGREGATED_DEFINITIONS = SPECIFIED_DQ_RULE__AGGREGATED_DEFINITIONS;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__LABEL = SPECIFIED_DQ_RULE__LABEL;

    /**
     * The feature id for the '<em><b>Sub Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__SUB_CATEGORIES = SPECIFIED_DQ_RULE__SUB_CATEGORIES;

    /**
     * The feature id for the '<em><b>Sql Generic Expression</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__SQL_GENERIC_EXPRESSION = SPECIFIED_DQ_RULE__SQL_GENERIC_EXPRESSION;

    /**
     * The feature id for the '<em><b>Aggregate1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__AGGREGATE1ARG_FUNCTIONS = SPECIFIED_DQ_RULE__AGGREGATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Date1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__DATE1ARG_FUNCTIONS = SPECIFIED_DQ_RULE__DATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Characters Mapping</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__CHARACTERS_MAPPING = SPECIFIED_DQ_RULE__CHARACTERS_MAPPING;

    /**
     * The feature id for the '<em><b>Indicator Definition Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int WHERE_RULE__INDICATOR_DEFINITION_PARAMETER = SPECIFIED_DQ_RULE__INDICATOR_DEFINITION_PARAMETER;

				/**
     * The feature id for the '<em><b>Criticality Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__CRITICALITY_LEVEL = SPECIFIED_DQ_RULE__CRITICALITY_LEVEL;

    /**
     * The feature id for the '<em><b>Elements</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__ELEMENTS = SPECIFIED_DQ_RULE__ELEMENTS;

    /**
     * The feature id for the '<em><b>Where Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__WHERE_EXPRESSION = SPECIFIED_DQ_RULE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Join Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__JOIN_EXPRESSION = SPECIFIED_DQ_RULE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Joins</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE__JOINS = SPECIFIED_DQ_RULE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Where Rule</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHERE_RULE_FEATURE_COUNT = SPECIFIED_DQ_RULE_FEATURE_COUNT + 3;


    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.JoinElementImpl <em>Join Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.JoinElementImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getJoinElement()
     * @generated
     */
    int JOIN_ELEMENT = 4;

    /**
     * The feature id for the '<em><b>Col A</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOIN_ELEMENT__COL_A = 0;

    /**
     * The feature id for the '<em><b>Col B</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOIN_ELEMENT__COL_B = 1;

    /**
     * The feature id for the '<em><b>Operator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOIN_ELEMENT__OPERATOR = 2;

    /**
     * The feature id for the '<em><b>Table Alias A</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOIN_ELEMENT__TABLE_ALIAS_A = 3;

    /**
     * The feature id for the '<em><b>Table Alias B</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOIN_ELEMENT__TABLE_ALIAS_B = 4;

    /**
     * The feature id for the '<em><b>Column Alias A</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOIN_ELEMENT__COLUMN_ALIAS_A = 5;

    /**
     * The feature id for the '<em><b>Column Alias B</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOIN_ELEMENT__COLUMN_ALIAS_B = 6;

    /**
     * The number of structural features of the '<em>Join Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JOIN_ELEMENT_FEATURE_COUNT = 7;


    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.ParserRuleImpl <em>Parser Rule</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.ParserRuleImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getParserRule()
     * @generated
     */
    int PARSER_RULE = 5;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__NAME = DQ_RULE__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__VISIBILITY = DQ_RULE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__CLIENT_DEPENDENCY = DQ_RULE__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__SUPPLIER_DEPENDENCY = DQ_RULE__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__CONSTRAINT = DQ_RULE__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__NAMESPACE = DQ_RULE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__IMPORTER = DQ_RULE__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__STEREOTYPE = DQ_RULE__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__TAGGED_VALUE = DQ_RULE__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__DOCUMENT = DQ_RULE__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__DESCRIPTION = DQ_RULE__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__RESPONSIBLE_PARTY = DQ_RULE__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__ELEMENT_NODE = DQ_RULE__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__SET = DQ_RULE__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__RENDERED_OBJECT = DQ_RULE__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__VOCABULARY_ELEMENT = DQ_RULE__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__MEASUREMENT = DQ_RULE__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__CHANGE_REQUEST = DQ_RULE__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__DASDL_PROPERTY = DQ_RULE__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__CATEGORIES = DQ_RULE__CATEGORIES;

    /**
     * The feature id for the '<em><b>Aggregated Definitions</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__AGGREGATED_DEFINITIONS = DQ_RULE__AGGREGATED_DEFINITIONS;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__LABEL = DQ_RULE__LABEL;

    /**
     * The feature id for the '<em><b>Sub Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__SUB_CATEGORIES = DQ_RULE__SUB_CATEGORIES;

    /**
     * The feature id for the '<em><b>Sql Generic Expression</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__SQL_GENERIC_EXPRESSION = DQ_RULE__SQL_GENERIC_EXPRESSION;

    /**
     * The feature id for the '<em><b>Aggregate1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__AGGREGATE1ARG_FUNCTIONS = DQ_RULE__AGGREGATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Date1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__DATE1ARG_FUNCTIONS = DQ_RULE__DATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Characters Mapping</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__CHARACTERS_MAPPING = DQ_RULE__CHARACTERS_MAPPING;

    /**
     * The feature id for the '<em><b>Indicator Definition Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__INDICATOR_DEFINITION_PARAMETER = DQ_RULE__INDICATOR_DEFINITION_PARAMETER;

    /**
     * The feature id for the '<em><b>Criticality Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__CRITICALITY_LEVEL = DQ_RULE__CRITICALITY_LEVEL;

    /**
     * The feature id for the '<em><b>Elements</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE__ELEMENTS = DQ_RULE__ELEMENTS;

    /**
     * The number of structural features of the '<em>Parser Rule</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARSER_RULE_FEATURE_COUNT = DQ_RULE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl <em>Match Rule Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getMatchRuleDefinition()
     * @generated
     */
    int MATCH_RULE_DEFINITION = 6;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__NAME = DefinitionPackage.INDICATOR_DEFINITION__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__VISIBILITY = DefinitionPackage.INDICATOR_DEFINITION__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__CLIENT_DEPENDENCY = DefinitionPackage.INDICATOR_DEFINITION__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__SUPPLIER_DEPENDENCY = DefinitionPackage.INDICATOR_DEFINITION__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__CONSTRAINT = DefinitionPackage.INDICATOR_DEFINITION__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__NAMESPACE = DefinitionPackage.INDICATOR_DEFINITION__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__IMPORTER = DefinitionPackage.INDICATOR_DEFINITION__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__STEREOTYPE = DefinitionPackage.INDICATOR_DEFINITION__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__TAGGED_VALUE = DefinitionPackage.INDICATOR_DEFINITION__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__DOCUMENT = DefinitionPackage.INDICATOR_DEFINITION__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__DESCRIPTION = DefinitionPackage.INDICATOR_DEFINITION__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__RESPONSIBLE_PARTY = DefinitionPackage.INDICATOR_DEFINITION__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__ELEMENT_NODE = DefinitionPackage.INDICATOR_DEFINITION__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__SET = DefinitionPackage.INDICATOR_DEFINITION__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__RENDERED_OBJECT = DefinitionPackage.INDICATOR_DEFINITION__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__VOCABULARY_ELEMENT = DefinitionPackage.INDICATOR_DEFINITION__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__MEASUREMENT = DefinitionPackage.INDICATOR_DEFINITION__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__CHANGE_REQUEST = DefinitionPackage.INDICATOR_DEFINITION__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__DASDL_PROPERTY = DefinitionPackage.INDICATOR_DEFINITION__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__CATEGORIES = DefinitionPackage.INDICATOR_DEFINITION__CATEGORIES;

    /**
     * The feature id for the '<em><b>Aggregated Definitions</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__AGGREGATED_DEFINITIONS = DefinitionPackage.INDICATOR_DEFINITION__AGGREGATED_DEFINITIONS;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__LABEL = DefinitionPackage.INDICATOR_DEFINITION__LABEL;

    /**
     * The feature id for the '<em><b>Sub Categories</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__SUB_CATEGORIES = DefinitionPackage.INDICATOR_DEFINITION__SUB_CATEGORIES;

    /**
     * The feature id for the '<em><b>Sql Generic Expression</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__SQL_GENERIC_EXPRESSION = DefinitionPackage.INDICATOR_DEFINITION__SQL_GENERIC_EXPRESSION;

    /**
     * The feature id for the '<em><b>Aggregate1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__AGGREGATE1ARG_FUNCTIONS = DefinitionPackage.INDICATOR_DEFINITION__AGGREGATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Date1arg Functions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__DATE1ARG_FUNCTIONS = DefinitionPackage.INDICATOR_DEFINITION__DATE1ARG_FUNCTIONS;

    /**
     * The feature id for the '<em><b>Characters Mapping</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__CHARACTERS_MAPPING = DefinitionPackage.INDICATOR_DEFINITION__CHARACTERS_MAPPING;

    /**
     * The feature id for the '<em><b>Indicator Definition Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__INDICATOR_DEFINITION_PARAMETER = DefinitionPackage.INDICATOR_DEFINITION__INDICATOR_DEFINITION_PARAMETER;

    /**
     * The feature id for the '<em><b>Block Keys</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__BLOCK_KEYS = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Match Rules</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__MATCH_RULES = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Xmi Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__XMI_ID = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Record Linkage Algorithm</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__RECORD_LINKAGE_ALGORITHM = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Applied Block Keys</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__APPLIED_BLOCK_KEYS = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Survivorship Keys</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__SURVIVORSHIP_KEYS = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Default Survivorship Definitions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__DEFAULT_SURVIVORSHIP_DEFINITIONS = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Match Group Quality Threshold</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__MATCH_GROUP_QUALITY_THRESHOLD = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Particular Default Survivorship Definitions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION__PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 8;

    /**
     * The number of structural features of the '<em>Match Rule Definition</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_DEFINITION_FEATURE_COUNT = DefinitionPackage.INDICATOR_DEFINITION_FEATURE_COUNT + 9;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.KeyDefinitionImpl <em>Key Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.KeyDefinitionImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getKeyDefinition()
     * @generated
     */
    int KEY_DEFINITION = 8;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__NAME = CorePackage.MODEL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__VISIBILITY = CorePackage.MODEL_ELEMENT__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__CLIENT_DEPENDENCY = CorePackage.MODEL_ELEMENT__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__SUPPLIER_DEPENDENCY = CorePackage.MODEL_ELEMENT__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__CONSTRAINT = CorePackage.MODEL_ELEMENT__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__NAMESPACE = CorePackage.MODEL_ELEMENT__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__IMPORTER = CorePackage.MODEL_ELEMENT__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__STEREOTYPE = CorePackage.MODEL_ELEMENT__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__TAGGED_VALUE = CorePackage.MODEL_ELEMENT__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__DOCUMENT = CorePackage.MODEL_ELEMENT__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__DESCRIPTION = CorePackage.MODEL_ELEMENT__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__RESPONSIBLE_PARTY = CorePackage.MODEL_ELEMENT__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__ELEMENT_NODE = CorePackage.MODEL_ELEMENT__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__SET = CorePackage.MODEL_ELEMENT__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__RENDERED_OBJECT = CorePackage.MODEL_ELEMENT__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__VOCABULARY_ELEMENT = CorePackage.MODEL_ELEMENT__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__MEASUREMENT = CorePackage.MODEL_ELEMENT__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__CHANGE_REQUEST = CorePackage.MODEL_ELEMENT__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__DASDL_PROPERTY = CorePackage.MODEL_ELEMENT__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION__COLUMN = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Key Definition</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int KEY_DEFINITION_FEATURE_COUNT = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.BlockKeyDefinitionImpl <em>Block Key Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.BlockKeyDefinitionImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getBlockKeyDefinition()
     * @generated
     */
    int BLOCK_KEY_DEFINITION = 7;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__NAME = KEY_DEFINITION__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__VISIBILITY = KEY_DEFINITION__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__CLIENT_DEPENDENCY = KEY_DEFINITION__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__SUPPLIER_DEPENDENCY = KEY_DEFINITION__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__CONSTRAINT = KEY_DEFINITION__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__NAMESPACE = KEY_DEFINITION__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__IMPORTER = KEY_DEFINITION__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__STEREOTYPE = KEY_DEFINITION__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__TAGGED_VALUE = KEY_DEFINITION__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__DOCUMENT = KEY_DEFINITION__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__DESCRIPTION = KEY_DEFINITION__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__RESPONSIBLE_PARTY = KEY_DEFINITION__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__ELEMENT_NODE = KEY_DEFINITION__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__SET = KEY_DEFINITION__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__RENDERED_OBJECT = KEY_DEFINITION__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__VOCABULARY_ELEMENT = KEY_DEFINITION__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__MEASUREMENT = KEY_DEFINITION__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__CHANGE_REQUEST = KEY_DEFINITION__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__DASDL_PROPERTY = KEY_DEFINITION__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__COLUMN = KEY_DEFINITION__COLUMN;

    /**
     * The feature id for the '<em><b>Pre Algorithm</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__PRE_ALGORITHM = KEY_DEFINITION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Algorithm</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__ALGORITHM = KEY_DEFINITION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Post Algorithm</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION__POST_ALGORITHM = KEY_DEFINITION_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Block Key Definition</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_KEY_DEFINITION_FEATURE_COUNT = KEY_DEFINITION_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.MatchKeyDefinitionImpl <em>Match Key Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.MatchKeyDefinitionImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getMatchKeyDefinition()
     * @generated
     */
    int MATCH_KEY_DEFINITION = 9;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__NAME = KEY_DEFINITION__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__VISIBILITY = KEY_DEFINITION__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__CLIENT_DEPENDENCY = KEY_DEFINITION__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__SUPPLIER_DEPENDENCY = KEY_DEFINITION__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__CONSTRAINT = KEY_DEFINITION__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__NAMESPACE = KEY_DEFINITION__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__IMPORTER = KEY_DEFINITION__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__STEREOTYPE = KEY_DEFINITION__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__TAGGED_VALUE = KEY_DEFINITION__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__DOCUMENT = KEY_DEFINITION__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__DESCRIPTION = KEY_DEFINITION__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__RESPONSIBLE_PARTY = KEY_DEFINITION__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__ELEMENT_NODE = KEY_DEFINITION__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__SET = KEY_DEFINITION__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__RENDERED_OBJECT = KEY_DEFINITION__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__VOCABULARY_ELEMENT = KEY_DEFINITION__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__MEASUREMENT = KEY_DEFINITION__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__CHANGE_REQUEST = KEY_DEFINITION__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__DASDL_PROPERTY = KEY_DEFINITION__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__COLUMN = KEY_DEFINITION__COLUMN;

    /**
     * The feature id for the '<em><b>Algorithm</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__ALGORITHM = KEY_DEFINITION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Confidence Weight</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__CONFIDENCE_WEIGHT = KEY_DEFINITION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Handle Null</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__HANDLE_NULL = KEY_DEFINITION_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Threshold</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__THRESHOLD = KEY_DEFINITION_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Tokenization Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION__TOKENIZATION_TYPE = KEY_DEFINITION_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Match Key Definition</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_KEY_DEFINITION_FEATURE_COUNT = KEY_DEFINITION_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.AlgorithmDefinitionImpl <em>Algorithm Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.AlgorithmDefinitionImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getAlgorithmDefinition()
     * @generated
     */
    int ALGORITHM_DEFINITION = 10;

    /**
     * The feature id for the '<em><b>Algorithm Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALGORITHM_DEFINITION__ALGORITHM_TYPE = 0;

    /**
     * The feature id for the '<em><b>Algorithm Parameters</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALGORITHM_DEFINITION__ALGORITHM_PARAMETERS = 1;

    /**
     * The number of structural features of the '<em>Algorithm Definition</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALGORITHM_DEFINITION_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.MatchRuleImpl <em>Match Rule</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.MatchRuleImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getMatchRule()
     * @generated
     */
    int MATCH_RULE = 11;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__NAME = CorePackage.MODEL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__VISIBILITY = CorePackage.MODEL_ELEMENT__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__CLIENT_DEPENDENCY = CorePackage.MODEL_ELEMENT__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__SUPPLIER_DEPENDENCY = CorePackage.MODEL_ELEMENT__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__CONSTRAINT = CorePackage.MODEL_ELEMENT__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__NAMESPACE = CorePackage.MODEL_ELEMENT__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__IMPORTER = CorePackage.MODEL_ELEMENT__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__STEREOTYPE = CorePackage.MODEL_ELEMENT__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__TAGGED_VALUE = CorePackage.MODEL_ELEMENT__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__DOCUMENT = CorePackage.MODEL_ELEMENT__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__DESCRIPTION = CorePackage.MODEL_ELEMENT__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__RESPONSIBLE_PARTY = CorePackage.MODEL_ELEMENT__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__ELEMENT_NODE = CorePackage.MODEL_ELEMENT__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__SET = CorePackage.MODEL_ELEMENT__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__RENDERED_OBJECT = CorePackage.MODEL_ELEMENT__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__VOCABULARY_ELEMENT = CorePackage.MODEL_ELEMENT__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__MEASUREMENT = CorePackage.MODEL_ELEMENT__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__CHANGE_REQUEST = CorePackage.MODEL_ELEMENT__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__DASDL_PROPERTY = CorePackage.MODEL_ELEMENT__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Match Keys</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__MATCH_KEYS = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Match Interval</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE__MATCH_INTERVAL = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Match Rule</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATCH_RULE_FEATURE_COUNT = CorePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.AppliedBlockKeyImpl <em>Applied Block Key</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.AppliedBlockKeyImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getAppliedBlockKey()
     * @generated
     */
    int APPLIED_BLOCK_KEY = 12;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__NAME = KEY_DEFINITION__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__VISIBILITY = KEY_DEFINITION__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__CLIENT_DEPENDENCY = KEY_DEFINITION__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__SUPPLIER_DEPENDENCY = KEY_DEFINITION__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__CONSTRAINT = KEY_DEFINITION__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__NAMESPACE = KEY_DEFINITION__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__IMPORTER = KEY_DEFINITION__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__STEREOTYPE = KEY_DEFINITION__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__TAGGED_VALUE = KEY_DEFINITION__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__DOCUMENT = KEY_DEFINITION__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__DESCRIPTION = KEY_DEFINITION__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__RESPONSIBLE_PARTY = KEY_DEFINITION__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__ELEMENT_NODE = KEY_DEFINITION__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__SET = KEY_DEFINITION__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__RENDERED_OBJECT = KEY_DEFINITION__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__VOCABULARY_ELEMENT = KEY_DEFINITION__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__MEASUREMENT = KEY_DEFINITION__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__CHANGE_REQUEST = KEY_DEFINITION__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__DASDL_PROPERTY = KEY_DEFINITION__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY__COLUMN = KEY_DEFINITION__COLUMN;

    /**
     * The number of structural features of the '<em>Applied Block Key</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLIED_BLOCK_KEY_FEATURE_COUNT = KEY_DEFINITION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.SurvivorshipKeyDefinitionImpl <em>Survivorship Key Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.SurvivorshipKeyDefinitionImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getSurvivorshipKeyDefinition()
     * @generated
     */
    int SURVIVORSHIP_KEY_DEFINITION = 13;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__NAME = KEY_DEFINITION__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__VISIBILITY = KEY_DEFINITION__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__CLIENT_DEPENDENCY = KEY_DEFINITION__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__SUPPLIER_DEPENDENCY = KEY_DEFINITION__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__CONSTRAINT = KEY_DEFINITION__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__NAMESPACE = KEY_DEFINITION__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__IMPORTER = KEY_DEFINITION__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__STEREOTYPE = KEY_DEFINITION__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__TAGGED_VALUE = KEY_DEFINITION__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__DOCUMENT = KEY_DEFINITION__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__DESCRIPTION = KEY_DEFINITION__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__RESPONSIBLE_PARTY = KEY_DEFINITION__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__ELEMENT_NODE = KEY_DEFINITION__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__SET = KEY_DEFINITION__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__RENDERED_OBJECT = KEY_DEFINITION__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__VOCABULARY_ELEMENT = KEY_DEFINITION__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__MEASUREMENT = KEY_DEFINITION__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__CHANGE_REQUEST = KEY_DEFINITION__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__DASDL_PROPERTY = KEY_DEFINITION__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__COLUMN = KEY_DEFINITION__COLUMN;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__FUNCTION = KEY_DEFINITION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Allow Manual Resolution</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION__ALLOW_MANUAL_RESOLUTION = KEY_DEFINITION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Survivorship Key Definition</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SURVIVORSHIP_KEY_DEFINITION_FEATURE_COUNT = KEY_DEFINITION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.DefaultSurvivorshipDefinitionImpl <em>Default Survivorship Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.DefaultSurvivorshipDefinitionImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getDefaultSurvivorshipDefinition()
     * @generated
     */
    int DEFAULT_SURVIVORSHIP_DEFINITION = 14;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_SURVIVORSHIP_DEFINITION__DATA_TYPE = 0;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_SURVIVORSHIP_DEFINITION__FUNCTION = 1;

    /**
     * The number of structural features of the '<em>Default Survivorship Definition</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_SURVIVORSHIP_DEFINITION_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.talend.dataquality.rules.impl.ParticularDefaultSurvivorshipDefinitionsImpl <em>Particular Default Survivorship Definitions</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.talend.dataquality.rules.impl.ParticularDefaultSurvivorshipDefinitionsImpl
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getParticularDefaultSurvivorshipDefinitions()
     * @generated
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS = 15;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__NAME = KEY_DEFINITION__NAME;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__VISIBILITY = KEY_DEFINITION__VISIBILITY;

    /**
     * The feature id for the '<em><b>Client Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__CLIENT_DEPENDENCY = KEY_DEFINITION__CLIENT_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Supplier Dependency</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__SUPPLIER_DEPENDENCY = KEY_DEFINITION__SUPPLIER_DEPENDENCY;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__CONSTRAINT = KEY_DEFINITION__CONSTRAINT;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__NAMESPACE = KEY_DEFINITION__NAMESPACE;

    /**
     * The feature id for the '<em><b>Importer</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__IMPORTER = KEY_DEFINITION__IMPORTER;

    /**
     * The feature id for the '<em><b>Stereotype</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__STEREOTYPE = KEY_DEFINITION__STEREOTYPE;

    /**
     * The feature id for the '<em><b>Tagged Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__TAGGED_VALUE = KEY_DEFINITION__TAGGED_VALUE;

    /**
     * The feature id for the '<em><b>Document</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__DOCUMENT = KEY_DEFINITION__DOCUMENT;

    /**
     * The feature id for the '<em><b>Description</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__DESCRIPTION = KEY_DEFINITION__DESCRIPTION;

    /**
     * The feature id for the '<em><b>Responsible Party</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__RESPONSIBLE_PARTY = KEY_DEFINITION__RESPONSIBLE_PARTY;

    /**
     * The feature id for the '<em><b>Element Node</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__ELEMENT_NODE = KEY_DEFINITION__ELEMENT_NODE;

    /**
     * The feature id for the '<em><b>Set</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__SET = KEY_DEFINITION__SET;

    /**
     * The feature id for the '<em><b>Rendered Object</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__RENDERED_OBJECT = KEY_DEFINITION__RENDERED_OBJECT;

    /**
     * The feature id for the '<em><b>Vocabulary Element</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__VOCABULARY_ELEMENT = KEY_DEFINITION__VOCABULARY_ELEMENT;

    /**
     * The feature id for the '<em><b>Measurement</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__MEASUREMENT = KEY_DEFINITION__MEASUREMENT;

    /**
     * The feature id for the '<em><b>Change Request</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__CHANGE_REQUEST = KEY_DEFINITION__CHANGE_REQUEST;

    /**
     * The feature id for the '<em><b>Dasdl Property</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__DASDL_PROPERTY = KEY_DEFINITION__DASDL_PROPERTY;

    /**
     * The feature id for the '<em><b>Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__COLUMN = KEY_DEFINITION__COLUMN;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__DATA_TYPE = KEY_DEFINITION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS__FUNCTION = KEY_DEFINITION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Particular Default Survivorship Definitions</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS_FEATURE_COUNT = KEY_DEFINITION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '<em>Td Expression List</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.List
     * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getTdExpressionList()
     * @generated
     */
    int TD_EXPRESSION_LIST = 16;


    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.DQRule <em>DQ Rule</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>DQ Rule</em>'.
     * @see org.talend.dataquality.rules.DQRule
     * @generated
     */
    EClass getDQRule();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.DQRule#getCriticalityLevel <em>Criticality Level</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Criticality Level</em>'.
     * @see org.talend.dataquality.rules.DQRule#getCriticalityLevel()
     * @see #getDQRule()
     * @generated
     */
    EAttribute getDQRule_CriticalityLevel();

    /**
     * Returns the meta object for the reference list '{@link org.talend.dataquality.rules.DQRule#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Elements</em>'.
     * @see org.talend.dataquality.rules.DQRule#getElements()
     * @see #getDQRule()
     * @generated
     */
    EReference getDQRule_Elements();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.SpecifiedDQRule <em>Specified DQ Rule</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Specified DQ Rule</em>'.
     * @see org.talend.dataquality.rules.SpecifiedDQRule
     * @generated
     */
    EClass getSpecifiedDQRule();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.InferredDQRule <em>Inferred DQ Rule</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Inferred DQ Rule</em>'.
     * @see org.talend.dataquality.rules.InferredDQRule
     * @generated
     */
    EClass getInferredDQRule();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.WhereRule <em>Where Rule</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Where Rule</em>'.
     * @see org.talend.dataquality.rules.WhereRule
     * @generated
     */
    EClass getWhereRule();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.WhereRule#getWhereExpression <em>Where Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Where Expression</em>'.
     * @see org.talend.dataquality.rules.WhereRule#getWhereExpression()
     * @see #getWhereRule()
     * @generated
     */
    EAttribute getWhereRule_WhereExpression();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.WhereRule#getJoinExpression <em>Join Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Join Expression</em>'.
     * @see org.talend.dataquality.rules.WhereRule#getJoinExpression()
     * @see #getWhereRule()
     * @generated
     */
    EAttribute getWhereRule_JoinExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.rules.WhereRule#getJoins <em>Joins</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Joins</em>'.
     * @see org.talend.dataquality.rules.WhereRule#getJoins()
     * @see #getWhereRule()
     * @generated
     */
    EReference getWhereRule_Joins();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.JoinElement <em>Join Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Join Element</em>'.
     * @see org.talend.dataquality.rules.JoinElement
     * @generated
     */
    EClass getJoinElement();

    /**
     * Returns the meta object for the reference '{@link org.talend.dataquality.rules.JoinElement#getColA <em>Col A</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Col A</em>'.
     * @see org.talend.dataquality.rules.JoinElement#getColA()
     * @see #getJoinElement()
     * @generated
     */
    EReference getJoinElement_ColA();

    /**
     * Returns the meta object for the reference '{@link org.talend.dataquality.rules.JoinElement#getColB <em>Col B</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Col B</em>'.
     * @see org.talend.dataquality.rules.JoinElement#getColB()
     * @see #getJoinElement()
     * @generated
     */
    EReference getJoinElement_ColB();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.JoinElement#getOperator <em>Operator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Operator</em>'.
     * @see org.talend.dataquality.rules.JoinElement#getOperator()
     * @see #getJoinElement()
     * @generated
     */
    EAttribute getJoinElement_Operator();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.JoinElement#getTableAliasA <em>Table Alias A</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Table Alias A</em>'.
     * @see org.talend.dataquality.rules.JoinElement#getTableAliasA()
     * @see #getJoinElement()
     * @generated
     */
    EAttribute getJoinElement_TableAliasA();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.JoinElement#getTableAliasB <em>Table Alias B</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Table Alias B</em>'.
     * @see org.talend.dataquality.rules.JoinElement#getTableAliasB()
     * @see #getJoinElement()
     * @generated
     */
    EAttribute getJoinElement_TableAliasB();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.JoinElement#getColumnAliasA <em>Column Alias A</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Column Alias A</em>'.
     * @see org.talend.dataquality.rules.JoinElement#getColumnAliasA()
     * @see #getJoinElement()
     * @generated
     */
    EAttribute getJoinElement_ColumnAliasA();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.JoinElement#getColumnAliasB <em>Column Alias B</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Column Alias B</em>'.
     * @see org.talend.dataquality.rules.JoinElement#getColumnAliasB()
     * @see #getJoinElement()
     * @generated
     */
    EAttribute getJoinElement_ColumnAliasB();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.ParserRule <em>Parser Rule</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Parser Rule</em>'.
     * @see org.talend.dataquality.rules.ParserRule
     * @generated
     */
    EClass getParserRule();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.MatchRuleDefinition <em>Match Rule Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Match Rule Definition</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition
     * @generated
     */
    EClass getMatchRuleDefinition();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.rules.MatchRuleDefinition#getBlockKeys <em>Block Keys</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Block Keys</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition#getBlockKeys()
     * @see #getMatchRuleDefinition()
     * @generated
     */
    EReference getMatchRuleDefinition_BlockKeys();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.rules.MatchRuleDefinition#getMatchRules <em>Match Rules</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Match Rules</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition#getMatchRules()
     * @see #getMatchRuleDefinition()
     * @generated
     */
    EReference getMatchRuleDefinition_MatchRules();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.MatchRuleDefinition#getXmiId <em>Xmi Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Xmi Id</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition#getXmiId()
     * @see #getMatchRuleDefinition()
     * @generated
     */
    EAttribute getMatchRuleDefinition_XmiId();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.MatchRuleDefinition#getRecordLinkageAlgorithm <em>Record Linkage Algorithm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Record Linkage Algorithm</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition#getRecordLinkageAlgorithm()
     * @see #getMatchRuleDefinition()
     * @generated
     */
    EAttribute getMatchRuleDefinition_RecordLinkageAlgorithm();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.rules.MatchRuleDefinition#getAppliedBlockKeys <em>Applied Block Keys</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Applied Block Keys</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition#getAppliedBlockKeys()
     * @see #getMatchRuleDefinition()
     * @generated
     */
    EReference getMatchRuleDefinition_AppliedBlockKeys();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.rules.MatchRuleDefinition#getSurvivorshipKeys <em>Survivorship Keys</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Survivorship Keys</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition#getSurvivorshipKeys()
     * @see #getMatchRuleDefinition()
     * @generated
     */
    EReference getMatchRuleDefinition_SurvivorshipKeys();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.rules.MatchRuleDefinition#getDefaultSurvivorshipDefinitions <em>Default Survivorship Definitions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Default Survivorship Definitions</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition#getDefaultSurvivorshipDefinitions()
     * @see #getMatchRuleDefinition()
     * @generated
     */
    EReference getMatchRuleDefinition_DefaultSurvivorshipDefinitions();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.MatchRuleDefinition#getMatchGroupQualityThreshold <em>Match Group Quality Threshold</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Match Group Quality Threshold</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition#getMatchGroupQualityThreshold()
     * @see #getMatchRuleDefinition()
     * @generated
     */
    EAttribute getMatchRuleDefinition_MatchGroupQualityThreshold();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.rules.MatchRuleDefinition#getParticularDefaultSurvivorshipDefinitions <em>Particular Default Survivorship Definitions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Particular Default Survivorship Definitions</em>'.
     * @see org.talend.dataquality.rules.MatchRuleDefinition#getParticularDefaultSurvivorshipDefinitions()
     * @see #getMatchRuleDefinition()
     * @generated
     */
    EReference getMatchRuleDefinition_ParticularDefaultSurvivorshipDefinitions();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.BlockKeyDefinition <em>Block Key Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Block Key Definition</em>'.
     * @see org.talend.dataquality.rules.BlockKeyDefinition
     * @generated
     */
    EClass getBlockKeyDefinition();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.rules.BlockKeyDefinition#getPreAlgorithm <em>Pre Algorithm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Pre Algorithm</em>'.
     * @see org.talend.dataquality.rules.BlockKeyDefinition#getPreAlgorithm()
     * @see #getBlockKeyDefinition()
     * @generated
     */
    EReference getBlockKeyDefinition_PreAlgorithm();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.rules.BlockKeyDefinition#getAlgorithm <em>Algorithm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Algorithm</em>'.
     * @see org.talend.dataquality.rules.BlockKeyDefinition#getAlgorithm()
     * @see #getBlockKeyDefinition()
     * @generated
     */
    EReference getBlockKeyDefinition_Algorithm();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.rules.BlockKeyDefinition#getPostAlgorithm <em>Post Algorithm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Post Algorithm</em>'.
     * @see org.talend.dataquality.rules.BlockKeyDefinition#getPostAlgorithm()
     * @see #getBlockKeyDefinition()
     * @generated
     */
    EReference getBlockKeyDefinition_PostAlgorithm();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.KeyDefinition <em>Key Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Key Definition</em>'.
     * @see org.talend.dataquality.rules.KeyDefinition
     * @generated
     */
    EClass getKeyDefinition();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.KeyDefinition#getColumn <em>Column</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Column</em>'.
     * @see org.talend.dataquality.rules.KeyDefinition#getColumn()
     * @see #getKeyDefinition()
     * @generated
     */
    EAttribute getKeyDefinition_Column();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.MatchKeyDefinition <em>Match Key Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Match Key Definition</em>'.
     * @see org.talend.dataquality.rules.MatchKeyDefinition
     * @generated
     */
    EClass getMatchKeyDefinition();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.rules.MatchKeyDefinition#getAlgorithm <em>Algorithm</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Algorithm</em>'.
     * @see org.talend.dataquality.rules.MatchKeyDefinition#getAlgorithm()
     * @see #getMatchKeyDefinition()
     * @generated
     */
    EReference getMatchKeyDefinition_Algorithm();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.MatchKeyDefinition#getConfidenceWeight <em>Confidence Weight</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Confidence Weight</em>'.
     * @see org.talend.dataquality.rules.MatchKeyDefinition#getConfidenceWeight()
     * @see #getMatchKeyDefinition()
     * @generated
     */
    EAttribute getMatchKeyDefinition_ConfidenceWeight();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.MatchKeyDefinition#getHandleNull <em>Handle Null</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Handle Null</em>'.
     * @see org.talend.dataquality.rules.MatchKeyDefinition#getHandleNull()
     * @see #getMatchKeyDefinition()
     * @generated
     */
    EAttribute getMatchKeyDefinition_HandleNull();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.MatchKeyDefinition#getThreshold <em>Threshold</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Threshold</em>'.
     * @see org.talend.dataquality.rules.MatchKeyDefinition#getThreshold()
     * @see #getMatchKeyDefinition()
     * @generated
     */
    EAttribute getMatchKeyDefinition_Threshold();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.MatchKeyDefinition#getTokenizationType <em>Tokenization Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Tokenization Type</em>'.
     * @see org.talend.dataquality.rules.MatchKeyDefinition#getTokenizationType()
     * @see #getMatchKeyDefinition()
     * @generated
     */
    EAttribute getMatchKeyDefinition_TokenizationType();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.AlgorithmDefinition <em>Algorithm Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Algorithm Definition</em>'.
     * @see org.talend.dataquality.rules.AlgorithmDefinition
     * @generated
     */
    EClass getAlgorithmDefinition();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.AlgorithmDefinition#getAlgorithmType <em>Algorithm Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Algorithm Type</em>'.
     * @see org.talend.dataquality.rules.AlgorithmDefinition#getAlgorithmType()
     * @see #getAlgorithmDefinition()
     * @generated
     */
    EAttribute getAlgorithmDefinition_AlgorithmType();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.AlgorithmDefinition#getAlgorithmParameters <em>Algorithm Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Algorithm Parameters</em>'.
     * @see org.talend.dataquality.rules.AlgorithmDefinition#getAlgorithmParameters()
     * @see #getAlgorithmDefinition()
     * @generated
     */
    EAttribute getAlgorithmDefinition_AlgorithmParameters();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.MatchRule <em>Match Rule</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Match Rule</em>'.
     * @see org.talend.dataquality.rules.MatchRule
     * @generated
     */
    EClass getMatchRule();

    /**
     * Returns the meta object for the containment reference list '{@link org.talend.dataquality.rules.MatchRule#getMatchKeys <em>Match Keys</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Match Keys</em>'.
     * @see org.talend.dataquality.rules.MatchRule#getMatchKeys()
     * @see #getMatchRule()
     * @generated
     */
    EReference getMatchRule_MatchKeys();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.MatchRule#getMatchInterval <em>Match Interval</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Match Interval</em>'.
     * @see org.talend.dataquality.rules.MatchRule#getMatchInterval()
     * @see #getMatchRule()
     * @generated
     */
    EAttribute getMatchRule_MatchInterval();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.AppliedBlockKey <em>Applied Block Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Applied Block Key</em>'.
     * @see org.talend.dataquality.rules.AppliedBlockKey
     * @generated
     */
    EClass getAppliedBlockKey();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.SurvivorshipKeyDefinition <em>Survivorship Key Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Survivorship Key Definition</em>'.
     * @see org.talend.dataquality.rules.SurvivorshipKeyDefinition
     * @generated
     */
    EClass getSurvivorshipKeyDefinition();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.rules.SurvivorshipKeyDefinition#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Function</em>'.
     * @see org.talend.dataquality.rules.SurvivorshipKeyDefinition#getFunction()
     * @see #getSurvivorshipKeyDefinition()
     * @generated
     */
    EReference getSurvivorshipKeyDefinition_Function();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.SurvivorshipKeyDefinition#isAllowManualResolution <em>Allow Manual Resolution</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Allow Manual Resolution</em>'.
     * @see org.talend.dataquality.rules.SurvivorshipKeyDefinition#isAllowManualResolution()
     * @see #getSurvivorshipKeyDefinition()
     * @generated
     */
    EAttribute getSurvivorshipKeyDefinition_AllowManualResolution();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.DefaultSurvivorshipDefinition <em>Default Survivorship Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Default Survivorship Definition</em>'.
     * @see org.talend.dataquality.rules.DefaultSurvivorshipDefinition
     * @generated
     */
    EClass getDefaultSurvivorshipDefinition();

    /**
     * Returns the meta object for the attribute '{@link org.talend.dataquality.rules.DefaultSurvivorshipDefinition#getDataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Data Type</em>'.
     * @see org.talend.dataquality.rules.DefaultSurvivorshipDefinition#getDataType()
     * @see #getDefaultSurvivorshipDefinition()
     * @generated
     */
    EAttribute getDefaultSurvivorshipDefinition_DataType();

    /**
     * Returns the meta object for the containment reference '{@link org.talend.dataquality.rules.DefaultSurvivorshipDefinition#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Function</em>'.
     * @see org.talend.dataquality.rules.DefaultSurvivorshipDefinition#getFunction()
     * @see #getDefaultSurvivorshipDefinition()
     * @generated
     */
    EReference getDefaultSurvivorshipDefinition_Function();

    /**
     * Returns the meta object for class '{@link org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions <em>Particular Default Survivorship Definitions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Particular Default Survivorship Definitions</em>'.
     * @see org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions
     * @generated
     */
    EClass getParticularDefaultSurvivorshipDefinitions();

    /**
     * Returns the meta object for data type '{@link java.util.List <em>Td Expression List</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Td Expression List</em>'.
     * @see java.util.List
     * @model instanceClass="java.util.List<org.talend.cwm.relational.TdExpression>"
     * @generated
     */
    EDataType getTdExpressionList();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    RulesFactory getRulesFactory();

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
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.DQRuleImpl <em>DQ Rule</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.DQRuleImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getDQRule()
         * @generated
         */
        EClass DQ_RULE = eINSTANCE.getDQRule();

        /**
         * The meta object literal for the '<em><b>Criticality Level</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DQ_RULE__CRITICALITY_LEVEL = eINSTANCE.getDQRule_CriticalityLevel();

        /**
         * The meta object literal for the '<em><b>Elements</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DQ_RULE__ELEMENTS = eINSTANCE.getDQRule_Elements();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.SpecifiedDQRuleImpl <em>Specified DQ Rule</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.SpecifiedDQRuleImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getSpecifiedDQRule()
         * @generated
         */
        EClass SPECIFIED_DQ_RULE = eINSTANCE.getSpecifiedDQRule();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.InferredDQRuleImpl <em>Inferred DQ Rule</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.InferredDQRuleImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getInferredDQRule()
         * @generated
         */
        EClass INFERRED_DQ_RULE = eINSTANCE.getInferredDQRule();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.WhereRuleImpl <em>Where Rule</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.WhereRuleImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getWhereRule()
         * @generated
         */
        EClass WHERE_RULE = eINSTANCE.getWhereRule();

        /**
         * The meta object literal for the '<em><b>Where Expression</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute WHERE_RULE__WHERE_EXPRESSION = eINSTANCE.getWhereRule_WhereExpression();

        /**
         * The meta object literal for the '<em><b>Join Expression</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute WHERE_RULE__JOIN_EXPRESSION = eINSTANCE.getWhereRule_JoinExpression();

        /**
         * The meta object literal for the '<em><b>Joins</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WHERE_RULE__JOINS = eINSTANCE.getWhereRule_Joins();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.JoinElementImpl <em>Join Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.JoinElementImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getJoinElement()
         * @generated
         */
        EClass JOIN_ELEMENT = eINSTANCE.getJoinElement();

        /**
         * The meta object literal for the '<em><b>Col A</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference JOIN_ELEMENT__COL_A = eINSTANCE.getJoinElement_ColA();

        /**
         * The meta object literal for the '<em><b>Col B</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference JOIN_ELEMENT__COL_B = eINSTANCE.getJoinElement_ColB();

        /**
         * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JOIN_ELEMENT__OPERATOR = eINSTANCE.getJoinElement_Operator();

        /**
         * The meta object literal for the '<em><b>Table Alias A</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JOIN_ELEMENT__TABLE_ALIAS_A = eINSTANCE.getJoinElement_TableAliasA();

        /**
         * The meta object literal for the '<em><b>Table Alias B</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JOIN_ELEMENT__TABLE_ALIAS_B = eINSTANCE.getJoinElement_TableAliasB();

        /**
         * The meta object literal for the '<em><b>Column Alias A</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JOIN_ELEMENT__COLUMN_ALIAS_A = eINSTANCE.getJoinElement_ColumnAliasA();

        /**
         * The meta object literal for the '<em><b>Column Alias B</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JOIN_ELEMENT__COLUMN_ALIAS_B = eINSTANCE.getJoinElement_ColumnAliasB();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.ParserRuleImpl <em>Parser Rule</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.ParserRuleImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getParserRule()
         * @generated
         */
        EClass PARSER_RULE = eINSTANCE.getParserRule();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl <em>Match Rule Definition</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getMatchRuleDefinition()
         * @generated
         */
        EClass MATCH_RULE_DEFINITION = eINSTANCE.getMatchRuleDefinition();

        /**
         * The meta object literal for the '<em><b>Block Keys</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATCH_RULE_DEFINITION__BLOCK_KEYS = eINSTANCE.getMatchRuleDefinition_BlockKeys();

        /**
         * The meta object literal for the '<em><b>Match Rules</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATCH_RULE_DEFINITION__MATCH_RULES = eINSTANCE.getMatchRuleDefinition_MatchRules();

        /**
         * The meta object literal for the '<em><b>Xmi Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCH_RULE_DEFINITION__XMI_ID = eINSTANCE.getMatchRuleDefinition_XmiId();

        /**
         * The meta object literal for the '<em><b>Record Linkage Algorithm</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCH_RULE_DEFINITION__RECORD_LINKAGE_ALGORITHM = eINSTANCE.getMatchRuleDefinition_RecordLinkageAlgorithm();

        /**
         * The meta object literal for the '<em><b>Applied Block Keys</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATCH_RULE_DEFINITION__APPLIED_BLOCK_KEYS = eINSTANCE.getMatchRuleDefinition_AppliedBlockKeys();

        /**
         * The meta object literal for the '<em><b>Survivorship Keys</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATCH_RULE_DEFINITION__SURVIVORSHIP_KEYS = eINSTANCE.getMatchRuleDefinition_SurvivorshipKeys();

        /**
         * The meta object literal for the '<em><b>Default Survivorship Definitions</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATCH_RULE_DEFINITION__DEFAULT_SURVIVORSHIP_DEFINITIONS = eINSTANCE.getMatchRuleDefinition_DefaultSurvivorshipDefinitions();

        /**
         * The meta object literal for the '<em><b>Match Group Quality Threshold</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCH_RULE_DEFINITION__MATCH_GROUP_QUALITY_THRESHOLD = eINSTANCE.getMatchRuleDefinition_MatchGroupQualityThreshold();

        /**
         * The meta object literal for the '<em><b>Particular Default Survivorship Definitions</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATCH_RULE_DEFINITION__PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS = eINSTANCE.getMatchRuleDefinition_ParticularDefaultSurvivorshipDefinitions();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.BlockKeyDefinitionImpl <em>Block Key Definition</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.BlockKeyDefinitionImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getBlockKeyDefinition()
         * @generated
         */
        EClass BLOCK_KEY_DEFINITION = eINSTANCE.getBlockKeyDefinition();

        /**
         * The meta object literal for the '<em><b>Pre Algorithm</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BLOCK_KEY_DEFINITION__PRE_ALGORITHM = eINSTANCE.getBlockKeyDefinition_PreAlgorithm();

        /**
         * The meta object literal for the '<em><b>Algorithm</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BLOCK_KEY_DEFINITION__ALGORITHM = eINSTANCE.getBlockKeyDefinition_Algorithm();

        /**
         * The meta object literal for the '<em><b>Post Algorithm</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BLOCK_KEY_DEFINITION__POST_ALGORITHM = eINSTANCE.getBlockKeyDefinition_PostAlgorithm();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.KeyDefinitionImpl <em>Key Definition</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.KeyDefinitionImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getKeyDefinition()
         * @generated
         */
        EClass KEY_DEFINITION = eINSTANCE.getKeyDefinition();

        /**
         * The meta object literal for the '<em><b>Column</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute KEY_DEFINITION__COLUMN = eINSTANCE.getKeyDefinition_Column();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.MatchKeyDefinitionImpl <em>Match Key Definition</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.MatchKeyDefinitionImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getMatchKeyDefinition()
         * @generated
         */
        EClass MATCH_KEY_DEFINITION = eINSTANCE.getMatchKeyDefinition();

        /**
         * The meta object literal for the '<em><b>Algorithm</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATCH_KEY_DEFINITION__ALGORITHM = eINSTANCE.getMatchKeyDefinition_Algorithm();

        /**
         * The meta object literal for the '<em><b>Confidence Weight</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCH_KEY_DEFINITION__CONFIDENCE_WEIGHT = eINSTANCE.getMatchKeyDefinition_ConfidenceWeight();

        /**
         * The meta object literal for the '<em><b>Handle Null</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCH_KEY_DEFINITION__HANDLE_NULL = eINSTANCE.getMatchKeyDefinition_HandleNull();

        /**
         * The meta object literal for the '<em><b>Threshold</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCH_KEY_DEFINITION__THRESHOLD = eINSTANCE.getMatchKeyDefinition_Threshold();

        /**
         * The meta object literal for the '<em><b>Tokenization Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCH_KEY_DEFINITION__TOKENIZATION_TYPE = eINSTANCE.getMatchKeyDefinition_TokenizationType();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.AlgorithmDefinitionImpl <em>Algorithm Definition</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.AlgorithmDefinitionImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getAlgorithmDefinition()
         * @generated
         */
        EClass ALGORITHM_DEFINITION = eINSTANCE.getAlgorithmDefinition();

        /**
         * The meta object literal for the '<em><b>Algorithm Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ALGORITHM_DEFINITION__ALGORITHM_TYPE = eINSTANCE.getAlgorithmDefinition_AlgorithmType();

        /**
         * The meta object literal for the '<em><b>Algorithm Parameters</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ALGORITHM_DEFINITION__ALGORITHM_PARAMETERS = eINSTANCE.getAlgorithmDefinition_AlgorithmParameters();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.MatchRuleImpl <em>Match Rule</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.MatchRuleImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getMatchRule()
         * @generated
         */
        EClass MATCH_RULE = eINSTANCE.getMatchRule();

        /**
         * The meta object literal for the '<em><b>Match Keys</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATCH_RULE__MATCH_KEYS = eINSTANCE.getMatchRule_MatchKeys();

        /**
         * The meta object literal for the '<em><b>Match Interval</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MATCH_RULE__MATCH_INTERVAL = eINSTANCE.getMatchRule_MatchInterval();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.AppliedBlockKeyImpl <em>Applied Block Key</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.AppliedBlockKeyImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getAppliedBlockKey()
         * @generated
         */
        EClass APPLIED_BLOCK_KEY = eINSTANCE.getAppliedBlockKey();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.SurvivorshipKeyDefinitionImpl <em>Survivorship Key Definition</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.SurvivorshipKeyDefinitionImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getSurvivorshipKeyDefinition()
         * @generated
         */
        EClass SURVIVORSHIP_KEY_DEFINITION = eINSTANCE.getSurvivorshipKeyDefinition();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SURVIVORSHIP_KEY_DEFINITION__FUNCTION = eINSTANCE.getSurvivorshipKeyDefinition_Function();

        /**
         * The meta object literal for the '<em><b>Allow Manual Resolution</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SURVIVORSHIP_KEY_DEFINITION__ALLOW_MANUAL_RESOLUTION = eINSTANCE.getSurvivorshipKeyDefinition_AllowManualResolution();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.DefaultSurvivorshipDefinitionImpl <em>Default Survivorship Definition</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.DefaultSurvivorshipDefinitionImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getDefaultSurvivorshipDefinition()
         * @generated
         */
        EClass DEFAULT_SURVIVORSHIP_DEFINITION = eINSTANCE.getDefaultSurvivorshipDefinition();

        /**
         * The meta object literal for the '<em><b>Data Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DEFAULT_SURVIVORSHIP_DEFINITION__DATA_TYPE = eINSTANCE.getDefaultSurvivorshipDefinition_DataType();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DEFAULT_SURVIVORSHIP_DEFINITION__FUNCTION = eINSTANCE.getDefaultSurvivorshipDefinition_Function();

        /**
         * The meta object literal for the '{@link org.talend.dataquality.rules.impl.ParticularDefaultSurvivorshipDefinitionsImpl <em>Particular Default Survivorship Definitions</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.talend.dataquality.rules.impl.ParticularDefaultSurvivorshipDefinitionsImpl
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getParticularDefaultSurvivorshipDefinitions()
         * @generated
         */
        EClass PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS = eINSTANCE.getParticularDefaultSurvivorshipDefinitions();

        /**
         * The meta object literal for the '<em>Td Expression List</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.List
         * @see org.talend.dataquality.rules.impl.RulesPackageImpl#getTdExpressionList()
         * @generated
         */
        EDataType TD_EXPRESSION_LIST = eINSTANCE.getTdExpressionList();

    }

} //RulesPackage
