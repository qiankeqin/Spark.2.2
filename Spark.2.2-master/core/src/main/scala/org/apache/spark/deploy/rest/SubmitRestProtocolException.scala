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

package org.apache.spark.deploy.rest

/**
  * An exception thrown in the REST application submission protocol.
  * 在REST应用程序提交协议中抛出的异常。
  */
private[rest] class SubmitRestProtocolException(message: String, cause: Throwable = null)
  extends Exception(message, cause)

/**
  * An exception thrown if a field is missing from a [[SubmitRestProtocolMessage]].
  * 如果[[SubmitRestProtocolMessage]]是缺少一个字段抛出一个异常。
  */
private[rest] class SubmitRestMissingFieldException(message: String)
  extends SubmitRestProtocolException(message)

/**
  * An exception thrown if the REST client cannot reach the REST server.
  * 如果REST客户端无法到达REST服务器，则抛出异常。
  */
private[deploy] class SubmitRestConnectionException(message: String, cause: Throwable)
  extends SubmitRestProtocolException(message, cause)
