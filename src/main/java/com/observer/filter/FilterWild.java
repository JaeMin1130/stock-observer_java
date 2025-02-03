package com.observer.filter;

public class FilterWild extends Filter {
    private static final String QUERYNAME = "query.wild";
    private static final String PARAMETERNAME = "query.wild.parameter";

    public FilterWild() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = super.getProperties().getProperty(PARAMETERNAME).split(", ");
        super.title = "FilterWild";
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
