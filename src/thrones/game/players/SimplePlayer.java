package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.character.CharacterEffect;
import thrones.game.gameLogic.sequence.plays.Battle;

import java.util.Optional;

public class SimplePlayer extends RandomPlayer{
    public SimplePlayer(Hand hand, GameOfThrones game, int playerIndex) {
        super(hand, game, playerIndex);
    }

    private Optional<Card> selectedCard;


    @Override
    public Optional<Card> pickCard(boolean isCharacter, Character[] characters) {
        selectedCard = pickACorrectSuit(isCharacter);
        return selectedCard;
    }

    @Override
    public int pickPile(Character[] characters) {
        int selectedPile = selectRandomPile();
        Battle battle = new Battle(null, characters,null,null);
        int[] battleOutcomeBefore = battle.simulateBattle(characters[0],characters[1] );
        Character characterNew = new CharacterEffect(selectedCard.get(),characters[selectedPile]);
        int[] battleOutcomeAfter;
        if(selectedPile==0){
            battleOutcomeAfter = battle.simulateBattle(characterNew,characters[1] );

        } else{
            battleOutcomeAfter = battle.simulateBattle(characters[0],characterNew );

        }

        if(playerIndex %2 ==0){
            //if in team 0
            if(battleOutcomeBefore[0]<battleOutcomeAfter[0] || battleOutcomeBefore[1] > battleOutcomeAfter[1]){
                //if character 0 wins OR if character 1 loses when they did not before
                return selectedPile;
            }
        } else{
            if(battleOutcomeBefore[0]>battleOutcomeAfter[0] || battleOutcomeBefore[1] < battleOutcomeAfter[1]){
                //if character 0 wins OR if character 1 loses when they did not before
                return selectedPile;
            }
        }


        return -1;
    }

}
