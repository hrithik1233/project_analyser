import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpotlightOverlay extends View {

    private List<Spotlight> spotlights;
    private Paint flamePaint;
    private Paint circlePaint;
    private CountDownTimer timer;

    public SpotlightOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        spotlights = new ArrayList<>();

        flamePaint = new Paint();
        flamePaint.setColor(Color.YELLOW); // Adjust the flame color as needed
        flamePaint.setStyle(Paint.Style.FILL);

        circlePaint = new Paint();
        circlePaint.setColor(Color.RED); // Adjust the circle color as needed
        circlePaint.setStyle(Paint.Style.FILL);

        // Dummy timer for illustration, you may need to manage multiple timers based on spotlights
        timer = new CountDownTimer(Long.MAX_VALUE, 16) {
            @Override
            public void onTick(long millisUntilFinished) {
                invalidate();
            }

            @Override
            public void onFinish() {
                // Not used in this example
            }
        };
        timer.start();
    }

    public void addSpotlight(float x, float y, long durationMillis) {
        spotlights.add(new Spotlight(x, y, durationMillis));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Iterator<Spotlight> iterator = spotlights.iterator();
        while (iterator.hasNext()) {
            Spotlight spotlight = iterator.next();

            // Draw flame-like effect at the top
            Path flamePath = createFlamePath(spotlight.getX(), spotlight.getY());
            canvas.drawPath(flamePath, flamePaint);

            // Draw rotating colored circle at the bottom
            float centerX = spotlight.getX();
            float centerY = spotlight.getY() + 100; // Adjust the vertical position as needed

            canvas.save();
            canvas.rotate(spotlight.getCircleRotation(), centerX, centerY);
            canvas.drawCircle(centerX, centerY, 50, circlePaint); // Assuming circle radius is 50
            canvas.restore();

            spotlight.decrementDuration();

            if (spotlight.getDuration() <= 0) {
                iterator.remove(); // Remove expired spotlights
            }
        }
    }

    private Path createFlamePath(float x, float y) {
        Path path = new Path();
        path.moveTo(x - 50, y); // Adjust coordinates as needed
        path.lineTo(x, y - 100);
        path.lineTo(x + 50, y);
        path.close();
        return path;
    }

    private static class Spotlight {
        private float x;
        private float y;
        private long duration;
        private float circleRotation;

        public Spotlight(float x, float y, long duration) {
            this.x = x;
            this.y = y;
            this.duration = duration;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public long getDuration() {
            return duration;
        }

        public float getCircleRotation() {
            return circleRotation;
        }

        public void decrementDuration() {
            duration -= 16; // Subtract elapsed time, assuming 60 FPS
            circleRotation += 2; // Adjust the rotation speed as needed
        }
    }
}
