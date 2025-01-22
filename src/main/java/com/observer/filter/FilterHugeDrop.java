package com.observer.filter;

public class FilterHugeDrop extends Filter {
    private static final String QUERYNAME = "query.hugedrop";
    private static final String PARAMETERNAME = "query.hugedrop.parameter";

    public FilterHugeDrop() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = super.getProperties().getProperty(PARAMETERNAME).split(", ");
        super.title = "FilterHugeDrop";
        super.description = String.format(
                "Stocks which rate of change is %s%% under in top%s marketcap",
                parameterArray);
    }

    public String getQueryName() {
        return QUERYNAME;
    }

    public String getParameterName() {
        return QUERYNAME;
    }
}
