package thrones.game.gameLogic.sequence;

import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.players.Player;
import thrones.game.players.PlayerType;
import thrones.game.utility.CardUI;

public abstract class Round {

    protected GameOfThrones game;
    protected CardUI cardUI;
    protected Character[] characters;
    protected Player[] players;
    protected int startingPlayer;
    protected Turn[] turns = null;
    protected boolean[] humanPlayers;

    public Round(GameOfThrones game, CardUI cardUI, Character[] characters, Player[] players, int startingPlayer, boolean[] humanPlayers) {
        // will clean this up later
        this.game = game;
        this.cardUI = cardUI;
        this.characters = characters;
        this.players = players;
        this.startingPlayer = startingPlayer;
        this.humanPlayers=humanPlayers;

        this.turns = createTurns();

    }

    public void runRound(){

        int currPlayer = startingPlayer;

        for(int i = 0; i < 4; i++){
            currPlayer=getPlayerIndex(currPlayer);


            game.setStatusText("Player " + currPlayer + " select a Heart card to play");
            PlayerType playerType;
            if (humanPlayers[currPlayer]) {
                playerType=PlayerType.HUMAN;
            } else {
                playerType=PlayerType.RANDOM;
            }

            turns[i].runTurn(players[currPlayer],playerType );



            currPlayer++;
        }



    }

    private int getPlayerIndex(int index) {
        return index % 4;
    }//magic for now


    protected abstract Turn[] createTurns();
}
