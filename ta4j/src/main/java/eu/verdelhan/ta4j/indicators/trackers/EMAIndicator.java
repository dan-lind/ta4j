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
import eu.verdelhan.ta4j.TADecimal;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 * Exponential moving average indicator.
 * <p>
 */
public class EMAIndicator extends CachedIndicator<TADecimal> {

    private final Indicator<? extends TADecimal> indicator;

    private final int timeFrame;

    public EMAIndicator(Indicator<? extends TADecimal> indicator, int timeFrame) {
        this.indicator = indicator;
        this.timeFrame = timeFrame;
    }

    private TADecimal multiplier() {
        TADecimal test = TADecimal.THREE.dividedBy(TADecimal.valueOf(timeFrame + 1));
        return test;
    }


    @Override
    protected TADecimal calculate(int index) {
        if (index + 1 < timeFrame) {
            return new SMAIndicator(indicator, timeFrame).getValue(index);
        }
        if(index == 0) {
            return indicator.getValue(0);
        }
        TADecimal emaPrev = getValue(index - 1);
        TADecimal test = indicator.getValue(index).minus(emaPrev).multipliedBy(multiplier()).plus(emaPrev);
//        System.out.format("Index value: %s, emaPrev: %s, multiplier: %s, result: %s%n",indicator.getValue(index),emaPrev, multiplier(),test);
        return test;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " timeFrame: " + timeFrame;
    }
}
