package thrones.game.gameLogic.sequence;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.character.CharacterEffect;
import thrones.game.players.Player;
import thrones.game.utility.CardCounter;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;
import thrones.game.utility.Publisher;

import java.util.Optional;

public class EffectTurn extends Turn implements Publisher {
    public EffectTurn(GameOfThrones game, CardUI cardUI, Character[] characters) {
        super(game, cardUI, characters);
    }


    private final int NON_SELECTION_VALUE = -1;


    @Override
    public void runTurn(Player player) {

        int selectedPileIndex;
        Optional<Card> selected;
        int playerIndex = player.getPlayerIndex();

        selected = player.pickCard(false, characters);

        if (selected.isPresent()) {
            // fix this later
            game.setStatusText("Selected: " + LoggingSystem.canonical(selected.get()) + ". Player" + playerIndex + " select a pile to play the card.");

            selectedPileIndex = player.pickPile(characters);
            if(selectedPileIndex == NON_SELECTION_VALUE){
                game.setStatusText("Pass.");

            } else {
                LoggingSystem.logMove(playerIndex,selected.get(),selectedPileIndex);

                publish(selected.get());

                cardUI.moveToPile(selected.get(), characters[selectedPileIndex].getPile());
                characters[selectedPileIndex] = new CharacterEffect(selected.get(), characters[selectedPileIndex]);

                updatePileRanks();
            }

        } else {
            game.setStatusText("Pass.");
        }

    }

    @Override
    public void publish(Card event) {
        CardCounter.getInstance().publish(this, event);
    }
}
