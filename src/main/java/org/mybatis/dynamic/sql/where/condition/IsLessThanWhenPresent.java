/**
 *    Copyright 2016-2018 the original author or authors.
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
package org.mybatis.dynamic.sql.where.condition;

import java.util.function.Supplier;

public class IsLessThanWhenPresent<T> extends IsLessThan<T> {

    protected IsLessThanWhenPresent(Supplier<T> valueSupplier) {
        super(valueSupplier);
    }
    
    @Override
    public boolean shouldRender() {
        return value() != null;
    }

    public static <T> IsLessThanWhenPresent<T> of(Supplier<T> valueSupplier) {
        return new IsLessThanWhenPresent<>(valueSupplier);
    }
}
