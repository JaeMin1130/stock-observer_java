package com.observer.filter;

public class FilterValue extends Filter {
    private static final String QUERYNAME = "query.value";
    private static final String PARAMETERNAME = "query.value.parameter";

    public FilterValue() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = super.getProperties().getProperty(PARAMETERNAME).split(", ");
        super.title = "FilterValue";
        super.description = String.format(
                "Stocks which rate of change is less than %s%% and per is less than %s%% and pbr is less than %s%% in top %s marketcap.",
                parameterArray);
    }

    public String getQueryName() {
        return QUERYNAME;
    }

    public String getParameterName() {
        return QUERYNAME;
    }
}
