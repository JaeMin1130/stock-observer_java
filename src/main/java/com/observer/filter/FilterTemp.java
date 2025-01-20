package com.observer.filter;

public class FilterTemp extends Filter {
    private static final String QUERYNAME = "query.temp";

    public FilterTemp() {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.title = "FilterTemp";
    }

    public FilterTemp(String[] parameterArray) {
        super.query = super.getProperties().getProperty(QUERYNAME);
        super.parameterArray = parameterArray;
        super.title = "FilterTemp";
    }

    public String getQueryName() {
        return QUERYNAME;
    }

    @Override
    public void setDescription(String[] parameterArray) {
    }
}
