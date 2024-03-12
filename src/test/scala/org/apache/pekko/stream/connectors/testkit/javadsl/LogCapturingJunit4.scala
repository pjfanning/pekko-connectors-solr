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

package org.apache.pekko.stream.connectors.testkit.javadsl

import org.apache.pekko.stream.connectors.testkit.CapturingAppender

import scala.util.control.NonFatal
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.slf4j.LoggerFactory

/**
 * See https://pekko.apache.org/docs/pekko/current/typed/testing-async.html#silence-logging-output-from-tests
 *
 * JUnit `TestRule` to make log lines appear only when the test failed.
 *
 * Use this in test by adding a public field annotated with `@Rule`:
 * {{{
 *   @Rule public final LogCapturingJunit4 logCapturing = new LogCapturingJunit4();
 * }}}
 *
 * Requires Logback and configuration like the following the logback-test.xml:
 *
 * {{{
 *     <appender name="CapturingAppender" class="org.apache.pekko.actor.testkit.typed.internal.CapturingAppender" />
 *
 *     <logger name="org.apache.pekko.actor.testkit.typed.internal.CapturingAppenderDelegate" >
 *       <appender-ref ref="STDOUT"/>
 *     </logger>
 *
 *     <root level="DEBUG">
 *         <appender-ref ref="CapturingAppender"/>
 *     </root>
 * }}}
 */
final class LogCapturingJunit4 extends TestRule {
  // eager access of CapturingAppender to fail fast if misconfigured
  private val capturingAppender = CapturingAppender.get("")

  private val myLogger = LoggerFactory.getLogger(classOf[LogCapturingJunit4])

  override def apply(base: Statement, description: Description): Statement = {
    new Statement {
      override def evaluate(): Unit = {
        try {
          myLogger.info(s"Logging started for test [${description.getClassName}: ${description.getMethodName}]")
          base.evaluate()
          myLogger.info(
            s"Logging finished for test [${description.getClassName}: ${description.getMethodName}] that was successful")
        } catch {
          case NonFatal(e) =>
            println(
              s"--> [${Console.BLUE}${description.getClassName}: ${description.getMethodName}${Console.RESET}] " +
              s"Start of log messages of test that failed with ${e.getMessage}")
            capturingAppender.flush()
            println(
              s"<-- [${Console.BLUE}${description.getClassName}: ${description.getMethodName}${Console.RESET}] " +
              s"End of log messages of test that failed with ${e.getMessage}")
            throw e
        } finally {
          capturingAppender.clear()
        }
      }
    }
  }
}
