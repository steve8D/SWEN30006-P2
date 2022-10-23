package thrones.game.gameSequence.turn;

import ch.aplu.jcardgame.Card;
import thrones.game.character.BaseCharacter;
import thrones.game.character.Character;
import thrones.game.players.Player;
import thrones.game.utility.rules.BrokeRuleException;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;
import thrones.game.utility.rules.CompositeRule;
import thrones.game.utility.rules.HeartRule;
import thrones.game.utility.rules.LegalityChecker;

import java.util.Optional;

public class HeartTurn extends Turn {
    int playerIndex;
    Optional<Card> selected;
    int pileIndex;
    public HeartTurn(CardUI cardUI, Character[] characters) {
        super(cardUI, characters);
    }

    @Override
    public void runTurn(Player player) {
        playerIndex = player.getPlayerIndex();
        cardUI.cardSelectedMessage(playerIndex,true);


        selected = player.pickCard(true, characters);
        pileIndex = playerIndex % 2;

        try {
            LegalityChecker legalityChecker = createRuleChecker();
            if (legalityChecker.isLegal(characters[pileIndex], selected.get()) == false) {
                throw new BrokeRuleException("rule violated");
            }

            assert selected.isPresent() : " Pass returned on selection of character.";
            moveCard();
        } catch (BrokeRuleException e) {

        }

    }

    private LegalityChecker createRuleChecker(){
        CompositeRule legalityChecker = new CompositeRule();
        legalityChecker.addRule(new HeartRule());
        return (LegalityChecker) legalityChecker;
    }

    private void moveCard(){
        LoggingSystem.logMove(playerIndex, selected.get(), pileIndex);
        cardUI.moveToPile(selected.get(), characters[pileIndex].getPile());
        // get existing pile and add heart card into the pile
        BaseCharacter base = (BaseCharacter) characters[pileIndex];
        base.addBaseCard(selected.get());
        updatePileRanks();
    }
}
