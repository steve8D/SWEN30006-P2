package thrones.game.gameSequence;

import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;
import thrones.game.character.Character;
import thrones.game.GameOfThrones.Rank;

public class Battle {


    public static final String CHAR_0_SUCCESS = "Character 0 attack on character 1 succeeded.";
    public static final String CHAR_0_FAIL = "Character 0 attack on character 1 failed.";
    public static final String CHAR_1_SUCCESS = "Character 1 attack on character 0 succeeded.";
    public static final String CHARACTER_0_FAILED = "Character 1 attack on character 0 failed.";
    private GameOfThrones game;
    private Character[] characters;
    private CardUI cardUI;
    //private int[] scores;
    private Player[] players;
    private boolean isReal;


    public Battle(GameOfThrones game, Character[] characters, CardUI cardUI, Player[] players) {
        this.game = game;
        this.characters = characters;
        this.cardUI = cardUI;
        this.players = players;
        this.isReal = true;
    }

    public Battle(){
        // for simulating battle only
        this.isReal = false;
    }

    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;



    private Hand[] getPilesFromCharacters(Character[] characters){
        // duplicate code only used for loggingsystem- change logPiles to use characters instead
        Hand[] piles =null;
        if(characters != null){ //if there are characters, get their piles
            piles = new Hand[2];
            piles[0] = characters[0].getPile();
            piles[1] = characters[1].getPile();
        }
        return piles;

    }


    public boolean[] simulateBattle(Character character0, Character character1){
        int[] character0stats = calculatePileRanks(character0);
        int[] character1stats = calculatePileRanks(character1);


        boolean character0success;
        boolean character1success;

        if (character0stats[ATTACK_RANK_INDEX] > character1stats[DEFENCE_RANK_INDEX]) {
            character0success=true;
        } else {
            character0success=false;
        }
        if (character1stats[ATTACK_RANK_INDEX] > character0stats[DEFENCE_RANK_INDEX]) {
            character1success=true;
        } else {
            character1success=false;
        }

        return new boolean[] {character0success,character1success};
    }

    public int[] doBattle(){

        int[] character0stats = calculatePileRanks(characters[0]);
        int[] character1stats = calculatePileRanks(characters[1]);
        LoggingSystem.logPiles(getPilesFromCharacters(characters), character0stats,character1stats );


        boolean[] successes = simulateBattle(characters[0],characters[1] );

        boolean character0success = successes[0];
        boolean character1success = successes[1];


        int[] scoresToAdd = {0,0};
        Rank pile0CharacterRank = characters[0].getBaseRank(); //(Rank) piles[0].getCardList().get(0).getRank();
        Rank pile1CharacterRank = characters[1].getBaseRank(); //(Rank) piles[1].getCardList().get(0).getRank();

        String character0Result;
        String character1Result  ;

        if (character0success==true) {
            scoresToAdd[0] += pile1CharacterRank.getRankValue();
             character0Result =  CHAR_0_SUCCESS;
        } else {
            scoresToAdd[1] += pile1CharacterRank.getRankValue();
             character0Result =  CHAR_0_FAIL;

        }

        if (character1success == true) {
            scoresToAdd[1] += pile0CharacterRank.getRankValue();
             character1Result =  CHAR_1_SUCCESS ;
        } else {
            scoresToAdd[0] += pile0CharacterRank.getRankValue();
             character1Result =CHARACTER_0_FAILED;
        }

        for (int i = 0; i < 4; i++){
            players[i].addScore(scoresToAdd[i%2]);
        }


        updateScores();


        LoggingSystem.logBattle(character0Result);
        LoggingSystem.logBattle(character1Result);

        game.setStatusText(character0Result + " " + character1Result);

        return null;
    }

    private int[] calculatePileRanks(Character character) {
        //Hand currentPile = piles[pileIndex];
        //int i = currentPile.isEmpty() ? 0 : ((Rank) currentPile.get(0).getRank()).getRankValue(); //remove

        Character currentCharacter = character;
        int attack = currentCharacter.getAttack();
        int def = currentCharacter.getDefense();

        return new int[] { attack, def };
    }

    private void updateScores() {
        for (int i = 0; i < players.length; i++) {//magic
            cardUI.updateScore(i, players[i].getScore());
        }
        int[] score = {players[0].getScore(), players[1].getScore()};
        LoggingSystem.logScores(score);
        //LoggingSystem.logScores(scores);
    }

}
