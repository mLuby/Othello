// Othello Applet (graphics code adapted from Postfix Calculator Applet
// 12/M3/2007
// CS 201 Final Project  -  Kazuaki Okumura & Michael Luby
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class Othello extends Applet implements ActionListener {
//-----------------------------Initialization------------------------------------------
//***numRanks and numFiles control the dimensions of the gameboard
//***the four starting pieces are initialized in "initializeGame()"
//***the computer player can be set to black(1), white(2) or off(0)
//***by changing the integer value in "if(currentPlayer == 0)" in "placeTile()"
// instance variables
	Label player1sum;	// label used to show player 1's total tiles
	Label gameState;	// label used to show game state
	Label player2sum;	// label used to show player 2's total tiles
	int numRanks = 8;	// ranks are horizontal
	int numFiles = 8;	// files are vertical
	int currentPlayer; 	//1 = black, 2 = white	
	int boardArray[][] = new int[numRanks][numFiles];
	int tempBoard[][] = new int[numRanks][numFiles];
	boolean flippable;
	Panel centerPanel = new Panel();
// local color constants
	static final Color black = Color.black;
	static final Color white = Color.white;
	static final Color green = Color.green;
	static final Color dgreen = new Color(0, 120, 90);
//-------------------------------------------------------------------------------------
public void init() 
//code to set up the applet, including layout
    {
        setLayout(new BorderLayout());
        setBackground(black);   
        add("North", gameStatePanel());
        add("Center", board());
	initializeGame();
    }
//-------------------------------------------------------------------------------------
public Panel gameStatePanel()
//dynamic text fields that display the state of the game (in progress, black won,
// white won, draw) and sum of player 1's tiles, sum of player 2's tiles
    {
        Panel resultPanel = new Panel();
        resultPanel.setBackground(dgreen);
        resultPanel.setLayout(new GridLayout(1, 3));
	//displays the sum of player 1 (black)'s tiles in the upper left
        player1sum = new Label("Black: ", Label.LEFT);
        player1sum.setFont(new Font("TimesRoman", Font.BOLD, 30));
        player1sum.setBackground(white);
        player1sum.setForeground(black);
        resultPanel.add("", player1sum);
	//displays game state in upper center: in progress, black won, white won, or draw)
        gameState = new Label("Black's Turn", Label.CENTER);
        gameState.setFont(new Font("TimesRoman", Font.BOLD, 30));
        gameState.setBackground(dgreen);
        gameState.setForeground(green);
        resultPanel.add("", gameState);
	//displays the sum of player 2 (white)'s tiles in the upper right
        player2sum = new Label("White: ", Label.RIGHT);
        player2sum.setFont(new Font("TimesRoman", Font.BOLD, 30));
        player2sum.setBackground(black);
        player2sum.setForeground(white);
        resultPanel.add("", player2sum);
        return resultPanel;
    }
//-------------------------------------------------------------------------------------
public Panel board()
//creates the main board
    {
        centerPanel.setLayout(new GridLayout(numRanks, numFiles));
	for(int i = 0; i < numRanks; i++)
	{
		for(int j = 0; j < numFiles; j++)
		{
			boardArray[i][j] = 0;
			centerPanel.add(CButton(""+(i)+(j), dgreen, dgreen));
		}
	}
        return centerPanel;
    }
//-------------------------------------------------------------------------------------
public void initializeGame() 
//places the starting four pieces, and sets currentPlayer = 1 (black) 
	{
		currentPlayer = 1;
		boardArray[3][3] = 1;
		boardArray[3][4] = 2;
		boardArray[4][3] = 2;
		boardArray[4][4] = 1;
		sumTiles(1);
		sumTiles(2);
		rePaint();
	}
//-----------------------------Button Methods------------------------------------------
//-------------------------------------------------------------------------------------
public void actionPerformed(ActionEvent e)
//This method handles button clicks
    {
        if (e.getSource() instanceof Button)
        {
            String label = ((Button)e.getSource()).getLabel();
            	char c = label.charAt(0);
		char k = label.charAt(1);
		int rank = Character.getNumericValue(c);
		int file = Character.getNumericValue(k);
                if(boardArray[rank][file]==0)
		{
		placeTile(rank,file);
		}
        }
    }
//-------------------------------------------------------------------------------------

protected Button CButton(String s, Color fg, Color bg)
//this method creates colored buttons
    {
        Button b = new Button(s);
        b.setBackground(bg);
        b.setForeground(fg);
        b.addActionListener(this);
        return b;
    }
//-----------------------------Functionality Methods-----------------------------------
//-------------------------------------------------------------------------------------
public void placeTile(int rank, int file ) 
//places a tile (black or white) depending on the currentPlayer) 
//at position (rank, file), and toggles currentPlayer
	{
		if(moveExists(rank,file))
		{
			boardArray[rank][file] = currentPlayer;
			flipTilesFrom(rank, file);
			rePaint();
			sumTiles(currentPlayer);
			checkVictory();
			togglePlayer();
		}
		if(currentPlayer == 0)
		{ computerPlay(); }
	}
//-------------------------------------------------------------------------------------
public void rePaint()
    {
	for(int i = 0; i < numRanks; i++)
	    {
		for(int j = 0; j < numFiles; j++)
		    {
			if(boardArray[i][j]==1)
			{ centerPanel.getComponent(i*numRanks+j).setBackground(black);
			  centerPanel.getComponent(i*numRanks+j).setForeground(black); }
			if(boardArray[i][j]==2)
			{ centerPanel.getComponent(i*numRanks+j).setBackground(white);
			  centerPanel.getComponent(i*numRanks+j).setForeground(white);  }
		    }
	    }
    }
//-------------------------------------------------------------------------------------
public void togglePlayer()
{
	if( currentPlayer==1 )
	{
		currentPlayer = 2;
		gameState.setText("White's Turn");
	}
	else
	{
		currentPlayer = 1;
		gameState.setText("Black's Turn");
	}
}
//-------------------------------------------------------------------------------------
public void checkVictory() 
//if the game is over, declare a winner (or a draw)
{
		if( sumTiles(1)+sumTiles(2) == numRanks*numFiles )
		{//Game Over
			if(sumTiles(1)==sumTiles(2))
				{ gameState.setText("Draw..."); }
			else if(sumTiles(1)>sumTiles(2))
				{ gameState.setText("Black Wins!"); }
			else
				{ gameState.setText("White Wins!"); }
 		}
}
//-------------------------------------------------------------------------------------
public int sumTiles(int currentPlayer) 
//determine the total number of tiles of currentPlayer's color on the board
	{ 
		int playerSum = 0;
		for(int i = 0; i < numRanks; i++)
		{
			for(int j = 0; j < numFiles; j++)
			{
				if( boardArray[i][j] == currentPlayer )
					{ playerSum++; }
			}
		}
		if(currentPlayer == 1)
			{ player1sum.setText("Black: " + playerSum ); }
		else
			{ player2sum.setText("White: " + playerSum ); }
		return playerSum;
	}
//-------------------------------------------------------------------------------------
public boolean moveExists(int rank, int file) 
// checks to see if the currentPlayer has a valid move, IE placing a piece would
// result in at least one flip for currentPlayer
    { 
	for(int i = 0; i < numRanks; i++)
	{
		for(int j = 0; j < numFiles; j++)
		{ tempBoard[i][j] = boardArray[i][j]; }
	}
	flippable = false;
	flipTilesFrom(rank, file);
	for(int i = 0; i < numRanks; i++)
		{
			for(int j = 0; j < numFiles; j++)
			{ boardArray[i][j] = tempBoard[i][j]; }
		}
	if(flippable == true)
	{
		return true;
	}
	else
	{
		return false;
	}
    }
//-------------------------------------------------------------------------------------
public void computerPlay()
//searches (exhaustively) for greatest number of tiles flipped, then makes that move
{
	int originalSum = sumTiles(currentPlayer);
	int bestGain = 0; // best currentSum-originalSum
	int bestRank = 0; // best move rank
	int bestFile = 0; // best move file
	for(int i = 0; i < numRanks; i++)
	{
		for(int j = 0; j < numFiles; j++)
		{
			//create temp board from original board
			for(int k = 0; k < numRanks; k++)
			{
				for(int l = 0; l < numFiles; l++)
				{ tempBoard[k][l] = boardArray[k][l]; }
			}
			//hypothetical: more tiles flipped by playing here?
			flipTilesFrom(i,j);			
			if( sumTiles(currentPlayer)-originalSum > bestGain)
			{
				bestGain = sumTiles(currentPlayer)-originalSum;
				bestRank = i;
				bestFile = j;
			}
			//restore original board from temp board
			for(int m = 0; m < numRanks; m++)
			{
				for(int n = 0; n < numFiles; n++)
				{ boardArray[m][n] = tempBoard[m][n]; }
			}
		}
	}	
	placeTile(bestRank, bestFile);
}
//-------------------------------------------------------------------------------------
public void flipTilesFrom(int rank, int file) 
//toggles a tile's color at position (rank, file)
{
	int newRank;
	int newFile;
//Flip Eastward
	newRank = rank;
	newFile = file + 1;
	while(newRank!=numRanks && newFile!=numFiles && newRank!=-1 && newFile!=-1 
		&& boardArray[rank][newFile]!=currentPlayer && boardArray[rank][newFile]!= 0)
	{

		newFile=newFile+1;
		if(boardArray[rank][newFile]==currentPlayer)
		{
			for(int i = newFile-1; i != file; i--)
			{
				boardArray[rank][i] = currentPlayer;
				flippable = true;
			}
		}
	}
	
//Flip Southward
	newRank=rank+1;
	newFile=file;
	while(newRank!=numRanks && newFile!=numFiles && newRank!=-1 && newFile!=-1
		&& boardArray[newRank][file]!=currentPlayer && boardArray[newRank][file]!= 0)
	{
		newRank=newRank+1;
		if(boardArray[newRank][file]==currentPlayer)
		{
			for(int i = newRank-1; i != rank; i--)
			{ 
				boardArray[i][file] = currentPlayer;
				flippable = true;
			}
		}
	}
//Flip Westward
	newRank = rank;
	newFile = file - 1;
	while(newRank!=numRanks && newFile!=numFiles && newRank!=-1 && newFile!=-1
		&& boardArray[rank][newFile]!=currentPlayer && boardArray[rank][newFile]!= 0)
	{
		newFile=newFile-1;
		if(boardArray[rank][newFile]==currentPlayer)
		{
			for(int i = newFile+1; i != file; i++)
			{
				boardArray[rank][i] = currentPlayer;
				flippable = true;
			}
		}
	}
//Flip Northward
	newRank=rank-1;
	newFile=file;
	while(newRank!=numRanks && newFile!=numFiles && newRank!=-1 && newFile!=-1 
		&& boardArray[newRank][file]!=currentPlayer && boardArray[newRank][file]!= 0)
	{
		newRank=newRank-1;
		if(boardArray[newRank][file]==currentPlayer) //<-- Occasionally OutOfBounds
		{
			for(int i = newRank+1; i != rank; i++)
			{ 
				boardArray[i][file] = currentPlayer; 
				flippable = true;
			}
		}
	}
//Flip Southeastward
	//South East
	newRank = rank + 1;
	newFile = file + 1;
	while(newRank<numRanks && newFile<numFiles && newRank>=1 && newFile>=1 &&
		boardArray[newRank][newFile]!=currentPlayer && boardArray[newRank][newFile]!= 0)
	{
		newRank=newRank+1;
		newFile=newFile+1;
		gameState.setText("e@"+newRank+","+newFile);
		if(boardArray[newRank][newFile]==currentPlayer) //<-- Occasionally OutOfBounds
		{
			for(int i = newRank-1; i != rank; i--)
			{ 
			    	newFile = newFile-1;
				boardArray[i][newFile] = currentPlayer;
				flippable = true;
			}
		}
	}
//Flip Northeastward
	newRank = rank - 1;
	newFile = file + 1;
	while(newRank<numRanks && newFile<numFiles && newRank>=1 && newFile>=1 &&
		boardArray[newRank][newFile]!=currentPlayer && boardArray[newRank][newFile]!= 0)
	{
		newRank=newRank-1;
		newFile=newFile+1;
		gameState.setText("e@"+newRank+","+newFile);
		if(boardArray[newRank][newFile]==currentPlayer) //<-- Occasionally OutOfBounds
		{
			for(int i = newRank+1; i != rank; i++)
			{ 
			   	newFile = newFile-1;
				boardArray[i][newFile] = currentPlayer;
				flippable = true;
			}
		}
	}
//Flip Northwestward
	newRank = rank - 1;
	newFile = file - 1;
	while(newRank<numRanks && newFile<numFiles && newRank>=1 && newFile>=1 &&
		boardArray[newRank][newFile]!=currentPlayer && boardArray[newRank][newFile]!= 0)
	{
		newRank=newRank-1;
		newFile=newFile-1;
		gameState.setText("e@"+newRank+","+newFile);
		if(boardArray[newRank][newFile]==currentPlayer) //<-- Occasionally OutOfBounds
		{

			for(int i = newRank+1; i != rank; i++)
			{ 
			    	newFile = newFile+1;
				boardArray[i][newFile] = currentPlayer;
				flippable = true;
			}
		}
	}

//Flip Southwestward
	newRank = rank + 1;
	newFile = file - 1;
	while(newRank<numRanks && newFile<numFiles && newRank>=1 && newFile>=1 &&
		boardArray[newRank][newFile]!=currentPlayer && boardArray[newRank][newFile]!= 0)
	{
		newRank=newRank+1;
		newFile=newFile-1;
		gameState.setText("e@"+newRank+","+newFile);
		if(boardArray[newRank][newFile]==currentPlayer)
		{
			for(int i = newRank-1; i != rank; i--)
			{
				newFile = newFile+1;
				boardArray[i][newFile] = currentPlayer;
				flippable = true;
			}
		}
	}	
}
} //-----------------------------end-------------------------------------------
