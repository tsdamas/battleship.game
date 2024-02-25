/**
 * This file build a computer player for battleship game that make guess based on targetMode, huntMode and dyanamic probability board
 *
 * @author Thais Santos Damasceno
 * @version November 21, 2023
 * @class COSC 111 - 002
 */

 import java.util.Scanner;
 public class RandomGuesser{ 
     public static String makeGuess(char[][] guesses)
     {
 
         String guess = " "; 
         int[][] targetCoordinates = new int[10][10];
         int largest = - 1;
         int row = -1;
         int col = -1;
         int[][] heatMapCopy = new int[10][10];
         heatMapCopy = initialHeatMap(guesses); 
         
         for(int i = 0; i < guesses.length; i++){
             for(int j = 0; j < guesses[i].length; j++){
                 //targetMode activate it 
                 if(guesses[i][j] == 'X') {
                     targetCoordinates = targetHeatMap(i, j); 
                     //duplicate X found check horizontal
                     if(inBounds(i, j+1, guesses) && guesses[i][j+1] == 'X') {
 
                         if(inBounds(i, j+2, guesses) && guesses[i][j+2] == '.'){
                         return stringGuess(i,j+2);
                         }
                         if(inBounds(i,j-1, guesses) && guesses[i][j-1] == '.'){
                         return stringGuess(i, j-1); 
                         } 
                         if(inBounds(i, j-2, guesses) && guesses[i][j-2] == '.'){
                         return stringGuess(i,j-2);
                         } 
                     }
                     //duplicate X found check vertical 
                     if(inBounds(i+1, j, guesses) && guesses[i+1][j] == 'X') {
 
                         if(inBounds(i+2, j, guesses) && guesses[i+2][j] == '.'){
                             return stringGuess(i+2,j);
                         }
                         if(inBounds(i-1,j, guesses) && guesses[i-1][j] == '.'){
                             return stringGuess(i-1, j); 
                         } 
                         if(inBounds(i-2, j, guesses) && guesses[i-2][j] == '.'){
                             return stringGuess(i-2,j);
                         } 
                     }
                     
                     
                     
                     //Hit found. Let's check the sorroundings. 
                     if(inBounds(i-1,j, guesses) && targetCoordinates[i-1][j] >= 4 && guesses[i-1][j] == '.' &&
                         guesses[i-1][j] != '5' && guesses[i-1][j] != '4' && guesses[i-1][j] != '3' &&
                         guesses[i-1][j] != '2' && guesses[i-1][j] != '1'){
                         return stringGuess(i-1,j); 
                     } 
                     if(inBounds(i+1,j, guesses) && targetCoordinates[i+1][j] >= 4 && guesses[i+1][j] == '.' && 
                        guesses[i+1][j] != '5' && guesses[i+1][j] != '4' && guesses[i+1][j] != '3' 
                        && guesses[i+1][j] != '2' && guesses[i+1][j] != '1'){
                         return stringGuess(i+1,j); 
                     } 
                     if(inBounds(i,j-1, guesses) && targetCoordinates[i][j-1] >= 4 && guesses[i][j-1] == '.' && 
                       guesses[i][j-1] != '5' && guesses[i][j-1] != '4' && guesses[i][j-1] != '3' && 
                       guesses[i][j-1] != '2' && guesses[i][j-1] != '1' ){
                         return stringGuess(i,j-1); 
                     } 
                     if(inBounds(i, j+1, guesses) && targetCoordinates[i][j+1] >= 4 && guesses[i][j+1] == '.' && 
                        guesses[i][j+1] != '5' && guesses[i][j+1] != '4' && guesses[i][j+1] != '3' &&
                       guesses[i][j+1] != '2' && guesses[i][j+1] != '1'){
                         return stringGuess(i,j+1); 
                     }
                     
                 }
             }
         }
         
         
         //Start with huntMode
         //No X found in the board. Let's come back to HuntMode.  
         //Guess in the middle first where the probability is higher
         for(int i = 0; i < heatMapCopy.length; i++){
             for(int j = 0; j < heatMapCopy[i].length; j++){
                 if(heatMapCopy[i][j] > largest){
                     largest = heatMapCopy[i][j];
                     row = i;
                     col = j;
                 }
             }
         }
         if(guesses[row][col] == '.'){
             return stringGuess(row,col); 
         }
 
         return guess; 
 
     }
     
     public static int[][] initialHeatMap(char[][] currentMap){
         int[][] initialHeatMapBoard = new int[10][10];
         //Heat map towards the center of the board
         //Create a nested loop and some inner loops checking for each boat length: 2, 3, 4 and 5
         //Check if you are in bounds
         //Check if your boat fit from i, j to the length
         //When we start the game first guess should be towards the center as they have more probility of having boats
         //As you guess in the center, if you start getting misses, the heatMap updates to decrease the probability in the sorrounding areas
         //Time to change areas with high probability 
         int weight = 5;
 
         // Assign probabilities based on the strategy considering a ship of length 2
         for(int i = 0; i < initialHeatMapBoard.length; i++){
             //int current = weight; 
             for(int j = 0; j < initialHeatMapBoard[i].length; j++){
                 
                 //fit a patrol horizontally
                 if(inBounds(i, j+1, initialHeatMapBoard) && currentMap[i][j+1] == '.' && currentMap[i][j] == '.'){
                     for(int k = j; k <= j+1; k++){
                         initialHeatMapBoard[i][k]++; 
                     }
                 }
                 
                 //fit a patrol vertically
                 if(inBounds(i+1, j, initialHeatMapBoard) && currentMap[i+1][j] == '.' && currentMap[i][j] == '.'){
                     for(int k = i; k <= i+1; k++){
                         initialHeatMapBoard[k][j]++; 
                     }
                 }
                 
                 //fit a submarine horizontally
                 if(inBounds(i, j+2, initialHeatMapBoard) && currentMap[i][j+2] == '.' && currentMap[i][j+1] == '.'  && currentMap[i][j] == '.'){
                     for(int k = j; k <= j+2; k++){
                         initialHeatMapBoard[i][k]++; 
                     }
                 }
                 
                 //fit a submarine vertically
                 if(inBounds(i+2, j, initialHeatMapBoard) && currentMap[i+2][j] == '.' && currentMap[i+1][j] == '.' && currentMap[i][j] == '.'){
                     for(int k = i; k <= i+2; k++){
                         initialHeatMapBoard[k][j]++; 
                     }
                 }
                 
                 //fit a destroyer horizontally
                 if(inBounds(i, j+2, initialHeatMapBoard) && currentMap[i][j+2] == '.' && currentMap[i][j+1] == '.'&& currentMap[i][j] == '.'){
                     for(int k = j; k <= j+2; k++){
                         initialHeatMapBoard[i][k]++; 
                     }
                 }
                 
                 //fit a destroyer vertically
                 if(inBounds(i+2, j, initialHeatMapBoard) && currentMap[i+2][j] == '.' && currentMap[i+1][j] == '.' && currentMap[i][j] == '.'){
                     for(int k = i; k <= i+2; k++){
                         initialHeatMapBoard[k][j]++; 
                     }
                 }
                 
                 //fit a Battlleship horizontally
                 if(inBounds(i, j+3, initialHeatMapBoard) && currentMap[i][j+3] == '.' && currentMap[i][j+2] == '.' &&
                    currentMap[i][j+1] == '.' && currentMap[i][j] == '.'){
                     for(int k = j; k <= j+3; k++){
                         initialHeatMapBoard[i][k]++; 
                     }
                 }
                 
                 //fit a Battlleship vertically
                 if(inBounds(i+3, j, initialHeatMapBoard) && currentMap[i+3][j] == '.' && currentMap[i+2][j] == '.' &&
                    currentMap[i+1][j] == '.' && currentMap[i][j] == '.'){
                     for(int k = i; k <= i+3; k++){
                         initialHeatMapBoard[k][j]++; 
                     }
                 }
                 
                 //fit a aircraft horizontally
                 if(inBounds(i, j+4, initialHeatMapBoard) && currentMap[i][j+4] == '.' && currentMap[i][j+3] == '.' &&
                     currentMap[i][j+2] == '.' && currentMap[i][j+1] == '.' && currentMap[i][j] == '.'){
                     for(int k = j; k <= j+4; k++){
                         initialHeatMapBoard[i][k]++; 
                     }
                 }
                 
                 //fit a aircraft vertically
                 if(inBounds(i+4, j, initialHeatMapBoard) && currentMap[i+4][j] == '.' && currentMap[i+3][j] == '.' &&
                     currentMap[i+2][j] == '.' && currentMap[i+1][j] == '.' && currentMap[i][j] == '.'){
                     for(int k = i; k <= i+4; k++){
                         initialHeatMapBoard[k][j]++; 
                     }
                 }
             }
         }
         return initialHeatMapBoard; 
         
     }
     
     public static int[][] targetHeatMap (int row, int col) {
         int[][] heatMap = new int[10][10];
         
         for(int i = 0; i < heatMap.length; i++) {
             for(int j = 0; j < heatMap[i].length; j++){
                 heatMap[i][j] = 1; 
             }
         }
         
         
         for(int i = row - 2; i < row + 2; i++) {
             for(int j = col - 2; j < col + 2; j++){
                if(inBounds(row-1,col,heatMap)){
                  heatMap[row-1][col]++;  
                }
                 if(inBounds(row+1,col,heatMap)){
                  heatMap[row+1][col]++;  
                }
                 if(inBounds(row,col-1,heatMap)){
                  heatMap[row][col-1]++;  
                }
                 if(inBounds(row,col+1,heatMap)){
                  heatMap[row][col+1]++;  
                }
             }
         } 
         return heatMap;
     }
     
     public static void incrementProbability(int[][] heatMap, int row, int col){
         if(inBounds(row,col,heatMap)){
             heatMap[row][col]++;   
         }
     }
     
     public static String stringGuess(int row, int col){
         String guess = " ";     
         char a = (char)((int)'A' + row);
         guess =  a + Integer.toString(col+1);
         return guess; 
     }
 
     public static boolean inBounds(int row, int col, char[][] board){
         return row >= 0 && col >=0 && row < board.length && col < board[0].length; 
     }
     
         public static boolean inBounds(int row, int col, int[][] board){
         return row >= 0 && col >=0 && row < board.length && col < board[0].length; 
     }
 
 }