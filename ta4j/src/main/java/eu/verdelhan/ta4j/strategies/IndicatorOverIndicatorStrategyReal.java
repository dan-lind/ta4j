/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Marc de Verdelhan & respective authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j.strategies;

import eu.verdelhan.ta4j.Indicator;
import org.jscience.mathematics.number.Real;

/**
 * Indicator over indicator strategy.
 * <p>
 * Enter: when the value of the first {@link eu.verdelhan.ta4j.Indicator indicator} is strictly greater than the value of the second one<br>
 * Exit: when the value of the first {@link eu.verdelhan.ta4j.Indicator indicator} is strictly lesser than the value of the second one
 */
public class IndicatorOverIndicatorStrategyReal extends AbstractStrategy {

    /** The first indicator */
    private Indicator<Real> first;
    /** The second indicator */
    private Indicator<Real> second;

    /**
     * Constructor.
     * @param first the first indicator
     * @param second the second indicator
     */
    public IndicatorOverIndicatorStrategyReal(Indicator<Real> first, Indicator<Real> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean shouldEnter(int index) {
        return first.getValue(index).isGreaterThan(second.getValue(index));
    }

    @Override
    public boolean shouldExit(int index) {
        return first.getValue(index).isLessThan(second.getValue(index));
    }

    @Override
    public String toString() {
        return String.format("%s : %s over %s", this.getClass().getSimpleName(), first, second);
    }
}
