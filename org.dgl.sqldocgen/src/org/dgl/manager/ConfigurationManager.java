package org.dgl.manager;

public class ConfigurationManager {

    private java.util.Properties p;

    private final String DEFAULT_PROPERTIES_FILE = "application.properties";

    public ConfigurationManager() throws Exception {
        p = new java.util.Properties();
        p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE));
    }

    public ConfigurationManager(String propertiesFile) throws Exception {
        p = new java.util.Properties();
        p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile));
    }

    public java.lang.String get(String key) {
        String toret = "";
        toret = p.getProperty(key);
        return toret;
    }
}
