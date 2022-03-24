/*
 * Copyright 2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.starter

import org.junit.jupiter.api.Test
import org.openrewrite.Recipe
import org.openrewrite.java.JavaParser
import org.openrewrite.java.JavaRecipeTest

class NoGuavaListsNewArrayListTest: JavaRecipeTest {
    override val parser: JavaParser
        get() = JavaParser.fromJavaVersion()
            .logCompilationWarningsAndErrors(true)
            .classpath("guava")
            .build()

    override val recipe: Recipe
        get() = NoGuavaListsNewArrayList()

    @Test
    fun replaceWithNewArrayList() = assertChanged(
        before = """
            import com.google.common.collect.*;
            
            import java.util.List;
            
            class Test {
                List<Integer> cardinalsWorldSeries = Lists.newArrayList();
            }
        """,
        after = """
            import java.util.ArrayList;
            import java.util.List;
            
            class Test {
                List<Integer> cardinalsWorldSeries = new ArrayList<>();
            }
        """
    )

    @Test
    fun replaceWithNewArrayListIterable() = assertChanged(
        before = """
            import com.google.common.collect.*;
            
            import java.util.Collections;
            import java.util.List;
            
            class Test {
                List<Integer> l = Collections.emptyList();
                List<Integer> cardinalsWorldSeries = Lists.newArrayList(l);
            }
        """,
        after = """
            import java.util.ArrayList;
            import java.util.Collections;
            import java.util.List;
            
            class Test {
                List<Integer> l = Collections.emptyList();
                List<Integer> cardinalsWorldSeries = new ArrayList<>(l);
            }
        """
    )

    @Test
    fun replaceWithNewArrayListWithCapacity() = assertChanged(
        before = """
            import com.google.common.collect.*;
            
            import java.util.ArrayList;
            import java.util.List;
            
            class Test {
                List<Integer> cardinalsWorldSeries = Lists.newArrayListWithCapacity(2);
            }
        """,
        after = """
            import java.util.ArrayList;
            import java.util.List;
            
            class Test {
                List<Integer> cardinalsWorldSeries = new ArrayList<>(2);
            }
        """
    )
}
