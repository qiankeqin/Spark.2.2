/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.types

import org.json4s.JsonAST.JValue
import org.json4s.JsonDSL._

import org.apache.spark.annotation.InterfaceStability

/**
 * A field inside a StructType. 结构内的一个字段。
 * @param name The name of this field.          这个字段的名称。
 * @param dataType The data type of this field. 这个字段的数据
 * @param nullable Indicates if values of this field can be `null` values.    表示该字段的值是否为“空”值。
 * @param metadata The metadata of this field. The metadata should be preserved during
 *                 transformation if the content of the column is not modified, e.g, in selection.
  *                 该字段的元数据。如果不修改列的内容，则在转换过程中保存元数据。g,在选择。
 *
 * @since 1.3.0
 */
@InterfaceStability.Stable
case class StructField(
    name: String,
    dataType: DataType,
    nullable: Boolean = true,
    metadata: Metadata = Metadata.empty) {

  /** No-arg constructor for kryo.  kryo的无参数构造函数。 */
  protected def this() = this(null, null)

  private[sql] def buildFormattedString(prefix: String, builder: StringBuilder): Unit = {
    builder.append(s"$prefix-- $name: ${dataType.typeName} (nullable = $nullable)\n")
    DataType.buildFormattedString(dataType, s"$prefix    |", builder)
  }

  // override the default toString to be compatible with legacy parquet files.
  override def toString: String = s"StructField($name,$dataType,$nullable)"

  private[sql] def jsonValue: JValue = {
    ("name" -> name) ~
      ("type" -> dataType.jsonValue) ~
      ("nullable" -> nullable) ~
      ("metadata" -> metadata.jsonValue)
  }

  /**
   * Updates the StructField with a new comment value.
    * 用新的注释值更新StructField。
   */
  def withComment(comment: String): StructField = {
    val newMetadata = new MetadataBuilder()
      .withMetadata(metadata)
      .putString("comment", comment)
      .build()
    copy(metadata = newMetadata)
  }

  /**
   * Return the comment of this StructField.
    * 返回该结构字段的注释。
   */
  def getComment(): Option[String] = {
    if (metadata.contains("comment")) Option(metadata.getString("comment")) else None
  }
}
