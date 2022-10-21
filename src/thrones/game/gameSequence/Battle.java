package thrones.game.gameSequence;

import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;
import thrones.game.character.Character;

public class Battle {
    private GameOfThrones game;
    private Character[] characters;
    private CardUI cardUI;
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

    public int[] doBattle(){
        int[] character0stats = calculatePileRanks(0);
        int[] character1stats = calculatePileRanks(1);

        LoggingSystem.logPiles(getPilesFromCharacters(characters), character0stats, character1stats);

        GameOfThrones.Rank pile0CharacterRank = characters[0].getBaseRank(); //(Rank) piles[0].getCardList().get(0).getRank();
        GameOfThrones.Rank pile1CharacterRank = characters[1].getBaseRank(); //(Rank) piles[1].getCardList().get(0).getRank();

        String character0Result;
        String character1Result;

        if (character0stats[ATTACK_RANK_INDEX] > character1stats[DEFENCE_RANK_INDEX]) {
            players[0].addScore( pile1CharacterRank.getRankValue());
            players[2].addScore( pile1CharacterRank.getRankValue());
            character0Result = "Character 0 attack on character 1 succeeded.";

        } else {
            players[1].addScore( pile1CharacterRank.getRankValue());
            players[3].addScore( pile1CharacterRank.getRankValue());
            character0Result = "Character 0 attack on character 1 failed.";


        }

        if (character1stats[ATTACK_RANK_INDEX] > character0stats[DEFENCE_RANK_INDEX]) {
            players[1].addScore( pile0CharacterRank.getRankValue());
            players[3].addScore( pile0CharacterRank.getRankValue());
            character1Result = "Character 1 attack on character 0 succeeded.";


        } else {
            players[0].addScore( pile0CharacterRank.getRankValue());
            players[2].addScore( pile0CharacterRank.getRankValue());
            character1Result = "Character 1 attack character 0 failed.";


        }
        updateScores();


        LoggingSystem.logBattle(character0Result);
        LoggingSystem.logBattle(character1Result);

        game.setStatusText(character0Result + " " + character1Result);

        return null;
    }

    private int[] calculatePileRanks(int pileIndex) {
        Character currentCharacter = characters[pileIndex];
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
