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

package org.apache.spark.rdd

import scala.reflect.ClassTag

import org.apache.spark.{Partition, SparkContext, TaskContext}

/**
  * An RDD partition used to recover checkpointed data.
  *
  *   用于恢复的检查点数据的RDD分区。
  *
  */
private[spark] class CheckpointRDDPartition(val index: Int) extends Partition

/**
  * An RDD that recovers checkpointed data from storage.
  *
  *   一个从存储中恢复检查点的RDD
  *
  */
private[spark] abstract class CheckpointRDD[T: ClassTag](sc: SparkContext)
  extends RDD[T](sc, Nil) {

  // CheckpointRDD should not be checkpointed again
  // CheckpointRDD不应再被设置检查点
  override def doCheckpoint(): Unit = { }
  override def checkpoint(): Unit = { }
  override def localCheckpoint(): this.type = this

  // Note: There is a bug in MiMa that complains about `AbstractMethodProblem`s in the
  // base [[org.apache.spark.rdd.RDD]] class if we do not override the following methods.
  // scalastyle:off

  // 注：这里有一个BUG在MiMa中包含`AbstractMethodProblem`的问题在基类[[org.apache.spark.rdd.RDD]]
  // 中体现，如果我们不重写以下方法。scalastyle:off
  protected override def getPartitions: Array[Partition] = ???
  override def compute(p: Partition, tc: TaskContext): Iterator[T] = ???
  // scalastyle:on

}
