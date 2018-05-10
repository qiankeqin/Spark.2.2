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

import com.codahale.metrics.{Gauge, MetricRegistry, Timer}

import org.apache.spark.metrics.source.Source

/**
  再创建DAGSchedulerSource，BlockManagerSource之前调用taskScheduler的postStartHook，其目的是为了等待backend就绪。
  DAGSchedulerSource，BlockManagerSource的过程类似于ExecutorSource，只不过

  DAGSchedulerSource的测量信息是
    stage.faileStages,satage.runningStages,stage.waitting-Stages,stage.allJobs,stage.activeJobs.

  BlockManagerSource
     测量信息是memory.maxMem_MB,memoery.remainingMem_MB,memory.memUsed_MB，memory.diskSpaceUsed_MB.
  */
private[scheduler] class DAGSchedulerSource(val dagScheduler: DAGScheduler)
    extends Source {
  override val metricRegistry = new MetricRegistry()
  override val sourceName = "DAGScheduler"

  metricRegistry.register(MetricRegistry.name("stage", "failedStages"), new Gauge[Int] {
    override def getValue: Int = dagScheduler.failedStages.size
  })

  metricRegistry.register(MetricRegistry.name("stage", "runningStages"), new Gauge[Int] {
    override def getValue: Int = dagScheduler.runningStages.size
  })

  metricRegistry.register(MetricRegistry.name("stage", "waitingStages"), new Gauge[Int] {
    override def getValue: Int = dagScheduler.waitingStages.size
  })

  metricRegistry.register(MetricRegistry.name("job", "allJobs"), new Gauge[Int] {
    override def getValue: Int = dagScheduler.numTotalJobs
  })

  metricRegistry.register(MetricRegistry.name("job", "activeJobs"), new Gauge[Int] {
    override def getValue: Int = dagScheduler.activeJobs.size
  })

  /** Timer that tracks the time to process messages in the DAGScheduler's event loop
    * 计时器记录在DAGScheduler的事件循环中处理消息的时间
    * */
  val messageProcessingTimer: Timer =
    metricRegistry.timer(MetricRegistry.name("messageProcessingTime"))
}
