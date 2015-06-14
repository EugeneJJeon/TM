package xyz.dcafe.touchingmessage.cameras;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.dcafe.touchingmessage.R;
import xyz.dcafe.touchingmessage.User;
import xyz.dcafe.touchingmessage.services.SendVideo;
import xyz.dcafe.touchingmessage.services.Upload;
import xyz.dcafe.touchingmessage.widgets.PaperButton;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class Record extends Activity implements SurfaceHolder.Callback {
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        // on Pause 상태에서 카메라 ,레코더 객체를 정리한다
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
        if (recorder != null){
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        super.onPause();
    }
    // 전면카메라로 설정
    private int mCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
    // Video View 객체
    private VideoView mVideoView=null;
    // 카메라 객체
    private Camera mCamera;
    // 레코더 객체 생성
    private MediaRecorder recorder = null;
    // 레코딩 플래그 변수
    private boolean isRecord = false;
    // 아웃풋 파일 경로
    private static String OUTPUT_FOLDER;
    private static String OUTPUT_FILE; // = "/sdcard/videooutput.mp4";
    // 녹화 시간 - 10초
    private static final int RECORDING_TIME = 10000;

    // 전송 알림창
    AlertDialog dialog;

    // 카메라 프리뷰를 설정한다
    private void setCameraPreview(SurfaceHolder holder){
        try {
            // 카메라 객체를 만든다
            mCamera = Camera.open(mCameraFacing);
            // 카메라 객체의 파라메터를 얻고 로테이션을 90도 꺽는다,옵Q의 경우 90회전을 필요로 한다 ,옵Q는 지원 안하는듯....
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setRotation(90);
            mCamera.setParameters(parameters);
            // 프리뷰 디스플레이를 담당한 서피스 홀더를 설정한다
            mCamera.setPreviewDisplay(holder);
            // 프리뷰 콜백을 설정한다 - 프레임 설정이 가능하다,
  /* mCamera.setPreviewCallback(new PreviewCallback() {
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
     // TODO Auto-generated method stub
    }
   });
   */
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // 서피스가 만들어졌을 때의 대응 루틴
        setCameraPreview(holder);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        // 서피스 변경되었을 때의 대응 루틴
        if (mCamera !=null){
            Camera.Parameters parameters = mCamera.getParameters();
            // 프리뷰 사이즈 값 재조정
            parameters.setPreviewSize(width,height);
            mCamera.setParameters(parameters);

            // 화면 정방향
            mCamera.setDisplayOrientation(90);

            // 프리뷰 다시 시작
            mCamera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

        //서피스 소멸시의 대응 루틴

        // 프리뷰를 멈춘다
        if (mCamera != null){
            mCamera.stopPreview();
            // 카메라 객체 초기화
            mCamera = null;
        }
    }
    // 프리뷰(카메라가 찍고 있는 화상을 보여주는 화면) 설정 함수
    private void setPreview()
    {
        // 1) 레이아웃의 videoView 를 멤버 변수에 매핑한다
        mVideoView = (VideoView) findViewById(R.id.record_view);
        // 2) surface holder 변수를 만들고 videoView로부터 인스턴스를 얻어온다
        final SurfaceHolder holder = mVideoView.getHolder();
        // 3)표면의 변화를 통지받을 콜백 객체를 등록한다
        holder.addCallback(this);
        // 4)Surface view의 유형을 설정한다, 아래 타입은 버퍼가 없이도 화면을 표시할 때 사용된다.카메라 프리뷰는 별도의 버퍼가 필요없다
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    private void setButtons()
    {
        if (isRecord) {
            // Rec Stop 버튼 콜백 설정
            PaperButton recStop = (PaperButton)findViewById(R.id.button_record);
            recStop.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    isRecord = false;
                    ((PaperButton)findViewById(R.id.button_record)).setText(getText(R.string.record_start).toString());

                    // TODO Auto-generated method stub
                    // 레코더 객체가 존재할 경우 이를 스톱시킨다
                    if ( recorder !=null ){
                        Log.e("CAM TEST","CAMERA STOP!!!!!");
                        //recorder.stop();
                        recorder.release();
                        recorder = null;

                        dialog.show();

                        // 녹화 버튼 재설정
                        setButtons();
                    }
                    // 프리뷰가 없을 경우 다시 가동 시킨다
                    if ( mCamera == null ) {
                        Log.e("CAM TEST","Preview Restart!!!!!");
                        // 프리뷰 다시 설정
                        setCameraPreview(mVideoView.getHolder());
                        // 프리뷰 재시작
                        mCamera.startPreview();
                    }
                }
            });
        }
        else {
            // Rec Start 버튼 콜백 설정
            PaperButton recStart = (PaperButton)findViewById(R.id.button_record);
            recStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isRecord = true;
                    ((PaperButton)findViewById(R.id.button_record)).setText(getText(R.string.record_stop).toString());

                    // TODO Auto-generated method stub
                    Log.e("CAM TEST","REC START!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                    if (mVideoView.getHolder() == null)
                    {
                        Log.e("CAM TEST","View Err!!!!!!!!!!!!!!!");
                    }
                    beginRecording(mVideoView.getHolder());
                }
            });
        }
    }

    private void beginRecording(SurfaceHolder holder) {
        // 레코더 객체 초기화
        Log.e("CAM TEST","#1 Begin REC!!!");
        if(recorder!= null)
        {
            //recorder.stop();
            recorder.release();
        }
        String state = android.os.Environment.getExternalStorageState();
        if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
            Log.e("CAM TEST","I/O Exception");
        }
        // 파일 생성/초기화
        Log.e("CAM TEST","#2 Create File!!!");
        // 저장 폴더 지정 및 폴더 생성
        File fileFolderPath = new File(OUTPUT_FOLDER);
        if (!fileFolderPath.exists()) fileFolderPath.mkdir();
        // 파일 저장
        File outFile = new File(OUTPUT_FILE);
        if (outFile.exists())
        {
            outFile.delete();
        }
//        Log.e("CAM TEST","#3 Release Camera!!!");
//        if (mCamera != null){
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera=null;
//            Log.e("CAM TEST","#3 Release Camera  _---> OK!!!");
//        }

        try {
            recorder = new MediaRecorder();
            // 중요!
            mCamera.unlock();
            recorder.setCamera(mCamera);
            // Video/Audio 소스 설정
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setOutputFormat(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).fileFormat);
            //recorder.setVideoSize(800, 480);
            recorder.setVideoSize(320,240);
            //recorder.setVideoFrameRate(25);
            recorder.setVideoFrameRate(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).videoFrameRate);
            // Video/Audio 인코더 설정
            //recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            recorder.setVideoEncoder(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).videoCodec);
            //recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioEncoder(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).audioCodec);
            recorder.setAudioSamplingRate(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).audioSampleRate);
            recorder.setAudioEncodingBitRate(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).audioBitRate);
            // 녹화 시간 한계 , 10초
            recorder.setMaxDuration(RECORDING_TIME);
            // 녹화 화면을 저장할때, 회전!
            recorder.setOrientationHint(270);
            // 프리뷰를 보여줄 서피스 설정
            recorder.setPreviewDisplay(holder.getSurface());
            // 녹화할 대상 파일 설정
            recorder.setOutputFile(OUTPUT_FILE);
            recorder.prepare();
            recorder.start();

            // 녹화 버튼 재설정
            setButtons();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("CAM TEST", "Error Occur???!!!");
            e.printStackTrace();
        }
    }


    private User mFriend;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Bundle bundle = getIntent().getExtras();
        mFriend = bundle.getParcelable(User.TAG);

        createDialog();

        OUTPUT_FILE = getFileName();

        // 세로화면 고정으로 처리한다
        //SCREEN_ORIENTATION_LANDSCAPE - 가로화면 고정
        //SCREEN_ORIENTATION_PORTRAIT - 세로화면 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 프리뷰를 설정한다
        setPreview();

        // 버튼을 설정한다
        setButtons();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("전송 확인")
                .setMessage(mFriend.nickName + "님에게 영상을 전송하시겠습니까?")
                .setCancelable(true)    // 뒤로 가기 버튼 : 취소 가능
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new SendVideo(getApplicationContext()).execute(mFriend.gcmID);

                        //TODO: 해결해야 하는 문제
                        // 녹화한 영상 업로드
                        //new Upload().execute(OUTPUT_FILE);

                        finish();
                    }
                })
                .setNegativeButton("다시찍기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "다시 찍기", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        dialog = builder.create();
    }

    private String getFileName() {
        String folderName = getText(R.string.app_name).toString();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        OUTPUT_FOLDER = path + File.separator + folderName;

        String date = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        date = formatter.format(now);

        return OUTPUT_FOLDER + File.separator + date + ".mp4";
    }
}
