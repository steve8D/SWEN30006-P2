package thrones.game.gameLogic.sequence;

import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

public class Play {

    protected GameOfThrones game;
    protected CardUI cardUI;
    protected Character[] characters;
    protected Player[] players;
    protected int startingPlayer;
    protected boolean[] humanPlayers;

    protected Round[] rounds;


    public Play(GameOfThrones game, CardUI cardUI, Character[] characters, Player[] players, int startingPlayer, boolean[] humanPlayers) {
        this.game = game;
        this.cardUI = cardUI;
        this.characters = characters;
        this.players = players;
        this.startingPlayer = startingPlayer; //factory will take care of this later
        this.humanPlayers = humanPlayers;

        this.rounds = createRounds();
    }

    private Round[] createRounds(){

        Round[] rounds = {
                new FirstRound(game,cardUI,characters,players,startingPlayer,humanPlayers),
                new ConsequentRound(game,cardUI,characters,players,startingPlayer,humanPlayers),
                new ConsequentRound(game,cardUI,characters,players,startingPlayer,humanPlayers)
        };

        return  rounds;
    }

    public void runPlay(){
        for (int i = 0; i < 3; i++){
            rounds[i].runRound();
        }
    }
}
