/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.df.node;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.mahout.df.data.Instance;

/**
 * Represents an abstract node of a decision tree
 */
public abstract class Node implements Writable {

  protected enum Type {
    MOCKLEAF, LEAF, NUMERICAL, CATEGORICAL
  }

  /**
   * predicts the label for the instance
   * 
   * @param instance
   * @return -1 if the label cannot be predicted
   */
  public abstract int classify(Instance instance);

  /**
   * returns the total number of nodes of the tree
   * 
   * @return
   */
  public abstract long nbNodes();

  /**
   * returns the maximum depth of the tree
   * 
   * @return
   */
  public abstract long maxDepth();

  protected abstract Type getType();

  public static Node read(DataInput in) throws IOException {
    Type type = Type.values()[in.readInt()];
    Node node;

    switch (type) {
      case MOCKLEAF:
        node = new MockLeaf();
        break;
      case LEAF:
        node = new Leaf();
        break;
      case NUMERICAL:
        node = new NumericalNode();
        break;
      case CATEGORICAL:
        node = new CategoricalNode();
        break;
      default:
        throw new IllegalStateException(
            "This implementation is not currently supported");
    }

    node.readFields(in);

    return node;
  }

  @Override
  public final String toString() {
    return getType() + ":" + getString() + ';';
  }

  protected abstract String getString();

  @Override
  public final void write(DataOutput out) throws IOException {
    out.writeInt(getType().ordinal());
    writeNode(out);
  }

  protected abstract void writeNode(DataOutput out) throws IOException;

}
