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

/**
 * An immutable object representing a complex number and its operations.
 */
public class Complex {
    /**
     * The Complex value 1.
     */
    public static final Complex UNITY = new Complex(1, 0);
    private final double mRe;
    private final double mIm;

    /**
     * Creates an immutable complex number with the specified real and imaginary components.
     *
     * @param re the real part.
     * @param im the imaginary part.
     */
    public Complex(double re, double im) {
        mRe = re;
        mIm = im;
    }

    /**
     * Creates a new complex number from the phasor (modulus and argument),
     * where: modulus*e^(argument*i) = modulus*(cos(argument) + i*sin(argument))
     *
     * @param modulus the distance from the origin.
     * @param argument the angle around the origin in radians.
     * @return new complex number from the phasor.
     */
    public static Complex fromPhasor(double modulus, double argument) {
        if (modulus < 0) {
            throw new IllegalArgumentException(
                    "The modulus cannot be negative (" + modulus + ")");
        }

        return new Complex(modulus * Math.cos(argument),
                modulus * Math.sin(argument));
    }

    /**
     * Returns the real part of this complex number.
     *
     * @return the real part of this complex number.
     */
    public double re() {
        return mRe;
    }

    /**
     * Returns the imaginary part of this complex number.
     *
     * @return the imaginary part of this complex number.
     */
    public double im() {
        return mIm;
    }

    /**
     * Returns the modulus of this complex number, i.e., the distance from
     * the number to the complex origin.
     *
     * @return the modulus of this complex number.
     */
    public double modulus() {
        return Math.hypot(mRe, mIm);
    }

    /**
     * Returns the argument of this complex number, i.e., the counterclockwise
     * angle around the complex origin starting at the positive real axis.
     * Always returns a value between -pi and pi.
     *
     * @return Returns the argument of this complex number.
     */
    public double argument() {
        return Math.atan2(mIm, mRe);
    }

    /**
     * Adds two complex numbers.
     *
     * @param other the complex number to add.
     * @return the sum of this and the other complex number.
     */
    public Complex add(Complex other) {
        return new Complex(mRe + other.mRe, mIm + other.mIm);
    }

    /**
     * Subtracts two complex numbers.
     *
     * @param other the complex number to subtract.
     * @return the difference of this and the other complex number,
     * in other words, this minus other.
     */
    public Complex subtract(Complex other) {
        return new Complex(mRe - other.mRe, mIm - other.mIm);
    }

    /**
     * Negates this complex number.
     *
     * @return the negation of this number.
     */
    public Complex negate() {
        return new Complex(-mRe, -mIm);
    }

    /**
     * Computes the conjugate.
     *
     * @return the conjugate of this number (only imaginary part negated).
     */
    public Complex conjugate() {
        return new Complex(mRe, -mIm);
    }

    /**
     * Computes the reciprocal.
     *
     * @return the reciprocal of this number.
     */
    public Complex reciprocal() {
        return fromPhasor(1 / modulus(), -argument());
    }

    /**
     * Multiplies complex numbers.
     *
     * @param other the complex number to multiply by.
     * @return the product of this number and the other.
     */
    public Complex multiply(Complex other) {
        return new Complex(mRe*other.mRe - mIm*other.mIm,
                mRe*other.mIm + mIm*other.mRe);
    }

    /**
     * Multiplies a complex number with a scalar.
     *
     * @param other the scalar number to multiply by.
     * @return the product of this number and a scalar.
     */
    public Complex multiply(double other) {
        return new Complex(mRe*other, mIm*other);
    }

    /**
     * Divides complex numbers.
     *
     * @param other the complex number to divide by.
     * @return the quotient of this number and the other, in other
     * words, this divided by other.
     */
    public Complex divide(Complex other) {
        return multiply(other.reciprocal());
    }

    /**
     * Divides a complex number by a scalar.
     *
     * @param other the scalar number to divide by.
     * @return the quotient of this number and a scalar.
     */
    public Complex divide(double other) {
        return new Complex(mRe/other, mIm/other);
    }

    /**
     * Exponentiates a complex number.
     *
     * @return e raised to this complex number.
     */
    public Complex exp() {
        // e^(re + im*i)
        //     = e^re * e^im*i

        return fromPhasor(Math.exp(mRe), mIm);
    }

    /**
     * Computes the log of a complex number.
     *
     * @return the log of this complex number.
     */
    public Complex log() {
        return new Complex(Math.log(modulus()), argument());
    }

    /**
     * Computes the principal root.
     *
     * @param n the number of roots.
     * @return the principal Nth root of this number.
     */
    public Complex root(int n) {
        return pow(1.0 / n);
    }

    /**
     * Computes a power of a complex number.
     *
     * @param n the scalar to raise this complex number to.
     * @return this number to the Nth power.
     */
    public Complex pow(double n) {
        // x^y = e^(log (x^y))
        //     = e^(y*log x)
        return log().multiply(n).exp();
    }
}
