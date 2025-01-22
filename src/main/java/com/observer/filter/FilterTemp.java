package com.observer.filter;

public class FilterTemp extends Filter {
    private static final String QUERYNAME = "query.temp";
    private static final String PARAMETERNAME = "query.temp.parameter";

    public FilterTemp() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = super.getProperties().getProperty(PARAMETERNAME).split(", ");
        super.title = "FilterTemp";
        super.description = String.format(
                "Stocks which rate of change is %s%% under and pbr is %s%% under and per is %s%% under and caprank is between %s and %s",
                parameterArray);
    }

    public String getQueryName() {
        return QUERYNAME;
    }

    public String getParameterName() {
        return QUERYNAME;
    }
}
