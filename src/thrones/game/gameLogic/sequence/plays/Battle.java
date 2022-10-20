package thrones.game.gameLogic.sequence.plays;

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


    public Battle(GameOfThrones game, Character[] characters, CardUI cardUI, Player[] players) {
        this.game = game;
        this.characters = characters;
        this.cardUI = cardUI;
        this.players = players;
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


    public int[] simulateBattle(Character character0, Character character1){
        int[] character0stats = calculatePileRanks(character0);
        int[] character1stats = calculatePileRanks(character1);


        int character0success;
        int character1success;

        if (character0stats[ATTACK_RANK_INDEX] > character1stats[DEFENCE_RANK_INDEX]) {
            character0success=1;
        } else {
            character0success=0;
        }
        if (character1stats[ATTACK_RANK_INDEX] > character0stats[DEFENCE_RANK_INDEX]) {
            character1success=1;
        } else {
            character1success=0;
        }

        return new int[] {character0success,character1success};
    }

    public int[] doBattle(){

        int[] character0stats = calculatePileRanks(characters[0]);
        int[] character1stats = calculatePileRanks(characters[1]);
        LoggingSystem.logPiles(getPilesFromCharacters(characters), character0stats,character1stats );


        int[] successes = simulateBattle(characters[0],characters[1] );

        int character0success = successes[0];
        int character1success = successes[1];

//        if (character0stats[ATTACK_RANK_INDEX] > character1stats[DEFENCE_RANK_INDEX]) {
//            character0success=1;
//        } else {
//            character0success=0;
//        }
//        if (character1stats[ATTACK_RANK_INDEX] > character0stats[DEFENCE_RANK_INDEX]) {
//            character1success=1;
//        } else {
//            character1success=0;
//        }

        int[] scoresToAdd = {0,0};
        Rank pile0CharacterRank = characters[0].getBaseRank(); //(Rank) piles[0].getCardList().get(0).getRank();
        Rank pile1CharacterRank = characters[1].getBaseRank(); //(Rank) piles[1].getCardList().get(0).getRank();


        if (character0success==1) {


            scoresToAdd[0] += pile1CharacterRank.getRankValue();

        } else {


            scoresToAdd[1] += pile1CharacterRank.getRankValue();

        }

        if (character1success == 1) {

            scoresToAdd[1] += pile0CharacterRank.getRankValue();

        } else {

            scoresToAdd[0] += pile0CharacterRank.getRankValue();

        }




        //move this into above if statment
        String character0Result = (character0success==1)? CHAR_0_SUCCESS: CHAR_0_FAIL;
        String character1Result = (character1success==1)? CHAR_1_SUCCESS : CHARACTER_0_FAILED;

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
