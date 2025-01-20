package com.observer.filter;

public class FilterHugeDrop extends Filter {
    private static final String QUERYNAME = "query.hugedrop";

    public FilterHugeDrop() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.title = "FilterHugeDrop";
    }

    public FilterHugeDrop(String[] parameterArray) {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = parameterArray;
        super.title = "FilterHugeDrop";
        super.description = String.format(
                "Stocks which rate of change is between %s%% ~ %s%% in top%s marketcap",
                parameterArray);
    }

    public String getQueryName() {
        return QUERYNAME;
    }

    @Override
    public void setDescription(String[] parameterArray) {
        super.description = String.format(
                "Stocks which dividend is between %s%% ~ %s%% in top%s marketcap",
                parameterArray);
    }
}
