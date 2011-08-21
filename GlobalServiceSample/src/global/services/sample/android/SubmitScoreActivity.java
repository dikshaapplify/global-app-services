package global.services.sample.android;

import global.services.lib.android.Highscore;
import global.services.lib.android.HighscoreFactory;
import global.services.sample.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class SubmitScoreActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_score);

		Button button = (Button) findViewById(R.id.btnSubmit);
		// Register the onClick listener with the implementation above
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Highscore gScore = new Highscore(getResources().getString(R.string.userid), Long
						.parseLong(getResources().getString(
								R.string.appid)));

				TextView txtPlayer = (TextView) findViewById(R.id.editPlayer);
				gScore.setPlayer(txtPlayer.getText().toString());

				TextView txtLevel = (TextView) findViewById(R.id.editLevel);
				gScore.setPlayer(txtLevel.getText().toString());

				TextView txtScore = (TextView) findViewById(R.id.editScore);
				gScore.setHighScore(Integer.parseInt(txtScore.getText()
						.toString()));

				TextView txtDuring = (TextView) findViewById(R.id.editDuring);
				gScore
						.setDuring(Long.parseLong(txtDuring.getText()
								.toString()));

				DatePicker dpkDate = (DatePicker) findViewById(R.id.editDate);
				gScore.setDate(dpkDate.getDrawingTime());

				TextView txtLocation = (TextView) findViewById(R.id.editLocation);
				gScore.setLocation(txtLocation.getText().toString());

				TextView txtComment = (TextView) findViewById(R.id.editComment);
				gScore.setComment(txtComment.getText().toString());

				HighscoreFactory scoreFactory = new HighscoreFactory(
						getResources().getString(R.string.userid), Long
								.parseLong(getResources().getString(
										R.string.appid)));
				scoreFactory.SubmitScore(gScore);
			}

		}

		);

	}

}
