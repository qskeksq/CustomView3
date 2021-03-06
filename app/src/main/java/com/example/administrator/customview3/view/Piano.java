package com.example.administrator.customview3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.customview3.R;

/**
 * 뷰 위젯4.
 * 피아노
 */

public class Piano extends View {

    // 건반 수, 간격
    private int MAX_POINTS = 5;
    private int WHITE_KEY_NUM = 7;
    private int BLACK_KEY_NUM = 5;
    private float KEY_PADDING = 0.02F;
    private float BLACK_KEY_PADDING = 0.5f;
    private Rect[] whiteKeys = new Rect[WHITE_KEY_NUM];
    private Rect[] blackKeys = new Rect[BLACK_KEY_NUM];

    // 건반 색
    private Paint[] whitePaints = new Paint[WHITE_KEY_NUM];
    private Paint[] blackPaints = new Paint[BLACK_KEY_NUM];

    // 실제 크기
    int width, keyWidth, blackWidth, blackKeyWidth;
    int height, blackHeight;
    int padding, paddings;

    // 포인터
    private Point[] points = new Point[MAX_POINTS];
    private MotionEvent.PointerCoords pointerCoords;
    
    // 소리
    int[] whiteSounds = new int[WHITE_KEY_NUM];
    int[] blackSounds = new int[BLACK_KEY_NUM];
    SoundPool mSoundPool;

    /**
     * 생성자
     * @param context
     */
    public Piano(Context context) {
        super(context);
    }

    public Piano(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRect();
        initPaint();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSoundPool();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mSoundPool.release();
    }

    /**
     * Rect 초기화
     */
    private void initRect(){
        for (int i = 0; i < WHITE_KEY_NUM; i++) {
            whiteKeys[i] = new Rect();
        }
        for (int i = 0; i < BLACK_KEY_NUM; i++) {
            blackKeys[i] = new Rect();
        }
    }

    /**
     * Paint 초기화
     */
    private void initPaint(){
        for (int i = 0; i < WHITE_KEY_NUM; i++) {
            whitePaints[i] = new Paint();
            whitePaints[i].setColor(Color.WHITE);
        }
        for (int i = 0; i < BLACK_KEY_NUM; i++) {
            blackPaints[i] = new Paint();
            blackPaints[i].setColor(Color.BLACK);
        }
    }

    /**
     * 소리 초기화
     */
    private void setSoundPool(){
        mSoundPool = new SoundPool(0, AudioTrack.MODE_STREAM, 0); //1
        whiteSounds[0] = mSoundPool.load(getContext(), R.raw.c, 0);
        whiteSounds[1] = mSoundPool.load(getContext(), R.raw.d, 0);
        whiteSounds[2] = mSoundPool.load(getContext(), R.raw.e, 0);
        whiteSounds[3] = mSoundPool.load(getContext(), R.raw.f, 0);
        whiteSounds[4] = mSoundPool.load(getContext(), R.raw.zz, 0);
        whiteSounds[5] = mSoundPool.load(getContext(), R.raw.a, 0);
        whiteSounds[6] = mSoundPool.load(getContext(), R.raw.b, 0);
        blackSounds[0] = mSoundPool.load(getContext(), R.raw.g, 0);
        blackSounds[1] = mSoundPool.load(getContext(), R.raw.hh, 0);
        blackSounds[2] = mSoundPool.load(getContext(), R.raw.ii, 0);
        blackSounds[3] = mSoundPool.load(getContext(), R.raw.jj, 0);
        blackSounds[4] = mSoundPool.load(getContext(), R.raw.kk, 0);
    }

    /**
     * 각 건반 크기 측정
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 전체 길이
        width = getDefaultSize(getSuggestedMinimumHeight(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        // 패팅 가로
        padding = ((int) (width*KEY_PADDING))/(WHITE_KEY_NUM+1);
        paddings = (int) (width*KEY_PADDING);
        
        // 흰 건반 가로
        keyWidth = (width-paddings)/(WHITE_KEY_NUM);

        // 검정건반
        blackWidth = (int) ((width-paddings)*BLACK_KEY_PADDING);
        blackKeyWidth = blackWidth/BLACK_KEY_NUM;
        blackHeight = (int) (height*BLACK_KEY_PADDING);
        
        setMeasuredDimension(width, height);
    }

    /**
     * 건반 위치 지정
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (int column = 0; column < WHITE_KEY_NUM; column++) {
            Rect rect = whiteKeys[column];
            rect.left = padding*(column+1) + keyWidth*column;
            rect.top = getPaddingTop();
            rect.bottom = height-getPaddingBottom();
            rect.right = padding*(column+1) + keyWidth*(column+1);
        }
        int WHITE_TO_BLACK = keyWidth-blackKeyWidth/2-padding/2;

        Rect rect = blackKeys[0];
        rect.left = padding+WHITE_TO_BLACK+keyWidth*0;
        rect.top = getPaddingTop();
        rect.bottom = getPaddingTop()+blackHeight;
        rect.right = rect.left+blackKeyWidth;

        rect = blackKeys[1];
        rect.left = padding*2+WHITE_TO_BLACK+keyWidth*1;
        rect.top = getPaddingTop();
        rect.bottom = getPaddingTop()+blackHeight;
        rect.right = rect.left+blackKeyWidth;

        rect = blackKeys[2];
        rect.left = padding*4+WHITE_TO_BLACK+keyWidth*3;
        rect.top = getPaddingTop();
        rect.bottom = getPaddingTop()+blackHeight;
        rect.right = rect.left+blackKeyWidth;

        rect = blackKeys[3];
        rect.left = padding*5+WHITE_TO_BLACK+keyWidth*4;
        rect.top = getPaddingTop();
        rect.bottom = getPaddingTop()+blackHeight;
        rect.right = rect.left+blackKeyWidth;

        rect = blackKeys[4];
        rect.left = padding*6+WHITE_TO_BLACK+keyWidth*5;
        rect.top = getPaddingTop();
        rect.bottom = getPaddingTop()+blackHeight;
        rect.right = rect.left+blackKeyWidth;
    }

    /**
     * 그려줌
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("onDraw", "호출 안 되는가");
        // 임시 구분용
        setBackgroundColor(Color.BLACK);
        for (int i = 0; i < WHITE_KEY_NUM; i++) {
            canvas.drawRect(whiteKeys[i], whitePaints[i]);
        }
        for (int i = 0; i < BLACK_KEY_NUM; i++) {
            canvas.drawRect(blackKeys[i], blackPaints[i]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionIndex = event.getActionIndex();
        int action = event.getActionMasked();
        int id = event.getPointerId(actionIndex);
        if(id < MAX_POINTS) {
            switch (action) {
                // 한 개의 터치 이벤트(ACTION_POINTER_DOWN 은 호출되지 않음)
                case MotionEvent.ACTION_DOWN:
                    // 두 개 이상의 터치 이벤트(ACTION_DOWN 은 호출되지 않음)
                case MotionEvent.ACTION_POINTER_DOWN:
//            case MotionEvent.ACTION_MOVE:
                    Log.e("id", id + "");
                    // 아하 왜 하나만 계속 눌리는지, 하나 누른 상태로 다른 것을 누르면 첫번쨰 것이 점유하는지 알았다.
                    // 첫 손가락이 계속 점유한 상태에서 getX, getY 를 했기 때문에 같은 좌표만 계속 들어가고, 첫번째 좌표만
                    // 계속 그려지고 있었던 것이다. 따라서 getActionIndex 를 통해 id 가 아닌 현재 눌린 좌표 값을 포인트로
                    // 저장해주면 정상적으로 작동한다.
                    points[id] = new Point((int) event.getX(actionIndex), (int) event.getY(actionIndex));
                    invalidateKey(points[id], true);
                    Log.e("down아이디", id + "");
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    invalidateKey(points[id], false);
                    points[id] = null;
                    Log.e("up아이디", id + "");
                    break;
            }
        }
        // 이것을 false 로 하면 다운은 되는데, 업이 안 된다. 즉, 연속으로 호출이 안 된다.
        return true;
    }

    /**
     * 키 갱신
     * @param point
     * @param isDown
     */
    private void invalidateKey(Point point, boolean isDown){
        Log.e("invalidateKey", "호출 확인1");
        if(isDown) {
            // 먼저 흰키 영역인지 확인하고
            for (int i = 0; i < WHITE_KEY_NUM; i++) {
                Rect rect = whiteKeys[i];
                if (rect.contains(point.x, point.y)) {
                    // 흰키 영역 안의 검정 영역이면
                    for (int j = 0; j < BLACK_KEY_NUM; j++) {
                        Rect blackRect = blackKeys[j];
                        if (blackRect.contains(point.x, point.y)) {
                            blackPaints[j].setColor(Color.CYAN);
                            mSoundPool.play(blackSounds[j],1f,1f,0,0,1);
                            invalidate();
                            return;
                        }
                    }
                    Log.e("invalidateKey", "호출 확인2");
                    whitePaints[i].setColor(Color.CYAN);
                    mSoundPool.play(whiteSounds[i],1f,1f,0,0,1);
                }
            }
        } else {
            for (int i = 0; i < WHITE_KEY_NUM; i++) {
                whitePaints[i].setColor(Color.WHITE);
            }
            for (int i = 0; i < BLACK_KEY_NUM; i++) {
                blackPaints[i].setColor(Color.BLACK);
            }
        }
        invalidate();
    }
}
