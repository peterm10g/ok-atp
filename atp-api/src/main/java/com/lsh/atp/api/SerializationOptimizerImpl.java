/**
 * Copyright 1999-2014 dangdang.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lsh.atp.api;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.lsh.atp.api.model.Hold.HoldRequest;
import com.lsh.atp.api.model.Hold.HoldResponse;
import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.api.model.baseVo.PreHold;
import com.lsh.atp.api.model.baseVo.SkuVo;
import com.lsh.atp.api.model.demo.User;
import com.lsh.atp.api.model.inventory.QueryInventory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 需要加速序列化的类列表
 */
public class SerializationOptimizerImpl implements SerializationOptimizer {

    public Collection<Class> getSerializableClasses() {
        List<Class> classList = new LinkedList<Class>();
        classList.add(User.class);
        classList.add(QueryInventory.class);
        classList.add(PreHold.class);
        classList.add(SkuVo.class);
        classList.add(Item.class);
        classList.add(HoldRequest.class);
        classList.add(HoldResponse.class);
        return classList;
    }

}
