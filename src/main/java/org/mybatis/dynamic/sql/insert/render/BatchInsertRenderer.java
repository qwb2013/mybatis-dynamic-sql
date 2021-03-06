/**
 *    Copyright 2016-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.dynamic.sql.insert.render;

import java.util.Objects;
import java.util.function.Function;

import org.mybatis.dynamic.sql.insert.BatchInsertModel;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.util.InsertMapping;

public class BatchInsertRenderer<T> {

    private BatchInsertModel<T> model;
    private RenderingStrategy renderingStrategy;
    
    private BatchInsertRenderer(Builder<T> builder) {
        model = Objects.requireNonNull(builder.model);
        renderingStrategy = Objects.requireNonNull(builder.renderingStrategy);
    }
    
    public BatchInsert<T> render() {
        ValuePhraseVisitor visitor = new ValuePhraseVisitor(renderingStrategy);
        FieldAndValueCollector<T> collector = model.mapColumnMappings(toFieldAndValue(visitor))
                .collect(FieldAndValueCollector.collect());
        return BatchInsert.withRecords(model.records())
                .withTableName(model.table().name())
                .withColumnsPhrase(collector.columnsPhrase())
                .withValuesPhrase(collector.valuesPhrase())
                .build();
    }
    
    private Function<InsertMapping, FieldAndValue> toFieldAndValue(ValuePhraseVisitor visitor) {
        return insertMapping -> toFieldAndValue(visitor, insertMapping);
    }
    
    private FieldAndValue toFieldAndValue(ValuePhraseVisitor visitor, InsertMapping insertMapping) {
        return insertMapping.accept(visitor);
    }
    
    public static <T> Builder<T> withBatchInsertModel(BatchInsertModel<T> model) {
        return new Builder<T>().withBatchInsertModel(model);
    }
    
    public static class Builder<T> {
        private BatchInsertModel<T> model;
        private RenderingStrategy renderingStrategy;
        
        public Builder<T> withBatchInsertModel(BatchInsertModel<T> model) {
            this.model = model;
            return this;
        }
        
        public Builder<T> withRenderingStrategy(RenderingStrategy renderingStrategy) {
            this.renderingStrategy = renderingStrategy;
            return this;
        }
        
        public BatchInsertRenderer<T> build() {
            return new BatchInsertRenderer<>(this);
        }
    }
}
