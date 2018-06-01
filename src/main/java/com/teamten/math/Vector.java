/*
 *
 *    Copyright 2016 Lawrence Kesteloot
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

// Copyright 2011 Lawrence Kesteloot

package com.teamten.math;

import java.util.Arrays;

/**
 * Immutable vector class. Represents each element as a double.
 */
public class Vector {
    /**
     * The X axis vector (1,0,0).
     */
    public static final Vector X = make(1, 0, 0);
    /**
     * The Y axis vector (0,1,0).
     */
    public static final Vector Y = make(0, 1, 0);
    /**
     * The Z axis vector (0,0,1).
     */
    public static final Vector Z = make(0, 0, 1);
    private final double[] mValues;
    private double mLength = -1;

    /**
     * Package-private constructor that doesn't copy its input array.
     */
    Vector(double[] values) {
        mValues = values;
    }

    /**
     * Public factory that copies its input array.
     *
     * @param values the values of the new vector.
     * @return a Vector of the passed-in values.
     */
    public static Vector make(double ... values) {
        double[] copiedValues = Arrays.copyOf(values, values.length);

        return new Vector(copiedValues);
    }

    /**
     * Gets an element.
     *
     * @param index the index (zero-based) to look up.
     * @return the element at index.
     */
    public double get(int index) {
        return mValues[index];
    }

    /**
     * Replaces one item of the vector. Does not change this object.
     *
     * @param index the index to replace.
     * @param value the new value at index.
     * @return a new vector with the specified element replaced.
     */
    public Vector with(int index, double value) {
        double[] copiedValues = Arrays.copyOf(mValues, mValues.length);
        copiedValues[index] = value;

        return new Vector(copiedValues);
    }

    /**
     * Get the number of elements.
     *
     * @return the number of elements.
     */
    public int getSize() {
        return mValues.length;
    }

    /**
     * Negates this vector.
     *
     * @return the negated vector (-v) of this.
     */
    public Vector negate() {
        double[] newValues = new double[mValues.length];

        for (int i = 0; i < mValues.length; i++) {
            newValues[i] = -mValues[i];
        }

        return new Vector(newValues);
    }

    /**
     * Adds two vectors.
     *
     * @param other the vector to add.
     * @return the sum of this and other.
     * @throws IllegalArgumentException if the vectors don't have the same length.
     */
    public Vector add(Vector other) {
        assertSameLength(other);

        double[] sum = new double[mValues.length];

        for (int i = 0; i < mValues.length; i++) {
            sum[i] = mValues[i] + other.mValues[i];
        }

        return new Vector(sum);
    }

    /**
     * Subtracts two vectors.
     *
     * @param other the vector to subtract.
     * @return the difference of this and other (this minus other).
     * @throws IllegalArgumentException if the vectors don't have the same length.
     */
    public Vector subtract(Vector other) {
        assertSameLength(other);

        double[] difference = new double[mValues.length];

        for (int i = 0; i < mValues.length; i++) {
            difference[i] = mValues[i] - other.mValues[i];
        }

        return new Vector(difference);
    }

    /**
     * Computes a dot product.
     *
     * @param other the vector to dot with.
     * @return the dot product between this and other.
     * @throws IllegalArgumentException if the vectors don't have the same length.
     */
    public double dot(Vector other) {
        assertSameLength(other);

        double dot = 0;

        for (int i = 0; i < mValues.length; i++) {
            dot += mValues[i] * other.mValues[i];
        }

        return dot;
    }

    /**
     * Computes a cross product. The two vectors must be of length 3.
     *
     * @param other the vector to cross with.
     * @return the cross product between this and other, following the
     * right-hand rule.  
     * @throws IllegalArgumentException if the vectors don't have length 3.
     */
    public Vector cross(Vector other) {
        assertSameLength(other);
        if (mValues.length != 3) {
            throw new IllegalArgumentException(
                    "Can only compute cross product of 3-vectors");
        }

        return new Vector(new double[] {
            mValues[1]*other.mValues[2] - mValues[2]*other.mValues[1],
            mValues[2]*other.mValues[0] - mValues[0]*other.mValues[2],
            mValues[0]*other.mValues[1] - mValues[1]*other.mValues[0]
        });
    }

    /**
     * Multiplies a vector and a scalar.
     *
     * @param constant the scalar to multiply by.
     * @return the product between this and a scalar.
     */
    public Vector multiply(double constant) {
        double[] product = new double[mValues.length];

        for (int i = 0; i < mValues.length; i++) {
            product[i] = mValues[i] * constant;
        }

        return new Vector(product);
    }

    /**
     * Normalizes a vector.
     *
     * @return a normalized (length 1) version of this vector.
     * @throws IllegalArgumentException if the vector has length 0.
     */
    public Vector normalize() {
        double length = length();
        if (length == 0.0) {
            throw new IllegalArgumentException(
                    "Can't normalize a zero vector");
        }

        // Easy case.
        if (length == 1.0) {
            return this;
        }

        return multiply(1 / length);
    }

    /**
     * Gets the length of the vector (square root of the sum of the squares of
     * the elements).
     *
     * @return the length of this vector.
     */
    public double length() {
        // Cache the length.
        if (mLength == -1) {
            mLength = Math.sqrt(dot(this));
        }

        return mLength;
    }

    /**
     * Throws an IllegalArgumentException if the other vector isn't the same
     * length as this one.
     */
    private void assertSameLength(Vector other) {
        if (mValues.length != other.mValues.length) {
            throw new IllegalArgumentException(
                    "Vectors must be the same length ("
                    + mValues.length + " vs. " + other.mValues.length + ")");
        }
    }

    /**
     * Returns a string like "[1,2,3]".
     */
    @Override // Object
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append('[');
        for (int i = 0; i < mValues.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(mValues[i]);
        }
        builder.append(']');

        return builder.toString();
    }
}
