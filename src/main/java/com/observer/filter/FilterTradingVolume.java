package com.observer.filter;

public class FilterTradingVolume extends Filter {
    private static final String QUERYNAME = "query.tradingVolume";

    public FilterTradingVolume() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.title = "FilterTradingVolume";
    }

    public FilterTradingVolume(String[] parameterArray) {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = parameterArray;
        super.title = "FilterTradingVolume";
        super.description = String.format("Stocks which close increses %s%% over sma20 in top100 tradingvolume",
                parameterArray);
    }

    public String getQueryName() {
        return QUERYNAME;
    }

    @Override
    public void setDescription(String[] parameterArray) {
        super.description = String.format("Stocks which close increses %s%% over sma20 in top100 tradingvolume",
                parameterArray);
    }
}
