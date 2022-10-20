package thrones.game.gameLogic.sequence.plays;

import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.gameLogic.sequence.ConsequentRound;
import thrones.game.gameLogic.sequence.FirstRound;
import thrones.game.gameLogic.sequence.Round;
import thrones.game.gameLogic.sequence.plays.strategy.IStartingPlayerStrategy;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

public class Play {

    protected GameOfThrones game;
    protected CardUI cardUI;
    protected Character[] characters;
    protected Player[] players;

    protected Round[] rounds;

    protected IStartingPlayerStrategy startingPlayerStrategy;

    public Play( GameOfThrones game, CardUI cardUI, Character[] characters, Player[] players, IStartingPlayerStrategy startingPlayerStrategy) {
        this.game = game;
        this.cardUI = cardUI;
        this.characters = characters;
        this.players = players;


        this.startingPlayerStrategy = startingPlayerStrategy;

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

    public void runPlay(){
        for (int i = 0; i < 3; i++){
            rounds[i].runRound();
        }
    }
}
