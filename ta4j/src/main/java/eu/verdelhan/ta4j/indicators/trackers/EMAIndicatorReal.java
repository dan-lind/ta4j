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
package eu.verdelhan.ta4j.indicators.trackers;

import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.CachedIndicator;
import org.jscience.mathematics.number.Real;

/**
 * Exponential moving average indicator.
 * <p>
 */
public class EMAIndicatorReal extends CachedIndicator<Real> {

    private final Indicator<? extends Real> indicator;

    private final int timeFrame;

    public EMAIndicatorReal(Indicator<? extends Real> indicator, int timeFrame) {
        this.indicator = indicator;
        this.timeFrame = timeFrame;
    }

    private Real multiplier() {
        Real test = Real.valueOf(2).divide(Real.valueOf(timeFrame + 1));
        return test;
    }


    @Override
    protected Real calculate(int index) {
        if (index + 1 < timeFrame) {
            return new SMAIndicatorReal(indicator, timeFrame).getValue(index);
        }
        if(index == 0) {
            return indicator.getValue(0);
        }
        Real emaPrev = getValue(index - 1);
<<<<<<< HEAD
        System.out.println(emaPrev);
        return indicator.getValue(index).minus(emaPrev).times(multiplier()).plus(emaPrev);
=======
        Real test = indicator.getValue(index).minus(emaPrev).times(multiplier()).plus(emaPrev);
        System.out.println(test);
        return test;
>>>>>>> 64ae5163fcd05ce4efa0e6b646809f69d759e67f
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " timeFrame: " + timeFrame;
    }
}
