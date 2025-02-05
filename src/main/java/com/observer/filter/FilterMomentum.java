package com.observer.filter;

public class FilterMomentum extends Filter {
    private static final String QUERYNAME = "query.momentum";
    private static final String PARAMETERNAME = "query.momentum.parameter";

    public FilterMomentum() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = super.getProperties().getProperty(PARAMETERNAME).split(", ");
        super.title = "FilterMomentum";
        super.description = String.format(
                "Stocks which closing price above the SMA20 is more than %s%% and the trading value is more than %s.",
                parameterArray);
    }

    public String getQueryName() {
        return QUERYNAME;
    }

    public String getParameterName() {
        return QUERYNAME;
    }
}
