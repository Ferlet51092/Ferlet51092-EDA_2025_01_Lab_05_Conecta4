import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class Player{
    private String name;
    private int wins;
    private int draws;
    private int losses;

    public Player(String name){
        this.name = name;
        wins = 0;
        draws = 0;
        losses = 0;
    }

    public String getName(){
        return name;
    }
    public int getWins(){
        return wins;
    }
    public int getDraws(){
        return draws;
    }
    public int getLosses(){
        return losses;
    }

    public void addWin(){
        wins++;
    }
    public void addDraw(){
        draws++;
    }
    public void addLoss(){
        losses++;
    }

    public float winRate() {
        int total = wins + draws + losses;
        if (total == 0) {
            return 0.0f;
        } else {
            return (float) wins / total;
        }
    }

}
class Scoreboard{
    private BST<Integer, String> winTree;
    private HashMap<String, Player> players;
    private int playedGames;

    public Scoreboard(){
        winTree = new BST<>();
        players = new HashMap<>();
        playedGames = 0;
    }

    public void registerPlayer(String playerName){
        if (players.containsKey(playerName)) return;
        Player nuevo = new Player(playerName);
        players.put(playerName, nuevo);
        winTree.put(0, playerName);
    }

    public void addGameResult(String winnerPlayerName, String loserPlayerName, boolean draw){
        Player winner = players.get(winnerPlayerName);
        Player loser = players.get(loserPlayerName);
        if (winner == null || loser == null) return;

        playedGames++;

        if (draw){
            winner.addDraw();
            loser.addDraw();
        }else{
            winner.addWin();
            loser.addLoss();

            winTree.put(winner.getWins(), winnerPlayerName);
        }
    }

    public boolean checkPlayer(String playerName){
        return players.containsKey(playerName);
    }

    public Player[] winRange(int lo, int hi){
        List<Integer> keys = winTree.keysInRange(lo, hi);
        List<Player> result = new ArrayList<>();
        for (Integer key : keys){
            String playerName = winTree.get(key);
            Player player = players.get(playerName);
            if (player != null){
                result.add(player);
            }
        }
        return result.toArray(new Player[0]);
    }
    public Player[] winSuccessor(int wins){
        Integer successorKey = winTree.successor(wins);
        if (successorKey == null){
            return new Player[0];
        }
        List<Player> result = new ArrayList<>();
        String playerName = winTree.get(successorKey);
        Player player = players.get(playerName);
        if (player != null){
            result.add(player);
        }
        return result.toArray(new Player[0]);
    }
    public Player getPlayer(String playerName) {
        return players.get(playerName);
    }
}
class ConnectFour{
    private char[][] grid;
    private char currentSymbol;

    public ConnectFour(){
        grid = new char[7][6];
        for (int col = 0; col < 7; col++){
            for (int fila = 0; fila < 6; fila++){
                grid[col][fila] = ' ';
            }
        }
        currentSymbol = 'X';
    }

    public boolean makeMove(int z){
        if (z < 0 || z >= 7){
            return false;
        }
        for (int fila = 5; fila >= 0; fila--){
            if (grid[z][fila] == ' '){
                grid[z][fila] = currentSymbol;
                toggleCurrentSymbol();
                return true;
            }
        }
        return false;
    }

    private void toggleCurrentSymbol(){
        currentSymbol = (currentSymbol == 'X') ? 'O' : 'X';
    }

    public String isGameOver(){
        if (hasWinner('X')) return "WIN_X";
        if (hasWinner('O')) return "WIN_O";
        if (isFull()) return "DRAW";
        return "ONGOING";
    }

    private boolean isFull(){
        for (int col = 0; col < 7; col++){
            for (int fila = 0; fila < 6; fila++){
                if (grid[col][fila] == ' '){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasWinner(char symbol){
        for (int fila = 0; fila < 6; fila++){
            for (int col = 0; col <= 3; col++){
                if (grid[col][fila] == symbol && grid[col+1][fila] == symbol &&
                        grid[col+2][fila] == symbol && grid[col+3][fila] == symbol){
                    return true;
                }
            }
        }
        for (int col = 0; col < 7; col++){
            for (int fila = 0; fila <= 2; fila++){
                if (grid[col][fila] == symbol && grid[col][fila+1] == symbol &&
                        grid[col][fila+2] == symbol && grid[col][fila+3] == symbol){
                    return true;
                }
            }
        }
        for (int col = 0; col <= 3; col++){
            for (int fila = 0; fila <= 2; fila++){
                if (grid[col][fila] == symbol && grid[col+1][fila+1] == symbol &&
                        grid[col+2][fila+2] == symbol && grid[col+3][fila+3] == symbol){
                    return true;
                }
            }
        }
        for (int col = 0; col <= 7 - 4; col++){
            for (int fila = 3; fila < 6; fila++){
                if (grid[col][fila] == symbol && grid[col+1][fila-1] == symbol &&
                        grid[col+2][fila-2] == symbol && grid[col+3][fila-3] == symbol){
                    return true;
                }
            }
        }
        return false;
    }

    public char getCurrentSymbol(){
        return currentSymbol;
    }

    public void printBoard(){
        for (int row = 0; row < 6; row++){
            for (int col = 0; col < 7; col++){
                System.out.print("|" + grid[col][row]);
            }
            System.out.println("|");
        }
        System.out.println("---------------------");
    }
}

class Game {
    private String status;
    private String winnerPlayerName;
    private String playerNameA;
    private String playerNameB;
    private ConnectFour connectFour;

    public Game(String playerNameA, String playerNameB){
        this.playerNameA = playerNameA;
        this.playerNameB = playerNameB;
        this.status = "IN_PROGRESS";
        this.winnerPlayerName = "";
        this.connectFour = new ConnectFour();
    }

    public String play() {
        Scanner scanner = new Scanner(System.in);
        boolean turnoA = true;

        while (true) {
            System.out.println("\nTablero actual:");
            connectFour.printBoard();

            String jugadorActual = turnoA ? playerNameA : playerNameB;
            System.out.println("Turno de: " + jugadorActual + " (" + connectFour.getCurrentSymbol() + ")");
            System.out.print("Ingrese columna (0-6): ");

            int columna;
            if (scanner.hasNextInt()){
                columna = scanner.nextInt();
                if (columna < 0 || columna > 6) {
                    System.out.println("Número fuera de rango. Intente nuevamente.");
                    scanner.nextLine();
                    continue;
                }
            } else{
                System.out.println("Entrada inválida, debe ingresar un número.");
                scanner.nextLine();
                continue;
            }

            if (!connectFour.makeMove(columna)){
                System.out.println("Movimiento inválido. Intente otra columna.");
                continue;
            }

            String resultado = connectFour.isGameOver();
            if (resultado.equals("WIN_X")){
                status = "VICTORY";
                winnerPlayerName = (resultado.equals("WIN_X")) ? playerNameA : playerNameB;
                System.out.println("\n¡Victoria para " + winnerPlayerName + "!");
                connectFour.printBoard();
                return winnerPlayerName;
            } else if (resultado.equals("WIN_O")){
                status = "VICTORY";
                winnerPlayerName = playerNameB;
                System.out.println("\n¡Victoria para " + winnerPlayerName + "!");
                connectFour.printBoard();
                return winnerPlayerName;
            } else if (resultado.equals("DRAW")){
                status = "DRAW";
                winnerPlayerName = "";
                System.out.println("\n¡El juego terminó en empate!");
                connectFour.printBoard();
                return "";
            }
            turnoA = !turnoA;
        }
    }

    public String getStatus() {
        return status;
    }

    public String getWinnerPlayerName() {
        return winnerPlayerName;
    }
}


class BST<Key extends Comparable<Key>, Value> {

    private class Node {
        Key key;
        Value val;
        Node left, right;

        Node(Key key, Value val) {
            this.key = key;
            this.val = val;
        }
    }

    private Node root;

    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    private Node put(Node node, Key key, Value val) {
        if (node == null) return new Node(key, val);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, val);
        else if (cmp > 0) node.right = put(node.right, key, val);
        else node.val = val;
        return node;
    }

    public Value get(Key key) {
        Node node = get(root, key);
        return node == null ? null : node.val;
    }

    private Node get(Node node, Key key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return get(node.left, key);
        else if (cmp > 0) return get(node.right, key);
        else return node;
    }

    public List<Key> keysInRange(Key lo, Key hi) {
        List<Key> result = new ArrayList<>();
        keysInRange(root, lo, hi, result);
        return result;
    }

    private void keysInRange(Node node, Key lo, Key hi, List<Key> result) {
        if (node == null) return;
        int cmpLo = lo.compareTo(node.key);
        int cmpHi = hi.compareTo(node.key);
        if (cmpLo < 0) keysInRange(node.left, lo, hi, result);
        if (cmpLo <= 0 && cmpHi >= 0) result.add(node.key);
        if (cmpHi > 0) keysInRange(node.right, lo, hi, result);
    }

    public Key successor(Key key) {
        Node current = root;
        Key successor = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                successor = current.key;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return successor;
    }
}
class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scoreboard scoreboard = new Scoreboard();

        System.out.println("Bienvenido a Conecta Cuatro");

        while (true) {
            System.out.println("\nIngrese nombre del jugador A:");
            String playerA = scanner.nextLine().trim();
            System.out.println("Ingrese nombre del jugador B:");
            String playerB = scanner.nextLine().trim();

            scoreboard.registerPlayer(playerA);
            scoreboard.registerPlayer(playerB);

            Game game = new Game(playerA, playerB);
            String winner = game.play();

            if (winner.isEmpty()) {
                scoreboard.addGameResult(playerA, playerB, true);
            } else {
                String loser = winner.equals(playerA) ? playerB : playerA;
                scoreboard.addGameResult(winner, loser, false);
            }

            System.out.println("\nEstadísticas:");
            for (String playerName : new String[]{playerA, playerB}) {
                Player p = scoreboard.getPlayer(playerName);
                System.out.printf("%s - Wins: %d, Draws: %d, Losses: %d, Win Rate: %.2f%%\n",
                        p.getName(), p.getWins(), p.getDraws(), p.getLosses(), p.winRate() * 100);
            }

            System.out.println("\n¿Desean jugar otra partida? (s/n):");
            String opcion = scanner.nextLine().trim().toLowerCase();
            if (!opcion.equals("s")) {
                System.out.println("¡Gracias por jugar! Hasta la próxima.");
                break;
            }
        }

        scanner.close();
    }
}

