/*
 * Copyright 2014 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.linkedin.gradle.hadoopdsl;

import com.linkedin.gradle.hadoopdsl.job.CommandJob;
import com.linkedin.gradle.hadoopdsl.job.HadoopJavaJob;
import com.linkedin.gradle.hadoopdsl.job.HiveJob;
import com.linkedin.gradle.hadoopdsl.job.JavaJob;
import com.linkedin.gradle.hadoopdsl.job.JavaProcessJob;
import com.linkedin.gradle.hadoopdsl.job.Job;
import com.linkedin.gradle.hadoopdsl.job.KafkaPushJob;
import com.linkedin.gradle.hadoopdsl.job.NoOpJob;
import com.linkedin.gradle.hadoopdsl.job.PigJob;
import com.linkedin.gradle.hadoopdsl.job.VoldemortBuildPushJob;

/**
 * Base implementation of Visitor to make it easy to implement visitor subclasses. By default, the
 * multi-method overloads will simply call the overload for the base type. This will make it easy
 * for subclasses that want to only override the overload for the base type.
 */
abstract class BaseVisitor implements Visitor {
  /**
   * Keep track of the top-level extension.
   */
  HadoopDslExtension extension;

  /**
   * Keeps track of the current parent scope for the DSL element being built.
   */
  NamedScope parentScope;

  /**
   * Keeps track of the current parent scope name for the DSL element being built.
   */
  String parentScopeName;

  /**
   * Helper method for DSL elements that subclass BaseNamedScopeContainer.
   *
   * @param container The DSL element subclassing BaseNamedScopeContainer
   */
  void visitScopeContainer(BaseNamedScopeContainer container) {
    // Save the last scope information
    NamedScope oldParentScope = this.parentScope;
    String oldParentScopeName = this.parentScopeName;

    // Set the new parent scope
    this.parentScopeName = container.scope.levelName;
    this.parentScope = container.scope;

    // Visit each job
    container.jobs.each() { Job job ->
      visitJob(job);
    }

    // Visit each workflow
    container.workflows.each() { Workflow workflow ->
      visitWorkflow(workflow);
    }

    // Visit each property set object
    container.propertySets.each() { PropertySet propertySet ->
      visitPropertySet(propertySet);
    }

    // Visit each properties object
    container.properties.each() { Properties props ->
      visitProperties(props);
    }

    // Restore the last parent scope
    this.parentScope = oldParentScope;
    this.parentScopeName = oldParentScopeName;
  }

  @Override
  void visitPlugin(HadoopDslPlugin plugin) {
    visitScopeContainer(plugin);
    visitExtension(plugin.extension);
  }

  @Override
  void visitExtension(HadoopDslExtension hadoop) {
    // Save the extension and visit its nested DSL elements
    this.extension = hadoop;
    visitScopeContainer(hadoop);
  }

  @Override
  void visitProperties(Properties props) {
  }

  @Override
  void visitPropertySet(PropertySet propertySet) {
  }

  @Override
  void visitWorkflow(Workflow workflow) {
    visitScopeContainer(workflow);
  }

  @Override
  void visitJob(Job job) {
  }

  @Override
  void visitJob(CommandJob job) {
    visitJob((Job)job);
  }

  @Override
  void visitJob(HadoopJavaJob job) {
    visitJob((Job)job);
  }

  @Override
  void visitJob(HiveJob job) {
    visitJob((Job)job);
  }

  @Override
  void visitJob(JavaJob job) {
    visitJob((Job)job);
  }

  @Override
  void visitJob(JavaProcessJob job) {
    visitJob((Job)job);
  }

  @Override
  void visitJob(KafkaPushJob job) {
    visitJob((Job)job);
  }

  @Override
  void visitJob(NoOpJob job) {
    visitJob((Job)job);
  }

  @Override
  void visitJob(PigJob job) {
    visitJob((Job)job);
  }

  @Override
  void visitJob(VoldemortBuildPushJob job) {
    visitJob((Job)job);
  }
}
