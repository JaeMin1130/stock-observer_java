package com.observer.filter;

public class FilterDividendPayoutRatio extends Filter {
    private static final String QUERYNAME = "query.dividendPayoutRatio";

    public FilterDividendPayoutRatio() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.title = "FilterTradingVolume";
        super.description = String.format("Stocks which close increses %s%% over sma20 in top100 tradingvolume",
                parameterArray);
    }

    public FilterDividendPayoutRatio(String[] parameterArray) {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = parameterArray;
        super.title = "FilterDividendPayoutRatio";
        super.description = String.format(
                "Stocks which dividendPayoutRatio is between %s%% ~ %s%% and dy is %s%% over in top%s marketcap",
                parameterArray);
    }

    public String getQueryName(){
        return QUERYNAME;
    }
}
