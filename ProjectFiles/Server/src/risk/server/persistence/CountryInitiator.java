package risk.server.persistence;

import risk.common.FileUtils;
import  risk.common.entities.Continent;
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
 * Initializes and manages country and continent data for the Risk game.
 * <p>
 * This class is responsible for loading country and neighbor relationships from a file, initializing country objects with their respective neighbors,
 * and organizing countries into continents. It provides methods to initialize countries and continents based on external data files, allowing for dynamic
 * game setups. The class also includes utility methods for retrieving country objects by name.
 */
public class CountryInitiator {
    final Map<String, List<String>> countriesAndNeighbors;

    /**
     * Constructs a new CountryInitiator with the specified file path.
     * <p>
     * This constructor initializes the {@code countriesAndNeighbors} map and begins the process of loading country and neighbor relationships from the specified file.
     * The file should contain country names followed by their respective neighbors, separated by commas. This data is essential for setting up the game map with accurate territorial adjacencies.
     *
     * @param filePath the path to the file containing country and neighbor data. This should include the full path and the file name.
     */
    public CountryInitiator(String filePath) {
        countriesAndNeighbors = new HashMap<>();
        loadCountriesAndNeighbors (filePath);
    }

    /**
     * Loads country and neighbor relationships from a specified file into a map.
     * <p>
     * This method reads from a file at the given file path, expecting each line to represent a country followed by its neighbors, separated by commas.
     * It parses each line, extracting the country name and its list of neighbors, and stores them in the {@code countriesAndNeighbors} map.
     * This setup is crucial for initializing the game map with accurate territorial adjacencies.
     *
     * @param filePath the path to the file containing country and neighbor data, including the full path and file name.
     * @throws IOException if an I/O error occurs reading from the file.
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
     * Initializes countries with their respective abbreviations and neighbors.
     * <p>
     * This method processes a pre-loaded map of countries and their neighbors to create {@link Country} objects. Each country is initialized with its name and abbreviation, and then its neighbors are added. The abbreviation is assumed to be the first element in the list of neighbors for each country, which is then removed from the list to leave only the actual neighbors.
     * <p>
     * After all countries are created and their neighbors set, the method returns a list of all the initialized {@link Country} objects. This list is useful for further operations that require a complete set of countries with their relationships established.
     *
     * @return A list of {@link Country} objects, each initialized with a name, abbreviation, and neighbors.
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
     * Initializes continents with their respective bonus units and assigns countries to them.
     * <p>
     * This method reads from a specified file to create {@link Continent} objects, each with a name and bonus units. It then assigns countries to these continents based on the file's contents. The file format should list each continent followed by its bonus units and the countries it contains, separated by commas.
     * <p>
     * The method utilizes the {@code getCountryByName} method to match country names from the file with {@link Country} objects provided in the {@code countries} list parameter. This ensures that the continents are accurately populated with the correct countries.
     * <p>
     * If the file cannot be read or if an error occurs during processing, an error message is printed to the console.
     *
     * @param filePath the path to the file containing continent names, their bonus units, and the countries they contain. The path should include the full path and file name.
     * @param countries a list of all {@link Country} objects to be assigned to continents. This should be the list of countries initialized by the {@code initializeCountries} method.
     * @return A list of {@link Continent} objects, each initialized with a name, bonus units, and a list of countries.
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
     * Retrieves a {@link Country} object by its name from a provided list of countries.
     * <p>
     * This method iterates through a list of {@link Country} objects, comparing each country's name with the specified {@code countryName}. If a match is found, the corresponding {@link Country} object is returned. If no match is found, the method returns {@code null}, indicating that no country with the specified name exists in the list.
     *
     * @param countryName the name of the country to be retrieved.
     * @param countries a list of {@link Country} objects to search through.
     * @return the {@link Country} object with the specified name, or {@code null} if no such country exists in the list.
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


