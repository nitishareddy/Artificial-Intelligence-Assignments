/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package homework1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class homework1 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		try {
			FileInputStream in = new FileInputStream("input.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));

			String line1 = br.readLine();
			String line2 = br.readLine();
			int n = Integer.parseInt(line2);
			String line3 = br.readLine();
			int p = Integer.parseInt(line3);

			int[][] zoo = new int[n][n];

			for (int i = 0; i < n; i++) {
				String s = br.readLine();
				for (int j = 0; j < n; j++) {
					zoo[i][j] = s.charAt(j) - '0';
				}

			}

			boolean foundSolution = false;
			if (line1.equals("DFS")) {
				foundSolution = DFS.arrangeLizards(zoo, p);
			} else if (line1.equals("BFS")) {
				foundSolution = BFS.arrangeLizards(zoo, p);
			}
                        

			if (foundSolution) {
				bw.write("OK\n");
				outputZoo(bw, zoo);
			} else {
				bw.write("FAIL\n");
			}

			in.close();
			bw.close();

		} catch (IOException | NumberFormatException e) {
			System.out.print(e);
		}

	}

	private static void outputZoo(BufferedWriter bw, int[][] zoo) throws IOException {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < zoo[0].length; row++) {
			for (int col = 0; col < zoo[0].length; col++) {
				sb.append(zoo[row][col]);
			}
			sb.append("\n");
		}
		bw.write(sb.toString());
	}
}

class BFS {
	private static HashMap<int[][], Integer> map = new HashMap<>();

	public static boolean arrangeLizards(int[][] zoo, int p) {

		// get all combinations for first queen
		ArrayList<int[][]> firstQueenPossibilities = getCombinationsForNextQueen(zoo);

		while (!firstQueenPossibilities.isEmpty()) {
			ArrayList<int[][]> queue = new ArrayList<>();
			int[][] curBoardWithOneQueen = firstQueenPossibilities.remove(0);
			queue.add(curBoardWithOneQueen);
			int numQueensPlaced = 1;

			while (!queue.isEmpty()) {
				ArrayList<int[][]> tempQueue = new ArrayList<>();
				for (int[][] curZoo : queue) {
					if (numQueensPlaced + 1 == p) {
						int[][] tmp = placeQueen(curZoo);
						if (tmp != null) {
							for (int r = 0; r < tmp.length; r++) {
								for (int c = 0; c < tmp[0].length; c++) {
									zoo[r][c] = tmp[r][c];
								}
							}
							return true;
						}
					} else {
						tempQueue.addAll(getCombinationsForNextQueen(curZoo));
					}
				}
				map.remove(tempQueue);
				++numQueensPlaced;
				queue = tempQueue;
			}
			map.remove(curBoardWithOneQueen);
		}
		return false;
	}

	private static int[][] placeQueen(int curZoo[][]) {
		int n = curZoo[0].length;
		int startPos = 0;
		if (map.get(curZoo) != null) {
			startPos = map.get(curZoo);
		} else {
			System.out.println("-------");
		}
		for (int i = startPos; i < n * n; i++) {
			int row = i / n;
			int col = i % n;
			if (Utils.noConflict(curZoo, row, col)) {
				int[][] tmp = new int[n][n];
				// create new copy
				for (int r = 0; r < curZoo.length; r++) {
					for (int c = 0; c < curZoo[0].length; c++) {
						tmp[r][c] = curZoo[r][c];
					}
				}
				// place queen
				tmp[row][col] = 1;
				return tmp;
			}
		}

		return null;
	}

	private static ArrayList<int[][]> getCombinationsForNextQueen(int curZoo[][]) {
		ArrayList<int[][]> possibilities = new ArrayList<>();
		int n = curZoo[0].length;

		int startPos = 0;
		if (map.get(curZoo) != null) {
			startPos = map.get(curZoo);
		}

		for (int i = startPos; i < n * n; i++) {
			int row = i / n;
			int col = i % n;
			if (Utils.noConflict(curZoo, row, col)) {
				int[][] tmp = new int[n][n];
				// create new copy
				for (int r = 0; r < curZoo.length; r++) {
					for (int c = 0; c < curZoo[0].length; c++) {
						tmp[r][c] = curZoo[r][c];
					}
				}
				// place queen
				tmp[row][col] = 1;
				possibilities.add(tmp);
				map.put(tmp, i);
			}
		}
		return possibilities;
	}
}

class DFS {

	public static boolean arrangeLizards(int[][] zoo, int p) {
		return arrangeEveryLizard(zoo, 0, p);
	}

	private static boolean arrangeEveryLizard(int zoo[][], int nextPos, int remLizards) {
		if (remLizards == 0) {
			return true;
		}

		boolean isEveryLizardPlaced = false;
		int n = zoo[0].length;

		for (int i = nextPos; i < n * n; i++) {
			int row = i / n;
			int col = i % n;

			if (Utils.noConflict(zoo, row, col)) {
				zoo[row][col] = 1;
				isEveryLizardPlaced = arrangeEveryLizard(zoo, nextPos + 1, remLizards - 1);

				if (isEveryLizardPlaced) {
					return true;
				} else {
					if (zoo[row][col] != 2) {
						zoo[row][col] = 0;
					}
				}
			}
		}
		return false;
	}
}

class Utils {
	public static boolean noConflict(int zoo[][], int row, int col) {
		if (zoo[row][col] == 2 || zoo[row][col] == 1)
			return false;

		int numRows = zoo.length;
		int numCols = zoo[0].length;

		// Check Right Upper Diagonal
		for (int i = row - 1, j = col + 1; i >= 0 && j < numCols && zoo[i][j] != 2; i--, j++) {
			if (zoo[i][j] == 1) {
				return false;
			}
		}

		// Check Right Lower Diagonal
		for (int i = row + 1, j = col + 1; i < numRows && j < numCols && zoo[i][j] != 2; i++, j++) {
			if (zoo[i][j] == 1) {
				return false;
			}
		}

		// Check left upper diagonal
		for (int i = row - 1, j = col - 1; i >= 0 && j >= 0 && zoo[i][j] != 2; i--, j--) {
			if (zoo[i][j] == 1) {
				return false;
			}
		}

		// Check left lower diagonal
		for (int i = row + 1, j = col - 1; i < numRows && j >= 0 && zoo[i][j] != 2; i++, j--) {
			if (zoo[i][j] == 1) {
				return false;
			}
		}

		// Check left
		for (int j = col - 1; j >= 0 && zoo[row][j] != 2; j--) {
			if (zoo[row][j] == 1) {
				return false;
			}
		}

		// Check right
		for (int j = col + 1; j < numCols && zoo[row][j] != 2; j++) {
			if (zoo[row][j] == 1) {
				return false;
			}
		}

		// Check up
		for (int i = row - 1; i >= 0 && zoo[i][col] != 2; i--) {
			if (zoo[i][col] == 1) {
				return false;
			}
		}

		// Check down
		for (int i = row + 1; i < numRows && zoo[i][col] != 2; i++) {
			if (zoo[i][col] == 1) {
				return false;
			}
		}

		return true;
	}
}

                        repeating = false;
		
	}
        return false;
        }
        public boolean Annealing(int[][] zoo, int p){
        DFS d = new DFS();
        d.arrangeLizards(zoo, p);
            return true;
        }
	
	public int countConflicts(int p, int n, int[][] zoo, int[][] current)
	{

    		int numConflicts = 0;
         	
            	for(int k1 = 0; k1 < n; k1++){
                    for(int k2 = 0; k2 < n; k2++){
                        if(zoo[k1][k2]==1)
                    {
			if(findConflicts(n, k1, k2, zoo, current))
                        {
                            numConflicts++;
                        }
                    
                    } 
                    }
                }
                     return numConflicts;
        }       
                    
            public boolean findConflicts(int n,int k1, int k2, int[][] zoo, int[][] current)
            {
                    
                    int row = k1;
                    int col = k2;
		// Check Right Upper Diagonal
		for (int i = row - 1, j = col + 1; i >= 0 && j < n && zoo[i][j] != 2; i--, j++) {
			if (zoo[i][j] == 1) {
				 return false;
			}
		}

		// Check Right Lower Diagonal
		for (int i = row + 1, j = col + 1; i < n && j < n && zoo[i][j] != 2; i++, j++) {
			if (zoo[i][j] == 1) {
				return false;
			}
		}

		// Check left upper diagonal
		for (int i = row - 1, j = col - 1; i >= 0 && j >= 0 && zoo[i][j] != 2; i--, j--) {
			if (zoo[i][j] == 1) {
				 return false;
			}
		}

		// Check left lower diagonal
		for (int i = row + 1, j = col - 1; i < n && j >= 0 && zoo[i][j] != 2; i++, j--) {
			if (zoo[i][j] == 1) {
				return false;
			}
		}

		// Check left
		for (int j = col - 1; j >= 0 && zoo[row][j] != 2; j--) {
			if (zoo[row][j] == 1) {
				return false;
			}
		}

		// Check right
		for (int j = col + 1; j < n && zoo[row][j] != 2; j++) {
			if (zoo[row][j] == 1) {
				return false;
			}
		}

		// Check up
		for (int i = row - 1; i >= 0 && zoo[i][col] != 2; i--) {
			if (zoo[i][col] == 1) {
				return false;
			}
		}

		// Check down
		for (int i = row + 1; i < n && zoo[i][col] != 2; i++) {
			if (zoo[i][col] == 1) {
				return false;
			}
		}
            
		return true;
                
	}
}
