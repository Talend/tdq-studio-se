// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.datascience.common.schema

import org.talend.datascience.common.statistics.NumericalFieldStatistics
import org.talend.datascience.common.statistics.TextualFieldStatistics
import org.talend.datascience.common.statistics.FieldStatistics

trait SemanticField extends Field[Any] {
  var semanticName: String
  val sampleValues: Seq[String] = Seq()
  var numericalStatistics = new NumericalFieldStatistics
  var textualStatistics = new TextualFieldStatistics
  var suggestedType: (DataType[Any], Map[DataType[Any], (Long, FieldStatistics)]) //(data type name, type infer details)

}