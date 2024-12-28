package risk.common.interfaces;

import java.util.*;

import risk.common.entities.Continent;
import risk.common.entities.Country;
import risk.common.entities.Player;
import risk.common.exceptions.CountryNotFoundException;
import risk.common.exceptions.InvalidUnitException;

/**
 * Defines the core interface for the Risk game, encapsulating all essential game functionalities.
 * <p>
 * This interface outlines the methods required for game setup, player management, game state updates, and gameplay actions such as attacks, unit movements, and card trading.
 * Implementations of this interface are responsible for managing the game logic, including the initialization of game components, handling player actions, and enforcing game rules.
 * <p>
 * Key functionalities include starting the game, managing players and their units, processing attacks between countries, and handling turn-based mechanics.
 */
public interface RiskInterface {

     public void addGameEventListener(GameEventListener listener);

     public void removeGameEventListener(GameEventListener listener);

     public void notifyPlayerListChange();

     public void notifyGameStarted();

     public void notifyAllowPlayerActions(boolean allow);

    //Alle Methode die der Client auf dem Server aufrufen kann
     public abstract void startGame();

     public abstract List<Country> getCountries();

     public abstract List<Continent> getContinents();

     public abstract void addPlayer(String name, String color);

     public abstract Player addPlayer(String name,int id, String color);

     public abstract void removePlayer(String name);

     public abstract Player getPlayerByName(String name);

     public abstract String listPlayers();

     public abstract List<Player> getPlayers();

     public abstract void makeAttack(String fromCou, String toCou, String attacker, int numberOfUnits);

     public abstract String moveUnits(String player, String fromCountry, String toCountry, int units);

     public abstract String getAvailableColors();

     public abstract void distributeUnits();

     public abstract void sendMessage(String message);

     public abstract Player getPlayerOnTurn();

     public abstract String turnInUnitCards(String player, String card1, String card2, String card3);

     public abstract void nextTurn();

     public abstract boolean validateOwnership(String country, String player);

     public abstract Country getCountryByName(String name) throws CountryNotFoundException;

     public int getUnitsByCountry(String country);

     public abstract String continentPlayerCheck(String playername);

     public abstract String addPlayerUnitsToCountry(String player, String country, int units) throws InvalidUnitException;

     public abstract String getCountryInformationGUI(String country);

     public abstract void distributeStartingUnits();
     public abstract String addPlayerUnitsPerRound(String player);
     public abstract String addPlayerUnitsPerRound(Player player);
     public boolean checkCountryNeighbor(String country1, String country2);

}

