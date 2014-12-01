package org.dgl.manager;

public class LangManager extends ConfigurationManager {

    private static final String PROPERTIES_FILE_EXTENSION = ".properties";

    public LangManager(String country) throws Exception {
        super(country + PROPERTIES_FILE_EXTENSION);
    }

    public String translate(String attributeName) throws Exception {
        return get(attributeName);
    }
}
