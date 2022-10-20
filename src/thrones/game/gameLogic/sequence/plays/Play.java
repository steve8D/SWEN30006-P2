package thrones.game.gameLogic.sequence.plays;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.PropertiesLoader;
import thrones.game.character.BaseCharacter;
import thrones.game.character.Character;
import thrones.game.gameLogic.sequence.ConsequentRound;
import thrones.game.gameLogic.sequence.FirstRound;
import thrones.game.gameLogic.sequence.Round;
import thrones.game.gameLogic.sequence.plays.strategy.IStartingPlayerStrategy;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

import java.util.Properties;

public class Play {

    protected GameOfThrones game;
    protected CardUI cardUI;
    protected Character[] characters;
    protected Player[] players;

    protected Round[] rounds;

    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;


    protected IStartingPlayerStrategy startingPlayerStrategy;

    public Play( GameOfThrones game, CardUI cardUI, Character[] characters, Player[] players, IStartingPlayerStrategy startingPlayerStrategy) {
        this.game = game;
        this.cardUI = cardUI;
        this.characters = characters;
        this.players = players;


        this.startingPlayerStrategy = startingPlayerStrategy;

        createNewPiles();

        this.rounds = createRounds();

    }


    private Round[] createRounds(){

        int startingPlayer = startingPlayerStrategy.getStartingPlayer();

        Round[] rounds = {
                new FirstRound(game,cardUI,characters,players,startingPlayer),
                new ConsequentRound(game,cardUI,characters,players,startingPlayer),
                new ConsequentRound(game,cardUI,characters,players,startingPlayer)
        };

        return  rounds;
    }

    private void updatePileRanks() {
        for (int j = 0; j < characters.length; j++) { //
            int[] ranks = characters[j].calculatePileRanks();
            cardUI.updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX]);
        }
    }
    private void removeOldPiles(){
        Hand[] piles = getPilesFromCharacters(characters);
        cardUI.removeAll(piles);
    }
    private Hand[] getPilesFromCharacters(Character[] characters){ // move this function into card UI and let cardUI accept character[] params
        Hand[] piles =null;
        if(characters != null){ //if there are characters, get their piles
            piles = new Hand[2];
            piles[0] = characters[0].getPile();
            piles[1] = characters[1].getPile();
        }
        return piles;

    }

    private void createNewPiles() {

        characters = new Character[2];

        for (int i = 0; i < 2; i++) {

            characters[i] = new BaseCharacter();

            //get the pile for setting up the listener (this will happen when the heart card is played later)
            // for now the code requires i set up the listener now
            Hand characterPile = characters[i].getPile();

            cardUI.drawPile(characterPile, i);

            final Hand currentPile = characterPile;
            final int pileIndex = i;



        }

        updatePileRanks();
    }

    public void runPlay(Properties properties){
        

        for (int i = 0; i < 3; i++){
            rounds[i].runRound();
        }
        updatePileRanks();

        Battle battle = new Battle(game,characters,cardUI,players);
        battle.doBattle();

        game.delay(Long.parseLong(properties.getProperty("watchingTime", PropertiesLoader.getDefaultWatchingTime())));
        removeOldPiles();

    }
}
