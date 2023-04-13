/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       12/04/2023
 *  Last updated:  13/04/2023
 *
 *  Compilation:   javac BaseballElimination.java
 *  Execution:     java BaseballElimination
 *
 *  Seam carver datatype
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseballElimination {
    private static final int TEAM_NAME_INDEX = 0;
    private static final int WINS_INDEX = 1;
    private static final int LOSSES_INDEX = 2;
    private static final int LEFT_GAMES_INDEX = 3;
    private final String[] teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] leftGames;
    private final int[][] games;

    /*
    Create a baseball division from given filename in format specified below
     */
    public BaseballElimination(String filename) {
        validateString(filename);

        var fileData = new In(filename);
        var fileLines = fileData.readAllLines();

        var count = Integer.parseInt(fileLines[0]);

        this.teams = new String[count];
        this.wins = new int[count];
        this.losses = new int[count];
        this.leftGames = new int[count];
        this.games = new int[count][count];

        var k = 0;
        for (var i = 1; i < fileLines.length; i++) {
            var line = fileLines[i];
            if (line != null) {
                var split = line.trim().split("\\s+"); // Split line by spaces
                this.teams[k] = split[TEAM_NAME_INDEX].trim();
                this.wins[k] = Integer.parseInt(split[WINS_INDEX].trim());
                this.losses[k] = Integer.parseInt(split[LOSSES_INDEX].trim());
                this.leftGames[k] = Integer.parseInt(split[LEFT_GAMES_INDEX].trim());
                for (var j = 0; j < count; j++) {
                    this.games[k][j] = Integer.parseInt(split[j + 4].trim());
                }
            }
            k++;
        }
    }

    /*
    Number of teams
     */
    public int numberOfTeams() {
        return this.teams.length;
    }

    /*
    All teams
     */
    public Iterable<String> teams() {
        return Arrays.asList(this.teams);
    }

    /*
    Number of wins for given team
     */
    public int wins(String team) {
        validateString(team);

        return this.wins[getTeamIndex(team)];
    }

    /*
    Number of losses for given team
     */
    public int losses(String team) {
        validateString(team);

        return this.losses[getTeamIndex(team)];
    }

    /*
    Number of remaining games for given team
     */
    public int remaining(String team) {
        validateString(team);

        return this.leftGames[getTeamIndex(team)];
    }

    /*
    Number of remaining games between team1 and team2
     */
    public int against(String team1, String team2) {
        validateString(team1);
        validateString(team2);

        return this.games[getTeamIndex(team1)][getTeamIndex(team2)];
    }

    /*
    Is given team eliminated?
     */
    public boolean isEliminated(String team) {
        var subset = getSubset(team);

        return !subset.isEmpty();
    }

    /*
    Subset R of teams that eliminates given team; null if not eliminated
     */
    public Iterable<String> certificateOfElimination(String team) {
        var subset = getSubset(team);

        return subset.isEmpty() ? null : subset;
    }

    private void validateString(String value) {
        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException();
        }
    }

    private int getTeamIndex(String team) {
        for (var i = 0; i < this.teams.length; i++) {
            if (this.teams[i] != null && this.teams[i].equalsIgnoreCase(team)) {
                return i;
            }
        }

        throw new IllegalArgumentException();
    }

    private ArrayList<String> getSubset(String team) {
        validateString(team);

        var count = this.teams.length;
        var targetTeamIndex = getTeamIndex(team);
        var targetTeamMaxPossibleWins = this.wins[targetTeamIndex] + this.leftGames[targetTeamIndex];

        // Trivial elimination
        var subset = getTrivialElimination(count, targetTeamIndex, targetTeamMaxPossibleWins);
        if (subset != null && !subset.isEmpty()) {
            return subset;
        }

        // Nontrivial elimination
        return getNontrivialElimination(count, targetTeamIndex, targetTeamMaxPossibleWins);
    }

    private ArrayList<String> getTrivialElimination(int count, int targetTeamIndex, int targetTeamMaxPossibleWins) {
        for (var x = 0; x < count; x++) {
            if (targetTeamIndex == x) {
                continue;
            }
            if (targetTeamMaxPossibleWins < this.wins[x]) {
                var subset = new ArrayList<String>();
                subset.add(this.teams[x]);
                return subset;
            }
        }

        return null;
    }

    private ArrayList<String> getNontrivialElimination(int count, int targetTeamIndex, int targetTeamMaxPossibleWins) {
        var numberOfGames = count * count;
        var numberOfVertices = 2 + numberOfGames + count; // 2 are vertices s and t plus number of games plus number of teams
        var flowNetwork = new FlowNetwork(numberOfVertices);
        var s = 0;
        for (var i = 0; i < count; i++) {
            if (i == targetTeamIndex) { // Skip target team row
                continue;
            }
            for (var j = i + 1; j < count; j++) {
                if (j == targetTeamIndex) { // Skip target team column
                    continue;
                }

                // Edge from the vertex s to the game vertex
                var gameVertex = getVertexFromCoordinate(i, j, count);
                var gamesLeft = this.games[i][j];
                flowNetwork.addEdge(new FlowEdge(s, gameVertex, gamesLeft));

                // Edges from the game vertex to the team vertices
                var teamVertex1 = numberOfGames + 1 + i;
                var teamVertex2 = numberOfGames + 1 + j;
                flowNetwork.addEdge(new FlowEdge(gameVertex, teamVertex1, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(gameVertex, teamVertex2, Double.POSITIVE_INFINITY));
            }
        }

        var t = numberOfVertices - 1;
        for (var x = 0; x < count; x++) {
            if (x != targetTeamIndex) {
                // Edge from team vertex to the vertex t
                var teamVertex = numberOfGames + 1 + x;
                var teamWins = Math.max(targetTeamMaxPossibleWins - this.wins[x], 0);
                flowNetwork.addEdge(new FlowEdge(teamVertex, t, teamWins));
            }
        }

        var fordFulkerson = new FordFulkerson(flowNetwork, s, t);

        var subset = new ArrayList<String>();
        for (var x = 0; x < count; x++) {
            if (x != targetTeamIndex) {
                var teamVertex = numberOfGames + 1 + x;
                if (fordFulkerson.inCut(teamVertex)) {
                    subset.add(this.teams[x]);
                }
            }
        }

        return subset;
    }

    private int getVertexFromCoordinate(int i, int j, int width) {
        return i * width + j + 1;
    }

    public static void main(String[] args) {
        var division = new BaseballElimination("teams5.txt");
        for (var team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (var t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
