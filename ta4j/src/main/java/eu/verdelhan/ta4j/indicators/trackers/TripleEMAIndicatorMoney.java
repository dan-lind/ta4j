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
import eu.verdelhan.ta4j.money.Money;
import eu.verdelhan.ta4j.money.MoneyFactory;

/**
 * Triple exponential moving average indicator.
 * <p>
 * a.k.a TRIX
 */
public class TripleEMAIndicatorMoney extends CachedIndicator<Money> {

    private final int timeFrame;

    private final EMAIndicatorMoney ema;

    public TripleEMAIndicatorMoney(Indicator<? extends Money> indicator, int timeFrame) {
        this.timeFrame = timeFrame;
        this.ema = new EMAIndicatorMoney(indicator, timeFrame);
    }

    @Override
    protected Money calculate(int index) {
        EMAIndicatorMoney emaEma = new EMAIndicatorMoney(ema, timeFrame);
        EMAIndicatorMoney emaEmaEma = new EMAIndicatorMoney(emaEma, timeFrame);
        return MoneyFactory.fromDouble(3).multiply(ema.getValue(index).subtract(emaEma.getValue(index)).toDouble()).add(emaEmaEma.getValue(index));

    }
}
