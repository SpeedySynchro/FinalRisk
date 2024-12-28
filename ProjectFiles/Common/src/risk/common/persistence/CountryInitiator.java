package risk.common.persistence;

import risk.common.FileUtils;
import risk.common.entities.Continent;
import risk.common.entities.Country;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Initializes and manages the loading and organization of countries and their neighboring relationships from a data file.
 * <p>
 * This class is responsible for reading country and neighbor data from a specified file, parsing the information, and organizing it into a usable structure for the game. It provides methods to initialize countries with their neighbors and continents with countries, ensuring that the game's geographical data is accurately represented and accessible.
 */
public class CountryInitiator {
    final Map<String, List<String>> countriesAndNeighbors;

    /**
     * Constructs a new {@code CountryInitiator} instance for initializing and managing countries and their neighbors.
     * <p>
     * This constructor initializes the {@code countriesAndNeighbors} map and starts the process of loading countries and their neighboring relationships from the specified data file. It serves as the entry point for preparing the game's geographical data.
     *
     * @param filePath The path to the file containing the countries and their neighbors. This file is expected to follow a specific format where each line represents a country followed by its neighbors, separated by commas.
     */
    public CountryInitiator(String filePath) {
        countriesAndNeighbors = new HashMap<>();
        loadCountriesAndNeighbors (filePath);
    }

    /**
     * Loads countries and their neighboring states from a specified file and stores them in a map.
     * <p>
     * This method reads from a file at the given file path, expecting each line to represent a country followed by its neighbors, separated by commas. It parses each line, extracting the country and its list of neighbors, and stores this information in the {@code countriesAndNeighbors} map. This setup facilitates easy access to a country's neighbors throughout the application.
     *
     * @param filePath The path to the file containing the countries and their neighbors. The file should have one country per line, followed by its neighbors, each separated by a comma.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    private void loadCountriesAndNeighbors (String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(FileUtils.getResource("/assets/" + filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String country = parts[0];
                List <String> neighbors = new ArrayList<>();
                for (int i=1; i<parts.length; i++) {
                    neighbors.add(parts[i]);
                }
                countriesAndNeighbors.put(country, neighbors);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes all countries with their abbreviations and neighbors, and returns a list of all initialized countries.
     * <p>
     * This method first creates all country objects from the {@code countriesAndNeighbors} map, where each country is initialized with its name and abbreviation. The abbreviation is assumed to be the first element in the list of neighbors. After all countries are created, the method iterates again over the {@code countriesAndNeighbors} map to set the neighbors for each country. Neighbors are added based on the remaining elements in the list for each country entry in the map.
     * <p>
     * The method returns a list of all the country objects it has initialized, with each country having its neighbors properly set. This list can be used for further processing or initialization in the game.
     *
     * @return A list of {@code Country} objects, each initialized with a name, abbreviation, and a list of neighbors.
     */
    public List<Country> initializeCountries() {
        Map<String, Country> countryMap = new HashMap<>();

        // Creates all Countries out of countriesAndNeighbors
        for (Map.Entry<String, List<String>> entry : countriesAndNeighbors.entrySet()) {
            String countryName = entry.getKey();
            String abbreviation = entry.getValue().remove(0); // Assuming abbreviation is the first element in the list
            Country country = new Country(countryName, abbreviation);
            countryMap.put(countryName, country);
        }

        // Adds Neighbors to all created Countries
        for (Map.Entry<String, List<String>> entry : countriesAndNeighbors.entrySet()) {
            String countryName = entry.getKey();
            Country country = countryMap.get(countryName);
            List<Country> neighbors = new ArrayList<>();
            for (String neighborName : entry.getValue()) {
                Country neighbor = countryMap.get(neighborName);
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
            }
            country.setNeighbors(neighbors);
        }
        return new ArrayList<>(countryMap.values());
    }

    /**
     * Initializes continents with their respective bonus units and assigns countries to them based on a data file.
     * <p>
     * This method reads from a specified file to create {@code Continent} objects, each with a name and bonus units. It then assigns countries to these continents based on the country names listed in the file for each continent. The countries must already be initialized and passed to this method in a list. This setup is crucial for accurately representing the game's geographical divisions and the strategic value of each continent.
     *
     * @param filePath The path to the file containing the continents, their bonus units, and the countries they contain. Each line in the file should start with the continent name, followed by the bonus units, and then the list of countries, all separated by commas.
     * @param countries A list of all {@code Country} objects previously initialized, which will be used to assign countries to their respective continents.
     * @return A list of {@code Continent} objects, each initialized with a name, bonus units, and a list of countries belonging to it.
     */
    public List<Continent> initializeContinents(String filePath, List<Country> countries) {
        List<Continent> continents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(FileUtils.getResource("/assets/" + filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String continentName = parts[0];
                int bonusUnits = Integer.parseInt(parts[1]);
                Continent continent = new Continent(continentName, bonusUnits);
                for (int i = 2; i < parts.length; i++) {
                    String countryName = parts[i];
                    Country country = getCountryByName(countryName, countries);
                    if (country != null) {
                        continent.addCountry(country);
                    }
                }
                continents.add(continent);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return continents;
    }

    /**
     * Retrieves a {@code Country} object by its name from a list of countries.
     * <p>
     * This method iterates through a given list of {@code Country} objects, comparing each country's name with the specified {@code countryName}. If a match is found, the corresponding {@code Country} object is returned. If no match is found, the method returns {@code null}, indicating that no country with the specified name exists in the list.
     *
     * @param countryName The name of the country to be retrieved.
     * @param countries A list of {@code Country} objects among which to search for the country.
     * @return The {@code Country} object with the specified name, or {@code null} if no such country exists in the list.
     */
    public Country getCountryByName(String countryName, List<Country> countries) {
        for (Country country : countries) {
            if (country.getName().equals(countryName)) {
                return country;
            }
        }
        return null;
    }

}


