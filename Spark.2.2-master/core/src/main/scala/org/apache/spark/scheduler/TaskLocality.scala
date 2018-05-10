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

package org.apache.spark.scheduler

import org.apache.spark.annotation.DeveloperApi

@DeveloperApi
object TaskLocality extends Enumeration {
  // Process local is expected to be used ONLY within TaskSetManager for now.
  val PROCESS_LOCAL, NODE_LOCAL, NO_PREF, RACK_LOCAL, ANY = Value

  type TaskLocality = Value

  def isAllowed(constraint: TaskLocality, condition: TaskLocality): Boolean = {
    // condition：条件 小于等于  constraint：约束
    condition <= constraint
  }
}

/**
  * 什么是NO_PREF？
  *   当Driver应用程序刚刚启动，Driver分配获得的Executor很可能还没有初始化完毕。所以会有一部分任务的本地化级别被设置为NO_PREF,
  * 如果是ShuffleRDD，其本地行始终为NO_PREF,对于这两种本地化级别是NO_PREF的情况，在任务分配时会被优先分配到非本地节点执行，
  * 达到一定的优化效果。
  *
  * PROCESS_LOCAL: 数据在同一个 JVM 中，即同一个 executor 上。这是最佳数据 locality。
  *
  * NODE_LOCAL: 数据在同一个节点上。比如数据在同一个节点的另一个 executor上；或在 HDFS 上，恰好有 block 在同一个节点上。速度比
  *             PROCESS_LOCAL 稍慢，因为数据需要在不同进程之间传递或从文件中读取
  *
  * NO_PREF: 数据从哪里访问都一样快，不需要位置优先
  *
  * RACK_LOCAL: 数据在同一机架的不同节点上。需要通过网络传输数据及文件 IO，比 NODE_LOCAL 慢
  *
  * ANY: 数据在非同一机架的网络上，速度最慢
  *
  */

