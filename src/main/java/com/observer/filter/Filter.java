package com.observer.filter;

import static com.observer.file.FilePath.QUERY;

import java.util.Properties;

import com.observer.file.FileReader;

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

    @Override
    public String toString(){
        return String.format("Filter[query: %s, title: %s, description: %s]", query, title, description);
    }
}
