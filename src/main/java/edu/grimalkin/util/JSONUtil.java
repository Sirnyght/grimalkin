package edu.grimalkin.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.*;
import edu.grimalkin.data.Library;

/**
 * Classe utilitaire pour la manipulation de fichiers JSON
 * La classe encapsule plusieurs méthodes permettant de manipuler les données d'un fichier JSON.
 * La classe utilise la librairie google Gson.
 * @see <a href="https://code.google.com/archive/p/json-simple/">JSON.simple</a>
 */
public class JSONUtil {
    private static final String FILENAME = "Library.json";
    private static final String LIBRARY_JSON = "Library.json";

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe.
     * @throws IllegalStateException
     * @see IllegalStateException
     */
    private JSONUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Méthode permettant d'écrire un fichier JSON depuis un objet Library.
     * @param library Bibliothèque à écrire
     * @throws IOException retourne une exception si le fichier n'a pas pu être écrit
     */
    public static void writeJSONFile(Library library) throws IOException {
        // Write JSON file
        Gson gson = new Gson();
        String json = gson.toJson(library);
        FileWriter writer = new FileWriter(FILENAME);
        writer.write(json);
        writer.close();
    }

    /**
     * Méthode permettant de lire un fichier JSON et de le convertir en objet Library.
     * @return Library
     * @throws IOException retourne une exception si le fichier n'a pas pu être lu
     */
    public static Library readJSONFile() throws IOException {
        // Read JSON file
        if (!new File(LIBRARY_JSON).exists()) {
            // Create file if it doesn't exist
            writeJSONFile(new Library());
            return new Library();
        }
        File file = new File(LIBRARY_JSON);
        String content = new String(Files.readAllBytes(file.toPath()));
        System.out.println(content);
        Gson gson = new Gson();
        Library library = gson.fromJson(content, Library.class);
        return library;
    }
}