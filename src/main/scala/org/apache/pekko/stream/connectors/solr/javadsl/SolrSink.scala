/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * license agreements; and to You under the Apache License, version 2.0:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * This file is part of the Apache Pekko project, which was derived from Akka.
 */

/*
 * Copyright (C) since 2016 Lightbend Inc. <https://www.lightbend.com>
 */

package org.apache.pekko.stream.connectors.solr.javadsl

import java.util.concurrent.CompletionStage
import java.util.function.Function

import org.apache.pekko
import pekko.stream.connectors.solr.{ SolrUpdateSettings, WriteMessage, WriteResult }
import pekko.stream.javadsl
import pekko.stream.javadsl.Sink
import pekko.{ Done, NotUsed }
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.common.SolrInputDocument
import java.util.{ List => JavaList }

/**
 * Java API
 */
object SolrSink {

  /**
   * Write `SolrInputDocument`s to Solr.
   */
  def documents(
      collection: String,
      settings: SolrUpdateSettings,
      client: SolrClient): javadsl.Sink[JavaList[WriteMessage[SolrInputDocument, NotUsed]], CompletionStage[Done]] =
    SolrFlow
      .documents(collection, settings, client)
      .toMat(javadsl.Sink.ignore[java.util.List[WriteResult[SolrInputDocument, NotUsed]]](),
        javadsl.Keep.right[NotUsed, CompletionStage[Done]])

  /**
   * Write Java bean stream elements to Solr.
   * The stream element classes must be annotated for use with [[org.apache.solr.client.solrj.beans.DocumentObjectBinder]] for conversion.
   */
  def beans[T](
      collection: String,
      settings: SolrUpdateSettings,
      client: SolrClient,
      clazz: Class[T]): Sink[JavaList[WriteMessage[T, NotUsed]], CompletionStage[Done]] =
    SolrFlow
      .beans[T](collection, settings, client, clazz)
      .toMat(javadsl.Sink.ignore[java.util.List[WriteResult[T, NotUsed]]](),
        javadsl.Keep.right[NotUsed, CompletionStage[Done]])

  /**
   * Write stream elements to Solr.
   *
   * @param binder a conversion function to create `SolrInputDocument`s of the stream elements
   */
  def typeds[T](
      collection: String,
      settings: SolrUpdateSettings,
      binder: Function[T, SolrInputDocument],
      client: SolrClient,
      clazz: Class[T]): javadsl.Sink[JavaList[WriteMessage[T, NotUsed]], CompletionStage[Done]] =
    SolrFlow
      .typeds[T](collection, settings, binder, client, clazz)
      .toMat(javadsl.Sink.ignore[java.util.List[WriteResult[T, NotUsed]]](),
        javadsl.Keep.right[NotUsed, CompletionStage[Done]])
}
