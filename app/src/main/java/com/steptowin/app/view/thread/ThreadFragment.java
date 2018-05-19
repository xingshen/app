package com.steptowin.app.view.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseFragment;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:线程计数器,测试线程
 * author：zg
 * date:16/6/18
 * time:下午3:36
 */
public class ThreadFragment extends BaseFragment {

    static final Integer STATE_IDLE = 1;
    static final Integer STATE_PLAYING = 2;
    static final Integer STATE_PAUSING = 3;

    static final int COMMAND_PAUSE = 1;
    static final int COMMAND_STOP = 2;
    static final int COMMAND_UPDATE_TIME = 3;

    @Bind(R.id.tv_hint)
    TextView tvHint;
    @Bind(R.id.btn_start)
    Button btnStart;
    @Bind(R.id.btn_stop)
    Button btnStop;
    @Bind(R.id.btn_pause)
    Button btnPause;

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private AtomicInteger countState = new AtomicInteger(STATE_IDLE);
    private AtomicInteger count = new AtomicInteger();
    private Handler uiHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case COMMAND_UPDATE_TIME:
                    tvHint.setText("" + count.incrementAndGet());
                    break;
                case COMMAND_STOP:
                    count.set(0);
                    tvHint.setText("已停止");
                    break;
                case COMMAND_PAUSE:
                    tvHint.setText("已暂停");
                    break;
            }

        }
    };

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_thread;
    }

    Runnable countRunnable = new Runnable() {
        @Override
        public void run() {

            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (countState.get() == STATE_PAUSING) {
                    uiHandler.sendEmptyMessage(COMMAND_PAUSE);
                    waitForStart();
                } else if (countState.get() == STATE_IDLE) {
                    uiHandler.sendEmptyMessage(COMMAND_STOP);
                    return;
                }
                uiHandler.sendEmptyMessage(COMMAND_UPDATE_TIME);
            }
        }
    };
    Thread countThread = null;

    private void waitForStart() {
        lock.lock();
        try {
            condition.await();
//            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void notifyCount() {
        lock.lock();
        countState.set(STATE_PLAYING);
//        notify();
        condition.signal();
        lock.unlock();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_start, R.id.btn_stop, R.id.btn_pause})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (countState.get() == STATE_IDLE) {
                    countState.set(STATE_PLAYING);
                    countThread = new Thread(countRunnable);
                    countThread.start();
                } else if (countState.get() == STATE_PAUSING) {
                    notifyCount();
                }
                break;
            case R.id.btn_stop:
                countState.set(STATE_IDLE);
                break;
            case R.id.btn_pause:
                countState.set(STATE_PAUSING);
                break;
        }

    }
}
