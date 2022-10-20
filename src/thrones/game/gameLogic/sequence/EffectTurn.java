package thrones.game.gameLogic.sequence;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.character.CharacterEffect;
import thrones.game.players.Player;
import thrones.game.players.PlayerType;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;

import java.util.Optional;

public class EffectTurn extends Turn{
    public EffectTurn(GameOfThrones game, CardUI cardUI, Character[] characters) {
        super(game, cardUI, characters);
    }

    @Override
    public void runTurn(Player player, PlayerType playerType) {

        int selectedPileIndex;
        Optional<Card> selected;
        int playerIndex = player.getPlayerIndex();

        if (playerType == PlayerType.HUMAN) {
            //waitForCorrectSuit(nextPlayer, false);
            selected = player.waitForCorrectSuit(false);

        } else {
            //pickACorrectSuit(nextPlayer, false);
            selected = player.pickACorrectSuit(false);

        }

        if (selected.isPresent()) {
            // fix this later
            game.setStatusText("Selected: " + LoggingSystem.canonical(selected.get()) + ". Player" + playerIndex + " select a pile to play the card.");
            if (playerType == PlayerType.HUMAN) {
                //waitForPileSelection();
                selectedPileIndex = player.waitForPileSelection(characters);
            } else {
                //selectRandomPile();
                selectedPileIndex = player.selectRandomPile();
            }
            LoggingSystem.logMove(playerIndex,selected.get(),selectedPileIndex);

            cardUI.moveToPile(selected.get(), characters[selectedPileIndex].getPile());
            characters[selectedPileIndex] = new CharacterEffect(selected.get(), characters[selectedPileIndex]);


            updatePileRanks();
        } else {
            game.setStatusText("Pass.");
            //for debugging
            System.out.println("passed "+ playerIndex);
        }

    }
}
