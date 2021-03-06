/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.slicer

import com.intellij.slicer.SliceRootNode
import org.jetbrains.kotlin.test.KotlinTestUtils
import java.io.File

abstract class AbstractSlicerLeafGroupingTest : AbstractSlicerTest() {
    override fun doTest(path: String, sliceProvider: KotlinSliceProvider, rootNode: SliceRootNode) {
        val treeStructure = SliceTreeStructure(rootNode)
        val analyzer = sliceProvider.leafAnalyzer
        val possibleElementsByNode = analyzer.createMap()
        val leafExpressions = analyzer.calcLeafExpressions(rootNode, treeStructure, possibleElementsByNode)
        val newRootNode = analyzer.createTreeGroupedByValues(leafExpressions, rootNode, possibleElementsByNode)
        val renderedForest = buildString {
            for (groupRootNode in newRootNode.children) {
                append(buildTreeRepresentation(groupRootNode))
                append("\n")
            }
        }
        KotlinTestUtils.assertEqualsToFile(File(path.replace(".kt", ".leafGroups.txt")), renderedForest)
    }
}