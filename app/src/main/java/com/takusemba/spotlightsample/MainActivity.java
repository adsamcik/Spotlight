package com.takusemba.spotlightsample;

import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.takusemba.spotlight.CustomTarget;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.Target;
import com.takusemba.spotlight.shapes.Circle;
import com.takusemba.spotlight.shapes.RoundedRectangle;

import kotlin.Unit;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.simple_target).setOnClickListener(v -> {

			View one = findViewById(R.id.one);
			int[] oneLocation = new int[2];
			one.getLocationInWindow(oneLocation);
			float oneX = oneLocation[0] + one.getWidth() / 2f;
			float oneY = oneLocation[1] + one.getHeight() / 2f;
			// make an target
			SimpleTarget firstTarget = new SimpleTarget.Builder(MainActivity.this).setPoint(oneX, oneY)
					.setTitle("first title")
					.setDescription("first description")
					.addButtonData(new SimpleTarget.ButtonData("skip", (view, spotlight) -> {
						spotlight.finishSpotlight();
						return Unit.INSTANCE;
					}))
					.addButtonData(new SimpleTarget.ButtonData("continue", (view, spotlight) -> {
						spotlight.next();
						return Unit.INSTANCE;
					}))
					.build();

			View two = findViewById(R.id.two);
			int[] twoLocation = new int[2];
			two.getLocationInWindow(twoLocation);
			PointF point =
					new PointF(twoLocation[0] + two.getWidth() / 2f, twoLocation[1] + two.getHeight() / 2f);
			// make an target
			SimpleTarget secondTarget = new SimpleTarget.Builder(MainActivity.this).setPoint(point)
					.setTitle("second title")
					.setDescription("second description")
					.setOnSpotlightStartedListener(new OnTargetStateChangedListener() {
						@Override
						public void onStarted(@NonNull Target target) {
							Toast.makeText(MainActivity.this, "second target is started", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onEnded(@NonNull Target target) {
							Toast.makeText(MainActivity.this, "second target is ended", Toast.LENGTH_SHORT).show();
						}
					})
					.build();

			SimpleTarget thirdTarget;

			if (Build.VERSION.SDK_INT >= 21)
				thirdTarget =
						new SimpleTarget.Builder(MainActivity.this)
								.setShape(new RoundedRectangle(findViewById(R.id.three), 40f, 20f))
								.setTitle("third title")
								.setDescription("third description")
								.build();
			else
				thirdTarget =
						new SimpleTarget.Builder(MainActivity.this)
								.setShape(new Circle(findViewById(R.id.three)))
								.setTitle("third title")
								.setDescription("third description")
								.build();

			Spotlight.Companion.with(MainActivity.this)
					.setOverlayColor(ContextCompat.getColor(MainActivity.this, R.color.background))
					.setDuration(1000L)
					.setAnimation(new DecelerateInterpolator(2f))
					.setTargets(firstTarget, secondTarget, thirdTarget)
					.setClosedOnTouchedOutside(true)
					.setOnSpotlightStartedListener(() -> {
						Toast.makeText(MainActivity.this, "spotlight is started", Toast.LENGTH_SHORT).show();
						return Unit.INSTANCE;
					})
					.setOnSpotlightEndedListener(() -> {
						Toast.makeText(MainActivity.this, "spotlight is ended", Toast.LENGTH_SHORT).show();
						return Unit.INSTANCE;
					})
					.start();
		});

		findViewById(R.id.custom_target).setOnClickListener(v -> {

			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			View view = inflater.inflate(R.layout.layout_target, (ViewGroup) v.getParent(), false);

			// make an target
			final CustomTarget thirdTarget =
					new CustomTarget.Builder(MainActivity.this).setPoint(findViewById(R.id.three))
							.setView(view)
							.build();

			view.findViewById(R.id.close_target).setOnClickListener(view1 -> thirdTarget.closeTarget());

			Spotlight.Companion.with(MainActivity.this)
					.setOverlayColor(ContextCompat.getColor(MainActivity.this, R.color.background))
					.setDuration(1000L)
					.setAnimation(new DecelerateInterpolator(2f))
					.setTargets(thirdTarget)
					.setClosedOnTouchedOutside(false)
					.setOnSpotlightStartedListener(() -> {
						Toast.makeText(MainActivity.this, "spotlight is started", Toast.LENGTH_SHORT)
								.show();
						return Unit.INSTANCE;
					})
					.setOnSpotlightEndedListener(() -> {
						Toast.makeText(MainActivity.this, "spotlight is ended", Toast.LENGTH_SHORT).show();
						return Unit.INSTANCE;
					})
					.start();
		});
	}
}
