package com.observer.filter;

public class FilterDividend extends Filter {
    private static final String QUERYNAME = "query.dividend";

    public FilterDividend() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.title = "FilterTradingVolume";
    }

    public FilterDividend(String[] parameterArray) {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = parameterArray;
        super.title = "FilterDividend";
        super.description = String.format(
                "Stocks which dividend is between %s%% ~ %s%% and dy is %s%% over in top%s marketcap",
                parameterArray);
    }

    public String getQueryName() {
        return QUERYNAME;
    }

    @Override
    public void setDescription(String[] parameterArray) {
        super.description = String.format(
                "Stocks which dividend is between %s%% ~ %s%% and dy is %s%% over in top%s marketcap",
                parameterArray);
    }
}
