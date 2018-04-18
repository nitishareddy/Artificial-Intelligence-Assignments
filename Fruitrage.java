/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fruitrage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author nitishareddy
 */
public class Fruitrage {

    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
              try{
                FileInputStream in = new FileInputStream("src/fruitrage/input.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter("src/fruitrage/output.txt"));
                String line1 = br.readLine();
		String line2 = br.readLine();
		int n = Integer.parseInt(line1);
                int p = Integer.parseInt(line2);
                char[][] board = new char[n][n];
                       // for (int i = 0; i < n; i++) {
                            String line = br.readLine();

                            //for (int j = 0; j < n; j++) {
                               while (line != null)
                        {
                           for (int i = 0; i < 15; i++)
                           {
                               board[i] = line.toCharArray();
                           }
                        }
                              //board[i][j] = String.valueOf(line.charAt(j));
                            
    
			
                        /*for (int i = 0; i < n; i++) {
				String s = br.readLine();
				for (int j = 0; j < n; j++) {
					board[i][j] = s.charAt(j) - '0';
				}

			}*/
                        
                        Board.GamePlay(board, row, col, depth, turn);
                        bw.write("\n");
               
                        in.close();
			bw.close();

              
              } catch (IOException | NumberFormatException e) {
			System.out.print(e);
		}
              
	}
}       
      

class Positions{
    
    int row, col;

    public Positions(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    
}

class PositionsAndScores {

    int score;
    Positions position;

    PositionsAndScores(int score, Positions position) {
        this.score = score;
        this.position = position;
    }
}

class Board{
    
    static List<PositionsAndScores> rootsChildrenScore = new ArrayList<>();
    
    public static int fillStars(int[][] board, int i, int j){
    
    int n = board[0].length;
        
        board[i][j] = '*';
        int count = 1;
        
        if(board[i][j] == board[i][j+1] && j < n-1){
            board[i][j+1] = '*';
        count += fillStars(board, i, j+1);
        }
        if(board[i][j] == board[i][j-1] && j >= 0){
            board[i][j-1] = '*';
        count += fillStars(board, i, j-1);
        }
        if(board[i][j] == board[i-1][j] && i >= 0){
            board[i-1][j] = '*';
        count += fillStars(board, i-1, j);
        }
        if(board[i][j] == board[i+1][j] && i < n-1){
            board[i+1][j] = '*';
        count += fillStars(board, i+1, j);
        }   
            
    return count;
}
    
    public static boolean ifFruitsPresent(int[][] board){
        
        int n = board[0].length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(board[i][j] == '*'){
                    
                    for(int k = i; k > -1; k--){
                        
                        if(board[k][j] != '*')
                            return true;
                     
                    }
                    
                    
                }
                
                
                
            }
            
            
        }
        
       return false; 
     
    }
    
public static void changeBoard(int[][] board){
    int n = board[0].length;
    while(ifFruitsPresent(board)){
        // start from the lowest row for each column
        for(int i = 0; i < n-1; i++){
            for(int j = 0; j < n; j++){
                if(board[i+1][j] == '*'){
                        
                        board[i+1][j] = board[i][j];
                        board[i][j] = '*';
                    }
                
                
            }
        }
      
    }
  
}    

public static List<Positions> getAvailableStates(int[][] board) {
        int n = board[0].length;
        ArrayList<Positions> availablePositions = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (board[i][j] == '*') {
                    availablePositions.add(new Positions(i, j));
                }
            }
        }
        return availablePositions;
    }

public static void placeAMove(int[][] board, Positions position, int player){
    
    board[position.row][position.col] = player;   //player = 1 for Max, 2 for Min
    
    
}

public Positions returnBestMove() {
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < rootsChildrenScore.size(); ++i) {
            if (MAX < rootsChildrenScore.get(i).score) {
                MAX = rootsChildrenScore.get(i).score;
                best = i;
            }
        }

        return rootsChildrenScore.get(best).position;
    }

public static boolean isGameOver(int[][] board){
    int n = board[0].length;
    for(int i = 0; i < n; i++){
        for(int j = 0; j < n; j++){
            if(board[i][j] == '*')
                return true;
        }
    }
    
    return false;
}





public static int alphaBetaMinimax(int[][] board,int row, int col, int depth, int turn){
    int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
    if(beta<=alpha){ 
       // System.out.println("Pruning at depth = "+depth);
    if(turn == 1) 
            return Integer.MAX_VALUE; 
    
        else 
            return Integer.MIN_VALUE; 
    }
    
    if(isGameOver(board)) // put while loop here
        return fillStars(board, row, col);
    
    
     List<Positions> positionsAvailable = getAvailableStates(board);// not needed
        
        //if(positionsAvailable.isEmpty()) return 0;
        
        if(depth==0) rootsChildrenScore.clear(); 
        
        int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;
    
    for(int x = 0; x < positionsAvailable.size(); ++x){
            Positions position = positionsAvailable.get(x);
            
            int currentScore = 0;
            
            if(turn == 1){
                placeAMove(board, position, 1); 
                currentScore = alphaBetaMinimax(board, row, col, depth+1, 2);
                maxValue = Math.max(maxValue, currentScore); 
                
                //Set alpha
                alpha = Math.max(currentScore, alpha);
                
                if(depth == 0)
                    rootsChildrenScore.add(new PositionsAndScores(currentScore, position));
            }else if(turn == 2){
                placeAMove(board, position, 2);
                currentScore = alphaBetaMinimax(board, row, col, depth+1, 1); 
                minValue = Math.min(minValue, currentScore);
                
                //Set beta
                beta = Math.min(currentScore, beta);
            }
            //reset board
            //board[position.row][position.col] = 0; 
            
            //If a pruning has been done, don't evaluate the rest of the sibling states
            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) 
                break;
        }
        return turn == 1 ? maxValue : minValue;
    
}

public static void generateTree(int[][] board, int depth, int turn){
       
        int n = board.length;
        //board = new int[n][n];
        Stack<int[][]> st = new Stack<>();
        st.push(board);
       
        while(!st.empty()){
            
            int[][] current = st.peek();
            st.pop();
            //for(int row = n-1; row >= 0; row--){
              //  for(int col = n-1; col >= 0; col--){
            for( int m = 0; m < n*n; m++){
            
                int row = m / n;
                int col = m % n;
                        if(current[row][col] != '*'){
                        int[][] temp = new int[n][n];
                        //for(int k = 0; k < n; k++)
                         //temp[k] = new int[n];
                        for(int x = 0; x < n; x++)
                            for(int y = 0; y < n; y++)
                                temp[x][y] = current[x][y];
                            
                        int number = fillStars(board, row, col);
                       
                        temp.score = number*number;
                        temp.x = row;
                        temp.y = col;
                        changeBoard(board);
                        st.push(temp);
                       alphaBetaMinimax(board, row, col, depth, turn);
                    }
            }
          
        }
    }
}


/*public static void GamePlay(int[][] board, int row, int col, int depth, int turn){
generateTree(board);
//alphaBetaMinimax(board, row, col, depth, turn);

}*/



