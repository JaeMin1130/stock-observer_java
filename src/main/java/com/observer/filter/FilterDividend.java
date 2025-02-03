package com.observer.filter;

public class FilterDividend extends Filter {
    private static final String QUERYNAME = "query.dividend";
    private static final String PARAMETERNAME = "query.dividend.parameter";

    public FilterDividend() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = super.getProperties().getProperty(PARAMETERNAME).split(", ");
        super.title = "FilterDividend";
        super.description = String.format(
                "Stocks which dividend is between %s%% ~ %s%% and dy is more than %s%% and rate of change is less than %s%% in top %s marketcap.",
                parameterArray);
    }

    public String getQueryName() {
        return QUERYNAME;
    }

    public String getParameterName() {
        return QUERYNAME;
    }
}
