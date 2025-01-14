package com.observer.filter;

import static com.observer.util.FilePath.QUERY;

import java.util.Properties;

import com.observer.util.FileReader;

public class Filter {
    private static final Properties properties = FileReader.read(QUERY);
    protected String query;
    protected String[] parameterArray;
    protected String title;
    protected String description;

    public Properties getProperties(){
        return properties;
    }
    public String getQuery(){
        return query;
    }
    public String[] getParameterArray(){
        return parameterArray;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }

    public void setParameterArray(String[] parameterArray){
        this.parameterArray = parameterArray;
    }

    @Override
    public String toString(){
        return String.format("Filter[query: %s, title: %s, description: %s]", query, title, description);
    }
}
