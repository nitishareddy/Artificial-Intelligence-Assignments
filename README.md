# Artificial-Intelligence-NLizards

Project description
You are a zookeeper in the reptile house. One of your rare South Pacific Tufted Wizzo lizards (Tufticus Wizzocus) has just had several babies. Your job is to find a place to put each baby lizard in a nursery.
However, there is a catch, the baby lizards have very long tongues. A baby lizard can shoot out its tongue and eat any other baby lizard before you have time to save it. As such, you want to make sure that no baby lizard can eat another baby lizard in the nursery (burp).
For each baby lizard, you can place them in one spot on a grid. From there, they can shoot out their tongue up, down, left, right and diagonally as well. Their tongues are very long and can reach to the edge of the nursery from any location.
Figure 1 shows in what ways a baby lizard can eat another.
 Figure 1 (A) the baby lizard can attack any other lizard in a red square. Thus it can be seen that a baby lizard can eat another lizard to its top, bottom, left right or diagonal. (B) In this example setup, both lizards can eat each other. Your algorithm will try to avoid this.
In addition to baby lizards, your nursery may have some trees planted in it. Your lizards cannot shoot their tongues through the trees nor can you move a lizard into the same place as a tree. As such, a tree will block any lizard from eating another lizard if it is in the path. Additionally, the tree will block you from moving the lizard to that location.
Figure 2 shows some different valid arrangements of lizards.
Figure 2 Both nurseries have valid arrangements of baby lizards such that they cannot eat one another. (A) with no trees, no lizard is in a position to eat another lizard. (B) Two trees are introduced such that the lizard in the last column cannot eat the lizard in the second or fourth column.
 
You will write a program that will take an input file that has an arrangement of trees and will output a new arrangement of lizards (and trees; as already mentioned, you can’t move the trees) such that no baby lizard can eat another one. You will be required to create a program that finds the solution. To find the solution you will use the following algorithms:
- Breadth-first search (BFS)
- Depth-first search (DFS)
- Simulated annealing (SA).
Input: The file input.txt in the current directory of your program will be formatted as follows:
First line: Second line: Third line: Next n lines:
instruction of which algorithm to use: BFS, DFS or SA
strictly positive 32-bit integer n, the width and height of the square nursery strictly positive 32-bit integer p, the number of baby lizards
the n x n nursery, one file line per nursery row (to show you where the trees are). It will have a 0 where there is nothing, and a 2 where there is a tree.
So for instance, an input file arranged like figure 2B (but with no lizards yet) and requesting you to use the DFS algorithm to place 8 lizards in the 8x8 nursery would look like:
DFS
8
8 00000000 00000000 00000000 00002000 00000000 00000200 00000000 00000000
Output: The file output.txt which your program creates in the current directory should be formatted as follows:
First line: OK or FAIL, indicating whether a solution was found or not. If FAIL, any following lines are ignored.
Next n lines: the n x n nursery, one line in the file per nursery row, including the baby lizards and trees. It will have a 0 where there is nothing, a 1 where you placed a baby lizard, and a 2 where there is a tree.
For example, a correct output.txt for the above sample input.txt (and matching Figure 2B) is:

OK 00000100 10000000 00001000 01002001 00000000 00100200 00000010 00010000


#Artificial-Intelligence-FruitRage


Project description
This project will provide you an opportunity to practice what you have learned about Game Playing in the class. In a typical zero sum two player game, players are generally competing for a certain common resource, and their gain is a function of their share of the resource. Often players have other challenges such as satisfying other constraints on other personal resources such as time, energy or computational power in the course of the game. In this homework we will introduce The Fruit Rage! a game that captures the nature of a zero sum two player game with strict limitation on allocated time for reasoning.
Your task is creating a software agent that can play this game against a human or another agent.
Rules of the game
The Fruit Rage is a two player game in which each player tries to maximize his/her share from a batch of fruits randomly placed in a box. The box is divided into cells and each cell is either empty or filled with one fruit of a specific type.
At the beginning of each game, all cells are filled with fruits. Players play in turn and can pick a cell of the box in their own turn and claim all fruit of the same type, in all cells that are connected to the selected cell through horizontal and vertical paths. For each selection or move the agent is rewarded a numeric value which is the square of the number of fruits claimed in that move. Once an agent picks the fruits from the cells, their empty place will be filled with other fruits on top of them (which fall down due to gravity), if any. In this game, no fruit is added during game play. Hence, players play until all fruits have been claimed.
Another big constraint of this game is that every agent has a limited amount of time to spend for thinking during the whole game. Spending more than the original allocated time will be penalized harshly. Each player is allocated a fixed total amount of time. When it is your turn to play, you will also be told how much remaining time you have. The time you take on each move will be subtracted from your total remaining time. If your remaining time reaches zero, your agent will automatically lose the game. Hence you should think about strategies for best use of your time (spend a lot of time on early moves, or on later moves?)
The overall score of each player is the sum of rewards gained for every turn. The game will terminate when there is no fruit left in the box or when a player has run out of time.

End of game: when the board is empty (no more fruits), the game is over. The score of each agent is the total points accumulated during the whole game (the sum of [fruits taken on each move]^2). The agent with the highest score wins. If both agents scored the same, the one with the most remaining time wins (to avoid draws during the competition). If remaining times are also exactly equal (highly unlikely), discard this run and do a new one with a different initial condition.




#Aritificial-Intelligence-Inference

Project description
After the high-profile lawsuit in which you succeeded to bring RealityMan to justice after proving that distributing his Nintendo emulator was a criminal act, everyone wants to hire you! From disputes over tech patents, to lawsuits on questions of privacy in social media, to suits on liability issues with self-driving cars, to disputes between Hollywood celebrities and their agents or producers, you are just running out of time and energy to run all these cases by hand like we have done in the lectures.
Because of the highly sensitive nature of the cases you handle, and because of the extremely high monetary amounts involved, you cannot trust any existing. You thus decide to create your own, ultra-optimized inference engine.
After much debating, you decide that the knowledge bases which you will create to handle each case will contain sentences with the following defined operators:
NOT X ~X
X OR Y X | Y
Your assignment is:
You will use first-order logic resolution to solve this problem.
Format for input.txt:
 <NQ = NUMBER OF QUERIES>
<QUERY 1>
...
<QUERY NQ>
<NS = NUMBER OF GIVEN SENTENCES IN THE KNOWLEDGE BASE>
<SENTENCE 1>
...
<SENTENCE NS>
where
• Each query will be a single literal of the form Predicate(Constant) or ~Predicate(Constant).
• Variables are all single lowercase letters.
• All predicates (such as Sibling) and constants (such as John) are case-sensitive alphabetical strings that
begin with an uppercase letter.
• Each predicate takes at least one argument. Predicates will take at most 100 arguments. A given
predicate name will not appear with different number of arguments.
• There will be at most 100 queries and 1000 sentences in the knowledge base.
• See the sample input below for spacing patterns.
• You can assume that the input format is exactly as it is described. There will be no syntax errors in the
given input.
Format for output.txt:
For each query, determine if that query can be inferred from the knowledge base or not, one query per line:
<ANSWER 1>
...
<ANSWER NQ>
where
each answer should be either TRUE if you can prove that the corresponding query sentence is true given the knowledge base, or FALSE if you cannot.


