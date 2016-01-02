package com.vedikunnel.tapestry.services;

import java.io.IOException;
import java.util.Properties;

import static com.vedikunnel.newStack.framework.configuration.NewStackConfiguration.FICHIER_CONFIGURATION;
import static com.vedikunnel.newStack.framework.configuration.NewStackConfiguration.VERSION_IMPORT_EXPORT;

/**
 * Classe utilisée pour initialiser la version Id'insito dans Tapestry.
 * Cette classe constitue un mécanisme d'indirection par rapport à "@Inject ProperyResolver resolver" au sein d'une signature de
 * méthode car cela provoque une erreur à l'initialisation sur la méthode "module.contributeApplicationDefaults()" en mode production.
 * Erreur du type : http://tapestryjava.blogspot.fr/2011/12/dissecting-tapestry-operation-trace.html
 */
public class NewStackProperties {

    private Properties properties;

    public NewStackProperties() throws IOException {
        properties = new Properties();
        properties.load(NewStackProperties.class.getResourceAsStream("/" + FICHIER_CONFIGURATION));
    }

    public String getVersion() {
        return properties.getProperty(VERSION_IMPORT_EXPORT, "Unversionned");
    }

}
