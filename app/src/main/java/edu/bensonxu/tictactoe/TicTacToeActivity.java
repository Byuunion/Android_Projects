package edu.bensonxu.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.widget.Toast;

// import android.media.audiofx;

import java.util.Random;

public class TicTacToeActivity extends AppCompatActivity {

	// Buttons making up the board
	private Button mBoardButtons[];

	// Represents the internal state of the game
	private TicTacToeGame mGame;

	// Various text displayed
	private TextView mInfoTextView;
	private TextView mhwinTextView;
	private TextView mtieTextView;
	private TextView mawinTextView;

	private Random mRand = new Random();

	char mTurn;

	int mHumanWins = 0;
	int	mComputerWins = 0;
	int	mTies = 0;

	static final int DIALOG_DIFFICULTY_ID = 0;
	static final int DIALOG_QUIT_ID = 1;
	static final int DIALOG_ABOUT_ID = 2;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d("TicTacToeActivity", "Entered onCreate Method");
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);

		mInfoTextView = (TextView) findViewById(R.id.information);
		mhwinTextView = (TextView) findViewById(R.id.hwin);
		mtieTextView = (TextView) findViewById(R.id.tie);
		mawinTextView = (TextView) findViewById(R.id.awin);

		mhwinTextView.append(String.valueOf(mHumanWins));
		mtieTextView.append(String.valueOf(mTies));
		mawinTextView.append(String.valueOf(mComputerWins));

        mGame = new TicTacToeGame();

		// Choose who goes first
		int x = mRand.nextInt(2);
		if(x == 0) {
			mTurn = 'X';
			mInfoTextView.setText(R.string.turn_human);
		}
		else {
			mTurn = 'O';
			mInfoTextView.setText(R.string.first_computer);
			int location = mGame.getComputerMove();
			mGame.setMove(mTurn,location);
			setMove(mTurn,location);
		}

        startNewGame();

		Log.d("TicTacToeActivity", "Completed onCreate Method");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.menu.option_menu, menu);
		 Log.d("TicTacToeActivity", "Completed onCreateOptionsMenu Method");
         return true;
    }

	@Override
	protected Dialog onCreateDialog(int id)
		{Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
			case DIALOG_DIFFICULTY_ID:
				builder.setTitle(R.string.difficulty_choose);
				final CharSequence[] levels =
						{getResources().getString(R.string.difficulty_easy),
						getResources().getString(R.string.difficulty_harder),
						getResources().getString(R.string.difficulty_expert)};

				int selected = -1;
				if (mGame.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Easy)
					selected = 0;
				if (mGame.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Harder)
					selected = 1;
				if (mGame.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Expert)
					selected = 2;

				builder.setSingleChoiceItems(levels, selected,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							dialog.dismiss();// Close dialog

							if (item == 0)
								mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
							if (item == 1)
								mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
							if (item == 2)
								mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);

							// Display the selected difficultylevel
								Toast.makeText(getApplicationContext(), levels[item],
										Toast.LENGTH_SHORT).show();

										}

					});
				dialog = builder.create();

				break;

			case DIALOG_ABOUT_ID:
				Context context = getApplicationContext();
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.about_dialog, null);
				builder.setView(layout);
				builder.setPositiveButton("OK", null);
				dialog = builder.create();

				break;

			case DIALOG_QUIT_ID:
				// Create the quit conformation dialog

				builder.setMessage(R.string.quit_question)
						.setCancelable(false)
						.setNegativeButton(R.string.quit_yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id){
								TicTacToeActivity.this.finish();
							}
						})
						.setPositiveButton(R.string.quit_no, null);
				dialog = builder.create();

				break;
		}

		Log.d("TicTacToeActivity", "Completed onCreateDialog Method");
		return dialog;
	}

    // Handles menu item selections
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.new_game:
				startNewGame();
				return true;
			case R.id.ai_difficulty:
				showDialog(DIALOG_DIFFICULTY_ID);
				return true;
			case R.id.about_dialog:
				showDialog(DIALOG_ABOUT_ID);
				return true;
			case R.id.quit:
				showDialog(DIALOG_QUIT_ID);
				return true;
		}
		Log.d("TicTacToeActivity", "Completed onOptionItemSelected method");
		return false;
    }


    // Set up the game board.
    private void startNewGame() {

    	mGame.clearBoard();


    	// Reset all buttons
    	for (int i = 0; i < mBoardButtons.length; i++) {
			mBoardButtons[i].setText("");
    		mBoardButtons[i].setEnabled(true);
    		mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
    	}

		//Alternate the starting turns
		if(mTurn == 'X')
		{
			mTurn = 'O';
			mInfoTextView.setText(R.string.first_computer);
			int location = mGame.getComputerMove();
			mGame.setMove(mTurn,location);
			setMove(mTurn, location);
		}
		else {
			mTurn = 'X';
			mInfoTextView.setText(R.string.first_human);
		}
		Log.d("TicTacToeActivity", "Completed startNewGame method");
    }

    private void setMove(char mTurn, int location) {

    	mGame.setMove(mTurn, location);
    	mBoardButtons[location].setEnabled(false);
    	mBoardButtons[location].setText(String.valueOf(mTurn));
    	if (mTurn == TicTacToeGame.HUMAN_PLAYER)
    		mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
    	else
        	mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
		Log.d("TicTacToeActivity", "Completed setMove Method");
    }

    // Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
             this.location = location;
			 Log.d("TicTacToeActivity", "Completed ButtonClickListener");
        }

        public void onClick(View view) {
        	if (mBoardButtons[location].isEnabled()) {
        		setMove(TicTacToeGame.HUMAN_PLAYER, location);

            	// If no winner yet, let the computer make a move
            	int winner = mGame.checkForWinner();
            	if (winner == 0) {
            		mInfoTextView.setText("It's Android's turn");
            		int move = mGame.getComputerMove();
            		setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            		winner = mGame.checkForWinner();
            	}

				//Disable Buttons on winner
				if(winner != 0)
				{
					for (Button mBoardButton : mBoardButtons) {
						mBoardButton.setEnabled(false);
					}
				}

            	if (winner == 0)
					mInfoTextView.setText(R.string.turn_human);

				else if (winner == 1) {
					mInfoTextView.setText(R.string.result_tie);
					mTies += 1;
					mtieTextView.setText(getString(R.string.ties) + String.valueOf(mTies));
				}
				else if (winner == 2){
					mInfoTextView.setText(R.string.result_human_wins);
				    mHumanWins += 1;
					mhwinTextView.setText(getString(R.string.hWins) + String.valueOf(mHumanWins));
		        }
				else {
					mInfoTextView.setText(R.string.result_computer_wins);
					mComputerWins += 1;
					mawinTextView.setText(getString(R.string.aWins) + String.valueOf(mComputerWins));
				}
				}
			Log.d("TicTacToeActivity", "Completed onClick Method");
			}
		}
	}