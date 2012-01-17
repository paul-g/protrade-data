package org.ic.protrade.data.match;

public class SetScore {

    private int playerOneGames;
    private int playerTwoGames;

    private int tiebreakPointsPlayerOne;
    private int tiebreakPointsPlayerTwo;

    private PlayerEnum winner;

    public SetScore() {
    }

    public SetScore(int playerOneGames, int playerTwoGames) {
        this.playerOneGames = playerOneGames;
        this.playerTwoGames = playerTwoGames;
        if (isFinished()) {
            winner = (playerOneGames > playerTwoGames ? PlayerEnum.PLAYER1
                    : PlayerEnum.PLAYER2);
        }
    }

    private void addPlayerGame(PlayerEnum player) {

        boolean isPlayerOne = player.equals(PlayerEnum.PLAYER1);
        int games = (isPlayerOne ? playerOneGames : playerTwoGames);
        int tiebreakPoints = (isPlayerOne ? tiebreakPointsPlayerOne
                : tiebreakPointsPlayerTwo);

        if (!isFinished()) {
            if (!isTiebreak()) {
                games++;
            } else {
                tiebreakPoints++;
                if (tiebreakPoints >= 7
                        && Math.abs(tiebreakPoints - opponentTieScore(player)) >= 2) {
                    games++;
                }
            }

            if (PlayerEnum.PLAYER1.equals(player)) {
                tiebreakPointsPlayerOne = tiebreakPoints;
                playerOneGames = games;
            } else {
                tiebreakPointsPlayerTwo = tiebreakPoints;
                playerTwoGames = games;
            }
        }
        if (isFinished()) {
            winner = player;
        }
    }

    private int opponentTieScore(PlayerEnum player) {
        if (PlayerEnum.PLAYER1.equals(player)) {
            return tiebreakPointsPlayerTwo;
        } else {
            return tiebreakPointsPlayerOne;
        }
    }

    public void addPlayerOneGame() {
        addPlayerGame(PlayerEnum.PLAYER1);
    }

    public void addPlayerTwoGame() {
        addPlayerGame(PlayerEnum.PLAYER2);
    }

    public void addPlayerOneGames(int games) {
        for (int i = 0; i < games; i++) {
            this.addPlayerOneGame();
        }
    }

    public void addPlayerTwoGames(int games) {
        for (int i = 0; i < games; i++) {
            this.addPlayerTwoGame();
        }
    }

    public final boolean isFinished() {
        return (playerOneGames == 7 || playerTwoGames == 7)
                || (Math.abs(playerOneGames - playerTwoGames) >= 2 && (playerOneGames >= 6 || playerTwoGames >= 6));
    }

    public boolean isTiebreak() {
        return (playerOneGames == playerTwoGames && playerOneGames == 6);
    }

    public int getPlayerOneGames() {
        return playerOneGames;
    }

    public int getPlayerTwoGames() {
        return playerTwoGames;
    }

    public int getTiebreakPointsPlayerOne() {
        return tiebreakPointsPlayerOne;
    }

    public int getTiebreakPointsPlayerTwo() {
        return tiebreakPointsPlayerTwo;
    }

    public PlayerEnum getWinner() {
        return winner;
    }

    public int getPlayerGames(PlayerEnum player) {
        if (PlayerEnum.PLAYER1.equals(player)) {
            return getPlayerOneGames();
        }
        return getPlayerTwoGames();
    }
}
