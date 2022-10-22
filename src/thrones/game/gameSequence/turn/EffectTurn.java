package thrones.game.gameSequence.turn;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;

import java.util.Optional;

public class EffectTurn extends Turn {
    public EffectTurn(GameOfThrones game, CardUI cardUI, Character[] characters) {
        super(game, cardUI, characters);
    }

    @Override
    public void runTurn(Player player) {
        int selectedPileIndex;
        Optional<Card> selected;
        int playerIndex = player.getPlayerIndex();

        selected = player.pickCard(false);

        if (selected.isPresent()) {
            // fix this later
            game.setStatusText("Selected: " + LoggingSystem.canonical(selected.get()) + ". Player" + playerIndex + " select a pile to play the card.");

            selectedPileIndex = player.pickPile(characters);

            LoggingSystem.logMove(playerIndex,selected.get(),selectedPileIndex);

            cardUI.moveToPile(selected.get(), characters[selectedPileIndex].getPile());

            Card selectedCard = selected.get();
            Character mostRecentCard = characters[selectedPileIndex];
            characters[selectedPileIndex] = characterEffectFactory.getInstance().createCharacter(selectedCard, mostRecentCard);
            updatePileRanks();
        } else {
            game.setStatusText("Pass.");
        }
    }
}
