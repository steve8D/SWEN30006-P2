package thrones.game.gameSequence.turn;

import ch.aplu.jcardgame.Card;
import thrones.game.character.Character;
import thrones.game.players.Player;
import thrones.game.utility.*;
import thrones.game.utility.rules.*;

import java.util.Optional;

public class EffectTurn extends Turn implements Publisher {
    public static final int NON_SELECTION_VALUE = -1;
    int selectedPileIndex;
    Optional<Card> selected;
    int playerIndex;

    public EffectTurn(CardUI cardUI, Character[] characters) {
        super(cardUI, characters);
    }

    @Override
    public void runTurn(Player player) {
        playerIndex = player.getPlayerIndex();
        cardUI.cardSelectedMessage(playerIndex,false);
        selected = player.pickCard(false, characters);

        if (selected.isPresent()) {

            cardUI.cardSelectedMessage(selected.get(),playerIndex);
            selectedPileIndex = player.pickPile(characters);
            if (selectedPileIndex == NON_SELECTION_VALUE) {
                cardUI.setStatusText("Pass.");
                return;
            }
            try {
                LegalityChecker legalityChecker = createRuleChecker();


                if (legalityChecker.isLegal(characters[selectedPileIndex], selected.get()) == false) {
                    throw new BrokeRuleException("rule violated");
                }
                makeMove();

            } catch (BrokeRuleException e) {
                // Empty
            }
        } else {
            cardUI.setStatusText("Pass.");
        }
    }

    private LegalityChecker createRuleChecker(){
        CompositeRule legalityChecker = new CompositeRule();
        legalityChecker.addRule(new EffectRule());
        legalityChecker.addRule(new DiamondOnHeartRule());
        return (LegalityChecker) legalityChecker;
    }

    @Override
    public void publish(Card event) {
        CardCounter.getInstance().publish(this, event);
    }

    private void makeMove(){
        LoggingSystem.logMove(playerIndex, selected.get(), selectedPileIndex);
        cardUI.moveToPile(selected.get(), characters[selectedPileIndex].getPile());
        Card selectedCard = selected.get();
        publish(selectedCard);
        Character mostRecentCard = characters[selectedPileIndex];
        characters[selectedPileIndex] = characterEffectFactory.getInstance().createCharacter(selectedCard, mostRecentCard, true);
        updatePileRanks();
    }
}
