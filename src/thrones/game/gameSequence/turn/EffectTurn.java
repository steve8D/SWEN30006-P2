package thrones.game.gameSequence.turn;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.players.Player;
import thrones.game.utility.*;
import thrones.game.utility.rules.CompositeRule;
import thrones.game.utility.rules.DiamondOnHeartRule;
import thrones.game.utility.rules.EffectRule;

import java.util.Optional;

public class EffectTurn extends Turn implements Publisher {
    public static final int NON_SELECTION_VALUE = -1;

    public EffectTurn(CardUI cardUI, Character[] characters) {
        super(cardUI, characters);
    }

    @Override
    public void runTurn(Player player) {
        int playerIndex = player.getPlayerIndex();
        cardUI.setStatusText("Player" + playerIndex + " select a non-Heart card to play.");
        int selectedPileIndex;
        Optional<Card> selected;
        selected = player.pickCard(false, characters);
        if (selected.isPresent()) {
            cardUI.roundStartMessage(selected.get(),playerIndex);
            selectedPileIndex = player.pickPile(characters);
            if (selectedPileIndex == NON_SELECTION_VALUE) {
                cardUI.setStatusText("Pass.");
                return;
            }
            try {
                CompositeRule legalityChecker = new CompositeRule();
                legalityChecker.addRule(new EffectRule());
                legalityChecker.addRule(new DiamondOnHeartRule());
                if (legalityChecker.isLegal(characters[selectedPileIndex], selected.get()) == false) {
                    throw new BrokeRuleException("rule violated");
                }
                LoggingSystem.logMove(playerIndex, selected.get(), selectedPileIndex);
                cardUI.moveToPile(selected.get(), characters[selectedPileIndex].getPile());
                Card selectedCard = selected.get();
                publish(selectedCard);
                Character mostRecentCard = characters[selectedPileIndex];
                characters[selectedPileIndex] = characterEffectFactory.getInstance().createCharacter(selectedCard, mostRecentCard);
                updatePileRanks();
            } catch (BrokeRuleException e) {
                // Empty
            }
        } else {
            cardUI.setStatusText("Pass.");
        }
    }

    @Override
    public void publish(Card event) {
        CardCounter.getInstance().publish(this, event);
    }
}
