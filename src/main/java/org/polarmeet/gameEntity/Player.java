package org.polarmeet.gameEntity;

// When you are having multiple properties / data / state related to a single entity, you need to create a class to hold such data.
// Here we are having a Player which might have height, weight, weaponType etc etc, and we want to hold this data at a single place (which is related to player) =
// hence we have created a class.

public class Player {
    int height = 1;
    String playerName = "Chinmay";
    String typeOfWeapon = "GUN";

    public void getPlayerName() {
        System.out.println(playerName);
    }
}
